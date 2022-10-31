package uz.example.instajclon.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import uz.example.instajclon.R;
import uz.example.instajclon.fragment.SearchFragment;
import uz.example.instajclon.model.User;

public class SearchAdapter extends BaseAdapter {
    SearchFragment fragment;
    ArrayList<User> items;

    public SearchAdapter(SearchFragment fragment, ArrayList<User> items) {
        this.fragment = fragment;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_search, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = items.get(position);
        if (holder instanceof UserViewHolder) {
            TextView tv_fullname = ((UserViewHolder) holder).tv_fullname;
            TextView tv_email = ((UserViewHolder) holder).tv_email;
            tv_fullname.setText(user.getFullname());
            tv_email.setText(user.getEmail());
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView iv_profile;
        TextView tv_fullname;
        TextView tv_email;
        TextView tv_follow;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_profile = itemView.findViewById(R.id.iv_profile);
            tv_fullname = itemView.findViewById(R.id.tv_fullname);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_follow = itemView.findViewById(R.id.tv_follow);
        }
    }
}
