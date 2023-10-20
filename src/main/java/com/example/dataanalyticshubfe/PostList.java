package com.example.dataanalyticshubfe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PostList {
    private static final PostList instance = new PostList();
    private ObservableList<Post> postList = FXCollections.observableArrayList();


    public static PostList getInstance() {
        return instance;
    }

    public ObservableList<Post> getPostList() {
        return postList;
    }

    public void setPostList(ObservableList<Post> postList) {
        this.postList = postList;
    }





}
