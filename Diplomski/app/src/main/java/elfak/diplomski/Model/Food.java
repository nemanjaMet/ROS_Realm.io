package elfak.diplomski.Model;

import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.annotations.LinkingObjects;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Neca on 25.10.2017..
 */

public class Food extends RealmObject {
    @PrimaryKey
    @Required
    private String name;
    private String describe;
    private String rating;
    private byte[] image;
    private String price;
    private MenuCategory menuCategory;
    @LinkingObjects("food")
    private final RealmResults<CommentAndRating> foodCoomments = null;
    //private String priceCurrency;

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setDescribe(String describe)
    {
        this.describe = describe;
    }

    public String getDescribe()
    {
        return this.describe;
    }

    public void setRating(String rating)
    {
        this.rating = rating;
    }

    public String getRating()
    {
        return this.rating;
    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    public byte[] getImage()
    {
        return this.image;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return this.price;
    }

    public void setMenuCategory(MenuCategory menuCategory) {
        this.menuCategory = menuCategory;
    }

    public MenuCategory getMenuCategory() {
        return this.menuCategory;
    }

    public RealmResults<CommentAndRating> getFoodCoomments() {
        return foodCoomments;
    }

    /*public String getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(String priceCurrency) {
        this.priceCurrency = priceCurrency;
    }*/
}

