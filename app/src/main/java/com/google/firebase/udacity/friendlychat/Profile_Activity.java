package com.google.firebase.udacity.friendlychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Profile_Activity extends AppCompatActivity {
    private FirebaseFirestore mFirebaseFirestore;
    private static final String TAG = "Profile_Activity";
    private String mUsername;
    TextView name;
    Map<String, Object> profile_data = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mUsername= intent.getStringExtra("key");
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_profile_);
        getData();
        addSnapshot();
        name = (TextView) findViewById(R.id.name);

    }

    private void getData(){
        DocumentReference getProfile = mFirebaseFirestore.collection("users").document(mUsername);
        getProfile.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Toast.makeText(getApplicationContext(),"Your data is back",Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        profile_data = document.getData();
                        name.setText("Welcome \n"+profile_data.get("first").toString());

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    private  void addSnapshot(){
        final DocumentReference docRef = mFirebaseFirestore.collection("users").document(mUsername);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    name.setText("Welcome \n"+snapshot.get("first").toString());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }
}
