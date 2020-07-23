import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;
import java.util.ArrayList;

/**
 * A class representing projectile on target slicer
 * parent class of TankProjectile and SuperTankProjectile
 */
public class Projectile {
    private Image proj_image;
    private String type;
    private static final int SPEED = 10;
    private Point tank_loc;
    private Slicer target;
    private Vector2 proj_direction;
    private int radius;
    private int damage;
    private boolean hit;
    private Rectangle rectangle;
    public static final String TANK_PATH = "res/images/tank_projectile.png";
    public static final String SUPERTANK_PATH = "res/images/supertank_projectile.png";
    private static final Vector2 DEFAULT_DIRECTION = new Vector2(1.0, 0.0);

    /**
     * Constructor of  projectile, set all variables to either 0 or null, assign real values in subclasses
     * @param type type of projectile
     * @param proj_loc Initial location of projectile
     * @param target targeted slicer
     */
    public Projectile(String type, Point proj_loc, Slicer target) {
        this.type = type;
        this.tank_loc = proj_loc;
        this.target = null;
        this.proj_direction = DEFAULT_DIRECTION;
        this.radius = 0;
        this.damage = 0;
        this.target = target;
        this.hit = false;
        if (type.equals("tankprojectile")){
            this.proj_image = new Image(TANK_PATH);
        }else if (type.equals("supertankprojectile")){
            this.proj_image = new Image(SUPERTANK_PATH);
        }
    }

    /**
     * This function is used to guide the projectile fly to the target
     * deduct lives and spawn new slicer if necessary
     */
    public void update(){
        tank_loc = tank_loc.asVector().add(proj_direction.mul(ShadowDefend.getTimescale() * SPEED)).asPoint();
        proj_direction = target.getCurrentPoint().asVector().sub(tank_loc.asVector()).normalised();
        rectangle = proj_image.getBoundingBoxAt(tank_loc);

        if (target.onMap()){
            //if the projectile hit the target.
            if (rectangle.intersects(target.getBoundingBox())){
                // The slicer has not been completely destroyed
                if (target.getHealth() > 1 && !target.isEliminated()){
                    target.setHealth(target.getHealth() - damage);
                }
                // Slicer is destroyed and get reward or spawn new slicer.
                else{
                    target.setEliminated(true);
                    // spawn child slicers
                    spawnChildren(target, ShadowDefend.getSlicers());
                    // add reward
                    ShadowDefend.addReward(target);
                }
                hit = true;
            }

            //render projectile if target slicer is not destroyed by other tank and is on map
            if (!hit && target.onMap()){
                proj_image.draw(tank_loc.x, tank_loc.y);
            }
        }
    }

    /**
     * Spawn children slicers when one slicer is destroyed
     * @param target target slicer which is destroyed
     * @param slicers ArrayList of slicers to destroy
     */
    public static void spawnChildren(Slicer target, ArrayList<Slicer> slicers){
        if (target instanceof SuperSlicer){
            slicers.add(new RegularSlicer(target));
            slicers.add(new RegularSlicer(target));
        } else if (target instanceof MegaSlicer){
            slicers.add(new SuperSlicer(target));
            slicers.add(new SuperSlicer(target));
        } else if (target instanceof ApexSlicer){
            for (int count = 0; count < 4; count++){
                slicers.add(new MegaSlicer(target));
            }
        }
    }

    /***
     * A setter for updating target slicer in Tower Class
     * @param target targeted slicer
     */
    public void setTarget(Slicer target) {
        this.target = target;
    }

    //Setters for assigning real values in case of specific type of projectile
    public void setDamage(int damage) {
        this.damage = damage;
    }
    public void setRadius(int radius){
        this.radius = radius;
    }
    public void setTowerLocation(Point location){
        this.tank_loc = location;
    }
}
