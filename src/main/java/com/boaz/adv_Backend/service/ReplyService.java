package com.boaz.adv_Backend.service;

import com.boaz.adv_Backend.vo.ReplyList;

import java.util.List;

public interface ReplyService {
    List<ReplyList> getRepliesByNewsId(long newsId);
}
