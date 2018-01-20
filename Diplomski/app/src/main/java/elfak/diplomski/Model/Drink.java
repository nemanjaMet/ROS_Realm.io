package elfak.diplomski.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Neca on 7.11.2017..
 */

public class Drink extends RealmObject {
    @PrimaryKey
    @Required
    private String name;
    private String describe;
    private String rating;
    private byte[] image;
    private String price;
    private MenuCategory menuCategory;

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
}
