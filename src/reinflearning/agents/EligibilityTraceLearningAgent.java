package reinflearning.agents;

import lombok.extern.slf4j.Slf4j;
import reinflearning.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
public abstract class EligibilityTraceLearningAgent<STATE, ACTION, E extends IEnvironment<SNAPSHOT>, SNAPSHOT> {
    /**
     * Instance that represents the environment.
     */
    private final E environment;

    /**
     * The configuration section
     */
    private final double learningRate;

    private final double discountRate;

    private final double lambda;

    private final double explorationProbability;

    private final StateActionTable<STATE, ACTION> eligibilityTrace = new StateActionTable<>();
    private final StateActionTable<STATE, ACTION> stateActionTable = new StateActionTable<>();

    private final List<ACTION> actions = new ArrayList<>();
    private final List<STATE> states = new ArrayList<>();

    private final Random random = new Random();


    /**
     * This is the current state when the agent receives the new information from the environment and performs
     * the evaluation.
     */
    private STATE currState;

    /**
     * The selected action to perform.
     */
    private ACTION prevAction;

    protected EligibilityTraceLearningAgent(
            final E environment,
            final double learningRate,
            final double discountRate,
            final double lambda,
            final double explorationProbability) {

        this.environment = environment;
        this.learningRate = learningRate;
        this.discountRate = discountRate;
        this.lambda = lambda;
        this.explorationProbability = explorationProbability;


    }

    /**
     * Initialize the learning agent.
     */
    public void initialize() {
        this.defineActions();
        this.defineStates();
        this.initializeStateActionValueTable();
        this.setInitialState();
    }



    private ACTION selectNextAction() {
        final double r = this.random.nextDouble();
        final ACTION nextAction;
        if (r < this.explorationProbability) {
            log.debug("Agent is exploring actions.");
            final int k = this.random.nextInt(this.actions.size());
            nextAction = this.actions.get(k);
            log.debug("Next action (selected randomly): state={}, nextAction={}", this.currState, nextAction);
        } else {
            double maxValue = Double.MIN_VALUE;
            ACTION candidateAction = this.actions.get(0);

            for (var act : this.actions) {
                final double q = this.stateActionTable.getValue(this.currState, act);
                if (q > maxValue) {
                    maxValue = q;
                    candidateAction = act;
                }
            }
            nextAction = candidateAction;
            log.debug("Agent selected the best action: state={}, nextAction={}.", this.currState, nextAction);
        }

        return nextAction;
    }


    public ACTION evaluateAndGetNextAction() {
        final STATE prevState = this.currState;

        final SNAPSHOT envSnapshot = this.environment.getSnapshot();
        this.currState = this.getNewStateFromEnvSnapshot(envSnapshot);
        final double reward = this.environment.getReward();

        final ACTION nextAction = this.selectNextAction();

        final double q_prime = this.stateActionTable.getValue(this.currState, nextAction);
        final double q = this.stateActionTable.getValue(prevState, this.prevAction);

        final double delta = reward + this.discountRate * q_prime - q;

        final double eligibilityTraceValue = this.eligibilityTrace.getValue(prevState, this.prevAction);
        this.eligibilityTrace.setValue(prevState, this.prevAction, eligibilityTraceValue + 1);

        for (final var s : this.states) {
            for (final var a : this.actions) {
                final double eligibilityTraceValueLocal = this.eligibilityTrace.getValue(s, a);
                final double actionValueLocal = this.stateActionTable.getValue(s, a);

                this.stateActionTable.setValue(s, a,
                        actionValueLocal + this.learningRate * delta * eligibilityTraceValueLocal);
                this.eligibilityTrace.setValue(s, a,
                        this.discountRate * this.lambda * eligibilityTraceValueLocal);
            }
        }

        this.prevAction = nextAction;

        return nextAction;
    }

    protected abstract STATE getNewStateFromEnvSnapshot(final SNAPSHOT snapshot);
    protected abstract void initializeStateActionValueTable();
    protected abstract void defineActions();
    protected abstract void defineStates();
    protected abstract void setInitialState();
}
