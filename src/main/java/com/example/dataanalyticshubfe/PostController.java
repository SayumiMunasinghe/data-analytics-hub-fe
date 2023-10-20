package com.example.dataanalyticshubfe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PostController implements Initializable {

    @FXML
    Button button_home, button_add_post, button_get_post, button_delete_post;
    @FXML
    TableView<Post> tv_posts;
    @FXML
    TableColumn <Post, Integer> idColumn;
    @FXML
    TableColumn <Post, String> contentColumn;
    @FXML
    TableColumn <Post, String> authorColumn;
    @FXML
    TableColumn <Post, Integer> likesColumn;
    @FXML
    TableColumn <Post, Integer> sharesColumn;
    @FXML
    TableColumn <Post, String> dateTimeColumn;

    static PostList postList = PostList.getInstance();
    UserData userData = UserData.getInstance();
    static PostData postData = PostData.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        idColumn.setCellValueFactory(new PropertyValueFactory<Post, Integer>("ID"));
        contentColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("content"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("author"));
        likesColumn.setCellValueFactory(new PropertyValueFactory<Post, Integer>("likes"));
        sharesColumn.setCellValueFactory(new PropertyValueFactory<Post, Integer>("shares"));
        dateTimeColumn.setCellValueFactory(new PropertyValueFactory<Post, String>("dateTime"));

        System.out.println(postData.getID());
        System.out.println(postData.getContent());
        System.out.println(postData.getAuthor());
        System.out.println(postData.getLikes());
        System.out.println(postData.getShares());
        System.out.println(postData.getDateTime());

        if(Objects.isNull(postData) || postData.getID() == 0) {
            tv_posts.setItems(postList.getPostList());
        } else {
            tv_posts.setItems(FXCollections.observableArrayList(new Post(postData.getID(), postData.getContent(), postData.getAuthor(), postData.getLikes(), postData.getShares(), postData.getDateTime())));
            postData.setID(0);
            postData.setContent(null);
            postData.setAuthor(null);
            postData.setLikes(0);
            postData.setShares(0);
            postData.setDateTime(null);
        }
        button_home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "logged-in.fxml", "Welcome!", userData.getFirstName(), userData.getLastName());
            }
        });

        button_add_post.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "add-post.fxml", "Add Post", null, null);

            }
        });

        button_get_post.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "post-get.fxml", "Get a post", null, null);
            }
        });

        button_delete_post.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "post-delete.fxml", "Delete a post", null, null);
            }
        });

    }

}
