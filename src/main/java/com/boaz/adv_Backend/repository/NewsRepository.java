package com.boaz.adv_Backend.repository;

import com.boaz.adv_Backend.vo.News;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NewsRepository extends JpaRepository<News, Long> {
    List<News> findAllByReadCheck(int read);
    boolean existsByUri(String url);
}
