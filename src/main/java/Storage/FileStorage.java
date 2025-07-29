package Storage;

import Entities.Admin;
import Entities.Book;
import Entities.RegularUser;
import Entities.User;
import Utilites.Search;
import Utilites.Services;
import Utilites.UserRole;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class FileStorage implements DataStorage , RegUserView{  // to prevent taking child from class that can caue problem of the current constructor

    private final String BOOKFILE = "books.dat";
    private final String USERFILE = "users.dat";
    
    private Map<String , Book> books = new HashMap<>(); 
    private Map<String , User> users = new HashMap<>(); 
    
    public FileStorage(){
        this.loadFiles();
    }
    public Map<String , User> getUsers() {
        return users;
    }
    
    @Override
    public Map<String, Book> getAllBooks() {
        return books;
    }
    
    public void loadFiles(){
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(BOOKFILE))){
            books = (Map<String, Book>) ois.readObject(); 
            books = Services.sortBookByComparator(books);
            System.out.println("\nBook File loaded Successfully");
            ois.close();
        
        }catch(Exception e){
            System.out.println("\nLoading Book File Failed " + e.getMessage());
        }
        
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERFILE))){
            users = (Map<String, User>) ois.readObject(); 
            System.out.println("\nUser File loaded Successfully");
            ois.close();
        
        }catch(Exception e){
            System.out.println("\nLoading User File Failed " + e.getMessage());
        }
        
    
    }
    public void saveToFile(){
       try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BOOKFILE))){
            oos.writeObject(books);
            System.out.println("\nBook File Saved Successfully");
            oos.close();
        
        }catch(Exception e){
            System.out.println("\nSaving Book File Failed  "+ e.getMessage());
            //e.printStackTrace();
        }
        
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERFILE))){
            oos.writeObject(users);
            System.out.println("\nUser File Saved Successfully");
            oos.close();
        
        }catch(Exception e){
            System.out.println("\nSaving User File Failed "+ e.getMessage());
            //e.printStackTrace();
        }
    
    }    
    
    @Override
    public void addBook(Book book) {
        if(!books.containsKey(book.getId())){
            books.put(book.getId(), book);
            books = Services.sortBookByComparator(books);
            System.out.println("Book Added Successfully");
        }
        else
            System.out.println("Id already exits");
    }

    @Override
    public void removeBook(String bookId) {
        if (books.containsKey(bookId))
            books.remove(bookId);
    }

    @Override
    public void updateBookById(Book book) {
        if (books.containsKey(book.getId()))
            books.get(book.getId());
        
    }

    @Override
    public void addUser(User user) {
        if (! users.containsKey(user.getId()) ){
            users.put(user.getId(),user);
            System.out.println("User Added Successfully");
        }
        else
            System.out.println("Id already exits");
    }

    @Override
    public Boolean updateCopies(String bookId, int copies) {
        if (books.containsKey(bookId)){
            Book book = books.get(bookId);
            book.setAvaliableCopies(copies);
            return true;
        }
        return false;
    }

    @Override
    public User isValidUser(String userId, String name , UserRole role) {
            if (users.containsKey(userId) ){
                User user = users.get(userId);
                if (user.getName().equals(name) && role.equals(user.getRole()))
                    return user;
            }
            if(users.isEmpty() && role.equals(UserRole.ADMIN)){
                Admin admin = new Admin((DataStorage)this,userId,name);
                users.put(userId, admin);
                return admin;
            }
            return null;
    }

    @Override
    public Book getBookById(String id) {
           List<Book> booksList = new ArrayList<>(books.values());
           Search<Book> searchBooks = new Search<>(booksList); 
           return searchBooks.searchById(id);             // implemented searching by ID here                  
    }

    @Override
    public Book getBookByName(String name) {
           List<Book> booksList = new ArrayList<>(books.values());
           Search<Book> searchBooks = new Search<>(booksList); 
           return searchBooks.searchByName(name);             // implemented searching by ID here       }
    }

    @Override
    public void viewAllBooks() { 
       if (!books.isEmpty()){
            System.out.println("Books in library:\n");
            books.forEach((id,book)-> {
                if(book.getAvaliableCopies() > 0)
                    System.out.println(book+"\n");
                else 
                    System.out.println(book + "  SOLD OUT !!" + "\n");
            });
       } 
    }
}
