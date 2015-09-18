

UPDATE FU_PACK  SET PROCESS_ORDER = 4 WHERE SHORT_NAME = 'POL06';

CREATE TABLE fup_version
    (fu_pack_id                     NUMBER,
    fup_version                    NUMBER,
    currency                       NUMBER,
    valid_from                     DATE,
    work_state                     CHAR(1),
    rec_version                    NUMBER)
/

INSERT INTO fup_version VALUES (422,1,43,TO_DATE('1/1/2006','DD/MM/YYYY'),'P',1);
INSERT INTO fup_version VALUES (423,1,43,TO_DATE('1/1/2006','DD/MM/YYYY'),'P',1);
INSERT INTO fup_version VALUES (364,1,43,TO_DATE('1/1/2006','DD/MM/YYYY'),'P',1);
INSERT INTO fup_version VALUES (365,1,43,TO_DATE('1/1/2006','DD/MM/YYYY'),'P',1);
--INSERT INTO fup_version VALUES (601,1,43,TO_DATE('1/1/2006','DD/MM/YYYY'),'P',1);
INSERT INTO fup_version VALUES (57,1,43,TO_DATE('1/1/2006','DD/MM/YYYY'),'P',1);
INSERT INTO fup_version VALUES (66,1,43,TO_DATE('1/1/2006','DD/MM/YYYY'),'P',1);



CREATE TABLE fup_select_criteria (
	FU_PACK_ID   NUMBER,
    FUP_VERSION  NUMBER,
	SERVICE_CODE    NUMBER(2),
    RATE_TYPE_CODE  NUMBER(2),
    SERVICE_PACKAGE_CODE    NUMBER(2),
	TARIFF_ZONE_CODE   NUMBER(2),
    ORIGIN_CODE NUMBER(2),
    DESTINATION_CODE    NUMBER(2),
	TARIFF_TIME_CODE   NUMBER(2),
    TYPE_OF_DAY_CODE    NUMBER(2),
    TIME_INTERVAL_CODE  NUMBER(2),
	MICRO_CELL_CODE    NUMBER(2),
    MICRO_CELL_HANDLING NUMBER(2),
    MICRO_CELL_TYPE NUMBER(2),
	CALL_TYPE_CODE NUMBER(2),
    CALL_ORIGIN NUMBER(2),
    DROPPED_CALLS   NUMBER(2),
    RTX_CHARGE_TYPE NUMBER(2),
    USAGE_INDICATOR NUMBER(2),
    RATE_PLAN_CODE  NUMBER(2),
	LOGICAL_ZONE_CODE  NUMBER(2),
    CHARGEABLE_QUANTITY NUMBER(2),
    ZERO_CHARGED    NUMBER(2),
	SELECTION_TYPE	CHAR(1) DEFAULT 'I'
)
/

INSERT INTO fup_select_criteria VALUES (364,1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, NULL, 'I');
INSERT INTO fup_select_criteria VALUES (365,1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, NULL, 'I');

--INSERT INTO fup_select_criteria VALUES (601,1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, NULL, 'I');
INSERT INTO fup_select_criteria VALUES (57,1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, NULL, 'I');
INSERT INTO fup_select_criteria VALUES (66,1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, NULL, 'I');
--INSERT INTO fup_select_criteria VALUES (601,1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 2, 2, 2, 5, 5, 5, NULL, 2, NULL, 'I');
INSERT INTO fup_select_criteria VALUES (57,1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 2, 2, 2, 5, 5, 5, NULL, 2, NULL, 'I');
INSERT INTO fup_select_criteria VALUES (66,1, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 2, 2, 2, 5, 5, 5, NULL, 2, NULL, 'I');





