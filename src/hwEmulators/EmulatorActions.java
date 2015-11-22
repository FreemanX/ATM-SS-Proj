package hwEmulators;

// TODO: Auto-generated Javadoc
/**
 * Interface for all hardware component.
 * Created by ting on 2015/11/19.
 */
public interface EmulatorActions {

    /**
     * Shutdown.
     */
    public void shutdown();

    /**
     * Restart.
     */
    public void restart();

    /**
     * Fatal halt.
     */
    public void fatalHalt();
}
