import bagel.util.Point;
import bagel.Image;
import java.util.List;
/**
 * Child class of Slicer, one type of slicer
 */
public class SuperSlicer extends Slicer{
    /**
     * Constructor
     * @param polyLine start at the starting point of the line
     */
    public SuperSlicer(List<Point> polyLine) {
        super(polyLine);
        setSpeed(1.5);
        setHealth(2);
        setREWARD(15);
        setPenalty(2);
        setImage("res/images/superslicer.png");
        setBoundingBox(new Image(getImage()).getBoundingBoxAt(getCurrentPoint()));
    }

    /**
     * Constructor for mega slicers as spawned children slicers
     * @param parentSlicer its parent slicer
     */
    public SuperSlicer(Slicer parentSlicer) {
        super(parentSlicer);
        setImage("res/images/superslicer.png");
        setSpeed(1.5);
        setHealth(2);
        setREWARD(15);
        setPenalty(2);
        setBoundingBox(new Image(getImage()).getBoundingBoxAt(getCurrentPoint()));
    }
}
