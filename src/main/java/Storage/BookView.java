package Storage;

import Entities.Book;
import java.util.Map;


public interface BookView {     // this is done to prevent user from calling interface that can access admin work
    public Map<String,Book> getAllBooks();
    public void viewAllBooks();
    
}
