package uz.example.instajclon.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import uz.example.instajclon.R;
import uz.example.instajclon.fragment.HomeFragment;
import uz.example.instajclon.model.Post;

public class HomeAdapter extends BaseAdapter{
    HomeFragment fragment;
    ArrayList<Post> items;

    public HomeAdapter(HomeFragment fragment, ArrayList<Post> items) {
        this.fragment = fragment;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_home,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post post = items.get(position);
        if (holder instanceof PostViewHolder) {
            ShapeableImageView iv_post = ((PostViewHolder) holder).iv_post;
            Glide.with(fragment).load(post.getPostImg()).into(iv_post);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class PostViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView iv_profile;
        ShapeableImageView iv_post;
        TextView tv_fullname;
        TextView tv_time;
        TextView tv_caption;
        ImageView iv_more;
        ImageView iv_like;
        ImageView iv_share;
        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_profile = itemView.findViewById(R.id.siv_profile);
            iv_post = itemView.findViewById(R.id.iv_post);
            tv_fullname = itemView.findViewById(R.id.tv_full_name);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_caption = itemView.findViewById(R.id.tv_caption);
            iv_more = itemView.findViewById(R.id.iv_more);
            iv_like = itemView.findViewById(R.id.iv_like);
            iv_share = itemView.findViewById(R.id.iv_share);
        }
    }
}
