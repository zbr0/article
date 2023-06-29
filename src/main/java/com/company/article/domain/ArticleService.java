package com.company.article.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    public Long getStat() {
        log.info("statistics getting process started");
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        Long result = articleRepository.countArticlesFromStartDate(sevenDaysAgo);
        log.info("find {} articles for last 7 days", result);
        return articleRepository.countArticlesFromStartDate(sevenDaysAgo);
    }

    public Page<Article> findAll(Pageable page) {
        log.info("getting page of article process started");
        Page<Article> articlePage = articleRepository.findAll(page);
        log.info("find {} articles", articlePage.stream().count());
        return articlePage;
    }

    public void add(CreateArticleRequest createArticleRequest) throws Exception {
        log.info("start add new article {}", createArticleRequest);
        Article article = Article.builder()
                .title(createArticleRequest.getTitle())
                .author(createArticleRequest.getAuthor())
                .content(createArticleRequest.getContent())
                .created(createArticleRequest.getLocalDate())
                .build();
        articleRepository.save(article);
        log.info("end add new article {}", article);
    }
}
