package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsFeedActivity extends AppCompatActivity implements MainNavAdapter.onItemClickListener {



    ImageView btMenu, btProfile, btSetting;
    DrawerLayout mDrawerLayout;
    RecyclerView mRecyclerView;
    static ArrayList<NavBarItems> drawerArrayList = new ArrayList<>();
    MainNavAdapter adapter;

    Dialog dialog, dialog_2, dialog_three;
    TextView mAddPost, mBtnDialog_Post, mAboutHA, mmDialogTitle,
            mmDialogMessage, mmCancelDialog_btn, mmProceedDialog_btn;
    ImageView mCloseButton, mImage_Container;
    LinearLayout mBtnCall_PostCamera, mDialog_DisplayTextView, mLinearAboutHA;
    EditText mDialog_PostDescription;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    private StorageReference mStorageRef;
    private StorageTask mUploadTask;
    String USER_ID;
    String FOLLOW_USERS;
    String NEWSFEED;
    String USER_LIKED;
    String USER_HEARTLIKED;
    String POST_COMMENTS;
    LinearLayout mNewsFeedPost_Btn;
    String firebaseUser = FirebaseAuth.getInstance().getUid();
    String saveCurrentDayTime ;

    int i = 0;
    CircleImageView pictureView;
    int TotalLikeCount = 0;

    int liked_Post_Position ;
    int Liked_heart_position;

    public static void closeDrawer(DrawerLayout mDrawerLayout) {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    //post News feed
    RecyclerView mNewsFeed_RecyclerView;

  FirestoreRecyclerOptions<NewsFeed_Model> options;
  FirestoreRecyclerAdapter<NewsFeed_Model, myViewHolderPost> mPostAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.Menu_bar_btn);
        btSetting = findViewById(R.id.Settings_Menu_bar_btn);
        btProfile = findViewById(R.id.Profile_Menu_bar_btn);
        mRecyclerView = findViewById(R.id.recycler_view);
        pictureView = findViewById(R.id.newsFeed_profile_image_first);

        mAboutHA = findViewById(R.id.TV_menu_about_HA);

        mNewsFeedPost_Btn = findViewById(R.id.Linear_postImage_newsFeed);

        //TODO: add post to activity
        mAddPost = findViewById(R.id.TV_add_post);

        //TODO: newsfeed post recyclerView
        mNewsFeed_RecyclerView = findViewById(R.id.RecyclerView_Post_NewsFeed);


        mStorageRef = FirebaseStorage.getInstance().getReference("NewsFeedPhotos");
        //TODO: clear array list
        drawerArrayList.clear();

        drawerArrayList.add(new NavBarItems(R.drawable.newsfeednew, "News Feed"));
        drawerArrayList.add(new NavBarItems(R.drawable.usersfollow, "Follow Users"));
        drawerArrayList.add(new NavBarItems(R.drawable.running, "Step Counter"));
        drawerArrayList.add(new NavBarItems(R.drawable.weightloss, "BMI Calculator"));
        drawerArrayList.add(new NavBarItems(R.drawable.preventioncovid, "Covid Info"));
        drawerArrayList.add(new NavBarItems(R.drawable.shoppingnewchange, "Extra(Recruiters)"));

        //TODO: initialize adapter
        adapter = new MainNavAdapter(drawerArrayList, this, NewsFeedActivity.this);

        //TODO: set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //TODO:set adapter
        mRecyclerView.setAdapter(adapter);

        //TODO:open drawer
        openNavDrawer();

        //TODO:navigate profile screen
        navigateProfile();

        //TODO:navigate setting screen
        navigateSetting();

        //TODO: from add a post
        addPost_to_newsFeed();

        //TODO:from add photo & video
        addPost_MergedButton();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewsFeedActivity.this);
        mNewsFeed_RecyclerView.setLayoutManager(linearLayoutManager);
        mNewsFeed_RecyclerView.setHasFixedSize(false);

        displayFirst_ProfilePic();


        //TODO: about App dialog box
        displayAboutApp();


    displayRecyclerView();

    }

    public void displayRecyclerView() {

        Query Q1 = FirebaseFirestore.getInstance().collectionGroup("NEWSFEED POST");

        options = new FirestoreRecyclerOptions.Builder<NewsFeed_Model>()
                .setQuery(Q1, NewsFeed_Model.class)
                .build();

        mPostAdapter = new FirestoreRecyclerAdapter<NewsFeed_Model, myViewHolderPost>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myViewHolderPost holder, int position, @NonNull NewsFeed_Model model) {


                if(position != RecyclerView.NO_POSITION){
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    assert firebaseUser != null;
                    if (model.getPostID().equals(firebaseUser.getUid())) {

                        holder.itemView.setVisibility(View.VISIBLE);

                        holder.mPostDate.setText(model.mPostDate);
                        holder.mNewFeedDesc.setText(model.getNewsFeed_Desc());
                        holder.mImageComment.setVisibility(View.VISIBLE);
                        holder.mCommentText.setVisibility(View.VISIBLE);
                        holder.mThreeDotImage.setVisibility(View.VISIBLE);
                        Picasso.get()
                                .load(model.getmNewsFeed_photoPost())
                                .into(holder.mMainImageNewsFeed);

                        publishersInfo(holder.ImageProfile, holder.fName, holder.lName, model.getPostID());

                    }else {

                        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        FOLLOW_USERS = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        CollectionReference colRef = FirebaseFirestore.getInstance()
                                .collection("REGISTERED USERS").document(USER_ID)
                                .collection("FOLLOWING");

                        Query q1 = colRef
                                .whereEqualTo("following user", model.getPostID());

                        q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"})
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                boolean isExisting = false;
                                for (DocumentSnapshot ds : queryDocumentSnapshots) {

                                    String userID;
                                    userID = ds.getString("following user");

                                    assert userID != null;
                                    if (userID.equals(model.getPostID())) {

                                        isExisting = true;
                                        holder.itemView.setVisibility(View.VISIBLE);

                                        holder.mPostDate.setText(model.mPostDate);
                                        holder.mNewFeedDesc.setText(model.getNewsFeed_Desc());
                                        holder.mImageComment.setVisibility(View.VISIBLE);
                                        holder.mCommentText.setVisibility(View.VISIBLE);
                                        holder.mThreeDotImage.setVisibility(View.VISIBLE);
                                        Picasso.get()
                                                .load(model.getmNewsFeed_photoPost())
                                                .into(holder.mMainImageNewsFeed);

                                        publishersInfo(holder.ImageProfile, holder.fName, holder.lName, model.getPostID());



                                    }
                                }



                                if (!isExisting) {
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
                //check comment count
                checkNumberOfCommentCount(model, position, holder.mCommentCount);

                //check if cardView is heart lied by user
                checkIsHeartLikedPosition(holder.mImageLoveLike, model, position, holder.mHeartCount);

                //check if cardView is liked by user
                checkIsPostLikedPosition(holder.mImageHandLike, model, position, holder.mLikeCount);


                holder.mImageHandLike.setTag("likeRemove");
                holder.mImageHandLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();

                        if (holder.mImageHandLike.getTag().equals("likeRemove")) {

                            addUser_LikeToFirestore(model, holder.mImageHandLike, position, holder.mLikeCount);

                        } else if (holder.mImageHandLike.getTag().equals("liked")) {
                            deleteUser_LikeToFirestore(model, holder.mImageHandLike, position, holder.mLikeCount);
                        }
                    }
                });


                holder.mImageLoveLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();

                        if (holder.mImageLoveLike.getTag().equals("addHeart")) {

                            addUser_heartToFirestore(model, holder.mImageLoveLike, position, holder.mHeartCount);

                        } else if (holder.mImageLoveLike.getTag().equals("removeHeart")) {
                            deleteUser_HeartFromFirestore(model, holder.mImageLoveLike, position, holder.mHeartCount);
                        }
                    }
                });



                holder.mImageComment.setTag("addComment");
                //for Image Comment when pressed by users => starts here
                holder.mImageComment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                            if (holder.mImageComment.getTag().equals("addComment")) {
                                addUser_CommentToFirestore(model, holder.mCommentCount, position);
                            }
                    }
                });


            }

            @NonNull
            @Override
            public myViewHolderPost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_feed, parent, false);
                return new myViewHolderPost(view);
            }
        };

        mNewsFeed_RecyclerView.setAdapter(mPostAdapter);


    }


    //db check likes for each user
    private void checkIsPostLikedPosition(ImageView mImageHandLike, NewsFeed_Model model, int position, TextView mLikeCount) {

        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            USER_LIKED = FirebaseAuth.getInstance().getCurrentUser().getUid();

            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("LIKED POST");

            Query q1 = colRef.whereEqualTo("liked Position", position);
            q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    boolean isExisting = false;
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        int LikedPostPosition;

                        LikedPostPosition = Objects.requireNonNull(ds.getLong("liked Position")).intValue();
                        for (i = queryDocumentSnapshots.size(); i <=queryDocumentSnapshots.size();
                             ++i) {
                            if (LikedPostPosition == position) {
                                isExisting = true;
                                mImageHandLike.setImageResource(R.drawable.postliked);
                                mImageHandLike.setTag("liked");
                                mLikeCount.setText( i + " " + "likes");
                            }
                        }

                    }

                    if (!isExisting) {
                        mImageHandLike.setImageResource(R.drawable.handlike);
                        mImageHandLike.setTag("likeRemove");
                        mLikeCount.setText(TotalLikeCount + " " + "Like");
                    }
                }
            });
        }
    }


    @SuppressLint("UseCompatLoadingForDrawables")
    private void addUser_CommentToFirestore(NewsFeed_Model model, TextView mCommentCount, int position) {
        dialog_2 = new Dialog(NewsFeedActivity.this);
        dialog_2.setContentView(R.layout.dialog_custom_comment);
        dialog_2.setCancelable(false);
        dialog_2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog_2.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mCloseButton = dialog_2.findViewById(R.id.ImageView_closeButton_Comment_post);
        mBtnDialog_Post = dialog_2.findViewById(R.id.Dialog_Comment_post);
        mDialog_PostDescription = dialog_2.findViewById(R.id.ET_DialogComment_Post_Desc);


        dialog_2.show();

        _cancelDialogCommentPost(mCloseButton);

        _userPost_To_CommentFeed(mBtnDialog_Post, mDialog_PostDescription, mCommentCount, model, position);

    }

    private void _userPost_To_CommentFeed(TextView mBtnDialog_post, EditText mDialog_postDescription, TextView mCommentCount,
                                          NewsFeed_Model model, int position) {

        mBtnDialog_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mDialog_postDescription.getText().toString())) {
                    return;
                }
                if (!TextUtils.isEmpty(mDialog_postDescription.getText().toString())) {
                    postMessageToComment(mBtnDialog_post, mCommentCount, model, position, mDialog_postDescription);
                }
            }
        });
    }

    private void postMessageToComment(TextView mBtnDialog_post, TextView mCommentCount, NewsFeed_Model model, int position,
                                      EditText mDialog_postDescription) {


        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            POST_COMMENTS = FirebaseAuth.getInstance().getUid();

            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("POST COMMENTS");

            PostComment postComment = new PostComment(position, model.getPostID(), mDialog_postDescription.getText().toString(),
                    firebaseUser);



            colRef.add(postComment).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @SuppressLint({"SetTextI18n"})
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(NewsFeedActivity.this, "Comment posted", Toast.LENGTH_SHORT).show();

                    dialog_2.dismiss();
                    CollectionReference colRef= FirebaseFirestore.getInstance()
                            .collection("REGISTERED USERS").document(USER_ID)
                            .collection("POST COMMENTS");

                    Query q2 = colRef.whereEqualTo("commentPosition", position);

                    q2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            if (queryDocumentSnapshots.getDocuments().size() > 0) {

                                int CommentPostPosition;
                                for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {

                                    CommentPostPosition = Objects.requireNonNull(ds.getLong("commentPosition")).intValue();

                                    for (i = queryDocumentSnapshots.size();
                                         i <= queryDocumentSnapshots.size(); i++) {

                                        if (CommentPostPosition == position) {

                                            mCommentCount.setText(queryDocumentSnapshots.size() + " " + "Comments");

                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private void _cancelDialogCommentPost(ImageView mCloseButton) {
        mCloseButton.setOnClickListener(view -> dialog_2.dismiss());
    }

    @SuppressLint("SetTextI18n")
    private void checkNumberOfCommentCount(NewsFeed_Model model, int position, TextView mCommentCount) {

        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            CollectionReference colRef= FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("POST COMMENTS");

            Query q2 = colRef.whereEqualTo("commentPosition", position);
            q2.get().addOnSuccessListener(queryDocumentSnapshots -> {
                boolean isExisting = false;
                if (queryDocumentSnapshots.getDocuments().size() > 0) {

                    int totalCount = 0;
                    int CommentPostPosition;
                    for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {

                        CommentPostPosition = Objects.requireNonNull(ds.getLong("commentPosition")).intValue();

                        if (CommentPostPosition == position ) {
                            isExisting  = true;
                        for (i = queryDocumentSnapshots.size();
                             i <= queryDocumentSnapshots.size(); i++) {

                                mCommentCount.setText(queryDocumentSnapshots.size()  + " " + "Comments");

                            }

                        }

                    }

                }

                if (!isExisting) {
                    mCommentCount.setText(TotalLikeCount + " " + "Comment");

                }



            });
        }

    }



    //check if heart is liked
    private void checkIsHeartLikedPosition(ImageView mImageLoveLike, NewsFeed_Model model, int position, TextView mHeartCount) {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            String USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            USER_HEARTLIKED = FirebaseAuth.getInstance().getCurrentUser().getUid();


            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("HEART_LOVE POST");


            Query q1 = colRef.whereEqualTo("heart Position", position);
            q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    boolean isExisting = false;
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        String heartPostInDB;
                        int heartPostPosition;

                        heartPostInDB = ds.getString("heart Post");
                        heartPostPosition = Objects.requireNonNull(ds.getLong("heart Position")).intValue();

                        assert heartPostInDB != null;

                        for (i = queryDocumentSnapshots.size(); i <=queryDocumentSnapshots.size();
                             ++i) {
                            if (heartPostPosition == position ) {

                                isExisting = true;

                                mImageLoveLike.setImageResource(R.drawable.redheart);
                                mImageLoveLike.setTag("removeHeart");
                                mHeartCount.setText( i + " " + "Hearts");

                            }
                        }

                    }
                    if (!isExisting) {
                        mImageLoveLike.setImageResource(R.drawable.heartlike);
                        mImageLoveLike.setTag("addHeart");
                        mHeartCount.setText(TotalLikeCount + " " + "Hearts");
                    }
                }
            });
        }
    }

    //delete hearts by user
    private void deleteUser_HeartFromFirestore(NewsFeed_Model model, ImageView mImageLoveLike, int position, TextView mHeartCount) {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            USER_HEARTLIKED = FirebaseAuth.getInstance().getUid();

            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("HEART_LOVE POST");


            Query q1 = colRef.whereEqualTo("heart Position", position);
            q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    boolean isExisting = false;
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        String heartPostInDB;
                        int heartPostPosition;

                        heartPostInDB = ds.getString("heart Post");
                        heartPostPosition = Objects.requireNonNull(ds.getLong("heart Position")).intValue();


                        assert heartPostInDB != null;
                        if (heartPostPosition == position) {
                            isExisting = true;

                            model.setPostID(ds.getId());
                            colRef.document(model.getPostID()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                        mImageLoveLike.setImageResource(R.drawable.heartlike);
                                        mImageLoveLike.setTag("addHeart");

                                        Toast.makeText(NewsFeedActivity.this, " heart deleted", Toast.LENGTH_SHORT).show();

                                        mHeartCount.setText(queryDocumentSnapshots.size() - 1 + " " + "Heart");

                                    }
                                }
                            });

                        }

                    }

                    if (!isExisting) {
                        addUser_heartToFirestore(model, mImageLoveLike, position, mHeartCount);
                    }


                }
            });


        }

    }

    //add hearts by user
    private void addUser_heartToFirestore(NewsFeed_Model model, ImageView mImageLoveLike, int position, TextView mHeartCount) {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            USER_HEARTLIKED = FirebaseAuth.getInstance().getCurrentUser().getUid();

            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("HEART_LOVE POST");


            //model.setPostID(firebaseUser);
            String postID = model.PostID;
            Map<String, Object> following = new HashMap<>();
            following.put("heart Post", postID);
            following.put("heart Position", position);
            following.put("heart number", 1);


            colRef.add(following).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @SuppressLint({"SetTextI18n"})
                @Override
                public void onSuccess(DocumentReference documentReference) {

                    Toast.makeText(NewsFeedActivity.this, "Heart liked", Toast.LENGTH_SHORT).show();
                    mImageLoveLike.setImageResource(R.drawable.redheart);
                    mImageLoveLike.setTag("removeHeart");

                    CollectionReference colRef = FirebaseFirestore.getInstance()
                            .collection("REGISTERED USERS").document(USER_ID)
                            .collection("HEART_LOVE POST");

                    Query q2 = colRef.whereEqualTo("heart Position", position).whereEqualTo("heart number", 1);

                    q2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.getDocuments().size() > 0) {

                                for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {

                                    int heartPostPosition;
                                    heartPostPosition = Objects.requireNonNull(ds.getLong("heart Position")).intValue();

                                    for (i = queryDocumentSnapshots.size();
                                         i <= queryDocumentSnapshots.size(); i++) {

                                        if (heartPostPosition == position) {
                                            mHeartCount.setText(queryDocumentSnapshots.size() + " " + "Hearts");
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private void deleteUser_LikeToFirestore(NewsFeed_Model model, ImageView mImageHandLike, int position, TextView mLikeCount) {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            USER_LIKED = FirebaseAuth.getInstance().getUid();

            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("LIKED POST");

            Query q1 = colRef.whereEqualTo("liked Position", position);
            q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    boolean isExisting = false;
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        int LikedPostPosition;
                        LikedPostPosition = Objects.requireNonNull(ds.getLong("liked Position")).intValue();

                        if (LikedPostPosition == position) {
                            isExisting = true;

                            model.setPostID(ds.getId());
                            colRef.document(model.getPostID()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mImageHandLike.setImageResource(R.drawable.handlike);
                                        mImageHandLike.setTag("likeRemove");

                                        mLikeCount.setText(queryDocumentSnapshots.size() - 1 + " " + "like");


                                        Toast.makeText(NewsFeedActivity.this, "Picture liked deleted", Toast.LENGTH_SHORT).show();


                                    }
                                }
                            });


                        }

                    }


                    if (!isExisting) {

                        addUser_LikeToFirestore(model, mImageHandLike, position, mLikeCount);
                    }
                }
            });
        }
    }

    private void addUser_LikeToFirestore(NewsFeed_Model model, ImageView mImageHandLike, int position, TextView mLikeCount) {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            USER_LIKED = FirebaseAuth.getInstance().getUid();

            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("LIKED POST");

            Map<String, Object> following = new HashMap<>();
            following.put("liked User", USER_ID);
            following.put("liked Position", position);
            following.put("liked PostID", model.getPostID());

            colRef.add(following).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @SuppressLint({"SetTextI18n"})
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Toast.makeText(NewsFeedActivity.this, "Picture liked", Toast.LENGTH_SHORT).show();
                    mImageHandLike.setImageResource(R.drawable.postliked);
                    mImageHandLike.setTag("liked");

                    Query q2 = FirebaseFirestore.getInstance()
                            .collection("REGISTERED USERS").document(USER_ID)
                            .collection("LIKED POST").whereEqualTo("liked Position", position);

                    q2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.getDocuments().size() > 0) {

                                int LikedPostPosition;
                                for (QueryDocumentSnapshot ds : queryDocumentSnapshots) {

                                    LikedPostPosition = Objects.requireNonNull(ds.getLong("liked Position")).intValue();

                                    if (LikedPostPosition == position) {
                                        for (i = queryDocumentSnapshots.size();
                                             i <= queryDocumentSnapshots.size(); i++) {


                                            mLikeCount.setText(queryDocumentSnapshots.size() + " " + "likes");
                                        }
                                        return;
                                    }
                                }


                            }
                        }
                    });

                }
            });


        }
    }

    public void publishersInfo(ImageView imageProfile, TextView fName, TextView lName, String postID) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference docRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID);

            docRef.getParent().document(postID).addSnapshotListener((value, error) -> {
                assert value != null;
                User_Model user_model = value.toObject(User_Model.class);
                assert user_model != null;
                fName.setText(user_model.getmFirstName());
                lName.setText(user_model.getmLastName());
                //Glide.with(NewsFeedActivity.this).load(user_model.getmPhotoUrl()).into(imageProfile);
                Picasso.get()
                        .load(user_model.getmPhotoUrl())
                        .into(imageProfile);
            });
        }

    }


    public String publishersInfoID(String postID) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser != null){
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference docRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID);

            docRef.getParent().document(postID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    assert value != null;
                    User_Model user_model = value.toObject(User_Model.class);
                    assert user_model != null;

                }
            });
        }

        return postID;
    }


    public void displayAboutApp() {
        mAboutHA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayDialogAboutApp();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void displayDialogAboutApp() {

        dialog_three = new Dialog(NewsFeedActivity.this);
        dialog_three.setContentView(R.layout.dialog_custom);
        dialog_three.setCancelable(true);
        dialog_three.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog_three.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mmDialogTitle = dialog_three.findViewById(R.id.Dialog_title);
        mmDialogMessage = dialog_three.findViewById(R.id.Dialog_Message);
        mmCancelDialog_btn = dialog_three.findViewById(R.id.Btn_Cancel);
        mmProceedDialog_btn = dialog_three.findViewById(R.id.Btn_Proceed);

        mmCancelDialog_btn.setVisibility(View.GONE);

        mmProceedDialog_btn.setText("Visit Developer Page");

        mmDialogTitle.setText("About Health App");
        mmDialogMessage.setText(
                "Health App is a personal side project carried out by Osazuwa Ogiemwanye, a software developer. Health app includes " +
                        "a step counter, Bmi Calculator, covid info, news feed , and follow users section." + "Also included is extra " +
                        "for IT recruiters a basic for Insurance company on eclaim submission and many more. Basically this app is met " +
                        "for IT recruiters to display my little " + "skills in mobile app development Android native (Java) and integrated " +
                        "with flexible backend (Firebase)");
        dialog_three.show();


        //confirm the dialog box and navigate to the payment section
        proceedToDeveloperScreen();

    }

    public void proceedToDeveloperScreen() {
        mmProceedDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_three.dismiss();
                Intent navigate_dev_screen = new Intent(NewsFeedActivity.this, ContactDeveloperActivity.class);
                startActivity(navigate_dev_screen);

            }
        });
    }


    //TODO: display first circle pic
    public void displayFirst_ProfilePic() {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference dR = FirebaseFirestore.getInstance().collection("REGISTERED USERS").document(USER_ID);
            dR.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot != null) {
                        String getProfilePic = documentSnapshot.getString("mPhotoUrl");
                        Picasso.get()
                                .load(getProfilePic)
                                .into(pictureView);
                    }
                }
            });

        }

    }

    //TODO: navDrawer
    private void openNavDrawer() {
        btMenu.setOnClickListener(view -> mDrawerLayout.openDrawer(GravityCompat.START));
    }

    //TODO: navigate to profile page
    private void navigateProfile() {
        btProfile.setOnClickListener(view -> {
            Intent intent = new Intent(NewsFeedActivity.this, UserProfileActivity.class);
            startActivity(intent); // start same activity
            finish(); // destroy older activity
        });
    }

    //TODO: navigate to Setting page
    private void navigateSetting() {
        btSetting.setOnClickListener(view -> {
            Intent intent = new Intent(NewsFeedActivity.this, UserSettingsActivity.class);
            startActivity(intent); // start same activity
            finish(); // destroy older activity
        });
    }


    //TODO: POSTING TO NEWS FEED DIRECTLY FROM USER STARTS HERE
    public void addPost_MergedButton() {
        mNewsFeedPost_Btn.setOnClickListener(view -> displayDialog_Add_Message());
    }

    public void addPost_to_newsFeed() {
        mAddPost.setOnClickListener(view -> displayDialog_Add_Message());
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void displayDialog_Add_Message() {
        dialog = new Dialog(NewsFeedActivity.this);
        dialog.setContentView(R.layout.custom_post_message);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mCloseButton = dialog.findViewById(R.id.ImageView_close_button_post);
        mBtnDialog_Post = dialog.findViewById(R.id.Dialog_post);
        mImage_Container = dialog.findViewById(R.id.IMG_PostImageView);
        mBtnCall_PostCamera = dialog.findViewById(R.id.Linear_Image_upload_camera_post);
        mDialog_PostDescription = dialog.findViewById(R.id.ET_Dialog_Post_Desc);

        dialog.show();

        _cancelDialog(mCloseButton);

        clickUpload_PostFeedImage(mBtnCall_PostCamera);

        _userPost_To_NewsFeed(mBtnDialog_Post, mDialog_PostDescription);


    }

    public void _userPost_To_NewsFeed(TextView mBtnDialog_post, EditText mDialog_postDescription) {

        mBtnDialog_post.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View view) {

                if (mUploadTask != null) {
                    Toast.makeText(NewsFeedActivity.this, "post in progress..", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mImageUri == null && TextUtils.isEmpty(mDialog_postDescription.getText().toString())) {
                    Toast.makeText(NewsFeedActivity.this, "Image and text is required for newsfeed", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mImageUri != null && !TextUtils.isEmpty(mDialog_postDescription.getText().toString())) {
                    saveToFirestore(mDialog_postDescription);
                }
            }
        });
    }

    public void clickUpload_PostFeedImage(LinearLayout mBtnCall_postCamera) {
        mBtnCall_postCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askGalleryPermission();
            }
        });
    }

    public void askGalleryPermission() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    //to close the dialog screen
    public void _cancelDialog(ImageView mCloseButton) {
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            mImageUri = data.getData();

            Picasso.get()
                    .load(mImageUri)
                    .into(mImage_Container);
        }
    }

    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public void saveToFirestore(EditText mDialog_postDescription) {
        StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
        mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getMetadata() != null) {
                    if (taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String newsFeedPhoto = uri.toString();

                                SimpleDateFormat sdf = new SimpleDateFormat(" d MMM yyyy HH:mm:ss", Locale.getDefault());
                                String currentDateAndTime = sdf.format(new Date());

                                Calendar callForDate = Calendar.getInstance();
                                SimpleDateFormat currentDay = new SimpleDateFormat("E");
                                saveCurrentDayTime = currentDay.format(callForDate.getTime());

                                FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
                                if (AppUser != null && mImageUri != null) {
                                    USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    NEWSFEED = FirebaseAuth.getInstance().getCurrentUser().getUid();


                                    CollectionReference ColRef = FirebaseFirestore.getInstance()
                                            .collection("REGISTERED USERS").document(USER_ID)
                                            .collection("NEWSFEED POST");


                                    NewsFeed_Model post = new NewsFeed_Model(NEWSFEED, newsFeedPhoto, mDialog_postDescription.getText().toString(),
                                            currentDateAndTime, saveCurrentDayTime, liked_Post_Position, Liked_heart_position);
                                    Map<String, Object> saveInfo = new HashMap<>();

                                    ColRef.add(post).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(NewsFeedActivity.this, "Post successful", Toast.LENGTH_SHORT).show();
                                                //dialog.dismiss();

                                            } else {
                                                Toast.makeText(NewsFeedActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    overridePendingTransition(0, 0);
                                    Intent intent = new Intent(NewsFeedActivity.this, NewsFeedActivity.class);
                                    startActivity(intent); // start same activity
                                    finish(); // destroy older activity
                                    overridePendingTransition(0, 0);
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    //TODO: POSTING TO NEWS FEED DIRECTLY FROM USER ENDS HERE


    @Override
    protected void onPause() {
        super.onPause();
        closeDrawer(mDrawerLayout);

   mPostAdapter.stopListening();

    }

    @Override
    protected void onResume() {
        super.onResume();

       mPostAdapter.startListening();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onItemClick(int position) {

    }


    public class myViewHolderPost extends RecyclerView.ViewHolder {

        ImageView ImageProfile, mDotMenu, mImageHandLike,
                mMainImageNewsFeed, mImageLoveLike, mImageComment, mThreeDotImage;
        TextView fName, lName, mPostDate, mLikeCount, mHeartCount, mNewFeedDesc, mCommentCount, mCommentText;

        public myViewHolderPost(@NonNull View itemView) {
            super(itemView);

            mDotMenu = itemView.findViewById(R.id.IMG_threeDotMenu);
            mImageHandLike = itemView.findViewById(R.id.IMG_handLike);

            mImageLoveLike = itemView.findViewById(R.id.IMG_LoveLike);
            mImageComment = itemView.findViewById(R.id.IMG_Comment);
            mLikeCount = itemView.findViewById(R.id.TV_Number_TextLike);
            mHeartCount = itemView.findViewById(R.id.TV_Count_LoveHeart);
            mCommentCount = itemView.findViewById(R.id.TV_Count_CommentText);
            mCommentText = itemView.findViewById(R.id.TV_CommentText);
            mThreeDotImage = itemView.findViewById(R.id.IMG_threeDotMenu);

            //pick data from collection register
            ImageProfile = itemView.findViewById(R.id.newsFeed_profile_image);
            fName = itemView.findViewById(R.id.Tv_firstName_user_newsfeed);
            lName = itemView.findViewById(R.id.Tv_lastName_user_newsfeed);

            //pick this from Firestore under Collection post
            mPostDate = itemView.findViewById(R.id.Tv_newsFeed_post_date);
            mNewFeedDesc = itemView.findViewById(R.id.TV_description_newsFeed);
            mMainImageNewsFeed = itemView.findViewById(R.id.IMG_news_feed);

           mCommentCount.setOnClickListener(view -> {
               int position = getAdapterPosition();
               Intent navigateComment = new Intent(NewsFeedActivity.this, CommentActivity.class);
               navigateComment.putExtra("newsFeedGetItems", mPostAdapter.getSnapshots().get(position));

               startActivity(navigateComment);

           });

        }
    }

}