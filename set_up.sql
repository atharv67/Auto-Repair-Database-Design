create table valid(id number(38) primary key, password char(40), type char(20));

CREATE TABLE SERVICE_CENTRE( Id INTEGER,Addr CHAR(1000),Sat_Open char(1),Tele_number CHAR(20), State CHAR(10),Rate INTEGER, PRIMARY KEY (Id), minimum_wage integer, maximum_wage integer,check(rate>=minimum_wage and rate<=maximum_wage));

create table manufacturer(name char(100) primary key);

CREATE TABLE CAR( Vin CHAR(100),Mileage INTEGER, Manufacturer char(100), Last_schedule CHAR(1), Year CHAR(20),PRIMARY KEY(Vin), foreign key (manufacturer) references manufacturer (name) );

CREATE TABLE CUSTOMER(username integer,address char(100), email char(30), pno char(20),Id INTEGER,S_id INTEGER NOT NULL,PRIMARY KEY(Id,S_id),FOREIGN KEY (S_id) REFERENCES SERVICE_CENTRE(Id) ON DELETE CASCADE, foreign key (username) references valid(id),FName CHAR(20),Lname CHAR(20), Status integer, Standing integer);

CREATE TABLE CUST_HAS_CAR( Vin CHAR(100),Id INTEGER,S_id INTEGER,PRIMARY KEY(Vin, Id,S_id),FOREIGN KEY (Id,S_id) REFERENCES CUSTOMER(Id,S_id),FOREIGN KEY (Vin) REFERENCES CAR(Vin) ON DELETE CASCADE);

CREATE TABLE SERVICES( Service_no INTEGER PRIMARY KEY, Name CHAR(100));

CREATE TABLE CAR_NEEDS_SERVICE( manufacturer char(100), Service_no INTEGER, PRIMARY KEY(manufacturer, Service_no),Duration INTEGER,FOREIGN KEY (manufacturer) REFERENCES manufacturer(name),FOREIGN KEY (Service_no) REFERENCES SERVICES(Service_no));

-- CREATE TABLE OFFERS(Service_No INTEGER,Id INTEGER,PRIMARY KEY(Service_no, Id),FOREIGN KEY (Service_no) REFERENCES SERVICES(Service_no), FOREIGN KEY (Id) REFERENCES SERVICE_CENTRE(Id));

CREATE TABLE CAR_HAS_COST_OF_SERVICE( manufacturer char(100), Service_no INTEGER, Id INTEGER, Cost INTEGER, PRIMARY KEY(manufacturer, Service_no, Id),FOREIGN KEY (manufacturer) REFERENCES manufacturer(name),FOREIGN KEY (Service_no) REFERENCES Services(Service_no),FOREIGN KEY (Id) REFERENCES Service_Centre(Id));

CREATE TABLE EMPLOYEE( E_Id INTEGER PRIMARY KEY, Pno CHAR(13), Name CHAR(20),Addr CHAR(1000),Email CHAR(30),role varchar2(10),Id INTEGER NOT NULL,FOREIGN KEY (Id) REFERENCES SERVICE_CENTRE(Id), foreign key (E_Id) references valid(id));

CREATE TABLE CONTRACT_EMPLOYEES( E_Id INTEGER PRIMARY KEY,Salary INTEGER,FOREIGN KEY (E_Id) references EMPLOYEE(E_Id) on delete cascade);

CREATE TABLE MANAGER(E_Id INTEGER PRIMARY KEY,FOREIGN KEY (E_Id) references CONTRACT_EMPLOYEES(E_Id) on delete cascade);

CREATE TABLE RECEPTIONIST(E_Id INTEGER PRIMARY KEY,FOREIGN KEY (E_Id) references CONTRACT_EMPLOYEES(E_Id) on delete cascade);

--CREATE TABLE MANAGES(Manager_id INTEGER,Employee_id INTEGER,PRIMARY KEY (Manager_id, Employee_id),FOREIGN KEY (Manager_id) REFERENCES MANAGER(E_Id), FOREIGN KEY (Employee_id) REFERENCES EMPLOYEE(E_Id) );

CREATE TABLE HOURLY_EMPLOYEES(E_Id INTEGER PRIMARY KEY,FOREIGN KEY (E_Id) references EMPLOYEE(E_Id) on delete cascade);

CREATE TABLE MECHANICS(E_Id INTEGER PRIMARY KEY,FOREIGN KEY (E_Id) references HOURLY_EMPLOYEES(E_Id) on delete cascade);

-- CREATE TABLE WEEK(Id INTEGER PRIMARY KEY);

-- CREATE TABLE MECHANIC_WORKS( Mech_id INTEGER,Week INTEGER,PRIMARY KEY (Mech_id, Week), Hours INTEGER,FOREIGN KEY (Mech_id) references MECHANICS(E_Id), FOREIGN KEY (Week) references WEEK(Id));

-- CREATE TABLE TIME_OFF( Id INTEGER PRIMARY KEY, Start_date DATE,End_date DATE);
CREATE TABLE TIME_SLOT( Id INTEGER PRIMARY KEY, daysofweek integer, week integer, slot integer);

CREATE TABLE MECH_TIME_OFF(M_Id INTEGER,T_Id INTEGER,PRIMARY KEY(M_Id, T_Id),FOREIGN KEY (M_id) references MECHANICS(E_Id), FOREIGN KEY (T_Id) references Time_Slot(Id));

CREATE TABLE REPAIRS(Service_no INTEGER PRIMARY KEY, FOREIGN KEY (Service_no) REFERENCES SERVICES(Service_no) on delete cascade);

CREATE TABLE ENGINE(Service_no INTEGER PRIMARY KEY,FOREIGN KEY (Service_no) REFERENCES REPAIRS(Service_no) on delete cascade);

CREATE TABLE EXHAUST(Service_no INTEGER PRIMARY KEY,FOREIGN KEY (Service_no) REFERENCES REPAIRS(Service_no) on delete cascade);

CREATE TABLE ELECTRICAL(Service_no INTEGER PRIMARY KEY,FOREIGN KEY (Service_no) REFERENCES REPAIRS(Service_no) on delete cascade);

CREATE TABLE TRANSMISSION(Service_no INTEGER PRIMARY KEY,FOREIGN KEY (Service_no) REFERENCES REPAIRS(Service_no) on delete cascade);

CREATE TABLE TIRE(Service_no INTEGER PRIMARY KEY,FOREIGN KEY (service_no) REFERENCES REPAIRS(Service_no) on delete cascade);

CREATE TABLE HEAT_AND_AC(Service_no INTEGER PRIMARY KEY,FOREIGN KEY (Service_no) REFERENCES REPAIRS(Service_no) on delete cascade);

CREATE TABLE MAINTENANCE(Service_no INTEGER PRIMARY KEY,schedule char(1),FOREIGN KEY (Service_no) REFERENCES SERVICES(Service_no)on delete cascade);

-- CREATE TABLE CUST_HAS_CAR_REQUESTS_SERVICE( Vin CHAR(100),Cust_Id INTEGER,S_Id INTEGER,Service_no INTEGER,PRIMARY KEY(Vin, Cust_Id, S_Id, Service_no),FOREIGN KEY (Vin,Cust_Id,S_Id) REFERENCES CUST_HAS_CAR(Vin,Id,S_Id), FOREIGN KEY (Service_no) REFERENCES SERVICES(Service_no) );

-- CREATE TABLE MECHANIC_PROVIDES_SERVICE(M_Id INTEGER,T_id integer, Vin CHAR(100),Cust_Id INTEGER,S_Id INTEGER,Service_No INTEGER,PRIMARY KEY(M_Id,T_id,Vin, Cust_Id, Id, Service_no),FOREIGN KEY (Vin,Cust_id,S_Id,Service_No) REFERENCES CUST_HAS_CAR_REQUESTS_SERVICE(Vin,Cust_id,S_Id,Service_No),FOREIGN KEY (M_id,T_id) REFERENCES bookings(M_id,T_id));

-- CREATE TABLE REQUEST_ALLOCATED_TIME_SLOT( M_Id INTEGER,Vin CHAR(100),Cust_Id INTEGER,Id INTEGER,Service_no INTEGER,Time_Slot INTEGER,PRIMARY KEY(M_Id,Vin, Cust_Id, Id, Service_no, Time_slot),FOREIGN KEY (M_Id,Vin, Cust_Id, Id, Service_no) REFERENCES MECHANIC_PROVIDES_PROVIDES_SERVICE(M_Id,Vin, Cust_Id, Id, Service_no), FOREIGN KEY (Time_Slot) REFERENCES TIME_SLOT(Id));

CREATE TABLE INVOICE( invoice_id INTEGER,m_id integer not null, cust_id integer not null, S_id integer not null, vin char(100) not null, start_time_slot integer, paid integer,cost integer, check(paid in (0,1)), PRIMARY KEY(invoice_id), foreign key (cust_id,s_id,vin) references cust_has_car(id,s_id,vin), foreign key (m_id) references mechanics(e_id), foreign key (start_time_slot) references time_slot(id));

-- CREATE TABLE GENERATED_INVOICE( M_Id INTEGER,Vin CHAR(100),Cust_Id INTEGER,Id INTEGER,Service_no INTEGER,Invoice_Id INTEGER,PRIMARY KEY(M_Id,Vin, Cust_Id, Id, Service_no, Invoice_Id),FOREIGN KEY (M_Id,Vin, Cust_Id, Id, Service_no) REFERENCES MECHANIC_PROVIDES_PROVIDES_SERVICE(M_Id,Vin, Cust_Id, Id, Service_no),  FOREIGN KEY (Invoice_Id) REFERENCES INVOICE(Id));

-- CREATE TABLE USERS(Id varchar(10), password varchar(20), type varchar(10));

create table bookings(M_id integer,T_Id integer, primary key (M_id,T_id), foreign key (M_id) references mechanics (E_id),foreign key (T_Id) references time_slot(ID));
create table invoice_has_service(invoice_id integer, service_no integer, foreign key (invoice_id) references invoice(invoice_id), foreign key (service_no) REFERENCES services(service_no));
create table swap_request(request_id integer primary key,M_Id1 integer,M_Id2 integer,start_time_slot1 integer,end_time_slot1 integer,start_time_slot2 integer,
end_time_slot2 integer, foreign key (m_id1) references mechanics(e_id),foreign key (m_id2) references mechanics(e_id),
foreign key (start_time_slot1) references time_slot(id),foreign key (start_time_slot2) references time_slot(id),foreign key (end_time_slot1) references time_slot(id),
foreign key (end_time_slot2) references time_slot(id));




--Altering Queries

-- alter table SERVICE_CENTRE add MANAGER_id varchar(13) not null;
-- alter table service_centre modify manager_id int;
-- alter table SERVICE_CENTRE add constraint FK_MANAGER Foreign Key (Manager_id) references Manager(E_id);
-- alter table SERVICE_CENTRE add minimum_wage integer;
-- alter table SERVICE_CENTRE add maximum_wage integer;
-- alter table service_centre modify sat_open char(1);

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

