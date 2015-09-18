

DROP TABLE REQUEST_CLEANUP_TMP;

DROP PROCEDURE CLEANUP_BILLCHECKOUT;
DROP PROCEDURE CLEANUP_WEB_REQUEST;
DROP PROCEDURE CLEANUP_CONSEQUENCES;

DROP TABLE QLF_SERVICES_PLANS;
DROP TABLE QLF_TARIFFZONE_USAGEGROUP;
DROP TABLE USAGE_RATES;
DROP TABLE PRICEPLAN_RATES;

DROP TABLE QLF_SERVICES;	
DROP TABLE QLF_PLANS;
DROP TABLE QLF_TARIFF_ZONE;
DROP TABLE QLF_USAGE_GROUP;
DROP TABLE QLF_RATE_TIME_ZONE;



DROP VIEW BCK_CYCLE_DM_VIEW;

DROP TABLE BCK_CONSEQUENCE;
DROP TABLE BCK_CONSEQUENCE_ATTR;

DROP SEQUENCE BCK_CONSEQUENCE_UID;
DROP SEQUENCE BCK_DIMENSIONS_UID;

DROP TABLE BCK_INVOICE_FACT;
DROP TABLE BCK_CONTRACT_TOTALS_FACT;

DROP TABLE BCK_CONTRACT_DM;
DROP TABLE BCK_RATEPLAN_DM;

DROP TABLE BCK_PARM_RECEIPT_THRESHOLDS;
DROP TABLE BCK_PARM_CYCLE_DATES;

DROP TABLE BCK_CARRIER_DATA;

DROP TABLE BCK_ACCOUNT_DM;
DROP TABLE BCK_CARRIER_DM;
DROP TABLE BCK_CYCLE_DM;
DROP TABLE BCK_GEO_DM;
DROP TABLE BCK_TIME_DM;
DROP TABLE BCK_RULE;
DROP TABLE BCK_RULE_TYPE;



drop table AUT_GROUP_PERM_ASGM;
drop table AUT_USER_GROUP_ASGM;
drop table AUT_USER;
drop table AUT_GROUP;
drop TABLE AUT_PERM;


drop sequence WEB_REQUEST_SEQUENCE;
drop sequence WEB_NOTIFICATION_SEQUENCE;
drop sequence BUNDLEFILE_SEQUENCE;

drop table WEB_REQUEST_REQUESTS;
drop table WEB_REQUEST_INFO;
drop table WEB_NOTIFICATION;
drop table WEB_BUNDLEFILE;
drop table WEB_REQUEST;


drop sequence USER_SEQUENCE;

drop table WEB_USER;


drop sequence PROC_REQUEST_SEQUENCE;
drop sequence PROC_TRAIL_SEQUENCE;
drop sequence OUTFILE_SEQUENCE;


drop table PROC_REQUEST_INFILE;
drop table PROC_REQUEST_INFO;

drop table PROC_OUTFILE_ATTRS;
drop table PROC_REQUEST_OUTFILE;

drop table PROC_REQUEST_TRAIL;
drop table PROC_REQUEST;
