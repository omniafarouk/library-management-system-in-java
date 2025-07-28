package Managment_System;

import Storage.DataStorage;
import Storage.DatabaseStorage;
import Storage.FileStorage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FileConfig {
        public static DataStorage readConfig() {
        Properties props = new Properties();

        try (FileInputStream input = new FileInputStream("config.properties")) {
            props.load(input);
        } catch (IOException e) {
            System.out.println("Failed to load configuration: " + e.getMessage());
            return null;
        }

        String storageType = props.getProperty("storage.type");

        DataStorage storage;

        if ("file".equalsIgnoreCase(storageType)) {
            storage = new FileStorage();
            return storage;
        } else if ("database".equalsIgnoreCase(storageType)) {
                storage = new DatabaseStorage(
                props.getProperty("db.url"),
                props.getProperty("db.user"),
                props.getProperty("db.password")
            );
                DatabaseStorage db_storage = (DatabaseStorage)storage;
                db_storage.ensureConnection();
                return storage;
        } else {
            System.out.println("Invalid storage type in config file.");
        }
            return null;
        }
    
}
