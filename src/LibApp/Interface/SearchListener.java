package LibApp.Interface;

public interface SearchListener {

    //TODO: Give parameters to the method signature
    public void onBooksSearched();

    public void onLoansSearched();

    public void onBorrowersSearched();

    public void onDebugAlert(String alertMessage);

    public void onAuthorize();
}
