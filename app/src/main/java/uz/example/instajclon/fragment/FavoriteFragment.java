package uz.example.instajclon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uz.example.instajclon.R;
import uz.example.instajclon.activity.SignInActivity;
import uz.example.instajclon.adapter.FavoriteAdapter;
import uz.example.instajclon.adapter.HomeAdapter;
import uz.example.instajclon.manager.AuthManager;
import uz.example.instajclon.manager.DBManager;
import uz.example.instajclon.manager.StorageManager;
import uz.example.instajclon.manager.handler.DBPostHandler;
import uz.example.instajclon.manager.handler.DBPostsHandler;
import uz.example.instajclon.model.Post;
import uz.example.instajclon.utils.DialogListener;
import uz.example.instajclon.utils.Utils;

/**
 * In FavoriteFragment, user can check all liked posts
 */
public class FavoriteFragment extends BaseFragment {
    String TAG = FavoriteFragment.class.getSimpleName();

    RecyclerView rv_favorite;
    
    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        initViews(view);
        return view;
    }
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        if (menuVisible) {
            //Utils.toast(this.requireContext(),"setUserVisibleHint Working : Yes");
            loadLikedFeeds();
            //Utils.toast(this.getContext(),"MenuVisibil");
            Log.d(TAG,"MenuVisible");
        }
    }
    private void initViews(View view) {
        rv_favorite = view.findViewById(R.id.rv_favorite);
        rv_favorite.setLayoutManager(new GridLayoutManager(getActivity(),1));

    }
    public void likeOrUnlikePost(Post post) {
        String uid = AuthManager.currentUser().getUid();
        DBManager.likeFeedPost(uid, post);

        loadLikedFeeds();
    }

    private void loadLikedFeeds() {

        Log.d("@@loadLiked","Favorite : Working");
        showLoading(requireActivity());
        String uid = AuthManager.currentUser().getUid();
        DBManager.loadLikedFeeds(uid, new DBPostsHandler() {
            @Override
            public void onSuccess(ArrayList<Post> posts) {
                dismissLoading();
                refreshAdapter(posts);
            }

            @Override
            public void onError(Exception e) {
                dismissLoading();
            }
        });
    }

    private void refreshAdapter(ArrayList<Post> items) {
        FavoriteAdapter adapter = new FavoriteAdapter(this, items);
        rv_favorite.setAdapter(adapter);
    }
    public void showDeleteDialog(Post post ){
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
            public void onSuccess(Post post) {
                StorageManager.deletePhoto(photoUrl);
                loadLikedFeeds();
            }

            @Override
            public void onError(Exception e ) {

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
}