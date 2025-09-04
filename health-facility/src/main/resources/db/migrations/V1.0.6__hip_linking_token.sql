CREATE TABLE hip_linking_token (
    health_facility_id varchar(20) NOT NULL,
    patient_id varchar(100) NOT NULL,
    last_token_request_appointment_id varchar(100) NOT NULL,
    last_token_request_id varchar(100) NOT NULL,
    last_token text,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT hip_linking_token_pkey PRIMARY KEY (health_facility_id, patient_id)
);

CREATE TRIGGER update_hip_linking_token_timestamp
BEFORE UPDATE ON hip_linking_token
FOR EACH ROW EXECUTE FUNCTION update_modified_column();

CREATE INDEX hip_linking_token_idx ON hip_linking_token (patient_id, last_token_request_id);