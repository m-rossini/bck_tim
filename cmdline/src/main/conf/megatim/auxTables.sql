CREATE TABLE TEMP_MEGATIM_PARAMS (
	ACTIVATION_UID 		INTEGER 		NOT NULL,
	GEO_UID				INTEGER 		NOT NULL,
	ONNET_ONLY			CHAR(1)			NOT NULL,
	VALID_PERIOD		INTEGER 		NOT NULL,
	INVOICE_DESCRIPTION VARCHAR2(64) 	NOT NULL,
	NEW_CUSTOMER_FEE    NUMBER(16,4)	NOT NULL,
	YET_CUSTOMER_FEE    NUMBER(16,4)	NOT NULL
);

CREATE TABLE TEMP_MEGATIM_SERVICES (
	ACTIVATION_UID 		INTEGER 	NOT NULL,
	GEO_UID				INTEGER 	NOT NULL,
	SERVICE_UID			INTEGER 	NOT NULL,
	PROMO_QTTY			INTEGER 	NOT NULL
);

CREATE SEQUENCE LOADER_SEQ START WITH 1 INCREMENT BY 1;