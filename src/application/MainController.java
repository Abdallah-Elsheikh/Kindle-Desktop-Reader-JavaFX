package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class MainController {

    @FXML private ComboBox<Book> bookSelector;
    @FXML private TextArea pageArea;
    @FXML private TextField ratingField;
    @FXML private Label pageCounter;
    @FXML private Label quoteLabel;

    private BookDAO bookDAO;
    private Book currentBook;
    private int currentPage = 0;
    private SoundPlayer sp = new SoundPlayer();

    @FXML
    public void initialize() {
        bookDAO = new BookDAO();

        bookSelector.setConverter(new StringConverter<Book>() {
            @Override public String toString(Book b) { return b != null ? b.getTitle() : ""; }
            @Override public Book fromString(String s) { return null; }
        });

        loadBooksToComboBox();

        bookSelector.setOnAction(e -> selectBook());

        pageArea.setWrapText(true);
        pageArea.setEditable(false);
        
        loadBonusQuote();
    }
    
    private void loadBonusQuote() {
        new Thread(() -> {
            try {
                URL url = new URL("https://api.kanye.rest/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                
                Scanner scanner = new Scanner(url.openStream());
                String response = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                String quote = response.replace("{\"quote\":\"", "").replace("\"}", "");
                
                Platform.runLater(() -> quoteLabel.setText("\"" + quote + "\""));
                
            } catch (Exception e) {
                Platform.runLater(() -> quoteLabel.setText("Enjoy reading your favorite books!"));
            }
        }).start();
    }

    private void loadBooksToComboBox() {
        List<Book> books = bookDAO.getAllBooks();
        bookSelector.getItems().setAll(books);
        if (!books.isEmpty()) {
            bookSelector.getSelectionModel().selectFirst();
            selectBook();
        }
    }

    private void selectBook() {
        currentBook = bookSelector.getSelectionModel().getSelectedItem();
        if (currentBook != null) {
            currentPage = 0;
            ratingField.setText(String.valueOf(currentBook.getRating()));
            loadPage();
        }
    }

    private void loadPage() {
        if (currentBook != null) {
            pageArea.setText(currentBook.getPage(currentPage));
            if (pageCounter != null) {
                int displayPage = currentPage + 1;
                int totalPages = currentBook.totalPages();
                pageCounter.setText("Page " + displayPage + " of " + (totalPages == 0 ? 1 : totalPages));
            }
        }
    }

    @FXML
    public void nextPage() {
        if (currentBook != null && currentPage < currentBook.totalPages() - 1) {
            currentPage++;
            loadPage();
        }
    }

    @FXML
    public void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            loadPage();
        }
    }

    @FXML
    public void rateBook() {
        if (currentBook == null) return;
        try {
            int rating = Integer.parseInt(ratingField.getText());
            if (rating < 1 || rating > 5) throw new NumberFormatException();

            bookDAO.updateRating(currentBook.getId(), rating);
            currentBook.setRating(rating);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Rating Saved");
            alert.setHeaderText(null);
            alert.setContentText("The rating has been saved: " + rating + " / 5 Stars");
            alert.show();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid number from 1 to 5");
            alert.show();
        }
    }

    @FXML public void playSound() { sp.play(); }
    @FXML public void stopSound() { sp.stop(); }
}