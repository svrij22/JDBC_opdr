-- S1.4. Adressen
CREATE TABLE adressen (
      postcode CHAR(6) constraint c_postcode_check CHECK(postcode similar to '[0-9]{4}[A-Z]{2}'),
      huisnummer INT NOT NULL,
      ingangsdatum DATE NOT NULL,
      einddatum DATE,
      CONSTRAINT check_datum CHECK ( einddatum > ingangsdatum OR einddatum IS NULL),
      telefoon varchar(10) constraint c_telefoon_unique UNIQUE,
      CONSTRAINT c_telefoon_check CHECK(telefoon similar to '[0-9]{10}'),
      med_nmr INT NOT NULL CONSTRAINT fk_med_nmr REFERENCES medewerkers(mnr) DEFERRABLE,
      CONSTRAINT c_pks PRIMARY KEY (postcode, huisnummer, ingangsdatum)
);

drop table adressen;
select * from adressen;
insert into adressen values ('1401RJ', 19, '1990-10-10','1991-10-10', '0623879773', 8000);

/*Werken niet*/
insert into adressen values ('140ARJ', 19, '1990-10-10','1991-10-10', '0623879777', 8000);
insert into adressen values ('1401RJ', 19, '1992-10-10','1991-10-10', '0623879777', 8000);
insert into adressen values ('1401RJ', 19, '1990-10-10','1991-10-10', '06238', 8000);

-- S2.2. Medewerkersoverzicht
select * from medewerkers order by functie, gbdatum desc;

-- S3.2.
select distinct (m.voorl||' '|| m.naam) AS naam, (d.voorl||' '|| d.naam) AS docent from inschrijvingen i
    join medewerkers m on i.cursist = m.mnr
    join uitvoeringen u on i.cursus = u.cursus and i.begindatum = u.begindatum
    join medewerkers d on u.docent = d.mnr
where i.cursus='S02';

-- S4.3.
select cursus, begindatum, count(*) as aantal_inschrijvingen
from inschrijvingen
GROUP BY cursus, begindatum
HAVING count(*) >= 3;

-- S5.7
select concat(voorl, ' ', naam)
from medewerkers m
where functie = 'TRAINER'
  and chef in (
    select cursist
    from inschrijvingen i
    where begindatum in (
        select begindatum
        from uitvoeringen u
        where m.mnr = u.docent));


select * from uitvoeringen;

-- S6.1
CREATE OR REPLACE VIEW deelnemers AS
SELECT i.cursist, i.cursus, i.begindatum, u.docent, u.locatie
FROM inschrijvingen i
         join uitvoeringen u on i.cursus = u.cursus and i.begindatum = u.begindatum;

CREATE OR REPLACE VIEW personeel AS
SELECT mnr, voorl, naam as medewerker, afd, functie
FROM medewerkers;

select * from personeel join deelnemers on mnr = cursist order by mnr ;

drop view deelnemers, personeel cascade;

-- S7.2
-- 1. Maak de volgende twee query’s:
-- 	  A. Toon uit de order tabel de order met order_id = 73590
-- 	  B. Toon uit de order tabel de order met customer_id = 1028
select * from orders where order_id = 73590;
select * from orders where customer_id = 1028;
-- 2. Analyseer met EXPLAIN hoe de query’s uitgevoerd worden en kopieer het explain plan onderaan de opdracht
EXPLAIN select * from orders where order_id = 73590;
EXPLAIN select * from orders where customer_id = 1028;

-- Index Scan using pk_sales_orders on orders  (cost=0.29..8.31 rows=1 width=155)
--   Index Cond: (order_id = 73590)

-- Seq Scan on orders  (cost=0.00..1819.94 rows=107 width=155)
--   Filter: (customer_id = 1028)

-- 3. Verklaar de verschillen en schrijf deze op

-- De ene is een sequence scan en de andere is een index scan
-- order_id is uniek

-- 4. Voeg een index toe, waarmee query B versneld kan worden
CREATE INDEX orders_cust_id_idx ON orders (customer_id);
DROP INDEX orders_cust_id_idx;
-- 5. Analyseer met EXPLAIN en kopieer het explain plan onder de opdracht

-- Bitmap Heap Scan on orders  (cost=5.12..308.96 rows=107 width=155)
--   Recheck Cond: (customer_id = 1028)
--   ->  Bitmap Index Scan on orders_cust_id_idx  (cost=0.00..5.10 rows=107 width=0)
--         Index Cond: (customer_id = 1028)

-- 6. Verklaar de verschillen en schrijf hieronder op
/*  De zoektijd is veel lager*/