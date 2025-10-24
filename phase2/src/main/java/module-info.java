// Sonith

module boba.shop {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
    requires transitive javafx.base;

    opens boba.shop to javafx.fxml;
    opens boba.shop.model to javafx.fxml;
    opens boba.shop.database to javafx.fxml;
    exports boba.shop;
    exports boba.shop.model;
    exports boba.shop.database;
}
