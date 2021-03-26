package com.boaz.adv_Backend.repository;

import com.boaz.adv_Backend.vo.NewsList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsListRepository extends JpaRepository<NewsList, Long> {
    public NewsList findByUri(String url);
}
