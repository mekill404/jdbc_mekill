package com.mekill404;
import java.sql.SQLException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.mekill404.DBconnection.DBConnection;
import com.mekill404.modele.Category;
import com.mekill404.modele.Product;
import com.mekill404.utils.DataRetriever;

public class Main {

    public static void main(String[] args) throws SQLException {
        DBConnection dbConnection = new DBConnection();
        DataRetriever dataRetriever = new DataRetriever(dbConnection);

        System.out.println("=============================================");
        System.out.println("             DÉBUT DES TESTS JDBC            ");
        System.out.println("=============================================");

        testGetAllCategories(dataRetriever);
        
        testGetProductList(dataRetriever);

        testGetProductsByCriteria(dataRetriever);

        testGetProductsByCriteriaWithPagination(dataRetriever);
    }

    private static void testGetAllCategories(DataRetriever dr) throws SQLException {
        System.out.println("\n--- TEST a) getAllCategories ---");
        List<Category> categories = dr.getAllCategories();
        System.out.println("Nombre total de catégories: " + categories.size());
        for (Category c : categories) {
            System.out.println(c);
        }
    }

    private static void testGetProductList(DataRetriever dr) {
        System.out.println("\n--- TEST b) getProductList (Pagination) ---");
        System.out.println("\n[Test 1/4] Page=1, Size=3 (produits 1 à 3):");
        List<Product> products1 = dr.getProductList(1, 3);
        products1.forEach(System.out::println);
        
        System.out.println("\n[Test 2/4] Page=2, Size=2 (produits 3 et 4):");
        List<Product> products2 = dr.getProductList(2, 2);
        products2.forEach(System.out::println);
    }
    
    private static void testGetProductsByCriteria(DataRetriever dr) {
        System.out.println("\n--- TEST c) getProductsByCriteria (Filtres seuls) ---");

        System.out.println("\n[Test 1/8] Product='Dell', Category=null, Dates=null:");
        dr.getProductsByCriteria("Dell", null, null, null).forEach(System.out::println);

        System.out.println("\n[Test 4/8] Dates entre 2024-02-01 et 2024-03-01:");
        try {
            Instant min = Instant.parse("2024-02-01T00:00:00Z");
            Instant max = Instant.parse("2024-03-01T00:00:00Z");
            dr.getProductsByCriteria(null, null, min, max).forEach(System.out::println);
        } catch (DateTimeParseException e) {
            System.err.println("Erreur de parsing de date: " + e.getMessage());
        }
        
        System.out.println("\n[Test 8/8] Tous les champs à null (Devrait tout retourner):");
        dr.getProductsByCriteria(null, null, null, null).forEach(System.out::println);
    }

    private static void testGetProductsByCriteriaWithPagination(DataRetriever dr) {
        System.out.println("\n--- TEST d) getProductsByCriteria (Filtres et Pagination) ---");

        System.out.println("\n[Test 3/3] Category='informatique', Page=1, Size=10:");
        dr.getProductsByCriteria(null, "informatique", null, null, 1, 10).forEach(System.out::println);
    }
}