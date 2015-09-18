-- removendo novos �ndices
DROP INDEX BILLCHKDB.NPACK_TM_IDX1;
DROP INDEX BILLCHKDB.CRASH_PROGRAM2_IDX1;
DROP INDEX BILLCHKDB.PRICEPLAN_RATES_PK;
DROP INDEX BILLCHKDB.PACKAGE_RATES_IDX1;
DROP INDEX BILLCHKDB.PACKAGE_RATES_IDX2;
DROP INDEX BILLCHKDB.SERVICE_RATES_IDX1;
DROP INDEX BILLCHKDB.SERVICE_RATES_IDX2;
DROP INDEX BILLCHKDB.MICROCELL_RATES_IDX1;
DROP INDEX BILLCHKDB.MICROCELL_RATES_IDX2;
DROP INDEX BILLCHKDB.MEGATIM_PARAMS_IDX1;
DROP INDEX BILLCHKDB.MEGATIM_PARAMS_IDX2;
DROP INDEX BILLCHKDB.MEGATIM_PARAMS_IDX3;
DROP INDEX BILLCHKDB.USAGE_RATES_IDX1;
DROP INDEX BILLCHKDB.USAGE_RATES_IDX2;
DROP INDEX BILLCHKDB.TEMP_MEGATIM_SERVICES_IDX1;
DROP INDEX BILLCHKDB.TEMP_MEGATIM_PARAMS_IDX1;
DROP INDEX BILLCHKDB.TEMP_USAGE_RATES_IDX1;

-- removendo novas tabelas
DROP TABLE BILLCHKDB.NPACK_TM;
DROP TABLE BILLCHKDB.CRASH_PROGRAM2;
DROP TABLE BILLCHKDB.TEMP_MEGATIM_SERVICES;
DROP TABLE BILLCHKDB.TEMP_MEGATIM_PARAMS;
DROP TABLE BILLCHKDB.TEMP_USAGE_RATES;
DROP TABLE BILLCHKDB.PRICEPLAN_RATES;
DROP TABLE BILLCHKDB.BCK_GEO_DM;
DROP TABLE BILLCHKDB.PACKAGE_RATES;
DROP TABLE BILLCHKDB.SERVICE_RATES;
DROP TABLE BILLCHKDB.MICROCELL_RATES;
DROP TABLE BILLCHKDB.MEGATIM_SERVICES;
DROP TABLE BILLCHKDB.MEGATIM_PARAMS;
DROP TABLE BILLCHKDB.USAGE_RATES;
DROP TABLE BILLCHKDB.MEUSONHO_RATES;