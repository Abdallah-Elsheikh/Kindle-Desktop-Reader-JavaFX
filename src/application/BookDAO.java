package application;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection conn;

    public BookDAO() {
        conn = DBConnection.getInstance().getConnection();
        createTable();

        // 1. تنظيف البيانات الوهمية القديمة لإجبار البرنامج على قراءة ملفاتك الجديدة
        clearDatabase();

        // 2. استيراد الملفات من مجلد books
        importBooksFromFolder();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "author TEXT, " +
                "content TEXT, " +
                "rating INTEGER DEFAULT 0)";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void clearDatabase() {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM books");
            stmt.execute("DELETE FROM sqlite_sequence WHERE name='books'");
            System.out.println("The old database has been cleaned up.");
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void importBooksFromFolder() {
        Path booksFolder = Paths.get("books");
        System.out.println("The program searches for txt files in: " + booksFolder.toAbsolutePath());

        if (!Files.exists(booksFolder)) {
            try { Files.createDirectories(booksFolder); } catch (IOException e) { return; }
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(booksFolder, "*.txt")) {
            int count = 0;
            for (Path entry : stream) {
                String title = entry.getFileName().toString().replace(".txt", "");
                String content = Files.readString(entry); // قراءة محتوى الملف الحقيقي
                insertBook(title, "Unknown Author", content);
                System.out.println("Book imported: " + title);
                count++;
            }
            if (count == 0) System.out.println("don't found txt file");
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void insertBook(String title, String author, String content) {
        String sql = "INSERT INTO books (title, author, content) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, content);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(new Book(rs.getInt("id"), rs.getString("title"),
                        rs.getString("author"), rs.getString("content"), rs.getInt("rating")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return books;
    }

    public void updateRating(int bookId, int newRating) {
        String sql = "UPDATE books SET rating = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, newRating);
            pstmt.setInt(2, bookId);
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}