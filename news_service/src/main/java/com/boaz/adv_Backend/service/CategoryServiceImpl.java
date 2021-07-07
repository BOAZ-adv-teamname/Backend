package com.boaz.adv_Backend.service;

import com.boaz.adv_Backend.repository.CategoryRepository;
import com.boaz.adv_Backend.vo.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getList() {
        return categoryRepository.findAll();
    }

    @Override
    public boolean addCategory(String categoryName) {
        Category category = Category.builder()
                .categoryName(categoryName)
                .build();
        System.out.println(category.getCategoryId()+"");
        category = categoryRepository.save(category);
        System.out.println(category.getCategoryId()+"");
        return true;
    }
}
