package com.clancy.conor.photobucketapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PhotoBucketActivityDetail extends AppCompatActivity {

    private ImageView mPhotoBucketImageView;
    private TextView mPhotoBucketTextView;
    private DocumentReference mDocRef;
    private DocumentSnapshot mDocSnapShot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_bucket_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mPhotoBucketTextView = findViewById(R.id.detail_text_view_caption);
        mPhotoBucketImageView = findViewById(R.id.detail_image_view);


        Intent receivedIntent = getIntent();
        String docId = receivedIntent.getStringExtra(Constants.EXTRA_DOC_ID);

        // Temporary Test
        // mPhotoBucketTextView.setText(docId);

        mDocRef = FirebaseFirestore.getInstance().
                collection(Constants.COLLECTION_PATH).document(docId);

        mDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(Constants.TAG, "listen failed");
                }
                if (documentSnapshot.exists()) {
                    mDocSnapShot = documentSnapshot; //Save document snapshot
                    mPhotoBucketTextView.setText((String) documentSnapshot.get(Constants.KEY_CAPTION));
                    //mPhotoBucketImageView.setImageResource((String)documentSnapshot.get(Constants.KEY_MOVIE));
                }
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showEditDialog();
                Snackbar.make(view, "Editing Successful", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View view = getLayoutInflater().inflate(R.layout.photobucket_dialog, null, false);
        builder.setView(view);

        final TextView captionEditText = view.findViewById(R.id.dialog_caption_edittext);
        final TextView urlEditText = view.findViewById(R.id.dialog_url_edittext);

        captionEditText.setText((String) mDocSnapShot.get(Constants.KEY_CAPTION));
        urlEditText.setText((String) mDocSnapShot.get(Constants.KEY_IMAGE_URL));

        builder.setTitle("Edit Mode!");

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                Map<String, Object> mq = new HashMap<>();

                mq.put(Constants.KEY_CAPTION, captionEditText.getText().toString());
                mq.put(Constants.KEY_IMAGE_URL, urlEditText.getText().toString());
                mq.put(Constants.KEY_CREATED, new Date());

                mDocRef.update(mq);

            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);

        builder.create().show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_delete:
                mDocRef.delete();
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

}