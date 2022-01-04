package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {

    ImageView mCommentImage;
    TextView mDescription, fName, lName, nDatePost;
    CircleImageView profilePhotoComment;

    RecyclerView mRecyclerViewComment;
    FirestoreRecyclerOptions<PostComment> options;
    FirestoreRecyclerAdapter<PostComment, myViewHolderComment> mCommentAdapter;


    String firebaseUser = FirebaseAuth.getInstance().getUid();
    String POST_COMMENTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);


        mCommentImage = findViewById(R.id.IMG_comment);
        mDescription = findViewById(R.id.TV_description_comment);
        profilePhotoComment = findViewById(R.id.Comment_profile_image);
        fName = findViewById(R.id.TV_Comment_fName);
        lName = findViewById(R.id.TV_Comment_lName);
        nDatePost = findViewById(R.id.TV_Comment_post_date);
        mRecyclerViewComment = findViewById(R.id.RecyclerView_Comment);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentActivity.this);
        mRecyclerViewComment.setLayoutManager(linearLayoutManager);
        mRecyclerViewComment.setHasFixedSize(false);

        getDetails();

        DisplayComments();
    }

    private void DisplayComments() {
        Query Q1 = FirebaseFirestore.getInstance().collectionGroup("POST COMMENTS");

        options = new FirestoreRecyclerOptions.Builder<PostComment>()
                .setQuery(Q1, PostComment.class)
                .build();

        mCommentAdapter = new FirestoreRecyclerAdapter<PostComment, myViewHolderComment>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myViewHolderComment holder, int position, @NonNull PostComment model) {


                if (model.getCommentPublisher().equals(firebaseUser)) {
                    String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    POST_COMMENTS = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    CollectionReference colRef = FirebaseFirestore.getInstance()
                            .collection("REGISTERED USERS").document(USER_ID)
                            .collection("POST COMMENTS");

                    Query q1 = colRef.whereEqualTo("commentPosition", position);

                    q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            boolean isExisting = false;
                            if(!queryDocumentSnapshots.isEmpty()){
                                int position = holder.getAdapterPosition();
                                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                        int CommentPosition;

                                        CommentPosition = Objects.requireNonNull(ds.getLong("commentPosition")).intValue();
                                        for(int i = queryDocumentSnapshots.size(); i<= queryDocumentSnapshots.size(); i++){
                                        if (CommentPosition == position) {
                                            isExisting = true;
                                            holder.itemView.setVisibility(View.VISIBLE);
                                            holder.userPostDescription.setText(model.getCommentDesc());

                                        }
                                    }
                                }


                                if (!isExisting) {
                                    // TODO:  blah blah Smile

                                }
                            }else{
                                holder.itemView.setVisibility(View.GONE);
                                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                                params.height = 0;
                                params.width = 0;
                                holder.itemView.setLayoutParams(params);
                            }

                        }
                    });

                }else {

                    String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    POST_COMMENTS = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    CollectionReference colRef = FirebaseFirestore.getInstance()
                            .collection("REGISTERED USERS").document(USER_ID)
                            .collection("POST COMMENTS");

                    Query q1 = colRef.whereEqualTo("commentPosition", position);

                    q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            boolean isExisting = false;
                            if(!queryDocumentSnapshots.isEmpty()){
                                int position = holder.getAdapterPosition();

                                for (DocumentSnapshot ds : queryDocumentSnapshots) {
                                    int CommentPosition;
                                    for(int i = queryDocumentSnapshots.size(); i<= queryDocumentSnapshots.size(); i++) {
                                        CommentPosition = Objects.requireNonNull(ds.getLong("commentPosition")).intValue();
                                        if (CommentPosition == position) {

                                            isExisting = true;
                                            holder.itemView.setVisibility(View.VISIBLE);
                                            holder.userPostDescription.setText(model.getCommentDesc());
                                        }
                                    }
                                }

                                if (!isExisting) {
                                    // TODO:  blah blah Smile

                                }
                            }else{
                                holder.itemView.setVisibility(View.GONE);
                                ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
                                params.height = 0;
                                params.width = 0;
                                holder.itemView.setLayoutParams(params);
                            }
                        }
                    });
                }
            }

            @NonNull
            @Override
            public myViewHolderComment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false);
                return new myViewHolderComment(v);
            }
        };

        mRecyclerViewComment.setAdapter(mCommentAdapter);
    }


    private void getDetails() {
        Intent intent = getIntent();
        NewsFeed_Model newsFeedModel = intent.getParcelableExtra("newsFeedGetItems");

        String imageRes = newsFeedModel.getmNewsFeed_photoPost();
        String description = newsFeedModel.getNewsFeed_Desc();
        String datePost = newsFeedModel.getmPostDate();

        Picasso.get()
                .load(imageRes)
                .into(mCommentImage);
        mDescription.setText(description);
        nDatePost.setText(datePost);
        publishersInfo(profilePhotoComment, fName, lName, newsFeedModel.getPostID());

    }


    //publish first info
    private void publishersInfo(ImageView profileImage, TextView firstName, TextView lastName, String USER_ID) {

        DocumentReference docRef = FirebaseFirestore.getInstance()
                .collection("REGISTERED USERS").document(USER_ID);

        docRef.getParent().document(USER_ID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                assert value != null;
                User_Model user_model = value.toObject(User_Model.class);
                assert user_model != null;
                firstName.setText(user_model.getmFirstName());
                lastName.setText(user_model.getmLastName());
                Glide.with(CommentActivity.this).load(user_model.getmPhotoUrl()).into(profileImage);

            }
        });


    }


    private static class myViewHolderComment extends RecyclerView.ViewHolder {
        CircleImageView postUserImage;
        TextView userPostDescription;

        public myViewHolderComment(@NonNull View itemView) {
            super(itemView);

            postUserImage = itemView.findViewById(R.id.displayComment_profile_image_first);
            userPostDescription = itemView.findViewById(R.id.TV_Display_comment);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCommentAdapter.stopListening();

    }

    @Override
    protected void onResume() {
        super.onResume();

        mCommentAdapter.startListening();
    }
}