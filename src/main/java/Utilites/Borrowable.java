package Utilites;

public interface Borrowable {
    public Boolean borrowBook(String bookId);
    public Boolean returnBook(String bookId);
}
