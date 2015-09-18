insert into priceplan_rates
( PLAN_UID,SERVICE_UID,TARIFF_ZONE_UID,EFFECTIVE_DATE,UMCODE,LOADED_DATE,PACKAGE_UID,
	END_RANGE_1,AMT_RANGE_1,END_RANGE_2,AMT_RANGE_2,END_RANGE_3,AMT_RANGE_3,END_RANGE_4,
	AMT_RANGE_4,END_RANGE_5,AMT_RANGE_5,AMT_RANGE_MAX )
 values ( 
   (select objid from qlf_plans where plan_name = 'Plano Nosso Modo' and state = 'RJ'),
   (select objid from qlf_services where short_desc = 'BSG1'),
   (select objid from qlf_tariff_zone where short_desc = 'GPR1'), 
   to_date('01/01/10','DD/MM/YY'), 'Bytes', sysdate, null,
   1048576, 0.000005712509, 5242880, 0.000005712509, 10485760, 0.000005712509, 41943040, 0.000005712509,0, 0, 0.0000004673);

insert into priceplan_rates
( PLAN_UID,SERVICE_UID,TARIFF_ZONE_UID,EFFECTIVE_DATE,UMCODE,LOADED_DATE,PACKAGE_UID,
	END_RANGE_1,AMT_RANGE_1,END_RANGE_2,AMT_RANGE_2,END_RANGE_3,AMT_RANGE_3,END_RANGE_4,
	AMT_RANGE_4,END_RANGE_5,AMT_RANGE_5,AMT_RANGE_MAX )
 values ( 
   (select objid from qlf_plans where plan_name = 'Plano Nosso Modo' and state = 'RJ'),
   (select objid from qlf_services where short_desc = 'BSG1'),
   (select objid from qlf_tariff_zone where short_desc = 'GPR2'), 
   to_date('01/01/10','DD/MM/YY'), 'Bytes', sysdate, null,
   102400,	0.0000390625, 512000, .00000390625, 1024000, 0.0000390625, 0, 0, 0, 0, 0.0000390625);




-- ACCOUNT5.BGH
insert into qlf_plans (objid, tmcode, description, short_desc, plan_name, state ) values ( 5, 1190, 'Plano Infinity 600-GRJ', 'IF7RJ', 'Plano Infinity 600', 'RJ');
insert into qlf_tariff_zone (objid, zone_code, description, short_desc, tariff_type) values (2, 1262, 'TIM Wap Fast', 'GPR2', 'D');

insert into priceplan_rates
( plan_uid, service_uid, tariff_zone_uid, effective_date, umcode, loaded_date, 
  end_range_1, amt_range_1, end_range_2, amt_range_2, end_range_3, amt_range_3,
  end_range_4, amt_range_4, end_range_5, amt_range_5, amt_range_max )
values 
( (select objid from qlf_plans where plan_name = 'Plano Infinity 600' and state = 'RJ'),
  (select objid from qlf_services where short_desc = 'BSG1'),
  (select objid from qlf_tariff_zone where zone_code = 1262),
  to_date('08-01-2009', 'DD-MM-YYYY'), 'Bytes', sysdate,
  102400, 0.0000390625, 512000, 0.000033203125, 1024000, 0.00001953125,
   0, 0, 0, 0, 0.00001171875 );
   