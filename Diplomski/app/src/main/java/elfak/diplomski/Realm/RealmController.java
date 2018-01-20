package elfak.diplomski.Realm;

import android.app.Activity;
import android.app.Application;
import android.app.Fragment;
import android.content.Context;
import android.view.Menu;
import android.widget.Toast;

import elfak.diplomski.BuildConfig;
import elfak.diplomski.LoginActivity;
import elfak.diplomski.Model.CommentAndRating;
import elfak.diplomski.Model.Drink;
import elfak.diplomski.Model.Food;
import elfak.diplomski.Model.MenuCategory;
import elfak.diplomski.Model.OrderPlaced;
import elfak.diplomski.Model.User;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

/**
 * Created by Neca on 25.10.2017..
 */

public class RealmController {
    private static RealmController instance;
    private final Realm realm;

    //public static final String REALM_URL = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/~/default";
    public static final String REALM_AUTH_URL = "http://" + BuildConfig.OBJECT_SERVER_IP + ":9080/auth";
    //public static final String REALM_URL_USERS = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/~/Users";
    //public static final String REALM_URL_CATEGORY = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/~/Category";
    //public static final String REALM_URL_FOOD = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/~/Food";
    //public static final String REALM_URL_DRINK = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/~/Drink";
    public static final String REALM_URL_ROS = "realm://" + BuildConfig.OBJECT_SERVER_IP + ":9080/~/ROS1";
    public static final String REALM_USERNAME = "golden";
    public static final String REALM_PASSWORD = "golden";

    public RealmController(Application application) {
        // Initialize Realm. Should only be done once when the application starts.

        // If dont work then uncomment this
        //Realm.init(application.getApplicationContext());

        // Create the Realm instance
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }



    public static void addUser(final User user, Realm realm, final Context context) {
       /* try {
            realm.beginTransaction();
            realm.insertOrUpdate(user);
            realm.commitTransaction();
            return true;
        } catch (Exception ex) {
            return false;
        }*/

       try {
           realm.executeTransactionAsync(new Realm.Transaction() {
                                             @Override
                                             public void execute(Realm bgRealm) {
                                                 bgRealm.insertOrUpdate(user);
                                                 //uploadData();
                                                 //realm.insertOrUpdate(user);
                                             }
                                         }, new Realm.Transaction.OnSuccess() {
                                             @Override
                                             public void onSuccess() {
                                                 // Original queries and Realm objects are automatically updated.
                                                 Toast.makeText(context, "Created", Toast.LENGTH_SHORT).show();
                                                 //realm.close();
                                             }
                                         }, new Realm.Transaction.OnError() {
                                             @Override
                                             public void onError(Throwable error) {
                                                 Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                             }
                                         }
           );
       } catch (Exception ex) {
           Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
       } finally {
           /*if(realm != null) {
               realm.close();
           }*/
       }

    }

    //clear all objects from User.class
    public void clearAllUsers() {

        realm.beginTransaction();
        realm.delete(User.class);
        realm.commitTransaction();
    }

    //find all objects in the User.class
    public RealmResults<User> getUsers() {

        return realm.where(User.class).findAll();
    }

    //query a single item with the given id
    public static User getUser(String username, Realm realm, Context context) {

        if (realm == null || realm.isClosed()) {
            final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(), RealmController.REALM_URL_ROS).build();
            Realm.setDefaultConfiguration(syncConfiguration);
            realm = Realm.getDefaultInstance();
        }

        User user = null;
        try {
            user = realm.where(User.class).equalTo("username", username).findFirst();
        } catch (Exception ex) {
            Toast.makeText(context,"RealmController: " + ex.toString(), Toast.LENGTH_LONG).show();
        } /*finally {
            if(realm != null && !realm.isClosed()) {
                realm.close();
            }
        }*/
        return user;
    }

    public static User getUser(String username, String password, Realm realm, Context context) {
        User user = null;
        try {
            user = realm.where(User.class).equalTo("username", username).equalTo("password", password).findFirst();
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
        return user;
    }

    public static void deleteUser(Realm realm, final Context context, final String username) {
        try {
            realm.executeTransactionAsync(new Realm.Transaction() {
                                              @Override
                                              public void execute(Realm bgRealm) {
                                                  bgRealm.where(CommentAndRating.class).equalTo("user.username", username).findAll().deleteAllFromRealm();
                                                  bgRealm.where(User.class).equalTo("username", username).findAll().deleteAllFromRealm();
                                              }
                                          }, new Realm.Transaction.OnSuccess() {
                                              @Override
                                              public void onSuccess() {
                                                  // Original queries and Realm objects are automatically updated.
                                                  Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                                  //realm.close();
                                              }
                                          }, new Realm.Transaction.OnError() {
                                              @Override
                                              public void onError(Throwable error) {
                                                  Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                              }
                                          }
            );
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if(realm != null && !realm.isClosed()) {
                realm.close();
            }
        }
    }

    //check if User.class is empty
    public boolean hasUsers() {

        return !realm.where(User.class).findAll().isEmpty();
    }

    //query example
    /*public RealmResults<User> queryedBooks() {

        return realm.where(User.class)
                .contains("author", "Author 0")
                .or()
                .contains("title", "Realm")
                .findAll();
    }*/

    //find all objects in the MenuCategory.class
    public static RealmResults<MenuCategory> getMenuCategories(Realm realm, Context context) {
        RealmResults<MenuCategory> realmResults = null;

        try {
            realmResults = realm.where(MenuCategory.class).findAll();
            if (realmResults != null) {
                realmResults = realmResults.sort("sortNumber", Sort.ASCENDING);
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
        return realmResults;
    }

    public static RealmResults<Food> getFood(Realm realm, Context context, String category) {
        RealmResults<Food> realmResults = null;

        try {
            realmResults = realm.where(Food.class).equalTo("menuCategory.category", category).findAll();
            if (realmResults != null) {
                realmResults = realmResults.sort("name", Sort.ASCENDING);
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
        return realmResults;
    }

    public static void deleteFoodOrDrink(Realm realm, final Context context, final String name) {
        //Realm realm = null;
        try {
            /*final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(), RealmController.REALM_URL_ROS).build();
            Realm.setDefaultConfiguration(syncConfiguration);
            realm = Realm.getDefaultInstance();*/

            realm.executeTransactionAsync(new Realm.Transaction() {
                                              @Override
                                              public void execute(Realm bgRealm) {
                                                  bgRealm.where(Food.class).equalTo("name", name).findAll().deleteAllFromRealm();
                                                  //uploadData();
                                                  //realm.insertOrUpdate(user);
                                              }
                                          }, new Realm.Transaction.OnSuccess() {
                                              @Override
                                              public void onSuccess() {
                                                  // Original queries and Realm objects are automatically updated.
                                                  Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                                  //realm.close();
                                              }
                                          }, new Realm.Transaction.OnError() {
                                              @Override
                                              public void onError(Throwable error) {
                                                  Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                              }
                                          }
            );
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if(realm != null && !realm.isClosed()) {
                realm.close();
            }
        }
    }

    public static void deleteAllFoodWithNullCategory(final Realm realm, final Context context) {
        try {
            /*final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(), RealmController.REALM_URL_ROS).build();
            Realm.setDefaultConfiguration(syncConfiguration);
            realm = Realm.getDefaultInstance();*/

            //final RealmResults<Food> foods = realm.where(Food.class).equalTo("menuCategory.category", category).findAll();
            //final RealmResults<MenuCategory> menuCategories = realm.where(MenuCategory.class).equalTo("category", category).findAll();

            realm.executeTransactionAsync(new Realm.Transaction() {
                                              @Override
                                              public void execute(Realm bgRealm) {

                                                  //bgRealm.where(CommentAndRating.class).equalTo("food", usernameAndFood).findAll().deleteAllFromRealm();

                                                  bgRealm.where(Food.class).isNull("menuCategory").findAll().deleteAllFromRealm();

                                                  /*bgRealm.where(Food.class).equalTo("menuCategory.category", null).findAll();//.deleteAllFromRealm();

                                                  for (int i= 0; i < foods.size(); i++) {
                                                      foods.get(i).getFoodCoomments().deleteAllFromRealm();
                                                  }
                                                  foods.deleteAllFromRealm();

                                                  bgRealm.where(MenuCategory.class).equalTo("category", category).findAll().deleteAllFromRealm();*/

                                              }
                                          }, new Realm.Transaction.OnSuccess() {
                                              @Override
                                              public void onSuccess() {
                                                  // Original queries and Realm objects are automatically updated.
                                                  //Toast.makeText(context, "Successfully deleted category and all items", Toast.LENGTH_SHORT).show();
                                                  deleteAllCommentsWithNullFood(realm, context);
                                              }
                                          }, new Realm.Transaction.OnError() {
                                              @Override
                                              public void onError(Throwable error) {
                                                  Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                              }
                                          }
            );
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        } /*finally {
            if(realm != null && !realm.isClosed()) {
                realm.close();
            }
        }*/
    }

    public static void deleteAllCommentsWithNullFood(final Realm realm, final Context context) {
        try {
            /*final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(), RealmController.REALM_URL_ROS).build();
            Realm.setDefaultConfiguration(syncConfiguration);
            realm = Realm.getDefaultInstance();*/

            //final RealmResults<Food> foods = realm.where(Food.class).equalTo("menuCategory.category", category).findAll();
            //final RealmResults<MenuCategory> menuCategories = realm.where(MenuCategory.class).equalTo("category", category).findAll();

            realm.executeTransactionAsync(new Realm.Transaction() {
                                              @Override
                                              public void execute(Realm bgRealm) {

                                                  //bgRealm.where(CommentAndRating.class).equalTo("food", usernameAndFood).findAll().deleteAllFromRealm();

                                                  bgRealm.where(CommentAndRating.class).isNull("food").findAll().deleteAllFromRealm();

                                                  /*bgRealm.where(Food.class).equalTo("menuCategory.category", null).findAll();//.deleteAllFromRealm();

                                                  for (int i= 0; i < foods.size(); i++) {
                                                      foods.get(i).getFoodCoomments().deleteAllFromRealm();
                                                  }
                                                  foods.deleteAllFromRealm();

                                                  bgRealm.where(MenuCategory.class).equalTo("category", category).findAll().deleteAllFromRealm();*/

                                              }
                                          }, new Realm.Transaction.OnSuccess() {
                                              @Override
                                              public void onSuccess() {
                                                  // Original queries and Realm objects are automatically updated.
                                                  //Toast.makeText(context, "Successfully deleted category and all items", Toast.LENGTH_SHORT).show();
                                              }
                                          }, new Realm.Transaction.OnError() {
                                              @Override
                                              public void onError(Throwable error) {
                                                  Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                              }
                                          }
            );
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if(realm != null && !realm.isClosed()) {
                realm.close();
            }
        }
    }

    public static void deleteCategoryAndAllItems(Realm realm, final Context context, final String category) {
       // Realm realm = null;
        try {
            /*final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(), RealmController.REALM_URL_ROS).build();
            Realm.setDefaultConfiguration(syncConfiguration);
            realm = Realm.getDefaultInstance();*/

            //final RealmResults<Food> foods = realm.where(Food.class).equalTo("menuCategory.category", category).findAll();
            //final RealmResults<MenuCategory> menuCategories = realm.where(MenuCategory.class).equalTo("category", category).findAll();

            realm.executeTransactionAsync(new Realm.Transaction() {
                                              @Override
                                              public void execute(Realm bgRealm) {

                                                  //bgRealm.where(CommentAndRating.class).equalTo("food", usernameAndFood).findAll().deleteAllFromRealm();

                                                  RealmResults<Food> foods = bgRealm.where(Food.class).equalTo("menuCategory.category", category).findAll();//.deleteAllFromRealm();

                                                  for (int i= 0; i < foods.size(); i++) {
                                                      foods.get(i).getFoodCoomments().deleteAllFromRealm();
                                                  }
                                                  foods.deleteAllFromRealm();

                                                  bgRealm.where(MenuCategory.class).equalTo("category", category).findAll().deleteAllFromRealm();
                                                  //foods.deleteAllFromRealm();
                                                  //menuCategories.deleteAllFromRealm();
                                              }
                                          }, new Realm.Transaction.OnSuccess() {
                                              @Override
                                              public void onSuccess() {
                                                  // Original queries and Realm objects are automatically updated.
                                                  Toast.makeText(context, "Successfully deleted category and all items", Toast.LENGTH_SHORT).show();
                                                  //deleteCategory(context, category);
                                                  //realm.close();
                                              }
                                          }, new Realm.Transaction.OnError() {
                                              @Override
                                              public void onError(Throwable error) {
                                                  Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                              }
                                          }
            );
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if(realm != null && !realm.isClosed()) {
                realm.close();
            }
        }
    }

    public static void deleteCategory(final Context context, final String category) {
        Realm realm = null;
        try {
            final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(), RealmController.REALM_URL_ROS).build();
            Realm.setDefaultConfiguration(syncConfiguration);
            realm = Realm.getDefaultInstance();

            realm.executeTransactionAsync(new Realm.Transaction() {
                                              @Override
                                              public void execute(Realm bgRealm) {

                                                  bgRealm.where(MenuCategory.class).equalTo("category", category).findAll().deleteAllFromRealm();
                                                  //uploadData();
                                                  //realm.insertOrUpdate(user);
                                              }
                                          }, new Realm.Transaction.OnSuccess() {
                                              @Override
                                              public void onSuccess() {
                                                  // Original queries and Realm objects are automatically updated.
                                                  Toast.makeText(context, "Successfully deleted category", Toast.LENGTH_SHORT).show();
                                                  //realm.close();
                                              }
                                          }, new Realm.Transaction.OnError() {
                                              @Override
                                              public void onError(Throwable error) {
                                                  Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                              }
                                          }
            );
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if(realm != null) {
                realm.close();
            }
        }
    }

    public static void addCommentAndRating(final Realm realm, final Context context, final CommentAndRating commentAndRating) {
        try {

            /*final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(), RealmController.REALM_URL_ROS).build();
            Realm.setDefaultConfiguration(syncConfiguration);
            realm = Realm.getDefaultInstance();*/



            realm.executeTransactionAsync(new Realm.Transaction() {
                                              @Override
                                              public void execute(Realm bgRealm) {
                                                  bgRealm.insertOrUpdate(commentAndRating);
                                                  /*Food food = commentAndRating.getFood();
                                                  RealmResults<CommentAndRating> commentAndRatings = bgRealm.where(CommentAndRating.class).equalTo("food.name", food.getName()).findAll();
                                                  food.setRating(String.valueOf(commentAndRatings.average("rating")));
                                                  bgRealm.insertOrUpdate(food);*/
                                              }
                                          }, new Realm.Transaction.OnSuccess() {
                                              @Override
                                              public void onSuccess() {
                                                  // Original queries and Realm objects are automatically updated.
                                                  try {
                                                      Food food = commentAndRating.getFood();
                                                      RealmResults<CommentAndRating> commentAndRatings = realm.where(CommentAndRating.class).equalTo("food.name", food.getName()).findAll();
                                                      realm.beginTransaction();
                                                      food.setRating(String.valueOf(commentAndRatings.average("rating")));
                                                      //realm.insertOrUpdate(food);
                                                      realm.commitTransaction();
                                                      Toast.makeText(context, "Successfully added comment and rating", Toast.LENGTH_SHORT).show();
                                                  } catch (Exception ex) {
                                                      Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
                                                  }

                                              }
                                          }, new Realm.Transaction.OnError() {
                                              @Override
                                              public void onError(Throwable error) {
                                                  Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                              }
                                          }
            );
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if(realm != null && !realm.isClosed()) {
                realm.close();
            }
        }
    }

    public static RealmResults<CommentAndRating> getCommentsAndRatings(Realm realm, final Context context, final String foodName) {
        RealmResults<CommentAndRating> realmResults = null;

        try {
            realmResults = realm.where(CommentAndRating.class).equalTo("food.name", foodName).findAll();
            if (realmResults != null) {
                realmResults = realmResults.sort("dateAndTime", Sort.DESCENDING);
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
        return realmResults;
    }

    /*public static void updateRating(Realm realm, final Context context, final String foodName) {

        try {
            Food food = realm.where(Food.class).equalTo("name", foodName).findFirst();
            RealmResults<CommentAndRating> commentAndRatings = realm.where(CommentAndRating.class).equalTo("food.name", food.getName()).findAll();
            realm.beginTransaction();
            food.setRating(String.valueOf(commentAndRatings.average("rating")));
            //realm.insertOrUpdate(food);
            realm.commitTransaction();
            //Toast.makeText(context, "Successfully added comment and rating", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }*/


    public static RealmResults<CommentAndRating> getMyCommentsAndRatings(Realm realm, final Context context, final String username) {
        RealmResults<CommentAndRating> realmResults = null;

        try {
            realmResults = realm.where(CommentAndRating.class).equalTo("user.username", username).findAll();
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
        return realmResults;
    }

    public static void deleteCommentAndRating(Realm realm, final Context context, final String usernameAndFood) {
        try {

           /* if (realm == null || realm.isClosed()) {
                final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(), RealmController.REALM_URL_ROS).build();
                Realm.setDefaultConfiguration(syncConfiguration);
                realm = Realm.getDefaultInstance();
            }*/
            /*final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(), RealmController.REALM_URL_ROS).build();
            Realm.setDefaultConfiguration(syncConfiguration);
            realm = Realm.getDefaultInstance();*/

            realm.executeTransactionAsync(new Realm.Transaction() {
                                              @Override
                                              public void execute(Realm bgRealm) {

                                                  bgRealm.where(CommentAndRating.class).equalTo("usernameAndFood", usernameAndFood).findAll().deleteAllFromRealm();

                                              }
                                          }, new Realm.Transaction.OnSuccess() {
                                              @Override
                                              public void onSuccess() {
                                                  Toast.makeText(context, "Successfully deleted comment", Toast.LENGTH_SHORT).show();
                                              }
                                          }, new Realm.Transaction.OnError() {
                                              @Override
                                              public void onError(Throwable error) {
                                                  Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                              }
                                          }
            );
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if(realm != null && !realm.isClosed()) {
                realm.close();
            }
        }
    }

    public static void addOrderPlaced(Realm realm, final Context context, final OrderPlaced orderPlaced) {
        try {

            if (realm == null || realm.isClosed()) {
                final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(), RealmController.REALM_URL_ROS).build();
                Realm.setDefaultConfiguration(syncConfiguration);
                realm = Realm.getDefaultInstance();
            }


            realm.executeTransactionAsync(new Realm.Transaction() {
                                              @Override
                                              public void execute(Realm bgRealm) {
                                                  bgRealm.insertOrUpdate(orderPlaced);
                                              }
                                          }, new Realm.Transaction.OnSuccess() {
                                              @Override
                                              public void onSuccess() {
                                                  // Original queries and Realm objects are automatically updated.
                                                  Toast.makeText(context, "Successfully ordered", Toast.LENGTH_SHORT).show();

                                              }
                                          }, new Realm.Transaction.OnError() {
                                              @Override
                                              public void onError(Throwable error) {
                                                  Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
                                              }
                                          }
            );
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if(realm != null && !realm.isClosed()) {
                realm.close();
            }
        }
    }

    public static RealmResults<OrderPlaced> getMyOrders(Realm realm, final Context context, final String username) {
        RealmResults<OrderPlaced> realmResults = null;

        try {
            realmResults = realm.where(OrderPlaced.class).equalTo("user.username", username).findAll();
            if (realmResults != null) {
                realmResults = realmResults.sort("dateAndTime", Sort.DESCENDING);
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
        return realmResults;
    }

    public static RealmResults<OrderPlaced> getMyNotCompletedOrders(Realm realm, final Context context, final String username) {
        RealmResults<OrderPlaced> realmResults = null;

        try {
            realmResults = realm.where(OrderPlaced.class).equalTo("user.username", username).notEqualTo("status", "Completed").or().notEqualTo("status", "Rejected").findAll();
            if (realmResults != null) {
                realmResults = realmResults.sort("dateAndTime", Sort.DESCENDING);
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
        return realmResults;
    }

    public static RealmResults<OrderPlaced> getAllOrders(Realm realm, final Context context) {
        RealmResults<OrderPlaced> realmResults = null;

        try {
            realmResults = realm.where(OrderPlaced.class).findAll();
            if (realmResults != null) {
                realmResults = realmResults.sort("dateAndTime", Sort.DESCENDING);
            }
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
        }
        return realmResults;
    }

}

