package com.example.database_pj;

import com.example.database_pj.ObserverTool.Observer;
import com.example.database_pj.ObserverTool.Subject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User extends Observer {
    private static int UserId;
    public String Name;
    public int Age;
    public String Gender;
    public String Phone;
    public static List<Product> FavorProduct = new ArrayList<Product>();

    public int getUserId() {
        return UserId;
    }

    public User(String a) throws SQLException {
        Name = a;
        SQLHelper temp = new SQLHelper();
        String query = "SELECT * FROM User WHERE Name = '" + Name + "'";
        ResultSet resultSet = temp.executeQuery(query);
        while (resultSet.next()) {
            // 获取查询结果的各个属性值
            UserId = resultSet.getInt("UserId");
            Age = resultSet.getInt("Age");
            Gender = resultSet.getString("Gender");
            Phone = resultSet.getString("Phone");
        }
        temp.close();
        initialFavorProductId();
    }

    /**
     * USer把这个商品以price_lower_limit的价格进行收藏
     * @param tempProduct 收藏的商品
     * @param price_lower_limit 设定的价格
     */
    public void addFavorProduct(Product tempProduct, double price_lower_limit) throws SQLException {
        FavorProduct.add(tempProduct);
        SQLHelper a = new SQLHelper();
        String query = "INSERT INTO Favorite (UserId, ProductId ,MerchantId, PlatformId,Price_Lower_Limit) " +
                "VALUES ('" + User.UserId + "', '" + tempProduct.ProductId + "', '" + tempProduct.MerchantId +
                "', '" + tempProduct.PlatformId + "', '" + price_lower_limit +
                "')";
        boolean successA = a.executeUpdate(query);
        a.close();
        update(tempProduct);
    }

    /**
     * 用于初始化FavorProduct
     *
     * @throws SQLException
     */
    private void initialFavorProductId() throws SQLException {
        if (!FavorProduct.isEmpty())
            FavorProduct.clear();
        SQLHelper a = new SQLHelper();
        String query = "Select ProductId From Favorite Where UserId =" + UserId;
        ResultSet resultSet = a.executeQuery(query);
        while (resultSet.next()) {
            Product temp = new Product(resultSet.getInt("ProductId"));
            FavorProduct.add(temp);
        }
    }

    /**
     * 得到用户是否收藏过这个商品
     *
     * @return 如果收藏过，就返回true
     */
    public boolean IsFavor(Product iproduct) {
        if (FavorProduct != null) {
            for (Product product : FavorProduct) {
                if (product.ProductId == iproduct.ProductId) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 听到价格变动或者加入收藏时
     * 判断价格是否低于设定价格
     * 若是，则发送信息
     * @param a 是指商品a
     */
    @Override
    public void update(Subject a) throws SQLException {
        Product newProduct = new Product(((Product) a).ProductId);
        //      查找Favor库，如果a的价格低于User设定的Price_Lower_Limit
        //      就往Message里新增一条信息
        if (IsLowerThanLimit((Product) a)) {
            SQLHelper temp = new SQLHelper();
            String insertQuery = "INSERT INTO Message " +
                    "(UserId,ProductId,MerchantId,PlatformId,CurrentPrice)" +
                    "Values('" + User.UserId + "', '" + newProduct.ProductId + "', '" + newProduct.MerchantId +
                    "', '" + newProduct.PlatformId + "', '" + newProduct.Price +
                    "')";
            boolean successA = temp.executeUpdate(insertQuery);
            temp.close();

        }
    }

    /**
     * 判收藏的断商品a的价格是否小于等于设定的提醒价格
     * @param a 商品
     * @return 如果小于等于设定的提醒价格，返回true
     */
    private static boolean IsLowerThanLimit(Product a) throws SQLException {
        Product changedProduct = new Product(a.ProductId);
        double Price_Lower_Limit = 0;
        SQLHelper searchLimitPrice = new SQLHelper();
        String query = "Select Price_Lower_Limit " +
                "From Favorite " +
                "Where UserId = " + User.UserId + " And ProductId = " + changedProduct.ProductId;
        ResultSet resultSet = searchLimitPrice.executeQuery(query);
        while (resultSet != null && resultSet.next()) {
            Price_Lower_Limit = resultSet.getDouble("Price_Lower_Limit");
        }
        searchLimitPrice.close();
        if (Price_Lower_Limit >= changedProduct.Price) {
            return true;
        }
        return false;
    }
}
