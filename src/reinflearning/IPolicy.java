package reinflearning;

/**
 * Interface for policies.
 *
 * @param <STATE> The state of the agent.
 * @param <ACTION> The action of the agent.
 */
public interface IPolicy<STATE, ACTION> {

    /**
     * Selects an action to take.
     *
     * @param s The state of the agent.
     * @return The action to take.
     */
    ACTION selectAction(STATE s);

}
