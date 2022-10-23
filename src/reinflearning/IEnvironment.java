package reinflearning;

public interface IEnvironment<SNAPSHOT> {

    /**
     * @return The reward of previous action.
     */
    double getReward();

    SNAPSHOT getSnapshot();
}
