package com.example.database_pj.StrategyTool;

import javafx.scene.chart.XYChart;

import java.sql.SQLException;

public interface injectStrategy {
    public void DoOperation(int productId, XYChart.Series<String, Double> dataSeries) throws SQLException;
}
