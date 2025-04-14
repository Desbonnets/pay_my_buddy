CREATE TABLE transaction
(
    id            INT AUTO_INCREMENT NOT NULL,
    sender        INT                NOT NULL,
    receiver      INT                NOT NULL,
    `description` VARCHAR(255)       NULL,
    amount        DOUBLE             NULL,
    KEY (`receiver`),
    KEY (`sender`),
    CONSTRAINT pk_transaction PRIMARY KEY (id)
);

CREATE TABLE user
(
    id       INT AUTO_INCREMENT NOT NULL,
    username VARCHAR(255)       NOT NULL,
    email    VARCHAR(255)       NOT NULL,
    password VARCHAR(255)       NOT NULL,
    UNIQUE KEY (`email`),
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE user_connections
(
    connection_id INT NOT NULL,
    user_id       INT NOT NULL,
    KEY (`user_id`),
    KEY (`connection_id`),
    CONSTRAINT pk_user_connections PRIMARY KEY (connection_id, user_id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_RECEIVER FOREIGN KEY (receiver) REFERENCES user (id);

ALTER TABLE transaction
    ADD CONSTRAINT FK_TRANSACTION_ON_SENDER FOREIGN KEY (sender) REFERENCES user (id);

ALTER TABLE user_connections
    ADD CONSTRAINT fk_usecon_on_connection FOREIGN KEY (connection_id) REFERENCES user (id);

ALTER TABLE user_connections
    ADD CONSTRAINT fk_usecon_on_user FOREIGN KEY (user_id) REFERENCES user (id);