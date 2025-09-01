CREATE TABLE user_initiated_linking (
    transaction_id varchar(100) NOT NULL,
    patient_id varchar(100) NOT NULL,
    status varchar(40) NOT NULL,
    link_reference_number varchar(100),
    otp varchar(10),
    otp_expiry_time timestamp with time zone,
    created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT user_initiated_linking_pkey PRIMARY KEY (transaction_id)
);

CREATE TRIGGER update_user_initiated_linking_timestamp
BEFORE UPDATE ON user_initiated_linking
FOR EACH ROW EXECUTE FUNCTION update_modified_column();