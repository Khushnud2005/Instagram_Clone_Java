package uz.example.instajclon.manager.handler;

import java.util.ArrayList;

import uz.example.instajclon.model.Post;

public interface DBPostsHandler {
    void onSuccess(ArrayList<Post> posts);
    void onError(Exception exception);
}
