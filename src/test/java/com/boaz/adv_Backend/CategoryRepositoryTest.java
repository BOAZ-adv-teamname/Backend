package com.boaz.adv_Backend;

import com.boaz.adv_Backend.repository.CategoryRepository;
import com.boaz.adv_Backend.vo.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void create(){
        Category category = new Category();
        category.setCategoryName("정치");
        Category newCategory = categoryRepository.save(category);
        System.out.println(newCategory);
    }
}
