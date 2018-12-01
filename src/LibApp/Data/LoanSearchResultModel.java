package LibApp.Data;

//TODO fill in with returned and displayed data from query

import javafx.beans.property.SimpleStringProperty;

public class LoanSearchResultModel {

    private SimpleStringProperty title;
    private SimpleStringProperty barcode;
    private SimpleStringProperty borrower;
    private SimpleStringProperty start;
    private SimpleStringProperty end;
    private SimpleStringProperty returned;
    private SimpleStringProperty branch; //includes name and address

    public LoanSearchResultModel(String title, String barcode, String borrower,
                                 String start, String end, String returned, String branch)  {

        this.title = new SimpleStringProperty(title);
        this.barcode = new SimpleStringProperty(barcode);
        this.borrower = new SimpleStringProperty(borrower);
        this.start = new SimpleStringProperty(start);
        this.end = new SimpleStringProperty(end);
        this.returned = new SimpleStringProperty(returned);
        this.branch = new SimpleStringProperty(branch);
    }

    public String getTitle() {
        return title.get();
    }

    public String getBarcode() {
        return barcode.get();
    }

    public String getBorrower() {
        return borrower.get();
    }

    public String getStart() {
        return start.get();
    }

    public String getEnd() {
        return end.get();
    }

    public String getReturned() {
        return returned.get();
    }

    public String getBranch() {
        return branch.get();
    }
}
