package com.example.dataanalyticshubfe;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DeletePostController implements Initializable {

    @FXML
    private Button button_delete_post, button_posts;
    @FXML
    private TextField tf_post_id;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_delete_post.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (Util.isNumeric(tf_post_id.getText())) {
                    DBUtils.deletePost(event, Integer.parseInt(tf_post_id.getText()));
                }

            }
        });
        button_posts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "posts-view.fxml", "Posts", null, null);
            }
        });


    }


}
