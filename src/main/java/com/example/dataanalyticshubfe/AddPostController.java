package com.example.dataanalyticshubfe;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddPostController implements Initializable {

    @FXML
    private Button button_add_post, button_home;
    @FXML
    private TextField tf_id, tf_content, tf_author, tf_likes, tf_shares, tf_date_time;
    static PostList postList = PostList.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_add_post.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean flagId = true;
                boolean flagLikes = true;
                boolean flagShares = true;

                if (!tf_id.getText().trim().isEmpty() && !tf_content.getText().trim().isEmpty() && !tf_author.getText().trim().isEmpty() && !tf_likes.getText().trim().isEmpty() && !tf_shares.getText().trim().isEmpty() && !tf_date_time.getText().trim().isEmpty()) {

//                    check ID input
                    if (Util.isNumeric(tf_id.getText())) {
                        for (Post p:postList.getPostList()) {
                            if(p.getID() == Integer.parseInt(tf_id.getText())) {
                                flagId = true;
                            } else {
                                flagId = false;
                            }
                        }
                    } else {
                        Util.showAlert(Alert.AlertType.ERROR, "ID input is not int", "Please enter a numeric input for Post ID");
                        flagId = false;
                    }
                    if (flagId){
                        Util.showAlert(Alert.AlertType.ERROR, "ID already in DB", "Please enter a different post ID.");
                    }
//                    check number of likes input
                    if (Util.isNumeric(tf_likes.getText())) {
                        flagLikes = false;
                    } else {
                        Util.showAlert(Alert.AlertType.ERROR, "Likes input is not int", "Please enter a numeric input for number of likes");
                        flagLikes = false;
                    }
//                      check number of shares input
                    if (Util.isNumeric(tf_shares.getText())) {
                        flagShares = false;
                    } else {
                        Util.showAlert(Alert.AlertType.ERROR, "Shares input is not int", "Please enter a numeric input for number of shares");
                        flagShares = false;
                    }

                    if (!flagId && !flagLikes && !flagShares) {
                        DBUtils.addPost(event, Integer.parseInt(tf_id.getText()), tf_content.getText(), tf_author.getText(), Integer.parseInt(tf_likes.getText()), Integer.parseInt(tf_shares.getText()), tf_date_time.getText());
                    } else {
                        Util.showAlert(Alert.AlertType.ERROR, "Inputs not valid", "Error, please try again.");
                    }

                } else {
                    System.out.println("Please fill in all the information!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to sign up!");
                    alert.show();
                }
            }
        });

        button_home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                DBUtils.changeScene(event, "posts-view.fxml", "Posts!", null, null);
            }
        });
    }
}
