-- Insert data for containers
INSERT INTO test_database.containers (container_id, container_volume, height, length, name, used, width)
VALUES (1, 67431541296, 2383, 12031, 'TEST', true, 2352),
       (2, 33126117000, 2390, 5898, 'DRY CONTAINER - 20 FEET', true, 2350),
       (3, 67577728000, 2390, 12032, 'DRY CONTAINER - 40 FEET', true, 2350),
       (4, 76201664000, 2695, 12032, 'DRY CONTAINER - 40 FEET HIGH CUBE', true, 2350),
       (5, 85990371264, 2697, 13556, 'DRY CONTAINER - 45 FEET', true, 2352);

-- Insert data for products
INSERT INTO test_database.products (used, height, length, product_id, product_volume, width, name)
VALUES (true, 200, 400, 1, 24000000, 300, 'test');

-- Insert data for patterns
INSERT INTO test_database.patterns (used, height, length, pattern_id, pattern_volume, width, name)
VALUES (true, 2352, 1, 1, 1, 1, '1');

-- Insert data for pallets
INSERT INTO test_database.pallets (used, height, length, pallet_id, width, name)
VALUES (true, 150, 1200, 1, 1000, 'test'),
       (true, 150, 1100, 2, 1100, 'test2'),
       (true, 150, 1200, 3, 1100, 'test3');