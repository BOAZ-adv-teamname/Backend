package com.boaz.news_service.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamicUpdate
@Builder
public class ReplyList implements Serializable, Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id", nullable = false)
    private long replyId;

    @Column(nullable = false)
    private long parent;

    @Column(nullable = false)
    private String content;

    @Column
    private String date;

    @Column(name = "member_id", nullable = false)
    private long memberId;

    @Column(nullable = false)
    private String nickname;

    @Column(name = "news", nullable = false)
    private long newsId;
}
