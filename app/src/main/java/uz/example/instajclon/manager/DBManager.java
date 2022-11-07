package uz.example.instajclon.manager;


import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import uz.example.instajclon.manager.handler.DBFollowHandler;
import uz.example.instajclon.manager.handler.DBPostHandler;
import uz.example.instajclon.manager.handler.DBPostsHandler;
import uz.example.instajclon.manager.handler.DBUserHandler;
import uz.example.instajclon.manager.handler.DBUsersHandler;
import uz.example.instajclon.model.Post;
import uz.example.instajclon.model.User;

public class DBManager {
    static final String USER_PATH = "users";
    static final String POST_PATH = "posts";
    static final String FEED_PATH = "feeds";
    static final String FOLLOWING_PATH = "following";
    static final String FOLLOWERS_PATH = "followers";

    @SuppressLint("StaticFieldLeak")
    static FirebaseFirestore database = FirebaseFirestore.getInstance();

    public static void storeUser(User user, DBUserHandler handler){
        database.collection(USER_PATH).document(user.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                handler.onSuccess(null);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });
    }

    public static void loadUser(String uid ,DBUserHandler handler) {
        database.collection(USER_PATH).document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                if (snapshot.exists()) {
                    String fullname = snapshot.getString("fullname");
                    String email = snapshot.getString("email");
                    String userImg = snapshot.getString("userImg");

                    User user = new User(fullname, email, userImg);
                    user.setUid(uid);
                    handler.onSuccess(user);
                } else {
                    handler.onSuccess(null);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });
    }

    public static void loadUsers(DBUsersHandler handler) {
        database.collection(USER_PATH).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<User> users = new ArrayList<User>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String uid = document.getString("uid");
                        String fullname = document.getString("fullname");
                        String email = document.getString("email");
                        String userImg = document.getString("userImg");


                        User user = new User(fullname, email, userImg);
                        user.setUid(uid);
                        users.add(user);
                    }
                    handler.onSuccess(users);
                } else {
                    handler.onError(task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });
    }

    public static void updateUserImage(String userImg) {
        String uid = AuthManager.currentUser().getUid();
        database.collection(USER_PATH).document(uid).update("userImg", userImg);
    }

    public static void storePosts(Post post , DBPostHandler handler) {
        Log.d("storePosts",post.getUid());
        CollectionReference reference = database.collection(USER_PATH).document(post.getUid()).collection(POST_PATH);
        String id = reference.document().getId();
        post.setId(id);

        reference.document(post.getId()).set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                handler.onSuccess(post);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });
    }
    public static void storeFeeds(Post post , DBPostHandler handler) {
        CollectionReference reference = database.collection(USER_PATH).document(post.getUid()).collection(FEED_PATH);

        reference.document(post.getId()).set(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                handler.onSuccess(post);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });
    }
    public static void loadFeeds(String uid, DBPostsHandler handler) {
        CollectionReference reference = database.collection(USER_PATH).document(uid).collection(FEED_PATH);
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Post> posts = new ArrayList<Post>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getString("id");
                        String caption = document.getString("caption");
                        String postImg = document.getString("postImg");
                        String fullname = document.getString("fullname");
                        String userImg = document.getString("userImg");
                        String currentDate = document.getString("currentDate");
                        Boolean isLiked = document.getBoolean("isLiked");
                        if (isLiked == null) isLiked = false;
                        String userId = document.getString("uid");
                        Post post = new  Post(id, caption, postImg);
                        post.setUid(userId);
                        post.setFullname(fullname);
                        post.setUserImg(userImg);
                        post.setCurrentDate(currentDate);
                        post.setLiked(isLiked);
                        posts.add(post);
                    }
                    handler.onSuccess(posts);
                } else {
                    handler.onError(task.getException());
                }
            }
        });
    }

    public static void loadPosts(String uid, DBPostsHandler handler) {
        CollectionReference reference = database.collection(USER_PATH).document(uid).collection(POST_PATH);
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Post> posts = new ArrayList<Post>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getString("id");
                        String caption = document.getString("caption");
                        String postImg = document.getString("postImg");
                        String fullname = document.getString("fullname");
                        String userImg = document.getString("userImg");
                        String currentDate = document.getString("currentDate");
                        Boolean isLiked = document.getBoolean("isLiked");
                        if (isLiked == null) isLiked = false;
                        String userId = document.getString("uid");

                        Post post = new  Post(id, caption, postImg);
                        post.setUid(userId);
                        post.setFullname(fullname);
                        post.setUserImg(userImg);
                        post.setCurrentDate(currentDate);
                        post.setLiked(isLiked);
                        posts.add(post);
                    }
                    handler.onSuccess(posts);
                } else {
                    handler.onError(task.getException());
                }
            }
        });
    }

    public static void followUser(User me, User to, DBFollowHandler handler){
        //User(to) in my following
        database.collection(USER_PATH).document(me.getUid()).collection(FOLLOWING_PATH).document(to.getUid()).set(to).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                database.collection(USER_PATH).document(to.getUid()).collection(FOLLOWERS_PATH).document(me.getUid()).set(me).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        handler.onSuccess(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handler.onError(e);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });



    }

    public static void unFollowUser(User me, User to, DBFollowHandler handler){
        //User(to) in my following
        database.collection(USER_PATH).document(me.getUid()).collection(FOLLOWING_PATH).document(to.getUid())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                database.collection(USER_PATH).document(to.getUid()).collection(FOLLOWERS_PATH).document(me.getUid())
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        handler.onSuccess(true);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handler.onError(e);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });
    }

    public static void loadFollowing(String uid,DBUsersHandler handler ) {
        database.collection(USER_PATH).document(uid).collection(FOLLOWING_PATH).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<User> users = new  ArrayList<User>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String uid = document.getString("uid");
                                String fullname = document.getString("fullname");
                                String email = document.getString("email");
                                String userImg = document.getString("userImg");
                                User user = new User(fullname, email, userImg);
                                user.setUid(uid);
                                users.add(user);
                            }
                            handler.onSuccess(users);
                        } else {
                            handler.onError(task.getException());
                        }
                    }
                });
    }

    public static void loadFollowers(String uid,DBUsersHandler handler) {
        database.collection(USER_PATH).document(uid).collection(FOLLOWERS_PATH).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<User> users = new  ArrayList<User>();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String uid = document.getString("uid");
                                String fullname = document.getString("fullname");
                                String email = document.getString("email");
                                String userImg = document.getString("userImg");
                                User user = new User(fullname, email, userImg);
                                user.setUid(uid);
                                users.add(user);
                            }
                            handler.onSuccess(users);
                        } else {
                            handler.onError(task.getException());
                        }
                    }
                });
    }

    public static void storePostsToMyFeed(String uid, User to) {
        loadPosts(to.getUid(), new DBPostsHandler() {
            @Override
            public void onSuccess(ArrayList<Post> posts) {
                for (Post post : posts) {
                    storeFeed(uid, post);
                }
            }

            @Override
            public void onError(Exception exception) {

            }
        });
    }
    public static void storeFeed(String uid ,Post post) {
        CollectionReference reference = database.collection(USER_PATH).document(uid).collection(FEED_PATH);
        reference.document(post.getId()).set(post);
    }

    public static void removePostsFromMyFeed(String uid, User to) {
        loadPosts(to.getUid(), new DBPostsHandler() {
            @Override
            public void onSuccess(ArrayList<Post> posts) {
                for (Post post : posts) {
                    removeFeed(uid, post);
                }
            }

            @Override
            public void onError(Exception exception) {

            }
        });
    }

    public static void removeFeed(String uid ,Post post) {
        CollectionReference reference = database.collection(USER_PATH).document(uid).collection(FEED_PATH);
        reference.document(post.getId()).delete();
    }

    public static void likeFeedPost(String uid ,Post post) {
        database.collection(USER_PATH).document(uid).collection(FEED_PATH).document(post.getId())
                .update("isLiked", post.isLiked());
        if (uid.equals(post.getUid()))
            database.collection(USER_PATH).document(uid).collection(POST_PATH).document(post.getId())
                    .update("isLiked", post.isLiked());
    }

    public static void loadLikedFeeds(String uid ,DBPostsHandler handler) {
        Query reference = database.collection(USER_PATH).document(uid).collection(FEED_PATH)
                .whereEqualTo("isLiked", true);
        reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                ArrayList<Post> posts = new  ArrayList<Post>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getString("id");
                        String caption = document.getString("caption");
                        String postImg = document.getString("postImg");
                        String fullname = document.getString("fullname");
                        String userImg = document.getString("userImg");
                        String currentDate = document.getString("currentDate");
                        Boolean isLiked = document.getBoolean("isLiked");
                        if (isLiked == null) isLiked = false;
                        String userId = document.getString("uid");

                        Post post = new  Post(id, caption, postImg);
                        post.setUid(userId);
                        post.setFullname(fullname);
                        post.setUserImg(userImg);
                        post.setCurrentDate(currentDate);
                        post.setLiked(isLiked);
                        posts.add(post);
                    }
                    handler.onSuccess(posts);
                } else {
                    handler.onError(task.getException());
                }
            }
        });
    }

    public static void deletePost(Post post,DBPostHandler handler) {
        CollectionReference reference1 = database.collection(USER_PATH).document(post.getUid()).collection(POST_PATH);
        reference1.document(post.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                CollectionReference reference2 = database.collection(USER_PATH).document(post.getUid()).collection(FEED_PATH);
                reference2.document(post.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        handler.onSuccess(post);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        handler.onError(e);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                handler.onError(e);
            }
        });
    }
}
