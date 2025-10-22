CREATE TABLE facility_manager (
    health_facility_id varchar(20) NOT NULL,
    facility_manager_id varchar(20) NOT NULL,
    CONSTRAINT facility_manager_pkey PRIMARY KEY (health_facility_id, facility_manager_id)
);

CREATE TABLE doctor (
    health_facility_id varchar(20) NOT NULL,
    id varchar(20) NOT NULL,
    facility_manager_id varchar(20) NOT NULL,
    name varchar(255) NOT NULL,
    facility_name varchar(255) NOT NULL,
    facility_state_code integer NOT NULL,
    facility_district_code integer NOT NULL,
    facility_address text NOT NULL,
    facility_pincode varchar(10) NOT NULL,
    facility_latitude double precision NOT NULL,
    facility_longitude double precision NOT NULL,
    speciality varchar(255),
    CONSTRAINT doctor_pkey PRIMARY KEY (health_facility_id, id)
);