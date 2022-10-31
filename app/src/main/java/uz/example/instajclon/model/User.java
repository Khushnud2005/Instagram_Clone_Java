package uz.example.instajclon.model;

public class User {
    String fullname;
    String email;

    public User(String fullname, String email) {
        this.fullname = fullname;
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }
}
