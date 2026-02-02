package ru.jabki.x6.product.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jabki.x6.product.exception.BadRequestException;
import ru.jabki.x6.product.model.Product;

import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class ProductRepository {
    private static final String INSERT = """
        INSERT INTO x6_product.product(name, price)
        VALUES (:name, :price)
        RETURNING *
        """;

    private static final String UPDATE = """
            UPDATE x6_product.product
            SET name = :name, price = :price, updated_at = now()
            WHERE id = :id
            RETURNING *
            """;

    private static final String GET_BY_ID = """
            SELECT *
            FROM x6_product.product
            WHERE id = :id
            """;

    private static final String FIND_EXISTING_IDS = """
            SELECT *
            FROM x6_product.product
            WHERE id IN (:ids)
            """;

    private final ProductMapper productMapper;
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Product insert(final Product product){
        return jdbcTemplate.queryForObject(INSERT, productToSql(product), productMapper);
    }

    public Product update( final Product product){
        return  jdbcTemplate.queryForObject(UPDATE, productToSql(product), productMapper);
    }

    public Product getById(final Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new MapSqlParameterSource("id", id), productMapper);
        } catch (Exception e) {
            throw new BadRequestException(String.format("Товар с id %d не найден", id));
        }
    }

    public List<Long> findExistingIds(List<Long> ids) {
        return jdbcTemplate.query(
                FIND_EXISTING_IDS,
                Map.of("ids", ids),
                (rs, rowNum) -> rs.getLong("id")
        );
    }

    private MapSqlParameterSource productToSql(Product product) {
        final MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", product.getId());
        params.addValue("name", product.getName());
        params.addValue("price", product.getPrice());
        return params;
    }
}