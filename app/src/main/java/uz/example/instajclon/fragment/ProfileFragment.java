package uz.example.instajclon.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import uz.example.instajclon.R;
import uz.example.instajclon.activity.SignInActivity;
import uz.example.instajclon.manager.PrefsManager;

/**
 * In ProfileFragment, user can check his/her posts and counts and change profile photo
 */
public class ProfileFragment extends BaseFragment {
    String TAG = ProfileFragment.class.getSimpleName();
    ImageView iv_logout;
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
                callSignInActivity();
            }
        });
    }

    private void callSignInActivity() {
        PrefsManager.getInstance(getContext()).saveData("login","false");
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);

    }
}