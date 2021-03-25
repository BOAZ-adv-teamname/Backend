package com.boaz.adv_Backend.controller;

import com.boaz.adv_Backend.repository.NewsListRepository;
import com.boaz.adv_Backend.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NewsController {
    @Autowired
    private NewsListRepository newsListRepository;
    @Autowired
    private NewsRepository newsRepository;
}
