package Entities;

import Storage.RegUserView;
import Utilites.Borrowable;
import Utilites.Services;
import Utilites.UserRole;
import java.io.Serializable;
import java.util.Map;

public class RegularUser extends User implements Borrowable,Serializable {
     
    private transient RegUserView library;
    private transient Map<String,Book> books;


    public RegularUser(String id , String name){
        super(id,name,UserRole.REGUSER);
    }

    public RegUserView getLibrary() {
        return library;
    }
    public void setLibrary(RegUserView library) {
        this.library = library;
    }
    
    public Map<String,Book> loadBooks(){
        books = getLibrary().getAllBooks();
        return books;
    }
    
    @Override
    public Boolean borrowBook(String bookId) {
        if (!books.isEmpty()){
            if(books.containsKey(bookId)){
                Book toBeBorrowedBook = books.get(bookId);
                if(toBeBorrowedBook.getAvaliableCopies() > 0){
                    // if user already borrowed book , can't borrow twice but if returned -> okai
                    Map<String,Book> borrowedBooks = this.getBorrowedBooks();
                    // check if not already borrowed first 
                    if (!borrowedBooks.containsKey(bookId)){ 
                        toBeBorrowedBook.setAvaliableCopies(toBeBorrowedBook.getAvaliableCopies()-1 );
                        //  add to borrowedbooks
                        borrowedBooks.put(bookId, toBeBorrowedBook);
                        
                        Services.sortBookByComparator(books);
                        
                        // update database
                        library.updateCopies(bookId, toBeBorrowedBook.getAvaliableCopies());
                        return true;
                    }
                }else
                    System.out.println("Book is SOLD OUT !!");
            }
        }
       return false;           
    }

    @Override
    public Boolean returnBook(String bookId) {
        if (!books.isEmpty()){
            if(books.containsKey(bookId)){
                Map<String,Book> borrowedBooks = this.getBorrowedBooks();
                
                if (borrowedBooks.containsKey(bookId)){
                    Book borrowedBook = borrowedBooks.remove(bookId);  // remove
                    // then dec the reference copies
                    borrowedBook.setAvaliableCopies( borrowedBook.getAvaliableCopies()+1 );
                    // update database
                    getLibrary().updateCopies(bookId, borrowedBook.getAvaliableCopies());
                    return true;
                    
                }    
                
            }
        }
        return false;      
    }
}
