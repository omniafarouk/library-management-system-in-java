package Storage;

import Entities.Book;
import Entities.User;
import Utilites.UserRole;
import java.io.Serializable;

public interface DataStorage extends BookView,Serializable{
    void addBook(Book book);
    void removeBook(String BookId);         // should i use book class whole or id ?
    Book getBookById(String id);
    Book getBookByName(String name);
    void updateBookById(Book book);
    void addUser(User user);
    User isValidUser(String userId, String name, UserRole role);
    //void saveRecords();             // i can do this and not write anything in DATABASE ?
    
    
}
 