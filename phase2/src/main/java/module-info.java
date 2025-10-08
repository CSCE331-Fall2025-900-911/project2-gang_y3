module boba.shop {
    requires javafx.controls;
    requires javafx.fxml;

    opens boba.shop to javafx.fxml;
    exports boba.shop;
}
