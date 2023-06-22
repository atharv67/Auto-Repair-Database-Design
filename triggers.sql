-- select ca.* from customer c, car ca, cust_has_car chc where c.Id=10001 and c.S_id=30001 and c.Id=chc.Id and c.S_Id=chc.S_Id and chc.vin=ca.vin;
-- select * from c;
-- drop trigger tgr_check_status;
create or replace trigger tgr_check_status
after delete on car
for each row
DECLARE COUNTER INT;cust INT;s_id INT;
BEGIN
select Id into cust from cust_has_car chc1 where chc1.vin=:old.vin;
select S_Id into s_id from cust_has_car chc1 where chc1.vin=:old.vin;
SELECT COUNT(*) into counter from cust_has_car chc where chc.Id=cust;
delete from cust_has_car chc2 where chc2.vin=:old.vin;
IF COUNTER=1
THEN
update customer c
set c.status=0
where c.Id=cust and c.S_Id=s_id;
END IF;
END;



create or replace trigger tgr_check_status_update
after insert on cust_has_car
for each row
DECLARE COUNTER INT;pragma autonomous_transaction;
BEGIN
dbms_output.put_line(:new.S_ID);
SELECT COUNT(*) into counter from cust_has_car chc where chc.Id=:new.Id and chc.S_Id=:new.S_ID;
dbms_output.put_line(counter);
IF COUNTER=0
THEN
update customer c
set c.status=1
where c.Id=:new.Id and c.S_Id=:new.S_Id;
COMMIT;
END IF;
END;


create or replace trigger tgr_add_manufacturer
before insert on car
for each row
DECLARE COUNTER INT;cust INT;s_id INT;
BEGIN
select count(*) into counter from manufacturer m where m.name=:new.manufacturer;
IF COUNTER=0
THEN
insert into manufacturer (name) values (:new.manufacturer);
END IF;
END;


create or replace trigger tgr_check_standing
after insert or update on invoice
for each row
DECLARE COUNTER INT;
BEGIN
select count(*) into counter from invoice i where i.cust_id=:new.cust_id and paid=0 and i.s_id=:new.s_id;
dbms_output.put_line(counter);
IF COUNTER>=1
THEN
dbms_output.put_line('in1')
update customer c
set c.standing=0
where c.Id=:new.cust_id and c.S_Id=:new.s_id;
commit;
ELSE
dbms_output.put_line('in2')
update customer c
set c.standing=1
where c.Id=:new.cust_id and c.S_Id=:new.s_id;
COMMIT;
END IF;
END;

alter table booking add constraint check(select count(*) from booking b, time_slot t where b.t_id= <=50)





