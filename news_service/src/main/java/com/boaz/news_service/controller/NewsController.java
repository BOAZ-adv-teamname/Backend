package com.boaz.news_service.controller;

import com.boaz.news_service.repository.NewsListRepository;
import com.boaz.news_service.repository.NewsRepository;
import com.boaz.news_service.service.CategoryService;
import com.boaz.news_service.service.NewsService;
import com.boaz.news_service.service.ReplyService;
import com.boaz.news_service.util.Conversion;
import com.boaz.news_service.util.CurrentArticle;
import com.boaz.news_service.vo.Category;
import com.boaz.news_service.vo.Member;
import com.boaz.news_service.vo.NewsList;
import com.boaz.news_service.vo.ReplyList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
public class NewsController {
    private final String DEFAULT_CATEGORY = "전체";
    private final String DEFAULT_PAGE = "1";
    private final String DEFAULT_LIST_SIZE = "5";
    private final int NOT_EXIST = 0;

    @Autowired
    private NewsService newsService;
    @Autowired
    private NewsListRepository newsListRepository;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private ReplyService replyService;

    @GetMapping("/news")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public void showNews(
            @RequestParam(defaultValue = DEFAULT_CATEGORY, required = false) String category,
            @RequestParam(defaultValue = DEFAULT_PAGE, required = false) Integer page,
            @RequestParam(defaultValue = DEFAULT_LIST_SIZE, required = false) Integer size,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        JSONObject res = new JSONObject();
        Page<NewsList> newsListPage = newsService.getList(category, page - 1, size);
        List<NewsList> newss = newsListPage.getContent();
        System.out.println("newss.size() : "+newss.size());

        if (newss.size() == NOT_EXIST) {
            res.put("result",-1);
            return;
        }

        List<Category> categories = categoryService.getList();
        int totalPage = newsListPage.getTotalPages();
        int startPage = Conversion.calcStartPage(page);

        Conversion.convertDateFormatForBoard(newss);
        Conversion.convertTitleLength(newss);

        JSONArray newsArray = new JSONArray();
        for (int i = 0; i < newss.size(); i++) {
            JSONObject data = new JSONObject();
            data.put("newsId", newss.get(i).getNewsId());
            data.put("category", newss.get(i).getCategory());
            data.put("title", newss.get(i).getTitle());
            data.put("date", newss.get(i).getDate());
            data.put("summary", newss.get(i).getSummary());
            data.put("content", newss.get(i).getContent());
            data.put("media", newss.get(i).getMedia());
            newsArray.add(i, data);
        }

        res.put("news", newsArray);
        res.put("category", category);
        res.put("selectSize", size);
        res.put("curPage", page);
        res.put("totalPage", totalPage);
        res.put("startPage", startPage);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);

        return;
    }

    @GetMapping("/news/{idx}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    public void viewPost(@PathVariable("idx") int newsId,
                         HttpServletRequest request,
                         HttpServletResponse response) throws IOException{

        if (!newsService.addViews(newsId)) {
            return;
        }
        JSONObject res = new JSONObject();

        NewsList article = newsService.getPostById(newsId);
        if (Objects.isNull(article)) {
            return;
        }
        CurrentArticle currentArticle = newsService.getPrevAndNextArticle(newsId);
        List<ReplyList> replies = replyService.getRepliesByNewsId(newsId);

        Member member = (Member) request.getSession().getAttribute("loginMember");

        boolean isLike = false;

        res.put("prev", currentArticle.getPrev());
        res.put("next", currentArticle.getNext());
        res.put("newsId", article.getNewsId());
        res.put("category", article.getCategory());
        res.put("title", article.getTitle());
        res.put("content", article.getContent());
        res.put("date", article.getDate());
        res.put("summary", article.getSummary());
        res.put("media", article.getMedia());

        JSONArray replyArray = new JSONArray();

        for (int i = 0; i < replies.size(); i++) {
            JSONObject data = new JSONObject();
            data.put("boardId", replies.get(i).getNewsId());
            data.put("replyId", replies.get(i).getReplyId());
            data.put("content", replies.get(i).getContent());
            data.put("date", replies.get(i).getDate());

            replyArray.add(i, data);
        }

        res.put("reply", replyArray);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print(res);

        return;
    }
}
