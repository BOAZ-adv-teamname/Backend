package com.boaz.adv_Backend;

import com.boaz.adv_Backend.repository.NewsListRepository;
import com.boaz.adv_Backend.repository.NewsRepository;
import com.boaz.adv_Backend.vo.News;
import com.boaz.adv_Backend.vo.NewsList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
public class NewsRepositoryTest {

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsListRepository newsListRepository;

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
    @Test
    public void readPage() {
        String category = "전체";
        int page = 0;
        int size = 3;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<NewsList> news = newsListRepository.findAllByCategory(category, pageRequest).getContent();
        news.forEach(board -> System.out.println(board.toString()));
    }
}