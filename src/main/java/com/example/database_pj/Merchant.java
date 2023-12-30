package com.example.database_pj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Merchant {
    public int MerchantId;
    public String Name;
    public String Address;
    public List<Product> ProductReleased = new ArrayList<>();

    /**
     * 根据merchantName来初始化id address 和发布了的商品
     *
     * @param merchantName
     * @throws SQLException
     */
    public Merchant(String merchantName) throws SQLException {
        GetInformation(merchantName);
        GetMyProduct();
    }

    /**
     * 根据商户名来初始化id address
     *
     * @param merchantName
     */
    public void GetInformation(String merchantName) throws SQLException {
        Name = merchantName;
        SQLHelper a = new SQLHelper();
        String query = "SELECT * FROM Merchant WHERE Name = '" + Name + "'";
        ResultSet resultSet = a.executeQuery(query);
        while (resultSet.next()) {
            MerchantId = resultSet.getInt("MerchantId");
            Address = resultSet.getString("Address");
        }
        a.close();
    }

    /**
     * 初始化ProductReleased
     *
     * @throws SQLException
     */
    public void GetMyProduct() throws SQLException {
        SQLHelper a = new SQLHelper();
        String query = "SELECT * FROM Product WHERE MerchantId = '" + MerchantId + "'";
        ResultSet resultSet = a.executeQuery(query);
        while (resultSet.next()) {
            int ProductId = resultSet.getInt("ProductId");
            Product tempProduct = new Product(ProductId);
            ProductReleased.add(tempProduct);
        }
        a.close();
    }

    /**
     * 用于展示商户发布的商品们
     *
     * @return 返回的是ProductId-Name 这样格式的List<String>
     */
    public List<String> getProductNames() {
        List<String> productNames = new ArrayList<>();
        if (ProductReleased != null) {
            for (Product product : ProductReleased) {
                String name = product.ProductId + " - " + product.Name;
                productNames.add(name);
            }
        }
        return productNames;
    }
}
