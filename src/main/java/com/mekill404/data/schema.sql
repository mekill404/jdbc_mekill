CREATE TABLE Product (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL, 
    price NUMERIC(10, 2),
    creation_datetime TIMESTAMP WITHOOUT ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE table Product_category(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    product_id INT NOT NULL,
);

CREATE TABLE Product_category(
    id SERIAL PRIMARY KEY, 
    name VARCHAR(255) NOT NULL,
    product_id INT NOT NULL,
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES Product(id) OON DELETE CASCADE
);