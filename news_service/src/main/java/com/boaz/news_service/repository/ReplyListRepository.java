package com.boaz.news_service.repository;

import com.boaz.news_service.vo.ReplyList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyListRepository extends JpaRepository<ReplyList, Long> {
    public List<ReplyList> findAllByNewsId(long news_id);
}
