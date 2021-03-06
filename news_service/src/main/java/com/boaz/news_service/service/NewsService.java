package com.boaz.news_service.service;

import com.boaz.news_service.util.CurrentArticle;
import com.boaz.news_service.vo.NewsList;
import org.springframework.data.domain.Page;

public interface NewsService {
    Page<NewsList> getList(String category, int page, int size);

    boolean write(Long memberId, String title, String content, long category);

    NewsList getPostById(long newsId);

    boolean addViews(long newsId);

    CurrentArticle getPrevAndNextArticle(long newsId);
}
