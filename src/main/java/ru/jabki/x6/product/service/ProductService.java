package ru.jabki.x6.product.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.jabki.x6.product.exception.ProductException;
import ru.jabki.x6.product.model.Product;
import ru.jabki.x6.product.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(rollbackFor = Exception.class)
    public Product create(final Product product) {
        validate(product);
        return productRepository.insert(product);
    }

    @Transactional(rollbackFor = Exception.class)
    //@CachePut(value = "product", key = "#product.id()")
    public Product update(final Product product){
        validate(product);
        final Product existProduct = getById(product.getId());
        existProduct.setName(product.getName());
        existProduct.setPrice(product.getPrice());
        productRepository.update(existProduct);
        return getById(product.getId());
    }

    @Transactional(readOnly = true)
    //@Cacheable(value = "product", key = "#id")
    public Product getById(final Long id) {
        return productRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public boolean checkProductsExist(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return true;
        }

        Set<Long> uniqueIds = Set.copyOf(ids);
        List<Long> existingIds = productRepository.findExistingIds(List.copyOf(uniqueIds));
        Set<Long> existingSet = Set.copyOf(existingIds);

        List<Long> missingIds = uniqueIds.stream()
                .filter(id -> !existingSet.contains(id))
                .toList();

        return missingIds.isEmpty();
    }

    private void validate(final Product product) {
        if (product == null) {
            throw new ProductException("Товар не задан");
        }
        if (!StringUtils.hasText(product.getName())) {
            throw new ProductException("Наименование товара не может быть пустым");
        }
        if (product.getPrice() == null) {
            throw new ProductException("Цена товара не задана");
        }
        if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductException("Цена товара должна быть больше нуля");
        }
    }
}