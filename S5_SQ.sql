-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S5: Subqueries
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

-- Vervolgens kun je je uitwerkingen testen door de testregels
-- (met [TEST] erachter) te activeren (haal hiervoor de commentaartekens
-- weg) en vervolgens het hele bestand uit te voeren. Hiervoor moet je de
-- testsuite in de database hebben geladen (bedrijf_postgresql_test.sql).
-- NB: niet alle opdrachten hebben testregels.
--
-- Lever je werk pas in op Canvas als alle tests slagen.
-- ------------------------------------------------------------------------


-- S5.1.
-- Welke medewerkers hebben zowel de Java als de XML cursus
-- gevolgd? Geef hun personeelsnummers.
-- DROP VIEW IF EXISTS s5_1; CREATE OR REPLACE VIEW s5_1 AS                                                   -- [TEST]

select cursist
from inschrijvingen
where cursus='S02'
and cursist in (SELECT cursist FROM inschrijvingen WHERE cursus='JAV');

-- S5.2.
-- Geef de nummers van alle medewerkers die niet aan de afdeling 'OPLEIDINGEN'
-- zijn verbonden.
-- DROP VIEW IF EXISTS s5_2; CREATE OR REPLACE VIEW s5_2 AS                                                     -- [TEST]

select * from medewerkers
where afd not in (SELECT anr from afdelingen a WHERE a.naam='OPLEIDINGEN');

-- S5.3.
-- Geef de nummers van alle medewerkers die de Java-cursus niet hebben
-- gevolgd.
-- DROP VIEW IF EXISTS s5_3; CREATE OR REPLACE VIEW s5_3 AS                                                     -- [TEST]

select * from medewerkers
where mnr not in (select cursist from inschrijvingen where cursus='JAV');

-- S5.4.
-- Welke medewerkers (naam) hebben ondergeschikten? En welke niet? (Je mag twee
-- queries gebruiken.)
-- DROP VIEW IF EXISTS s5_4; CREATE OR REPLACE VIEW s5_4 AS                                                     -- [TEST]

select * from medewerkers
where mnr in (select chef from medewerkers)
and mnr in (select chef from medewerkers group by chef having count (*) > 1);

-- S5.5.
-- Geef cursuscode en begindatum van alle uitvoeringen van programmeercursussen
-- ('BLD') in 2020.
-- DROP VIEW IF EXISTS s5_5; CREATE OR REPLACE VIEW s5_5 AS                                                     -- [TEST]


-- BLD bestaat niet

-- S5.6.
-- Geef van alle cursusuitvoeringen: de cursuscode, de begindatum en het
-- aantal inschrijvingen (`aantal_inschrijvingen`). Sorteer op begindatum.
-- DROP VIEW IF EXISTS s5_6; CREATE OR REPLACE VIEW s5_6 AS                                                     -- [TEST]

-- join
select i.cursus, i.begindatum, count(*) as aantal_inschrijvingen
from uitvoeringen u
join inschrijvingen i on u.cursus = i.cursus and u.begindatum = i.begindatum
group by i.begindatum, i.cursus
order by i.begindatum;

-- subquery
select cursus, begindatum,
       (SELECT count(*) from inschrijvingen i WHERE
               u.cursus = i.cursus and u.begindatum = i.begindatum) as aantal_inschrijvingen
from uitvoeringen u;

-- S5.7.
-- Geef voorletter(s) en achternaam van alle trainers die ooit tijdens een
-- algemene cursus hun eigen chef als cursist hebben gehad.
-- DROP VIEW IF EXISTS s5_7; CREATE OR REPLACE VIEW s5_7 AS                                                     -- [TEST]

select concat(voorl, ' ', naam)
from medewerkers m
where functie = 'TRAINER'
and chef in (
    select docent
    from uitvoeringen u
    where begindatum in (
        select begindatum
        from inschrijvingen i
        where m.mnr = i.cursist));

-- S5.8.
-- Geef de naam van de medewerkers die nog nooit een cursus hebben gegeven.
-- DROP VIEW IF EXISTS s5_8; CREATE OR REPLACE VIEW s5_8 AS                                                     -- [TEST]

select concat(voorl, ' ', naam) from medewerkers
where mnr not in (select cursist from inschrijvingen);


-- -------------------------[ HU TESTRAAMWERK ]--------------------------------
-- Met onderstaande query kun je je code testen. Zie bovenaan dit bestand
-- voor uitleg.

SELECT * FROM test_select('S5.1') AS resultaat
UNION
SELECT * FROM test_select('S5.2') AS resultaat
UNION
SELECT * FROM test_select('S5.3') AS resultaat
UNION
SELECT * FROM test_select('S5.4') AS resultaat
UNION
SELECT * FROM test_select('S5.5') AS resultaat
UNION
SELECT * FROM test_select('S5.6') AS resultaat
UNION
SELECT * FROM test_select('S5.7') AS resultaat
UNION
SELECT * FROM test_select('S5.8') AS resultaat
ORDER BY resultaat;