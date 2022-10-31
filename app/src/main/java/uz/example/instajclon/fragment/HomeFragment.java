package uz.example.instajclon.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import uz.example.instajclon.R;
import uz.example.instajclon.adapter.HomeAdapter;
import uz.example.instajclon.model.Post;

/**
 * In HomeFragment, user can check his/her posts and friends posts
 */
public class HomeFragment extends BaseFragment {
    String TAG = HomeFragment.class.getSimpleName();

    RecyclerView rv_home;
    private HomeListener listener;
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
        refreshAdapter(loadPosts());

        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.scrollToUpload();
            }
        });
    }

    private void refreshAdapter(ArrayList<Post> items) {
        HomeAdapter adapter = new HomeAdapter(this, items);
        rv_home.setAdapter(adapter);
    }

    private ArrayList<Post> loadPosts() {
        ArrayList<Post> items = new ArrayList<>();
        items.add(new Post("https://images.unsplash.com/photo-1657214058650-31cc8400713b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHwxfHx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=600&q=60"));
        items.add(new Post("https://images.unsplash.com/photo-1664575196044-195f135295df?ixlib=rb-4.0.3&ixid=MnwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHwyMXx8fGVufDB8fHx8&auto=format&fit=crop&w=600&q=60"));
        items.add(new Post("https://images.unsplash.com/photo-1509395286499-2d94a9e0c814?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTR8fHBob25lfGVufDB8MnwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        items.add(new Post("https://images.unsplash.com/photo-1665436752144-4e9236563aff?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHx0b3BpYy1mZWVkfDEyMHw2c01WalRMU2tlUXx8ZW58MHx8fHw%3D&auto=format&fit=crop&w=600&q=60"));

        return items;
    }

    /**
     * This interface is created for communication with UploadFragment
     */
    public interface HomeListener {
        public void scrollToUpload();
    }
}