package com.clancy.conor.photobucketapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int mTempCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FirebaseFirestore dbPhotoBucket = FirebaseFirestore.getInstance();

        RecyclerView recyclerView=findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        PhotoBucketAdapter photoBucketAdapter = new PhotoBucketAdapter();
        recyclerView.setAdapter(photoBucketAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog();
                Snackbar.make(view, "Adding item to Firebase!", Snackbar.LENGTH_LONG)
                        .setAction("Firebase", null).show();

                /*
                //Create a new mq with a first and lastname
                Map<String,Object> mq =new HashMap<>();
                mTempCounter = mTempCounter + 1;
                mq.put(Constants.KEY_CAPTION, "Caption#" +mTempCounter);
                mq.put(Constants.KEY_IMAGE_URL, "ImageURL#" +mTempCounter);

                dbPhotoBucket.collection(Constants.COLLECTION_PATH).add(mq)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(Constants.TAG,"DocumentSnapshot added with ID: " + mTempCounter);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(Constants.TAG, "Error adding document", e);
                            }
                        });
                */

            }
        });
    }

    private void showAddDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.photobucket_dialog, null, false);
        builder.setView(view);

        final TextView captionEditText = view.findViewById(R.id.dialog_caption_edittext);
        final TextView imageUrlEditText = view.findViewById(R.id.dialog_url_edittext);

        builder.setTitle("Add this Picture!");

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                Map<String, Object> mq = new HashMap<>();

                mq.put(Constants.KEY_CAPTION, captionEditText.getText().toString());
                mq.put(Constants.KEY_IMAGE_URL, imageUrlEditText.getText().toString());
                mq.put(Constants.KEY_CREATED, new Date());

                FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PATH).add(mq);

            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {


        return true;
    }
}
