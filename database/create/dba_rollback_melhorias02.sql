
ALTER TABLE BILLCHKDB.QLF_USAGE_RATES ADD RATE NUMBER(16,4);
	
ALTER TABLE BILLCHKDB.QLF_USAGE_RATES DROP COLUMN INIT_RATE;
	
ALTER TABLE BILLCHKDB.QLF_USAGE_RATES DROP COLUMN STEP_RATE;	