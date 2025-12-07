CREATE USER product_management_user WITH PASSWORD '123456';
CREATE DATABASE IF NOT EXISTS product_management_db;
GRANT ALL PRIVILEGES ON DATABASE product_management_db TO product_manager_user;
\connect product_management_db;

GRANT CREATE ON SCHEMA public TO product_manager_user;