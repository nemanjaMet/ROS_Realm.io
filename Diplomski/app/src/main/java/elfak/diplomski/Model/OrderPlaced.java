package elfak.diplomski.Model;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Neca on 19.11.2017..
 */

public class OrderPlaced extends RealmObject {

    @PrimaryKey
    @Required
    private String usernameAndDateTime;
    private String dateAndTime; // Maybe is not good to be dateAndTime primary key
    @Required
    private RealmList<Integer> quantitys;
    private String totalPrice;
    private String paymentMethod;
    private String tableNumber;
    private User user;
    @Required
    private RealmList<Food> foods;
    private String status;
    private String orderListText;

    public RealmList<Integer> getQuantitys() {
        return quantitys;
    }

    public void setQuantitys(RealmList<Integer> quantitys) {
        this.quantitys = quantitys;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public RealmList<Food> getFoods() {
        return foods;
    }

    public void setFoods(RealmList<Food> foods) {
        this.foods = foods;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getUsernameAndDateTime() {
        return usernameAndDateTime;
    }

    public void setUsernameAndDateTime(String userAndDateTime) {
        this.usernameAndDateTime = userAndDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getOrderListText() {
        return orderListText;
    }

    public void setOrderListText(String orderListText) {
        this.orderListText = orderListText;
    }
}
