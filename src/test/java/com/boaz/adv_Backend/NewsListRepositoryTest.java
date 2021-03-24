package com.boaz.adv_Backend;

import com.boaz.adv_Backend.repository.NewsListRepository;
import com.boaz.adv_Backend.vo.NewsList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class NewsListRepositoryTest {

    @Autowired
    private NewsListRepository newsListRepository;

    @Test
    public void readAll(){
        List<NewsList> news = newsListRepository.findAll();
        for(NewsList n : news) {
            System.out.println(n);
        }
    }
}