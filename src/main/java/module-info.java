module com.recordstore.core {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.desktop;

    exports com.recordstore.controller;
    opens com.recordstore.controller to java.persistence, javafx.fxml, org.hibernate.orm.core;
    exports com.recordstore.model;
    opens com.recordstore.model to java.persistence, javafx.fxml, org.hibernate.orm.core;
    exports com.recordstore.exceptions;
    opens com.recordstore.exceptions to java.persistence, javafx.fxml, org.hibernate.orm.core;
    exports com.recordstore;
    opens com.recordstore to java.persistence, javafx.fxml, org.hibernate.orm.core;
}