import bagel.Image;
import bagel.util.Point;
import java.util.ArrayList;

public class Explosive {
    private static final Image IMAGE = new Image("res/images/explosive.png");
    private static final int RADIUS = 200;
    private static final int DAMAGE = 500;
    private static final int DETONATE_GAP = 2 ; //wait time in seconds
    private static final int FPS = 60;
    private int dropTime;
    private Point dropPosition;

    /**
     * Constructor for explosive
     * @param dropPosition position to drop explosive
     */
    public Explosive(Point dropPosition){
        this.dropTime = ShadowDefend.getTimer();
        this.dropPosition = dropPosition;
    }

    /**
     * Update detonation by checking it is at correct time and explode range.
     */
    public void update(){
        ArrayList<Slicer> newSpawn = new ArrayList<>();
        //check it is at the correct time
        if (ShadowDefend.getTimer() == dropTime + DETONATE_GAP *FPS){
            for (Slicer slicer : ShadowDefend.getSlicers()) {
                // check if all slicers are on the explosive range
                if (dropPosition.distanceTo(slicer.getCurrentPoint()) <= RADIUS && !slicer.isEliminated()) {
                    slicer.setEliminated(true);
                    slicer.setHealth(slicer.getHealth() - DAMAGE);
                    Projectile.spawnChildren(slicer, newSpawn);
                    ShadowDefend.addReward(slicer);
                }
            }
            //add split child slicers and clear immediately to avoid concurrent modification
            ShadowDefend.getSlicers().addAll(newSpawn);
            newSpawn.clear();

        }
        //draw updated explosives
        if (ShadowDefend.getTimer() <= dropTime + DETONATE_GAP *FPS){
            IMAGE.draw(dropPosition.x, dropPosition.y);
        }
    }
}
