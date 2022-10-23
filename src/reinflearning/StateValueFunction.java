package reinflearning;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StateValueFunction<S> implements IStateValueFunction<S> {

    private final Map<S, Double> stateValues = new HashMap<>();

    @Override
    public double getValue(S s) {
        if (this.stateValues.containsKey(s)) {
            return this.stateValues.get(s);
        } else {
            throw new RuntimeException("State " + s + " does not exist.");
        }
    }

    @Override
    public void setValue(S s, double v) {
        this.stateValues.put(s, v);
    }

    @Override
    public List<S> getStates() {
        return ImmutableList.copyOf(this.stateValues.keySet());
    }

    @Override
    public  void copyFrom(IStateValueFunction<S> other) {
        for (final var state : other.getStates()) {
            this.stateValues.put(state, other.getValue(state));
        }
    }

}
