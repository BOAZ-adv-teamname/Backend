package com.boaz.news_service.repository;

import com.boaz.news_service.vo.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    boolean existsByUri(String url);

    @Query(value = "SELECT * FROM (SELECT * FROM news WHERE news_id < :newsId ORDER BY news_id desc LIMIT 1) A UNION SELECT * FROM (SELECT * FROM news WHERE news_id > :newsId ORDER BY news_id asc LIMIT 1) B", nativeQuery = true)
    List<News> findPrevAndNextNewsIdByNewsId(@Param("newsId") long newsId);
}
