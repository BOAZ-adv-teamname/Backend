package com.boaz.adv_Backend.service;

import com.boaz.adv_Backend.vo.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getList();

    boolean addCategory(String category);
}
