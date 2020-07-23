/**
 * A class represent one line in WAVES file
 * Parent class of DelayEvent and SpawnEvent
 */
public class Event {
    private int waveNum;
    private boolean eventStarted;
    private int startTime;
    private int endTime;

    /**
     * Constructor for Event
     * @param waveNumber Which wave the event belongs to
     */
    public Event(int waveNumber){
        this.waveNum = waveNumber;
        eventStarted = false;
    }

    /**
     * Start a wave event by setting its startTime as its current timer(frames)
     */
    public void start(){startTime = ShadowDefend.getTimer();}

    //Getters for accessing variables in other classes
    //Setters for assigning real values in case of specific type of event
    public int getEndTime() {return endTime;}
    public void setEndTime(int endTime) {this.endTime = endTime;}
    public boolean isEventStarted() {return eventStarted;}
    public void setEventStarted(boolean eventStarted) {this.eventStarted = eventStarted;}
    public int getStartTime() {return startTime; }
    public void setStartTime(int startTime) {this.startTime = startTime;}
    public int getWaveNum() {return waveNum;}

}
