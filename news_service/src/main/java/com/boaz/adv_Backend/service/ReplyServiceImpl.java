package com.boaz.adv_Backend.service;

import com.boaz.adv_Backend.repository.ReplyListRepository;
import com.boaz.adv_Backend.util.Conversion;
import com.boaz.adv_Backend.vo.ReplyList;
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
