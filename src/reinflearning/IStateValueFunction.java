package reinflearning;

import java.util.List;

/**
 * Interface of the state-value function (V-function).
 *
 * @param <STATE> The state of the agent.
 */
public interface IStateValueFunction<STATE> {
    /**
     * Gets the value of the state.
     *
     * @param s The agent state.
     * @return The value of the state.
     */
    double getValue(STATE s);

    /**
     * Sets the value for the given state.
     * @param s The state.
     * @param v The value.
     */
    void setValue(STATE s, double v);


    /**
     * @return All states.
     */
    List<STATE> getStates();

    /**
     * Copy from another state value function.
     *
     * @param v The other state value function.
     */
    void copyFrom(IStateValueFunction<STATE> v);

}
