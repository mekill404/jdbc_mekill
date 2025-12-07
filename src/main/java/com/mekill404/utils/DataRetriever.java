package com.mekill404.utils;

import java.time.Instant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.mekill404.modele.Category;
import com.mekill404.modele.Product;
import com.mekill404.DBconnection.DBConnection;

public class DataRetriever 
{

    private final DBConnection dbConnection;

    public DataRetriever(DBConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }

    public List<Category> getAllCategories() throws SQLException
    {
        List<Category> categories = new ArrayList<>();
        final String SQL = "SELECT id, name FROM Product_category";

        try
        (
            Connection connection = dbConnection.getDBConnection(); 
            Statement statement = connection.createStatement();
            ResultSet resultat = statement.executeQuery(SQL)
        )
        {
            while (resultat.next()) {
                int id = resultat.getInt("id");
                String name = resultat.getString("name");
                categories.add(new Category(id, name));
            }
        }
        catch (SQLException e)
        {
            System.err.println("Erreur lors de la récupération des catégories: " + e.getMessage());
        }
        return categories;
    }

    public List<Product> getProductList(int page, int size) {
        List<Product> products = new ArrayList<>();
        
        int offset = (page - 1) * size;

        final String SQL = 
        "SELECT " +
            "p.id as product_id, p.name as product_name, p.price, p.creation_datetime, " +
            "pc.id as category_id, pc.name as category_name " +
            "FROM Product p " +
            "JOIN Product_category pc ON p.id = pc.product_id " +
            "ORDER BY p.id " + 
            "LIMIT ? OFFSET ?";

        try 
        (
            Connection connection = dbConnection.getDBConnection();
            PreparedStatement pstatement = connection.prepareStatement(SQL)
        ) 
        {
            pstatement.setInt(1, size);
            pstatement.setInt(2, offset);
            try (ResultSet resultat = pstatement.executeQuery()) 
            {
                while (resultat.next()) 
                {
                    Category category = new Category
                    (
                        resultat.getInt("category_id"),
                        resultat.getString("category_name")
                    );
                    Timestamp timestamp = resultat.getTimestamp("creation_datetime");
                    Instant creationInstant = (timestamp != null) ? timestamp.toInstant() : null;
                    
                    Product product = new Product
                    (
                        resultat.getInt("product_id"),
                        resultat.getString("product_name"),
                        resultat.getBigDecimal("price"),
                        creationInstant,
                        category
                    );
                    products.add(product);
                }
            }

        } 
        catch (SQLException e) 
        {
            System.err.println("Erreur lors de la récupération de la liste des produits: " + e.getMessage());
        }
        return products;
    }
    public List<Product> getProductsByCriteria
    (
        String productName,
        String categoryName,
        Instant creationMin,
        Instant creationMax
    ) 
    {
        return executeCriteriaQuery(productName, categoryName, creationMin, creationMax, 0, 0, false);
    }
    
    private List<Product> executeCriteriaQuery
    (
        String productName,
        String categoryName,
        Instant creationMin,
        Instant creationMax,
        int page,
        int size,
        boolean usePagination
    ) 
    {
        List<Product> products = new ArrayList<>();
        List<String> conditions = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();
        
        StringBuilder sql = new StringBuilder
        (
            "SELECT " +
            "p.id as product_id, p.name as product_name, p.price, p.creation_datetime, " +
            "pc.id as category_id, pc.name as category_name " +
            "FROM Product p " +
            "JOIN Product_category pc ON p.id = pc.product_id"
        );
        if (productName != null && !productName.trim().isEmpty())
        {
            conditions.add("p.name ILIKE ?");
            parameters.add("%" + productName.trim() + "%");
        }
        
        if (categoryName != null && !categoryName.trim().isEmpty())
        {
            conditions.add("pc.name ILIKE ?");
            parameters.add("%" + categoryName.trim() + "%");
        }
        
        if (creationMin != null)
        {
            conditions.add("p.creation_datetime >= ?");
            parameters.add(Timestamp.from(creationMin));
        }
        
        if (creationMax != null)
        {
            conditions.add("p.creation_datetime <= ?");
            parameters.add(Timestamp.from(creationMax));
        }

        if (!conditions.isEmpty())
        {
            sql.append(" WHERE ").append(String.join(" AND ", conditions));
        }

        if (usePagination)
        {
            sql.append(" ORDER BY p.id ");
            sql.append(" LIMIT ? OFFSET ?");
            
            int offset = (page - 1) * size;
            parameters.add(size);
            parameters.add(offset);
        }
        
        try 
        (
            Connection connection = dbConnection.getDBConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql.toString())
        )
        {
            int index = 1;
            for (Object param : parameters) {
                if (param instanceof String) 
                {
                    pstmt.setString(index++, (String) param);
                } else if (param instanceof Timestamp)
                {
                    pstmt.setTimestamp(index++, (Timestamp) param);
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) 
            {
                while (rs.next()) 
                {
                    Category category = new Category(rs.getInt("category_id"), rs.getString("category_name"));
                    Timestamp ts = rs.getTimestamp("creation_datetime");
                    Instant creationInstant = (ts != null) ? ts.toInstant() : null;
                    
                    products.add(new Product(
                        rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getBigDecimal("price"),
                        creationInstant,
                        category
                    ));
                }
            }

        } 
        catch (SQLException e)
        {
            System.err.println("Erreur lors de l'exécution de la requête par critères: " + e.getMessage());
        }
        return products;
    }

    public List<Product> getProductsByCriteria
    (
        String productName,
        String categoryName,
        Instant creationMin,
        Instant creationMax,
        int page,
        int size
    ) 
    {
        
        return executeCriteriaQuery(productName, categoryName, creationMin, creationMax, page, size, true);
    }
}
