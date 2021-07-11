package com.boaz.news_service.repository;

import com.boaz.news_service.vo.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
