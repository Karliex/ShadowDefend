import bagel.DrawOptions;
import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;
import java.util.List;
import static java.lang.StrictMath.atan2;

/**
 * A class represents the enemy in this game
 */
public class Slicer {
    private String Image;
    private final List<Point> polyLine;
    private Point currentPoint;
    private Point destination;
    private Vector2 velocity;
    private int lineNum;
    private double angle;
    private static int zero = 0;
    private static int one = 1;
    private boolean eliminated;
    private boolean reached;
    private Rectangle boundingBox;
    private static final Vector2 DEFAULT_DIRECTION = new Vector2(1.0, 0.0);

    // four basic attributes of a slicer
    private double speed;
    private int health;
    private int reward;
    private int penalty;

    // spawn slicers at the starting point of the line
    Slicer(List<Point> polyLine){
        // initialise slicer attributes
        this.lineNum = zero;
        this.angle = zero;
        this.polyLine = polyLine;
        this.currentPoint = polyLine.get(lineNum);
        this.destination = polyLine.get(lineNum + one);
        this.eliminated = false;
        this.reached = false;
        this.velocity = destination.asVector().sub(currentPoint.asVector()).normalised();

    }

    // spawn slicers when parent slicers destroyed
    Slicer(Slicer parentSlicer){
        this.lineNum = parentSlicer.lineNum;
        this.polyLine = parentSlicer.polyLine;
        this.currentPoint = parentSlicer.currentPoint;
        this.destination = parentSlicer.destination;
        this.velocity = parentSlicer.velocity;
        this.reached = parentSlicer.reached;
        this.angle = parentSlicer.angle;
    }

    public void update(){
        int lineLength = polyLine.size();
        //check if the slicer reaches the nodes of the ployLine
        if(!reached) {

            if (currentPoint.distanceTo(destination) < speed * ShadowDefend.getTimescale()) {

                //increment on the number of the line as the slicer transfer to the next line segment
                lineNum++;

                // check if the slicer reached the last coordinate of the polyLine
                if (lineNum < lineLength) {

                    // update the endPoint to the new destination
                    destination = polyLine.get(lineNum);

                    // calculate the rotation angle of the slicer
                    this.angle = atan2(destination.y - currentPoint.y, destination.x - currentPoint.x);

                    // update the velocity as the direction vector changes
                    velocity = destination.asVector().sub(currentPoint.asVector()).normalised();
                }
                else{
                    reached = true;
                    ShadowDefend.setLives(ShadowDefend.getLives() - penalty);
                }
            }

            // keep changing the current location and current velocity */
            // to make sure the all the changes result directly */

            currentPoint = currentPoint.asVector().add(velocity.mul(speed * ShadowDefend.getTimescale())).asPoint();
            boundingBox = new Image(getImage()).getBoundingBoxAt(currentPoint);
        }
        new Image(Image).draw(this.currentPoint.x, this.currentPoint.y, new DrawOptions().setRotation(angle));
    }

    /**
     * This function checks if the slicer is not destroyed by tower and has not reached the end
     */
    public boolean onMap(){
        if(reached || eliminated){
            return false;
        }
        return true;
    }


    public Point getCurrentPoint() {
        return currentPoint;
    }

    public boolean isEliminated() {
        return eliminated;
    }

    public void setEliminated(boolean eliminated) {
        this.eliminated = eliminated;
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(Rectangle boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getReward() {
        return reward;
    }

    public void setREWARD(int REWARD) {
        this.reward = REWARD;
    }

    public int getPenalty() {
        return penalty;
    }

    public void setPenalty(int penalty) {
        this.penalty = penalty;
    }
}
