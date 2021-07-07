package com.boaz.adv_Backend.util;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CurrentArticle {
    private long prev;
    private long next;
}