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
public class Precedent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "precedent_id", nullable = false)
    private Long precedentId;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "category", nullable = false)
    private Long category;
}
