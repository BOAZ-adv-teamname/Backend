package com.boaz.adv_Backend.repository;

import com.boaz.adv_Backend.vo.ReplyList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyListRepository extends JpaRepository<ReplyList, Long> {
    public List<ReplyList> findAllByNewsId(long news_id);
}
