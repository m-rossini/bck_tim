CREATE TABLE BILLCHKDB.PROMOTION_EXCLUSIVITY (
    OBJID       NUMBER(19)   NOT NULL,
    SHORT_DESC  VARCHAR2(32) NOT NULL,
    DESCRIPTION VARCHAR2(128) NOT NULL
)
/

ALTER TABLE BILLCHKDB.PROMOTION_EXCLUSIVITY ADD CONSTRAINT PROMOTION_EXCLUSIVITY_PK PRIMARY KEY (OBJID)
/

CREATE UNIQUE INDEX BILLCHKDB.PROMOTION_EXCLUSIVITY_UK1 ON BILLCHKDB.PROMOTION_EXCLUSIVITY (DESCRIPTION)
/

CREATE TABLE BILLCHKDB.PROMOTION_EXCLUSIVITY_REL (
    OBJID_1 NUMBER(19) NOT NULL,
    OBJID_2 NUMBER(19) NOT NULL
)
/

ALTER TABLE BILLCHKDB.PROMOTION_EXCLUSIVITY_REL ADD CONSTRAINT 
PROMOTION_EXCLUSIVITY_REL_PK PRIMARY KEY (OBJID_1, OBJID_2)
/

ALTER TABLE BILLCHKDB.PROMOTION_EXCLUSIVITY_REL ADD CONSTRAINT 
    PROMOTION_EXCLUSIVITY_REL_FK1 FOREIGN KEY (OBJID_1) REFERENCES 
    BILLCHKDB.PROMOTION_EXCLUSIVITY (OBJID)
    ON DELETE CASCADE
/

ALTER TABLE BILLCHKDB.PROMOTION_EXCLUSIVITY_REL ADD CONSTRAINT 
    PROMOTION_EXCLUSIVITY_REL_FK2 FOREIGN KEY (OBJID_2) REFERENCES 
    BILLCHKDB.PROMOTION_EXCLUSIVITY (OBJID)
    ON DELETE CASCADE
/
