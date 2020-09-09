-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S3: Multiple Tables
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
--
--
-- Opdracht: schrijf SQL-queries om onderstaande resultaten op te vragen,
-- aan te maken, verwijderen of aan te passen in de database van de
-- bedrijfscasus.
--
-- Codeer je uitwerking onder de regel 'DROP VIEW ...' (bij een SELECT)
-- of boven de regel 'ON CONFLICT DO NOTHING;' (bij een INSERT)
-- Je kunt deze eigen query selecteren en los uitvoeren, en wijzigen tot
-- je tevreden bent.
--
-- Vervolgens kun je je uitwerkingen testen door de testregels
-- (met [TEST] erachter) te activeren (haal hiervoor de commentaartekens
-- weg) en vervolgens het hele bestand uit te voeren. Hiervoor moet je de
-- testsuite in de database hebben geladen (bedrijf_postgresql_test.sql).
-- NB: niet alle opdrachten hebben testregels.
--
-- Lever je werk pas in op Canvas als alle tests slagen.
-- ------------------------------------------------------------------------


-- S3.1.
-- Produceer een overzicht van alle cursusuitvoeringen; geef de
-- cursuscode, de begindatum, de cursuslengte en de naam van de docent.
-- DROP VIEW IF EXISTS s3_1; CREATE OR REPLACE VIEW s3_1 AS                                                     -- [TEST]
select u.cursus, u.begindatum, c.lengte, m.naam from uitvoeringen u
    join medewerkers m on u.docent = m.mnr
    join cursussen c on u.cursus = c.code;

-- S3.2.
-- Geef in twee kolommen naast elkaar de naam van elke cursist (`cursist`)
-- die een S02-cursus heeft gevolgd, met de naam van de docent (`docent`).
-- DROP VIEW IF EXISTS s3_2; CREATE OR REPLACE VIEW s3_2 AS                                                     -- [TEST]
select distinct (m.voorl||' '|| m.naam) AS naam, (d.voorl||' '|| d.naam) AS docent from inschrijvingen i
join medewerkers m on i.cursist = m.mnr
join uitvoeringen u on i.cursus = u.cursus and i.begindatum = u.begindatum
join medewerkers d on u.docent = d.mnr
where i.cursus='S02';


-- S3.3.
-- Geef elke afdeling (`afdeling`) met de naam van het hoofd van die
-- afdeling (`hoofd`).
-- DROP VIEW IF EXISTS s3_3; CREATE OR REPLACE VIEW s3_3 AS                                                     -- [TEST]
select (anr, a.naam, a.locatie) AS afdeling, (m.voorl||' '|| m.naam) AS hoofd
from afdelingen a
join medewerkers m on a.hoofd = m.mnr;


-- opdr van discord
select (m.voorl||' '|| m.naam) AS naam, (a.naam) AS AFDNAAM from medewerkers m
join afdelingen a on m.afd = a.anr;

select (m.voorl||' '|| m.naam) AS naam, (a.naam) AS AFDNAAM from medewerkers m
join afdelingen a on m.afd = a.anr where a.naam='OPLEIDINGEN';

select omschrijving, begindatum
from uitvoeringen u join cursussen c on u.cursus = c.code;

select omschrijving, begindatum
from uitvoeringen u join cursussen c on u.cursus = c.code ORDER BY begindatum;

select cursus, omschrijving, begindatum
from uitvoeringen u join cursussen c on u.cursus = c.code ORDER BY begindatum;

select (m.voorl||' '|| m.naam) AS medewerker, (c.voorl, c.naam) AS chef
from medewerkers m
join medewerkers c on m.chef = c.mnr ORDER BY medewerker;


-- S3.4.
-- Geef de namen van alle medewerkers, de naam van hun afdeling (`afdeling`)
-- en de bijbehorende locatie.
-- DROP VIEW IF EXISTS s3_4; CREATE OR REPLACE VIEW s3_4 AS                                                     -- [TEST]
select (m.voorl ||' '|| m.naam) as naam, a.naam as afdeling, locatie
from medewerkers m
join afdelingen a on m.afd = a.anr;

-- S3.5.
-- Geef de namen van alle cursisten die staan ingeschreven voor de cursus S02 van 12 april 2019
-- DROP VIEW IF EXISTS s3_5; CREATE OR REPLACE VIEW s3_5 AS                                                     -- [TEST]
select (m.voorl ||' '|| m.naam) as naam
from inschrijvingen i
join medewerkers m on i.cursist = m.mnr
where begindatum >= '2019-04-12' and cursus = 'S02';

-- S3.6.
-- Geef de namen van alle medewerkers en hun toelage.
-- DROP VIEW IF EXISTS s3_6; CREATE OR REPLACE VIEW s3_6 AS                                                     -- [TEST]
select (voorl ||' '|| naam) as naam, toelage
from medewerkers
join schalen on (maandsal >= ondergrens and maandsal <= bovengrens);


-- -------------------------[ HU TESTRAAMWERK ]--------------------------------
-- Met onderstaande query kun je je code testen. Zie bovenaan dit bestand
-- voor uitleg.

SELECT * FROM test_select('S3.1') AS resultaat
UNION
SELECT * FROM test_select('S3.2') AS resultaat
UNION
SELECT * FROM test_select('S3.3') AS resultaat
UNION
SELECT * FROM test_select('S3.4') AS resultaat
UNION
SELECT * FROM test_select('S3.5') AS resultaat
UNION
SELECT * FROM test_select('S3.6') AS resultaat
ORDER BY resultaat;
