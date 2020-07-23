import bagel.util.Point;
/**
 * A class represents tank
 * A child class of Tower, with real values for variables
 */
public class Tank extends Tower{
    public Tank(Point towerLocation) {
        super("tank", towerLocation);
        this.setCoolDown(1000);
        this.setRange(100);
    }
}
