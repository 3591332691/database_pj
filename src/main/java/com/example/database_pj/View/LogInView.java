package com.example.database_pj.View;

import com.example.database_pj.PriceCompareApplication;
import com.example.database_pj.SQLHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LogInView {

    @FXML
    private TextField usernameField;
    @FXML
    private void login() throws SQLException, IOException {
        String username = usernameField.getText();
        // 处理登录逻辑
        SQLHelper a = new SQLHelper();
        if(a.isUserNameExists(username)==0&&a.isMerchantNameExists(username)==0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("您的用户名未注册");
            alert.showAndWait();
        } else if (a.isUserNameExists(username) != 0) {
            switchToUserMainScene(username);
        } else {//他是商户
            switchToMerchantMainScene(username);
        }
    }

    private void switchToUserMainScene(String username) throws IOException, SQLException {
        // 创建新的窗体（Stage）
        Stage newStage = new Stage();

        // 设置新窗体的内容
        UserMainView controller = new UserMainView(username);
        FXMLLoader loader = new FXMLLoader(PriceCompareApplication.class.getResource("user_main.fxml"));
        loader.setController(controller);
        Parent root = loader.load(); // 先加载FXML文件
        Scene scene = new Scene(root, 800, 500); // 设置Scene

        newStage.setTitle("welcome! "+username);
        newStage.setScene(scene);

        // 关闭或销毁原来的窗体（Stage）
        Stage originalStage = (Stage) usernameField.getScene().getWindow();
        originalStage.close();

        // 显示新窗体
        newStage.show();
    }

    private void switchToMerchantMainScene(String username) throws IOException, SQLException {
        // 创建新的窗体（Stage）
        Stage newStage = new Stage();
        // 设置新窗体的内容，把新窗体和新的控制器连接在一起
        MerchantMainView controller = new MerchantMainView(username);
        //set location
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(PriceCompareApplication.class.getResource("merchant_main.fxml"));

        loader.setController(controller);
        Parent root = loader.load(); // 先加载FXML文件
        Scene scene = new Scene(root, 800, 500); // 设置Scene

        newStage.setTitle("welcome! " + username);
        newStage.setScene(scene);

        // 关闭或销毁原来的窗体（Stage）
        Stage originalStage = (Stage) usernameField.getScene().getWindow();
        originalStage.close();

        // 显示新窗体
        newStage.show();
    }

}