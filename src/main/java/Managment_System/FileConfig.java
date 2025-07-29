package Managment_System;

import Storage.DataStorage;
import Storage.DatabaseStorage;
import Storage.FileStorage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// any commented parts is for handling the data storage using config file instead of .env 

public class FileConfig {
        public static DataStorage readConfig() {
        /*
        Properties props = new Properties();
        try (InputStream input = FileConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            props.load(input);
        } catch (IOException e) {
            System.out.println("Failed to load configuration: " + e.getMessage());
            return null;
        }
        */

        //String storageType = props.getProperty("storage.type");

        String storageType = System.getenv("STORAGE_TYPE");
        
        DataStorage storage;
        if ("file".equalsIgnoreCase(storageType)) {
            storage = new FileStorage();
            return storage;
        } else if ("database".equalsIgnoreCase(storageType)) {
                String dbUrl = "jdbc:mysql://" + System.getenv("DB_HOST") + ":" + System.getenv("DB_PORT") + "/" + System.getenv("DB_NAME");
                String dbUser = System.getenv("DB_USER");
                String dbPass = System.getenv("DB_PASSWORD");
                
                if (dbUrl == null || dbUser == null || dbPass == null) {
                        throw new IllegalArgumentException("Database environment variables not set properly");
                }

                storage = new DatabaseStorage(dbUrl, dbUser, dbPass);
                /*
                storage = new DatabaseStorage(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
                );
                */
                
                DatabaseStorage db_storage = (DatabaseStorage)storage;
                db_storage.ensureConnection();
                
                return storage;
        } else {
            System.out.println("Invalid storage type in config file.");
        }
            return null;
        }
    
}
