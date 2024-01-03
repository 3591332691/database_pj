package com.example.database_pj.View;

import com.example.database_pj.Product;
import com.example.database_pj.SQLHelper;
import com.example.database_pj.StrategyTool.injectByMonth;
import com.example.database_pj.StrategyTool.injectByWeek;
import com.example.database_pj.StrategyTool.injectByYear;
import com.example.database_pj.StrategyTool.injectStrategy;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserProductView {
    private Product product;
    @FXML
    private Text product_name;
    @FXML
    private Text product_category;
    @FXML
    private Text product_origin;
    @FXML
    private Text product_date;
    @FXML
    private Text product_platform;
    @FXML
    private Text product_merchant;
    @FXML
    private Text product_price;
    @FXML
    private Text product_lowestPrice;
    @FXML
    private LineChart WeekChart;
    @FXML
    private LineChart MonthChart;
    @FXML
    private LineChart YearChart;
    @FXML
    private ComboBox<String> chooseTimeGap;

    public void setProductId(int ProductId) {
        try {
            product = new Product(ProductId);
            initial();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initial() throws SQLException {
        product_name.setText("商品名称： " + product.Name);
        product_category.setText("商品类别： " + product.Category);
        product_origin.setText("产地： " + product.Origin);
        product_date.setText("生产日期： " + product.ProductionDate.toString());
        product_platform.setText("发布平台： " + product.PlatformName);
        product_merchant.setText("发布人： " + product.MerchantName);
        product_price.setText("价格： " + product.Price);
        product_lowestPrice.setText("历史最低价: " + GetLowestPrice());
        initialLineChart();
        chooseTimeGap.setOnAction(this::handleComboBoxAction);

    }

    /**
     * 用于得到此商品的历史最低价
     */
    private double GetLowestPrice() throws SQLException {
        //1.Price_History里面寻找是否有价格变动
        //如果有变动的话，找到最低价
        double lowest_price = product.Price;
        SQLHelper a = new SQLHelper();
        String query = "Select Price From PriceHistory " +
                "Where ProductId = " + product.ProductId;
        ResultSet resultSet = a.executeQuery(query);
        while (resultSet.next()) {
            double tempPrice = resultSet.getDouble("Price");
            if (tempPrice < lowest_price) {
                lowest_price = tempPrice;
            }
        }
        a.close();
        return lowest_price;
    }

    /**
     * 用来画三个折线图
     */
    private void initialLineChart() throws SQLException {
        WeekChart.setStyle("-fx-background-color: lightgray;");
        MonthChart.setStyle("-fx-background-color: lightgray;");
        YearChart.setStyle("-fx-background-color: lightgray;");
        injectByWeek byWeek = new injectByWeek();
        injectByMonth byMonth = new injectByMonth();
        injectByYear byYear = new injectByYear();
        // 创建一个数据系列 注入Week数据
        XYChart.Series<String, Double> weekDataSeries = new XYChart.Series<>();
        injectChartData(byWeek, product.ProductId, weekDataSeries);
        WeekChart.getData().add(weekDataSeries);
        //创建一个数据系列 注入Month数据
        XYChart.Series<String, Double> monthDataSeries = new XYChart.Series<>();
        injectChartData(byMonth, product.ProductId, monthDataSeries);
        MonthChart.getData().add(monthDataSeries);
        //创建一个数据系列 注入Year数据
        XYChart.Series<String, Double> yearDataSeries = new XYChart.Series<>();
        injectChartData(byYear, product.ProductId, yearDataSeries);
        YearChart.getData().add(yearDataSeries);
    }

    private void injectChartData(injectStrategy a, int ProductId,
                                 XYChart.Series<String, Double> DataSeries) throws SQLException {
        a.DoOperation(ProductId, DataSeries);
    }

    private void handleComboBoxAction(ActionEvent event) {
        String selectedOption = chooseTimeGap.getSelectionModel().getSelectedItem();
        // 根据选择的选项决定哪个Chart在最上面
        if ("1.近一周".equals(selectedOption)) {
            WeekChart.toFront();
        } else if ("2.近一月".equals(selectedOption)) {
            MonthChart.toFront();
        } else if ("3.近一年".equals(selectedOption)) {
            YearChart.toFront();
        }
    }
}
