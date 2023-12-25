package com.example.database_pj;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class User extends Observer{
    private int UserId;
    public String Name;
    public int Age;
    public String Gender;
    public String Phone;

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
    }

    /**
     * 得到用户收藏的商品
     * @return  查询过程中没有找到匹配的Favorite项目，favoriteProductsArray 将返回一个长度为 0 的空数组
     * @throws SQLException
     */
    public int[] GetFavor() throws SQLException {
        List<Integer> favorite_products_id = new ArrayList<>();
        SQLHelper temp = new SQLHelper();
        String query = "SELECT * FROM Favorite WHERE UserId = '" + UserId + "'";
        ResultSet resultSet = temp.executeQuery(query);

        while (resultSet.next()) {
            favorite_products_id.add(resultSet.getInt("ProductId"));
        }
        temp.close();
        // 将List转换为int数组
        int[] favoriteProductsArray = new int[favorite_products_id.size()];
        for (int i = 0; i < favorite_products_id.size(); i++) {
            favoriteProductsArray[i] = favorite_products_id.get(i);
        }

        return favoriteProductsArray;
    }

    /**
     * 听到价格变动或者加入收藏时
     * 判断价格是否低于设定价格
     * 若是，则发送信息
     */
    @Override
    public void update(){
        //TODO:可能是往message里insert一条信息
    }
}
