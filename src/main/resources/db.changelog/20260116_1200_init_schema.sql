CREATE SCHEMA IF NOT EXISTS x6_product;

CREATE TABLE IF NOT EXISTS x6_product.product (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    price NUMERIC(15,2) NOT NULL,
	created_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at timestamp with time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    UNIQUE (name)
);