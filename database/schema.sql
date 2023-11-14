CREATE DATABASE GMS;
USE GMS;

CREATE TABLE gym (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255) NOT NULL,
  postal_code VARCHAR(6) NOT NULL,
  city VARCHAR(255) NOT NULL,
  phone VARCHAR(10) NOT NULL,
  email VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE employee (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  position enum('admin', 'employee', 'trainer') NOT NULL,
  date_of_employment DATE NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE employee_credentials (
  id INT NOT NULL AUTO_INCREMENT,
  login VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  employee_id INT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE employee_card (
  id INT NOT NULL AUTO_INCREMENT,
  employee_number VARCHAR(8) NOT NULL UNIQUE,
  expiration_date DATE NOT NULL,
  employee_id INT,
  PRIMARY KEY (id)
);

CREATE TABLE employee_work_time (
  id INT NOT NULL AUTO_INCREMENT,
  entrance_date DATE NOT NULL,
  entrance_time TIME NOT NULL,
  exit_date DATE NOT NULL,
  exit_time TIME NOT NULL,
  employee_id INT,
  PRIMARY KEY (id)
);

CREATE TABLE client (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  date_of_birth DATE NOT NULL,
  phone_number VARCHAR(10) NOT NULL,
  email VARCHAR(255) NOT NULL,
  membership_card_id INT,
  PRIMARY KEY (id)
);

CREATE TABLE client_credentials (
  id INT NOT NULL AUTO_INCREMENT,
  login VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  client_id INT NOT NULL,
  PRIMARY KEY (id)
);


CREATE TABLE membership_card (
  id INT NOT NULL AUTO_INCREMENT,
  card_number VARCHAR(8) NOT NULL UNIQUE,
  expiration_date DATE NOT NULL,
  type ENUM('multisport', 'membership'),
  original_gym_id INT,
  all_gyms_access BOOLEAN NOT NULL,
  client_id INT,
  PRIMARY KEY (id)
);

CREATE TABLE training (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  date DATE NOT NULL,
  hour TIME NOT NULL,
  capacity INT NOT NULL,
  room int NOT NULL,
  trainer_id INT,
  PRIMARY KEY (id)
);

CREATE TABLE reservation (
  id INT NOT NULL AUTO_INCREMENT,
  client_id INT NOT NULL,
  training_id INT NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE payment (
  id INT NOT NULL AUTO_INCREMENT,
  payment_date DATE NOT NULL,
  amount DECIMAL NOT NULL,
  payment_method ENUM('cash', 'card', 'blik'),
  client_id INT,
  PRIMARY KEY (id)
);

CREATE TABLE gym_visits (
  id INT NOT NULL AUTO_INCREMENT,
  gym_id INT NOT NULL,
  client_id INT NOT NULL,
  entrance_date DATE NOT NULL,
  entrance_time TIME NOT NULL,
  exit_date DATE NOT NULL,
  exit_time TIME NOT NULL,
  PRIMARY KEY (id)
);

-- Add the foreign keys

ALTER TABLE client ADD FOREIGN KEY (membership_card_id) REFERENCES membership_card (id);

ALTER TABLE training ADD FOREIGN KEY (trainer_id) REFERENCES employee (id);

ALTER TABLE reservation ADD FOREIGN KEY (client_id) REFERENCES client (id);

ALTER TABLE reservation ADD FOREIGN KEY (training_id) REFERENCES training (id);

ALTER TABLE payment ADD FOREIGN KEY (client_id) REFERENCES client (id);

ALTER TABLE gym_visits ADD FOREIGN KEY (client_id) REFERENCES client (id);

ALTER TABLE employee_card ADD FOREIGN KEY (employee_id) REFERENCES employee (id);

ALTER TABLE employee_work_time ADD FOREIGN KEY (employee_id) REFERENCES employee (id);

ALTER TABLE gym_visits ADD FOREIGN KEY (gym_id) REFERENCES gym (id);

ALTER TABLE membership_card ADD FOREIGN KEY (original_gym_id) REFERENCES gym (id);

-- Insert a client
INSERT INTO client (first_name, last_name, date_of_birth, phone_number, email)
VALUES ('John', 'Doe', '1980-01-01', '1234567890', 'john.doe@example.com');

-- Get the ID of the client we just inserted
SET @client_id = LAST_INSERT_ID();

-- Insert a client's credentials
INSERT INTO client_credentials (login, password, client_id)
VALUES ('client', 'passwd', @client_id);

-- Insert an admin
INSERT INTO employee (first_name, last_name, position, date_of_employment)
VALUES ('Admin', 'User', 'admin', '2020-01-01');

-- Get the ID of the admin we just inserted
SET @admin_id = LAST_INSERT_ID();

-- Insert an admin's credentials
INSERT INTO employee_credentials (login, password, employee_id)
VALUES ('admin', 'passwd', @admin_id);

-- Insert an employee
INSERT INTO employee (first_name, last_name, position, date_of_employment)
VALUES ('Employee', 'User', 'employee', '2020-01-01');

-- Get the ID of the employee we just inserted
SET @employee_id = LAST_INSERT_ID();

-- Insert an employee's credentials
INSERT INTO employee_credentials (login, password, employee_id)
VALUES ('employee', 'passwd', @employee_id);

-- Insert a trainer
INSERT INTO employee (first_name, last_name, position, date_of_employment)
VALUES ('Trainer', 'User', 'trainer', '2020-01-01');

-- Get the ID of the trainer we just inserted
SET @trainer_id = LAST_INSERT_ID();

-- Insert a trainer's credentials
INSERT INTO employee_credentials (login, password, employee_id)
VALUES ('trainer', 'passwd', @trainer_id);
