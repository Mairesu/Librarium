package LibApp.GUI;

import LibApp.Data.BookSearchResultModel;
import LibApp.Data.BorrowerSearchResultModel;
import LibApp.Data.LoanSearchResultModel;
import LibApp.Interface.SearchListener;
import LibApp.Logic.SQLApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
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

        buildBranchDropdown();

        bookSearchYear.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                bookSearchYear.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        bookSearchInitButton.setOnMouseClicked((MouseEvent e) ->    {

            String title = bookSearchTitle.getText();
            String author = bookSearchAuthor.getText();
            String publisher = bookSearchPublisher.getText();
            String edition = bookSearchEdition.getText();

            Iterator<JSONObject> it;

            if(title.equals("") && author.equals("") && publisher.equals("")
                    && edition.equals("") && bookSearchYear.getText().equals("")
                    && (bookSearchBranchDropdown.getSelectionModel().isEmpty()
                    || bookSearchBranchDropdown.getSelectionModel().getSelectedItem().toString().equals("No branch")))   {

                clearTable();
                resultTable.setPlaceholder(new Label("Please fill in at least one search field"));
            }
            else if(!bookSearchYear.getText().equals(""))   {

                if(Integer.parseInt(bookSearchYear.getText()) <= (Year.now().getValue())) {

                    if(bookSearchBranchDropdown.getSelectionModel().isEmpty()
                            || bookSearchBranchDropdown.getSelectionModel().getSelectedItem().toString().equals("No branch")) {

                        clearTable();
                        resultTable.setPlaceholder(new Label("Search yielded no result"));

                        it = application.doBookSearch(title, author, publisher, edition,
                                Integer.parseInt(bookSearchYear.getText()), "");
                        if(it.hasNext())    {
                            buildBookSearchResult(it);
                        }
                    }
                    else {
                        clearTable();
                        resultTable.setPlaceholder(new Label("Search yielded no result"));

                        it = application.doBookSearch(title, author, publisher, edition,
                                Integer.parseInt(bookSearchYear.getText()),
                                bookSearchBranchDropdown.getSelectionModel().getSelectedItem().toString());
                        if(it.hasNext())    {
                            buildBookSearchResult(it);
                        }
                    }

                }
                else {
                    clearTable();
                    resultTable.setPlaceholder(new Label("Please input a year that is not in the future"));
                }
            }
            else if(bookSearchBranchDropdown.getSelectionModel().isEmpty()
                    || bookSearchBranchDropdown.getSelectionModel().getSelectedItem().toString().equals("No branch")) {

                clearTable();
                resultTable.setPlaceholder(new Label("Search yielded no result"));

                it = application.doBookSearch(title, author, publisher, edition,
                        -1, "");
                if(it.hasNext())    {
                    buildBookSearchResult(it);
                }

            }
            else {
                clearTable();
                resultTable.setPlaceholder(new Label("Search yielded no result"));

                it = application.doBookSearch(title, author, publisher, edition,
                        -1, bookSearchBranchDropdown.getSelectionModel().getSelectedItem().toString());
                if(it.hasNext())    {
                    buildBookSearchResult(it);
                }
            }
        });
    }

    private void buildBookSearchResult(Iterator<JSONObject> it)    {

        TableColumn<BookSearchResultModel, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookSearchResultModel, String> authorCol = new TableColumn<>("Author");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<BookSearchResultModel, String> pubCol = new TableColumn<>("Publisher");
        pubCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));

        TableColumn<BookSearchResultModel, String> editionCol = new TableColumn<>("Edition");
        editionCol.setCellValueFactory(new PropertyValueFactory<>("edition"));

        TableColumn<BookSearchResultModel, String> yearCol = new TableColumn<>("Year");
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));

        TableColumn<BookSearchResultModel, String> barcodeCol = new TableColumn<>("Barcode");
        barcodeCol.setCellValueFactory(new PropertyValueFactory<>("barcode"));

        TableColumn<BookSearchResultModel, String> branchCol = new TableColumn<>("Branch");
        branchCol.setCellValueFactory(new PropertyValueFactory<>("branch"));

        TableColumn<BookSearchResultModel, String> adrCol = new TableColumn<>("Address");
        adrCol.setCellValueFactory(new PropertyValueFactory<>("address"));

        resultTable.setEditable(true);
        resultTable.getColumns().addAll(titleCol, authorCol, pubCol, editionCol,
                yearCol, barcodeCol, branchCol, adrCol);
        resultTable.getItems().setAll(observableBookSearchResultList(it));
    }

    private ObservableList<BookSearchResultModel> observableBookSearchResultList(Iterator<JSONObject> it)    {

        ObservableList<BookSearchResultModel> observableResults = FXCollections.observableArrayList();

        while (it.hasNext())    {
            JSONObject jsonObject = it.next();
            if(!jsonObject.has("edition"))   {
                jsonObject.put("edition", "------");
            }

            observableResults.add(new BookSearchResultModel(jsonObject.get("title").toString(),
                    jsonObject.get("author").toString(),
                    jsonObject.get("publisher").toString(),
                    jsonObject.get("edition").toString(),
                    jsonObject.get("year").toString(),
                    jsonObject.get("barcode").toString(),
                    jsonObject.get("branch").toString(),
                    jsonObject.get("address").toString()));
        }

        return observableResults;
    }

    private void setupLoanSearch()  {
        loanSearchBarcode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                loanSearchBarcode.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        loanSearchInitButton.setOnMouseClicked((MouseEvent e) ->    {

            String borrowerName = loanSearchBorrowerName.getText();
            boolean isActive = loanSearchActiveLoans.isSelected();
            String startDateString = "";
            String endDateString = "";

            Iterator<JSONObject> it;

            if (loanSearchBarcode.getText().equals("") && borrowerName.equals("")
                    && null == loanSearchStartDate.getValue() && null == loanSearchEndDate.getValue())  {

                clearTable();
                resultTable.setPlaceholder(new Label("Please fill in at least one search field"));
            }
            else if(!loanSearchBarcode.getText().equals(""))   {

                clearTable();
                resultTable.setPlaceholder(new Label("Search yielded no result"));

                if(null != loanSearchStartDate.getValue()) {
                    startDateString = loanSearchStartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                if(null != loanSearchEndDate.getValue()) {
                    endDateString = loanSearchEndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }

                it = application.doLoanSearch(Integer.parseInt(loanSearchBarcode.getText()), borrowerName,
                        startDateString, endDateString, isActive);
                if (it.hasNext())   {
                    buildLoanSearchResult(it);
                }
            }
            else {

                clearTable();
                resultTable.setPlaceholder(new Label("Search yielded no result"));

                if(null != loanSearchStartDate.getValue()) {
                    startDateString = loanSearchStartDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                if(null != loanSearchEndDate.getValue()) {
                    endDateString = loanSearchEndDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }

                it = application.doLoanSearch(-1, borrowerName,
                        startDateString, endDateString, isActive);
                if (it.hasNext())   {
                    buildLoanSearchResult(it);
                }
            }
        });
    }

    private void buildLoanSearchResult(Iterator<JSONObject> it) {

        TableColumn<BookSearchResultModel, String> titleCol = new TableColumn<>("Book Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<BookSearchResultModel, String> barcodeCol = new TableColumn<>("Barcode");
        barcodeCol.setCellValueFactory(new PropertyValueFactory<>("barcode"));

        TableColumn<BookSearchResultModel, String> borrowerCol = new TableColumn<>("Borrower");
        borrowerCol.setCellValueFactory(new PropertyValueFactory<>("borrower"));

        TableColumn<BookSearchResultModel, String> startCol = new TableColumn<>("Start Date");
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));

        TableColumn<BookSearchResultModel, String> endCol = new TableColumn<>("End Date");
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));

        TableColumn<BookSearchResultModel, String> returnedCol = new TableColumn<>("Returned Date");
        returnedCol.setCellValueFactory(new PropertyValueFactory<>("returned"));

        TableColumn<BookSearchResultModel, String> branchCol = new TableColumn<>("Library Branch");
        branchCol.setCellValueFactory(new PropertyValueFactory<>("branch"));

        resultTable.setEditable(true);
        resultTable.getColumns().addAll(titleCol, barcodeCol, borrowerCol, startCol, endCol, returnedCol, branchCol);
        resultTable.getItems().setAll(observableLoanSearchResultList(it));
    }

    private ObservableList<LoanSearchResultModel> observableLoanSearchResultList(Iterator<JSONObject> it)    {

        ObservableList<LoanSearchResultModel> observableResults = FXCollections.observableArrayList();

        while (it.hasNext())    {
            JSONObject jsonObject = it.next();
            if(!jsonObject.has("returned"))   {
                jsonObject.put("returned", "------");
            }

            observableResults.add(new LoanSearchResultModel(jsonObject.get("title").toString(),
                    jsonObject.get("barcode").toString(),
                    jsonObject.get("borrower").toString(),
                    jsonObject.get("start").toString(),
                    jsonObject.get("end").toString(),
                    jsonObject.get("returned").toString(),
                    jsonObject.get("branch").toString() + ", " +
                    jsonObject.get("address").toString()));
        }

        return observableResults;
    }

    private void setupBorrowerSearch()  {

        borrowerSearchBarcode.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                borrowerSearchBarcode.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        borrowerSearchInitButton.setOnMouseClicked((MouseEvent e) ->    {
            System.out.println("Borrower search initiated");
            System.out.println(borrowerSearchBarcode.getText());
            System.out.println(borrowerSearchBorrowerName.getText());
            System.out.println(borrowerSearchPhone.getText());

            String name = borrowerSearchBorrowerName.getText();
            String phone = borrowerSearchPhone.getText();

            Iterator<JSONObject> it;

            if(borrowerSearchBarcode.getText().equals("") && name.equals("") && phone.equals(""))   {

                clearTable();
                resultTable.setPlaceholder(new Label("Please fill in at least one search field"));
            }
            else if (!borrowerSearchBarcode.getText().equals(""))   {
                clearTable();
                resultTable.setPlaceholder(new Label("Search yielded no result"));

                it = application.doBorrowerSearch(name, phone, Integer.parseInt(borrowerSearchBarcode.getText()));
                if (it.hasNext())   {
                    buildBorrowerSearchResult(it);
                }
            }
            else {
                clearTable();
                resultTable.setPlaceholder(new Label("Search yielded no result"));

                it = application.doBorrowerSearch(name, phone, -1);
                if (it.hasNext())   {
                    buildBorrowerSearchResult(it);
                }
            }
        });
    }

    private void buildBorrowerSearchResult(Iterator<JSONObject> it) {
        TableColumn<BookSearchResultModel, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<BookSearchResultModel, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<BookSearchResultModel, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        resultTable.setEditable(true);
        resultTable.getColumns().addAll(nameCol, phoneCol, emailCol);
        resultTable.getItems().setAll(observableBorrowerSearchResultList(it));
    }

    private ObservableList<BorrowerSearchResultModel> observableBorrowerSearchResultList(Iterator<JSONObject> it)    {

        ObservableList<BorrowerSearchResultModel> observableResults = FXCollections.observableArrayList();

        while (it.hasNext())    {
            JSONObject jsonObject = it.next();
            if(!jsonObject.has("email"))   {
                jsonObject.put("email", "------");
            }

            observableResults.add(new BorrowerSearchResultModel(jsonObject.get("name").toString(),
                    jsonObject.get("phone").toString(),
                    jsonObject.get("email").toString()));
        }

        return observableResults;
    }

    private void clearTable()   {
        resultTable.getColumns().clear();
        resultTable.getItems().clear();
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
