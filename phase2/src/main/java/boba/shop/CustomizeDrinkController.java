


package boba.shop;


import boba.shop.model.DrinkItem;
import boba.shop.model.MenuItem;
import boba.shop.model.OrderManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;


/**
 * Controller for customizing drink options such as sweetness and ice levels.
 * Handles user selections and adds customized drinks to the order.
 * 
 * @author Joshua Samuel and Sudeep Sirivella
 */
public class CustomizeDrinkController {


    @FXML
    private Label drinkNameLabel;


    @FXML
    private RadioButton sweetness0;
    @FXML
    private RadioButton sweetness25;
    @FXML
    private RadioButton sweetness50;
    @FXML
    private RadioButton sweetness100;


    @FXML
    private RadioButton ice0;
    @FXML
    private RadioButton ice25;
    @FXML
    private RadioButton ice50;
    @FXML
    private RadioButton ice100;


    private ToggleGroup sweetnessGroup;
    private ToggleGroup iceGroup;
    private MenuItem menuItem;
    private OrderManager orderManager;


    /** Initializes toggle groups and default selections. */
    public void initialize() {
        orderManager = OrderManager.getInstance();
        sweetnessGroup = new ToggleGroup();
        sweetness0.setToggleGroup(sweetnessGroup);
        sweetness25.setToggleGroup(sweetnessGroup);
        sweetness50.setToggleGroup(sweetnessGroup);
        sweetness100.setToggleGroup(sweetnessGroup);
        sweetness100.setSelected(true);


        iceGroup = new ToggleGroup();
        ice0.setToggleGroup(iceGroup);
        ice25.setToggleGroup(iceGroup);
        ice50.setToggleGroup(iceGroup);
        ice100.setToggleGroup(iceGroup);
        ice100.setSelected(true);
    }


    /**
     * Sets the selected menu item to customize.
     * @param menuItem The drink menu item
     */
    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
        drinkNameLabel.setText(menuItem.getItemName() + " - $" + String.format("%.2f", menuItem.getPrice()));
    }


    /**
     * Sets the drink name and price manually (used for custom drinks).
     * @param name Drink name
     * @param price Drink price
     */
    public void setDrinkInfo(String name, double price) {
        this.menuItem = new MenuItem(0, name, "", price, true, "");
        drinkNameLabel.setText(name + " - $" + String.format("%.2f", price));
    }


    /** Adds the customized drink to the order and closes the window. */
    @FXML
    private void onAddToCart() {
        if (menuItem == null) return;


        RadioButton selectedSweetness = (RadioButton) sweetnessGroup.getSelectedToggle();
        RadioButton selectedIce = (RadioButton) iceGroup.getSelectedToggle();


        String sweetnessLevel = selectedSweetness.getText();
        String iceLevel = selectedIce.getText();


        DrinkItem newItem = new DrinkItem(menuItem, iceLevel, sweetnessLevel, 1);
        orderManager.addItem(newItem);


        Stage stage = (Stage) drinkNameLabel.getScene().getWindow();
        stage.close();
    }


    /** Closes the customization window without saving changes. */
    @FXML
    private void onCancel() {
        Stage stage = (Stage) drinkNameLabel.getScene().getWindow();
        stage.close();
    }
}



