package Entities;

import Utilites.SearchService;
import Utilites.UserRole;
import java.util.*;
import java.io.Serializable;

public abstract class User implements SearchService,Serializable{
    private static final long serialVersionUID = 1L;
    //each class must keep its own UID consistent between serialization and deserialization
    //id is between classes not projects --> diff classes can have the same id
    
    private String id;
    private String name;
    private Map<String, Book> borrowedBooks = new HashMap<>();
    private UserRole role;
    
    public User(String id , String name,UserRole role){
        this.id = id;
        this.name = name;
        this.role = role;
    }
    
    public UserRole getRole(){
        return this.role;
    }
    public void setRole(UserRole role){
        this.role = role;
    }
    
    
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(Map<String, Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
    @Override
    public String toString() {
        return "User{" +
               "id='" + id + '\'' +
               ", name='" + name + '\''+
               ", borrowedBooks='" + borrowedBooks + '\''+
               '}';
    }
}
