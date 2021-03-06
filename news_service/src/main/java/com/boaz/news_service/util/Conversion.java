package com.boaz.news_service.util;

import com.boaz.news_service.vo.Article;
import com.boaz.news_service.vo.NewsList;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Conversion {
    private static final int DATE_START = 0;
    private static final int DATE_END = 10;
    private static final int TIME_START = 11;
    private static final int TIME_END = 16;
    private static final int BEGIN = 0;
    private static final int MAX_LENGTH = 30;

    public static void convertDateFormatForBoard(List<NewsList> boards) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date());

        boards.forEach(board -> {
            if (board.getDate().substring(DATE_START, DATE_END).equals(today)) {
                board.setDate(board.getDate().substring(TIME_START, TIME_END));
            } else {
                board.setDate(board.getDate().substring(DATE_START, DATE_END));
            }
        });
    }

    public static void convertTitleLength(List<NewsList> boards) {
        boards.forEach(board -> {
            if (board.getTitle().length() > MAX_LENGTH) {
                StringBuilder title = new StringBuilder(board.getTitle().substring(BEGIN, MAX_LENGTH));
                title.append( "...");
                board.setTitle(title.toString());
            }
        });
    }

    public static int calcStartPage(int page) {
        return ((page - 1) / 10) * 10 + 1;
    }

    public static void convertContent(Article article) {
        String oldContent = article.getContent();
        article.setContent(oldContent.replace("\n", "<br>"));
    }

    public static void convertDateFormatForArticle(Article article) {
        article.setDate(article.getDate().substring(DATE_START, TIME_END));
    }
}
