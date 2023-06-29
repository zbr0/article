CREATE TABLE "article" (
                           "id" UUID PRIMARY KEY,
                           "title" varchar NOT NULL,
                           "content" varchar(100) NOT NULL,
                           "author" varchar NOT NULL,
                           "created" timestamp NOT NULL
);

CREATE TABLE "_user" (
                         "id" UUID PRIMARY KEY,
                         "firstname" varchar,
                         "lastname" varchar,
                         "email" varchar UNIQUE NOT NULL,
                         "password" varchar UNIQUE NOT NULL,
                         "role" varchar
);