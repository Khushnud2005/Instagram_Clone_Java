package uz.example.instajclon.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import uz.example.instajclon.R;
import uz.example.instajclon.fragment.FavoriteFragment;
import uz.example.instajclon.fragment.HomeFragment;
import uz.example.instajclon.manager.AuthManager;
import uz.example.instajclon.model.Post;

public class FavoriteAdapter extends BaseAdapter{
    FavoriteFragment fragment;
    ArrayList<Post> items;

    public FavoriteAdapter(FavoriteFragment fragment, ArrayList<Post> items) {
        this.fragment = fragment;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_favorite,parent,false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Post post = items.get(position);
        if (holder instanceof PostViewHolder) {
            ShapeableImageView iv_post = ((PostViewHolder) holder).iv_post;
            ShapeableImageView iv_profile = ((PostViewHolder) holder).iv_profile;
            TextView tv_fullname = ((PostViewHolder) holder).tv_fullname;
            TextView tv_caption = ((PostViewHolder) holder).tv_caption;
            TextView tv_time = ((PostViewHolder) holder).tv_time;
            ImageView iv_like = ((PostViewHolder) holder).iv_like;
            ImageView iv_more = ((PostViewHolder) holder).iv_more;

            tv_fullname.setText(post.getFullname());
            tv_caption.setText(post.getCaption());
            tv_time.setText(post.getCurrentDate());
            Glide.with(fragment).load(post.getUserImg()).placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar).into(iv_profile);
            Glide.with(fragment).load(post.getPostImg()).into(iv_post);

            iv_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(post.isLiked()){
                        post.setLiked(false);
                        iv_like.setImageResource(R.mipmap.ic_favorite);
                    }else{
                        post.setLiked(true);
                        iv_like.setImageResource(R.mipmap.ic_liked);
                    }
                    fragment.likeOrUnlikePost(post);
                }
            });

            String uid = AuthManager.currentUser().getUid();
            if(uid.equals(post.getUid())){
                iv_more.setVisibility(View.VISIBLE);
            }else{
                iv_more.setVisibility(View.GONE);
            }
            iv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.showDeleteDialog(post);
                }
            });
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
