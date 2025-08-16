package com.libraryapp.util;

import java.io.*;

public class FileUtil {

    public static <T extends Serializable> void save(String filename, T object) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(object);
            System.out.println("✅ Data saved to " + filename);
        } catch (IOException e) {
            System.out.println("⚠ Failed to save: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T load(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ℹ No previous data found at " + filename + " (starting fresh).");
            return null;
        }
    }
}
