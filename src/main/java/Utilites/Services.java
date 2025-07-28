package Utilites;

import Entities.Book;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class Services {
    
    public static Map<String, Book> sortBookByComparator(Map<String, Book> books) {
        Comparator<Book> com = (i,j) -> {
            return i.getName().compareTo(j.getName()) ;        // =0 if equal , <0 if less (or capital) , >0 if more (or small)
        };
        
        List<Book> booksList =  new ArrayList<>(books.values());
        Collections.sort(booksList, com);
        
        Map<String, Book> sortedMap = new LinkedHashMap<>();
        for (Book book : booksList) {
            sortedMap.put(book.getId(), book);
        }
        return sortedMap;
    }

    
}
