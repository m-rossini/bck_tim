
CREATE TABLE BILLCHKDB.PROG_DISCOUNT_PLANS (
	DISCOUNT_UID NUMBER(19) NOT NULL,
	PLAN_UID NUMBER (19) NOT NULL
)
/

ALTER TABLE BILLCHKDB.PROG_DISCOUNT_PLANS ADD CONSTRAINT 
	PROG_DISCOUNT_PLANS_PK PRIMARY KEY ( DISCOUNT_UID, PLAN_UID )
/

ALTER TABLE BILLCHKDB.PROG_DISCOUNT_PLANS ADD CONSTRAINT 
	PROG_DISCOUNT_PLANS_FK1 FOREIGN KEY ( DISCOUNT_UID ) REFERENCES BILLCHKDB.PROG_DISCOUNT_DESC ( OBJID )
/

ALTER TABLE BILLCHKDB.PROG_DISCOUNT_PLANS ADD CONSTRAINT 
	PROG_DISCOUNT_PLANS_FK2 FOREIGN KEY ( PLAN_UID ) REFERENCES BILLCHKDB.QLF_PLANS ( OBJID )
/


CREATE OR REPLACE FORCE VIEW BILLCHKDB.BCK_CYCLE_DM_VIEW 
	(OBJID, CYCLE_CODE, CUT_YEAR,CUT_DATE, CUT_DATE_ORDER, ISSUE_DATE, ISSUE_DATE_ORDER, DUE_DATE, DUE_DATE_ORDER) AS 
	( SELECT OBJID, CYCLE_CODE, to_char(CUT_DATE, 'YYYY') as CUT_YEAR, to_char(CUT_DATE, 'DD/MM/YY') as CUT_DATE, CUT_DATE as CUT_DATE_ORDER, 
	         to_char(ISSUE_DATE) as ISSUE_DATE, ISSUE_DATE as ISSUE_DATE_ORDER,
	         to_char(DUE_DATE, 'DD/MM/YY') as DUE_DATE, DUE_DATE as DUE_DATE_ORDER
  	FROM BILLCHKDB.BCK_CYCLE_DM )
/

ALTER TABLE BILLCHKDB.PROMOTION_EXCLUSIVITY_REL DROP CONSTRAINT 
PROMOTION_EXCLUSIVITY_REL_FK1
/

ALTER TABLE BILLCHKDB.PROMOTION_EXCLUSIVITY_REL ADD CONSTRAINT 
    PROMOTION_EXCLUSIVITY_REL_FK1 FOREIGN KEY (OBJID_1) 
    REFERENCES BILLCHKDB.PROMOTION_EXCLUSIVITY (OBJID) 
    ON DELETE CASCADE
/


ALTER TABLE BILLCHKDB.PROMOTION_EXCLUSIVITY_REL DROP CONSTRAINT 
PROMOTION_EXCLUSIVITY_REL_FK2
/

ALTER TABLE BILLCHKDB.PROMOTION_EXCLUSIVITY_REL ADD CONSTRAINT 
    PROMOTION_EXCLUSIVITY_REL_FK2 FOREIGN KEY (OBJID_2) 
		REFERENCES BILLCHKDB.PROMOTION_EXCLUSIVITY (OBJID)
    ON DELETE CASCADE
/

CREATE SEQUENCE progressive_promotion_uid
    MINVALUE 1
    MAXVALUE 999999999999999999999999999
    START WITH 1
    INCREMENT BY 1
/