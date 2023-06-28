CREATE TABLE "artilce" (
                           "id" UUID PRIMARY KEY,
                           "title" varchar,
                           "content" varchar(100),
                           "author" varchar,
                           "create_at" timestamp
);

CREATE TABLE "_user" (
                         "id" UUID PRIMARY KEY,
                         "firstname" varchar,
                         "lastname" varchar,
                         "email" varchar UNIQUE NOT NULL,
                         "password" varchar UNIQUE NOT NULL,
                         "role" varchar
);