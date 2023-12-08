module com.example.bd8 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;


    opens com.example.bd8 to javafx.fxml;
    exports com.example.bd8;
    exports com.example.bd8.Models;
    opens com.example.bd8.Models to javafx.fxml;
}