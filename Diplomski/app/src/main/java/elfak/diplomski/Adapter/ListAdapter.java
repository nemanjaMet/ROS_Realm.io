package elfak.diplomski.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import elfak.diplomski.MainActivity;
import elfak.diplomski.Model.Food;
import elfak.diplomski.Model.MenuCategory;
import elfak.diplomski.R;
import elfak.diplomski.Realm.RealmController;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by Neca on 14.11.2017..
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.FoodViewHolder> {

    //private List<Food> mFoodList;
    private RealmResults<Food> mFoodList;
    private volatile Realm realm;
    /**
     * This LinkedList is used to map the position of the item and the image
     */
    //protected LinkedList<Integer> drawableLinkedList;
    private Context mContext;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout_showFullItem;
    private int hideListView = -1;
    ///private LinkedList<String> itemsNameLinkedList;
    Map<String, Integer> nameDrawableList = new HashMap<String, Integer>();
    private int defaultDrawable = R.drawable.listview_add_touch;

    public ListAdapter(Context context, RealmResults<Food> followerList, Map<String, Integer> nameDrawableList, RelativeLayout relativeLayout, LinearLayout linearLayout_showFullItem, Realm realm) {
        this.mContext = context;
        this.mFoodList = followerList;
        //this.drawableLinkedList = drawableLinkedList;
        this.nameDrawableList = nameDrawableList;
        this.relativeLayout = relativeLayout;
        this.linearLayout_showFullItem = linearLayout_showFullItem;
        this.realm = realm;

        Iterator<Food> foodIterator;
        foodIterator = mFoodList.iterator();
        foodIterator.hasNext();


        setListenerOnData();
    }

    public void setListenerOnData() {

        /*for (int i=0; i<mFoodList.size(); i++) {
            //itemsNameLinkedList.add(mFoodList.get(i).getName());
            nameDrawableList.put(mFoodList.get(i).getName(), i);
        }*/

        if (nameDrawableList.containsValue(R.drawable.ic_action_delete)) {
            defaultDrawable = R.drawable.ic_action_delete;
        }

        mFoodList.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Food>>() {
            @Override
            public void onChange(RealmResults<Food> foods, @Nullable OrderedCollectionChangeSet changeSet) {

                /*if (mFoodList.size() != drawableLinkedList.size()) {
                    // Option 1: Only add new object in HashMap (save time)
                    // Option 2: Make new HashMap (save memory)
                    /**
                     * Da se napravi lista string(food name), drawable i da se posalje kroz konstruktor
                     * Na osnovu pozicije moze da se nadje food name iz liste da bise doslo do drawable
                     * a drawable lista da se doda novi item ili izbrise iz lista ako je moguce
                     * **/

                    //nameDrawableList
                //}

                /*try {
                    if (MainActivity.orderPlaced.getFoods() != null) {
                        for (int i = 0; i < MainActivity.orderPlaced.getFoods().size(); i++) {
                            if (!foods.contains(MainActivity.orderPlaced.getFoods().get(i))) {
                                MainActivity.orderPlaced.getFoods().deleteFromRealm(i);
                                //MainActivity.orderPlaced.getFoods().
                                i--;
                            }
                        }
                    }
                } catch (Exception ex) {
                    Toast.makeText(mContext, ex.toString(), Toast.LENGTH_SHORT).show();
                }*/


                notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onBindViewHolder(FoodViewHolder holder, final int position) {

        Food food = mFoodList.get(position);
        //final int actionDrawableId = this.drawableLinkedList.get(position);

        int drawableId = defaultDrawable;
        if (nameDrawableList.containsKey(food.getName())) {
            drawableId = nameDrawableList.get(food.getName());
        }

        final int actionDrawableId = drawableId;

        holder.name.setText(food.getName());
        if (food.getRating() != null) {
            holder.rating.setText("Rating: " + String.valueOf(food.getRating()));
        } else {
            holder.rating.setText("Rating: unrated");
        }
        //Use Glide to load the Image
        //Glide.with(mContext).load(movie.getThumbnailUrl()).centerCrop().into(holder.thumbNail);
        holder.thumbNail.setImageBitmap(byteToBitmap(food.getImage()));
        holder.thumbNail.setScaleType(ImageView.ScaleType.FIT_CENTER);
        /*int newWidth = rowView.getResources().getDisplayMetrics().widthPixels;
        //int newHeight = (newWidth*bitmaps[position].getHeight())/bitmaps[position].getWidth();
        int newHeight = rowView.getResources().getDisplayMetrics().heightPixels;
        //holder.imageView.setImageBitmap(bitmaps[position]);
        holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmaps[position], (int)(newWidth * 0.4), (int)(newHeight * 0.2), true));
        holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);*/
        // genre

        holder.describe.setText("Describe: " + food.getDescribe());
        holder.price.setText("Price: " + String.valueOf(food.getPrice()));


        /**
         * Set OnClickListener on the Button.
         * We pass in 3 parameters:
         * @param position :Position of the object on the List
         * @param mMovieList Movie Object
         * @param actionDrawableId Drawable ID
         */
        holder.imageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //onMemberClick(position, mFoodList, actionDrawableId);
                onMemberClick2(position, mFoodList, actionDrawableId);
            }
        });
        //Set the Image Resource
        holder.imageViewAdd.setImageResource(actionDrawableId);

        holder.describe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "Clicked!", Toast.LENGTH_SHORT).show();
                try {
                    hideListView = position;
                    relativeLayout.setVisibility(View.INVISIBLE);
                    linearLayout_showFullItem.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    Toast.makeText(mContext, ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "Clicked!", Toast.LENGTH_SHORT).show();
                try {
                    hideListView = position;
                    relativeLayout.setVisibility(View.INVISIBLE);
                    linearLayout_showFullItem.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    Toast.makeText(mContext, ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.thumbNail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "Clicked!", Toast.LENGTH_SHORT).show();
                try {
                    hideListView = position;
                    relativeLayout.setVisibility(View.INVISIBLE);
                    linearLayout_showFullItem.setVisibility(View.VISIBLE);
                } catch (Exception ex) {
                    Toast.makeText(mContext, ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

    @Override
    public long getItemId(int position) {
        //return position;
        if (hideListView != -1) {
            int posReturn = hideListView;
            hideListView = -1;
            return posReturn;
        }

        /*if (drawableLinkedList.get(position) == R.drawable.listview_added_touch){
            return position;
        } else {
            return -1;
        }*/

        if (nameDrawableList.get(mFoodList.get(position).getName()) == R.drawable.listview_added_touch){
            return position;
        } else {
            return -1;
        }
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.food_listview_layout, viewGroup, false);

        return new FoodViewHolder(itemView);
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbNail;
        ImageView imageViewAdd;
        TextView name;
        TextView rating;
        TextView describe;
        TextView price;
        //Button button;

        public FoodViewHolder(View itemView) {
            super(itemView);

            thumbNail = (ImageView) itemView.findViewById(R.id.thumbnail_listview);
            name = (TextView) itemView.findViewById(R.id.name_listview);
            rating = (TextView) itemView.findViewById(R.id.rating_listview);
            describe = (TextView) itemView.findViewById(R.id.describe_listview);
            price = (TextView) itemView.findViewById(R.id.price_listview);
            imageViewAdd = (ImageView) itemView.findViewById(R.id.btnAdd_listview);
            //button = (Button) itemView.findViewById(R.id.show_describe_listview);
        }
    }

    private Bitmap byteToBitmap(byte[] bytes) {
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        } catch (Exception ex) {
            //Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
            ex.getMessage();
            return null;
        }
    }

    /*protected void onMemberClick(final int position, final List<Food> followerList,int actionDrawableId) {
        final Food follower = followerList.get(position);
        switch (actionDrawableId) {
            case R.drawable.listview_add_touch:

                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        for (int i = 0; i < 1; i++) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.interrupted();
                            }
                        }
                        return "Complete";
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        if (result.equals("Complete")) {
                            Toast.makeText(mContext, follower.getName(), Toast.LENGTH_SHORT).show();
                            drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.listview_added_touch);
                            notifyDataSetChanged();

                        } else {
                            drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.listview_error_touch);
                            Toast.makeText(mContext, mContext.getString(R.string.text_something_went_wrong),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
                break;

            case R.drawable.listview_added_touch:
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        for (int i = 0; i < 1; i++) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.interrupted();
                            }
                        }
                        return "Complete";
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        if (result.equals("Complete")) {
                            drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.listview_add_touch);
                            notifyDataSetChanged();
                        } else {
                            drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.listview_error_touch);
                            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
                break;
            case R.drawable.listview_error_touch:
                new AsyncTask<String, Void, String>() {
                    @Override
                    protected String doInBackground(String... params) {
                        for (int i = 0; i < 1; i++) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Thread.interrupted();
                            }
                        }
                        return "Complete";
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        if (result.equals("Complete")) {
                            drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.listview_added_touch);
                            notifyDataSetChanged();
                        } else {
                            drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.listview_error_touch);
                            Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
                break;
        }
    }*/

    protected void onMemberClick2(final int position, final List<Food> followerList,int actionDrawableId) {
        final Food follower = followerList.get(position);
        switch (actionDrawableId) {
            case R.drawable.listview_add_touch:
                            //Toast.makeText(mContext, follower.getName(), Toast.LENGTH_SHORT).show();
                            /*drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.listview_added_touch);*/
                            nameDrawableList.put(follower.getName(), R.drawable.listview_added_touch);
                            notifyDataSetChanged();
                break;

            case R.drawable.listview_added_touch:
                            /*drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.listview_add_touch);*/
                            nameDrawableList.put(follower.getName(),  R.drawable.listview_add_touch);
                            notifyDataSetChanged();
                break;
            case R.drawable.listview_error_touch:
                            /*drawableLinkedList.remove(position);
                            drawableLinkedList.add(position, R.drawable.listview_added_touch);*/
                            nameDrawableList.put(follower.getName(),  R.drawable.listview_added_touch);
                            notifyDataSetChanged();
            case R.drawable.ic_action_delete:

                try {
                    alertDialogDelete(position);
                } catch (Exception ex) {
                    Toast.makeText(mContext, ex.toString(), Toast.LENGTH_SHORT).show();
                }


               /* RealmController.deleteFoodOrDrink(realm, mContext, mFoodList.get(position).getName());
                drawableLinkedList.remove(position);
                mFoodList.remove(position);
                notifyDataSetChanged();*/
                break;
            default:
                /*drawableLinkedList.remove(position);
                drawableLinkedList.add(position, R.drawable.listview_error_touch);*/
                nameDrawableList.put(follower.getName(),  R.drawable.listview_error_touch);
                notifyDataSetChanged();
                break;
        }
    }

    private void alertDialogDelete(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Delete item ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        try {
                           // RealmController.deleteFoodOrDrink(realm, mContext, mFoodList.get(position).getName());

                            String objectName = mFoodList.get(position).getName();
                            realm.beginTransaction();
                            mFoodList.get(position).deleteFromRealm();
                            realm.commitTransaction();

                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, mFoodList.size());
                            nameDrawableList.remove(objectName);
                            //notifyDataSetChanged();

                            // THIS IS WORKING
                           /* LinkedList<Integer> drawableLinkedListCopy = new LinkedList<>(Collections.nCopies(mFoodList.size() - 1,R.drawable.ic_action_delete));
                            List<Food> mFoodListCopy = new ArrayList<>();

                            for (int i=0; i < mFoodList.size(); i++) { //for (int i=0; i < mFoodList.size() - 1; i++)
                                if (i != position) {
                                    mFoodListCopy.add(mFoodList.get(i));
                                }
                            }

                            drawableLinkedList = drawableLinkedListCopy;
                            mFoodList = mFoodListCopy;
                            notifyDataSetChanged();*/


                        } catch (Exception ex) {
                            Toast.makeText(mContext, ex.toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });
        builder.show();
    }

}
