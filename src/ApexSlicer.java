import bagel.util.Point;
import bagel.Image;
import java.util.List;

/**
 * Child class of Slicer, one type of slicer
 */
public class ApexSlicer extends Slicer{
    /**
     * Constructor for ApexSlicer
     * @param polyLine where the slicer start from
     */
    public ApexSlicer(List<Point> polyLine) {
        super(polyLine);
        setSpeed(0.75);
        setHealth(25);
        setREWARD(150);
        setPenalty(16);
        setImage("res/images/apexslicer.png");
        setBoundingBox(new Image(getImage()).getBoundingBoxAt(getCurrentPoint()));
    }
}
