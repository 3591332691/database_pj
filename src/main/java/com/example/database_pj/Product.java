package com.example.database_pj;

import com.example.database_pj.ObserverTool.Observer;
import com.example.database_pj.ObserverTool.Subject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Product extends Subject {
    private List<Observer> ObserverList;
    public int ProductId;
    public String Name;
    public String Category;
    public String Origin;
    public LocalDate ProductionDate;
    public int PlatformId;
    public int MerchantId;
    public double Price;
    public String MerchantName;
    public String PlatformName;

    /**
     * 根据ProductId来初始化
     */
    public Product(int Product_ID) throws SQLException {
        ProductId = Product_ID;
        //从商品库里找到商品信息
        SQLHelper a = new SQLHelper();
        String query = "SELECT * FROM Product WHERE ProductId = '" + ProductId + "'";
        ResultSet resultset = a.executeQuery(query);
        while (resultset.next()) {
            Name = resultset.getString("Name");
            Category = resultset.getString("Category");
            Origin = resultset.getString("Origin");
            //转换
            Date sqlDate = resultset.getDate("Production_Date");
            ProductionDate = ((java.sql.Date) sqlDate).toLocalDate();

            PlatformId = resultset.getInt("ProductId");
            MerchantId = resultset.getInt("MerchantId");
            Price = resultset.getDouble("Price");
        }
        a.close();
        //从商户库里找到商户名字
        SQLHelper b = new SQLHelper();
        String queryB = "SELECT * FROM Merchant WHERE MerchantId = '" + MerchantId + "'";
        ResultSet resultsetB = b.executeQuery(queryB);
        while (resultsetB.next()) {
            MerchantName = resultsetB.getString("Name");
        }
        b.close();
        //从平台库里找到平台名字
        SQLHelper c = new SQLHelper();
        String queryC = "SELECT * FROM Platform WHERE PlatformId = '" + PlatformId + "'";
        ResultSet resultsetC = c.executeQuery(queryC);
        while (resultsetC.next()) {
            PlatformName = resultsetC.getString("PlatformName");
        }
        c.close();

    }

    /**
     * 用于初始化ObserverList
     */
    private void initialObserverList() throws SQLException {
        SQLHelper a = new SQLHelper();
        String query = "Select UserId from Favorite Where ProductId = " + ProductId;
        ResultSet resultSet = a.executeQuery(query);
        while (resultSet.next()) {
            String username = selectUsernameFromUserId(resultSet.getInt("UserId"));
            User temp = new User(username);
            ObserverList.add(temp);
        }
    }

    @Override
    public void notifyObserver() throws SQLException {
        initialObserverList();
        Iterator<Observer> iterator = ObserverList.iterator();
        while (iterator.hasNext()) {
            Observer observer = iterator.next();
            observer.update(this);
        }
    }

    /**
     * 已知UserId,返回username
     *
     * @return username
     */
    private String selectUsernameFromUserId(int UserId) throws SQLException {
        String username = "";
        SQLHelper a = new SQLHelper();
        String query = "select Name From User Where UserId = " + UserId;
        ResultSet resultSet = a.executeQuery(query);
        while (resultSet.next()) {
            username = resultSet.getString("Name");
        }
        return username;
    }
}
