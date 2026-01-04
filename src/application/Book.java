package application;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private int id;
    private String title;
    private String author;
    private String content;
    private int rating;
    private List<String> pages;

    public Book(int id, String title, String author, String content, int rating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.content = content;
        this.rating = rating;
        this.pages = splitContentToPages(content);
    }

    private List<String> splitContentToPages(String text) {
        List<String> p = new ArrayList<>();
        if (text == null || text.isEmpty()) return p;

        int pageSize = 600; // كل 600 حرف يتم اعتبارها صفحة
        for (int i = 0; i < text.length(); i += pageSize) {
            p.add(text.substring(i, Math.min(text.length(), i + pageSize)));
        }
        return p;
    }

    public String getPage(int index) {
        if (pages.isEmpty()) return "this book is empty";
        return (index >= 0 && index < pages.size()) ? pages.get(index) : "";
    }

    public int totalPages() { return pages.size(); }
    public String getTitle() { return title; }
    public int getId() { return id; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}