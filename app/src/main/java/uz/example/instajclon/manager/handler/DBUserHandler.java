package uz.example.instajclon.manager.handler;

import uz.example.instajclon.model.User;

public interface DBUserHandler {
    void onSuccess(User user);
    void onError(Exception exception);
}
