package uz.example.instajclon.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import uz.example.instajclon.R;
import uz.example.instajclon.adapter.SearchAdapter;
import uz.example.instajclon.manager.AuthManager;
import uz.example.instajclon.manager.DBManager;
import uz.example.instajclon.manager.handler.DBUsersHandler;
import uz.example.instajclon.model.User;

/**
 * In HomeFragment, user can search friends
 */
public class SearchFragment extends BaseFragment {

    String TAG = SearchFragment.class.getSimpleName();
    RecyclerView rv_search;
    ArrayList<User> items = new ArrayList<User>();
    ArrayList<User> users = new ArrayList<User>();
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        rv_search = view.findViewById(R.id.rv_search);
        rv_search.setLayoutManager(new GridLayoutManager(getActivity(),1));
        EditText et_search = view.findViewById(R.id.et_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString().trim();
                usersByKeyword(keyword);
            }
        });
        loadUsers();
    }
    private void refreshAdapter(ArrayList<User> items) {
        SearchAdapter adapter = new SearchAdapter(this, items);
        rv_search.setAdapter(adapter);
    }
    private void loadUsers(){
        String uid = AuthManager.currentUser().getUid();
        DBManager.loadUsers(new DBUsersHandler() {
            @Override
            public void onSuccess(ArrayList<User> users) {
                items.clear();
                items.addAll(users);
                refreshAdapter(items);
            }

            @Override
            public void onError(Exception exception) {

            }
        });
    }
    void usersByKeyword(String keyword) {
        if (keyword.isEmpty())
            refreshAdapter(items);

        users.clear();
        for (User user : items)
            if (user.getFullname().toLowerCase().startsWith(keyword.toLowerCase()))
                users.add(user);

        refreshAdapter(users);
    }
}