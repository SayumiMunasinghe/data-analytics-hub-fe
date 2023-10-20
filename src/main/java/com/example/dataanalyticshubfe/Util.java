package com.example.dataanalyticshubfe;

import javafx.scene.control.Alert;

public class Util {
    public static boolean isNumeric(String input) {
        try {
            int intInput = Integer.parseInt(input);
            if (intInput < 0) {
                return false;
            }
        }catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
    public static void showAlert(Alert.AlertType a, String cmdOutput, String userOutput) {
        System.out.println(cmdOutput);
        Alert alert = new Alert(a);
        alert.setContentText(userOutput);
        alert.show();
    }
}
