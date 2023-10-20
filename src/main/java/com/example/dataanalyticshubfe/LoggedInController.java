package com.example.dataanalyticshubfe;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoggedInController implements Initializable {

    @FXML
    private Button button_logout;
    @FXML
    private Button button_profile, button_posts;
    @FXML
    private Label label_welcome;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        button_logout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "sample.fxml", "Log in!", null, null);
            }
        });
        button_posts.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.readPosts(event);
                DBUtils.changeScene(event, "posts-view.fxml", "Posts", null, null);
            }
        });
        button_profile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "edit-profile.fxml", "Edit your profile!", null, null);
            }
        });

    }

    public void setUserInformation(String firstName, String lastName) {
        label_welcome.setText("Welcome " + firstName + " " + lastName + "!");
    }
}
