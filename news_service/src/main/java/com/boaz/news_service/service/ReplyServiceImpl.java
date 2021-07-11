package com.boaz.news_service.service;

import com.boaz.news_service.repository.ReplyListRepository;
import com.boaz.news_service.util.Conversion;
import com.boaz.news_service.vo.ReplyList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {
    @Autowired
    private ReplyListRepository replylistRepository;

    @Override
    public List<ReplyList> getRepliesByNewsId(long newsId) {
        List<ReplyList> replies = replylistRepository.findAllByNewsId(newsId);
        replies.stream()
                .peek(Conversion::convertContent)
                .forEach(Conversion::convertDateFormatForArticle);
        return replies;
    }
}
