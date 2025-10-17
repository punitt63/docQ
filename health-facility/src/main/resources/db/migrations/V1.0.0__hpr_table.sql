CREATE TABLE facility_manager (
    health_facility_id varchar(20) NOT NULL,
    facility_manager_id varchar(20) NOT NULL,
    CONSTRAINT facility_manager_pkey PRIMARY KEY (health_facility_id, facility_manager_id)
);

CREATE TABLE doctor (
    health_facility_id varchar(20) NOT NULL,
    doctor_id varchar(20) NOT NULL,
    facility_manager_id varchar(20) NOT NULL,
    CONSTRAINT doctor_pkey PRIMARY KEY (health_facility_id, doctor_id)
);