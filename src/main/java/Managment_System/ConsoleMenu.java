package Managment_System;

import java.util.Scanner;
import Entities.User;
import Entities.Admin;
import Entities.Book;
import Entities.RegularUser;
import Storage.DataStorage;
import Storage.DatabaseStorage;
import Storage.FileStorage;
import Storage.RegUserView;
import Utilites.Search;
import Utilites.UserRole;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ConsoleMenu {
    private final Scanner stdin = new Scanner(System.in);
    private final DataStorage storage;

    public ConsoleMenu(DataStorage storage) {
        this.storage = storage;
    }

    public void run() {
        while (true) {
            System.out.println("\n=== Welcome to the Library System ===");
            System.out.println("\n1) Login as Admin");
            System.out.println("\n2) Login as Regular User");
            System.out.println("\n3) Exit");
            System.out.print("\nChoose an option: ");

            String choice = stdin.nextLine().trim();     
            switch (choice) {
                case "1" -> handleLogin(true);
                case "2" -> handleLogin(false);
                case "3" -> {
                    System.out.println("\nGoodbye!");
                    return;
                }
                default -> System.out.println("\nInvalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private void handleLogin(Boolean isAdmin) {
        System.out.print("Enter ID: ");
        String id = stdin.nextLine().trim();
        System.out.print("Enter Name: ");
        String name = stdin.nextLine().trim();
        if (isAdmin){
            User user = storage.isValidUser(id, name,UserRole.ADMIN) ;
            if( user != null ){
                Admin admin = (Admin)user;
                System.out.println("=== Welcome " + name + " ===");
                this.adminMenu(admin);
            }
            else
                System.out.println("\nwrong login credentials");
        }
        else
        {
            User user = storage.isValidUser(id, name,UserRole.REGUSER) ;
            
            if( user != null ){
                RegularUser regUser = (RegularUser)user;

                if (storage instanceof RegUserView) {
                    regUser.setLibrary((RegUserView) storage);
                } else {
                    System.out.println("\nError: Storage is not a RegUserView.");
                    return;
                }
                System.out.println("=== Welcome " + name + " ===");
                this.userMenu(regUser);
            }
            else
                System.out.println("\nwrong login credentials");
        }

    }

    private void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("\n1) Add Book");
            System.out.println("\n2) Remove Book");
            System.out.println("\n3) Update Book");
            System.out.println("\n4) View All Books");
            System.out.println("\n5) Register new User");
            System.out.println("\n6) Logout");
            System.out.print("\nChoose an option: ");

            String choice = stdin.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    Book book = new Book();
                    System.out.println("\n=== Enter Book information ===");
                    System.out.println("\n1) Enter Book Id");
                    String res = stdin.nextLine().trim();
                    book.setId(res);
                    System.out.println("\n2) Enter Book Title");
                    res = stdin.nextLine().trim();
                    book.setTitle(res);
                    System.out.println("\n3) Enter Book Author");
                    res = stdin.nextLine().trim();
                    book.setAuthor(res);
                    System.out.println("\n4) Enter Book Genre");
                    res = stdin.nextLine().trim();
                    book.setGenre(res);
                    System.out.println("\n5) Enter Book Available Copies");
                    res = stdin.nextLine().trim();
                    book.setAvaliableCopies(Integer.parseInt(res));

                    storage.addBook(book);
                }
                case "2" -> {
                    System.out.println("\n Enter Book Id to be removed");
                    String res = stdin.nextLine().trim();
                    admin.deleteBook(res);
                }
                case "3" -> {
                    Boolean flag = true;
                    Book book = null;
                    while (flag){
                        System.out.println("\n1) Enter Book Id to be updated:");
                        String res = stdin.nextLine().trim();

                        Map<String,Book> booksMap = storage.getAllBooks();

                        List<Book> booksList = new ArrayList<>(booksMap.values());
                        Search<Book> searchBooks = new Search<>(booksList); 
                        book = searchBooks.searchById(res);             // implemented searching by ID here

                        if (book == null) {
                            System.out.println("Book not found.");
                            continue;
                        }

                        System.out.println("\nField to be updated:");
                        System.out.println("\n1) Title");
                        System.out.println("\n2) Author");
                        System.out.println("\n3) Genre");
                        System.out.println("\n4) Avaliable Copies");
                        System.out.print("\nChoose an option: ");
                        res = stdin.nextLine().trim();    
                        switch (res) {
                            case "1" -> {
                                System.out.println("\n Enter new Title:");
                                res = stdin.nextLine().trim();
                                book.setTitle(res);
                            }

                            case "2" -> {
                                System.out.println("\n Enter new Author:");
                                res = stdin.nextLine().trim();
                                book.setAuthor(res);
                            }

                            case "3" -> {
                                System.out.println("\n Enter new Genre:");
                                res = stdin.nextLine().trim();
                                book.setGenre(res);
                            }
                            case "4" -> {
                                System.out.println("\n Enter Current Avalaible Copies: ");
                                res = stdin.nextLine().trim();
                                book.setAvaliableCopies(Integer.parseInt(res));
                            }
                            default  ->{
                                System.out.println("\n No field was updated");
                                System.out.println("Invalid choice. Please enter 1–4.");
                            }
                        }
                        System.out.println("\nDo you want to update another field?\n please enter a number:");
                        System.out.println("\n1) yes");
                        System.out.println("\n2) no");                        
                        res = stdin.nextLine().trim();
                        if (res.equals("2") || res.equals("no"))
                            flag = false;
                    }

                    if (book != null) {
                        admin.editBook(book);
                    }
                }
                case "4"->{
                    storage.viewAllBooks();
                }
                case "5" -> {
                    System.out.println("\n=== Enter User information ===");
                    System.out.println("\n1) Enter User Id");
                    String id = stdin.nextLine().trim();
                    System.out.println("\n2) Enter User Name");
                    String name = stdin.nextLine().trim();
                    System.out.println("\n2) Enter User Role\n 1)Admin \n 2)Regular User");
                    System.out.println("Please Choose a valid number:");
                    String roleno = stdin.nextLine().trim();
                    User user = null;
                    switch(roleno){
                        case "1" -> user = new Admin(storage,id,name);
                        case "2" -> user = new RegularUser(id,name);
                        default -> System.out.println("Invalid Choice , please enter 1 or 2 ");
                    }
                    if (user != null)
                        admin.registerUser(user);
                }
                case "6" -> {
                    if(storage instanceof FileStorage){
                        FileStorage fileSt = (FileStorage) storage;
                        fileSt.saveToFile();
                    }
                    return;  // back to main menu
                }
                default -> System.out.println("Invalid choice. Please enter 1–5.");
            }
        }
    }

    private void userMenu(RegularUser user) {
        Map<String,Book> booksMap = user.loadBooks(); // load available books
        
        if(storage instanceof DatabaseStorage){
            DatabaseStorage db_storage = (DatabaseStorage)storage;
            Map<String,Book> borrowedBooks = db_storage.getStoredBorrowedBooks(user.getId()); // read borrowed books from database
            if(borrowedBooks != null)
                user.setBorrowedBooks(db_storage.getStoredBorrowedBooks(user.getId()));  
        }

        if (booksMap == null) {
            System.out.println("Error: Could not load books.");
            return;
        }

        while (true) {
            System.out.println("\n--- User Menu ---");
            System.out.println("1) View All Books");
            System.out.println("2) Borrow Book");
            System.out.println("3) Return Book");
            System.out.println("4) Show Borrowed Books");
            System.out.println("5) Logout");
            System.out.print("Choose an option: ");

            String choice = stdin.nextLine().trim();
            switch (choice) {
                case "1" -> storage.viewAllBooks();
                case "2" -> {
                    System.out.print("Enter Book Name to borrow: ");
                    String res = stdin.nextLine().trim();

                    List<Book> booksList = new ArrayList<>(booksMap.values());
                    Search<Book> searchBooks = new Search<>(booksList); 
                    Book toBorrowBook = searchBooks.searchByName(res);    

                    if (toBorrowBook != null){
                        boolean ok = user.borrowBook(toBorrowBook.getId());
                        System.out.println(ok ? "Borrowed."
                                : "Cannot borrow.");
                    } else {
                        System.out.println("Book not found.");
                    }
                }
                case "3" -> {
                    System.out.print("Enter Book Name to return: ");
                    String res = stdin.nextLine().trim();

                    List<Book> booksList = new ArrayList<>(booksMap.values());
                    Search<Book> searchBooks = new Search<>(booksList); 
                    Book toDelBook = searchBooks.searchByName(res);    

                    if (toDelBook != null) {
                        boolean ok = user.returnBook(toDelBook.getId());
                        System.out.println(ok ? "Returned."
                                : "Cannot return.");
                    } else {
                        System.out.println("Book not found.");
                    }
                }
                case "4" -> {
                    System.out.println("Your Borrowed Books:"); 
                    user.getBorrowedBooks().forEach((id,book)->System.out.println( book.toStringUserView() ));
                // could be represented by book.toString but it would have to display available copies too and that wrong
                }
                case "5" -> {
                    if(storage instanceof FileStorage){
                        FileStorage fileSt = (FileStorage) storage;
                        fileSt.saveToFile();
                    }
                    if(storage instanceof DatabaseStorage){
                       DatabaseStorage db_storage = (DatabaseStorage) storage;
                       db_storage.StoreBorrowedBooks(user.getBorrowedBooks(), user.getId());
                    }
                    return;  // back to main menu
                }
                default -> System.out.println("Invalid choice. Please enter 1–4.");
            }
        }
    }
}
