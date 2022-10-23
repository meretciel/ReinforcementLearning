package reinflearning;

import java.util.List;

/**
 * Interface for the action value function (Q-function).
 *
 * @param <STATE> The state of the agent.
 * @param <ACTION> The action of the agent.
 */
public interface IActionValueFunction<STATE, ACTION> {
    /**
     * Gets the value of taking the specific action given the state.
     *
     * @param s The state of the agent.
     * @param a The action to take.
     * @return The value of the action given the state.
     */
    double getValue(STATE s, ACTION a);

    /**
     * Sets the value.
     *
     * @param s The state.
     * @param a The action.
     * @param v The value.
     */
    void setValue(STATE s, ACTION a, double v);

    /**
     * Resets the value for all (state, action) pair to zero.
     */
    void reset();

    /**
     * @return All states.
     */
    List<STATE> getStates();

    /**
     * @return All actions.
     */
    List<ACTION> getActions();

}
