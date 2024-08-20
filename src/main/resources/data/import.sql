INSERT INTO wsa_poa.containers (container_id,
                                container_volume,
                                height,
                                length,
                                name,
                                used,
                                width)
VALUES (1,
        400,
        400,
        400,
        'TEST',
        true,
        400);
INSERT INTO wsa_poa.products (used, height, length, product_id, product_volume, width, name)
VALUES (true, 200, 400, 1, 24000000, 300, 'test');

INSERT INTO wsa_poa.patterns (used, height, length, pattern_id, pattern_volume, width, name)
VALUES (true, 1650, 1, 1, 1, 1, '1');

INSERT INTO wsa_poa.pallets (used, height, length, pallet_id, width, name)
VALUES (true, 150, 1200, 1, 1000, 'test');