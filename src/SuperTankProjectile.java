import bagel.util.Point;
/**
 * Child class of Projectile, a type of projectile for supertank only
 */
public class SuperTankProjectile extends Projectile {
    /**
     * Constructor for super tank projectile.
     * @param position start position
     * @param target target slicer
     */
    public SuperTankProjectile(Point position, Slicer target) {
        super("supertankprojectile", position, target);
        this.setDamage(3);
        this.setRadius(150);
        this.setTowerLocation(position);
    }
}

