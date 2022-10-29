package uz.example.instajclon.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import uz.example.instajclon.R;

/**
 * In HomeFragment, user can check his/her posts and friends posts
 */
public class HomeFragment extends BaseFragment {
    String TAG = HomeFragment.class.getSimpleName();
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
        iv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.scrollToUpload();
            }
        });
    }

    /**
     * This interface is created for communication with UploadFragment
     */
    public interface HomeListener {
        public void scrollToUpload();
    }
}