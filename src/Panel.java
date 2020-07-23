import bagel.Image;

/**
 * A class representing panel in the game
 * Parent class of StatusPanel and BuyPanel
 */
public class Panel {
    private Image image;

    /**
     * Panel constructor
     * @param imagePath fire path of panel image
     */
    public Panel(String imagePath){
        this.image = new Image(imagePath);
    }

    /**
     * Get the image of panel
     * @return image
     */
    public Image getImage() {
        return image;
    }
}
