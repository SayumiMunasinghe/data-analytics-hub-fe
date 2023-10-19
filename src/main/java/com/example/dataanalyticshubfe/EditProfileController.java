package com.example.dataanalyticshubfe;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {

    @FXML
    private Button button_home, button_update;
    @FXML
    private Label label_heading;
    @FXML
    private TextField tf_username, tf_password, tf_first_name, tf_last_name;
    UserData userData = UserData.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label_heading.setText("Update your profile " + userData.getFirstName());
        tf_username.setText(userData.getUsername());
        tf_password.setText(userData.getPassword());
        tf_first_name.setText(userData.getFirstName());
        tf_last_name.setText(userData.getLastName());
        button_home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "logged-in.fxml", "Log in!", userData.getFirstName(), userData.getLastName());
            }
        });
        button_update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!tf_username.getText().trim().isEmpty() && !tf_password.getText().trim().isEmpty() && !tf_first_name.getText().trim().isEmpty() && !tf_last_name.getText().trim().isEmpty()) {
                    DBUtils.updateUser(event, userData.getUsername(), tf_username.getText(), tf_password.getText(), tf_first_name.getText(), tf_last_name.getText());
                } else {
                    System.out.println("Please fill in all the information!");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please fill in all information to update!");
                    alert.show();
                }
            }
        });
    }


}
