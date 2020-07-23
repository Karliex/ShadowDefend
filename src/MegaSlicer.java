import bagel.Image;
import bagel.util.Point;
import java.util.List;
/**
 * Child class of Slicer, one type of slicer
 */
public class MegaSlicer extends Slicer{
    /**
     * Constructor
     * @param polyLine start at the starting point of the line
     */
    public MegaSlicer(List<Point> polyLine) {
        super(polyLine);
        setSpeed(1.5);
        setHealth(2);
        setREWARD(10);
        setPenalty(4);
        setImage("res/images/megaslicer.png");
        setBoundingBox(new Image(getImage()).getBoundingBoxAt(getCurrentPoint()));
    }

    /**
     * Constructor for mega slicers as spawned children slicers
     * @param parentSlicer its parent slicer
     */
    public MegaSlicer(Slicer parentSlicer) {
        super(parentSlicer);
        setImage("res/images/megaslicer.png");
        setSpeed(1.5);
        setHealth(2);
        setREWARD(10);
        setPenalty(4);
        setBoundingBox(new Image(getImage()).getBoundingBoxAt(getCurrentPoint()));
    }
}
