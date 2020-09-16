-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S6: Views
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
-- ------------------------------------------------------------------------


-- S6.1.
--
-- 1. Maak een view met de naam "deelnemers" waarmee je de volgende gegevens uit de tabellen inschrijvingen en uitvoering combineert:
--    inschrijvingen.cursist, inschrijvingen.cursus, inschrijvingen.begindatum, uitvoeringen.docent, uitvoeringen.locatie
-- 2. Gebruik de view in een query waarbij je de "deelnemers" view combineert met de "personeels" view (behandeld in de les):
--     CREATE OR REPLACE VIEW personeel AS
-- 	     SELECT mnr, voorl, naam as medewerker, afd, functie
--       FROM medewerkers;
-- 3. Is de view "deelnemers" updatable ? Waarom ?

CREATE OR REPLACE VIEW deelnemers AS
SELECT i.cursist, i.cursus, i.begindatum, u.docent, u.locatie
FROM inschrijvingen i
join uitvoeringen u on i.cursus = u.cursus and i.begindatum = u.begindatum;

CREATE OR REPLACE VIEW personeel AS
SELECT mnr, voorl, naam as medewerker, afd, functie
FROM medewerkers;

select *
from deelnemers;

select * from personeel join deelnemers on mnr = cursist order by mnr ;

-- S6.2.
--
-- 1. Maak een view met de naam "dagcursussen". Deze view dient de gegevens op te halen:
--      code, omschrijving en type uit de tabel curssussen met als voorwaarde dat de lengte = 1. Toon aan dat de view werkt.

CREATE OR REPLACE VIEW dagcursussen AS
    SELECT code, omschrijving, type from cursussen
    where lengte=1;

select * from cursussen;
select * from dagcursussen;
-- 2. Maak een tweede view met de naam "daguitvoeringen".
--    Deze view dient de uitvoeringsgegevens op te halen voor de "dagcurssussen" (gebruik ook de view "dagcursussen"). Toon aan dat de view werkt

CREATE OR REPLACE VIEW daguitvoeringen AS
    SELECT * FROM uitvoeringen
    JOIN dagcursussen c on uitvoeringen.cursus = c.code;

select * from daguitvoeringen;

-- 3. Verwijder de views en laat zien wat de verschillen zijn bij DROP view <viewnaam> CASCADE en bij DROP view <viewnaam> RESTRICT

DROP view dagcursussen CASCADE;

-- CASCADE dropt ook daguitvoeringen omdat die leunt op de dagcursussen view