CREATE TABLE country (
    iso3     CHAR(3) PRIMARY KEY,
    name_en  TEXT NOT NULL,
    name_ko  TEXT,
    region   TEXT
);

CREATE TABLE treaty (
    code             TEXT PRIMARY KEY,
    name_en          TEXT NOT NULL,
    name_ko          TEXT,
    adopted_year     INT,
    data_updated_at  DATE
);

CREATE TABLE ratification (
    iso3                            CHAR(3) REFERENCES country(iso3),
    treaty_code                     TEXT    REFERENCES treaty(code),
    status                          TEXT NOT NULL,
    date_signature                  DATE,
    date_ratification               DATE,
    date_individual_communications  DATE,
    date_inquiry_procedure          DATE,
    PRIMARY KEY (iso3, treaty_code)
);
