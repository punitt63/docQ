CREATE TABLE consent_request (
    request_id varchar(100) PRIMARY KEY,
    consent_request_id varchar(100),
    requester_id varchar(100) NOT NULL,
    hiu_id varchar(100) NOT NULL,
    request jsonb NOT NULL,
    status varchar(20),
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT consent_request_unique UNIQUE (consent_request_id)
);

CREATE INDEX hiu_id_requester_idx ON consent_request (hiu_id, requester_id);

CREATE TRIGGER update_consent_request_timestamp
BEFORE UPDATE ON consent_request
FOR EACH ROW EXECUTE FUNCTION update_modified_column();

CREATE TABLE consent_health_record (
    consent_id varchar(100) PRIMARY KEY,
    consent_request_id varchar(100) NOT NULL,
    hiu_id varchar(100) NOT NULL,
    hip_id varchar(100),
    page_no int,
    health_records jsonb,
    status varchar(20) NOT NULL,
    health_data_request_id varchar(100),
    transaction_id varchar(100),
    key_material jsonb,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT consent_unique UNIQUE (consent_id)
);

CREATE INDEX health_data_request_id_idx ON consent_health_record (health_data_request_id);
CREATE INDEX transaction_id_idx ON consent_health_record (transaction_id);

CREATE TRIGGER update_consent_health_record_timestamp
BEFORE UPDATE ON consent_health_record
FOR EACH ROW EXECUTE FUNCTION update_modified_column();
