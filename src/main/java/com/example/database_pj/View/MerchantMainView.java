package com.example.database_pj.View;

import com.example.database_pj.Merchant;
import com.example.database_pj.PriceCompareApplication;
import com.example.database_pj.Product;
import com.example.database_pj.SQLHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicReference;

public class MerchantMainView {
    @FXML
    private ListView productList;
    @FXML
    private Text merchant_name;
    @FXML
    private Text merchant_address;
    @FXML
    private Button releaseButton;
    @FXML
    private TextField productNameTextField;
    @FXML
    private TextField productPriceTextField;
    @FXML
    private TextField productCategoryTextField;
    @FXML
    private TextField productOriginTextField;
    @FXML
    private TextField productProductionDateTextField;
    @FXML
    private ComboBox<String> publishPlatformComboBox;
    Merchant merchant;

    public MerchantMainView(String name) throws SQLException {
        merchant = new Merchant(name);
    }

    public void initialize() throws SQLException {
        if (merchant_name != null && merchant_address != null) {
            merchant_name.setText("名字： " + merchant.Name);
            merchant_address.setText("地址： " + merchant.Address);
        }

        initialProductList();
        productList.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
                // 双击，打开商品详情页
                HBox selectedItem = (HBox) productList.getSelectionModel().getSelectedItem();
                if (selectedItem != null && selectedItem.getChildren().size() > 0) {
                    // 获取选中项的第一个孩子节点
                    Text firstChild = (Text) selectedItem.getChildren().get(0);
                    String a = firstChild.getText();
                    OpenMerchantProductScene(a);
                }
            }
        });
        if (releaseButton != null)
            releaseButton.setOnAction(event -> {
                try {
                    handlereleaseButton();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
    }

    /**
     * 点击修改价格按钮之后的函数
     *
     * @param a
     */
    private void ClickModifyPriceButton(Product a) throws SQLException, InterruptedException {
        AtomicReference<String> inputText = new AtomicReference<>("");
        Stage stage = new Stage();
        stage.setTitle("设置新价格");

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
            double newPrice = numericValue;
            if (a.ModifyPriceTo(newPrice)) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提醒");
                alert.setHeaderText(null);
                alert.setContentText("修改价格成功！");
                alert.showAndWait();
                refreshScene();
            }

        } else {
            // inputText 中的内容不全是数字
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("请输入有效的数字！");
            alert.showAndWait();
        }
    }

    /**
     * 用于初始化商家的已上架的商品页面
     */
    private void initialProductList() throws SQLException {
        SQLHelper a = new SQLHelper();
        String query = "SELECT * FROM Product WHERE MerchantId =" + merchant.MerchantId;
        ResultSet resultSet = a.executeQuery(query);
        while (resultSet != null && resultSet.next()) {
            int tempProductId = resultSet.getInt("ProductId");
            Product tempProduct = new Product(tempProductId);
            //系列商品的简略信息，包括商品名称、商品所属商家、平台、价格等。
            Text productText = new Text(tempProduct.ProductId + "." + tempProduct.Name +
                    " " + tempProduct.PlatformName + " 价格：" + tempProduct.Price);
            Button favoriteButton = new Button("修改价格");
            favoriteButton.setOnAction(event -> {
                try {
                    ClickModifyPriceButton(tempProduct);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            HBox productContainer = new HBox();
            productContainer.setSpacing(10);
            productContainer.getChildren().addAll(productText, favoriteButton);
            productList.getItems().add(productContainer);
        }
    }

    /**
     * 双击商品的话，打开一个给商户看的商品页面
     * @param selectedProduct
     */
    private void OpenMerchantProductScene(String selectedProduct) {
        int ProductId = Character.getNumericValue(selectedProduct.charAt(0));
        try {
            FXMLLoader loader = new FXMLLoader(PriceCompareApplication.class.getResource("merchant_product.fxml"));
            Parent root = loader.load();

            // 获取新页面的控制器
            MerchantProductView newController = loader.getController();
            // 调用新页面控制器的方法，传递selectedProduct参数
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
     * 用于上传成功后刷新右侧商品页面
     * @throws SQLException
     */
    private void refreshScene() throws SQLException {
        String a = merchant.Name;
        productList.getItems().clear();

        merchant = new Merchant(a);
        initialize();
    }

    /**
     * 点击发布按钮的话,发布商品
     *
     * @throws SQLException
     */
    private void handlereleaseButton() throws SQLException {
        String productName = productNameTextField.getText();
        String productPrice = productPriceTextField.getText();
        String productCategory = productCategoryTextField.getText();
        String productOrigin = productOriginTextField.getText();
        String productProductionDate = productProductionDateTextField.getText();
        String selectedPlatform = publishPlatformComboBox.getValue();

        // 检查字段是否为空
        if (productName.isEmpty() || productPrice.isEmpty() ||
                productCategory.isEmpty() || productOrigin.isEmpty() ||
                productProductionDate.isEmpty() || selectedPlatform == null) {
            // 有字段为空，显示错误消息或执行相应的操作
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("您的表单未填写完整");
            alert.showAndWait();
            return;
        }
        int selectedPlatformId = Character.getNumericValue(selectedPlatform.charAt(0));
        // 检查 productPrice 是否为数字和小数点，最多保留两位小数
        if (!productPrice.matches("\\d+(\\.\\d{1,2})?")) {
            // productPrice 不符合要求
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("商品价格格式不正确");
            alert.showAndWait();
            return;
        }
        double sqlPrice = Double.parseDouble(productPrice);

        // 检查 productProductionDate 是否符合格式要求
        if (!productProductionDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            // productProductionDate 不符合要求
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("商品生产日期格式不正确");
            alert.showAndWait();
            return;
        }

        //都填写正确的话,就上传到Product表和价格历史表
        SQLHelper a = new SQLHelper();
        String query = "INSERT INTO Product (Name, Category, Origin ,Production_Date, MerchantId,Price, PlatformId) " +
                "VALUES ('" + productName + "', '" + productCategory + "', '" + productOrigin +
                "', '" + productProductionDate + "', '" + merchant.MerchantId + "', '" + sqlPrice +
                "', '" + selectedPlatformId +
                "')";
        boolean successA = a.executeUpdate(query);
        a.close();
        if (successA == true) {
            //1.找到刚才上传的商品的id
            int Max_ProductId = 0;
            SQLHelper searchId = new SQLHelper();
            String queryS = "Select max(ProductId) as MaxProductId" +
                    " from Product " +
                    "Where MerchantId = " + merchant.MerchantId;
            ResultSet resultSetS = searchId.executeQuery(queryS);
            while (resultSetS.next()) {
                Max_ProductId = resultSetS.getInt("MaxProductId");
            }
            searchId.close();
            //2.上传到价格历史表
            // 获取当前日期
            LocalDate currentDate = LocalDate.now();
            // 将日期转化为 SQL 的 DATE 类型
            java.sql.Date sqlDate = java.sql.Date.valueOf(currentDate);
            SQLHelper b = new SQLHelper();
            String queryB = "INSERT INTO PriceHistory (ProductId,MerchantId,PlatformId,Price,Date) " +
                    "VALUES ('" + Max_ProductId + "', '" + merchant.MerchantId + "', '" + selectedPlatformId +
                    "', '" + sqlPrice + "', '" + sqlDate +
                    "')";
            boolean successB = b.executeUpdate(queryB);
            b.close();
        }
        //
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (successA == true) {
            alert.setTitle("提示");
            alert.setHeaderText(null);
            alert.setContentText("上传成功！");
            refreshScene();
        } else {
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("上传失败！");
        }
        alert.showAndWait();
    }
}
