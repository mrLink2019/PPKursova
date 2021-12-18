module com.pp.kursova {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires log4j;
    requires com.microsoft.sqlserver.jdbc;
    requires java.sql;
    requires java.naming;

    opens com.pp.kursova to javafx.fxml;
    exports com.pp.kursova;
    exports com.pp.kursova.view;
    opens com.pp.kursova.view to javafx.fxml;
}