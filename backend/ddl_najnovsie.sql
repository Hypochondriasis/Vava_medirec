ds
ds3456789031230123012
Sharing their screen

Matik — 07/04/2025, 23:25
Resp. aj toto by mohla byť funkčná požiadavka.
Image
Matik — 07/04/2025, 23:39
Oki, ešte som tam dal preč tú prvú nefukčnú požiadavku plus som tam ešte pridal nejaké čiarky.
Attachment file type: acrobat
ProjektovýZámer_MediRec.pdf
435.78 KB
Matik — 08/04/2025, 07:32
Okay, teraz, keď už máme ísť náš projekt implementovať, tak by nám asi trebalo vytvoriť GitHub repozitár (poprípade ho linknúť s Jirou), dohodnúť sa na Java verzii (21 alebo 17), aký DBMS budeme používať (resp. aj či to budeme robiť na localhoste alebo našu DB eventuálne zavesíme aj na nejaký hosting), aký build system budeme používať, ako budeme realizovať GUI (napr. JavaFX alebo Swing + FlatLaf - toto čo ukazoval na prednáške), či budeme používať Lombok(resp. toto musíme, lebo je to v tých jeho požiadavkách) a ešte ktorý logger framework budeme používať.
ArRoNNis — 08/04/2025, 21:23
Okej, nahodil som to ako Task do Jiry a je hodení na @ds .
Do komentára som pridal ľudí, s ktorými je potrebné to prediskutovať, čiže najlepšie bude, keď sa porozprávate medzi sebou. 
Termín tasku som dal do 11.4. čiže tento Piatok,lebo si nemyslím, že na to treba viac ako pár dní, a aby sme mohli začať vývoj čo najskôr.
ds — 10/04/2025, 11:16
Okej ja sa priklanam pouzit to co je najjednoduchsie. 
Pre DB by som pouzival Sqlite (este treba JDBC na pripojenie db). 
Pre GUI JavaFx,  JavaFx CSS, FXML a MaterialFX plus k tomu mat scene builder. 
Na logging je dost dobre SLF4J s Logback, lahka je integracia a pouzivanie. 
Build system bude Maven.
a pouzil by som java 17, kedze chceme teoreticky aby bezalo na tych jebutych kompoch v nemocniciach.
sad — 10/04/2025, 11:47
Dal by som postgre, sqllite asi nepodporuje pristup z inych pocitacov
Matik — 10/04/2025, 11:55
Ja by som tiež skôr bol za Postgre. A ten Flatlaf, ktorý spomínal na prednáškach je pre Swing, takže keď budeme používať JavaFX, tak ten Flatlaf asi budeme musieť nechať tak.
ds — 10/04/2025, 12:34
okej tak postgres. a hovoril ze musime pouzivat the flatlaf?
Matik — 10/04/2025, 12:36
Nie, len to ukazoval, že vieme tam importovať themes z IntelliJ, etc... Ale v podmienkach to nie, resp. je tam, že GUI môže byť v JavaFX. 
ds — 10/04/2025, 12:41
tak ono to je prakticky to iste ako ten javaFx a tie ostane co tam su. len to vyzera moderne. a keby pouzijeme swing stratime scene builder co je podla mna fajn vec
ale keby mate viac skusenosit v swing kludne mozme to
ArRoNNis — 12/04/2025, 11:35
No dobre tak začnime s vývojom aplikácie nech to stíhame za necelý mesiac dokončiť.

@ds , pridal som ti v Jire task, dohodni sa dnes alebo najneskôr zajtra s programátormi, kto bude mať ktorú časť tasku za úlohu. Chcem to vidieť rozdelené v Jire tak, že im každému vytvoriš task s popisom čo ma robiť, Deadline im daj do 18.4. Následne si svoj task daj do "Hotovo", nech vidím že si ho dokončil. Čím skôr tým lepšie.

@dodo6104 Tebe som tiež pridal prípravu databázy pre našu aplikáciu, deadline do 18.4.

Ak náhodou ma niekto z programátorov pocit, že za týžden vie spraviť viacej tak nech kludne povie. Lepšie spraviť task navyše teraz ako potom v 12. týždni
sad — 12/04/2025, 14:18
Otazka na programatorov. Chceme si to skor rozdelit tak, ze jeden bude mat napriklad GUI, jeden middleware, jeden logovanie, xml, atd? Alebo to rozdelime po jednotlivych castiach - jeden bude mat celu cast pacienti, jeden vysetrenia, atd?
ds — 13/04/2025, 13:18
Co mame mat vobec hotove do 18.4? Ma byt dovetedy cela aplikacia hotova?
ds — 13/04/2025, 14:00
zatial to mam rozdelene na Používateľské rozhranie a frontendová časť,  Správa pacientov a zdravotných záznamov a Kalendár a administratívne funkcie. Kazde pre jedneho programatora
ArRoNNis — 13/04/2025, 14:23
Nejaká začiatočná časť aplikácíe. Napr. Login screen spolu s Home screenov
Je to v tom Jira tasku na tebe
ds — 13/04/2025, 14:28
aha sorry okej
ds — 13/04/2025, 16:28
okej bude to rozdelene na frontend, backend a integraciu.
sad — 13/04/2025, 16:31
Vies ten backend a integraciu trocha lepsie popisat, ze co konkretne je ktorym myslene? Zatial ten frontend by si zobral Alex
ds — 13/04/2025, 16:33
hej akurat na tom robim
ds — 13/04/2025, 17:03
frontend bude mat:
Inicializovať Maven projekt pre JavaFX (s JavaFX, MaterialFX, FXML)
Vytvoriť Login.fxml pomocou Scene Buildera podľa mockupu
Nastaviť fx pre komponenty a event handler pre prihlasenie ( Pridať fx:id a onAction pre tlačidlá atd.)
Základné CSS štýly pre prihlasovaciu obrazovku
(to iste pre Home.fxml)
Implementovať MaterialFX komponenty do oboch obrazoviek
backend:
Vytvoriť Maven projekt a pom.xml s PostgreSQL, SLF4J, Logback
Zorganizovať package štruktúru napr. (config, controller, model, service, util)
Nastaviť DatabaseConfig.java s JDBC pripojením na PostgreSQL (toto by bolo napr v config)
Nastaviť SLF4J s Logback pre logovanie v aplikácii
Implementovať základné utility pre prácu s databázou
Vytvoriť servisnú triedu pre autentifikáciu používateľov (overenie prihlasovacích údajov, Použiť JwtUtil.java na generovanie tokenu)
a integracia:
Implementovať základné dátové modely (POJO) pre aplikáciu:
        User.java (model používateľa)
        LoginRequest.java (model pre prihlásenie)
        LoginResponse.java (model odpovede po prihlásení)
Vytvoriť kostru hlavných controllerov:
       LoginController.java
       HomeController.java
Implementovať LoginController so všetkými potrebnými @FXML anotáciami pre komponenty (aj pre home)
Vytvoriť metódy na spracovanie prihlasovacích údajov a volanie backend služieb
Zabezpečiť validáciu vstupných polí (napr. nepovolené prázdne meno)
Vytvoriť metódy pre zobrazenie údajov a navigáciu v aplikácii
Zabezpečiť správne presmerovanie medzi obrazovkami 
pridam este vysvetlenia na niektore veci
a este poslem strukturu dokumentov
ArRoNNis — 13/04/2025, 17:19
Prosim ta vytvor to aj v Jire 😭 
ds — 13/04/2025, 17:19
hej hej. staci jebut 3 tasky na front back end a integraciu? 
ArRoNNis — 13/04/2025, 17:21
Kazdemu programatorovi daj jeden task kde mu napises co vsetko treba. Ciže celkovo 3 tasky
ds — 13/04/2025, 17:21
🫡
ds — 13/04/2025, 18:19
okej dava zatial @alex2792002  na front end a este vy dvaja sa rozhodnite kto by chcel kde byt @sad @Matik
sad — 13/04/2025, 18:21
Akoze asi preferujem backend
ds — 13/04/2025, 18:22
aight
Matik — 13/04/2025, 18:32
Okay, tak potom mne ostala integrácia.
Matik — 14/04/2025, 12:35
Btw, nemohli by sme vytvoriť repozitár na GitHube? Asi by to bolo jednoduchšie, ako vytvárať si sami projekty pre svoje časti, lebo teraz sa to dá iba asi takto.
sad — 14/04/2025, 13:11
Ano, jasne. Vytvori admin, ci vytvorim ja?
Matik — 14/04/2025, 13:17
Ako to je v podstate jedno, len by bolo treba, aby sme tam všetci boli pridaní. Tam asi stačí zobrať e-maily z toho excelu čo je na Gite predmetu, resp. ak chce niekto používať iný účet, tak nech svoj e-mail napíše tu.

Len potom by mohol @ds  vytvoriť projekt na svojom stroji a len vytvoriť package structure ako si ju predstavuje, aby tam potom nevznikali nejaké nedorozumenia. 
Matik — 16/04/2025, 11:23
Neviem, @ds už by trebalo ten git spraviť, lebo už je front-end in-progress.
ds — 16/04/2025, 15:41
jop idem na to dnes by to malo byt hotove
ds — 16/04/2025, 17:20
mam github repo urobene a potrebujem vase github mena
lebo v jira potrebujes byt admin co niesom
ChilledDaniel — 16/04/2025, 17:34
ruzsikdaniel
dodo6104 — 16/04/2025, 17:35
dodo6104
Hypno — 16/04/2025, 17:35
AlexejPutiska
ArRoNNis — 16/04/2025, 18:49
Dal som ti nejake admin prava tak skus pozriet ci ich niekde nemas prijat lebo u mna to pise ,,invited”
Matik — 16/04/2025, 18:53
Matik080
alex2792002 — 16/04/2025, 20:49
xovsonka
sad — 17/04/2025, 14:30
FilipChromek
ArRoNNis — 17/04/2025, 15:04
Ak budete mať niečo hotové alebo chcete skôr niečo otestovať, tak máme IT testerov, ktorý vám určite pomôžu. Keď už ich máme v tíme tak prečo to nevyužiť. Je to ale na vás programatoroch
ChilledDaniel — 17/04/2025, 15:15
presne, neváhajte písať keby niečo
ArRoNNis — Yesterday at 12:19
Ako sa vám darí ? @alex2792002 @Matik @sad 

Tento týžden budem riešit plánovanie ďalších táskov lebo DEADLINE na projekt je už o 2 týždne ! Tak nech všetko stíhame.
Matik — 22:02
Okay, máme tam viacero branches na GitHube. Ak by ste mohli, tak by bolo fajn buď rovno pushovať do integration alebo mergenúť integration s main.

Plus info pre testerov. V tej integration branchi som nechal db.properties súbor v src/main/resources/org/medirec/backend. Keď budete testovať, tak si v tom súbore upravte tie db info podľa toho ako máte db/dbms nastavené na svojich strojoch. 
dodo6104 — 22:22
neviem ci ta uplne chapem, ale db je verejna, takze nemusi si ju kazdy setupovat len sa pripojit
postgresql.r6.websupport.sk
5432
vavadb

User-info
databazaVava
databazaVava123+
Matik — 22:27
Jaj, ja som to robil na localhoste, to preto som spomínal to nastavovanie. Ak budeme používať hostovanú db, tak sa nastavia tie db.properties na tie hostované údaje. Ale pre účely testovania by asi bolo tiež dobré bežať na localhoste, aby sme niečo na hostingu náhodou nerozbili.
dodo6104 — 22:27
No okej...ja len tak pre istotu
ds — 22:31
spojil som celu aplikaciu do main staci stiahnut a spustit cez maven. Len nedostanete sa daleko z login lebo chyba databaza a nikde som ju nenasiel 
Matik — 22:34
V integration branchi je .sql file. Len tam nie sú inserty. Ja som to skúšal na tomto (len som tam pridal inserty, heslá nie sú zahashované. Tam mám ešte TODO).
-- This script was generated by the ERD tool in pgAdmin 4.
-- Please log an issue at https://github.com/pgadmin-org/pgadmin4/issues/new/choose if you find any bugs, including reproduction steps.
BEGIN;

DROP TABLE IF EXISTS public."Appoitment" cascade;
CREATE TABLE IF NOT EXISTS public."Appointment"
    Expand
    Medirec_ddl.sql
    13 KB
    ds — 22:35
jop je to tam len chyba db.properties. DatabaseConfig ju nevie najst, keby sa to tam da pridat tak by bolo super
dodo6104 — 22:37
patient nie je rola, daj tam sestricka 
-- 3. Role
INSERT INTO public."Role"(id, name) VALUES
  (1, 'Admin'),
  (2, 'Doctor'),
  (3, 'Patient')
ON CONFLICT DO NOTHING;
Matik — 22:37
Je tam ten súbor, len cesta je zlá lebo sa mi zdá, že si to len mergeol dohromady, tak potom tá cesta nesedí.
-- This script was generated by the ERD tool in pgAdmin 4.
-- Please log an issue at https://github.com/pgadmin-org/pgadmin4/issues/new/choose if you find any bugs, including reproduction steps.
BEGIN;

DROP TABLE IF EXISTS public."Appoitment" cascade;
CREATE TABLE IF NOT EXISTS public."Appointment"
    Expand
    Medirec_ddl.sql
    13 KB
    ds — 22:38
jaj hej sorry
    ﻿
-- This script was generated by the ERD tool in pgAdmin 4.
-- Please log an issue at https://github.com/pgadmin-org/pgadmin4/issues/new/choose if you find any bugs, including reproduction steps.
BEGIN;

DROP TABLE IF EXISTS public."Appoitment" cascade;
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

DROP TABLE IF EXISTS public."Appoitment_Status" cascade;
CREATE TABLE IF NOT EXISTS public."Appointment_Status"
(
    id smallserial NOT NULL,
    name character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Appointment_Status_pkey" PRIMARY KEY (id),
    CONSTRAINT "Appointment_Status_name_key" UNIQUE (name)
    );

DROP TABLE IF EXISTS public."Diagnosis" cascade;
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

DROP TABLE IF EXISTS public."Diagnosis_type" cascade;
CREATE TABLE IF NOT EXISTS public."Diagnosis_type"
(
    id smallserial NOT NULL,
    name character varying(30) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Diagnosis_type_pkey" PRIMARY KEY (id)
    );

DROP TABLE IF EXISTS public."Doctor" cascade;
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

DROP TABLE IF EXISTS public."MedicalRecord" cascade;
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

DROP TABLE IF EXISTS public."Medication" cascade;
CREATE TABLE IF NOT EXISTS public."Medication"
(
    id bigserial NOT NULL,
    code character varying(10) COLLATE pg_catalog."default" NOT NULL,
    name text COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Medication_pkey" PRIMARY KEY (id),
    CONSTRAINT "Medication_code_key" UNIQUE (code),
    CONSTRAINT "Medication_name_key" UNIQUE (name)
    );

DROP TABLE IF EXISTS public."Patient" cascade;
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

DROP TABLE IF EXISTS public."Prescription" cascade;
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

DROP TABLE IF EXISTS public."RecordDiagnosis" cascade;
CREATE TABLE IF NOT EXISTS public."RecordDiagnosis"
(
    id bigserial NOT NULL,
    medical_record_id bigint NOT NULL,
    diagnosis_id bigint NOT NULL,
    CONSTRAINT "RecordDiagnosis_pkey" PRIMARY KEY (id)
    );

DROP TABLE IF EXISTS public."Role" cascade;
CREATE TABLE IF NOT EXISTS public."Role"
(
    id smallserial NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT "Role_pkey" PRIMARY KEY (id),
    CONSTRAINT "Role_name_key" UNIQUE (name)
    );

DROP TABLE IF EXISTS public."Token" cascade;
CREATE TABLE IF NOT EXISTS public."Token"
(
    id bigserial NOT NULL,
    user_id bigint NOT NULL,
    token character varying(50) COLLATE pg_catalog."default" NOT NULL,
    validity timestamp with time zone NOT NULL DEFAULT (now() + '1 mon'::interval),
    CONSTRAINT "Token_pkey" PRIMARY KEY (id)
    );

DROP TABLE IF EXISTS public."User" cascade;
CREATE TABLE IF NOT EXISTS public."User"
(
    id bigserial NOT NULL,
    email character varying(255) COLLATE pg_catalog."default" NOT NULL,
    password_hash character varying(50) COLLATE pg_catalog."default" NOT NULL,
    role_id integer NOT NULL,
    doctor_id integer NOT NULL,
    CONSTRAINT "User_pkey" PRIMARY KEY (id),
    CONSTRAINT "User_email_key" UNIQUE (email)
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

--	INSERTS

-- 1. Appointment_Status
INSERT INTO public."Appointment_Status"(id, name) VALUES
                                                      (1, 'Scheduled'),
                                                      (2, 'Completed'),
                                                      (3, 'Cancelled')
    ON CONFLICT DO NOTHING;

-- 2. Diagnosis_type
INSERT INTO public."Diagnosis_type"(id, name) VALUES
                                                  (1, 'Chronic'),
                                                  (2, 'Acute'),
                                                  (3, 'Infectious'),
                                                  (4, 'Genetic')
    ON CONFLICT DO NOTHING;

-- 3. Role
INSERT INTO public."Role"(id, name) VALUES
                                        (1, 'Admin'),
                                        (2, 'Doctor'),
                                        (3, 'Nurse')
    ON CONFLICT DO NOTHING;

-- 4. Doctor
INSERT INTO public."Doctor"(id, first_name, last_name, specialization, code) VALUES
                                                                                 (1, 'Alice', 'Smith', 'Cardiology',  'DOC001'),
                                                                                 (2, 'Bob',   'Jones', 'Neurology',   'DOC002'),
                                                                                 (3, 'Clara', 'Davis', 'Dermatology', 'DOC003')
    ON CONFLICT DO NOTHING;

-- 5. Patient
INSERT INTO public."Patient"(
    id, first_name, last_name, birth_date, personal_id_number, gender,
    email, phone_number, birth_city, permanent_city, street, postal_code,
    insurance_number, blood_pressure, weight, height, "BMI", doctor_id
) VALUES
      (1, 'Eve',   'Adams', '1975-02-14', 'PID1001', 'F',
       'eve.adams@example.com', '+421900000001', 'Bratislava', 'Bratislava',
       'Main St 1', '81101', 'INS1001', '120/80', 68.0, 1.65, 24.98, 1),
      (2, 'Frank', 'Brown', '1982-07-30', 'PID1002', 'M',
       'frank.brown@example.com', '+421900000002', 'Košice',    'Košice',
       '2nd Ave 5', '04001', 'INS1002', '130/85', 82.0, 1.80, 25.32, 2)
    ON CONFLICT DO NOTHING;

-- 6. "User"
INSERT INTO public."User"(id, email, password_hash, role_id, doctor_id) VALUES
                                                                            (1, 'admin@example.com',  'hash_admin', 1, 1),
                                                                            (2, 'alice.smith@doc.com','hash_doc1',   2, 1),
                                                                            (3, 'eve.adams@pat.com',  'hash_pat1',   3, 1)
    ON CONFLICT DO NOTHING;

-- 8. Medication
INSERT INTO public."Medication"(id, code, name) VALUES
                                                    (1, 'MED001', 'Aspirin'),
                                                    (2, 'MED002', 'Ibuprofen'),
                                                    (3, 'MED003', 'Paracetamol')
    ON CONFLICT DO NOTHING;

-- 9. Diagnosis
INSERT INTO public."Diagnosis"(id, code, name, diagnosis_type_id) VALUES
                                                                      (1, 'HYP01', 'Hypertension', 1),
                                                                      (2, 'FLU01', 'Influenza',    3),
                                                                      (3, 'ECZ01', 'Eczema',       2)
    ON CONFLICT DO NOTHING;

-- 10. MedicalRecord
INSERT INTO public."MedicalRecord"(id, patient_id, doctor_id, notes) VALUES
                                                                         (1, 1, 1, 'Routine annual check—normal.'),
                                                                         (2, 2, 2, 'Complaints of migraine—MRI scheduled.')
    ON CONFLICT DO NOTHING;

-- 11. Prescription
INSERT INTO public."Prescription"(id, medical_record_id, medication_id, dosage, frequency, duration) VALUES
                                                                                                         (1, 1, 1, '100mg', 'Once daily',  '7 days'),
                                                                                                         (2, 2, 2, '200mg', 'Twice daily', '5 days')
    ON CONFLICT DO NOTHING;

-- 12. RecordDiagnosis
INSERT INTO public."RecordDiagnosis"(id, medical_record_id, diagnosis_id) VALUES
                                                                              (1, 1, 1),
                                                                              (2, 2, 2)
    ON CONFLICT DO NOTHING;

-- 13. Appointment
INSERT INTO public."Appointment"(id, patient_id, doctor_id, appointment_date, reason, appointment_status_id) VALUES
                                                                                                                 (1, 1, 1, '2025-05-01', 'Annual check-up',    1),
                                                                                                                 (2, 2, 2, '2025-05-02', 'Follow-up migraine', 2),
                                                                                                                 (3, 1, 3, '2025-05-03', 'Skin irritation',     1)
    ON CONFLICT DO NOTHING;
END;

SELECT id FROM public."User" WHERE email = 'admin@example.com' AND password_hash = 'hash_admin'