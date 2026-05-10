USE train_booking;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE email_outbox;
TRUNCATE TABLE train_delay_events;
TRUNCATE TABLE tickets;
TRUNCATE TABLE booking_legs;
TRUNCATE TABLE booking_passengers;
TRUNCATE TABLE bookings;
TRUNCATE TABLE customers;
TRUNCATE TABLE train_run_stops;
TRUNCATE TABLE train_runs;
TRUNCATE TABLE route_stops;
TRUNCATE TABLE routes;
TRUNCATE TABLE trains;
TRUNCATE TABLE stations;

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO stations (code, name, city, active) VALUES
                                                    ('CLJ', 'Cluj-Napoca', 'Cluj-Napoca', TRUE),
                                                    ('ALB', 'Alba Iulia', 'Alba Iulia', TRUE),
                                                    ('SB', 'Sibiu', 'Sibiu', TRUE),
                                                    ('BV', 'Brașov', 'Brașov', TRUE),
                                                    ('PLO', 'Ploiești Vest', 'Ploiești', TRUE),
                                                    ('BUC', 'București Nord', 'București', TRUE),
                                                    ('TM', 'Timișoara Nord', 'Timișoara', TRUE),
                                                    ('AR', 'Arad', 'Arad', TRUE);

INSERT INTO trains (train_number, name, capacity, active) VALUES
                                                              ('IR1745', 'InterRegio Cluj - București', 120, TRUE),
                                                              ('IR1834', 'InterRegio Cluj - Brașov', 80, TRUE),
                                                              ('IR1622', 'InterRegio Brașov - București', 100, TRUE),
                                                              ('R3001', 'Regio Cluj - Alba Iulia', 40, TRUE),
                                                              ('IR1550', 'InterRegio Timișoara - Cluj', 90, TRUE);

INSERT INTO routes (code, name, active) VALUES
                                            ('R-CLJ-BUC', 'Cluj-Napoca - București Nord', TRUE),
                                            ('R-CLJ-BV', 'Cluj-Napoca - Brașov', TRUE),
                                            ('R-BV-BUC', 'Brașov - București Nord', TRUE),
                                            ('R-CLJ-ALB', 'Cluj-Napoca - Alba Iulia', TRUE),
                                            ('R-TM-CLJ', 'Timișoara Nord - Cluj-Napoca', TRUE);

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 1, 0
FROM routes r, stations s
WHERE r.code = 'R-CLJ-BUC' AND s.code = 'CLJ';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 2, 100
FROM routes r, stations s
WHERE r.code = 'R-CLJ-BUC' AND s.code = 'ALB';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 3, 170
FROM routes r, stations s
WHERE r.code = 'R-CLJ-BUC' AND s.code = 'SB';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 4, 310
FROM routes r, stations s
WHERE r.code = 'R-CLJ-BUC' AND s.code = 'BV';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 5, 480
FROM routes r, stations s
WHERE r.code = 'R-CLJ-BUC' AND s.code = 'BUC';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 1, 0
FROM routes r, stations s
WHERE r.code = 'R-CLJ-BV' AND s.code = 'CLJ';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 2, 100
FROM routes r, stations s
WHERE r.code = 'R-CLJ-BV' AND s.code = 'ALB';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 3, 170
FROM routes r, stations s
WHERE r.code = 'R-CLJ-BV' AND s.code = 'SB';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 4, 310
FROM routes r, stations s
WHERE r.code = 'R-CLJ-BV' AND s.code = 'BV';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 1, 0
FROM routes r, stations s
WHERE r.code = 'R-BV-BUC' AND s.code = 'BV';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 2, 110
FROM routes r, stations s
WHERE r.code = 'R-BV-BUC' AND s.code = 'PLO';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 3, 170
FROM routes r, stations s
WHERE r.code = 'R-BV-BUC' AND s.code = 'BUC';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 1, 0
FROM routes r, stations s
WHERE r.code = 'R-CLJ-ALB' AND s.code = 'CLJ';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 2, 100
FROM routes r, stations s
WHERE r.code = 'R-CLJ-ALB' AND s.code = 'ALB';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 1, 0
FROM routes r, stations s
WHERE r.code = 'R-TM-CLJ' AND s.code = 'TM';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 2, 55
FROM routes r, stations s
WHERE r.code = 'R-TM-CLJ' AND s.code = 'AR';

INSERT INTO route_stops (route_id, station_id, stop_order, distance_from_start_km)
SELECT r.id, s.id, 3, 330
FROM routes r, stations s
WHERE r.code = 'R-TM-CLJ' AND s.code = 'CLJ';

INSERT INTO train_runs (train_id, route_id, run_code, service_date, status, delay_minutes)
SELECT t.id, r.id, 'RUN-IR1745-2026-05-10', '2026-05-10', 'SCHEDULED', 0
FROM trains t, routes r
WHERE t.train_number = 'IR1745' AND r.code = 'R-CLJ-BUC';

INSERT INTO train_runs (train_id, route_id, run_code, service_date, status, delay_minutes)
SELECT t.id, r.id, 'RUN-IR1834-2026-05-10', '2026-05-10', 'SCHEDULED', 0
FROM trains t, routes r
WHERE t.train_number = 'IR1834' AND r.code = 'R-CLJ-BV';

INSERT INTO train_runs (train_id, route_id, run_code, service_date, status, delay_minutes)
SELECT t.id, r.id, 'RUN-IR1622-2026-05-10', '2026-05-10', 'SCHEDULED', 0
FROM trains t, routes r
WHERE t.train_number = 'IR1622' AND r.code = 'R-BV-BUC';

INSERT INTO train_runs (train_id, route_id, run_code, service_date, status, delay_minutes)
SELECT t.id, r.id, 'RUN-R3001-2026-05-10', '2026-05-10', 'SCHEDULED', 0
FROM trains t, routes r
WHERE t.train_number = 'R3001' AND r.code = 'R-CLJ-ALB';

INSERT INTO train_runs (train_id, route_id, run_code, service_date, status, delay_minutes)
SELECT t.id, r.id, 'RUN-IR1550-2026-05-10', '2026-05-10', 'SCHEDULED', 0
FROM trains t, routes r
WHERE t.train_number = 'IR1550' AND r.code = 'R-TM-CLJ';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 1, NULL, '2026-05-10 08:30:00'
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1745-2026-05-10' AND s.code = 'CLJ';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 2, '2026-05-10 10:00:00', '2026-05-10 10:05:00'
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1745-2026-05-10' AND s.code = 'ALB';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 3, '2026-05-10 11:25:00', '2026-05-10 11:35:00'
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1745-2026-05-10' AND s.code = 'SB';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 4, '2026-05-10 14:10:00', '2026-05-10 14:20:00'
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1745-2026-05-10' AND s.code = 'BV';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 5, '2026-05-10 17:10:00', NULL
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1745-2026-05-10' AND s.code = 'BUC';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 1, NULL, '2026-05-10 09:00:00'
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1834-2026-05-10' AND s.code = 'CLJ';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 2, '2026-05-10 10:30:00', '2026-05-10 10:35:00'
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1834-2026-05-10' AND s.code = 'ALB';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 3, '2026-05-10 12:00:00', '2026-05-10 12:10:00'
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1834-2026-05-10' AND s.code = 'SB';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 4, '2026-05-10 14:30:00', NULL
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1834-2026-05-10' AND s.code = 'BV';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 1, NULL, '2026-05-10 15:10:00'
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1622-2026-05-10' AND s.code = 'BV';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 2, '2026-05-10 16:40:00', '2026-05-10 16:45:00'
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1622-2026-05-10' AND s.code = 'PLO';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 3, '2026-05-10 18:00:00', NULL
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1622-2026-05-10' AND s.code = 'BUC';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 1, NULL, '2026-05-10 07:15:00'
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-R3001-2026-05-10' AND s.code = 'CLJ';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 2, '2026-05-10 09:00:00', NULL
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-R3001-2026-05-10' AND s.code = 'ALB';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 1, NULL, '2026-05-10 06:00:00'
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1550-2026-05-10' AND s.code = 'TM';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 2, '2026-05-10 07:00:00', '2026-05-10 07:05:00'
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1550-2026-05-10' AND s.code = 'AR';

INSERT INTO train_run_stops (train_run_id, station_id, stop_order, planned_arrival, planned_departure)
SELECT tr.id, s.id, 3, '2026-05-10 11:30:00', NULL
FROM train_runs tr, stations s
WHERE tr.run_code = 'RUN-IR1550-2026-05-10' AND s.code = 'CLJ';

SELECT 'Seed completed successfully' AS message;