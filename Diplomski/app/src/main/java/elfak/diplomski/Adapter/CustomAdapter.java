package elfak.diplomski.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import elfak.diplomski.AdminActivity;
import elfak.diplomski.MainActivity;
import elfak.diplomski.Model.Food;
import elfak.diplomski.Model.MenuCategory;
import elfak.diplomski.OrderingActivity;
import elfak.diplomski.R;
import elfak.diplomski.Realm.RealmController;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncUser;


/**
 * Created by Neca on 7.11.2017..
 */

public class CustomAdapter extends BaseAdapter {

    //**String [] result;
    Context context;
    private volatile Realm realm;
    //int [] imageId;
    //**Bitmap[] bitmaps;
    //***private List<MenuCategory> mFoodList = new ArrayList<>();
    private RealmResults<MenuCategory> mFoodList;

    private static LayoutInflater inflater = null;

    //private boolean adminFlag = true;

    public CustomAdapter(MainActivity mainActivity, RealmResults<MenuCategory> menuCategories, Realm realm) {
        // TODO Auto-generated constructor stub
        //**result = NameList;
        context = mainActivity;
        //imageId = Images;
        //**bitmaps = Images;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mFoodList = menuCategories;
        this.realm = realm;

        mFoodList.addChangeListener(new RealmChangeListener<RealmResults<MenuCategory>>() {
            @Override
            public void onChange(RealmResults<MenuCategory> menuCategories) {
                notifyDataSetChanged();
            }
        });
    }

    /*@Override
    public int getCount() {
        return result.length;
    }*/

    @Override
    public int getCount() {
        return mFoodList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView textView;
        ImageView imageView;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        //ViewHolder holder;
        View rowView;


        rowView = inflater.inflate(R.layout.sample_gridlayout, null);

        try {
            holder.textView =(TextView) rowView.findViewById(R.id.texts);
            holder.imageView =(ImageView) rowView.findViewById(R.id.menu_img);
            //Picasso.with(context).load(bitmaps[position]) //Bitmap.createScaledBitmap(nameYourBitmap, newWidth, newHeight, true);
            //***holder.textView.setText(result[position]);
            holder.textView.setText(mFoodList.get(position).getCategory());
            //holder.imageView.setImageResource(imageId[position]);
            //holder.imageView.setImageBitmap(bitmaps[position]);
            int newWidth = rowView.getResources().getDisplayMetrics().widthPixels;
            //int newHeight = (newWidth*bitmaps[position].getHeight())/bitmaps[position].getWidth();
            int newHeight = rowView.getResources().getDisplayMetrics().heightPixels;
            //holder.imageView.setImageBitmap(bitmaps[position]);
            //****holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmaps[position], (int)(newWidth * 0.4), (int)(newHeight * 0.2), true));
            holder.imageView.setImageBitmap(Bitmap.createScaledBitmap(byteToBitmap(mFoodList.get(position).getImage()), (int)(newWidth * 0.4), (int)(newHeight * 0.2), true));
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
        }



        // Event for click on items in gridView
        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //**Toast.makeText(context, "You Clicked "+(mFoodList.get(position).getCategory()), Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_SHORT).show();
                //Bundle bundle = new Bundle();
                //bundle.putParcelable("syncUser", mFoodList.get(position));
                Intent intent = new Intent(context, OrderingActivity.class);
                intent.putExtra("category", mFoodList.get(position).getCategory());
                intent.putExtra("shop_btn", "false");
                context.startActivity(intent);
            }
        });



        //if (MainActivity.adminFlag) {
        // If is admin then set this event
        rowView.setLongClickable(true);
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //Toast.makeText(context, "LONG CLICK", Toast.LENGTH_SHORT).show();
                if (MainActivity.adminFlag) {
                    alertDialogDelete(mFoodList.get(position).getCategory(), position);
                }

                return true; //false;
            }
        });
        // }


        return rowView;
    }

    private Bitmap byteToBitmap(byte[] bytes) {
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return bitmap;
        } catch (Exception ex) {
            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
            //ex.getMessage();
            return null;
        }
    }

    private void alertDialogDelete(final String category, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Delete or edit this category and all items in that category ?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        try {
                            /*final SyncConfiguration syncConfiguration = new SyncConfiguration.Builder(SyncUser.currentUser(), RealmController.REALM_URL_ROS).build();
                            Realm.setDefaultConfiguration(syncConfiguration);
                            realm = Realm.getDefaultInstance();*/

                            // THIS IS WORKING
                            //RealmController.deleteCategoryAndAllItems(realm, context, category);

                            // Test
                            //RealmController.deleteCategoryAndAllItems(realm, context, category);
                            realm.beginTransaction();
                            mFoodList.get(position).deleteFromRealm();
                            realm.commitTransaction();
                            notifyDataSetChanged();
                            RealmController.deleteAllFoodWithNullCategory(realm, context); // cascade delete


                            //This is working
                            // Making new list of category without deleted category
                            /*List<MenuCategory> menuCategories = new ArrayList<>();

                            for (int i = 0; i < mFoodList.size(); i++) {
                                if (i != position) {
                                    menuCategories.add(mFoodList.get(i));
                                }
                            }

                            mFoodList = menuCategories;
                            notifyDataSetChanged();*/


                        } catch (Exception ex) {
                            Toast.makeText(context, ex.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                })
                .setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, AdminActivity.class);
                        Bitmap bitmap = byteToBitmap(mFoodList.get(position).getImage());
                        intent.putExtra("BitmapImage", bitmap);
                        intent.putExtra("categoryName", mFoodList.get(position).getCategory());
                        intent.putExtra("sortNumber", mFoodList.get(position).getSortNumber());
                        context.startActivity(intent);
                    }
                });
        builder.show();
    }

}
