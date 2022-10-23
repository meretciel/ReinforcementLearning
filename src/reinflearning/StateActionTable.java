package reinflearning;

import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

public class StateActionTable<S, A> {
    private final Map<Pair<S, A>, Double> data = new HashMap<>();

    public double getValue(final S s, final A a) {
        return this.data.getOrDefault(Pair.of(s, a), 0.0);
    }

    public void setValue(final S s, final A a, final double v) {
        this.data.put(Pair.of(s, a), v);
    }
}
