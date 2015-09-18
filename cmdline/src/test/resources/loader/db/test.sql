create table grades ( id integer, name varchar2(32));

insert into grades values (  9, 'Pessoal' );
insert into grades values ( 10, 'Profissional' );

create table import ( grade varchar2(32), name varchar2(32), birth date );



create table SERVICE_RATES (
	PLAN_UID Number(19,0) NOT NULL ,
	SERVICE_UID Number(19,0) NOT NULL ,
	EFFECTIVE_DATE Date NOT NULL ,
	LOADED_DATE Date NOT NULL ,
	ACCESS_FEE Number(16,4),
	ONETIME_RATE Number(16,4),
	SUBSCRIPTION_RATE Number(16,4),
	CUSTOM_1 Varchar2 (30),
	CUSTOM_2 Varchar2 (30),
	CUSTOM_3 Varchar2 (30),
	constraint SERVICE_RATES_PK primary key ("PLAN_UID","SERVICE_UID","EFFECTIVE_DATE") 
);

alter table SERVICE_RATES add Constraint SERVICE_RATES_BY_PLAN_FK1 foreign key (PLAN_UID) references QLF_PLANS (OBJID);

alter table SERVICE_RATES add Constraint SERVICE_RATES_BY_SERVICE_FK1 foreign key (SERVICE_UID) references QLF_SERVICES (OBJID);

