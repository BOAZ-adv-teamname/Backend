package com.boaz.adv_Backend.repository;

import com.boaz.adv_Backend.vo.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
