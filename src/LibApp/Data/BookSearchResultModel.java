package LibApp.Data;

import javafx.beans.property.SimpleStringProperty;

public class BookSearchResultModel {

    private SimpleStringProperty title;
    private SimpleStringProperty author;
    private SimpleStringProperty publisher;
    private SimpleStringProperty edition;
    private SimpleStringProperty year;
    private SimpleStringProperty barcode;
    private SimpleStringProperty branch;
    private SimpleStringProperty address;

    public BookSearchResultModel(String title, String author, String publisher, String edition,
                                 String year, String barcode, String branch, String address)    {
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.publisher = new SimpleStringProperty(publisher);
        this.edition = new SimpleStringProperty(edition);
        this.year = new SimpleStringProperty(year);
        this.barcode = new SimpleStringProperty(barcode);
        this.branch = new SimpleStringProperty(branch);
        this.address = new SimpleStringProperty(address);
    }

    public String getTitle() {
        return title.get();
    }

    public String getAuthor() {
        return author.get();
    }

    public String getPublisher() {
        return publisher.get();
    }

    public String getEdition() {
        return edition.get();
    }

    public String getYear() {
        return year.get();
    }

    public String getBarcode() {
        return barcode.get();
    }

    public String getBranch() {
        return branch.get();
    }

    public String getAddress() {
        return address.get();
    }
}
