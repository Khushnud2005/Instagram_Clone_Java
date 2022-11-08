package uz.example.instajclon.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;

import java.util.ArrayList;

import uz.example.instajclon.R;
import uz.example.instajclon.activity.MainActivity;
import uz.example.instajclon.activity.SignInActivity;
import uz.example.instajclon.adapter.ProfileAdapter;
import uz.example.instajclon.manager.AuthManager;
import uz.example.instajclon.manager.DBManager;
import uz.example.instajclon.manager.PrefsManager;
import uz.example.instajclon.manager.StorageManager;
import uz.example.instajclon.manager.handler.DBPostsHandler;
import uz.example.instajclon.manager.handler.DBUserHandler;
import uz.example.instajclon.manager.handler.DBUsersHandler;
import uz.example.instajclon.manager.handler.StorageHandler;
import uz.example.instajclon.model.Post;
import uz.example.instajclon.model.User;
import uz.example.instajclon.utils.Logger;

/**
 * In ProfileFragment, user can check his/her posts and counts and change profile photo
 */
public class ProfileFragment extends BaseFragment {
    String TAG = ProfileFragment.class.getSimpleName();
    ImageView iv_logout;
    RecyclerView rv_profile;
    TextView tv_fullname;
    TextView tv_email;
    TextView tv_posts;
    TextView tv_followers;
    TextView tv_following;
    ShapeableImageView iv_profile;

    Uri pickedPhoto = null;
    ArrayList<Uri> allPhotos = new ArrayList<Uri>();
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        iv_logout = view.findViewById(R.id.iv_logout);
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthManager.signOut();
                callSignInActivity();
            }
        });
        tv_fullname = view.findViewById(R.id.tv_fullname);
        tv_email = view.findViewById(R.id.tv_email);
        tv_posts = view.findViewById(R.id.tv_posts);
        tv_followers = view.findViewById(R.id.tv_followers);
        tv_following = view.findViewById(R.id.tv_following);
        iv_profile = view.findViewById(R.id.iv_profile);

        rv_profile = view.findViewById(R.id.rv_profile);
        rv_profile.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFishBunPhoto();
            }
        });
        loadUserInfo();
        loadMyPosts();
        loadMyFollowing();
        loadMyFollowers();
    }

    private void loadMyPosts() {
        String uid = AuthManager.currentUser().getUid();
        DBManager.loadPosts(uid, new DBPostsHandler() {
            @Override
            public void onSuccess(ArrayList<Post> posts) {
                tv_posts.setText(String.valueOf(posts.size()));
                refreshAdapter(posts);
            }
            @Override
            public void onError(Exception e) {

            }
        });
    }

    private void loadUserInfo() {
        DBManager.loadUser(AuthManager.currentUser().getUid(), new DBUserHandler() {
            @Override
            public void onSuccess(User user) {
                if (user != null) {
                    showUserInfo(user);
                }
            }

            @Override
            public void onError(Exception exception) {

            }
        });
    }

    private void showUserInfo(User user) {
        tv_fullname.setText(user.getFullname());
        tv_email.setText(user.getEmail());
        Glide.with(this).load(user.getUserImg())
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(iv_profile);
        Log.d("@@ShowUser","User Token : "+user.getDevice_token());
    }

    private void uploadUserPhoto() {
        showLoading(requireActivity());
        if (pickedPhoto == null) return;
        StorageManager.uploadUserPhoto(pickedPhoto, new StorageHandler() {
            @Override
            public void onSuccess(String imgUrl) {
                dismissLoading();
                DBManager.updateUserImage(imgUrl);
                iv_profile.setImageURI(pickedPhoto);
            }

            @Override
            public void onError(Exception exception) {
                Logger.d(TAG,exception.getMessage());
            }
        });

    }
    /**
     * Pick photo using FishBun library
     */
    private void pickFishBunPhoto() {
        FishBun.with(this)
                .setImageAdapter(new GlideAdapter())
                .setMaxCount(1)
                .setMinCount(1)
                .setSelectedImages(allPhotos)
                .startAlbumWithActivityResultCallback(photoLauncher);
    }
    public ActivityResultLauncher<Intent> photoLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        if (result.getData() != null){
                            allPhotos = result.getData().getParcelableArrayListExtra(FishBun.INTENT_PATH);
                            pickedPhoto = allPhotos.get(0);
                            uploadUserPhoto();
                        }

                    }
                }
            });

    private void refreshAdapter(ArrayList<Post> items) {
        ProfileAdapter adapter = new ProfileAdapter(this, items);
        rv_profile.setAdapter(adapter);
    }
    private void callSignInActivity() {
        //PrefsManager.getInstance(getContext()).saveData("login","false");
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);

    }
    private void loadMyFollowing(){
        String uid = AuthManager.currentUser().getUid();
        DBManager.loadFollowing(uid, new DBUsersHandler() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                tv_following.setText(String.valueOf(users.size()));
            }
            @Override
            public void  onError(Exception e) {
            }
        });
    }

    private void loadMyFollowers(){
        String uid = AuthManager.currentUser().getUid();
        DBManager.loadFollowers(uid, new DBUsersHandler(){
            @Override
            public void onSuccess(ArrayList<User> users) {
                tv_followers.setText(String.valueOf(users.size()));
            }
            @Override
            public void onError(Exception e) {
            }
        });
    }
}