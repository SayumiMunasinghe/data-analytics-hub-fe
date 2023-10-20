package com.example.dataanalyticshubfe;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    static UserData userData = UserData.getInstance();
    static PostList postList = PostList.getInstance();
    static PostData postData = PostData.getInstance();
    
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
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {

            db = DatabaseConnection.getInstance();
            connection = db.getCon();
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
                readUser(event, username);
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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            db = DatabaseConnection.getInstance();
            connection = db.getCon();
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
                    String retrievedLastName = resultSet.getString("lastName");
                    if (retrievedPassword.equals(password)) {
                        readUser(event, username);
                        changeScene(event, "logged-in.fxml", "Welcome!", retrievedFirstName, retrievedLastName);
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

    public static void readUser(ActionEvent event, String username) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            db = DatabaseConnection.getInstance();
            connection = db.getCon();
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
                    String retrievedLastName = resultSet.getString("lastName");

                    userData.setUsername(username);
                    userData.setPassword(retrievedPassword);
                    userData.setFirstName(retrievedFirstName);
                    userData.setLastName(retrievedLastName);
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
    public static void updateUser(ActionEvent event, String initialUsername, String username, String password, String firstName, String lastName) {
//        Useful for interacting with database
        PreparedStatement psUpdate = null;
        PreparedStatement psCheckUserExists = null;
        ResultSet resultSet = null;

        try {

            db = DatabaseConnection.getInstance();
            connection = db.getCon();
            psCheckUserExists = connection.prepareStatement("SELECT * FROM Users WHERE username = ?");
            psCheckUserExists.setString(1, initialUsername);

            resultSet = psCheckUserExists.executeQuery();

//            Used to check whether result set is empty
//            Will return false if result set is empty
            if (resultSet.isBeforeFirst()) {
                psUpdate = connection.prepareStatement("UPDATE Users SET username = ?, password = ?, firstName = ?, lastName = ? WHERE username = ?");
                psUpdate.setString(1, username);
                psUpdate.setString(2, password);
                psUpdate.setString(3, firstName);
                psUpdate.setString(4, lastName);
                psUpdate.setString(5, initialUsername);
                psUpdate.executeUpdate();
                readUser(event, username);
                changeScene(event, "logged-in.fxml", "Welcome!", firstName, lastName);
            } else {
                System.out.println("This user does not exist");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Update unsuccessful.");
                alert.show();


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
            if (psUpdate != null) {
                try {
                    psUpdate.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void readPosts(ActionEvent event) {
        ObservableList<Post> tempList = FXCollections.observableArrayList();
                
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            db = DatabaseConnection.getInstance();
            connection = db.getCon();
            preparedStatement = connection.prepareStatement("SELECT * FROM Posts");
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("No posts available in the DB");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("No posts yet!");
                alert.show();
            } else {
                while (resultSet.next()) {
                    int ID = resultSet.getInt("ID");
                    String content = resultSet.getString("content");
                    String author = resultSet.getString("author");
                    int likes = resultSet.getInt("likes");
                    int shares = resultSet.getInt("shares");
                    String dateTime = resultSet.getString("dateTime");

                    tempList.add(new Post(ID, content, author, likes, shares, dateTime));
                }
                postList.setPostList(tempList);
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

    public static void addPost(ActionEvent event, int ID, String content, String author, int likes, int shares, String dateTime) {
//        Useful for interacting with database
        PreparedStatement psInsert = null;
        PreparedStatement psCheckIdExists = null;
        ResultSet resultSet = null;

        try {

            db = DatabaseConnection.getInstance();
            connection = db.getCon();
            psCheckIdExists = connection.prepareStatement("SELECT * FROM Posts WHERE ID = ?");
            psCheckIdExists.setInt(1, ID);
            resultSet = psCheckIdExists.executeQuery();

//            Used to check whether result set is empty
//            Will return false if result set is empty
            if (resultSet.isBeforeFirst()) {
                System.out.println("Post ID already exists");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("This post ID cannot be used.");
                alert.show();
            } else {
                psInsert = connection.prepareStatement("INSERT INTO Posts (ID, content, author, likes, shares, dateTime) VALUES (?, ?, ?, ?, ?, ?)");
                psInsert.setInt(1, ID);
                psInsert.setString(2, content);
                psInsert.setString(3, author);
                psInsert.setInt(4, likes);
                psInsert.setInt(5, shares);
                psInsert.setString(6, dateTime);
                psInsert.executeUpdate();
                readPosts(event);
                changeScene(event, "posts-view.fxml", "Welcome!", null, null);
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
            if (psCheckIdExists != null) {
                try {
                    psCheckIdExists.close();
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

    public static void getPost(ActionEvent event, int postId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Post post = null;
        try {
            db = DatabaseConnection.getInstance();
            connection = db.getCon();
            preparedStatement = connection.prepareStatement("SELECT * FROM Posts WHERE ID = ?");
            preparedStatement.setInt(1, postId);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("Post with given ID does not exist!");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Post not found in the database!");
                alert.show();
            } else {
                while (resultSet.next()) {
                    int ID = resultSet.getInt("ID");
                    String content = resultSet.getString("content");
                    String author = resultSet.getString("author");
                    int likes = resultSet.getInt("likes");
                    int shares = resultSet.getInt("shares");
                    String dateTime = resultSet.getString("dateTime");

                    postData.setID(ID);
                    postData.setContent(content);
                    postData.setAuthor(author);
                    postData.setLikes(likes);
                    postData.setShares(shares);
                    postData.setDateTime(dateTime);

//                    postList.setPostList(FXCollections.observableArrayList(new Post(pos)));

                    changeScene(event, "posts-view.fxml", "Post with ID " + ID, null, null);
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

    public static void deletePost(ActionEvent event, int postId) {
        PreparedStatement preparedStatement = null;
        PreparedStatement psDeletePost = null;
        ResultSet resultSet = null;
        Post post = null;
        try {
            db = DatabaseConnection.getInstance();
            connection = db.getCon();
            preparedStatement = connection.prepareStatement("SELECT * FROM Posts WHERE ID = ?");
            preparedStatement.setInt(1, postId);
            resultSet = preparedStatement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                Util.showAlert(Alert.AlertType.ERROR, "Post with given ID does not exist!", "Post not found in the database!");
            } else {
                psDeletePost = connection.prepareStatement("DELETE FROM Posts WHERE ID = ?");
                psDeletePost.setInt(1, postId);
                boolean result = psDeletePost.execute();

                if(!result) {
                    Util.showAlert(Alert.AlertType.CONFIRMATION, "Post Deleted", "Post Successfully Deleted");
                } else {
                    Util.showAlert(Alert.AlertType.ERROR, "Post Could Not Be Deleted", "Post Not Deleted. Please try again");
                }
                readPosts(event);
//                changeScene(event, "posts-view.fxml", "Posts", null, null);
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
