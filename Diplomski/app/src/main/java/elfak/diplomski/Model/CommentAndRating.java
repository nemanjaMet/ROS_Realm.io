package elfak.diplomski.Model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Neca on 28.11.2017..
 */

public class CommentAndRating extends RealmObject {

    @PrimaryKey
    @Required
    private String usernameAndFood;
    private String dateAndTime;
    private String comment;
    private double rating;
    private User user;
    private Food food;

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }

    public String getUsernameAndFood() {
        return usernameAndFood;
    }

    public void setUsernameAndFood(String usernameAndFood) {
        this.usernameAndFood = usernameAndFood;
    }
}
