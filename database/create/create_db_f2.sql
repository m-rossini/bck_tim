CREATE TABLE BCK_PARM_OCC_THRESHOLD (
	OBJID 	            NUMBER(19,0)	NOT NULL, 
	CARRIER_UID         NUMBER(19,0)	NOT NULL,
	LOWER_LIMIT			NUMBER(19,4)			,
	UPPER_LIMIT			NUMBER(19,4)			,
	CUSTOM_1	    	VARCHAR2(30)			,
	CUSTOM_2            VARCHAR2(30)			,
	CUSTOM_3            VARCHAR2(30)
);


ALTER TABLE BCK_PARM_OCC_THRESHOLD ADD CONSTRAINT BCK_PARM_OCC_THRESHOLD_PK
PRIMARY KEY(OBJID);

ALTER TABLE BCK_PARM_OCC_THRESHOLD ADD CONSTRAINT BCK_PARM_OCC_THRESHOLD_FK1
FOREIGN KEY (CARRIER_UID) REFERENCES BCK_CARRIER_DM (OBJID);

CREATE UNIQUE INDEX BCK_PARM_OCC_THRESHOLD_IDX1 ON BCK_PARM_OCC_THRESHOLD (CARRIER_UID);


-- Table: BCK_TAX_TYPE
CREATE TABLE BCK_TAX_TYPE (
	OBJID 				NUMBER(19,0) 	NOT NULL, 
	TAX_CODE 			VARCHAR2(10)	NOT NULL, 
	TAX_NAME			VARCHAR2(64)	NOT NULL, 
	CUSTOM_1			VARCHAR2(30)			,
	CUSTOM_2			VARCHAR2(30)			, 
	CUSTOM_3			VARCHAR2(30)
);	
	
ALTER TABLE BCK_TAX_TYPE ADD CONSTRAINT BCK_TAX_TYPE_PK
	 PRIMARY KEY (OBJID);

-- Table: BCK_FISCAL_CODE
CREATE TABLE BCK_FISCAL_CODE (
	OBJID 				NUMBER(19,0) 	NOT NULL, 
	FISCAL_CODE			VARCHAR2(10)	NOT NULL, 
	CODE_DESCRIPTION	VARCHAR2(128)	NOT NULL, 
	CUSTOM_1			VARCHAR2(30)			,
	CUSTOM_2			VARCHAR2(30)			, 
	CUSTOM_3			VARCHAR2(30)
);	
	
ALTER TABLE BCK_FISCAL_CODE ADD CONSTRAINT BCK_FISCAL_CODE_PK
	 PRIMARY KEY (OBJID);

CREATE UNIQUE INDEX BCK_FISCAL_CODE_IX01 ON BCK_FISCAL_CODE (FISCAL_CODE);

-- Table: BCK_TAX_RATE
CREATE TABLE BCK_TAX_RATE (
	OBJID 				NUMBER(19,0) 	NOT NULL, 
	TAX_RATE			NUMBER(6,4)		NOT NULL, 
	GEO_UID				NUMBER(19,0)	NOT NULL, 
	TAX_TYPE_UID		NUMBER(19,0)	NOT NULL, 
	FISCAL_CODE_UID		NUMBER(19,0)			, 
	CUSTOM_1			VARCHAR2(30)			,
	CUSTOM_2			VARCHAR2(30)			, 
	CUSTOM_3			VARCHAR2(30)
);	

ALTER TABLE BCK_TAX_RATE ADD CONSTRAINT BCK_TAX_RATE_PK
	 PRIMARY KEY (OBJID);

ALTER TABLE BCK_TAX_RATE ADD CONSTRAINT BCK_TAX_RATE_FK1
	 FOREIGN KEY (GEO_UID) REFERENCES BCK_GEO_DM (OBJID);

ALTER TABLE BCK_TAX_RATE ADD CONSTRAINT BCK_TAX_RATE_FK2
	 FOREIGN KEY (TAX_TYPE_UID) REFERENCES BCK_TAX_TYPE (OBJID);

ALTER TABLE BCK_TAX_RATE ADD CONSTRAINT BCK_TAX_RATE_FK3
	 FOREIGN KEY (FISCAL_CODE_UID) REFERENCES BCK_FISCAL_CODE (OBJID);

CREATE UNIQUE INDEX BCK_TAX_RATE_IX01 ON BCK_TAX_RATE (GEO_UID, TAX_TYPE_UID, FISCAL_CODE_UID);