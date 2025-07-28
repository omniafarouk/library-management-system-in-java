package Managment_System;

import Storage.*;
import static java.lang.System.exit;

public class LibrarySystem {

        public static void main(String[] args) {
            System.out.println("Starting App....!");

            // construct a console based menu
            DataStorage storage = FileConfig.readConfig();
            if (storage == null){
                System.out.println("error in reading storage");
                exit(1);
            }
            ConsoleMenu menu = new ConsoleMenu(storage);
            menu.run();
            

        }
        
}

