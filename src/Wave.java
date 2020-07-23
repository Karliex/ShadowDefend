import java.util.ArrayList;

/**
 * A class represents an ArrayList of Events(lines) with same waveNumber
 */
public class Wave {
    private int waveNum;
    private ArrayList<Event> waveEvent;

    /**
     * Constructor
     * @param waveNumber
     */
    public Wave(int waveNumber){
        this.waveNum = waveNumber;
        this.waveEvent = new ArrayList<Event>();
    }

    public ArrayList<Event> getWaveEvent() {return waveEvent; }
}
