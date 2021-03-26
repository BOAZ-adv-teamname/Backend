package com.boaz.adv_Backend;

import com.boaz.adv_Backend.repository.NewsRepository;
import com.boaz.adv_Backend.vo.News;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrawlingTest {

    @Autowired
    NewsRepository newsRepository;

    @Test
    void T1() {
        try {

            String url ="https://news.naver.com/main/read.nhn?mode=LS2D&mid=shm&sid1=102&sid2=276&oid=022&aid=0003558477";

            Document document = Jsoup.connect(url).get();
            String title = document.select("meta[property^=og:title]").get(0).attr("content");
            //String summary = document.select(".media_end_summary").get(0).text();
            String content = document.select("._article_body_contents").get(0).text();
            String pubDate = document.select(".t11").get(0).text();
            String mediaName = document.select(".press_logo").select("img").attr("title");
            String rootDomain = document.select(".press_logo").select("a").attr("href");

            System.out.println("title : " + title);
            //System.out.println("summary : " + summary);
            System.out.println("content : "+content);
            System.out.println("pubDate : "+pubDate);
            System.out.println("mediaName : "+mediaName);
            System.out.println("rootDomain : "+rootDomain);

            News news = new News();
            news.setTitle(title);
            news.setContent(content);
            news.setMedia(mediaName);
            news.setDate(pubDate);
            //news.setLikes(0L);
            //news.setWriter(0L);
            //news.setCategory(0L);

            //News newsBoard = newsRepository.save(news);
            //System.out.println(newsBoard);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
