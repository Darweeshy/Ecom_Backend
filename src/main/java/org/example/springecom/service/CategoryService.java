package org.example.springecom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.example.springecom.model.AdminCategoryDTO;
import org.example.springecom.model.Category;
import org.example.springecom.model.CategoryFlatDTO;
import org.example.springecom.model.CategoryNodeDTO;
import org.example.springecom.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired private CategoryRepo categoryRepo;
    @Autowired private FileStorageService fileStorageService;
    @Autowired private ObjectMapper objectMapper;

    @Transactional
    public Category createOrUpdateCategory(Long id, String categoryStr, MultipartFile image) throws IOException {
        Category categoryRequest = objectMapper.readValue(categoryStr, Category.class);
        Category category = (id == null) ? new Category() : getCategoryByIdForAdmin(id);

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        if (categoryRequest.getParent() != null && categoryRequest.getParent().getId() != null) {
            Category parent = getCategoryByIdForAdmin(categoryRequest.getParent().getId());
            category.setParent(parent);
        } else {
            category.setParent(null);
        }

        if (image != null && !image.isEmpty()) {
            String subdirectory = "categories";
            String savedFilename = fileStorageService.saveFile(image, subdirectory);

            // Construct the full, servable URL path and store it in the database.
            String imageUrl = "/images/" + subdirectory + "/" + savedFilename;
            category.setImageUrl(imageUrl);
        }

        return categoryRepo.save(category);
    }

    // ... other methods remain the same ...

    @Transactional(readOnly = true)
    public List<CategoryNodeDTO> getPublicCategoryTree() {
        List<Category> rootCategories = categoryRepo.findActiveCategoryTree();
        return rootCategories.stream().map(CategoryNodeDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Category> getTopLevelCategories() {
        return categoryRepo.findTopLevelActiveCategories();
    }

    @Transactional(readOnly = true)
    public List<AdminCategoryDTO> getAdminCategoryTree() {
        List<Category> allCategories = categoryRepo.findAll();
        Map<Long, Category> categoryMap = allCategories.stream()
                .peek(cat -> cat.getChildren().clear())
                .collect(Collectors.toMap(Category::getId, cat -> cat));

        List<Category> rootCategories = new ArrayList<>();
        for (Category cat : allCategories) {
            if (cat.getParent() != null) {
                Category parent = categoryMap.get(cat.getParent().getId());
                if (parent != null) {
                    parent.getChildren().add(cat);
                }
            } else {
                rootCategories.add(cat);
            }
        }
        return rootCategories.stream().map(AdminCategoryDTO::new).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CategoryFlatDTO> getAdminCategoryFlatList() {
        return categoryRepo.findAll().stream()
                .map(CategoryFlatDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Category getCategoryByIdForAdmin(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public Category getPublicCategoryById(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        if (category.isArchived()) {
            throw new EntityNotFoundException("Category not found with id: " + id);
        }
        return category;
    }

    @Transactional
    public void toggleArchiveCategory(Long id) {
        Category category = getCategoryByIdForAdmin(id);
        category.setArchived(!category.isArchived());
        categoryRepo.save(category);
    }
}