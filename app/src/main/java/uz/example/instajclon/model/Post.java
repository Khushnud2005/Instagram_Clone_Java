package uz.example.instajclon.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Post {
    String id;
    String caption;
    String postImg;
    String currentDate;
    String uid;
    String fullname;
    String userImg;
    boolean isLiked = false;
    public Post(String postImg) {
        this.postImg = postImg;
    }

    public Post(String caption, String postImg) {
        this.caption = caption;
        this.postImg = postImg;
    }

    public Post(String id, String caption, String postImg) {
        this.id = id;
        this.caption = caption;
        this.postImg = postImg;
    }

    public String getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getUid() {
        return uid;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPostImg(String postImg) {
        this.postImg = postImg;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getPostImg() {
        return postImg;
    }

    public void setCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/M/yyyy hh:mm");
        currentDate = sdf.format(new Date());
        Log.d("setTime",currentDate);
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
