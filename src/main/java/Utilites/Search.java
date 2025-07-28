package Utilites;

import java.util.List;

public class Search <T extends SearchService>  {    // T means Book or User
    private List<T> list;
    
    public Search(List<T> list){
            this.list = list;
    }
    
    public T searchById(String id){
            for(T i : list){
                    if ( i.getId().equals(id) ){
                        return i;
                    }
            }
            System.out.println("Search result : no item with id" + id);
            return null;
                
    }
    public T searchByName(String name){
            for(T i : list){
                    if ( i.getName().equals(name) ){
                        return i;
                    }
            }
            System.out.println("Search result : no item with name" + name);
            return null; 
    }
}
