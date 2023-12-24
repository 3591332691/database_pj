package com.example.database_pj;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LogIn {
    @FXML
    private TextField usernameField;

    @FXML
    private Button loginButton;

    @FXML
    private void login() {
        String username = usernameField.getText();
        // 处理登录逻辑
    }
}