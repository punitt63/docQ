CREATE TABLE care_context (
    opd_date DATE NOT NULL,
    opd_id varchar(40) NOT NULL,
    appointment_id INT NOT NULL,
    is_hip_linked BOOLEAN DEFAULT false,
    is_user_linked BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT care_context_unique UNIQUE (opd_date, opd_id, appointment_id)
) PARTITION BY RANGE (opd_date);

CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = NOW();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_care_context_timestamp
BEFORE UPDATE ON care_context
FOR EACH ROW EXECUTE FUNCTION update_modified_column();

CREATE OR REPLACE FUNCTION create_care_context_partitions(start_year INT, num_years INT)
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
            partition_name := 'care_context_y' || year || 'm' || LPAD(month::TEXT, 2, '0');
            start_date := make_date(year, month, 1);

            -- Calculate the first day of the next month for the end boundary
            IF month = 12 THEN
                end_date := make_date(year + 1, 1, 1);
            ELSE
                end_date := make_date(year, month + 1, 1);
            END IF;

            EXECUTE format(
                'CREATE TABLE %I PARTITION OF care_context
                FOR VALUES FROM (%L) TO (%L)',
                partition_name, start_date, end_date
            );

        END LOOP;
    END LOOP;
END;
$$ LANGUAGE plpgsql;

SELECT create_care_context_partitions(2025, 10);