package Storage;

import Entities.Admin;
import Entities.Book;
import Entities.RegularUser;
import Entities.User;
import Utilites.Services;
import Utilites.UserRole;
import static java.lang.System.exit;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseStorage implements DataStorage,RegUserView{
    private final String db_URL ;
    private final String db_username ;
    private final String db_password;
    
    public DatabaseStorage(String db_URL, String db_username, String db_password){
            this.db_URL = db_URL;
            this.db_username = db_username;
            this.db_password = db_password;
    }
    public void ensureConnection(){
        try (Connection conn = DriverManager.getConnection(this.db_URL, this.db_username, this.db_password)) {
            System.out.println("Connected to MySQL successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Connection failed: " + e.getMessage());
            exit(1);
        }
    
    }
    public Boolean BookExists(String id){
           String sql = "Select * from Books where id = ? ;";        
           // is it good to do that common practice or rely on db?
           try(Connection conn = DriverManager.getConnection(db_URL, db_username, db_password);
                PreparedStatement stmt = conn.prepareStatement(sql)
                ){
               stmt.setString(1,id);
               
               ResultSet rs = stmt.executeQuery();
               
               if(rs.next()){
                   conn.close();
                   return true;
               }
               conn.close();
           }
           catch(SQLException e){
               System.out.println("\nCheckBook failed : something wrong happened in SQL" + e.getMessage());
           }
        return false;
    }
    public Boolean UserExists(String id){
           String sql = "Select * from Users where id = ? ;";        
           // is it good to do that common practice or rely on db?
           try(Connection conn = DriverManager.getConnection(db_URL, db_username, db_password);
                PreparedStatement stmt = conn.prepareStatement(sql)
                ){
               stmt.setString(1,id);
               
               ResultSet rs = stmt.executeQuery();
               
               if(rs.next()){
                   conn.close();
                   return true;
               }
               conn.close();
           }
           catch(SQLException e){
               System.out.println("\nCheckUser failed : something wrong happened in SQL"+ e.getMessage());
           }
        return false;
    }
    public Map<String,Book> getStoredBorrowedBooks(String userId){
         String sql = """
                      Select 
                          B.id AS bookId,
                          B.title AS bookTitle,
                          B.author AS bookAuthor,
                          B.genre As bookGenre,
                          B.avaliableCopies AS bookCopies
                          from BorrowedBooks as BB join Books as B 
                          on BB.bookId = B.id where userId = ?;""";   
         
           try(Connection conn = DriverManager.getConnection(db_URL, db_username, db_password);
                PreparedStatement stmt = conn.prepareStatement(sql)
                ){
               stmt.setString(1,userId);
               
               ResultSet rs = stmt.executeQuery();
               Map<String,Book> borrowedBooks = new HashMap<>();
               
               while(rs.next()){
                   String bookId = rs.getString("BookId");                
                   String title = rs.getString("bookTitle");
                   String author = rs.getString("bookAuthor");
                   String genre = rs.getString("bookGenre");
                   int availableCopies = rs.getInt("bookCopies"); 
                   
                   Book book = new Book(bookId, title, author, genre, availableCopies); 
                   borrowedBooks.put(bookId, book);
               }
               conn.close();
               return borrowedBooks;
           }
           catch(SQLException e){
               System.out.println("\nCheckUser failed : something wrong happened in SQL"+ e.getMessage());
           }    
           return null;
    }
    
    public Boolean StoreBorrowedBooks(Map<String,Book> borrowedBooks, String userId){
            String sql = """
                INSERT INTO BorrowedBooks (bookId, userId)
                SELECT ?, ?
                WHERE NOT EXISTS (
                    SELECT 1 FROM BorrowedBooks WHERE bookId = ? AND userId = ?
                )
            """;
            try(Connection conn = DriverManager.getConnection(db_URL, db_username, db_password)){
                for(Book book : borrowedBooks.values()) {
                    try(PreparedStatement stmt = conn.prepareStatement(sql))
                    {    
                        stmt.setString(1,book.getId());
                        stmt.setString(2,userId);
                        stmt.setString(3,book.getId());
                        stmt.setString(4,userId);
                        stmt.executeUpdate();

                    }
                    catch(SQLException e){
                        System.out.println("Storing BorrowedBooks failed while inserting" + book.getId() + " : " + e.getMessage());
                        return false;
                    }}
                System.out.println("User Borrowed Books were added successfully");
                return true;
            }
            catch(SQLException e){
               System.out.println("\nStoring BorrowedBooks failed : something wrong happened in SQL"+ e.getMessage());
           }    
            return false;
    
    }
    
    @Override
    public void addBook(Book book) {
            if (!this.BookExists(book.getId())){     // " rowsSelected > 0"
                String sql = "Insert into Books(id,title,author,genre,avaliableCopies) VALUES (?,?,?,?,?);";
                try(Connection conn = DriverManager.getConnection(db_URL, db_username, db_password);
                    PreparedStatement stmt = conn.prepareStatement(sql);){
                    stmt.setString(1,book.getId());
                    stmt.setString(2,book.getName());
                    stmt.setString(3,book.getAuthor());
                    stmt.setString(4,book.getGenre());
                    stmt.setInt(5,book.getAvaliableCopies());

                    int rowsInserted = stmt.executeUpdate();
                    if( rowsInserted > 0 )
                            System.out.println("\nBook inserted successfully");

                    else
                            System.out.println("\nbook insertion failed");

                    conn.close();
                }
                catch(SQLException e){
                    System.out.println("\nAddBook : Connection to server failed"+ e.getMessage());
                }
            }
            else{
                
                System.out.println("\nBook ID already exists");
                System.out.println("Increment Book Copies if book data is in sync");
            }
            
    }

    @Override
    public void removeBook(String bookId) {
            if(this.BookExists(bookId)){
                String sql = "Delete from Books where id = ? ;";
                try(Connection conn = DriverManager.getConnection(db_URL, db_username, db_password);
                        PreparedStatement stmt = conn.prepareStatement(sql);
                    ){
                    stmt.setString(1, bookId);
                    int rowsAffected = stmt.executeUpdate();
                    
                    if(rowsAffected > 0) 
                        System.out.println("\nBook deleted successfully");
                    else
                        System.out.println("\nBook deletion failed");
                 
                    conn.close();
                }catch(SQLException e){
                        System.out.println("\nRemoveBook : SQL server connection failed"+ e.getMessage());
                }
            
            }
            else{
                System.out.println("\nBook doesn't exist");
            }
    }

    @Override
    public void updateBookById(Book book) {
            if(this.BookExists(book.getId())){
                String sql = "UPDATE Books SET title = ? , author = ? , genre = ? , avaliableCopies = ? where id = ? ;";
                try(Connection conn = DriverManager.getConnection(db_URL, db_username, db_password);
                        PreparedStatement stmt = conn.prepareStatement(sql);
                    ){
                    stmt.setString(1, book.getName());
                    stmt.setString(2, book.getAuthor());
                    stmt.setString(3, book.getGenre());
                    stmt.setInt(4, book.getAvaliableCopies());
                    stmt.setString(5, book.getId());
                    
                    int rowsAffected = stmt.executeUpdate();
                    
                    if(rowsAffected > 0) 
                        System.out.println("\nBook updated successfully");
                    else
                        System.out.println("\nBook update failed");
                    
                    conn.close();
                }catch(SQLException e){
                        System.out.println("\nUpdateBook : SQL server connection failed"+ e.getMessage());
                }
            
            }
            else{
                System.out.println("\nBook doesn't exist");
            }
    }

    @Override
    public Map<String,Book> getAllBooks() {
            String sql = "Select * from Books ;";
            Map<String, Book> allBooks = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(db_URL, db_username, db_password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                int avaliableCopies = rs.getInt("avaliableCopies");

                Book book = new Book(id, title, author, genre, avaliableCopies);
                allBooks.put(id, book);
            }
            
            conn.close();
            } catch (SQLException e) {
                System.out.println("\nServer is down... , Please try again");
            }
            if(!allBooks.isEmpty())
                allBooks = Services.sortBookByComparator(allBooks);
            return allBooks;
    }

    @Override
    public void addUser(User user) {
            if(!this.UserExists(user.getId())){ // borrowed books must be different table
                String sql = "Insert into Users(id,name,role) VALUES (?,?,?);";
                try(Connection conn = DriverManager.getConnection(db_URL, db_username, db_password);
                        PreparedStatement stmt = conn.prepareStatement(sql);
                    ){
                    stmt.setString(1,user.getId());
                    stmt.setString(2,user.getName());
                    stmt.setString(3,user.getRole().toString());
                    
                    int rowsAffected = stmt.executeUpdate();
                    
                    if(rowsAffected > 0) 
                        System.out.println("\nUser aded successfully");
                    else
                        System.out.println("\nUser insertion failed");
                  
                    conn.close();  
                }catch(SQLException e){
                        System.out.println("\nAddUser : SQL server connection failed"+ e.getMessage());
                }
            
            }
            else{
                System.out.println("\nUser ID already exists");
            }
    }

    @Override
    public Boolean updateCopies(String bookId, int copies){
        if(this.BookExists(bookId)){
                String sql = "UPDATE Books SET avaliableCopies = ? where id = ? ;";
                try ( Connection conn = DriverManager.getConnection(db_URL, db_username, db_password);
                        PreparedStatement stmt = conn.prepareStatement(sql);
                     ){
                    stmt.setInt(1,copies);
                    stmt.setString(2,bookId);
                    
                    int rowsAffected = stmt.executeUpdate();
                    
                    conn.close();
                    if (rowsAffected > 0){
                            System.out.println("\nBook Copies updated successfully");
                            return true;
                    }
                            
                    else{
                            System.out.println("\nBook Copies update failes");
                            return false;
                    }
                    
                }catch(SQLException e){
                    System.out.println("\nCopies Update: SQL server connection failed "+ e.getMessage());
                    
                }
            
        } 
            return false;
    }

    @Override
    public User isValidUser(String userId, String name , UserRole role) {
           String sql = "Select * from Users where id = ? and name = ? and role = ?;";        
           // is it good to do that common practice or rely on db?
           try(Connection conn = DriverManager.getConnection(db_URL, db_username, db_password);
                PreparedStatement stmt = conn.prepareStatement(sql)
                ){
               stmt.setString(1,userId);
               stmt.setString(2,name);
               stmt.setString(3,role.toString());
               
               ResultSet rs = stmt.executeQuery();
               
               if(rs.next()){
                   conn.close();
                   if (role.equals(UserRole.REGUSER))
                        return new RegularUser(userId,name);
                   else if(role.equals(UserRole.ADMIN))
                        return new Admin(this,userId,name);   
               }
               conn.close();
           }
           catch(SQLException e){
               System.out.println("\n isValidUser failed : something wrong happened in SQL"+ e.getMessage());
           }
        return null;
    }

    @Override
    public Book getBookById(String id) {
        String sql = "SELECT * FROM Books WHERE id = ?;";

        try (Connection conn = DriverManager.getConnection(db_URL, db_username, db_password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                
                String bookId = rs.getString("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                int availableCopies = rs.getInt("avaliableCopies"); 

                
                return new Book(bookId, title, author, genre, availableCopies);
            }
        } catch (SQLException e) {
            System.out.println("\ngetBookById failed: " + e.getMessage());
        }

        return null; // Book not found
    }


    @Override
    public Book getBookByName(String name) {
        String sql = "SELECT * FROM Books WHERE title = ?;";

        try (Connection conn = DriverManager.getConnection(db_URL, db_username, db_password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                
                String bookId = rs.getString("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                int availableCopies = rs.getInt("avaliableCopies"); 

                
                return new Book(bookId, title, author, genre, availableCopies);
            }
        } catch (SQLException e) {
            System.out.println("\ngetBookById failed: " + e.getMessage());
        }

        return null;
    }

    @Override
    public void viewAllBooks() {
        Map<String,Book> books= this.getAllBooks();
        
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
