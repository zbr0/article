CREATE TABLE "token" (
                         "id" UUID PRIMARY KEY,
                         "token" varchar,
                         "revoked" bool,
                         "expired" bool,
                         "token_type" varchar,
                         "user_id" UUID
);