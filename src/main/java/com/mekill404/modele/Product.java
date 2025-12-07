package com.mekill404.modele;

import java.math.BigDecimal;
import java.time.Instant;

public class Product 
{
    private int id;
    private String name; 
    private BigDecimal price;
    private Instant creationDatetime;
    private Category category;

    public Product(int id, String name, BigDecimal price, Instant creationDatetime, Category category) 
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.creationDatetime = creationDatetime;
        this.category = category;
    }

    public String getCategoryName()
    {
        return (category != null) ? category.getName() : "N/A";
    }
    
    @Override
    public String toString() 
    {
        return 
        "Product{ " +
            "id = " + id +
            ", name = '" + name + '\'' +
            ", price = " + price +
            ", creationDatetime = " + creationDatetime +
            ", categoryName = '" + getCategoryName() + '\'' +
            '}';
    }
}
