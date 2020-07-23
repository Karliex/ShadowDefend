import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.ArrayList;

/**
 * A class representing tower which target on slicers
 * A parent class of Tank and SuperTank
 */
public class Tower {
    private static final int ZERO = 0;
    private static final Image TANK_IMAGE = new Image(BuyPanel.TANK_IMAGE);
    private static final Image SUPERTANK_IMAGE = new Image(BuyPanel.SUPER_TANK_IMAGE);
    private String towerType;
    private int coolDown;
    private Point towerLocation;
    private String image;
    private Slicer target;
    private int range;
    private int lastProjTime;
    private double rotatAngle;

    /**
     * Constructor of Tower,set every variable eith zero or null, assign real values in subclasses
     * @param towerType a String which specify type of tower
     * @param towerLocation location of tower
     */
    public Tower(String towerType, Point towerLocation){
        this.towerType = towerType;
        this.towerLocation = towerLocation;
        this.range = ZERO;
        this.coolDown = ZERO;
        this.image = null;
        this.lastProjTime = ZERO; //the timer of latest projection
        this.rotatAngle = 0.0;
        this.target = null;
    }

    /**
     * Update target slicer and corresponding facing angle for a tower
     */
    public void update(){
        //Update its target slicer for every slicer
        target = null;
        targetSlicer(ShadowDefend.getSlicers());

        // update the rotation angle of the tower to face the slicer
        if (target != null){
            Vector2 targetLocation = target.getCurrentPoint().asVector();
            Vector2 location = towerLocation.asVector();
            rotatAngle = Math.PI/2 + Math.atan2(targetLocation.y - location.y, targetLocation.x - location.x);
        }
        drawTower();
        drawProjection();
    }

    /**
     * Draw updated tower corresponding to specific type
     */
    public void drawTower(){
        if (towerType.equals("tank")){
            TANK_IMAGE.draw(towerLocation.x, towerLocation.y, new DrawOptions().setRotation(rotatAngle));
        }
        if (towerType.equals("supertank")){
            SUPERTANK_IMAGE.draw(towerLocation.x, towerLocation.y, new DrawOptions().setRotation(rotatAngle));}
        }

    /**
     * This function enumerate every slicer inside the slicer array and find the first slicer within its range as target
     * @param slicers An ArrayList of slicers
     */
    public void targetSlicer(ArrayList<Slicer> slicers){
        for (Slicer slicer : slicers){
            // if the enemy is close to the tower
            if (slicer.getCurrentPoint().distanceTo(towerLocation) <= this.range){
                // if the enemy is still on map, update
                if(slicer.onMap()){
                    this.target = slicer;
                }
            }
        }
    }

    /**
     * Draw projection of the tower, if it has a target and the target is in range
     */
    private void drawProjection(){
        if (target != null && target.getCurrentPoint().distanceTo(towerLocation) <= range) {

            // Check if it is the first fire (no cool down required) || the cool down is over.
            if (lastProjTime == -1 || ShadowDefend.getTimer() - lastProjTime >= coolDown * 60 / 1000) {
                if (towerType.equals("tank")) {
                    TankProjectile tankProjectile = new TankProjectile(towerLocation, target);
                    tankProjectile.setTarget(target);
                    ShadowDefend.getProjectiles().add(tankProjectile);
                } else if (towerType.equals("supertank")) {
                    SuperTankProjectile supertankProjectile = new SuperTankProjectile(towerLocation, target);
                    supertankProjectile.setTarget(target);
                    ShadowDefend.getProjectiles().add(supertankProjectile);
                }
                lastProjTime = ShadowDefend.getTimer();
            }
        }
    }



    public int getCoolDown() {return coolDown; }

    public void setCoolDown(int coolDown) {this.coolDown = coolDown;}

    public Point getTowerLocation() {return towerLocation; }

    public void setTowerLocation(Point towerLocation) { this.towerLocation = towerLocation; }

    public String getImage() {return image;}

    public void setImage(String image) {this.image = image; }

    public Slicer getTargetEnemy() {return target;}

    public void setTargetEnemy(Slicer target) {this.target = target; }

    public int getRange() {return range; }

    public void setRange(int range) {this.range = range; }

    public int getLastProjTime() {
        return lastProjTime;
    }

    public void setLastProjTime(int lastProjTime) {
        this.lastProjTime = lastProjTime;
    }

    public double getRotatAngle() {
        return rotatAngle;
    }



}
