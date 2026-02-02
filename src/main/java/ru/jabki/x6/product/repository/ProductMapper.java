package ru.jabki.x6.product.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.jabki.x6.product.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

@Component
public class ProductMapper implements RowMapper<Product> {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Product.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .price(rs.getBigDecimal("price"))
                .createdAt(rs.getObject("created_at", OffsetDateTime.class))
                .updatedAt(rs.getObject("updated_at", OffsetDateTime.class))
                .build();
    }
}