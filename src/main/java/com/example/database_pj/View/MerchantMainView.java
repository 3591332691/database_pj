package com.example.database_pj.View;

import com.example.database_pj.Merchant;
import com.example.database_pj.SQLHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.List;

public class MerchantMainView {
    @FXML
    private ListView<String> productList;
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

    public void initialize() {
        if (merchant_name != null && merchant_address != null) {
            merchant_name.setText("名字： " + merchant.Name);
            merchant_address.setText("地址： " + merchant.Address);
        }
        if (productList != null) {
            List<String> names = merchant.getProductNames();
            productList.getItems().addAll(names);
        }
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
     * 用于刷新页面
     *
     * @throws SQLException
     */
    private void refreshScene() throws SQLException {
        String a = merchant.Name;
        productList.getItems().clear();

        merchant = new Merchant(a);
        initialize();
    }

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

        //都填写正确的话,就上传到数据库
        SQLHelper a = new SQLHelper();
        String query = "INSERT INTO Product (Name, Category, Origin ,Production_Date, MerchantId,Price, PlatformId) " +
                "VALUES ('" + productName + "', '" + productCategory + "', '" + productOrigin +
                "', '" + productProductionDate + "', '" + merchant.MerchantId + "', '" + sqlPrice +
                "', '" + selectedPlatformId +
                "')";
        boolean success = a.executeUpdate(query);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (success == true) {
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
