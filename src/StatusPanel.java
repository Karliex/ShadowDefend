import bagel.DrawOptions;
import bagel.Font;
import bagel.util.Colour;

/**
 * Child class of Panel, draw at the bottom
 */
public class StatusPanel extends Panel{
    private static final double X = 0.0;
    private static final int FONT_SIZE = 20;
    private static final int LIFE_X = 900;
    private static final int STATUS_X = 450;
    private static final int TIMESCALE_X = 200;
    private static final double PADDING = 4.5;
    private static final double INITIAL_TIMESCALE = 1.00;
    private static final String IMAGE_PATH = "res/images/statuspanel.png";
    private static final String FONT_PATH = "res/fonts/DejaVuSans-Bold.ttf";

    /**
     * StatusPanel constructor
     */
    public StatusPanel() {
        super(IMAGE_PATH);
    }

    /**
     * Draw Status Panel and related information
     * @param waveNumber current wave that we are processing
     * @param timescale current timescale
     * @param status current status
     * @param lives current lives
     */
    public void render(int waveNumber, double timescale, Status status, int lives){
        getImage().drawFromTopLeft(X,ShadowDefend.getHEIGHT() - getImage().getHeight());
        //render wave info
        Font font = new Font(FONT_PATH, FONT_SIZE);
        font.drawString(String.format("Wave: %d", waveNumber), PADDING, ShadowDefend.getHEIGHT()-PADDING);

        // render status info
        String currentStatus = null;
        switch (status) {
            case WINNER:
                currentStatus = "WINNER";
                break;
            case PLACING:
                currentStatus = "PLACING";
                break;
            case WAVE_IN_PROGRESS:
                currentStatus = "WAVE_IN_PROGRESS";
                break;
            case AWAITING_START:
                currentStatus = "AWAITING_START";
                break;
        }
        font.drawString("Status: "+currentStatus, STATUS_X,ShadowDefend.getHEIGHT()-PADDING);

        //render life info
        font.drawString(String.format("Lives: %d",lives), LIFE_X,ShadowDefend.getHEIGHT()-PADDING);

        // render timeScale info
        if (timescale > INITIAL_TIMESCALE){
            font.drawString(String.format("Time Scale: %.1f", timescale), TIMESCALE_X,
                    ShadowDefend.getHEIGHT()-PADDING, new DrawOptions().setBlendColour(Colour.GREEN));
        }
        else{font.drawString(String.format("Time Scale: %.1f", timescale), TIMESCALE_X,
                    ShadowDefend.getHEIGHT()-PADDING);
        }
    }
}
