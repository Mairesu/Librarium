package LibApp.GUI;

import LibApp.Interface.SearchListener;
import LibApp.Logic.SQLApplication;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.time.format.DateTimeFormatter;

public class GUIController implements SearchListener {

    private boolean debug = true;

    private SQLApplication application;

    //========== GUI ELEMENTS ==========
    //  ######## Menu bar ########
    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem exitAppButton;

    @FXML
    private MenuItem adminAuthorizeButton;

    @FXML
    private MenuItem adminBranchesButton;

    @FXML
    private MenuItem helpAboutButton;


    //  ######## Manage tab ########
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


    //  ######## Administrate tab ########
    @FXML
    private Button editBorrowerButton;

    @FXML
    private Button modifyBarcodeButton;


    //  ######## Book search tab ########
    @FXML
    private TextField bookSearchTitle;

    @FXML
    private TextField bookSearchAuthor;

    @FXML
    private TextField bookSearchPublisher;

    @FXML
    private TextField bookSearchEdition;

    @FXML
    private TextField bookSearchYear;

    @FXML
    private ComboBox bookSearchBranchDropdown;

    @FXML
    private Button bookSearchInitButton;


    //  ######## Loan search tab ########
    @FXML
    private TextField loanSearchBarcode;

    @FXML
    private TextField loanSearchBorrowerName;

    @FXML
    private DatePicker loanSearchStartDate;

    @FXML
    private DatePicker loanSearchEndDate;

    @FXML
    private CheckBox loanSearchActiveLoans;

    @FXML
    private Button loanSearchInitButton;


    //  ######## Borrower search tab ########
    @FXML
    private TextField borrowerSearchBorrowerName;

    @FXML
    private TextField borrowerSearchPhone;

    @FXML
    private TextField borrowerSearchBarcode;

    @FXML
    private Button borrowerSearchInitButton;


    public void initialize() {
        application = new SQLApplication(debug);
        setupMenuBar();
        setupActions();
        setupBookSearch();
        setupLoanSearch();
        setupBorrowerSearch();
        if(debug)   {
            buildDebugMenu();
            System.out.println("Debug: " + application.isDebug());
        }
        application.connectToDatabase();
    }

    public void setupMenuBar()  {

        exitAppButton.setOnAction((ActionEvent Event) ->  {
            System.out.println("Exit app clicked");
        });

        adminAuthorizeButton.setOnAction((ActionEvent Event) ->  {
            System.out.println("Authorize clicked");
        });
        //TODO make the form update the branches dropdown after completion
        adminBranchesButton.setOnAction((ActionEvent Event) ->  {
            System.out.println("Branches clicked");
        });

        helpAboutButton.setOnAction((ActionEvent Event) ->  {
            System.out.println("Help clicked");
        });
    }

    public void setupActions()  {

        //######## Manage tab ########
        loanCreateButton.setOnMouseClicked((MouseEvent Event) -> {
            System.out.println("Create loan button pressed");
        });

        loanReturnButton.setOnMouseClicked((MouseEvent Event) -> {
            System.out.println("Return loan button pressed");
        });

        loanExtendButton.setOnMouseClicked((MouseEvent Event) -> {
            System.out.println("Extend loan button pressed");
        });

        registerBorrowerButton.setOnMouseClicked((MouseEvent Event) -> {
            System.out.println("Register borrower button pressed");
        });

        newBookButton.setOnMouseClicked((MouseEvent Event) -> {
            System.out.println("New book button pressed");
        });

        linkBarcodeBookButton.setOnMouseClicked((MouseEvent Event) -> {
            System.out.println("Link barcode button pressed");
        });

        //######## Administrate tab ########
        editBorrowerButton.setOnMouseClicked((MouseEvent Event) -> {
            System.out.println("Edit borrower button pressed");
        });

        modifyBarcodeButton.setOnMouseClicked((MouseEvent Event) -> {
            System.out.println("Modify barcode button pressed");
        });
    }

    private void setupBookSearch()  {
        bookSearchBranchDropdown.setOnMouseClicked((MouseEvent Event) ->  {
            System.out.println("Branch dropdown clicked");
        });

        bookSearchInitButton.setOnMouseClicked((MouseEvent Event) ->    {
            System.out.println("Search initiated");
            System.out.println(bookSearchTitle.getText());
            System.out.println(bookSearchAuthor.getText());
            System.out.println(bookSearchPublisher.getText());
            System.out.println(bookSearchEdition.getText());
            System.out.println(bookSearchYear.getText());
        });
    }

    private void setupLoanSearch()  {
        loanSearchStartDate.setOnMouseClicked((MouseEvent Event) -> {
            System.out.println("Start date clicked");
        });

        loanSearchEndDate.setOnMouseClicked((MouseEvent Event) -> {
            System.out.println("End date clicked");
        });

        loanSearchActiveLoans.setOnMouseClicked((MouseEvent Event) ->   {
            System.out.println("Active loans clicked");
        });

        loanSearchInitButton.setOnMouseClicked((MouseEvent Event) ->    {
            System.out.println("Loan search initiated");
            System.out.println(loanSearchBarcode.getText());
            System.out.println(loanSearchBorrowerName.getText());
            if(null != loanSearchStartDate.getValue()) {
                System.out.println("start: " + loanSearchStartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            if(null != loanSearchEndDate.getValue()) {
                System.out.println("end: " + loanSearchEndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            System.out.println(loanSearchActiveLoans.isSelected());
        });
    }

    private void setupBorrowerSearch()  {

        borrowerSearchInitButton.setOnMouseClicked((MouseEvent Event) ->    {
            System.out.println("Borrower search initiated");
            System.out.println(borrowerSearchBarcode.getText());
            System.out.println(borrowerSearchBorrowerName.getText());
            System.out.println(borrowerSearchPhone.getText());
        });
    }

    private void buildDebugMenu()   {
        if(debug) {

            Menu debugMenu = new Menu("Debug");

            MenuItem dropDatabaseButton = new MenuItem("Drop DB");
            MenuItem rebuildDatabaseButton = new MenuItem("Rebuild DB");
            MenuItem fillDatabaseButton = new MenuItem("Fill DB");

            debugMenu.getItems().addAll(dropDatabaseButton, rebuildDatabaseButton, fillDatabaseButton);

            menuBar.getMenus().add(debugMenu);
        }
    }


    //TODO when called through the interface, have the table update with the corresponding information
    @Override
    public void onBooksSearched() {

    }

    @Override
    public void onLoansSearched() {

    }

    @Override
    public void onBorrowersSearched() {

    }
}
