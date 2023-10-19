package com.example.dataanalyticshubfe;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.sql.*;

import java.io.IOException;

public class DBUtils {
    private static DatabaseConnection db;
    private static Connection connection;
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String firstName, String lastName) {
        Parent root = null;
        if (firstName != null && lastName != null) {
           try {
               FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
               root = loader.load();
               LoggedInController loggedInController = loader.getController();
               loggedInController.setUserInformation(firstName, lastName);
           } catch (IOException e) {
               e.printStackTrace();
           }
        } else {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }

    public static void signUpUser(ActionEvent event, String username, String password, String firstName, String lastName) {
//        Useful for interacting with database
//        Connection connection = null;
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {

            db = DatabaseConnection.getInstance();
            connection = db.getCon();
//                    DriverManager.getConnection("jdbc:mysql://localhost:3306/data-analytics-hub", "root", "password");
            psCheckUserExists = connection.prepareStatement("SELECT * FROM Users WHERE username = ?");
            psCheckUserExists.setString(1, username);
            resultSet = psCheckUserExists.executeQuery();

//            Used to check whether result set is empty
//            Will return false if result set is empty
            if (resultSet.isBeforeFirst()) {
                System.out.println("User already exists");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("This username cannot be used.");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO Users (username, password, firstName, lastName) VALUES (?, ?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, firstName);
                psInsert.setString(4, lastName);
                psInsert.executeUpdate();

                changeScene(event, "logged-in.fxml", "Welcome!", firstName, lastName);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void loginUser(ActionEvent event, String username, String password) {
//        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            db = DatabaseConnection.getInstance();
            connection = db.getCon();
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/data-analytics-hub", "root", "password");
            preparedStatement = connection.prepareStatement("SELECT password, firstName, lastName FROM Users WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("User not found in the database!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Provided credentials are incorrect!");
                alert.show();
            } else {
                while (resultSet.next()) {
                    String retrievedPassword = resultSet.getString("password");
                    String retrievedFirstName = resultSet.getString("firstName");
                    String retrievedLasName = resultSet.getString("lastName");
                    if (retrievedPassword.equals(password)) {
                        changeScene(event, "logged-in.fxml", "Welcome!", retrievedFirstName, retrievedLasName);
                    } else {
                        System.out.println("Password is incorrect!");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Provided credentials did not match!");
                        alert.show();
                    }
                }
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
