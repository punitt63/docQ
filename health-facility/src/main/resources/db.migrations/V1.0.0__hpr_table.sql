CREATE TABLE health_professional (
    health_facility_id varchar(20),
    health_professional_id varchar(20),
    type varchar(10)
    CONSTRAINT health_professional_pkey PRIMARY KEY (health_facility_id, health_professional_id)
);