CREATE TABLE care_context (
    appointment_id varchar(100) NOT NULL,
    health_facility_id varchar(20) NOT NULL,
    patient_id varchar(100) NOT NULL,
    is_linked BOOLEAN DEFAULT false,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT care_context_pkey PRIMARY KEY  (appointment_id)
);

CREATE TRIGGER update_care_context_timestamp
BEFORE UPDATE ON care_context
FOR EACH ROW EXECUTE FUNCTION update_modified_column();

CREATE INDEX care_context_linked_idx ON care_context (health_facility_id, patient_id, is_linked);