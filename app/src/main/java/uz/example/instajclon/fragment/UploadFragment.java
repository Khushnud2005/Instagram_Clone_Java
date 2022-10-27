package uz.example.instajclon.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import uz.example.instajclon.R;

/**
 * In UploadFragment, user can upload
 * a post with photo and caption
 */
public class UploadFragment extends BaseFragment {
    String TAG = UploadFragment.class.getSimpleName();

    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upload, container, false);
    }
}