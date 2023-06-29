package com.company.article;

import com.company.article.domain.Article;
import com.company.article.domain.ArticleRepository;
import com.company.article.domain.ArticleService;
import com.company.article.domain.CreateArticleRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.transaction.TransactionSystemException;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ArticleService2Test {

    @InjectMocks
    private ArticleService articleService;

    @Mock
    private ArticleRepository articleRepository;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStat() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        when(articleRepository.countArticlesFromStartDate(sevenDaysAgo)).thenReturn(5L);

        Long result = articleService.getStat();

        assertEquals(5L, result);
        verify(articleRepository, times(2)).countArticlesFromStartDate(sevenDaysAgo);
    }

    @Test
    public void testAdd() throws Exception {
        CreateArticleRequest request = new CreateArticleRequest();
        request.setTitle("test title");
        request.setAuthor("test author");
        request.setContent("test content");
        request.setLocalDate(LocalDate.now());

        Article article = Article.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .content(request.getContent())
                .created(request.getLocalDate())
                .build();

        when(articleRepository.save(any(Article.class))).thenReturn(article);

        articleService.add(request);

        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    public void testAddWithLongTitle() throws Exception {
        CreateArticleRequest request = new CreateArticleRequest();
        String longTitle = String.join("", Collections.nCopies(100, "a")); // Generates a string of 100 "a" characters
        request.setTitle(longTitle);
        request.setAuthor("test author");
        request.setContent("test content");
        request.setLocalDate(LocalDate.now());

        Article article = Article.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .content(request.getContent())
                .created(request.getLocalDate())
                .build();

        when(articleRepository.save(any(Article.class))).thenReturn(article);

        articleService.add(request);

        verify(articleRepository, times(1)).save(any(Article.class));
    }

    @Test
    public void testAddWithTooLongTitle() {
        CreateArticleRequest request = new CreateArticleRequest();
        String longTitle = String.join("", Collections.nCopies(101, "a")); // Generates a string of 101 "a" characters
        request.setTitle(longTitle);
        request.setAuthor("test author");
        request.setContent("test content");
        request.setLocalDate(LocalDate.now());

        Article article = Article.builder()
                .title(request.getTitle())
                .author(request.getAuthor())
                .content(request.getContent())
                .created(request.getLocalDate())
                .build();

        when(articleRepository.save(article)).thenThrow(TransactionSystemException.class);


        // Assuming your service or repository throws an exception when the title is too long
        assertThrows(TransactionSystemException.class, () -> {
            articleService.add(request);
        });

        verify(articleRepository, times(1)).save(any(Article.class));
    }
}
