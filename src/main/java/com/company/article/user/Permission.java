package com.company.article.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    ADMIN_READ_STAT("admin:read_stat"),
    USER_READ("user:read"),
    USER_CREATE("user:create")
    ;

    @Getter
    private final String permission;
}
