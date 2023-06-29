package com.company.article.domain;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article")
@PreAuthorize("hasRole('USER')")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
@Hidden
public class ArticleController {
  private final ArticleService articleService;

  @GetMapping
  @PreAuthorize("hasAuthority('user:read')")
  public ResponseEntity<Page<Article>> getAll(
          @PageableDefault(value = 10, page = 0)
          Pageable page
  ) {
    return ResponseEntity.ok(
            articleService.findAll(page)
    );
  }

  @PostMapping
  @PreAuthorize("hasAuthority('user:create')")
  public ResponseEntity<String> create(
          @RequestBody
          CreateArticleRequest createArticleRequest) {
    articleService.add(createArticleRequest);
    return ResponseEntity.ok("Hello from secured endpoint");
  }


}
