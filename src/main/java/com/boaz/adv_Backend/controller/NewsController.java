package com.boaz.adv_Backend.controller;

import com.boaz.adv_Backend.repository.NewsListRepository;
import com.boaz.adv_Backend.repository.NewsRepository;
import com.boaz.adv_Backend.service.CategoryService;
import com.boaz.adv_Backend.service.NewsService;
import com.boaz.adv_Backend.service.ReplyService;
import com.boaz.adv_Backend.util.Conversion;
import com.boaz.adv_Backend.util.CurrentArticle;
import com.boaz.adv_Backend.vo.Category;
import com.boaz.adv_Backend.vo.NewsList;
import com.boaz.adv_Backend.vo.ReplyList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
public class NewsController {
    private final String DEFAULT_CATEGORY = "전체";
    private final String DEFAULT_PAGE = "1";
    private final String DEFAULT_LIST_SIZE = "10";
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
            data.put("newsId", newss.get(i).getBoardId());
            data.put("category", newss.get(i).getCategory());
            data.put("title", newss.get(i).getTitle());
            data.put("date", newss.get(i).getDate());
            data.put("likes", newss.get(i).getLikes());
            data.put("replies", newss.get(i).getReplies());
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
    public void viewPost(@PathVariable("idx") int newsId) {
        if (!newsService.addViews(newsId)) {
            return;
        }

        NewsList article = newsService.getPostById(newsId);
        if (Objects.isNull(article)) {
            return;
        }
        CurrentArticle currentArticle = newsService.getPrevAndNextArticle(newsId);
        List<ReplyList> replies = replyService.getRepliesByNewsId(newsId);

        // todo

        return;
    }


}
