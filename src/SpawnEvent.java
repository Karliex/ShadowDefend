/**
 * A type of event which spawn slicers
 */
public class SpawnEvent extends Event {
    private String spawnType;
    private int spawnNum;
    private int spawnCount;
    private int spawnDelay;

    /**
     * Constructor
     * @param waveNumber which wave this event belongs to
     * @param spawnType type of slicer to spawn
     * @param spawnNum number of slicers to spawn
     * @param spawnDelay time gap between spawned slicers
     */
    public SpawnEvent(int waveNumber, String spawnType, int spawnNum, int spawnDelay) {
        super(waveNumber);
        this.spawnType = spawnType;
        this.spawnNum = spawnNum;
        this.spawnDelay = spawnDelay;
        spawnCount = 0;
    }

    /**
     * start a event by setting its startTime as current timer and endTime as calculated result
     */
    public void start(){
        int duration =  (spawnNum - 1) * (spawnDelay * 60 / 1000);
        int start = ShadowDefend.getTimer();
        setStartTime(start);
        setEndTime(start + duration);
        setEventStarted(true);
    }

    //Getters and Setters for accessing and modifying variables in other classes
    public int getSpawnDelay() { return spawnDelay; }
    public int getSpawnCount() { return spawnCount; }
    public void setSpawnCount(int spawnCount) { this.spawnCount = spawnCount; }
    public int getSpawnNum() { return spawnNum;}
    public String getSpawnType() { return spawnType; }
}
