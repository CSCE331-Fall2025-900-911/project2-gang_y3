/**
 * Module definition for the boba.shop application.
 *
 * This module sets up the Boba Shop project, including its JavaFX
 * user interface and its database access.
 *
 * It declares which libraries are required and which packages are
 * available to other parts of the program.
 *
 * @author Sonith
 */
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
