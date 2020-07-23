/**
 * A type of Event which does not spawn any slicers
 */
public class DelayEvent extends Event {
    private int delayTime;

    /**
     * Constructor
     * @param waveNumber which wave the event belongs to
     * @param delayTime time gap to process two SpawnEvents
     */
    public DelayEvent(int waveNumber, int delayTime) {
        super(waveNumber);
        this.delayTime = delayTime;
    }

    /**
     * Start a delay event.
     * Set startTime at current timer and add delayed gap to endtime
     */
    public void start(){
        int duration = delayTime * 60 / 1000;
        int start = ShadowDefend.getTimer();
        setStartTime(start);
        setEndTime(start + duration);
        setEventStarted(true);
    }
}
