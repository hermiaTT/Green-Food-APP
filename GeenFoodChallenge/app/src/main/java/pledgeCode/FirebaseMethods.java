package pledgeCode;


import android.support.annotation.NonNull;

import com.byteme.greenfoodchallenge.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;


/*
 Some methods that might be useful
 */
public class FirebaseMethods {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference databaseReference = database.getReference();
    private static DatabaseReference pledgeReference = databaseReference.child("Pledges");
    private static DatabaseReference restaurantReference = databaseReference.child("Restaurants");

    public static FirebaseUser getFireBaseCurrentUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        return mAuth.getCurrentUser();
    }

    public static void savePledge(Pledge pledge) {
        String userId = getFireBaseCurrentUser().getUid();
        pledgeReference.child(userId).setValue(pledge);
    }

    public static void updateCo2Pledge(String newCo2Pledged) {
        String userId = getFireBaseCurrentUser().getUid();
        pledgeReference.child(userId).child("co2Pledged").setValue(newCo2Pledged);
    }

    public static void deleteMyPledge(Pledge pledge) {
        //To Do
        String userId = getFireBaseCurrentUser().getUid();
        pledgeReference.child(userId).removeValue();
    }

    public static void getMyPledge(final MyCallback<Pledge> myCallback, String uid) {
        pledgeReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Pledge> pledgeList = new ArrayList<>();
                Pledge pledge = dataSnapshot.getValue(Pledge.class); //get pledge and put into list
                pledgeList.add(pledge);
                myCallback.onCallback(pledgeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //sort smallest to largest
    public static void getSortedCo2Pledges(final MyCallback<Pledge> myCallback) {

        //final Query queryRef = pledgeReference.orderByChild("co2Pledged").limitToLast(20);
        final Query queryRef = pledgeReference.orderByChild("co2Pledged");
        queryRef.addValueEventListener(new ValueEventListener() {
            final ArrayList<Pledge> pledgeList = new ArrayList<>();
            @Override
            public void onDataChange(@NonNull  DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Pledge pledge = snapshot.getValue(Pledge.class);
                    pledgeList.add(pledge);
                }
                queryRef.removeEventListener(this);
                Collections.reverse(pledgeList);
                myCallback.onCallback(pledgeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //get total pledges
    public static void getTotalPledges(final CallBackValue callBackValue){
        databaseReference.child("GlobalData").child("NumberOfPledges").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalPledges = dataSnapshot.getValue(Integer.class); //get pledge and put into list
                callBackValue.onCallback(totalPledges);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //get total co2
    public static void getTotalCo2(final CallBackValue callBackValue){
        databaseReference.child("GlobalData").child("TotalCo2Pledged").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalCo2 = dataSnapshot.getValue(Integer.class); //get pledge and put into list
                callBackValue.onCallback(totalCo2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    //restaurant stuff
    public static void saveRestaurant(Restaurant restaurant) {
        String userId = getFireBaseCurrentUser().getUid();
        DatabaseReference pushedRestaurantRef = restaurantReference.child(userId).push();
        String id = pushedRestaurantRef.getKey();
        restaurantReference.child(userId).child(id).setValue(restaurant);

    }

    //remove restaurant by looking up the image and finding a match
    public static void deleteRestaurant(Restaurant restaurant){
        String userId = getFireBaseCurrentUser().getUid();
        final Query query = restaurantReference.child(userId).orderByChild("foodImage").equalTo(restaurant.getFoodImage());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot restaurantSnapshot : dataSnapshot.getChildren()){
                    Restaurant restaurant = restaurantSnapshot.getValue(Restaurant.class);
                    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
                    StorageReference imageRef = firebaseStorage.getReference().child(restaurant.getFoodImage());
                    imageRef.delete(); // may want to add a success or failure listener
                    restaurantSnapshot.getRef().removeValue();
                }
                query.removeEventListener(this);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //get restaurants from user
    public static void getUserRestaurants(final MyCallback<Restaurant> myCallback) {
        final ArrayList<Restaurant> restaurantList = new ArrayList<>();
        String userId = getFireBaseCurrentUser().getUid();
        final Query query = restaurantReference.child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot restaurantSnapshot : dataSnapshot.getChildren()){
                    Restaurant restaurant = restaurantSnapshot.getValue(Restaurant.class);
                    restaurantList.add(restaurant);
                }
                query.removeEventListener(this);
                myCallback.onCallback(restaurantList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //get all restaurant
    public static void getAllRestaurant(final MyCallback<Restaurant> myCallback) {
        final ArrayList<Restaurant> restaurantList = new ArrayList<>();
        final Query query = restaurantReference;
        query.addValueEventListener((new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userChildSnapshot : dataSnapshot.getChildren()) {
                            for(DataSnapshot restaurantSnapshot : userChildSnapshot.getChildren()) {
                                Restaurant restaurant = restaurantSnapshot.getValue(Restaurant.class);
                                restaurantList.add(restaurant);
                            }
                        }

                        query.removeEventListener(this);
                        Collections.sort(restaurantList);
                        myCallback.onCallback(restaurantList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                })
        );

    }

    //get restaurant Image
    public static StorageReference getRestaurantImage(String path){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        return storageReference.child(path);
        /*
        * see restaurant adapter for glide part*/
    }
}

