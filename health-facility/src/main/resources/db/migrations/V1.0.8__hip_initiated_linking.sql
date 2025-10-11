CREATE TABLE hip_initiated_linking (
    appointment_id varchar(100) PRIMARY KEY,
    health_facility_id varchar(100) NOT NULL,
    patient_id varchar(100) NOT NULL,
    link_request_id varchar(40),
    is_patient_notified boolean DEFAULT false,
    notify_request_id varchar(40),
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES care_context(appointment_id) ON DELETE CASCADE,
    CONSTRAINT hip_initiated_linking_request_unique UNIQUE (link_request_id)
);

CREATE TRIGGER update_hip_initiated_linking_timestamp
BEFORE UPDATE ON hip_initiated_linking
FOR EACH ROW EXECUTE FUNCTION update_modified_column();