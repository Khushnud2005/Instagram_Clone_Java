package uz.example.instajclon.fragment;

import android.content.Context;
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

import java.util.ArrayList;

import uz.example.instajclon.R;
import uz.example.instajclon.adapter.HomeAdapter;
import uz.example.instajclon.manager.AuthManager;
import uz.example.instajclon.manager.DBManager;
import uz.example.instajclon.manager.handler.DBPostsHandler;
import uz.example.instajclon.model.Post;
import uz.example.instajclon.utils.Utils;

/**
 * In HomeFragment, user can check his/her posts and friends posts
 */
public class HomeFragment extends BaseFragment {
    String TAG = HomeFragment.class.getSimpleName();
    RecyclerView rv_home;
    private HomeListener listener;
    ArrayList<Post> feeds = new ArrayList<>();
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
        loadMyFeeds();

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



    /**
     * This interface is created for communication with UploadFragment
     */
    public interface HomeListener {
        public void scrollToUpload();
    }
}