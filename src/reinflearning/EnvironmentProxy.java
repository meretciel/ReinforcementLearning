package reinflearning;

import java.util.List;

public interface EnvironmentProxy<STATE, ACTION> {

    List<StateTransitionRecord<STATE>> generateScenarios(STATE s, ACTION a);

}
