package com.boaz.adv_Backend.vo;

import lombok.AllArgsConstructor;
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

    @Column(name = "likes", nullable = false)
    private long likes;

    @Column(name = "views", nullable = false)
    private long views;

    @Column(name = "writer", nullable = false)
    private Long writer;

    @Column(name = "category", nullable = false)
    private Long category;

    @Column(name = "precedent")
    private Long precedent;

    @Column(name = "media")
    private String media;
}
