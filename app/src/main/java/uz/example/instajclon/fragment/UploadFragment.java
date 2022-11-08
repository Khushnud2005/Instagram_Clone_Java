package uz.example.instajclon.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;

import java.util.ArrayList;

import uz.example.instajclon.R;
import uz.example.instajclon.manager.AuthManager;
import uz.example.instajclon.manager.DBManager;
import uz.example.instajclon.manager.StorageManager;
import uz.example.instajclon.manager.handler.DBPostHandler;
import uz.example.instajclon.manager.handler.DBUserHandler;
import uz.example.instajclon.manager.handler.StorageHandler;
import uz.example.instajclon.model.Post;
import uz.example.instajclon.model.User;
import uz.example.instajclon.utils.Utils;

/**
 * In UploadFragment, user can upload
 * a post with photo and caption
 */
public class UploadFragment extends BaseFragment {
    String TAG = UploadFragment.class.getSimpleName();

    FrameLayout fl_photo;
    ImageView iv_photo;
    EditText et_caption;
    private UploadListener listener;
    Uri pickedPhoto;
    String exten;
    static ArrayList<Uri> allPhotos = new  ArrayList<Uri>();
    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        initViews(view);
        return view;
    }

    /**
     * onAttach is for communication of Fragments
     */
    @Override
    public void onAttach(Context context ) {
        super.onAttach(context);
         if (context instanceof UploadListener) {
             listener = (UploadListener) context;
        } else {
            throw new RuntimeException("$context must implement FirstListener");
        }
    }

    /**
     * onDetach is for communication of Fragments
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void initViews(View view) {
        FrameLayout fl_view = view.findViewById(R.id.fl_view);
        setViewHeight(fl_view);
        et_caption = view.findViewById(R.id.et_caption);
        fl_photo = view.findViewById(R.id.fl_photo);
        iv_photo = view.findViewById(R.id.iv_photo);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        ImageView iv_pick = view.findViewById(R.id.iv_pick);
        ImageView iv_upload = view.findViewById(R.id.iv_upload);

        iv_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFishBunPhoto();
            }
        });
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidePickedPhoto();
            }
        });
        iv_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadNewPost();
            }
        });
    }
    /**
     * Set view height as screen width
     */
    private void setViewHeight(FrameLayout flView) {
        ViewGroup.LayoutParams params  = flView.getLayoutParams();
        params.height = Utils.screenSize(requireActivity().getApplication()).getWidth();
        flView.setLayoutParams(params);
    }
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
                            String mimeType = requireActivity().getContentResolver().getType(pickedPhoto);
                            exten = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
                            showPickedPhoto();
                        }

                    }
                }
            });

    private void showPickedPhoto() {
        fl_photo.setVisibility(View.VISIBLE);
        iv_photo.setImageURI(pickedPhoto);
    }

    private void hidePickedPhoto() {
        pickedPhoto = null;
        allPhotos.clear();
        fl_photo.setVisibility(View.GONE);
    }

    private void uploadNewPost() {
        String caption = et_caption.getText().toString().trim();
        if (!caption.isEmpty() && pickedPhoto != null) {
            showLoading(requireActivity());
            uploadPostPhoto(pickedPhoto,caption);
        }
        if (caption.isEmpty() || pickedPhoto == null){
            Utils.toast(this.requireContext(),"Please pick Photo && write Caption !!!");
        }
    }

    private void uploadPostPhoto(Uri uri, String caption) {
        StorageManager.uploadPostPhoto(uri,exten, new StorageHandler() {
            @Override
            public void onSuccess(String imgUrl ) {
                Post post = new Post(caption, imgUrl);
                post.setCurrentTime();

                String uid = AuthManager.currentUser().getUid();

                DBManager.loadUser(uid, new DBUserHandler() {
                    @Override
                    public void onSuccess(User user) {
                        post.setUid(uid);
                        post.setFullname(user.getFullname());
                        post.setUserImg(user.getUserImg());
                        storePostToDB(post);
                    }
                    @Override
                    public void onError(Exception e ) {
                    }
                });
            }

            @Override
            public void onError(Exception exception ) {

            }
        });
    }

    private void storePostToDB(Post post) {

            DBManager.storePosts(post, new DBPostHandler() {
                @Override
                public void onSuccess(Post post) {
                    storeFeedToDB(post);
                }

                @Override
                public void onError(Exception exception ) {
                    Log.d("storePost",exception.getMessage());
                }
            });
        
    }

    private void storeFeedToDB(Post post) {
        DBManager.storeFeeds(post, new DBPostHandler() {
            @Override
            public void onSuccess(Post post) {
                dismissLoading();
                resetAll();
                listener.scrollToHome();
            }

            @Override
            public void onError(Exception exception) {
                dismissLoading();
            }
        });
    }

    private void resetAll() {
        allPhotos.clear();
        et_caption.getText().clear();
        pickedPhoto = null;
        fl_photo.setVisibility(View.GONE);
    }

    /**
     * This interface is created for communication with HomeFragment
     */
    public interface UploadListener {
        void scrollToHome();
    }

}