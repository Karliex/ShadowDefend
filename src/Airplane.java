import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Vector2;
import bagel.util.Point;
import java.util.Random;

public class Airplane {
    private static final Image IMAGE = new Image("res/images/airsupport.png");

    //drop status related
    private int status; //1: waiting for drop, 2: dropped
    private static final int WAIT = 1;
    private static final int DROPPED = 2;

    //fly related
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    private static final int FPS = 60;
    private static final int SPEED = 5;

    //location related
    private Point currentPoint;
    private Vector2 direction;
    private double rotation;

    //drop time related
    private int dropTime;
    private int startTime;

    /**
     * Airplane constructor
     * @param dropLocation position to drop first explosive
     */
    public Airplane(Point dropLocation) {
        this.status = DROPPED;
        //if fly horizontally, fly vertically in next turn
        if (ShadowDefend.flyDirection == HORIZONTAL){
            this.currentPoint = new Point(0, dropLocation.y);
            this.direction = new Vector2(1, 0);
            //from upwards direction to rightwards direction
            this.rotation = Math.PI / 2;
            ShadowDefend.flyDirection = VERTICAL;
        }
        else{
            this.currentPoint = new Point(dropLocation.x, 0);
            this.direction = new Vector2(0, 1);
            //from upwards direction to downwards direction
            this.rotation = Math.PI;
            ShadowDefend.flyDirection = HORIZONTAL;
        }
    }

    /**
     * Updates airplane related information (location, status)
     */
    public void update(){
        //update airplane location
        currentPoint = currentPoint.asVector().add(direction.mul(ShadowDefend.getTimescale() * SPEED)).asPoint();
        //update airplane status
        if (status == DROPPED){
            Random rand = new Random();
            dropTime = rand.nextInt(3 + 1) * FPS;
            startTime = ShadowDefend.getTimer();
            status = WAIT;
        }
        else if(status == WAIT){
            if (ShadowDefend.getTimer() >= startTime + dropTime){
                ShadowDefend.getExplosives().add(new Explosive(currentPoint));
                status = DROPPED;
            }
        }
        //draw updated airplane
        IMAGE.draw(currentPoint.x, currentPoint.y, new DrawOptions().setRotation(rotation));
    }
}
