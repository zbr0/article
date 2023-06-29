package com.company.article;

import org.springframework.boot.SpringApplication;

public class TestApplication {
    public static void main(String[] args) {
        SpringApplication
                .from(ArticleApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}