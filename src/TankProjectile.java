import bagel.util.Point;

/**
 * Child class of Projectile, a type of projectile for tank only
 */
public class TankProjectile extends Projectile{
    /**
     * This function is used to initialise the super tank projectile.
     * @param position
     * @param target
     */
    public TankProjectile(Point position, Slicer target) {
        super("tankprojectile", position, target);
        this.setDamage(1);
        this.setRadius(100);
        this.setTowerLocation(position);
    }

}

