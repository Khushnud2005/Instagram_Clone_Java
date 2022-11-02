package uz.example.instajclon.manager.handler;

import java.util.ArrayList;

import uz.example.instajclon.model.User;

public interface DBUsersHandler {
    void onSuccess(ArrayList<User> users);
    void onError(Exception exception);
}
