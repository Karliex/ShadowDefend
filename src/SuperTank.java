import bagel.util.Point;

/**
 * A class represents super tank
 * A child class of Tower, with real values for variables
 */
public class SuperTank extends Tower{
    public SuperTank(Point towerLocation) {
        super("supertank", towerLocation);
        this.setImage(BuyPanel.SUPER_TANK_IMAGE);
        this.setRange(150);
        this.setCoolDown(500);
    }

}
