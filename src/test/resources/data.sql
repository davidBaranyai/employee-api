DELETE FROM employees;

INSERT INTO employees
    (id, name, age, role, team, start_date, manager_id)
VALUES
    (1, 'Test Joe', 35, 'MANAGER', 'Dev1', '2025-01-01', NULL),
    (2, 'Test Doe', 35, 'DEVELOPER', 'Dev1', '2025-01-01', 1),
    (3, 'Test Jane', 35, 'DEVELOPER', 'Dev1', '2025-01-01', 1),
    (4, 'Test Joe2', 35, 'MANAGER', 'Dev2', '2025-01-01', NULL);