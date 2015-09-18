--ACCOUNT8.BGH
INSERT INTO CONTR_SERVICES (SNCODE, CO_ID, TMCODE, SPCODE,  CS_SEQNO, CS_STAT_CHNG, CS_DATE_BILLED, CURRENCY, REC_VERSION, INITIATOR_TYPE)
	VALUES (528, 7829813, 354, 3,  1, '070522a', null, 3, 1, 1)
/

INSERT INTO MPULKTMB (TMCODE, VSCODE, VSDATE, STATUS, SPCODE, SNCODE, SUBSCRIPT, PRINTSUBSCRIND, PRINTACCESSIND, REC_VERSION)
VALUES (354, 2, to_date('19/1/2006','DD/MM/YYYY'), 'P', 3,528, 9.9, '1', '1', 1)
/



-- ACCOUNT9.BGH
INSERT INTO contr_services
VALUES
(4618518,100,123,1,123,NULL,'040613a|051213d|051214a|061128d|060720a|070602d',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,123,NULL,NULL,NULL,'A',NULL,NULL,NULL,NULL)
/
INSERT INTO contr_services
VALUES
(4618518,100,123,2,123,NULL,'040613a|051213d|051214a|061128d|060720a',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,123,NULL,123,NULL,NULL,NULL,'A',NULL,NULL,NULL,NULL)
/
INSERT INTO contr_services
VALUES
(4618518,1,1,77,1,NULL,'040613a|051213d|051214a|061128d|060720a',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,1,NULL,1,NULL,NULL,NULL,'A',NULL,NULL,NULL,NULL)
/
INSERT INTO mpulktmb
VALUES
(1,1,to_date('6/25/2006','MM/DD/YYYY'),'a',1,77,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,2806,NULL,'a','a',1)
/

CREATE TABLE MPULKPVM (
PV_COMBI_ID      NUMBER not null,
  VSCODE           NUMBER not null,
  SET_ID           NUMBER not null,
  SCCODE           NUMBER not null,
  PARAMETER_ID     NUMBER not null,
  LOWER_THRESHOLD  NUMBER not null,
  PRM_VALUE_DATE   DATE,
  PRM_VALUE_NUMBER NUMBER,
  PRM_VALUE_STRING VARCHAR2(100),
  REC_VERSION      NUMBER default (0) not null )
/


CREATE TABLE PARAMETER_VALUE (
  PRM_VALUE_ID     NUMBER not null,
  PRM_NO           NUMBER not null,
  PRM_SEQNO        NUMBER not null,
  PARENT_SEQNO     NUMBER not null,
  SIBLING_SEQNO    NUMBER not null,
  COMPLEX_SEQNO    NUMBER not null,
  VALUE_SEQNO      NUMBER not null,
  COMPLEX_LEVEL    NUMBER not null,
  PARAMETER_ID     NUMBER not null,
  DELETED_FLAG     VARCHAR2(1),
  PRM_VALUE_DATE   DATE,
  PRM_VALUE_STRING VARCHAR2(100),
  PRM_VALUE_NUMBER FLOAT,
  PRM_DESCRIPTION  VARCHAR2(100),
  PRM_VALID_FROM   DATE,
  REQUEST_ID       NUMBER,
  REC_VERSION      NUMBER default (0) not null)
/

CREATE TABLE MPULKPVB (
PV_COMBI_ID NUMBER NOT NULL,
VSCODE      NUMBER NOT NULL,
SET_ID      NUMBER NOT NULL,
DES         VARCHAR2(100),
SUBSCRIPT   NUMBER,
ACCESSFEE   NUMBER,
REC_VERSION NUMBER )
/