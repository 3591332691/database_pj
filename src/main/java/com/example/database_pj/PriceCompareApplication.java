package com.example.database_pj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PriceCompareApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //加载数据库驱动程序
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        //在这里创建表并且注入数据
        SQLHelper a = new SQLHelper();
        a.initializeTables();
        a.close();
        //创建窗体
        FXMLLoader fxmlLoader = new FXMLLoader(PriceCompareApplication.class.getResource("log_in.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 300);
        stage.setTitle("Go Smart Shop!");
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}