package LibApp.Data;

import javafx.beans.property.SimpleStringProperty;

public class BorrowerSearchResultModel {

    private SimpleStringProperty name;
    private SimpleStringProperty phone;
    private SimpleStringProperty email;

    public BorrowerSearchResultModel(String name, String phone, String email)   {
        this.name = new SimpleStringProperty(name);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
    }

    public String getName() {
        return name.get();
    }

    public String getPhone() {
        return phone.get();
    }

    public String getEmail() {
        return email.get();
    }
}
