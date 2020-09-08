-- S1.1. Geslacht
CREATE TYPE geslacht AS ENUM('M', 'V', 'Onzeker', 'Apache Helicopter');
ALTER TABLE medewerkers ADD COLUMN m_geslacht_chk geslacht;
SELECT * FROM medewerkers;

UPDATE medewerkers SET m_geslacht_chk='V' WHERE naam='SMIT';
-- [22P02] ERROR: invalid input value for enum geslacht: "X"

-- S1.2. Nieuwe afdeling
SELECT * FROM afdelingen;
INSERT INTO medewerkers VALUES (8000, 'DONK', 'A', 'DIRECTEUR', 8000, '1945-09-23', 4000.00, null, 10, 'M');
INSERT INTO afdelingen VALUES (50, 'ONDERZOEK', 'ZWOLLE', 8000);

-- S1.3. Verbetering op afdelingentabel
CREATE SEQUENCE afdelingsnummers
    start with 60
    increment by 10
    minvalue 10;

ALTER TABLE medewerkers ALTER COLUMN afd TYPE numeric(3);

INSERT INTO afdelingen VALUES (nextval('afdelingsnummers'), 'BLAH', 'BUSSUM', 8000);
INSERT INTO afdelingen VALUES (nextval('afdelingsnummers'), 'INKOOP', 'UTRECHT', 8000);
INSERT INTO afdelingen VALUES (nextval('afdelingsnummers'), 'CAFE', 'AMSTERDAM', 8000);

-- S1.4. Adressen
CREATE TABLE adressen (
    postcode char(6),
    huisnummer int,
    ingangsdatum date,
    einddatum date,
    CONSTRAINT check_datum CHECK ( einddatum > ingangsdatum OR einddatum IS NULL),
    telefoon numeric(10),
    med_nmr int not null
);

-- S1.5. Commissie
--
-- De commissie van een medewerker (kolom `comm`) moet een bedrag bevatten als de medewerker een functie als
-- 'VERKOPER' heeft, anders moet de commissie NULL zijn. Schrijf hiervoor een beperkingsregel. Gebruik onderstaande
-- 'illegale' INSERTs om je beperkingsregel te controleren.
ALTER TABLE medewerkers ADD CONSTRAINT comm_verkoper CHECK (comm IS NULL OR functie='VERKOPER');
select * from medewerkers WHERE (comm IS NULL OR functie='VERKOPER');
delete from medewerkers where naam='MULLER';

INSERT INTO medewerkers (mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm)
VALUES (8001, 'MULLER', 'TJ', 'TRAINER', 7566, '1982-08-18', 2000, 500);

INSERT INTO medewerkers (mnr, naam, voorl, functie, chef, gbdatum, maandsal, comm)
VALUES (8002, 'JANSEN', 'M', 'VERKOPER', 7698, '1981-07-17', 1000, NULL);

INSERT INTO adressen VALUES ('1111AA', 11, '1990-09-09', null, 0912312312, 121212)
SELECT * FROM adressen;

-- -------------------------[ HU TESTRAAMWERK ]--------------------------------
-- Met onderstaande query kun je je code testen. Zie bovenaan dit bestand
-- voor uitleg.

SELECT * FROM test_exists('S1.1', 1) AS resultaat
UNION
SELECT * FROM test_exists('S1.2', 1) AS resultaat
UNION
SELECT 'S1.3 wordt niet getest: geen test mogelijk.' AS resultaat
UNION
SELECT * FROM test_exists('S1.4', 6) AS resultaat
UNION
SELECT 'S1.5 wordt niet getest: handmatige test beschikbaar.' AS resultaat
ORDER BY resultaat;


-- Draai alle wijzigingen terug om conflicten in komende opdrachten te voorkomen.
DROP TABLE IF EXISTS adressen;
UPDATE medewerkers SET afd = NULL WHERE mnr < 7369 OR mnr > 7934;
UPDATE afdelingen SET hoofd = NULL WHERE anr > 40;
DELETE FROM afdelingen WHERE anr > 40;
DELETE FROM medewerkers WHERE mnr < 7369 OR mnr > 7934;
ALTER TABLE medewerkers DROP CONSTRAINT IF EXISTS m_geslacht_chk;
ALTER TABLE medewerkers DROP COLUMN IF EXISTS geslacht;