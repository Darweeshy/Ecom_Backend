package org.example.springecom.service;

import jakarta.persistence.EntityNotFoundException;
import org.example.springecom.model.Product;
import org.example.springecom.model.ProductSummaryDTO;
import org.example.springecom.model.ProductVariant;
import org.example.springecom.repo.ProductRepo;
import org.example.springecom.repo.ProductVariantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ProductVariantRepo variantRepo;
    @Autowired
    private FileStorageService fileStorageService;

    private static final String PRODUCTS_SUBDIR = "products";

    @Transactional
    public Product addProduct(Product product, List<ProductVariant> variants, List<MultipartFile> images)
            throws IOException {
        product.setStockQuantity(variants.stream().mapToInt(ProductVariant::getStockQuantity).sum());
        Product savedProduct = productRepo.save(product);

        for (int i = 0; i < variants.size(); i++) {
            ProductVariant variant = variants.get(i);
            variant.setProduct(savedProduct);

            if (images != null && i < images.size()) {
                MultipartFile image = images.get(i);
                if (image != null && !image.isEmpty()) {
                    String filename = fileStorageService.saveFile(image, PRODUCTS_SUBDIR);
                    variant.setImageUrl("/images/" + PRODUCTS_SUBDIR + "/" + filename);
                }
            }
            variantRepo.save(variant);
        }
        return productRepo.findById(savedProduct.getId()).orElseThrow();
    }

    @Transactional
    public Product updateProduct(int id, Product productDetails, List<ProductVariant> variantsDetails,
            List<MultipartFile> newImageFiles) throws IOException {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setBrand(productDetails.getBrand());
        product.setCategory(productDetails.getCategory());

        Map<Long, ProductVariant> existingVariantsMap = product.getVariants().stream()
                .collect(Collectors.toMap(ProductVariant::getId, Function.identity()));

        List<ProductVariant> processedVariants = new ArrayList<>();
        int newImageCounter = 0;

        for (ProductVariant variantDetail : variantsDetails) {
            ProductVariant variantToSave;
            boolean isNewVariant = variantDetail.getId() == null;

            if (isNewVariant) {
                variantToSave = new ProductVariant();
                variantToSave.setProduct(product);
            } else {
                variantToSave = existingVariantsMap.remove(variantDetail.getId());
                if (variantToSave == null)
                    continue;
            }

            variantToSave.setColor(variantDetail.getColor());
            variantToSave.setSize(variantDetail.getSize());
            variantToSave.setPrice(variantDetail.getPrice());
            variantToSave.setStockQuantity(variantDetail.getStockQuantity());

            if (newImageFiles != null && newImageCounter < newImageFiles.size()) {
                MultipartFile image = newImageFiles.get(newImageCounter++);
                if (image != null && !image.isEmpty()) {
                    String filename = fileStorageService.saveFile(image, PRODUCTS_SUBDIR);
                    variantToSave.setImageUrl("/images/" + PRODUCTS_SUBDIR + "/" + filename);
                }
            } else if (variantDetail.getImageUrl() != null) {
                // Preserve existing imageUrl if no new file uploaded (e.g., from media library)
                variantToSave.setImageUrl(variantDetail.getImageUrl());
            processedVariants.add(variantToSave);
        }

        product.getVariants().clear();
        product.getVariants().addAll(processedVariants);

        if (!existingVariantsMap.isEmpty()) {
            variantRepo.deleteAll(existingVariantsMap.values());
        }

        product.setStockQuantity(product.getVariants().stream().mapToInt(ProductVariant::getStockQuantity).sum());
        return productRepo.save(product);
    }

    // ... other methods remain the same ...

    public void archiveProduct(int id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        product.setArchived(true);
        product.getVariants().forEach(v -> v.setArchived(true));
        productRepo.save(product);
    }

    @Transactional(readOnly = true)
    public List<ProductSummaryDTO> searchAndFilterProducts(Long categoryId, List<String> brands, List<String> sizes,
            String sort) {
        Specification<Product> spec = Specification.where(isNotArchived());
        if (categoryId != null) {
            spec = spec.and(inCategory(categoryId));
        }
        if (brands != null && !brands.isEmpty()) {
            spec = spec.and(withBrand(brands));
        }
        if (sizes != null && !sizes.isEmpty()) {
            spec = spec.and(withSize(sizes));
        }
        Sort sortOrder = switch (sort) {
            case "price-asc" -> Sort.by("variants.price").ascending();
            case "price-desc" -> Sort.by("variants.price").descending();
            default -> Sort.by("releaseDate").descending();
        };
        return productRepo.findAll(spec, sortOrder).stream().map(ProductSummaryDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProductSummaryDTO> searchAndFilterProducts(Long categoryId, List<String> brands, List<String> sizes,
            String sort, int page) {
        Specification<Product> spec = Specification.where(isNotArchived());
        if (categoryId != null) {
            spec = spec.and(inCategory(categoryId));
        }
        if (brands != null && !brands.isEmpty()) {
            spec = spec.and(withBrand(brands));
        }
        if (sizes != null && !sizes.isEmpty()) {
            spec = spec.and(withSize(sizes));
        }
        Sort sortOrder = switch (sort) {
            case "price,asc" -> Sort.by("variants.price").ascending();
            case "price,desc" -> Sort.by("variants.price").descending();
            default -> Sort.by("releaseDate").descending();
        };
        Pageable pageable = PageRequest.of(page, 12, sortOrder);
        return productRepo.findAll(spec, pageable).map(ProductSummaryDTO::new);
    }

    private Specification<Product> isNotArchived() {
        return (root, query, cb) -> cb.equal(root.get("archived"), false);
    }

    private Specification<Product> inCategory(Long categoryId) {
        return (root, query, cb) -> cb.equal(root.get("category").get("id"), categoryId);
    }

    private Specification<Product> withBrand(List<String> brands) {
        return (root, query, cb) -> root.get("brand").in(brands);
    }

    private Specification<Product> withSize(List<String> sizes) {
        return (root, query, cb) -> {
            query.distinct(true);
            return root.join("variants").get("size").in(sizes);
        };
    }

    @Transactional(readOnly = true)
    public Optional<Product> getPublicProductById(int id) {
        return productRepo.findById(id).filter(product -> !product.isArchived());
    }

    @Transactional(readOnly = true)
    public List<String> getDistinctBrands() {
        return productRepo.findDistinctBrands();
    }

    @Transactional(readOnly = true)
    public List<String> getDistinctSizes() {
        return variantRepo.findDistinctSizes();
    }

    public List<Product> searchProducts(String keyword) {
        return productRepo.searchProducts(keyword);
    }
}
