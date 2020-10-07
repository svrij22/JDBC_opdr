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

select * from medewerkers order by functie, gbdatum desc;