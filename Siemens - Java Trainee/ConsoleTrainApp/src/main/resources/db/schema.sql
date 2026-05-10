DROP TABLE IF EXISTS email_outbox;
DROP TABLE IF EXISTS train_delay_events;
DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS booking_legs;
DROP TABLE IF EXISTS booking_passengers;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS train_run_stops;
DROP TABLE IF EXISTS train_runs;
DROP TABLE IF EXISTS route_stops;
DROP TABLE IF EXISTS routes;
DROP TABLE IF EXISTS trains;
DROP TABLE IF EXISTS stations;
DROP TABLE IF EXISTS users;

CREATE TABLE stations (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          code VARCHAR(20) NOT NULL UNIQUE,
                          name VARCHAR(100) NOT NULL,
                          city VARCHAR(100) NOT NULL,
                          active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE trains (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        train_number VARCHAR(30) NOT NULL UNIQUE,
                        name VARCHAR(100),
                        capacity INT NOT NULL,
                        active BOOLEAN NOT NULL DEFAULT TRUE,

                        CONSTRAINT chk_train_capacity CHECK (capacity > 0)
);

CREATE TABLE routes (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        code VARCHAR(50) NOT NULL UNIQUE,
                        name VARCHAR(150) NOT NULL,
                        active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE route_stops (
                             id BIGINT PRIMARY KEY AUTO_INCREMENT,
                             route_id BIGINT NOT NULL,
                             station_id BIGINT NOT NULL,
                             stop_order INT NOT NULL,
                             distance_from_start_km DECIMAL(8,2) NOT NULL DEFAULT 0,

                             CONSTRAINT fk_route_stops_route
                                 FOREIGN KEY (route_id) REFERENCES routes(id),

                             CONSTRAINT fk_route_stops_station
                                 FOREIGN KEY (station_id) REFERENCES stations(id),

                             CONSTRAINT uq_route_stop_order
                                 UNIQUE (route_id, stop_order),

                             CONSTRAINT uq_route_station
                                 UNIQUE (route_id, station_id),

                             CONSTRAINT chk_route_stop_order
                                 CHECK (stop_order > 0),

                             CONSTRAINT chk_route_distance
                                 CHECK (distance_from_start_km >= 0)
);

CREATE TABLE train_runs (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT,
                            train_id BIGINT NOT NULL,
                            route_id BIGINT NOT NULL,
                            run_code VARCHAR(80) NOT NULL UNIQUE,
                            service_date DATE NOT NULL,
                            status VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED',
                            delay_minutes INT NOT NULL DEFAULT 0,
                            created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_train_runs_train
                                FOREIGN KEY (train_id) REFERENCES trains(id),

                            CONSTRAINT fk_train_runs_route
                                FOREIGN KEY (route_id) REFERENCES routes(id),

                            CONSTRAINT chk_train_run_delay
                                CHECK (delay_minutes >= 0)
);

CREATE TABLE train_run_stops (
                                 id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                 train_run_id BIGINT NOT NULL,
                                 station_id BIGINT NOT NULL,
                                 stop_order INT NOT NULL,
                                 planned_arrival DATETIME,
                                 planned_departure DATETIME,

                                 CONSTRAINT fk_train_run_stops_train_run
                                     FOREIGN KEY (train_run_id) REFERENCES train_runs(id),

                                 CONSTRAINT fk_train_run_stops_station
                                     FOREIGN KEY (station_id) REFERENCES stations(id),

                                 CONSTRAINT uq_train_run_stop_order
                                     UNIQUE (train_run_id, stop_order),

                                 CONSTRAINT uq_train_run_station
                                     UNIQUE (train_run_id, station_id),

                                 CONSTRAINT chk_train_run_stop_order
                                     CHECK (stop_order > 0)
);

CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       full_name VARCHAR(150) NOT NULL,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role VARCHAR(30) NOT NULL,
                       active BOOLEAN NOT NULL DEFAULT TRUE,
                       created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE customers (
                           id BIGINT PRIMARY KEY AUTO_INCREMENT,
                           full_name VARCHAR(150) NOT NULL,
                           email VARCHAR(150) NOT NULL UNIQUE,
                           created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE bookings (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          booking_code VARCHAR(80) NOT NULL UNIQUE,
                          customer_id BIGINT NOT NULL,
                          status VARCHAR(30) NOT NULL DEFAULT 'CONFIRMED',
                          created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                          CONSTRAINT fk_bookings_customer
                              FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE booking_passengers (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    booking_id BIGINT NOT NULL,
                                    full_name VARCHAR(150) NOT NULL,

                                    CONSTRAINT fk_booking_passengers_booking
                                        FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

CREATE TABLE booking_legs (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              booking_id BIGINT NOT NULL,
                              train_run_id BIGINT NOT NULL,
                              origin_run_stop_id BIGINT NOT NULL,
                              destination_run_stop_id BIGINT NOT NULL,
                              leg_order INT NOT NULL,
                              passenger_count INT NOT NULL,
                              status VARCHAR(30) NOT NULL DEFAULT 'CONFIRMED',

                              CONSTRAINT fk_booking_legs_booking
                                  FOREIGN KEY (booking_id) REFERENCES bookings(id),

                              CONSTRAINT fk_booking_legs_train_run
                                  FOREIGN KEY (train_run_id) REFERENCES train_runs(id),

                              CONSTRAINT fk_booking_legs_origin_stop
                                  FOREIGN KEY (origin_run_stop_id) REFERENCES train_run_stops(id),

                              CONSTRAINT fk_booking_legs_destination_stop
                                  FOREIGN KEY (destination_run_stop_id) REFERENCES train_run_stops(id),

                              CONSTRAINT chk_booking_leg_order
                                  CHECK (leg_order > 0),

                              CONSTRAINT chk_booking_passenger_count
                                  CHECK (passenger_count > 0),

                              CONSTRAINT chk_booking_origin_destination
                                  CHECK (origin_run_stop_id <> destination_run_stop_id)
);

CREATE TABLE tickets (
                         id BIGINT PRIMARY KEY AUTO_INCREMENT,
                         ticket_code VARCHAR(80) NOT NULL UNIQUE,
                         booking_leg_id BIGINT NOT NULL,
                         passenger_id BIGINT NOT NULL,
                         status VARCHAR(30) NOT NULL DEFAULT 'VALID',
                         created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                         CONSTRAINT fk_tickets_booking_leg
                             FOREIGN KEY (booking_leg_id) REFERENCES booking_legs(id),

                         CONSTRAINT fk_tickets_passenger
                             FOREIGN KEY (passenger_id) REFERENCES booking_passengers(id)
);

CREATE TABLE train_delay_events (
                                    id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                    train_run_id BIGINT NOT NULL,
                                    delay_minutes INT NOT NULL,
                                    reason TEXT,
                                    notified_customers BOOLEAN NOT NULL DEFAULT FALSE,
                                    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                    CONSTRAINT fk_delay_events_train_run
                                        FOREIGN KEY (train_run_id) REFERENCES train_runs(id),

                                    CONSTRAINT chk_delay_minutes
                                        CHECK (delay_minutes > 0)
);

CREATE TABLE email_outbox (
                              id BIGINT PRIMARY KEY AUTO_INCREMENT,
                              recipient_email VARCHAR(150) NOT NULL,
                              subject VARCHAR(200) NOT NULL,
                              body TEXT NOT NULL,
                              type VARCHAR(50) NOT NULL,
                              status VARCHAR(30) NOT NULL DEFAULT 'SIMULATED',
                              booking_id BIGINT,
                              train_run_id BIGINT,
                              created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              sent_at DATETIME,

                              CONSTRAINT fk_email_outbox_booking
                                  FOREIGN KEY (booking_id) REFERENCES bookings(id),

                              CONSTRAINT fk_email_outbox_train_run
                                  FOREIGN KEY (train_run_id) REFERENCES train_runs(id)
);

CREATE INDEX idx_station_code ON stations(code);
CREATE INDEX idx_train_number ON trains(train_number);
CREATE INDEX idx_train_runs_service_date ON train_runs(service_date);
CREATE INDEX idx_train_run_stops_train_run ON train_run_stops(train_run_id);
CREATE INDEX idx_bookings_code ON bookings(booking_code);
CREATE INDEX idx_booking_legs_train_run ON booking_legs(train_run_id);
CREATE INDEX idx_booking_legs_status ON booking_legs(status);
CREATE INDEX idx_email_outbox_status ON email_outbox(status);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

