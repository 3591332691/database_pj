package com.example.database_pj.StrategyTool;

import com.example.database_pj.SQLHelper;
import javafx.scene.chart.XYChart;

import java.sql.ResultSet;
import java.sql.SQLException;

public class injectByYear implements injectStrategy {
    @Override
    public void DoOperation(int productId, XYChart.Series<String, Double> dataSeries) throws SQLException {
        SQLHelper a = new SQLHelper();
        String query = "SELECT Price, Date " +
                "FROM PriceHistory " +
                "WHERE productId = " + productId + " " +
                "AND Date >= DATE_SUB(NOW(), INTERVAL 1 Year)";
        ResultSet resultSet = a.executeQuery(query);
        while (resultSet.next()) {
            dataSeries.getData().add(new XYChart.Data<>(resultSet.getDate("Date").toString(), resultSet.getDouble("Price")));
        }
        a.close();
    }
}
