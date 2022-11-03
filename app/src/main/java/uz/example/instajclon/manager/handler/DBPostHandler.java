package uz.example.instajclon.manager.handler;

import uz.example.instajclon.model.Post;

public interface DBPostHandler {
    void onSuccess(Post post);
    void onError(Exception exception);
}
