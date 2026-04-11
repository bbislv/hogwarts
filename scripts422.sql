CREATE TABLE person (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age INTEGER CHECK (age >= 0),
    has_driver_license BOOLEAN DEFAULT FALSE
);

CREATE TABLE car (
    id BIGSERIAL PRIMARY KEY,
    make VARCHAR(255) NOT NULL,
    model VARCHAR(255) NOT NULL,
    cost DECIMAL(15, 2) CHECK (cost >= 0)
);

CREATE TABLE person_car (
    person_id BIGINT NOT NULL,
    car_id BIGINT NOT NULL,
    PRIMARY KEY (person_id, car_id),
    FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE,
    FOREIGN KEY (car_id) REFERENCES car(id) ON DELETE CASCADE
);

INSERT INTO person (name, age, has_driver_license) VALUES
('Harry Potter', 17, true),
('Hermione Granger', 17, true),
('Ron Weasley', 17, true),
('Draco Malfoy', 17, false);

INSERT INTO car (make, model, cost) VALUES
('Ford', 'Anglia', 5000.00),
('Mercedes', 'E-Class', 45000.00),
('BMW', 'X5', 55000.00);

INSERT INTO person_car (person_id, car_id) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 3);