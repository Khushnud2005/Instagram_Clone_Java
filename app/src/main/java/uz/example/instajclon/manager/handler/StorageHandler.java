package uz.example.instajclon.manager.handler;

public interface StorageHandler {
    void  onSuccess(String imgUrl);
    void  onError(Exception exception);
}
