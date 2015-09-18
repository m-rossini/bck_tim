--------INSERINDO SOMENTE REGISTROS PARA CHAMADAS ORIGINADAS (incoming_Flag=='N'), pois é a validação desta regra

-----------------------------------INICIO ACCOUNT1.BGH----------------------------------
/*
51110000^14/09/06^18:29:43^SP AREA 11^SP FIXO - AREA 11^3871-9635^DI^^00:02:30^00:02:26^1.78^TS11^VCFC^H^9^C^0.24^P^U^^0^0^1.78^0.00
51110010^ICMS^25.00
51110020^4577933^23^DCH01^TIM Meia Tarifa 20^1^1^00:00:18^00:00:18^Sec^27000^0.24
51110000^19/09/06^15:08:21^SP AREA 11^SP FIXO - AREA 11^3829-8100^DI^^00:10:30^00:10:28^5.62^TS11^VCFC^H^9^C^0.00^N^U^^0^0^5.62^0.00
51110010^ICMS^25.00
51120000^0000:25:42^0000:24:53^7.40^7.40^9^7.55^0.00
*/

insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCFC'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='SP'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'DI'),
   to_date('8-DEZ-2004','DD-MON-RRRR'),'N', 0, 9999, 30, 0.45,
   to_date(sysdate,'DD-MON-RRRR')
)
/

COMMIT;
-----------------------------------INICIO ACCOUNT3.BGH----------------------------------
--Variaveis
--b.SHORT_DESC = ? (VCTS, VCMS, VCFS)				-- zona tarifaria
--and c.PLAN_NAME = ?  (TIM Meia Tarifa)			-- plano do cliente  
--and f.SHORT_DESC = ? 	(TS11)					-- cod. serviço  
--and d.GEO_STATE = ? 	(SC)					-- estado  
--and e.BILL_FILE_CODE = ?	(DI,N,FDS,NO)		-- modulação - tariffTimeIndicator


--PARA CONJUNTO A
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCTS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='SC'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'DI'),
   to_date('8-DEZ-2004','DD-MON-RRRR'),'N', 0, 9999, 30, 0.44,
   to_date(sysdate,'DD-MON-RRRR')
)
/
--PARA CONJUNTO B
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCTS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='SC'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'NO'),
   to_date('8-DEZ-2004','DD-MON-RRRR'),'N', 0, 9999, 30, 0.44,
   to_date(sysdate,'DD-MON-RRRR')
)
/
--PARA CONJUNTO C
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCMS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='SC'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'N' and ttcode=4), --ttcode=4 apenas para ter um
   to_date('8-DEZ-2004','DD-MON-RRRR'),'N', 0, 9999, 30, 0.44,
   to_date(sysdate,'DD-MON-RRRR')
)
/
--PARA CONJUNTO D
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCMS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='SC'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'FDS' and ttcode=26), --ttcode=26 apenas para ter um
   to_date('8-DEZ-2004','DD-MON-RRRR'),'N', 0, 9999, 30, 0.44,
   to_date(sysdate,'DD-MON-RRRR')
)
/
--PARA CONJUNTO E
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCMS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='SC'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'DI'),
   to_date('8-DEZ-2004','DD-MON-RRRR'),'N', 0, 9999, 30, 0.44,
   to_date(sysdate,'DD-MON-RRRR')
)
/
--PARA CONJUNTO F
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCFS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='SC'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'FDS' and ttcode=26), --ttcode=26 apenas para ter um
   to_date('8-DEZ-2004','DD-MON-RRRR'),'N', 0, 9999, 30, 0.44,
   to_date(sysdate,'DD-MON-RRRR')
)
/

COMMIT;

-----------------------------------INICIO ACCOUNT4.BGH----------------------------------
/*
  	51110000^02/02/10^12:10:05^SP AREA 11^SP FIXO - AREA 11^2693-6265^DI^^00:02:36^00:02:33^0.60^TS11^VCFC^H^9^C^1.97^P^U^^0^0^0.60^0.00^724031100C707
	51110010^ICMS^25.00
	51110020^51776047^19^DCH01^TIM Meia Tarifa 20^1^1^00:01:58^00:02:00^Sec^224000^1.97
	51110000^02/02/10^17:50:02^SP AREA 11^SP FIXO - AREA 11^2693-6265^DI^^00:01:18^00:01:17^0.72^TS11^VCFC^H^9^C^0.00^N^U^^0^0^0.72^0.00^724031100C707
	51110010^ICMS^25.00
  
 */
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCFC'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='SP'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'DI'),
   to_date('1-FEV-2010','DD-MON-RRRR'),'N', 0, 9999, 30, 0.56,
   to_date(sysdate,'DD-MON-RRRR')
)
/

COMMIT;
-----------------------------------INICIO ACCOUNT5.BGH----------------------------------
--Tarifas a aplicar, onde incoming_flag sempre 'N', onde valida-se chamada originada.

--DI - TS11 - (VCFS -> 0,50 POR 30SEGS.)
--DI - TS11 - (VCTS -> 0,50 POR 30SEGS.)
--DI - TS11 - (VCMS -> 0,52 POR 30SEGS.)

--NO -  TS11 - (VCFS -> 0,50 POR 30 SEGS.)

--FDS - TS11 - (VCTS -> 0,50 POR 30 SEGS.)
--FDS - TS11 - (VCFS -> 0,50 POR 30 SEGS.)
--FDS - TS11 - (VCMS -> 0,52 POR 30 SEGS.)

insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCFS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='PR'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'DI'),
   to_date('1-JAN-2010','DD-MON-RRRR'),'N', 0, 9999, 30, 0.50,
   to_date(sysdate,'DD-MON-RRRR')
)
/
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCTS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='PR'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'DI'),
   to_date('1-JAN-2010','DD-MON-RRRR'),'N', 0, 9999, 30, 0.50,
   to_date(sysdate,'DD-MON-RRRR')
)
/
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCMS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='PR'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'DI'),
   to_date('1-JAN-2010','DD-MON-RRRR'),'N', 0, 9999, 30, 0.52,
   to_date(sysdate,'DD-MON-RRRR')
)
/
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCTS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='PR'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'FDS' and ttcode=26), --ttcode=26 apenas para ter um
   to_date('1-JAN-2010','DD-MON-RRRR'),'N', 0, 9999, 30, 0.50,
   to_date(sysdate,'DD-MON-RRRR')
)
/
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCFS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='PR'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'FDS' and ttcode=26), --ttcode=26 apenas para ter um
   to_date('1-JAN-2010','DD-MON-RRRR'),'N', 0, 9999, 30, 0.50,
   to_date(sysdate,'DD-MON-RRRR')
)
/
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCMS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='PR'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'FDS' and ttcode=26), --ttcode=26 apenas para ter um
   to_date('1-JAN-2010','DD-MON-RRRR'),'N', 0, 9999, 30, 0.52,
   to_date(sysdate,'DD-MON-RRRR')
)
/
insert into usage_rates (TARIFF_ZONE_UID,PLANS_UID,SERVICES_UID,RATE_TIME_ZONE_UID,
						 EFFECTIVE_DATE,INCOMING_FLAG,INIT_VOLUME,END_VOLUME,STEP_VOLUME,
						 STEP_COST,LOADED_DATE)
values
( (select objid from qlf_tariff_zone where short_desc='VCFS'),
  (select objid from qlf_plans where plan_name='TIM Meia Tarifa' and state='PR'),
  (select objid from qlf_services where short_desc = 'TS11'),
  (select objid from qlf_rate_time_zone where BILL_FILE_CODE = 'NO'),
   to_date('1-JAN-2010','DD-MON-RRRR'),'N', 0, 9999, 30, 0.50,
   to_date(sysdate,'DD-MON-RRRR')
)
/

COMMIT;
-----------------------------------FIM ACCOUNT5.BGH----------------------------------
