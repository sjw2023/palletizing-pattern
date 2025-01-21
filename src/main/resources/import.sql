INSERT INTO wsa_poa.containers (container_id,
                                container_volume,
                                height,
                                length,
                                name,
                                used,
                                width)
VALUES (1,67431541296,
        2383,
        12031,
        'TEST',
        1,
        2352),
       (2,33126117000, 2390, 5898, 'DRY CONTAINER - 20 FEET',1, 2350),
       (3,67577728000, 2390, 12032, 'DRY CONTAINER - 40 FEET', 1,2350),
       (4,76201664000, 2695, 12032, 'DRY CONTAINER - 40 FEET HIGH CUBE', 1,2350),
       (5,85990371264, 2697, 13556, 'DRY CONTAINER - 45 FEET',1, 2352);
INSERT INTO wsa_poa.products (used, height, length, product_id, product_volume, width, name)
VALUES (1, 200, 400, 1, 24000000, 300, 'test');

INSERT INTO wsa_poa.patterns (used, height, length, pattern_id, pattern_volume, width, name)
VALUES (1, 2352, 1, 1, 1, 1, '1');

INSERT INTO wsa_poa.pallets (used, height, length, pallet_id, width, name)
VALUES (1, 150, 1200, 1, 1000, 'test'),
       (1, 150, 1100, 2, 1100, 'test2'),
       (1, 150, 1200, 3, 1100, 'test3');