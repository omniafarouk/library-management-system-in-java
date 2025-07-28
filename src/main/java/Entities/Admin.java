package Entities;

import Storage.DataStorage;
import Utilites.UserRole;
import java.io.Serializable;

public class Admin extends User implements Serializable{

    private transient DataStorage storage;
    
    public Admin( DataStorage bookStorage ,String id , String name){
        super(id,name,UserRole.ADMIN);
        this.storage = bookStorage;
    }
    // construct 
    
    public void addBook(Book book){
        System.out.println("adding book...");
        storage.addBook(book);
        // on adding books sort books using comparator in list
    }
    
    public void editBook(Book book){       
        System.out.println("editing book");
        storage.updateBookById(book);
    }
    // use overloading to edit book based on the parameter?, how?
    
    public void deleteBook(String id){
        System.out.println("Deleting book");
        storage.removeBook(id);
    }
    
    public void registerUser(User user){
        System.out.println("Adding user ...");
        storage.addUser(user);
    }

    public DataStorage getBookStorage() {
        return storage;
    }
    
    public void setBookStorage(DataStorage bookStorage) {
        this.storage = bookStorage;
    }
}
