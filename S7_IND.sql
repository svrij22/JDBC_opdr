-- ------------------------------------------------------------------------
-- Data & Persistency
-- Opdracht S7: Indexen
--
-- (c) 2020 Hogeschool Utrecht
-- Tijmen Muller (tijmen.muller@hu.nl)
-- André Donk (andre.donk@hu.nl)
-- ------------------------------------------------------------------------
-- LET OP, zoals in de opdracht op Canvas ook gezegd kun je informatie over
-- het query plan vinden op: https://www.postgresql.org/docs/current/using-explain.html


-- S7.1.
--
-- Je maakt alle opdrachten in de 'sales' database die je hebt aangemaakt en gevuld met
-- de aangeleverde data (zie de opdracht op Canvas).
--
-- Voer het voorbeeld uit wat in de les behandeld is:
-- 1. Voer het volgende EXPLAIN statement uit:
--    EXPLAIN SELECT * FROM order_lines WHERE stock_item_id = 9;
--    Bekijk of je het resultaat begrijpt. Kopieer het explain plan onderaan de opdracht

EXPLAIN SELECT * FROM order_lines WHERE stock_item_id = 9;
-- Gather  (cost=1000.00..6151.67 rows=1004 width=96)
--         Workers Planned: 2
--   ->  Parallel Seq Scan on order_lines  (cost=0.00..5051.27 rows=418 width=96)
--         Filter: (stock_item_id = 9)

-- 2. Voeg een index op stock_item_id toe:
--    CREATE INDEX ord_lines_si_id_idx ON order_lines (stock_item_id);
CREATE INDEX ord_lines_si_id_idx ON order_lines (stock_item_id);
-- 3. Analyseer opnieuw met EXPLAIN hoe de query nu uitgevoerd wordt
--    Kopieer het explain plan onderaan de opdracht
EXPLAIN SELECT * FROM order_lines WHERE stock_item_id = 9;

-- Bitmap Heap Scan on order_lines  (cost=20.20..2306.51 rows=1004 width=96)
--   Recheck Cond: (stock_item_id = 9)
--   ->  Bitmap Index Scan on ord_lines_si_id_idx  (cost=0.00..19.95 rows=1004 width=0)
--         Index Cond: (stock_item_id = 9)

-- 4. Verklaar de verschillen. Schrijf deze hieronder op.

/*Bij het aanmaken van een nieuwe index op stock_item_id gaat de zoektijd van 5051 rows naar 20 rows.
  De 'kosten' van het lezen worden lager. Dit houdt in dat er minder rows hoeven worden afgelezen
  De database is minder lang bezig met handmatig zoeken omdat hij via de index sneller kan zoeken*/

-- S7.2.
--
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


-- S7.3.A
--
-- Het blijkt dat customers regelmatig klagen over trage bezorging van hun bestelling.
-- Het idee is dat verkopers misschien te lang wachten met het invoeren van de bestelling in het systeem.
-- Daar willen we meer inzicht in krijgen.
-- We willen alle orders (order_id, order_date, salesperson_person_id (als verkoper),
--    het verschil tussen expected_delivery_date en order_date (als levertijd),
--    en de bestelde hoeveelheid van een product zien (quantity uit order_lines).
-- Dit willen we alleen zien voor een bestelde hoeveelheid van een product > 250
--   (we zijn nl. als eerste geïnteresseerd in grote aantallen want daar lijkt het vaker mis te gaan)
-- En verder willen we ons focussen op verkopers wiens bestellingen er gemiddeld langer over doen.
-- De meeste bestellingen kunnen binnen een dag bezorgd worden, sommige binnen 2-3 dagen.
-- Het hele bestelproces is er op gericht dat de gemiddelde bestelling binnen 1.45 dagen kan worden bezorgd.
-- We willen in onze query dan ook alleen de verkopers zien wiens gemiddelde levertijd
--  (expected_delivery_date - order_date) over al zijn/haar bestellingen groter is dan 1.45 dagen.
-- Maak om dit te bereiken een subquery in je WHERE clause.
-- Sorteer het resultaat van de hele geheel op levertijd (desc) en verkoper.
-- 1. Maak hieronder deze query (als je het goed doet zouden er 377 rijen uit moeten komen, en het kan best even duren...)

-- one query
select order_id, order_date, salesperson_person_id from orders
where salesperson_person_id in
    (select salesperson_person_id
    from orders
    group by salesperson_person_id
    having (avg(expected_delivery_date - order_date) > 1.45))
and order_id in
    (select order_id from order_lines
    where quantity > 250);


-- S7.3.B
--
-- 1. Vraag het EXPLAIN plan op van je query (kopieer hier, onder de opdracht)
-- 2. Kijk of je met 1 of meer indexen de query zou kunnen versnellen
-- 3. Maak de index(en) aan en run nogmaals het EXPLAIN plan (kopieer weer onder de opdracht)
-- 4. Wat voor verschillen zie je? Verklaar hieronder.

explain select order_id, order_date, salesperson_person_id from orders

where salesperson_person_id in
      (select salesperson_person_id
       from orders
       group by salesperson_person_id
       having (avg(expected_delivery_date - order_date) > 1.45))
  and order_id in
      (select order_id from order_lines
       where quantity > 250);

CREATE INDEX sales_p_id_idx ON orders (salesperson_person_id);
CREATE INDEX orders_cust_id_idx ON orders (customer_id);
CREATE INDEX orderl_cust_id_idx ON order_lines (order_id);

/*
VOOR

Hash Join  (cost=8353.03..10200.96 rows=303 width=12)
  Hash Cond: (orders.salesperson_person_id = orders_1.salesperson_person_id)
  ->  Hash Semi Join  (cost=6164.90..8009.06 rows=1010 width=12)
        Hash Cond: (orders.order_id = order_lines.order_id)
        ->  Seq Scan on orders  (cost=0.00..1635.95 rows=73595 width=12)
        ->  Hash  (cost=6152.27..6152.27 rows=1010 width=4)
              ->  Gather  (cost=1000.00..6152.27 rows=1010 width=4)
                    Workers Planned: 2
                    ->  Parallel Seq Scan on order_lines  (cost=0.00..5051.27 rows=421 width=4)
                          Filter: (quantity > 250)
  ->  Hash  (cost=2188.09..2188.09 rows=3 width=4)
        ->  HashAggregate  (cost=2187.91..2188.06 rows=3 width=4)
              Group Key: orders_1.salesperson_person_id
              Filter: (avg((orders_1.expected_delivery_date - orders_1.order_date)) > 1.45)
              ->  Seq Scan on orders orders_1  (cost=0.00..1635.95 rows=73595 width=12)

NA

  Hash Join  (cost=8353.03..10200.96 rows=303 width=12)
  Hash Cond: (orders.salesperson_person_id = orders_1.salesperson_person_id)
  ->  Hash Semi Join  (cost=6164.90..8009.06 rows=1010 width=12)
        Hash Cond: (orders.order_id = order_lines.order_id)
        ->  Seq Scan on orders  (cost=0.00..1635.95 rows=73595 width=12)
        ->  Hash  (cost=6152.27..6152.27 rows=1010 width=4)
              ->  Gather  (cost=1000.00..6152.27 rows=1010 width=4)
                    Workers Planned: 2
                    ->  Parallel Seq Scan on order_lines  (cost=0.00..5051.27 rows=421 width=4)
                          Filter: (quantity > 250)
  ->  Hash  (cost=2188.09..2188.09 rows=3 width=4)
        ->  HashAggregate  (cost=2187.91..2188.06 rows=3 width=4)
              Group Key: orders_1.salesperson_person_id
              Filter: (avg((orders_1.expected_delivery_date - orders_1.order_date)) > 1.45)
              ->  Seq Scan on orders orders_1  (cost=0.00..1635.95 rows=73595 width=12)
*/


/*4. Ik zie werkelijk geen verschil*/

-- S7.3.C
--
-- Zou je de query ook heel anders kunnen schrijven om hem te versnellen?
-- Je zou hem eventueel met joins kunnen schrijven