CREATE TABLE endpoint_hits
(
    id        BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    app       VARCHAR(255)  NOT NULL,
    uri       VARCHAR(1024) NOT NULL,
    ip        VARCHAR(45)   NOT NULL,
    timestamp TIMESTAMP     NOT NULL
);


