ALTER TABLE conference_measurement_draft ADD automatic BIT NOT NULL DEFAULT 'FALSE';
ALTER TABLE conference_measurement_draft ADD waiting BIT NOT NULL DEFAULT 'FALSE';

ALTER TABLE conference_measurement_draft ADD fev6 DOUBLE PRECISION;
ALTER TABLE conference_measurement_draft ADD fev1_fev6_ratio DOUBLE PRECISION;
ALTER TABLE conference_measurement_draft ADD fef2575 DOUBLE PRECISION;
ALTER TABLE conference_measurement_draft ADD good_test BIT;
ALTER TABLE conference_measurement_draft ADD software_version INT;
