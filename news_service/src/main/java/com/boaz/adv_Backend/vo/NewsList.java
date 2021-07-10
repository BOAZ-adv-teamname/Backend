package com.boaz.adv_Backend.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewsList implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private Long newsId;

    @Column(name = "category")
    private String category;

    @Column(name = "title")
    private String title;

    @Column(name="content")
    private String content;

    @Column(name = "writer_id")
    private String writerId;

    @Column(name = "writer_nick")
    private String writerNickname;

    @Column(name = "date")
    private String date;

    @Column(name = "likes")
    private long likes;

    @Column(name = "views")
    private long views;

    @Column(name = "replies")
    private long replies;

    @Column(name = "summary")
    private String summary;

    @Column(name="media")
    private String media;
}
