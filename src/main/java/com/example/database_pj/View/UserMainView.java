package com.example.database_pj.View;

import com.example.database_pj.PriceCompareApplication;
import com.example.database_pj.Product;
import com.example.database_pj.SQLHelper;
import com.example.database_pj.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicReference;

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
    }
    @FXML
    public StackPane user_main_left_container;
    @FXML
    private VBox PersonalInformationVBox;
    @FXML
    private VBox FavoriteVBox;
    @FXML
    private VBox MessageVBox;
    @FXML
    private Button SearchButton;
    @FXML
    private TextField SearchContentText;
    @FXML
    private ListView<HBox> SearchForProductList;
    @FXML
    private ListView FavoriteList;
    @FXML
    private ListView MessageList;

    public void initialize() throws SQLException {
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
        if (SearchButton != null)
            SearchButton.setOnAction(event -> {
                try {
                    handleSearchButton();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        SearchForProductList.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                // 双击，打开商品详情页
                HBox selectedItem = SearchForProductList.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedItem.getChildren().size() > 0) {
                    // 获取选中项的第一个孩子节点
                    Text firstChild = (Text) selectedItem.getChildren().get(0);
                    String a = firstChild.getText();
                    OpenUserProductScene(a);
                }
            }
        });
        initialMessageBlock();
        initialFavoriteBlock();
    }

    private void initialFavoriteBlock() throws SQLException {
        SQLHelper a = new SQLHelper();
        String query = "Select * From Favorite Where USerId = " + user.getUserId();
        ResultSet resultSet = a.executeQuery(query);
        while (resultSet != null && resultSet.next()) {
            int ProductId = resultSet.getInt("ProductId");
            Product product = new Product(ProductId);
            double currentPrice = product.Price;
            double Price_Limit = resultSet.getDouble("Price_Lower_Limit");
            String ProductName = product.Name;
            String item = ProductId + ". " + ProductName + " Price:" + currentPrice + " Limited Price:" + Price_Limit;
            FavoriteList.getItems().add(item);
        }
    }

    private void initialMessageBlock() throws SQLException {
        SQLHelper a = new SQLHelper();
        String query = "Select * From Message Where UserId = " + user.getUserId();
        ResultSet resultSet = a.executeQuery(query);
        while (resultSet != null && resultSet.next()) {
            int ProductId = resultSet.getInt("ProductId");
            Product product = new Product(ProductId);
            double CurrentPrice = resultSet.getDouble("CurrentPrice");
            String MerchantName = product.MerchantName;
            String PlatformName = product.PlatformName;
            String item = "您收藏的" + product.Name + "已降价到" + CurrentPrice + "! 商家:"
                    + MerchantName + " 平台: " + PlatformName + " 当前价格:" + product.Price;
            MessageList.getItems().add(item);
        }
    }

    /**
     * 打开商品详情页面
     * @param selectedProduct
     */
    private void OpenUserProductScene(String selectedProduct) {
        int ProductId = Character.getNumericValue(selectedProduct.charAt(0));
        try {
            FXMLLoader loader = new FXMLLoader(PriceCompareApplication.class.getResource("user_product.fxml"));
            Parent root = loader.load();
            // 获取新页面的控制器
            UserProductView newController = loader.getController();
            // 调用新页面控制器的方法，传递selectedProduct参数 ProductId
            newController.setProductId(ProductId);
            // 创建新的场景和舞台
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如果点击了搜索按钮，就添加相关内容
     */
    private void handleSearchButton() throws SQLException {
        SearchForProductList.getItems().clear();
        String searchContent = SearchContentText.getText();
        ShowSearchForProductList(searchContent);
    }

    /**
     * handleSearchButton()的辅助函数
     *用来动态改变搜索出来的内容
     * @param searchContent
     * @throws SQLException
     */
    private void ShowSearchForProductList(String searchContent) throws SQLException {
        SQLHelper a = new SQLHelper();
        String query = "SELECT ProductId FROM Product WHERE Category LIKE '%" + searchContent + "%'" +
                "OR Name LIKE '%" + searchContent + "%'";
        ResultSet resultSet = a.executeQuery(query);
        while (resultSet.next()) {
            int tempProductId = resultSet.getInt("ProductId");
            Product tempProduct = new Product(tempProductId);
            //系列商品的简略信息，包括商品名称、商品所属商家、平台、价格等。
            Text productText = new Text(tempProduct.ProductId + "." + tempProduct.Name + " " +
                    tempProduct.MerchantName + " " + tempProduct.PlatformName + " 价格：" + tempProduct.Price);
            Button favoriteButton = new Button("收藏");
            favoriteButton.setOnAction(event -> {
                try {
                    ClickFavoriteButton(tempProduct);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            HBox productContainer = new HBox();
            productContainer.setSpacing(10);
            productContainer.getChildren().addAll(productText, favoriteButton);
            SearchForProductList.getItems().add(productContainer);
        }
    }

    /**
     * 点击了收藏按钮
     *
     * @param product 要收藏的商品
     */
    private void ClickFavoriteButton(Product product) throws SQLException {
        //  先判断这个商品是否已经被收藏了，如果还没有的话，点击出现弹窗
        if (user.IsFavor(product)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提醒");
            alert.setHeaderText(null);
            alert.setContentText("您已经收藏过此商品！");
            alert.showAndWait();
        } else {
            AtomicReference<String> inputText = new AtomicReference<>("");
            Stage stage = new Stage();
            stage.setTitle("设置价格提醒下限");

            VBox vbox = new VBox();
            vbox.setSpacing(10);
            vbox.setPadding(new Insets(10));

            TextField textField = new TextField();
            Button button = new Button("确认");

            button.setOnAction(e -> {
                inputText.set(textField.getText()); // 获取输入的文本内容
                stage.close(); // 关闭弹窗
            });

            vbox.getChildren().addAll(textField, button);

            Scene scene = new Scene(vbox, 200, 100);
            stage.setScene(scene);
            stage.showAndWait(); // 等待弹窗关闭

            if (inputText.get().matches("\\d+")) {
                // inputText 中的内容全是数字
                int numericValue = Integer.parseInt(inputText.get()); // 将 inputText 转换为整数
                double lower_limit = numericValue;
                user.addFavorProduct(product, lower_limit);
                initialize();
            } else {
                // inputText 中的内容不全是数字
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setContentText("请输入有效的数字！");
                alert.showAndWait();
            }
        }
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
