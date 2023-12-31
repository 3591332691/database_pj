module com.example.database_pj {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics; // 声明对javafx.graphics模块的依赖


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.example.database_pj to javafx.fxml;
    exports com.example.database_pj;
    exports com.example.database_pj.View;
    opens com.example.database_pj.View to javafx.fxml;
    exports com.example.database_pj.ObserverTool;
    opens com.example.database_pj.ObserverTool to javafx.fxml;
}