DROP TABLE IF EXISTS todouser CASCADE;

CREATE TABLE todouser
(
  id SERIAL,
  email varchar(80) UNIQUE NOT NULL,
  password varchar(80),
  registered boolean DEFAULT FALSE,
  confirmation_code varchar(280),
  CONSTRAINT todouser_pkey PRIMARY KEY (id)
);
