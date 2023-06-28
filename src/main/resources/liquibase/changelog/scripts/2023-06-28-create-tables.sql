CREATE TABLE "artilce" (
                           "id" UUID PRIMARY KEY,
                           "title" varchar,
                           "content" varchar(100),
                           "author" varchar,
                           "create_at" timestamp
);

CREATE TABLE "user" (
                        "id" UUID PRIMARY KEY,
                        "username" varchar UNIQUE NOT NULL,
                        "email" varchar UNIQUE NOT NULL,
                        "password" varchar UNIQUE NOT NULL
);
