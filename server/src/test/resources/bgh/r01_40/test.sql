-- custCode for tests
create table pacote_cliente_custcode ( cust_code varchar2(256) );
insert into pacote_cliente_custcode values ('6A.161058');
insert into pacote_cliente_custcode values ('6B.161058');
insert into pacote_cliente_custcode values ('6C.161058');
insert into pacote_cliente_custcode values ('6D.161058');

-- crash program workbooks
create table fup_rate_plan ( fu_pack_id integer, tmcode integer );
insert into fup_rate_plan ( select 1, tmcode from qlf_plans where plan_name = 'Plano Nosso Modo' );

create table fu_pack_id ( fu_pack_id integer, sncode integer, long_name varchar2(256) );
insert into fu_pack_id ( select 1, svc_code, description from qlf_services where short_desc like '%3FSS1%' );
insert into fu_pack_id ( select 1, svc_code, description from qlf_services where short_desc like '%3FSS2%' );
insert into fu_pack_id ( select 1, svc_code, description from qlf_services where short_desc like '%3FSS3%' );

create table crash_service_item ( csgcode integer, svccode integer );
insert into crash_service_item ( select 1, svc_code from qlf_services where short_desc like '%3FSS1%' );
insert into crash_service_item ( select 1, svc_code from qlf_services where short_desc like '%3FSS2%' );
insert into crash_service_item ( select 1, svc_code from qlf_services where short_desc like '%3FSS3%' );


create table costcenter ( cost_id integer, cost_code varchar2(10), description varchar2(64));
insert into costcenter values (1	,'20AM', 'AM_TIM CELULAR S.A.');
insert into costcenter values (2	,'20AP', 'AP_TIM CELULAR S.A.');
insert into costcenter values (3	,'20ES', 'ES_TIM CELULAR S.A.');
insert into costcenter values (4	,'20MA', 'MA_TIM CELULAR S.A.');
insert into costcenter values (5	,'20PA', 'PA_TIM CELULAR S.A.');
insert into costcenter values (6	,'20RJ', 'RJ_TIM CELULAR S.A.');
insert into costcenter values (7	,'20RR', 'RR_TIM CELULAR S.A.');
insert into costcenter values (8	,'40AC', 'AC_TIM CELULAR S.A.');
insert into costcenter values (9	,'40DF', 'DF_TIM CELULAR S.A.');
insert into costcenter values (10	,'40GO', 'GO_TIM CELULAR S.A.');
insert into costcenter values (11	,'40MT', 'MT_TIM CELULAR S.A.');
insert into costcenter values (12	,'40MS', 'MS_TIM CELULAR S.A.');
insert into costcenter values (13	,'40RS', 'RS_TIM CELULAR S.A.');
insert into costcenter values (14	,'40RO', 'RO_TIM CELULAR S.A.');
insert into costcenter values (15	,'40TO', 'TO_TIM CELULAR S.A.');
insert into costcenter values (16	,'30SP', 'SP_TIM CELULAR S.A.');
insert into costcenter values (17	,'70AL', 'AL_TIM CELULAR S.A.');
insert into costcenter values (18	,'70CE', 'CE_TIM CELULAR S.A.');
insert into costcenter values (19	,'50MG', 'MG_TIM CELULAR S.A.');
insert into costcenter values (20	,'70PB', 'PB_TIM CELULAR S.A.');
insert into costcenter values (21	,'70PI', 'PI_TIM CELULAR S.A.');
insert into costcenter values (22	,'70RN', 'RN_TIM CELULAR S.A.');
insert into costcenter values (23	,'60SC', 'SC_TIM CELULAR S.A.');
insert into costcenter values (24	,'50SE', 'SE_TIM CELULAR S.A.');
insert into costcenter values (25	,'50BA', 'BA_TIM CELULAR S.A.');
insert into costcenter values (26	,'60PR', 'PR_TIM CELULAR S.A.');
insert into costcenter values (27	,'70PE', 'PE_TIM CELULAR S.A.');
insert into costcenter values (28	,'60PL', 'PL_TIM CELULAR S.A.');
insert into costcenter values (29	,'40LO', 'LO_TIM CELULAR S.A.');


create table crash_program2 ( cost_id integer, csgcode integer, init_volume integer, end_volume integer, ppm number(20,18), loaded_date date );
insert into crash_program2 values (6, 1, 0, 500, 0.0035, sysdate);
insert into crash_program2 values (6, 1, 500, 1000, 0.0040, sysdate);
insert into crash_program2 values (6, 1, 1000, 9999, 0.0045, sysdate);


create table crash_service_group ( csgcode integer, des varchar2(128));
insert into crash_service_group (csgcode, des) values (1	,'GRUPO - Pacote de Minutos- Nosso Modo');
insert into crash_service_group (csgcode, des) values (2	,'GRUPO - Pacote Minutos Compart. Dpto.');
insert into crash_service_group (csgcode, des) values (3	,'GRUPO - Pacote Minutos Compartilhado');
insert into crash_service_group (csgcode, des) values (4	,'GRUPO - Pacote de Minutos - 1');
insert into crash_service_group (csgcode, des) values (5	,'GRUPO - Pacote de Minutos - 2');
insert into crash_service_group (csgcode, des) values (6	,'GRUPO - Pacote de Minutos - 3');
insert into crash_service_group (csgcode, des) values (7	,'GRUPO - Pacote de Minutos - 4');
insert into crash_service_group (csgcode, des) values (8	,'GRUPO - Pacote de Minutos - 5');
insert into crash_service_group (csgcode, des) values (9	,'GRUPO - Pacote de Minutos - 6');
insert into crash_service_group (csgcode, des) values (10	,'GRUPO - Pacote de Minutos - 7');
insert into crash_service_group (csgcode, des) values (11	,'GRUPO - Pcte Promocional - Minutos');
insert into crash_service_group (csgcode, des) values (12	,'GRUPO - Pcte Promocional - Torpedos');
insert into crash_service_group (csgcode, des) values (13	,'GRUPO - Pacote Minutos Compart. Dpto.1');
insert into crash_service_group (csgcode, des) values (14	,'GRUPO - Pacote Minutos Compart. Dpto.2');
insert into crash_service_group (csgcode, des) values (15	,'GRUPO - Pacote Minutos Compart. Dpto.3');
insert into crash_service_group (csgcode, des) values (16	,'GRUPO - Pacote Minutos Compart. Dpto.4');
insert into crash_service_group (csgcode, des) values (17	,'GRUPO - Pacote Minutos Compart. Dpto.5');
insert into crash_service_group (csgcode, des) values (18	,'GRUPO - Pacote Minutos Compart. Dpto.6');
insert into crash_service_group (csgcode, des) values (19	,'GRUPO - Pacote Minutos Compart. Dpto.7');
insert into crash_service_group (csgcode, des) values (20	,'GRUPO - Pacote Minutos Compart. Dpto.8');
insert into crash_service_group (csgcode, des) values (21	,'GRUPO - Pacote Minutos Compart. Dpto.9');
insert into crash_service_group (csgcode, des) values (22	,'GRUPO - Pacote Minutos Compartilhado 1');
insert into crash_service_group (csgcode, des) values (23	,'GRUPO - Pacote Minutos Compartilhado 2');
insert into crash_service_group (csgcode, des) values (24	,'GRUPO - Pacote Minutos Compartilhado 3');
insert into crash_service_group (csgcode, des) values (25	,'GRUPO - Pacote Minutos Compartilhado 4');
insert into crash_service_group (csgcode, des) values (26	,'GRUPO - Pacote Minutos Compartilhado 5');
insert into crash_service_group (csgcode, des) values (27	,'GRUPO - Pacote Minutos Compartilhado 6');
insert into crash_service_group (csgcode, des) values (28	,'GRUPO - Pacote Minutos Compartilhado 7');
insert into crash_service_group (csgcode, des) values (29	,'GRUPO - Pacote Minutos Compartilhado 8');
insert into crash_service_group (csgcode, des) values (30	,'GRUPO - Pacote Minutos Compartilhado 9');
insert into crash_service_group (csgcode, des) values (31	,'GRUPO - Pacote GPRS/Edge Compartilhado');
insert into crash_service_group (csgcode, des) values (32	,'GRUPO - Pacote TIM w-VPN Compartilhado');
insert into crash_service_group (csgcode, des) values (33	,'GRUPO - Pacote de Minutos - Alternativo Nosso Modo');