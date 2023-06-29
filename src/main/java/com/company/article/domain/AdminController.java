package com.company.article.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AdminController {

    private final ArticleService articleService;

    @GetMapping
    @PreAuthorize("hasAuthority('admin:read_stat')")
    public ResponseEntity<Long> stat() {

        return ResponseEntity.ok(
                articleService.getStat()
        );
    }
}
