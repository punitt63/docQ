CREATE TABLE patient (
    id varchar(100),
    abha_no varchar(60),
    abha_address varchar(60),
    name text NOT NULL,
    mobile_no text NOT NULL,
    dob date NOT NULL,
    gender varchar(1) NOT NULL,
    last_hip_link_token_request_id varchar(40),
    last_hip_link_token text,
    CONSTRAINT patient_pkey PRIMARY KEY (id),
    CONSTRAINT patient_mob_name_dob_idx UNIQUE (mobile_no, name, dob)
);