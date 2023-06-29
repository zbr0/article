package com.company.article;

import com.company.article.domain.Article;
import com.company.article.domain.ArticleRepository;
import com.company.article.domain.ArticleService;
import com.company.article.domain.CreateArticleRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
@SpringBootTest
@ContextConfiguration(initializers = {ArticleServiceTest.Initializer.class})
public class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15.1-alpine")
            .withExposedPorts(5432)
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }

    @BeforeEach
    void setUp() {
        articleRepository.deleteAll();
    }

    @Test
    void contextLoads() {
        assertNotNull(articleService);
    }

    @Test
    public void getStat_WithNoArticles_ShouldReturnZero() {
        //Given there are no articles
        //When getStat is called
        Long count = articleService.getStat();
        //Then it should return 0
        assertEquals(0, count);
    }

    @Test
    public void getStat_WithArticlesMoreThanWeekOld_ShouldReturnZero() {
        //Given there are 5 articles created more than a week ago
        IntStream.range(0, 5).forEach(i ->
                articleRepository.save(Article.builder()
                        .title("Title " + i)
                        .author("Author " + i)
                        .content("Content " + i)
                        .created(LocalDate.now().minusDays(8))
                        .build())
        );
        //When getStat is called
        Long count = articleService.getStat();
        //Then it should return 0 because those articles are more than a week old
        assertEquals(0, count);
    }

    @Test
    public void getStat_WithSomeArticlesInLastWeek_ShouldReturnTheirCount() {
        //Given there are 3 articles created in the last week
        IntStream.range(0, 3).forEach(i ->
                articleRepository.save(Article.builder()
                        .title("Title " + i)
                        .author("Author " + i)
                        .content("Content " + i)
                        .created(LocalDate.now().minusDays(6))
                        .build())
        );
        //And 2 articles created more than a week ago
        IntStream.range(3, 5).forEach(i ->
                articleRepository.save(Article.builder()
                        .title("Title " + i)
                        .author("Author " + i)
                        .content("Content " + i)
                        .created(LocalDate.now().minusDays(8))
                        .build())
        );
        //When getStat is called
        Long count = articleService.getStat();
        //Then it should return 3 because there are 3 articles in the last week
        assertEquals(3, count);
    }

    @Test
    public void findAll_WithNoArticles_ShouldReturnEmptyPage() {
        // Given no articles exist

        // When findAll is called
        Page<Article> articles = articleService.findAll(Pageable.unpaged());

        // Then it should return an empty page
        assertTrue(articles.isEmpty());
    }

    @Test
    public void findAll_WithSomeArticles_ShouldReturnTheirPage() {
        // Given there are 3 articles
        IntStream.range(0, 3).forEach(i ->
                articleRepository.save(Article.builder()
                        .title("Title " + i)
                        .author("Author " + i)
                        .content("Content " + i)
                        .created(LocalDate.now())
                        .build())
        );

        // When findAll is called
        Page<Article> articles = articleService.findAll(Pageable.unpaged());

        // Then it should return a page with those articles
        assertEquals(3, articles.getNumberOfElements());
    }

    @Test
    public void findAll_WithMoreArticlesThanPageSize_ShouldReturnFullPage() {
        // Given there are 5 articles
        IntStream.range(0, 5).forEach(i ->
                articleRepository.save(Article.builder()
                        .title("Title " + i)
                        .author("Author " + i)
                        .content("Content " + i)
                        .created(LocalDate.now())
                        .build())
        );

        // When findAll is called with a page size of 3
        Page<Article> articles = articleService.findAll(PageRequest.of(0, 3));

        // Then it should return a page with 3 articles
        assertEquals(3, articles.getNumberOfElements());
    }


    @Test
    void testGetStatWithOneArticle() {
        Article article = Article.builder()
                .title("title")
                .author("author")
                .content("content")
                .created(LocalDate.now())
                .build();
        articleRepository.save(article);

        Long stats = articleService.getStat();
        assertEquals(1, stats);
    }

    @Test
    void testFindAllWithNoArticles() {
        Page<Article> articles = articleService.findAll(PageRequest.of(0, 5));
        assertEquals(0, articles.getTotalElements());
    }

    @Test
    void testFindAllWithOneArticle() {
        Article article = Article.builder()
                .title("title")
                .author("author")
                .content("content")
                .created(LocalDate.now())
                .build();
        articleRepository.save(article);

        Page<Article> articles = articleService.findAll(PageRequest.of(0, 5));
        assertEquals(1, articles.getTotalElements());
    }

    @Test
    void testAddValidArticle() throws Exception {
        CreateArticleRequest request = CreateArticleRequest.builder()
                .title("title")
                .author("author")
                .content("content")
                .localDate(LocalDate.now())
                .build();

        articleService.add(request);
        assertEquals(1, articleRepository.count());
    }

    @Test
    void testAddArticleWithLongTitle() {
        CreateArticleRequest request = CreateArticleRequest.builder()
                .title("title".repeat(101))
                .author("author")
                .content("content")
                .localDate(LocalDate.now())
                .build();

        assertThrows(Exception.class, () -> articleService.add(request));
    }

    @Test
    void testAddArticleTitleNull() {
        CreateArticleRequest request = CreateArticleRequest.builder()
                .author("author")
                .content("content")
                .localDate(LocalDate.now())
                .build();
        assertThrows(Exception.class, () -> articleService.add(request));
    }

    @Test
    void testAddArticleAuthorNull() {
        CreateArticleRequest request = CreateArticleRequest.builder()
                .title("title")
                .content("content")
                .localDate(LocalDate.now())
                .build();
        assertThrows(Exception.class, () -> articleService.add(request));
    }

    @Test
    void testAddArticleContentNull() {
        CreateArticleRequest request = CreateArticleRequest.builder()
                .title("title")
                .author("author")
                .localDate(LocalDate.now())
                .build();
        assertThrows(Exception.class, () -> articleService.add(request));
    }
}
