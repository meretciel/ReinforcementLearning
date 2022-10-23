package reinflearning;

import lombok.Builder;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

@Builder
public class ValueIteration<STATE, ACTION> {

    private final List<STATE> states;
    private final List<ACTION> actions;

    public Pair<IStateValueFunction<STATE>, IPolicy<STATE, ACTION>> run(
            final EnvironmentProxy<STATE, ACTION> envProxy,
            double discountRate,
            double convergenceThreshold) {

        // Initialize the state value function
        final IStateValueFunction stateValueFunction = new StateValueFunction<>();
        this.states.forEach(s -> stateValueFunction.setValue(s, 0));

        final IActionValueFunction actionValueFunction = new ActionValueFunction<>();

        for (;;) {
            actionValueFunction.reset();

            for (var state : this.states) {
                for (var action : this.actions) {

                    for (final StateTransitionRecord<STATE> record : envProxy.generateScenarios(state, action)) {
                        final double delta = record.getProbability()
                                * (record.getReward() + discountRate * stateValueFunction.getValue(record.getToState()));

                        actionValueFunction.setValue(state, action,
                                actionValueFunction.getValue(state, action) + delta);
                    }
                }
            }

            if (computeDifference(stateValueFunction, actionValueFunction) < convergenceThreshold) {
                break;
            }

            stateValueFunction.copyFrom(this.deriveStateValueFunction(actionValueFunction));
        }


        return Pair.of(stateValueFunction, derivePolicy(actionValueFunction));


    }

    private static <STATE, ACTION> IStateValueFunction<STATE> deriveStateValueFunction(
            final IActionValueFunction<STATE, ACTION> q) {

        final IStateValueFunction<STATE> v = new StateValueFunction<>();

        for (final var state : q.getStates()) {
            double maxValue = Double.MIN_VALUE;
            for (final var action : q.getActions()) {
                maxValue = Math.max(maxValue, q.getValue(state, action));
            }
            v.setValue(state, maxValue);
        }

        return v;
    }

    private static <STATE, ACTION> IPolicy<STATE, ACTION> derivePolicy(
            final IActionValueFunction<STATE, ACTION> q) {
        final Policy policy = new Policy();

        for (final var state : q.getStates()) {
            double maxValue = Double.MIN_VALUE;
            ACTION optimalAction = null;

            for (final var action : q.getActions()) {
                final double value = q.getValue(state, action);

                if (value > maxValue) {
                    maxValue = value;
                    optimalAction = action;
                }
            }

            policy.setAction(state, optimalAction);
        }

        return policy;


    }

    private static <STATE, ACTION> double computeDifference(
            final IStateValueFunction<STATE> v,
            final IActionValueFunction<STATE, ACTION> q) {

        final var w = deriveStateValueFunction(q);
        double maxValue = Double.MIN_VALUE;

        for (final var state : v.getStates()) {
            maxValue = Math.max(maxValue, Math.abs(v.getValue(state) - w.getValue(state)));
        }
        return maxValue;
    }
}
