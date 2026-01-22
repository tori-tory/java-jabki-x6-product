package ru.jabki.x6.product;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.jabki.x6.product.exception.ProductException;
import ru.jabki.x6.product.model.Product;
import ru.jabki.x6.product.repository.ProductRepository;
import ru.jabki.x6.product.service.ProductService;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_valid() {
        final Product product = getProduct();

        when(productRepository.insert(product)).thenReturn(product);

        Product result = productService.create(product);

        assertThat(result).isEqualTo(product);
        verify(productRepository).insert(product);
    }

    @Test
    void createProduct_notPositivePrice_throwsProductException() {
        final Product product = getProduct();
        product.setPrice(BigDecimal.valueOf(-1));

        final ProductException exception = assertThrows(
                ProductException.class,
                () -> productService.create(product)
        );

        assertEquals(exception.getMessage(), "Цена товара должна быть больше нуля");

        verify(productRepository, never()).insert(any());
    }

    // Пустой список
    @Test
    void whenEmptyList() {
        assertDoesNotThrow(() -> productService.checkProductsExist(List.of()));
        verifyNoInteractions(productRepository);
    }

    // Дубликаты, все существуют
    @Test
    void whenDuplicateIdsAndAllExist() {
        List<Long> ids = List.of(1L, 1L, 2L, 2L, 3L);

        when(productRepository.findExistingIds(argThat(list ->
                list.size() == 3 && list.containsAll(List.of(1L, 2L, 3L))
        ))).thenReturn(List.of(1L, 2L, 3L));

        assertDoesNotThrow(() -> productService.checkProductsExist(ids));
    }

    private Product getProduct() {
        return Product.builder()
                .id(1L)
                .name("water")
                .price(BigDecimal.valueOf(100))
                .build();
    }
}