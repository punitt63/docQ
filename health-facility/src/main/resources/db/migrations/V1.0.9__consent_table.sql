CREATE TABLE consent (
    id varchar(100) PRIMARY KEY,
    content jsonb NOT NULL,
    status varchar(20) NOT NULL,
    created_at timestamp DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp DEFAULT CURRENT_TIMESTAMP
);

CREATE TRIGGER update_consent_timestamp
BEFORE UPDATE ON consent
FOR EACH ROW EXECUTE FUNCTION update_modified_column();

CREATE TABLE health_information_request (
    transaction_id varchar(100) PRIMARY KEY,
    consent_id varchar(100) NOT NULL,
    request jsonb NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TRIGGER update_health_information_requests_timestamp
BEFORE UPDATE ON health_information_request
FOR EACH ROW EXECUTE FUNCTION update_modified_column();