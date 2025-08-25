CREATE TABLE hip_linking_token (
    health_facility_id varchar(20) NOT NULL,
    patient_id varchar(100) NOT NULL,
    last_token_request_appointment_id varchar(100) NOT NULL,
    last_token_request_id varchar(100) NOT NULL,
    last_token text,
    CONSTRAINT hip_linking_token_pkey PRIMARY KEY (health_facility_id, patient_id)
);

CREATE INDEX hip_linking_token_idx ON hip_linking_token (patient_id, last_token_request_id);