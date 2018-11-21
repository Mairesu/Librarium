package LibApp.GUI;

import javafx.event.Event;
import javafx.scene.control.*;
import javafx.fxml.FXML;

public class GUIController {

    //Manage tab
    @FXML
    private Button loanCreateButton;

    @FXML
    private Button loanReturnButton;

    @FXML
    private Button loanExtendButton;

    @FXML
    private Button registerBorrowerButton;

    @FXML
    private Button newBookButton;

    @FXML
    private Button linkBarcodeBookButton;

    //Administrate tab
    @FXML
    private Button editBorrowerButton;

    @FXML
    private Button modifyBarcodeButton;

    public void initialize() {
        setupGUIElements();
    }

    public void setupGUIElements()  {

        //######## Manage tab ########
        loanCreateButton.setOnMouseClicked(Event -> {
            System.out.println("Create loan button pressed");
        });

        loanReturnButton.setOnMouseClicked(Event -> {
            System.out.println("Return loan button pressed");
        });

        loanExtendButton.setOnMouseClicked(Event -> {
            System.out.println("Extend loan button pressed");
        });

        registerBorrowerButton.setOnMouseClicked(Event -> {
            System.out.println("Register borrower button pressed");
        });

        newBookButton.setOnMouseClicked(Event -> {
            System.out.println("New book button pressed");
        });

        linkBarcodeBookButton.setOnMouseClicked(Event -> {
            System.out.println("Link barcode button pressed");
        });

        //######## Administrate tab ########
        editBorrowerButton.setOnMouseClicked(Event -> {
            System.out.println("Edit borrower button pressed");
        });

        modifyBarcodeButton.setOnMouseClicked(Event ->  {
            System.out.println("Modify barcode button pressed");
        });
    }

}
