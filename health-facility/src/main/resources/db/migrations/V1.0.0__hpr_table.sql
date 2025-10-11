CREATE TABLE health_professional (
    health_professional_id varchar(20),
    health_professional_name text NOT NULL,
    type varchar(20),
    speciality VARCHAR(20),
    health_facility_id varchar(20),
    health_facility_name text NOT NULL,
    state_code INT not null,
    district_code INT not null,
    address text,
    pincode varchar(10),
    latitude double precision,
    longitude double precision,
    CONSTRAINT health_professional_pkey PRIMARY KEY (health_facility_id, health_professional_id)
);