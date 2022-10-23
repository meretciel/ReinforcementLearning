package reinflearning;

import lombok.Builder;
import lombok.Getter;

/**
 * Class that represents the (agent) state transition record.
 */
@Builder
@Getter
public class StateTransitionRecord<STATE> {
    /**
     * The from state.
     */
    private final STATE fromState;

    /**
     * The to state.
     */
    private final STATE toState;


    /**
     * The probability of the transition.
     */
    private final double probability;


    /**
     * The reward triggered by the transition.
     *
     * Note that this is kind of cheating because the state in this class is supposed to be the state of the agent.
     */
    private final double reward;

}
