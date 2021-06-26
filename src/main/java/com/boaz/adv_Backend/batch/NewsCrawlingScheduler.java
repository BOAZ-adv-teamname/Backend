package com.boaz.adv_Backend.batch;


import com.boaz.adv_Backend.repository.NewsRepository;
import com.boaz.adv_Backend.vo.News;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
@PropertySource("static/batch.properties")
public class NewsCrawlingScheduler {

    @Qualifier("webApplicationContext")
    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    NewsRepository newsRepository;

    static boolean flag = false;

    @Scheduled(fixedDelay = 20000000)
    @Async
    public void crawling_naver_main_news() throws IOException, ParseException {
        if (flag) {
            log.info("finished!!!!!!!");
        }
        String news_list_url_format = "https://news.naver.com/main/list.nhn?" +
                "mode=LS2D" +
                "&mid=shm" +
                "&sid1=%s" +
                "&sid2=%s"+
                "&date=%s"+
                "&page=%d";
        Resource resource = resourceLoader.getResource("classpath:/static/newsClassification.json");
        log.info(String.valueOf(resource.exists()));

        Map<String, Map<String, String>> typeMap = getClassfication();
        long before = System.currentTimeMillis();
        for (String sid2 : typeMap.keySet()) {
            String sid1 = typeMap.get(sid2).get("sid1");
            String class1 = typeMap.get(sid2).get("class1");
            String class2 = typeMap.get(sid2).get("class2");
            Calendar calendar = Calendar.getInstance();
            String date = new SimpleDateFormat("YYYYMMdd").format(calendar.getTime());
            int page = 1;
            int repeatCnt = 0;
            while (true) {
                boolean hasNextDate = false;
                while (true) {
                    String news_list_url = String.format(news_list_url_format,
                            sid1, sid2, date, page);
                    log.info(news_list_url);
                    Document document = Jsoup.connect(news_list_url).get();
                    String[] arr = new String[]{".type06_headline", ".type06"};
                    for (String str : arr) {
                        Elements newsList = document.select(str);
                        if (!ObjectUtils.isEmpty(newsList)) {
                            newsList = newsList.select("li");
                        }
                        for (Element elem : newsList) {
                            Elements aElems = elem.select("a");
                            if (!ObjectUtils.isEmpty(aElems)) {
                                String href = aElems.get(0).attr("href");
                                log.info(href);
                                repeatCnt++;
                                log.info(String.valueOf(repeatCnt));
                                getNewsInfo(href, sid1, sid2, class1, class2);
                            }
                        }
                    }
                    Elements pageElems = document.getElementsByClass("nclicks(fls.page)");
                    boolean hasNextPage = false;
                    if (!ObjectUtils.isEmpty(pageElems)) {
                        String lastPageStr = pageElems.last().text();
                        if (!"다음".equals(lastPageStr) && !"이전".equals(lastPageStr)) {
                            int lastPage = Integer.parseInt(pageElems.last().text());
                            if (lastPage > page) {
                                hasNextPage = true;
                            }
                        } else if("다음".equals(lastPageStr)) {
                            hasNextPage = true;
                        }
                    }
                    if (!hasNextPage) {
                        page = 1;
                        Elements dateElems = document.getElementsByClass("nclicks(fls.date)");
                        if (!ObjectUtils.isEmpty(dateElems)) {
                            Element dateElem = dateElems.last();
                            String preDate = dateElem.attr("href");
                            Pattern pattern = Pattern.compile("[0-9]{8}");
                            Matcher matcher = pattern.matcher(preDate);
                            if (matcher.find()) {
                                int curDate = Integer.parseInt(date);
                                int date1 = Integer.parseInt(matcher.group());
                                if (date1 < curDate) {
                                    hasNextDate = true;
                                    date = String.valueOf(curDate-1);
                                }
                            }
                        }
                        break;
                    }
                    page++;
                    if (repeatCnt > 1) break;
                }
                if (!hasNextDate) {
                    date = new SimpleDateFormat("YYYYMMdd").format(calendar.getTime());
                    break;
                }
                if (repeatCnt > 1) break;
            }
            if (repeatCnt > 1) break;
        }
        long after = System.currentTimeMillis();
        flag = true;
        log.info(String.valueOf(after - before));

        return;
    }

    private void getNewsInfo(String url,String sid1,String sid2, String class1, String class2) throws IOException, ParseException {
        Pattern pattern1 = Pattern.compile("oid=[0-9]{3}");
        Matcher matcher = pattern1.matcher(url);
        String oid = "";
        if (matcher.find()) {
            String str = matcher.group();
            oid = str.substring(4);
        }

        Map<String, Map<String, String>> pressMap = getPressList();
        String mediaName = "";
        String rootDomain = "";
        if (pressMap.containsKey(oid)) {
            mediaName = pressMap.get(oid).get("mediaName");
            rootDomain = pressMap.get(oid).get("rootDomain");
        }

        Document document = Jsoup.connect(url).get();

        String title = "";
        String summary = "";
        String content = "";
        String pubDate = "";

        Elements titleElems = document.select("meta[property^=og:title]");
        Elements summaryElems = document.select(".media_end_summary");
        Elements contentElems = document.select("._article_body_contents");
        Elements dateElems = document.select(".t11");

        if (!ObjectUtils.isEmpty(titleElems)) {
            title = titleElems.get(0).attr("content");
        }
        if (!ObjectUtils.isEmpty(summaryElems)) {
            summary = summaryElems.get(0).text();
        }
        if (!ObjectUtils.isEmpty(contentElems)) {
            content = contentElems.get(0).text();
        }
        if (!ObjectUtils.isEmpty(dateElems)) {
            pubDate = dateElems.get(0).text();
        }

        //url 중복검사 필요
        if (newsRepository.findById(5L).isPresent()) return;

        News news = new News();

        news.setCategory(1L);
        news.setTitle(title);
        news.setContent(content);
        news.setLikes(1L);
        news.setViews(1L);
        news.setWriter(1L);
        news.setCategory(1L);
        news.setPrecedent(1L);
        news.setMedia(mediaName);

        newsRepository.save(news);

        return;

    }

    private Map<String, Map<String,String>> getPressList() {
        Resource resource = resourceLoader.getResource("classpath:/static/pressList.json");
        Map<String, Map<String,String>> map = new HashMap<>();
        try {
            StringBuilder sb = new StringBuilder();
            Path path = Paths.get(resource.getURI());
            List<String> content = Files.readAllLines(path);
            content.forEach(str -> sb.append(str));

            String jsonStr = sb.toString();

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonStr);
            JSONObject jsonArray = (JSONObject) parser.parse(String.valueOf(jsonObject.get("pressList")));
            JSONArray jsonArray1 = (JSONArray) parser.parse(String.valueOf(jsonArray.get("press")));

            for (Object obj : jsonArray1) {
                Map<String, String> tempMap1 = (Map<String, String>)obj;
                String oid = tempMap1.get("oid");
                Map<String, String> tempMap2 = new HashMap<>();
                tempMap2.put("rootDomain",tempMap1.get("rootDomain"));
                tempMap2.put("mediaName",tempMap1.get("mediaName"));
                map.put(oid,tempMap2);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return map;
    }

    private Map<String, Map<String, String>> getClassfication() {
        Resource resource = resourceLoader.getResource("classpath:/static/newsClassification.json");
        Map<String,Map<String, String>> map = new HashMap<>();
        try {
            StringBuilder sb = new StringBuilder();
            Path path = Paths.get(resource.getURI());
            List<String> content = Files.readAllLines(path);
            content.forEach(str -> sb.append(str));

            String jsonStr = sb.toString();

            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonStr);
            JSONObject jsonObject1 = (JSONObject) parser.parse(String.valueOf(jsonObject.get("classificationList")));
            JSONArray jsonArray = (JSONArray) parser.parse(String.valueOf(jsonObject1.get("classification")));

            for (Object obj : jsonArray) {
                Map<String, String> tempMap1 = (Map<String, String>) obj;
                Map<String, String> tempMap2 = new HashMap<>();

                tempMap2.put("class1",tempMap1.get("class1"));
                tempMap2.put("class2",tempMap1.get("class2"));
                tempMap2.put("sid1",tempMap1.get("sid1"));
                map.put(tempMap1.get("sid2"),tempMap2);
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return map;
    }
}

