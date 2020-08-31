package com.byteme.greenfoodchallenge;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import GlideCode.GlideApp;
import pledgeCode.FirebaseMethods;
import pledgeCode.MyCallback;
import pledgeCode.Pledge;

public class RestaurantAdapter extends RecyclerView.Adapter< RestaurantViewHolder > {

    private Context mContext;
    private List<Restaurant> displayedRestaurantList;
    private List<Restaurant> restaurantList;
    private boolean delete = false;

    RestaurantAdapter(Context mContext, List<Restaurant> restaurantList) {
        this.mContext = mContext;
        this.restaurantList = restaurantList;
        this.displayedRestaurantList = new ArrayList<>();
        this.displayedRestaurantList.addAll(restaurantList);
    }

    @Override
    public RestaurantViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new RestaurantViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final RestaurantViewHolder holder, int position) {

        String imagePath = displayedRestaurantList.get(position).getFoodImage();
        StorageReference storageReference = FirebaseMethods.getRestaurantImage(imagePath);
        GlideApp.with(holder.itemView)
                .load(storageReference)
                .placeholder(R.drawable.no_image)
                .override(200,200)
                .into(holder.foodImage);

        holder.mealName.setText(displayedRestaurantList.get(holder.getAdapterPosition()).getMealName());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(delete == false)
                {
                    Intent mIntent = new Intent(mContext, RestaurantDetailsActivity.class);
                    mIntent.putExtra("Restaurant", displayedRestaurantList.get(holder.getAdapterPosition()).getRestaurantName());
                    mIntent.putExtra("Location", displayedRestaurantList.get(holder.getAdapterPosition()).getLocation());
                    mIntent.putExtra("Protein", displayedRestaurantList.get(holder.getAdapterPosition()).getProtein());
                    mIntent.putExtra("Description", displayedRestaurantList.get(holder.getAdapterPosition()).getDescription());
                    mIntent.putExtra("Meal", displayedRestaurantList.get(holder.getAdapterPosition()).getMealName());
                    mIntent.putExtra("Image", displayedRestaurantList.get(holder.getAdapterPosition()).getFoodImage());
                    mContext.startActivity(mIntent);
                }
                else{

                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                    alert.setTitle("Delete Restaurant Guide");
                    alert.setMessage("Are you sure you want to delete your Restaurant Guide? Doing so will remove your record from the community page.");
                    alert.setIconAttribute(android.R.attr.alertDialogIcon);

                    alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //delete from the adapter and firebase;
                            int position = holder.getAdapterPosition();
                            Restaurant restaurant = displayedRestaurantList.get(position);
                            FirebaseMethods.deleteRestaurant(restaurant);
                            displayedRestaurantList.remove(restaurant);

                            //remove from original list in activity;
                            Restaurant orginalRestaurant = restaurantList.get(position);
                            restaurantList.remove(orginalRestaurant);
                            notifyDataSetChanged();
                        }});
                    alert.setNegativeButton(android.R.string.no, null).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return displayedRestaurantList.size();
    }

    public void toggleDelete(){
        delete = delete == false;
    }

    public List<Restaurant> getRestaurantList(){
        return restaurantList;
    }
    //clear list and set new list.
    public void setFilteredList(List<Restaurant> list){
        displayedRestaurantList.clear();
        displayedRestaurantList.addAll(list);
    }

}

class RestaurantViewHolder extends RecyclerView.ViewHolder {

    ImageView foodImage;
    CardView mCardView;
    TextView mealName;

    RestaurantViewHolder(View itemView) {
        super(itemView);

        mCardView = itemView.findViewById(R.id.cardview);
        foodImage = itemView.findViewById(R.id.foodImage);
        mealName = itemView.findViewById(R.id.mealName);
    }
}
