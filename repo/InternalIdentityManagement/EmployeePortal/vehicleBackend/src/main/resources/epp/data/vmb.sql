Rem ! $ORACLE_HOME/bin/sqlplus
Rem
Rem Program: sqlplus
Rem
Rem Requirements: sqlplus
Rem     SCHEMA MUST EXISTS
Rem
Rem Purpose:
Rem     This script creates Employee Self Service Vehicle Brands.
Rem
Rem Usage Information:
Rem     sqlplus eppowner/<password>
Rem     @<path>/images/brand
Rem
Rem
Rem Revisions   Date        Editor      Comment
Rem -----------+-----------+-----------+-----------------------------------
Rem 1.0.0.0     2010-03-30  DSteding    First release version
Rem

PROMPT ********************************************************************
PROMPT Setting user context
PROMPT ********************************************************************
BEGIN
  epp$session.set_identity('xelsysadm', 'CLI');
END;
/
PROMPT ********************************************************************
PROMPT Create default vehicle brands for Employee Self Service Portal
PROMPT ********************************************************************
DECLARE
  cg$rec cg$ept_vehicle_brands.cg$row_type;
  cg$ind cg$ept_vehicle_brands.cg$ind_type;
BEGIN
  cg$ind.id   := TRUE;
  cg$rec.id   := '0005';
  cg$rec.name := 'Bayrische Motoren Werke';
  cg$rec.icon := '/images/brand/bmw-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '0009';
  cg$rec.name := 'Daimler Benz';
  cg$rec.icon := '/images/brand/mercedes-benz-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '0030';
  cg$rec.name := 'Opel';
  cg$rec.icon := '/images/brand/opel-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '0583';
  cg$rec.name := 'Porsche';
  cg$rec.icon := '/images/brand/porsche-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '0588';
  cg$rec.name := 'Audi';
  cg$rec.icon := '/images/brand/audi-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '0600';
  cg$rec.name := 'Volkswagen';
  cg$rec.icon := '/images/brand/volkswagen-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '0708';
  cg$rec.name := 'Mercedes-Benz';
  cg$rec.icon := '/images/brand/mercedes-benz-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1005';
  cg$rec.name := 'Ford';
  cg$rec.icon := '/images/brand/ford-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1006';
  cg$rec.name := 'General Motors';
  cg$rec.icon := '/images/brand/gmc-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1012';
  cg$rec.name := 'Mitsubishi';
  cg$rec.icon := '/images/brand/mitsubishi-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1032';
  cg$rec.name := 'Mazda';
  cg$rec.icon := '/images/brand/mazda-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1153';
  cg$rec.name := 'Honda';
  cg$rec.icon := '/images/brand/honda-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1158';
  cg$rec.name := 'Jeep';
  cg$rec.icon := '/images/brand/jeep-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1159';
  cg$rec.name := 'Toyota';
  cg$rec.icon := '/images/brand/toyota-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1260';
  cg$rec.name := 'Kia Motors';
  cg$rec.icon := '/images/brand/kia-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1392';
  cg$rec.name := 'Nissan';
  cg$rec.icon := '/images/brand/nissan-24.png';
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1349';
  cg$rec.name := 'Hyundai Motors';
  cg$rec.icon := '/images/brand/hyundai-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1414';
  cg$rec.name := 'Mercedes AMG';
  cg$rec.icon := '/images/brand/amg-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1480';
  cg$rec.name := 'Tesla';
  cg$rec.icon := '/images/brand/tesla-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1590';
  cg$rec.name := 'Land Rover';
  cg$rec.icon := '/images/brand/land-rover-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1731';
  cg$rec.name := 'KTM';
  cg$rec.icon := '/images/brand/ktm-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '1842';
  cg$rec.name := 'Subaru';
  cg$rec.icon := '/images/brand/subaru-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '2051';
  cg$rec.name := 'Jaguar';
  cg$rec.icon := '/images/brand/jaguar-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '2052';
  cg$rec.name := 'Morris';
  cg$rec.icon := '/images/brand/mini-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '2055';
  cg$rec.name := 'Rover';
  cg$rec.icon := '/images/brand/rover-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '2068';
  cg$rec.name := 'Vauxhall';
  cg$rec.icon := '/images/brand/vauxhall-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '2086';
  cg$rec.name := 'Chrysler';
  cg$rec.icon := '/images/brand/chrysler-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '2091';
  cg$rec.name := 'Bentley';
  cg$rec.icon := '/images/brand/bentley-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '2197';
  cg$rec.name := 'Rolls Royce';
  cg$rec.icon := '/images/brand/rolls-royce-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '3000';
  cg$rec.name := 'Bugatti';
  cg$rec.icon := '/images/brand/bugatti-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '3001';
  cg$rec.name := 'Citroen';
  cg$rec.icon := '/images/brand/citroen-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '3003';
  cg$rec.name := 'Peugeot';
  cg$rec.icon := '/images/brand/peugeot-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '3004';
  cg$rec.name := 'Renault';
  cg$rec.icon := '/images/brand/renault-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '4000';
  cg$rec.name := 'Alfa Romeo';
  cg$rec.icon := '/images/brand/alfa-remeo-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '4001';
  cg$rec.name := 'Fiat';
  cg$rec.icon := '/images/brand/fiat-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '4002';
  cg$rec.name := 'Lancia';
  cg$rec.icon := '/images/brand/lancia-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '4014';
  cg$rec.name := 'Masarati';
  cg$rec.icon := '/images/brand/masarati-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '4019';
  cg$rec.name := 'Ferrari';
  cg$rec.icon := '/images/brand/ferrari-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
  --
  cg$ind.id   := TRUE;
  cg$rec.id   := '4026';
  cg$rec.name := 'Lamborghini';
  cg$rec.icon := '/images/brand/lamborghini-24.png';
  --
  cg$ept_vehicle_brands.ins(cg$rec, cg$ind);
END;
/
COMMIT
/
