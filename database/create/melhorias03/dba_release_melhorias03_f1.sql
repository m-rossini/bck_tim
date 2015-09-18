
-- removendo tabela que n�o e mais utilizada
--DROP TABLE BILLCHKDB.QLF_USAGE_RATES;

-- criando novas tabelas
CREATE TABLE BILLCHKDB.USAGE_RATES (
	TARIFF_ZONE_UID NUMBER(19,0) NOT NULL ,
	PLANS_UID NUMBER(19,0) NOT NULL ,
	SERVICES_UID NUMBER(19,0) NOT NULL ,
	RATE_TIME_ZONE_UID NUMBER(19,0) NOT NULL ,
	EFFECTIVE_DATE DATE NOT NULL ,
	INCOMING_FLAG CHAR (1) Default 'N' NOT NULL ,
	INIT_VOLUME NUMBER(16,4) NOT NULL ,
	END_VOLUME NUMBER(16,4) NOT NULL ,
	STEP_VOLUME NUMBER(16,4) NOT NULL ,
	STEP_COST NUMBER(16,4) NOT NULL ,
	LOADED_DATE DATE NOT NULL ,
	CUSTOM_1 VARCHAR2 (30),
	CUSTOM_2 VARCHAR2 (30),
	CUSTOM_3 VARCHAR2 (30),
 CONSTRAINT USAGE_RATES_PK PRIMARY KEY (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME) 
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 400M NEXT 100M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


CREATE TABLE BILLCHKDB.MEGATIM_PARAMS (
	OBJID NUMBER(19,0) NOT NULL ,
	ACTIVATION_UID NUMBER(19,0) NOT NULL ,
	GEO_UID NUMBER(19,0) NOT NULL ,
	ONNET_ONLY CHAR (1) Default  'N' NOT NULL ,
	VALID_PERIOD INTEGER NOT NULL ,
	INVOICE_DESCRIPTION VARCHAR2 (128) NOT NULL ,
	LOADED_DATE DATE NOT NULL ,
	NEW_CUSTOMER_FEE NUMBER(16,4),
	YET_CUSTOMER_FEE NUMBER(16,4),
	CUSTOM_1 VARCHAR2 (30),
	CUSTOM_2 VARCHAR2 (30),
	CUSTOM_3 VARCHAR2 (30),
 CONSTRAINT MEGATIM_PARAMS_PK PRIMARY KEY (OBJID) 
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 60M NEXT 15M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


CREATE TABLE BILLCHKDB.MEGATIM_SERVICES (
	SERVICE_UID NUMBER(19,0) NOT NULL ,
	MEGATIM_UID NUMBER(19,0) NOT NULL ,
	PROMO_QTTY INTEGER NOT NULL ,
 CONSTRAINT MEGATIM_SERVICES_PK PRIMARY KEY (SERVICE_UID,MEGATIM_UID) 
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 60M NEXT 15M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


CREATE TABLE BILLCHKDB.MICROCELL_RATES (
	PLAN_UID NUMBER(19,0) NOT NULL ,
	EFFECTIVE_DATE DATE NOT NULL ,
	SHORT_DESC VARCHAR2 (12) NOT NULL ,
	LOADED_DATE DATE NOT NULL ,
	UMCODE INTEGER,
	DESCRIPTION VARCHAR2 (64),
	SCALEFACTOR NUMBER(16,4),
	PRICE_VALUE NUMBER(16,4),
	CUSTOM_1 VARCHAR2 (30),
	CUSTOM_2 VARCHAR2 (30),
	CUSTOM_3 VARCHAR2 (30),
 CONSTRAINT MICROCELL_RATES_PK PRIMARY KEY (PLAN_UID,EFFECTIVE_DATE,SHORT_DESC) 
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 120M NEXT 30M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


CREATE TABLE BILLCHKDB.SERVICE_RATES (
	PLAN_UID NUMBER(19,0) NOT NULL ,
	SERVICE_UID NUMBER(19,0) NOT NULL ,
	EFFECTIVE_DATE DATE NOT NULL ,
	LOADED_DATE DATE NOT NULL ,
	ACCESS_FEE NUMBER(16,4),
	ONETIME_RATE NUMBER(16,4),
	SUBSCRIPTION_RATE NUMBER(16,4),
	CUSTOM_1 VARCHAR2 (30),
	CUSTOM_2 VARCHAR2 (30),
	CUSTOM_3 VARCHAR2 (30),
 CONSTRAINT SERVICE_RATES_PK PRIMARY KEY (PLAN_UID,SERVICE_UID,EFFECTIVE_DATE) 
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 200M NEXT 50M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


CREATE TABLE BILLCHKDB.PACKAGE_RATES (
	PLAN_UID NUMBER(19,0) NOT NULL ,
	PACKAGE_UID NUMBER(19,0) NOT NULL ,
	EFFECTIVE_DATE DATE NOT NULL ,
	LOADED_DATE DATE NOT NULL ,
	ACCESS_FEE NUMBER(16,4),
	SUBSCRIPTION_RATE NUMBER(16,4),
	CUSTOM_1 VARCHAR2 (30),
	CUSTOM_2 VARCHAR2 (30),
	CUSTOM_3 VARCHAR2 (30),
 CONSTRAINT PACKAGE_RATES_PK PRIMARY KEY (PLAN_UID,PACKAGE_UID,EFFECTIVE_DATE) 
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 100M NEXT 25M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


CREATE TABLE BILLCHKDB.PRICEPLAN_RATES (
	PLAN_UID NUMBER(19,0) NOT NULL ,
	SERVICE_UID NUMBER(19,0) NOT NULL ,
	TARIFF_ZONE_UID NUMBER(19,0) NOT NULL ,
	EFFECTIVE_DATE DATE NOT NULL ,
	UMCODE VARCHAR2 (16) NOT NULL ,
	LOADED_DATE DATE NOT NULL ,
	PACKAGE_UID NUMBER(19,0),
	END_RANGE_1 NUMBER(16,0),
	AMT_RANGE_1 NUMBER(16,12),
	END_RANGE_2 NUMBER(16,0),
	AMT_RANGE_2 NUMBER(16,12),
	END_RANGE_3 NUMBER(16,0),
	AMT_RANGE_3 NUMBER(16,12),
	END_RANGE_4 NUMBER(16,0),
	AMT_RANGE_4 NUMBER(16,12),
	END_RANGE_5 NUMBER(16,0),
	AMT_RANGE_5 NUMBER(16,12),
	AMT_RANGE_MAX NUMBER(16,12)
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 60M NEXT 15M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


CREATE TABLE BILLCHKDB.TEMP_USAGE_RATES (
	TARIFF_ZONE_UID INTEGER NOT NULL ,
	PLANS_UID INTEGER NOT NULL ,
	SERVICES_UID INTEGER NOT NULL ,
	RATE_TIME_ZONE_UID INTEGER NOT NULL ,
	EFFECTIVE_DATE DATE NOT NULL ,
	INCOMING_FLAG CHAR (1) NOT NULL ,
	RATE_TYPE INTEGER NOT NULL ,
	AMOUNT NUMBER(16,4) NOT NULL,
        VS INTEGER NOT NULL
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 400M NEXT 100M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


CREATE TABLE BILLCHKDB.TEMP_MEGATIM_PARAMS (
	ACTIVATION_UID INTEGER,
	GEO_UID INTEGER,
	ONNET_ONLY CHAR (1),
	VALID_PERIOD INTEGER,
	INVOICE_DESCRIPTION VARCHAR2 (128),
	NEW_CUSTOMER_FEE NUMBER(16,4),
	YET_CUSTOMER_FEE NUMBER(16,4)
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 60M NEXT 15M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


CREATE TABLE BILLCHKDB.TEMP_MEGATIM_SERVICES (
	ACTIVATION_UID INTEGER,
	GEO_UID INTEGER,
	SERVICE_UID INTEGER,
	PROMO_QTTY INTEGER
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 60M NEXT 15M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


CREATE TABLE BILLCHKDB.CRASH_PROGRAM2 (
	COST_ID INTEGER NOT NULL ,
	CSGCODE INTEGER NOT NULL ,
	INIT_VOLUME INTEGER NOT NULL ,
	END_VOLUME INTEGER,
	PPM NUMBER(24,18) NOT NULL ,
	LOADED_DATE DATE NOT NULL ,
 CONSTRAINT CRASH_PROGRAM2_PK PRIMARY KEY (COST_ID,CSGCODE,INIT_VOLUME) 
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 60M NEXT 15M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


CREATE TABLE BILLCHKDB.NPACK_RATES (
	PLAN_UID INTEGER NOT NULL,
	EFFECTIVE_DATE DATE NOT NULL,
	INIT_RANGE INTEGER NOT NULL,
	END_RANGE INTEGER,
	AMOUNT NUMBER(24,18) NOT NULL,
	LOADED_DATE DATE NOT NULL,
 CONSTRAINT NPACK_RATES_PK PRIMARY KEY (PLAN_UID,EFFECTIVE_DATE,INIT_RANGE) 
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 60M NEXT 15M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


CREATE TABLE BILLCHKDB.MEUSONHO_RATES
(
   TARIFF_ZONE_UID decimal(19) NOT NULL,
   STATE varchar2(2) NOT NULL,
   PACKAGE_NAME varchar2(128) NOT NULL,
   PPM decimal(18) NOT NULL,
   LOADED_DATE date NOT NULL,
   CONSTRAINT MEUSONHO_RATES_PK PRIMARY KEY (TARIFF_ZONE_UID,STATE,PACKAGE_NAME)
)  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255
   STORAGE (INITIAL 60M NEXT 15M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_DATA_1;


-- criando indices
CREATE INDEX BILLCHKDB.USAGE_RATES_IDX1 ON BILLCHKDB.USAGE_RATES (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,INCOMING_FLAG,EFFECTIVE_DATE,INIT_VOLUME)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 100M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.USAGE_RATES_IDX2 ON BILLCHKDB.USAGE_RATES (LOADED_DATE)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 100M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.MEGATIM_PARAMS_IDX1 ON BILLCHKDB.MEGATIM_PARAMS (LOADED_DATE)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.MEGATIM_PARAMS_IDX2 ON BILLCHKDB.MEGATIM_PARAMS (ACTIVATION_UID,GEO_UID)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.MEGATIM_PARAMS_IDX3 ON BILLCHKDB.MEGATIM_PARAMS (INVOICE_DESCRIPTION)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.MICROCELL_RATES_IDX1 ON BILLCHKDB.MICROCELL_RATES (LOADED_DATE)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.MICROCELL_RATES_IDX2 ON BILLCHKDB.MICROCELL_RATES (SHORT_DESC)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.SERVICE_RATES_IDX1 ON BILLCHKDB.SERVICE_RATES (LOADED_DATE)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.SERVICE_RATES_IDX2 ON BILLCHKDB.SERVICE_RATES (PLAN_UID,SERVICE_UID)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.PACKAGE_RATES_IDX1 ON BILLCHKDB.PACKAGE_RATES (LOADED_DATE)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.PACKAGE_RATES_IDX2 ON BILLCHKDB.PACKAGE_RATES (PLAN_UID,PACKAGE_UID)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE UNIQUE INDEX BILLCHKDB.PRICEPLAN_RATES_PK ON BILLCHKDB.PRICEPLAN_RATES (PLAN_UID,SERVICE_UID,TARIFF_ZONE_UID,PACKAGE_UID,EFFECTIVE_DATE)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.TEMP_USAGE_RATES_IDX1 ON BILLCHKDB.TEMP_USAGE_RATES (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,EFFECTIVE_DATE,INCOMING_FLAG)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.TEMP_MEGATIM_PARAMS_IDX1 ON BILLCHKDB.TEMP_MEGATIM_PARAMS (ACTIVATION_UID,GEO_UID,ONNET_ONLY,VALID_PERIOD,INVOICE_DESCRIPTION,NEW_CUSTOMER_FEE,YET_CUSTOMER_FEE)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;

CREATE INDEX BILLCHKDB.TEMP_MEGATIM_SERVICES_IDX1 ON BILLCHKDB.TEMP_MEGATIM_SERVICES (ACTIVATION_UID,GEO_UID,SERVICE_UID,PROMO_QTTY)
   PCTFREE 10 INITRANS 2
   STORAGE (INITIAL 10M NEXT 10M MINEXTENTS 1 MAXEXTENTS UNLIMITED PCTINCREASE 0)
   TABLESPACE &V_TBS_IDX_1;


-- executando comandos de ALTER TABLE
ALTER TABLE BILLCHKDB.PACKAGE_RATES ADD CONSTRAINT PACKAGE_RATES_BY_PACKAGE_FK1 FOREIGN KEY (PACKAGE_UID) REFERENCES BILLCHKDB.QLF_PACKAGE (OBJID);
ALTER TABLE BILLCHKDB.PRICEPLAN_RATES ADD CONSTRAINT PRICEPLAN_RATES_BY_PACKAGE_FK FOREIGN KEY (PACKAGE_UID) REFERENCES BILLCHKDB.QLF_PACKAGE (OBJID);
ALTER TABLE BILLCHKDB.USAGE_RATES ADD CONSTRAINT USAGE_RATES_FK2 FOREIGN KEY (PLANS_UID) REFERENCES BILLCHKDB.QLF_PLANS (OBJID);
ALTER TABLE BILLCHKDB.MICROCELL_RATES ADD CONSTRAINT MICROCELL_PLAN_UF_FK1 FOREIGN KEY (PLAN_UID) REFERENCES BILLCHKDB.QLF_PLANS (OBJID);
ALTER TABLE BILLCHKDB.SERVICE_RATES ADD CONSTRAINT SERVICE_RATES_BY_PLAN_FK1 FOREIGN KEY (PLAN_UID) REFERENCES BILLCHKDB.QLF_PLANS (OBJID);
ALTER TABLE BILLCHKDB.PACKAGE_RATES ADD CONSTRAINT PACKAGE_RATES_BY_PLAN_FK1 FOREIGN KEY (PLAN_UID) REFERENCES BILLCHKDB.QLF_PLANS (OBJID);
ALTER TABLE BILLCHKDB.PRICEPLAN_RATES ADD CONSTRAINT PRICEPLAN_RATES_BY_PLAN_FK FOREIGN KEY (PLAN_UID) REFERENCES BILLCHKDB.QLF_PLANS (OBJID);
ALTER TABLE BILLCHKDB.USAGE_RATES ADD CONSTRAINT USAGE_RATES_FK5 FOREIGN KEY (RATE_TIME_ZONE_UID) REFERENCES BILLCHKDB.QLF_RATE_TIME_ZONE (OBJID);
ALTER TABLE BILLCHKDB.USAGE_RATES ADD CONSTRAINT USAGE_RATES_FK3 FOREIGN KEY (SERVICES_UID) REFERENCES BILLCHKDB.QLF_SERVICES (OBJID);
ALTER TABLE BILLCHKDB.MEGATIM_SERVICES ADD CONSTRAINT MEGATIM_SERVICES_BY_SERVICES_F FOREIGN KEY (SERVICE_UID) REFERENCES BILLCHKDB.QLF_SERVICES (OBJID);
ALTER TABLE BILLCHKDB.MEGATIM_PARAMS ADD CONSTRAINT MEGATIM_ACTIVATION_SERVICE_FK1 FOREIGN KEY (ACTIVATION_UID) REFERENCES BILLCHKDB.QLF_SERVICES (OBJID);
ALTER TABLE BILLCHKDB.SERVICE_RATES ADD CONSTRAINT SERVICE_RATES_BY_SERVICE_FK1 FOREIGN KEY (SERVICE_UID) REFERENCES BILLCHKDB.QLF_SERVICES (OBJID);
ALTER TABLE BILLCHKDB.PRICEPLAN_RATES ADD CONSTRAINT PRICEPLAN_RATES_BY_SERVICE_FK FOREIGN KEY (SERVICE_UID) REFERENCES BILLCHKDB.QLF_SERVICES (OBJID);
ALTER TABLE BILLCHKDB.USAGE_RATES ADD CONSTRAINT USAGE_RATES_FK1 FOREIGN KEY (TARIFF_ZONE_UID) REFERENCES BILLCHKDB.QLF_TARIFF_ZONE (OBJID);
ALTER TABLE BILLCHKDB.PRICEPLAN_RATES ADD CONSTRAINT PRICEPLAN_RATES_BY_TZ_FK FOREIGN KEY (TARIFF_ZONE_UID) REFERENCES BILLCHKDB.QLF_TARIFF_ZONE (OBJID);
ALTER TABLE BILLCHKDB.MEGATIM_SERVICES ADD CONSTRAINT MEGATIM_SERVICES_BY_MEGATIM_FK FOREIGN KEY (MEGATIM_UID) REFERENCES BILLCHKDB.MEGATIM_PARAMS (OBJID);
ALTER TABLE BILLCHKDB.MEGATIM_PARAMS ADD CONSTRAINT MEGATIM_PARAMS_GEO_FK1 FOREIGN KEY (GEO_UID) REFERENCES BILLCHKDB.BCK_GEO_DM (OBJID);
ALTER TABLE BILLCHKDB.MEUSONHO_RATES ADD CONSTRAINT MEUSONHO_TARIFF_ZONE_FK FOREIGN KEY (TARIFF_ZONE_UID) REFERENCES BILLCHKDB.QLF_TARIFF_ZONE (TARIFF_ZONE_UID);

-- criando nova sequ�ncia

CREATE SEQUENCE BILLCHKDB.LOADER_SEQ INCREMENT BY 1 START WITH 1;





