package com.boaz.news_service.repository;

import com.boaz.news_service.vo.NewsList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsListRepository extends JpaRepository<NewsList, Long> {
    public NewsList findByUri(String url);
    Page<NewsList> findAllByCategory(String category, Pageable pageRequest);
}
