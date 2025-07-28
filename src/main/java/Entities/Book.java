package Entities;

import Utilites.SearchService;
import java.io.Serializable;


public class Book implements SearchService,Serializable{
    private static final long serialVersionUID = 1L;
    
    private String id;
    private String title;   // or name it name like user?
    private String author;
    private String genre;
    private int avaliableCopies;
    
    
    public Book(String id,String title,String author,String genre , int avaliableCopies){
        this.author = author;
        this.genre = genre;
        this.id = id;
        this.title = title;
        if (avaliableCopies >= 0)
            this.avaliableCopies = avaliableCopies;
        else
            System.out.println("not valid value for copies");
        
        
    }
    
    public Book(){
    }
    
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String getName() {
        return title;
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

    public int getAvaliableCopies() {
        return avaliableCopies;
    }

    public void setAvaliableCopies(int avaliableCopies) {
        if (avaliableCopies >= 0)
            this.avaliableCopies = avaliableCopies;
        else
            System.out.println("not valid value for copies");
        
    }
    @Override
    public String toString() {
        return "Book{" +
               "id='" + id + '\'' +
               ", title='" + title + '\'' +
               ", author='" + author + '\'' +
               ", genre='" + genre + '\'' +
               ", copies=" + avaliableCopies +
               '}';
    }
    public String toStringUserView(){
               return "Book{" +
               "id='" + id + '\'' +
               ", title='" + title + '\'' +
               ", author='" + author + '\'' +
               ", genre='" + genre + '\'' +
               '}';
    }


}
