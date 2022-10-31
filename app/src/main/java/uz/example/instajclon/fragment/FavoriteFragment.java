package uz.example.instajclon.fragment;

import android.os.Bundle;
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
import uz.example.instajclon.model.Post;

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

    private void initViews(View view) {
        rv_favorite = view.findViewById(R.id.rv_favorite);
        rv_favorite.setLayoutManager(new GridLayoutManager(getActivity(),1));
        refreshAdapter(loadPosts());
    }

    private void refreshAdapter(ArrayList<Post> items) {
        FavoriteAdapter adapter = new FavoriteAdapter(this, items);
        rv_favorite.setAdapter(adapter);
    }

    private ArrayList<Post> loadPosts() {
        ArrayList<Post> items = new ArrayList<>();

        items.add(new Post("https://images.unsplash.com/photo-1664575196044-195f135295df?ixlib=rb-4.0.3&ixid=MnwxMjA3fDF8MHxlZGl0b3JpYWwtZmVlZHwyMXx8fGVufDB8fHx8&auto=format&fit=crop&w=600&q=60"));
        items.add(new Post("https://images.unsplash.com/photo-1509395286499-2d94a9e0c814?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTR8fHBob25lfGVufDB8MnwwfHw%3D&auto=format&fit=crop&w=600&q=60"));

        return items;
    }
}