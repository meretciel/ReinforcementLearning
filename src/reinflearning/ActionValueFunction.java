package reinflearning;

import com.google.common.collect.ImmutableList;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;

public class ActionValueFunction<S, A> implements IActionValueFunction<S, A> {


    private final Map<Pair<S, A>, Double> table = new HashMap<>();

    private final Set<S> states = new HashSet<>();

    private final Set<A> actions = new HashSet<>();

    @Override
    public double getValue(S s, A a) {
        final Double v = this.table.get(Pair.of(s, a));

        if (v != null) {
            return v;
        } else {
            throw new RuntimeException("Cannot find value for state-action pair: state=" + s + ", action=" + a);
        }
    }

    @Override
    public void setValue(S s, A a, double v) {
        this.table.put(Pair.of(s, a), v);
    }


    @Override
    public void reset() {
        for (final var s : this.states) {
            for (final var a : this.actions) {
                this.setValue(s, a, 0.0);
            }
        }
    }

    @Override
    public List<S> getStates() {
        return ImmutableList.copyOf(this.states);
    }

    @Override
    public List<A> getActions() {
        return ImmutableList.copyOf(this.actions);
    }

}
