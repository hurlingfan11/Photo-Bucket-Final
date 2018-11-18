package com.clancy.conor.photobucketapp;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class PhotoBucketAdapter extends RecyclerView.Adapter<PhotoBucketAdapter.PhotoBucketViewHolder> {

    private List<DocumentSnapshot> mPhotoBucketSnapShots = new ArrayList<>();

    public PhotoBucketAdapter(){

        CollectionReference pbRef = FirebaseFirestore.getInstance().collection(Constants.COLLECTION_PATH);
        pbRef.orderBy(Constants.KEY_CAPTION, Query.Direction.DESCENDING).limit(50).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if(e!=null){
                    Log.w(Constants.TAG, "Listening Failed");
                    return;
                }

                mPhotoBucketSnapShots = queryDocumentSnapshots.getDocuments();
                notifyDataSetChanged();// Whenever it changes will notify the adapater

            }
        });
    }


    @NonNull
    @Override
    public PhotoBucketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photobucket_itemview, parent, false);
        return new PhotoBucketViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoBucketViewHolder photoBucketViewHolder, int i) {
        DocumentSnapshot ds = mPhotoBucketSnapShots .get(i);
        String caption = (String) ds.get(Constants.KEY_CAPTION);
        photoBucketViewHolder.mPhotoBucketTextView.setText(caption);

    }

    @Override
    public int getItemCount() {

        return mPhotoBucketSnapShots .size();
    }

    class PhotoBucketViewHolder extends RecyclerView.ViewHolder{

        private TextView mPhotoBucketTextView;


        public PhotoBucketViewHolder(@NonNull View itemView) {
            super(itemView);

            mPhotoBucketTextView = itemView.findViewById(R.id.itemview_caption);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DocumentSnapshot ds = mPhotoBucketSnapShots.get(getAdapterPosition());
                    Context c = v.getContext();
                    Intent intent = new Intent(c, PhotoBucketActivityDetail.class);

                    intent.putExtra(Constants.EXTRA_DOC_ID, ds.getId());

                    c.startActivity(intent);

                }
            });
        }
    }

}
