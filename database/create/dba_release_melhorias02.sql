
ALTER TABLE BILLCHKDB.QLF_USAGE_RATES DROP COLUMN RATE;
	
ALTER TABLE BILLCHKDB.QLF_USAGE_RATES ADD INIT_RATE NUMBER(16,4);
	
ALTER TABLE BILLCHKDB.QLF_USAGE_RATES ADD STEP_RATE NUMBER(16,4);

insert into bck_rule values(66, 'R30-1', 'Aplicação Pct Ilimitado de Dados', null, 1, null, null, null);
insert into bck_rule values(67, 'R31-1', 'Validação de Chamadas INFINITY', null, 1, null, null, null);

commit;	