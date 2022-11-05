package uz.example.instajclon.manager.handler;

public interface DBFollowHandler {
    void onSuccess(boolean isDone);
    void onError(Exception exception);
}
