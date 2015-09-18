Create table MEGATIM_PARAMS (
	OBJID Number(19,0) NOT NULL ,
	ACTIVATION_UID Number(19,0) NOT NULL ,
	GEO_UID Number(19,0) NOT NULL ,
	ONNET_ONLY Char (1) Default  'N' NOT NULL ,
	VALID_PERIOD Integer NOT NULL ,
	INVOICE_DESCRIPTION Varchar2 (128) NOT NULL ,
	LOADED_DATE Date NOT NULL ,
	NEW_CUSTOMER_FEE Number(16,4),
	YET_CUSTOMER_FEE Number(16,4),
	CUSTOM_1 Varchar2 (30),
	CUSTOM_2 Varchar2 (30),
	CUSTOM_3 Varchar2 (30),
 Constraint pk_MEGATIM_PARAMS primary key (OBJID) 
);

Create table MEGATIM_SERVICES (
	SERVICE_UID Number(19,0) NOT NULL ,
	MEGATIM_UID Number(19,0) NOT NULL ,
	PROMO_QTTY Integer NOT NULL ,
 Constraint pk_MEGATIM_SERVICES primary key (SERVICE_UID,MEGATIM_UID) 
);

Create Index MEGATIM_PARAMS_IDX1 ON MEGATIM_PARAMS (LOADED_DATE);

alter table MEGATIM_SERVICES add Constraint MEGATIM_SERVICES_BY_SERVICES_FK foreign key (SERVICE_UID) references QLF_SERVICES (OBJID);
alter table MEGATIM_PARAMS add Constraint MEGATIM_ACTIVATION_SERVICE_FK1 foreign key (ACTIVATION_UID) references QLF_SERVICES (OBJID);
alter table MEGATIM_SERVICES add Constraint MEGATIM_SERVICES_BY_MEGATIM_FK foreign key (MEGATIM_UID) references MEGATIM_PARAMS (OBJID);
alter table MEGATIM_PARAMS add Constraint MEGATIM_PARAMS_GEO_FK1 foreign key (GEO_UID) references BCK_GEO_DM (OBJID);

create table contract_history ( co_id integer, ch_validfrom date, ch_status varchar2(1), ch_seqno integer);

-- inserts for ACCOUNT2.BGH
insert into megatim_params values (1, 
  (select objid from qlf_services where short_desc = 'PMT04'), 
  (select objid from bck_geo_dm where geo_state = 'AM'),
  'F', 30, 'MegaTIM Mensagens 2009', sysdate,
  10, 12, null, null, null);

insert into megatim_services ( select objid, 1, 200 from qlf_services where short_desc like '%TS22%')

-- inserts for ACCOUNT3.BGH
insert into contr_services (co_id, tmcode, spcode, sncode, cs_seqno, cs_stat_chng, cs_subscript, cs_access, cs_dis_subscr,
                            cs_entdate, currency, username, initiator_type, rec_version) values 
                           (54360138, 313, 1, 440, 1, '090101a', 0, 0, 0, sysdate, 43, 'NONE', 'D', 1);
                           
insert into contract_history values ( 54360138, to_date('01/01/07', 'DD/MM/YY'), 'a', 1);

-- inserts for ACCOUNT9.BGH

insert into qlf_services (objid, svc_code, description, short_desc) values (1000, 899,'Torpedo Ilimitado-PcteAd-1','MTT07');
insert into qlf_services (objid, svc_code, description, short_desc) values (1001, 900,'Torpedo Ilimitado-PcteAd-2','MTT08');
insert into qlf_services (objid, svc_code, description, short_desc) values (1002, 901,'Torpedo Ilimitado-PcteAd-3','MTT09');
insert into qlf_services (objid, svc_code, description, short_desc) values (1003, 902,'Torpedo Ilimitado-PcteAd-4','MTT10');

insert into contr_services (co_id, tmcode, spcode, sncode, cs_seqno, cs_stat_chng, cs_subscript, cs_access, cs_dis_subscr,
                            cs_entdate, currency, username, initiator_type, rec_version) values 
                           (54397240, 374, 3, 899, 1, '091015a', 0, 0, 0, sysdate, 43, 'NONE', 'D', 1);

insert into contr_services (co_id, tmcode, spcode, sncode, cs_seqno, cs_stat_chng, cs_subscript, cs_access, cs_dis_subscr,
                            cs_entdate, currency, username, initiator_type, rec_version) values 
                           (54397240, 374, 3, 900, 1, '091104a', 0, 0, 0, sysdate, 43, 'NONE', 'D', 1);                           
                           
insert into megatim_params (objid, activation_uid, geo_uid, onnet_only, valid_period, invoice_description,
                            loaded_date, new_customer_fee, yet_customer_fee)
values (3, 
       (select objid from qlf_services where svc_code = 899), 
       ()select objid from bck_geo_dm where geo_state = 'RJ'),
       'T', 30, 'DELETE-Torpedo Ilimitado', sysdate, 14.9, 14.9);

insert into megatim_services ( select objid, 3, -1 from qlf_services where short_desc = 'TS22' );
       
       
insert into megatim_params (objid, activation_uid, geo_uid, onnet_only, valid_period, invoice_description,
                            loaded_date, new_customer_fee, yet_customer_fee)
values (4, 
       (select objid from qlf_services where svc_code = 900), 
       ()select objid from bck_geo_dm where geo_state = 'RJ'),
       'T', 30, 'DELETE-Torpedo Ilimitado', sysdate, 14.9, 14.9);

insert into megatim_services ( select objid, 4, -1 from qlf_services where short_desc = 'TS22' );

insert into megatim_params (objid, activation_uid, geo_uid, onnet_only, valid_period, invoice_description,
                            loaded_date, new_customer_fee, yet_customer_fee)
values (5, 
       (select objid from qlf_services where svc_code = 902), 
       ()select objid from bck_geo_dm where geo_state = 'RJ'),
       'T', 30, 'DELETE-Torpedo Ilimitado', sysdate, 14.9, 14.9);

insert into megatim_services ( select objid, 5, -1 from qlf_services where short_desc = 'TS22' );       
