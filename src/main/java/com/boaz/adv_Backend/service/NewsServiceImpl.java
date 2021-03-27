package com.boaz.adv_Backend.service;

import com.boaz.adv_Backend.repository.NewsListRepository;
import com.boaz.adv_Backend.repository.NewsRepository;
import com.boaz.adv_Backend.util.Conversion;
import com.boaz.adv_Backend.util.CurrentArticle;
import com.boaz.adv_Backend.vo.Article;
import com.boaz.adv_Backend.vo.News;
import com.boaz.adv_Backend.vo.NewsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class NewsServiceImpl implements NewsService {
    private final String ALL_POSTS = "전체";
    private final int PREV_OR_NEXT = 0;
    private final int PREV_ARTICLE = 0;
    private final int NEXT_ARTICLE = 1;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private NewsListRepository NewsListRepository;


    @Override
    public Page<NewsList> getList(String category, int page, int size) {
        Page<NewsList> NewsListPage = null;
        PageRequest pageRequest = PageRequest.of(page, size);
        if (ALL_POSTS.equals(category)) {
            System.out.println(page);
            System.out.println(size);
            System.out.println(pageRequest);
            NewsListPage = NewsListRepository.findAll(pageRequest);
        }
        else {
            NewsListPage = NewsListRepository.findAllByCategory(category, pageRequest);
        }
        return NewsListPage;
    }

    @Override
    public boolean write(Long memberId, String title, String content, long category) {
        News news = News.builder()
                .writer(memberId)
                .title(title)
                .content(content)
                .category(category)
                .build();
        news = newsRepository.save(news);
        return !Objects.isNull(news);
    }

    @Override
    public NewsList getPostById(long newsId) {
        Optional<NewsList> opNews = NewsListRepository.findById(newsId);
        if (!opNews.isPresent()) {
            return null;
        }
        NewsList news = opNews.get();
        Conversion.convertContent((Article) news);
        Conversion.convertDateFormatForArticle((Article) news);
        return news;
    }

    @Override
    public boolean addViews(long newsId) {
        Optional<News> opnews = newsRepository.findById(newsId);
        if (!opnews.isPresent()) {
            return false;
        }
        News news = opnews.get();
        long views = news.getViews() + 1;
        news.setViews(views);
        news = newsRepository.save(news);

        return news.getViews() == views;
    }

    @Override
    public CurrentArticle getPrevAndNextArticle(long newsId) {
        CurrentArticle currentArticle = null;
        List<News> newss = newsRepository.findPrevAndNextNewsIdByNewsId(newsId);
        switch (newss.size()) {
            case 0:
                currentArticle = new CurrentArticle();
                break;
            case 1:
                long prevOrNext = newss.get(PREV_OR_NEXT).getNewsId();
                if (prevOrNext > newsId) {
                    currentArticle = CurrentArticle.builder()
                            .next(prevOrNext)
                            .build();
                } else {
                    currentArticle = CurrentArticle.builder()
                            .prev(prevOrNext)
                            .build();
                }
                break;
            case 2:
                currentArticle = CurrentArticle.builder()
                        .prev(newss.get(PREV_ARTICLE).getNewsId())
                        .next(newss.get(NEXT_ARTICLE).getNewsId())
                        .build();
                break;
        }
        return currentArticle;
    }
}
