CREATE TABLE roles
(
id UUID UNIQUE NOT NULL,
authority VARCHAR(20) NOT NULL,
PRIMARY KEY (id)
);

INSERT INTO roles
VALUES
('3ddda5e7-625e-4bd3-8db9-cceb46992fb9', 'USER'),
('1343ff10-fc3a-49d5-8c4c-8cca7f34cd5e', 'ADMIN');
