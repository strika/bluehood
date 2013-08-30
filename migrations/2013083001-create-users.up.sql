CREATE TABLE users
(
  id         serial primary key,
  email      VARCHAR(50) not null,
  name       VARCHAR(50) not null,
  password   VARCHAR(100) not null,
  created_at timestamp,
  updated_at timestamp
);
