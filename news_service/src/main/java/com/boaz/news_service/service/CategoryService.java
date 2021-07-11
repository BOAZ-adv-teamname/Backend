package com.boaz.news_service.service;

import com.boaz.news_service.vo.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getList();

    boolean addCategory(String category);
}
