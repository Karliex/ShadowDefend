import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;

/**
 * Child class of Panel, draw at top
 */
public class BuyPanel extends Panel {
    //Image paths
    public static final String BUY_PANEL_IMAGE = "res/images/buypanel.png";
    public static final String TANK_IMAGE = "res/images/tank.png";
    public static final String SUPER_TANK_IMAGE = "res/images/supertank.png";
    public static final String AIR_PLANE_IMAGE = "res/images/airsupport.png";

    //define key binds
    private static final String TITLE = "Key binds:";
    private static final String TIPS_S = "S - Start Wave";
    private static final String TIPS_L = "L - Increase TimeScale";
    private static final String TIPS_K = "K - Decrease TimeScale";

    //Panel settings
    private static final int HEIGHT = 10;
    private static final int APART = 120;
    private static final int RIGHT_GAP = 200;
    private static final int LEFT_GAP = 64;
    private static final int MONEY_HEIGHT = 65;
    private static final int TRY = 50;
    private static final int TIPS_X = 450;
    private static final int TIPS_TITLE_Y = 20;
    private static final int TIPS_S_Y = 50;
    private static final int TIPS_L_Y = 65;
    private static final int TIPS_K_Y = 80;
    private static final int WIDTH_OF_NUMBERS = 37;
    private static final int ZERO = 0;

    //size related
    private static final int FONT_SIZE = 20;
    private static final int TIPS_SIZE = 15;
    private static final int MONEY_SIZE = 45;

    //position of three products
    public static final int AIRPLANE_X = LEFT_GAP + 2 * APART;
    public static final int SUPERTANK_X = LEFT_GAP + APART;
    public static final int TANK_X = LEFT_GAP;
    public static double Y;

    // three products of buy panel
    private Product airplane;
    private Product tank;
    private Product superTank;

    // prices of three products
    private static final int AIRPLANE_PRICE = 500;
    private static final int TANK_PRICE = 250;
    private static final int SUPER_TANK_PRICE = 600;

    /**
     * A constructor for BuyPanel
     */
    public BuyPanel() {
        super(BUY_PANEL_IMAGE);
        this.airplane = new Product(new Image(AIR_PLANE_IMAGE),AIRPLANE_PRICE);
        this.tank = new Product(new Image(TANK_IMAGE),TANK_PRICE);
        this.superTank = new Product(new Image(SUPER_TANK_IMAGE), SUPER_TANK_PRICE);
        Y = getImage().getHeight() / 2 - HEIGHT;
    }

    /**
     * Draw BuyPanel with related information
     */
    public void render(){
        // render background
        getImage().drawFromTopLeft(ZERO, ZERO);

        // render tips
        renderKeyBinds(TITLE, TIPS_X, TIPS_TITLE_Y, TIPS_SIZE);
        renderKeyBinds(TIPS_K, TIPS_X, TIPS_K_Y, TIPS_SIZE);
        renderKeyBinds(TIPS_L, TIPS_X, TIPS_L_Y, TIPS_SIZE);
        renderKeyBinds(TIPS_S, TIPS_X, TIPS_S_Y, TIPS_SIZE);

        // render money
        renderKeyBinds("$" + ShadowDefend.getMoney(),
                ShadowDefend.getWIDTH() - RIGHT_GAP, MONEY_HEIGHT, MONEY_SIZE);

        //render products(airplane, tank, supertank)
        renderProduct(airplane, AIRPLANE_X, Y);
        renderProduct(tank, TANK_X, Y);
        renderProduct(superTank, SUPERTANK_X, Y);
    }

    /**
     * Draw three products which are available on BuyPanel
     * @param tower type of tower that could be purchased
     * @param x x-coordinate from left
     * @param y y-coordinate form right
     */
    public void renderProduct(Product tower, double x, double y){
        String showPrice = Integer.toString(tower.getPrice());
        double productWidth = tower.getImage().getWidth();
        Font font = new Font("res/fonts/DejaVuSans-Bold.ttf", FONT_SIZE);

        tower.getImage().draw(x,y);

        //if funds is sufficient
        if(ShadowDefend.getMoney() >= tower.getPrice()){
            font.drawString(showPrice,x - WIDTH_OF_NUMBERS / 2.00,y + TRY);
        }
        else{
            font.drawString(showPrice,x - WIDTH_OF_NUMBERS / 2.00,y + TRY,
                    new DrawOptions().setBlendColour(Colour.RED));
        }
    }

    /**
     * Draw content for key binds
     * @param name explanation related object
     * @param x x-coordinate from left
     * @param y y-coordinate from top
     * @param size font size
     */
    public void renderKeyBinds(String name, double x, double y, int size){
        Font font = new Font("res/fonts/DejaVuSans-Bold.ttf", size);
        font.drawString(name, x, y);
    }

    //getters for accessing variables in other classes
    public static int getSuperTankPrice() {
        return SUPER_TANK_PRICE;
    }
    public static int getTankPrice(){
        return TANK_PRICE;
    }
    public static int getAirplanePrice(){
        return AIRPLANE_PRICE;
    }

}
