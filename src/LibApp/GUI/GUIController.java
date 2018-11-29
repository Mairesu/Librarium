package LibApp.GUI;

import LibApp.Interface.SearchListener;
import LibApp.Logic.SQLApplication;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import org.json.JSONObject;

import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Optional;

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
    private Tab administrateTab;

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
    private ComboBox<String> bookSearchBranchDropdown;

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


    //  ######## Other ########

    @FXML
    private TableView resultTable;


    public void initialize() {
        application = new SQLApplication(debug);
        application.addSearchListeners(this);
        application.connectToDatabase();
        setupMenuBar();
        setupActions();
        setupBookSearch();
        setupLoanSearch();
        setupBorrowerSearch();
        if(debug)   {
            buildDebugMenu();
            System.out.println("Debug: " + application.isDebug());
        }
    }

    private void setupMenuBar()  {

        exitAppButton.setOnAction((ActionEvent e) ->  {
            System.out.println("Exit app clicked");
        });

        adminAuthorizeButton.setOnAction((ActionEvent e) ->  {
            System.out.println("Authorize clicked");
            buildAuthorizationMenu();
        });

        adminBranchesButton.setOnAction((ActionEvent e) ->  {
            System.out.println("Branches clicked");
            addBranchDialog();
        });

        helpAboutButton.setOnAction((ActionEvent e) ->  {
            System.out.println("Help clicked");
        });
    }

    private void setupActions()  {

        //######## Manage tab ########
        loanCreateButton.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Create loan button pressed");
        });

        loanReturnButton.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Return loan button pressed");
        });

        loanExtendButton.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Extend loan button pressed");
        });

        registerBorrowerButton.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Register borrower button pressed");
        });

        newBookButton.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("New book button pressed");
        });

        linkBarcodeBookButton.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Link barcode button pressed");
        });

        //######## Administrate tab ########

        if(!application.getAuthorized())    {
            administrateTab.setDisable(true);
        }

        editBorrowerButton.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Edit borrower button pressed");
        });

        modifyBarcodeButton.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Modify barcode button pressed");
        });
    }

    private void setupBookSearch()  {
        bookSearchBranchDropdown.setOnMouseClicked((MouseEvent e) ->  {
            System.out.println("Branch dropdown clicked");
        });

        bookSearchYear.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                bookSearchYear.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        bookSearchInitButton.setOnMouseClicked((MouseEvent e) ->    {
            System.out.println("Book search initiated");

            String title = bookSearchTitle.getText();
            String author = bookSearchAuthor.getText();
            String publisher = bookSearchPublisher.getText();
            String edition = bookSearchEdition.getText();

            if(title.equals("") && author.equals("") && publisher.equals("")
                    && edition.equals("") && bookSearchYear.getText().equals("")
                    && (bookSearchBranchDropdown.getSelectionModel().isEmpty()
                        || bookSearchBranchDropdown.getSelectionModel().getSelectedItem().toString().equals("No branch")))   {

                resultTable.getItems().clear();
                resultTable.setPlaceholder(new Label("Please fill in at least one search field"));
            }
            else if(!bookSearchYear.getText().equals(""))   {
                if(Integer.parseInt(bookSearchYear.getText()) <= (Year.now().getValue())) {

                    Iterator<JSONObject> it = application.doBookSearch(title, author, publisher, edition,
                                                Integer.parseInt(bookSearchYear.getText()),
                                                bookSearchBranchDropdown.getSelectionModel().getSelectedItem().toString());

                    while (it.hasNext()) {
                        //TODO handle result JSON objects and empty result iterators
                    }
                }
                else {
                    resultTable.getItems().clear();
                    resultTable.setPlaceholder(new Label("Please input a year that is not in the future"));
                }
            }
            else {
                Iterator<JSONObject> it = application.doBookSearch(title, author, publisher, edition,
                        -1, bookSearchBranchDropdown.getSelectionModel().getSelectedItem().toString());
            }
        });

        buildBranchDropdown();
    }

    private void setupLoanSearch()  {
        loanSearchStartDate.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("Start date clicked");
        });

        loanSearchEndDate.setOnMouseClicked((MouseEvent e) -> {
            System.out.println("End date clicked");
        });

        loanSearchActiveLoans.setOnMouseClicked((MouseEvent e) ->   {
            System.out.println("Active loans clicked");
        });

        loanSearchInitButton.setOnMouseClicked((MouseEvent e) ->    {
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

        borrowerSearchInitButton.setOnMouseClicked((MouseEvent e) ->    {
            System.out.println("Borrower search initiated");
            System.out.println(borrowerSearchBarcode.getText());
            System.out.println(borrowerSearchBorrowerName.getText());
            System.out.println(borrowerSearchPhone.getText());
        });
    }

    private void buildDebugMenu()   {
        if(debug) {

            Menu debugMenu = new Menu("Debug");

            MenuItem clearDatabaseButton = new MenuItem("Clear DB");
            MenuItem rebuildDatabaseButton = new MenuItem("Rebuild DB");
            MenuItem fillDatabaseButton = new MenuItem("Fill DB");

            clearDatabaseButton.setOnAction((ActionEvent e) ->  {
                application.debugClearDatabase();
            });

            rebuildDatabaseButton.setOnAction((ActionEvent e) -> {
                application.debugRebuildDatabase();
            });

            fillDatabaseButton.setOnAction((ActionEvent e) ->   {
                application.debugFillDatabase();
            });

            debugMenu.getItems().addAll(clearDatabaseButton, rebuildDatabaseButton, fillDatabaseButton);

            menuBar.getMenus().add(debugMenu);

        }
    }

    private void buildAuthorizationMenu()   {
        if(application.getAuthorized()) {
            application.adminLogOut();
            administrateTab.setDisable(true);
            adminBranchesButton.setDisable(true);
            adminAuthorizeButton.setText("Authorize");
            Alert logoutAlert = new Alert(Alert.AlertType.CONFIRMATION, "Log out successful", ButtonType.OK);
            logoutAlert.setHeaderText("Success");
            logoutAlert.showAndWait();

        }
        else {
            TextInputDialog authDialog = new TextInputDialog();
            authDialog.setTitle("Authorization");
            authDialog.setHeaderText("Admin Authorization");
            authDialog.setContentText("Please enter the admin password");
            Optional<String> result = authDialog.showAndWait();
            result.ifPresent(password -> application.authorizeAsAdmin(password));
        }

    }

    private void addBranchDialog()  {

        //TODO do queries for creating a new library branch

        buildBranchDropdown();
    }

    private void buildBranchDropdown()  {
        bookSearchBranchDropdown.getItems().clear();
        bookSearchBranchDropdown.getItems().add("No branch");
        Iterator<String> branches= application.getAllBranches();

        while(branches.hasNext())   {
            bookSearchBranchDropdown.getItems().add(branches.next());
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

    @Override
    public void onDebugAlert(String alertMessage) {
        Alert debugAlert = new Alert(Alert.AlertType.INFORMATION, alertMessage, ButtonType.OK);
        debugAlert.setHeaderText("Debug message");
        debugAlert.setTitle("Information");
        debugAlert.showAndWait();
    }

    @Override
    public void onAuthorize(boolean success)   {
        Alert authAlert;

        if(success) {
            authAlert = new Alert(Alert.AlertType.CONFIRMATION, "Authorization successful", ButtonType.OK);
            authAlert.setHeaderText("Success");
            authAlert.showAndWait();
            administrateTab.setDisable(false);
            adminBranchesButton.setDisable(false);
            adminAuthorizeButton.setText("Admin log out");
        }
        else {
            authAlert = new Alert(Alert.AlertType.ERROR, "Authorization failed", ButtonType.OK);
            authAlert.setHeaderText("Error");
            authAlert.showAndWait();
        }
    }
}
