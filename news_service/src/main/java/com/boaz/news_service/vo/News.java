package com.boaz.news_service.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@DynamicInsert
@DynamicUpdate
@Builder
public class News implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id", nullable = false)
    private Long newsId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "date")
    private String date;

    @Column(name = "likes")
    private long likes;

    @Column(name = "views")
    private long views;

    @Column(name = "writer")
    private Long writer;

    @Column(name = "category", nullable = false)
    private Long category;

    @Column(name = "media")
    private String media;

    @Column(name = "summary")
    private String summary;
}
