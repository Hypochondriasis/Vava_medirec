-- This script was generated by the ERD tool in pgAdmin 4.
-- Please log an issue at https://github.com/pgadmin-org/pgadmin4/issues/new/choose if you find any bugs, including reproduction steps.
BEGIN;


CREATE TABLE IF NOT EXISTS public."Appointment"
(
    id bigserial NOT NULL,
    patient_id bigint NOT NULL,
    doctor_id integer NOT NULL,
    appointment_date date NOT NULL,
    reason text COLLATE pg_catalog."default",
    appointment_status_id smallint NOT NULL,
    CONSTRAINT "Appointment_pkey" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public."Appointment_Status"
(
    id smallserial NOT NULL,
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Appointment_Status_pkey" PRIMARY KEY (id),
    CONSTRAINT "Appointment_Status_name_key" UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS public."Diagnosis"
(
    id bigserial NOT NULL,
    code character varying(10) COLLATE pg_catalog."default" NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    diagnosis_type_id integer NOT NULL,
    CONSTRAINT "Diagnosis_pkey" PRIMARY KEY (id),
    CONSTRAINT "Diagnosis_code_key" UNIQUE (code),
    CONSTRAINT "Diagnosis_name_key" UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS public."Diagnosis_type"
(
    id smallserial NOT NULL,
    name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Diagnosis_type_pkey" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public."Doctor"
(
    id serial NOT NULL,
    first_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    specialization character varying(100) COLLATE pg_catalog."default" NOT NULL,
    code character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Doctor_pkey" PRIMARY KEY (id),
    CONSTRAINT "Doctor_code_key" UNIQUE (code)
);

CREATE TABLE IF NOT EXISTS public."MedicalRecord"
(
    id bigserial NOT NULL,
    patient_id bigint NOT NULL,
    doctor_id integer NOT NULL,
    notes text COLLATE pg_catalog."default",
    created_at time with time zone NOT NULL DEFAULT now(),
    updated_at time with time zone NOT NULL DEFAULT now(),
    CONSTRAINT "MedicalRecord_pkey" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public."Medication"
(
    id bigserial NOT NULL,
    code character varying(10) COLLATE pg_catalog."default" NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Medication_pkey" PRIMARY KEY (id),
    CONSTRAINT "Medication_code_key" UNIQUE (code),
    CONSTRAINT "Medication_name_key" UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS public."Patient"
(
    id bigserial NOT NULL,
    first_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    last_name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    birth_date date NOT NULL,
    personal_id_number character varying(10) COLLATE pg_catalog."default" NOT NULL,
    gender character(1) COLLATE pg_catalog."default" NOT NULL,
    email character varying(255) COLLATE pg_catalog."default",
    phone_number character varying(20) COLLATE pg_catalog."default",
    birth_city character varying(100) COLLATE pg_catalog."default" NOT NULL,
    permanent_city character varying(100) COLLATE pg_catalog."default" NOT NULL,
    street text COLLATE pg_catalog."default" NOT NULL,
    postal_code character varying(5) COLLATE pg_catalog."default" NOT NULL,
    insurance_number character varying(50) COLLATE pg_catalog."default" NOT NULL,
    created_at timestamp with time zone NOT NULL DEFAULT now(),
    updated_at time with time zone NOT NULL DEFAULT now(),
    blood_pressure character varying(10) COLLATE pg_catalog."default",
    weight real,
    height real,
    "BMI" real,
    doctor_id integer NOT NULL,
    CONSTRAINT "Patient_pkey" PRIMARY KEY (id),
    CONSTRAINT "Patient_email_key" UNIQUE (email),
    CONSTRAINT "Patient_personal_id_number_key" UNIQUE (personal_id_number),
    CONSTRAINT "Patient_phone_number_key" UNIQUE (phone_number)
);

CREATE TABLE IF NOT EXISTS public."Prescription"
(
    id bigserial NOT NULL,
    medical_record_id bigint NOT NULL,
    medication_id bigint NOT NULL,
    dosage character varying(100) COLLATE pg_catalog."default" NOT NULL,
    frequency character varying(100) COLLATE pg_catalog."default" NOT NULL,
    duration character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Prescription_pkey" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public."RecordDiagnosis"
(
    id bigserial NOT NULL,
    medical_record_id bigint NOT NULL,
    diagnosis_id bigint NOT NULL,
    CONSTRAINT "RecordDiagnosis_pkey" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public."Role"
(
    id smallserial NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Role_pkey" PRIMARY KEY (id),
    CONSTRAINT "Role_name_key" UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS public."Token"
(
    id bigserial NOT NULL,
    user_id bigint NOT NULL,
    token character varying(50) COLLATE pg_catalog."default" NOT NULL,
    validity timestamp with time zone NOT NULL DEFAULT (now() + '1 mon'::interval),
    CONSTRAINT "Token_pkey" PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public."User"
(
    id bigserial NOT NULL,
    emal character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password_hash character varying(50) COLLATE pg_catalog."default" NOT NULL,
    role_id integer NOT NULL,
    doctor_id integer NOT NULL,
    CONSTRAINT "User_pkey" PRIMARY KEY (id),
    CONSTRAINT "User_emal_key" UNIQUE (emal)
);

ALTER TABLE IF EXISTS public."Appointment"
    ADD CONSTRAINT "Appointment_appointment_status_id_fkey" FOREIGN KEY (appointment_status_id)
    REFERENCES public."Appointment_Status" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."Appointment"
    ADD CONSTRAINT "Appointment_doctor_id_fkey" FOREIGN KEY (doctor_id)
    REFERENCES public."Doctor" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."Appointment"
    ADD CONSTRAINT "Appointment_patient_id_fkey" FOREIGN KEY (patient_id)
    REFERENCES public."Patient" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."Diagnosis"
    ADD CONSTRAINT "Diagnosis_diagnosis_type_id_fkey" FOREIGN KEY (diagnosis_type_id)
    REFERENCES public."Diagnosis_type" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."MedicalRecord"
    ADD CONSTRAINT "MedicalRecord_doctor_id_fkey" FOREIGN KEY (doctor_id)
    REFERENCES public."Doctor" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."MedicalRecord"
    ADD CONSTRAINT "MedicalRecord_patient_id_fkey" FOREIGN KEY (patient_id)
    REFERENCES public."Patient" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."Patient"
    ADD CONSTRAINT fk_doctor FOREIGN KEY (doctor_id)
    REFERENCES public."Doctor" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."Prescription"
    ADD CONSTRAINT "Prescription_medical_record_id_fkey" FOREIGN KEY (medical_record_id)
    REFERENCES public."MedicalRecord" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."Prescription"
    ADD CONSTRAINT "Prescription_medication_id_fkey" FOREIGN KEY (medication_id)
    REFERENCES public."Medication" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."RecordDiagnosis"
    ADD CONSTRAINT "RecordDiagnosis_diagnosis_id_fkey" FOREIGN KEY (diagnosis_id)
    REFERENCES public."Diagnosis" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."RecordDiagnosis"
    ADD CONSTRAINT "RecordDiagnosis_medical_record_id_fkey" FOREIGN KEY (medical_record_id)
    REFERENCES public."MedicalRecord" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."Token"
    ADD CONSTRAINT "Token_user_id_fkey" FOREIGN KEY (user_id)
    REFERENCES public."User" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."User"
    ADD CONSTRAINT "User_doctor_id_fkey" FOREIGN KEY (doctor_id)
    REFERENCES public."Doctor" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public."User"
    ADD CONSTRAINT "User_role_id_fkey" FOREIGN KEY (role_id)
    REFERENCES public."Role" (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

END;