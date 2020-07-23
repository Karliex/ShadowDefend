import bagel.Image;

/**
 * A class represents purchasable product on BuyPanel
 */
public class Product {
    private Image image;
    private int price;

    /**
     * Constructor for a product on BuyPanel
     * @param image
     * @param price
     */
    public Product(Image image, int price) {
        this.image = image;
        this.price = price;
    }

    //Getters and setters for accessing and modifying variables in other classes
    public Image getImage() {return image;}
    public void setImage(Image image) { this.image = image; }
    public int getPrice() { return price; }
}
