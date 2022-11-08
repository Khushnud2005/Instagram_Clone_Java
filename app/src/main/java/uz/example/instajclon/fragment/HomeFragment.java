package uz.example.instajclon.fragment;

import static uz.example.instajclon.network.Values.TO;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uz.example.instajclon.R;
import uz.example.instajclon.adapter.HomeAdapter;
import uz.example.instajclon.manager.AuthManager;
import uz.example.instajclon.manager.DBManager;
import uz.example.instajclon.manager.StorageManager;
import uz.example.instajclon.manager.handler.DBPostHandler;
import uz.example.instajclon.manager.handler.DBPostsHandler;
import uz.example.instajclon.manager.handler.DBUserHandler;
import uz.example.instajclon.model.NotificationData;
import uz.example.instajclon.model.Post;
import uz.example.instajclon.model.PushNotification;
import uz.example.instajclon.model.User;
import uz.example.instajclon.network.ApiService;
import uz.example.instajclon.network.RetrofitHttp;
import uz.example.instajclon.service.FCMService;
import uz.example.instajclon.utils.DialogListener;
import uz.example.instajclon.utils.Utils;

/**
 * In HomeFragment, user can check his/her posts and friends posts
 */
public class HomeFragment extends BaseFragment {
    String TAG = HomeFragment.class.getSimpleName();
    RecyclerView rv_home;
    private HomeListener listener;
    ArrayList<Post> feeds = new ArrayList<>();
    boolean liked = false;


    public HomeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if (menuVisible) {
            //Utils.toast(this.requireContext(),"setUserVisibleHint Working : Yes");
            loadMyFeeds();
        }
    }
    /*@Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && feeds.size()>0) {
            loadMyFeeds();
            Log.d("onVisibil","Working : Yes");
            Utils.toast(this.requireContext(),"setUserVisibleHint Working : Yes");
        }
    }*/


    /**
     * onAttach is for communication of Fragments
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UploadFragment.UploadListener) {
            listener = (HomeListener) context;
        } else {
            throw new RuntimeException("$context must implement FirstListener");
        }
    }
    /**
     * onAttach is for communication of Fragments
     */
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    private void initViews(View view ) {
        ImageView iv_camera = view.findViewById(R.id.iv_camera);
        rv_home = view.findViewById(R.id.rv_home);
        rv_home.setLayoutManager(new GridLayoutManager(getActivity(),1));
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.scrollToUpload();
            }
        });
        //loadMyFeeds();

    }

    private void loadMyFeeds() {
        showLoading(requireActivity());

        String uid = AuthManager.currentUser().getUid();
        DBManager.loadFeeds(uid, new DBPostsHandler() {
            @Override
            public void onSuccess(ArrayList<Post> posts) {
                dismissLoading();
                feeds.clear();
                feeds.addAll(posts);
                refreshAdapter(feeds);
                Log.d("onVisibil","Feeds : "+String.valueOf(feeds.size()));
            }

            @Override
            public void onError(Exception exception) {
                dismissLoading();
            }
        });
    }

    private void refreshAdapter(ArrayList<Post> items) {
        HomeAdapter adapter = new HomeAdapter(this, items);
        rv_home.setAdapter(adapter);
    }
    public void liked(){
        liked = true;
    }
    public void disLiked(){
        liked = false;
    }
    public void likeOrUnlikePost(Post post ) {

        String uid = AuthManager.currentUser().getUid();
        DBManager.likeFeedPost(uid, post);
        if (liked){
            msg();
            sendNoti(post.getUid(),post.getCaption());
        }
    }

    private void sendNoti(String userId,String caption) {
        Log.d("@@sendNoti","userId : "+userId);
        String title = "Xabar";
        String message = caption + " nomli postingizga  LIKE Bosildi";
        DBManager.loadUser(userId, new DBUserHandler() {
            @Override
            public void onSuccess(User user) {
                Log.d("@@sendNoti","DevToken : "+user.toString());
                if (!user.getDevice_token().equals("null")){
                    RetrofitHttp.apiService.sendNotification(new PushNotification(new NotificationData(title,message),user.getDevice_token()))
                            .enqueue(new Callback<PushNotification>() {
                                @Override
                                public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                                    if (response.isSuccessful()){
                                        Utils.toast(requireContext(),"Like Notification Sent");
                                    }else{
                                        Utils.toast(requireContext(),"Like failed");
                                    }
                                }

                                @Override
                                public void onFailure(Call<PushNotification> call, Throwable t) {
                                    Utils.toast(requireContext(),t.getMessage().toString());
                                }
                            });
                }
            }

            @Override
            public void onError(Exception exception) {

            }
        });



    }

    private void msg() {
        FirebaseMessaging.getInstance()
                .subscribeToTopic("All");
    }

    public void showDeleteDialog(Post post){
        Utils.dialogDouble(requireContext(), getString(R.string.str_delete_post), new DialogListener() {
            @Override
            public void onCallback(Boolean isChosen) {
                if(isChosen){
                    deletePost(post);
                }
            }
        });
    }

    public void deletePost(Post post) {
        String photoUrl = post.getPostImg();
        DBManager.deletePost(post, new DBPostHandler() {
            @Override
            public void onSuccess(Post post ) {
                StorageManager.deletePhoto(photoUrl);
                loadMyFeeds();
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    public void sharePost(Post post){
        Intent intent= new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,post.getCaption()+" : "+post.getPostImg());
        intent.putExtra("imgUrl",post.getPostImg());
        intent.setType("text/plain");
        startActivity(Intent.createChooser(intent,"Share To:"));
    }

    /**
     * This interface is created for communication with UploadFragment
     */
    public interface HomeListener {
        public void scrollToUpload();
    }
}