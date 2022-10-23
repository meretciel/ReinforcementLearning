package reinflearning;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class Policy<STATE, ACTION> implements IPolicy<STATE, ACTION> {

    private final Map<STATE, ACTION> stateActionMap = new HashMap<>();

    @Override
    @Nullable
    public ACTION selectAction(final STATE state) {
        return this.stateActionMap.get(state);
    }


    public void setAction(final STATE state, final ACTION action) {
        this.stateActionMap.put(state, action);
    }
}
