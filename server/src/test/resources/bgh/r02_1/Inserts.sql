SELECT A.disc_description, A.service_shdes, B.range_name, C.lower_limit, C.upper_limit, C.discount_rate
    FROM PROG_DISCOUNT_DESC A, PROG_DISCOUNT B, PROG_DISCOUNT_RATES C
    WHERE A.disc_uid = B.objid
        AND C.disc_uid = B.objid
        AND A.disc_description =  'LDN Econômica 41 TIM'
        order by C.objid


/* Caso 1 */
insert into PROG_DISCOUNT values (1, 'Tabela1');

insert into PROG_DISCOUNT_RATES values (1, 1, 0, 0.25, 2);
insert into PROG_DISCOUNT_RATES values (2, 1, 0.26, 0.4, 3);
insert into PROG_DISCOUNT_RATES values (3, 1, 0.41, 200, 5);
insert into PROG_DISCOUNT_RATES values (10, 1, 201, 500, 20);
insert into PROG_DISCOUNT_RATES values (11, 1, 501, 8499, 5);

insert into PROG_DISCOUNT_DESC values (1, 1, 'LDN Econômica 41 TIM', 'DPN');


/* Caso 2 */
insert into PROG_DISCOUNT values (2, 'Tabela2');

insert into PROG_DISCOUNT_RATES values (4, 2, 0, 5, 2);
insert into PROG_DISCOUNT_RATES values (5, 2, 6, 8, 5);
insert into PROG_DISCOUNT_RATES values (6, 2, 9, 120, 10);

insert into PROG_DISCOUNT_DESC values (2, 2, 'LDI Econômica 41 TIM', 'DPI');


/* Caso 3 */
insert into PROG_DISCOUNT values (3, 'Tabela3');

insert into PROG_DISCOUNT_DESC values(3,3,'Desconto Assinatura Nosso Modo', '');

insert into PROG_DISCOUNT_RATES values(7, 3, 0, 12, 10);
insert into PROG_DISCOUNT_RATES values(8, 3, 13, 50, 10);
insert into PROG_DISCOUNT_RATES values(9, 3, 12, 13, 5);

/* Caso 9 */
insert into prog_discount values (4, 'Tabela4');

insert into prog_discount_desc values (4, 4, 'Desconto Assinatura Nosso Modo 1', '');

insert into prog_discount_rates values (13, 3, 51, 170, 20);
insert into prog_discount_rates values (12, 4, 0, 20, 5);
insert into prog_discount_rates values (14, 2, 121, 500, 20);
insert into prog_discount_rates values (15, 1, 8500, null, 50);

/* Caso 10 */
insert into prog_discount_desc values (5, 2, 'LDN Econômica 41 TIM', 'DPN', 'GO');
insert into prog_discount_desc values (6, 1, 'LDI Econômica 41 TIM', 'DPI', 'GO');

/* Caso 12 */
insert into prog_discount values (62, 'DESC_ASS_NOSO_MODO');

insert into prog_discount_rates values (121,62,5,9,0);
insert into prog_discount_rates values (122,62,10,19,5);
insert into prog_discount_rates values (123,62,20,49,10);
insert into prog_discount_rates values (124,62,50,149,15);
insert into prog_discount_rates values (125,62,150,249,20);
insert into prog_discount_rates values (126,62,250,999999.99,25);

insert into prog_discount_desc values (62, 62, 'Desconto Assinatura Nosso Modo 2', '', '');

insert into prog_discount_plans ( select 62, objid from qlf_plans where plan_name = 'Plano Nosso Modo' and state = 'ES' );


/* Caso 13 */
insert into prog_discount values (84, 'DES_TIM_EMP_NACIONAL');

insert into prog_discount_rates values (127,84,1,250,0);
insert into prog_discount_rates values (128,84,251,750,10);
insert into prog_discount_rates values (129,84,751,1499,20);

insert into prog_discount_desc values (84, 84, 'Desc. Ass. TIM Emp. Nacional', '', '');

insert into prog_discount_plans ( select 84, objid from qlf_plans where plan_name = 'TIM Empresa Nacional' );
