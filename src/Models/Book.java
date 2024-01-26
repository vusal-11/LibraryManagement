package Models;

public class Book {
    private int bookID;
    private String title;
    private String author;
    private String genre;
    private int yearPublished;


    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public int getYearPublished() {
        return yearPublished;
    }
    public void setYearPublished(int yearPublished) {
        this.yearPublished = yearPublished;
    }

    @Override
    public String toString() {
        return "Book ID: " + bookID +
                ", Title: " + title +
                ", Author: " + author +
                ", Genre: " + genre +
                ", Year Published: " + yearPublished;
    }

}
