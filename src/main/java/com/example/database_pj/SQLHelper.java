package com.example.database_pj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SQLHelper {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/database_pj";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    // 创建一个Logger实例，用于更快的debug
    private static final Logger logger = Logger.getLogger(SQLHelper.class.getName());
    private Connection connection;

    public SQLHelper() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 在应用程序开始的时候创建表并且注入数据
     */
    public void initializeTables() {
        //如果连接成功了
        if(connection!=null){
            //只有在没有表的时候才会创建
            createTableUser();
            createTableMerchant();
            createTablePlatform();
            createTableProduct();
            createTableFavorite();
            createTableMessage();
            createTablePriceHistory();
            //注入数据
            injectUserData();
            injectMerchantData();
            injectPlatformData();
            injectProductData();
        }
    }

    /**
     * 用来注入User数据
     */
    private void injectUserData(){
        try {
            // 读取userData.csv文件
            String csvFilePath = "userData.csv";
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            String line;
            // 逐行读取CSV文件并插入到User表中
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                // 解析CSV行的数据
                int userId = Integer.parseInt(fields[0]);
                String name = fields[1];
                int age = Integer.parseInt(fields[2]);
                String gender = fields[3];
                String phone = fields[4];

                // 检查用户ID是否已存在,防止重复插入报错
                if (isTableIdExists("User",userId)) {
                    continue;
                }

                // 插入数据到User表
                String sql = "INSERT INTO User (UserId, Name, Age, Gender, Phone) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, userId);
                preparedStatement.setString(2, name);
                preparedStatement.setInt(3, age);
                preparedStatement.setString(4, gender);
                preparedStatement.setString(5, phone);
                preparedStatement.executeUpdate();
            }
            reader.close();
            System.out.println("User数据注入成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用来注入商户数据
     */
    private void injectMerchantData(){
        try {
            // 读取merchantData.csv文件
            String csvFilePath = "merchantData.csv";
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            String line;
            // 逐行读取CSV文件并插入到merchant表中
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                // 解析CSV行的数据
                int MerchantId = Integer.parseInt(fields[0]);
                String MerchantName = fields[1];
                String MerchantAddress = fields[2];
                // 检查ID是否已存在,防止重复插入报错
                if (isTableIdExists("Merchant",MerchantId)) {
                    continue;
                }
                // 插入数据到merchant表
                String sql = "INSERT INTO Merchant (MerchantId, Name, Address) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1,MerchantId);
                preparedStatement.setString(2, MerchantName);
                preparedStatement.setString(3, MerchantAddress);
                preparedStatement.executeUpdate();
            }
            reader.close();
            System.out.println("Merchant数据注入成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 用来注入平台数据
     */
    private void injectPlatformData(){
        try {
            // 读取platformData.csv文件
            String csvFilePath = "platformData.csv";
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            String line;
            // 逐行读取CSV文件并插入到platform表中
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                // 解析CSV行的数据
                int PlatformId = Integer.parseInt(fields[0]);
                String PlatformName = fields[1];
                String PlatformURL = fields[2];
                // 检查ID是否已存在,防止重复插入报错
                if (isTableIdExists("Platform",PlatformId)) {
                    continue;
                }
                // 插入数据到platform表
                String sql = "INSERT INTO Platform (PlatformId, PlatformName, PlatformURL) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, PlatformId);
                preparedStatement.setString(2, PlatformName);
                preparedStatement.setString(3, PlatformURL);
                preparedStatement.executeUpdate();
            }
            reader.close();
            System.out.println("Platform数据注入成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用来注入商品数据
     */
    private void injectProductData() {
        try {
            // 读取productData.csv文件
            String csvFilePath = "productData.csv";
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            String line;
            // 逐行读取CSV文件并插入到product表中
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                // 解析CSV行的数据
                int ProductId = Integer.parseInt(fields[0]);
                String Name = fields[1];
                String Category = fields[2];
                String Origin = fields[3];
                //处理date数据
                String dateString = fields[4];
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDate;
                parsedDate = dateFormat.parse(dateString);
                Date sqlDate = new Date(parsedDate.getTime());
                //
                int MerchantId = Integer.parseInt(fields[5]);
                //处理price
                double Price = Double.parseDouble(fields[6]);
                int PlatformId = Integer.parseInt(fields[7]);
                // 检查ID是否已存在,防止重复插入报错
                if (isTableIdExists("Product", ProductId)) {
                    continue;
                }
                // 插入数据到platform表
                String sql = "INSERT INTO Product (ProductId, Name, Category, Origin ,Production_Date, MerchantId, Price ,PlatformId) " +
                        "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setInt(1, ProductId);
                preparedStatement.setString(2, Name);
                preparedStatement.setString(3, Category);
                preparedStatement.setString(4, Origin);
                preparedStatement.setDate(5, sqlDate);
                preparedStatement.setInt(6, MerchantId);
                preparedStatement.setDouble(7, Price);
                preparedStatement.setInt(8, PlatformId);
                preparedStatement.executeUpdate();
            }
            reader.close();
            System.out.println("Product数据注入成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 用来创建User表
     */
    private void createTableUser(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS User (" +
                    "UserId INT AUTO_INCREMENT PRIMARY KEY," +
                    "Name VARCHAR(255) NOT NULL UNIQUE," +  // 添加UNIQUE关键字
                    "Age INT," +
                    "Gender VARCHAR(10)," +
                    "Phone VARCHAR(20)" +
                    ")";
            statement.executeUpdate(sql);
            System.out.println("User表创建成功！");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "An SQL exception occurred", e);
        }
    }
    /**
     * 用来创建Merchant表
     */
    private void createTableMerchant(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Merchant (" +
                    "MerchantId INT AUTO_INCREMENT PRIMARY KEY," +
                    "Name VARCHAR(255) NOT NULL UNIQUE," +  // 添加UNIQUE关键字
                    "Address VARCHAR(255)" +
                    ")";
            statement.executeUpdate(sql);
            System.out.println("Merchant表创建成功！");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "An SQL exception occurred", e);
        }
    }
    /**
     * 用来创建Product表,ProductId、Name、Category、Origin、
     * Production_Date、MerchantId、Price 和 PlatformId。
     * ProductId 列被指定为主键，并使用 AUTO_INCREMENT 使其自增。
     */
    private void createTableProduct(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Product (" +
                    "ProductId INT AUTO_INCREMENT PRIMARY KEY," +
                    "Name VARCHAR(255) NOT NULL," +
                    "Category VARCHAR(50)," +
                    "Origin VARCHAR(100)," +
                    "Production_Date DATE," +
                    "MerchantId INT," +
                    "Price DOUBLE," +
                    "PlatformId INT," +
                    "FOREIGN KEY (MerchantId) REFERENCES Merchant(MerchantId)," +
                    "FOREIGN KEY (PlatformId) REFERENCES Platform(PlatformId)" +
                    ")";
            statement.executeUpdate(sql);
            System.out.println("Product表创建成功！");
        } catch (SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "An SQL exception occurred", e);
        }
    }

    /**
     * 创建一个名为 "Platform" 的表
     * 包含三个列：PlatformId、PlatformName 和 PlatformURL。
     * PlatformId 列被指定为主键，并使用 AUTO_INCREMENT 使其自增。
     */
    private void createTablePlatform(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Platform (" +
                    "PlatformId INT AUTO_INCREMENT PRIMARY KEY," +
                    "PlatformName VARCHAR(255) NOT NULL," +
                    "PlatformURL VARCHAR(255)" +
                    ")";
            statement.executeUpdate(sql);
            System.out.println("Platform表创建成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个名为 "Favorite" 的表，包含六个列：Favorite、UserId、ProductId、MerchantId、PlatformId
     * 和 Price_Lower_Limit。Favorite 列被指定为主键，并使用 AUTO_INCREMENT 使其自增。
     * 其他列用于存储用户ID、产品ID、商家ID、平台ID和价格下限。
     */
    private void createTableFavorite(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Favorite (" +
                    "FavoriteId INT AUTO_INCREMENT PRIMARY KEY," +
                    "UserId INT," +
                    "ProductId INT," +
                    "MerchantId INT," +
                    "PlatformId INT," +
                    "Price_Lower_Limit DECIMAL(10, 2)," +
                    "FOREIGN KEY (UserId) REFERENCES User(UserId)," +
                    "FOREIGN KEY (ProductId) REFERENCES Product(ProductId)," +
                    "FOREIGN KEY (MerchantId) REFERENCES Merchant(MerchantId)," +
                    "FOREIGN KEY (PlatformId) REFERENCES Platform(PlatformId)" +
                    ")";
            statement.executeUpdate(sql);
            System.out.println("Favorite表创建成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个名为 "Message" 的表，包含六个列：MessageId、UserId、ProductId、MerchantId、PlatformId
     * 和 CurrentPrice。MessageId 列被指定为主键，并使用 AUTO_INCREMENT 使其自增。
     * 其他列用于存储用户ID、产品ID、商家ID、平台ID和当前价格。
     */
    private void createTableMessage(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS Message (" +
                    "MessageId INT AUTO_INCREMENT PRIMARY KEY," +
                    "UserId INT," +
                    "ProductId INT," +
                    "MerchantId INT," +
                    "PlatformId INT," +
                    "CurrentPrice DECIMAL(10, 2)," +
                    "FOREIGN KEY (UserId) REFERENCES User(UserId)," +
                    "FOREIGN KEY (ProductId) REFERENCES Product(ProductId)," +
                    "FOREIGN KEY (MerchantId) REFERENCES Merchant(MerchantId)," +
                    "FOREIGN KEY (PlatformId) REFERENCES Platform(PlatformId)" +
                    ")";
            statement.executeUpdate(sql);
            System.out.println("Message表创建成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建一个名为 "PriceHistory" 的表，包含六个列：PriceHistoryId、ProductId、MerchantId、PlatformId、Price 和 Date。
     * PriceHistoryId 列被指定为主键，并使用 AUTO_INCREMENT 使其自增。
     * 其他列用于存储产品ID、商家ID、平台ID、价格和日期。
     */
    private void createTablePriceHistory(){
        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS PriceHistory (" +
                    "PriceHistoryId INT AUTO_INCREMENT PRIMARY KEY," +
                    "ProductId INT," +
                    "MerchantId INT," +
                    "PlatformId INT," +
                    "Price DECIMAL(10, 2)," +
                    "Date DATE," +
                    "FOREIGN KEY (ProductId) REFERENCES Product(ProductId)," +
                    "FOREIGN KEY (MerchantId) REFERENCES Merchant(MerchantId)," +
                    "FOREIGN KEY (PlatformId) REFERENCES Platform(PlatformId)" +
                    ")";
            statement.executeUpdate(sql);
            System.out.println("PriceHistory表创建成功！");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行该查询并返回一个 ResultSet 对象，其中包含了查询结果的数据。
     * @param query  查询语句
     * @return
     */
    public ResultSet executeQuery(String query) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用于执行插入操作
     *
     * @param query
     * @return 返回值为 true，则表示插入成功；如果返回值为 false，则表示插入失败。
     */
    public boolean executeUpdate(String query) {
        try {
            Statement statement = connection.createStatement();
            int rowsAffected = statement.executeUpdate(query);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean isTableIdExists(String table_name, int id) throws SQLException {
        // 构建查询语句
        String sql = "SELECT COUNT(*) FROM " + table_name + " WHERE " + table_name + "Id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count > 0;
    }

    /**
     * 用来判断这个用户名在数据库里是否存在
     * @param UserName
     * @return 如果存在，返回UserId(从1开始的)，如果不存在，返回0
     * @throws SQLException
     */
    public int isUserNameExists(String UserName) throws SQLException {
        String sql = "SELECT UserId FROM User WHERE Name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, UserName);
        ResultSet resultSet = preparedStatement.executeQuery();

        int UserId = 0; // 默认为0，表示用户名不存在
        if (resultSet.next()) {
            UserId = resultSet.getInt("UserId");
        }
        return UserId;
    }

    /**
     * 用来判断这个商户名在数据库里是否存在
     * @param MerchantName
     * @return 如果存在，返回MerchantId(从1开始的)，如果不存在，返回0
     * @throws SQLException
     */
    public int isMerchantNameExists(String MerchantName) throws SQLException {
        String sql = "SELECT MerchantId FROM Merchant WHERE Name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, MerchantName);
        ResultSet resultSet = preparedStatement.executeQuery();

        int MerchantId = 0; // 默认为0，表示用户名不存在
        if (resultSet.next()) {
            MerchantId = resultSet.getInt("MerchantId");
        }
        return MerchantId;
    }

    /**
     * 关闭与数据库的连接
     */
    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}