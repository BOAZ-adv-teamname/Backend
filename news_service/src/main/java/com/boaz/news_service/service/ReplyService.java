package com.boaz.news_service.service;

import com.boaz.news_service.vo.ReplyList;

import java.util.List;

public interface ReplyService {
    List<ReplyList> getRepliesByNewsId(long newsId);
}
