package com.company.article.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    public Long getStat() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        return articleRepository.countArticlesFromStartDate(sevenDaysAgo);
    }

    public Page<Article> findAll(Pageable page) {
        return articleRepository.findAll(page);
    }

    public void add(CreateArticleRequest createArticleRequest) {
        Article article = Article.builder()
                .title(createArticleRequest.getTitle())
                .author(createArticleRequest.getAuthor())
                .content(createArticleRequest.getContent())
                .created(createArticleRequest.getLocalDate())
                .build();
        articleRepository.save(article);
    }
}
