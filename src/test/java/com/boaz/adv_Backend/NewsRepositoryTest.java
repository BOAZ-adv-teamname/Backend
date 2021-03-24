package com.boaz.adv_Backend;

import com.boaz.adv_Backend.repository.NewsRepository;
import com.boaz.adv_Backend.vo.News;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class NewsRepositoryTest {

    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void create(){
        News news = new News();

        news.setCategory(1L);
        news.setTitle("제목");
        news.setContent("내용무");
        news.setWriter(1L);

        News newsBoard = newsRepository.save(news);
        System.out.println(newsBoard);

    }

    @Test
    public void read(){
        Optional<News> news = newsRepository.findById(1L);
        news.ifPresent(selectNews -> {
            System.out.println("board:"+selectNews);
        });
    }

    @Test
    public void readAll(){
        List<News> news = newsRepository.findAll();
        for(News board : news) {
            System.out.println(board);
        }
    }
}