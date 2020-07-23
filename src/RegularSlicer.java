import bagel.Image;
import bagel.util.Point;
import java.util.List;
/**
 * Child class of Slicer, one type of slicer
 */
public class RegularSlicer extends Slicer{
    /**
     * Constructor
     * @param polyLine start at the starting point of the line
     */
    public RegularSlicer(List<Point> polyLine) {
        super(polyLine);
        setSpeed(2);
        setHealth(1);
        setREWARD(2);
        setPenalty(1);
        setImage("res/images/slicer.png");
        setBoundingBox(new Image(getImage()).getBoundingBoxAt(getCurrentPoint()));
    }

    /**
     * Constructor for mega slicers as spawned children slicers
     * @param parentSlicer its parent slicer
     */
    public RegularSlicer(Slicer parentSlicer) {
        super(parentSlicer);
        setImage("res/images/slicer.png");
        setSpeed(2);
        setHealth(1);
        setREWARD(2);
        setPenalty(1);
        setBoundingBox(new Image(getImage()).getBoundingBoxAt(getCurrentPoint()));
    }
}
