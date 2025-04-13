CREATE TABLE opd (
    id varchar(40) NOT NULL,
    health_facility_id varchar(20) NOT NULL,
    health_professional_id varchar(20) NOT NULL,
    name VARCHAR(255) NOT NULL,
    start_hour INTEGER NOT NULL DEFAULT 0,
    end_hour INTEGER NOT NULL DEFAULT 0,
    start_minute INTEGER NOT NULL DEFAULT 0,
    end_minute INTEGER NOT NULL DEFAULT 0,
    opd_date DATE NOT NULL,
    max_slots INTEGER NOT NULL,
    minutes_per_slot INTEGER NOT NULL,
    activate_time TIMESTAMP WITH TIME ZONE NOT NULL,
    state VARCHAR(20) NOT NULL,
    actual_start_time TIMESTAMP WITH TIME ZONE,
    actual_end_time TIMESTAMP WITH TIME ZONE,
    appointments_count INT DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT opd_pkey PRIMARY KEY (opd_date, id),
    CONSTRAINT opd_unique UNIQUE (health_facility_id, health_professional_id, opd_date, start_hour)
) PARTITION BY RANGE (opd_date);

CREATE OR REPLACE FUNCTION update_opd()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.appointments_count > NEW.max_slots THEN
        RAISE EXCEPTION 'APPOINTMENTS_COUNT_EXCEEDED';
    END IF;
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_opd_trigger
AFTER UPDATE ON opd
FOR EACH ROW EXECUTE FUNCTION update_opd();

CREATE OR REPLACE FUNCTION create_opd_partitions(start_year INT, num_years INT)
RETURNS void AS $$
DECLARE
    year INT;
    month INT;
    partition_name TEXT;
    start_date DATE;
    end_date DATE;
BEGIN
    FOR year IN start_year..(start_year + num_years - 1) LOOP
        FOR month IN 1..12 LOOP
            partition_name := 'opd_y' || year || 'm' || LPAD(month::TEXT, 2, '0');
            start_date := make_date(year, month, 1);

            -- Calculate the first day of the next month for the end boundary
            IF month = 12 THEN
                end_date := make_date(year + 1, 1, 1);
            ELSE
                end_date := make_date(year, month + 1, 1);
            END IF;

            EXECUTE format(
                'CREATE TABLE %I PARTITION OF opd
                FOR VALUES FROM (%L) TO (%L)',
                partition_name, start_date, end_date
            );

        END LOOP;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

SELECT create_opd_partitions(2025, 10);