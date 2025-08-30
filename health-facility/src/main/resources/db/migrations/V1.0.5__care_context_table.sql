CREATE TABLE care_context (
    appointment_id varchar(100) NOT NULL,
    health_facility_id varchar(20) NOT NULL,
    patient_id varchar(100) NOT NULL,
    link_request_id varchar(40),
    is_linked BOOLEAN DEFAULT false,
    is_patient_notified BOOLEAN DEFAULT false,
    notify_request_id varchar(40),
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT care_context_pkey PRIMARY KEY  (appointment_id),
    CONSTRAINT care_context_request_unique UNIQUE (link_request_id),
    CONSTRAINT notify_request_unique UNIQUE (notify_request_id)
);

CREATE INDEX care_context_linked_idx ON care_context (health_facility_id, patient_id, is_linked);