package uz.example.instajclon.model;

public class User {
    String uid;
    String fullname;
    String email;
    String password;
    String userImg;

    String device_id = "";
    String device_type = "A";
    String device_token = "";

    boolean isFollowed = false;

    public User(String fullname, String email) {
        this.fullname = fullname;
        this.email = email;
    }

    public User(String fullname, String email, String userImg) {
        this.fullname = fullname;
        this.email = email;
        this.userImg = userImg;
    }

    public User(String fullname, String email, String password, String userImg) {
        this.fullname = fullname;
        this.email = email;
        this.password = password;
        this.userImg = userImg;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getPassword() {
        return password;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }
}
