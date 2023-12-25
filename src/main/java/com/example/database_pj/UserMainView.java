package com.example.database_pj;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.SQLException;

public class UserMainView {
    @FXML
    private Button ToPersonalInformationButton;
    @FXML
    private Button ToFavoriteButton;
    @FXML
    private Button ToMessageButton;
    User user;
    @FXML
    Text Name;
    @FXML
    Text Age;
    @FXML
    Text Gender;
    @FXML
    Text Phone;
    public UserMainView(String username) throws SQLException {
        user = new User(username);
        initialize();
    }
    @FXML
    public StackPane user_main_left_container;
    @FXML
    private VBox PersonalInformationVBox;
    @FXML
    private VBox FavoriteVBox;
    @FXML
    private VBox MessageVBox;

    public void initialize() {
        if(user_main_left_container!=null)
        {
            if(Name!=null)Name.setText("Name: "+user.Name);
            if(Age!=null)Age.setText("Age: "+String.valueOf(user.Age));
            if(Gender!=null)Gender.setText("Gender: "+user.Gender);
            if(Phone!=null)Phone.setText("Phone: "+user.Phone);
        }
        if(ToPersonalInformationButton!=null)
            ToPersonalInformationButton.setOnAction(event -> handleToPersonalInformationButton());
        if(ToMessageButton!=null)
            ToMessageButton.setOnAction(event ->handleToMessageButton());
        if(ToFavoriteButton!=null)
            ToFavoriteButton.setOnAction(event ->handleToFavoriteButton());

    }
    //按钮被按
    private void handleToPersonalInformationButton(){
        PersonalInformationVBox.toFront();
    }
    public void handleToFavoriteButton() {
        FavoriteVBox.toFront();
    }
    public void handleToMessageButton() {
        MessageVBox.toFront();
    }


}
