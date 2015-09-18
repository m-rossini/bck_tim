/* mpulkfxo */

insert into mpulkfxo
values (12265730,0, '724','0055',1,1,51);

insert into mpulkfxo
values (12265223,0, '724','*',1,1,51);


/* contr_services */

insert into contr_services
values (679815,381,1,1,4,NULL,'060527a','',to_date('2007-07-19','YYYY-MM-DD'),79017916,1,'',0,'',NULL,'',0,'','',0,NULL,'',NULL
,NULL,NULL,to_date('2003-10-11 14:46:10', 'YYYY-MM-DD HH24:MI:SS'),'','','','',NULL,43,NULL,16,'',-206,'DS010RRM','C','',NULL,'',NULL);

insert into contr_services values(679815,381,1,3,4,NULL,'060527a','',to_date('2007-07-19','YYYY-MM-DD'),79017916,3,'',0,'',NULL
,'',0,'','',0,NULL,'',NULL,NULL,NULL,to_date('2003-10-11 14:46:10', 'YYYY-MM-DD HH24:MI:SS'),'','','','',NULL,43,NULL,16,'',-206,'DS010RRM','C','',NULL,'',NULL);

insert into contr_services
values (679815,381,1,567,4,NULL,'060527a','',to_date('2007-07-19','YYYY-MM-DD'),79017916,1,'',0,'',NULL,'',0,'','',0,NULL,'',NULL
,NULL,NULL,to_date('2003-10-11 14:46:10', 'YYYY-MM-DD HH24:MI:SS'),'','','','',NULL,43,NULL,16,'',-206,'DS010RRM','C','',NULL,'',NULL);

insert into contr_services
values (6344777,354,1,567,4,NULL,'070701a070704d','',to_date('2007-07-19','YYYY-MM-DD'),79017916,1,'',0,'',NULL,'',0,'','',0,NULL,'',NULL
,NULL,NULL,to_date('2003-10-11 14:46:10', 'YYYY-MM-DD HH24:MI:SS'),'','','','',NULL,43,NULL,16,'',-206,'DS010RRM','C','',NULL,'',NULL);


/* mpufftab */

insert into mpufftab values(1212392,679815,'Namorados 2005 TIM','NAM5',1E-5,'','W',0,'Y',18000,0,'Y',1,'1,278','','','','','',NULL,'N','','',NULL);
insert into mpufftab values(8481831,679815,'7 Centavos','NAT06',NULL,'','W',1,'N',NULL,NULL,'N',10,'1,278','2312,2313,2314,2315','','','','',8,'Y','N','2',0.07);
insert into mpufftab values(12264716,679815,'Pais 2007 - 7 Centav','PAI07',NULL,'','W',1,'N',NULL,NULL,'N',10,'1,278','2312,2313,2314,2315','','','','',8,'Y','N','2',0.07);
insert into mpufftab values(12265223,679815,'Pais 2007 - 7 Centav','PLD07',NULL,'','W',1,'Y',1200,0,'Y',10,'1,278','2586,2587,2591,2595,2599','','','','',8,'N','N','2',0.07);
insert into mpufftab values(12265730,679815,'Pais 2007 - 7 Centav','PTP07',NULL,'','W',1,'N',NULL,NULL,'N',10,'3','4','','','','',3,'Y','N','1',0.07);
insert into mpufftab(FFCODE,CO_ID,DES,SHDES,SCALEFACTOR,UNSPECIFIC_ORIGIN,CLOSURE,REC_VERSION,RESET_FLAG,USER_LIMIT,USED_COUNTER,LIMITED_FLAG,PRIORITY,SNCODE,ZNCODE,TTCODE,LZCODE,SPCODE,TDCODE,UMCODE,FREEUNITS_IND,PRICEPLAN_IND,PRICEMECH_TYPE,PRICE_VALUE)
values(6521121,'7049218','100000','LD41',1,'','W',1,'N','',0,'','',1,'1,278','null','null','null','null','','N','',2,0.0065);



-- For ACCOUNT12.BGH
INSERT INTO mpufftab
VALUES
(10345492,8816768,'41 SEM LIMITES','PALD',NULL,NULL,'W',1,'N',NULL,NULL,'N',20,'1','1358,1361,1362,1365',NULL,NULL,NULL,NULL,1,'Y','N','2',',008')
/
INSERT INTO mpufftab
VALUES
(10376673,8816768,'41 SEM LIMITES','PALD',NULL,NULL,'W',1,'N',NULL,NULL,'N',20,'1','1358,1361,1362,1365',NULL,NULL,NULL,NULL,1,'Y','N','2',',006')
/
INSERT INTO mpufftab
VALUES
(11143453,8816768,'41 SEM LIMITES','PALD',NULL,NULL,'W',1,'N',NULL,NULL,'N',20,'1','1358,1361,1362,1365',NULL,NULL,NULL,NULL,1,'Y','N','2',',008')
/
INSERT INTO mpulkfxo VALUES(11143453,0,'724','0055',1,1,51)
/


-- FOR ACCOUNT13.BGH
INSERT INTO mpufftab
VALUES
(13048624,6344777,'Pais 2007 - 7 Centav','PTP07',NULL,NULL,'W',1,'N',NULL,NULL,'N',10,'3','4',NULL,NULL,NULL,NULL,3,'Y','N','1',',07')
/
INSERT INTO mpulkfxo
VALUES
(13048624,0,'724','0055',1,1,51)
/


-- FOR ACCOUNT15.BGH
insert into mpufftab (FFCODE, CO_ID, DES, SHDES, SCALEFACTOR, CLOSURE, REC_VERSION, USED_COUNTER, PRIORITY, SNCODE, FREEUNITS_IND)
values (13568, '10279314', 'P/ Toda Vida TIM-TIM', 'MVT', 15, 'W', 3, 0, 1, '1,278', 'N');

insert into DWPROMO_MC (COD_PROMOCAO_OLTP,SHDES)
values (334, 'MVT');
insert into DWPROMO_SN (COD_PROMOCAO_OLTP,COD_SERVICO_OLTP)
values (334, 703);
insert into DWPROMO_SN (COD_PROMOCAO_OLTP,COD_SERVICO_OLTP)
values (334, 704);

insert into contr_services values('10279314','451','1','1','1',null,'071016a',null,null,'80766587','1',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','1','2','1',null,'071016a',null,null,'80766587','2',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','1','3','1',null,'071016a',null,null,'80766587','3',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','1','4','1',null,'071016a',null,null,'80766587','4',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','2','5','1',null,'071016a',null,null,'80766587','5',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','3','6','1',null,'071016a',null,null,'80766587','6',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','3','7','1',null,'071016a',null,null,'80766587','7',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','3','8','1',null,'071016a',null,null,'80766587','8',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','3','9','1',null,'071016a',null,null,'80766587','9',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,'63060287','43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','3','13','1',null,'071016a',null,null,'80766587','13',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','1','22','1',null,'071016a',null,null,null,'22',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','3','53','1',null,'071016a',null,null,'80766587','53',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,'63060288','43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','10','62','1',null,'071016a',null,null,null,'62',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','3','76','1',null,'071016a',null,null,null,null,null,'0',null,null,null,'0',null,null,'0',null,'V',null,null,null,null,null,null,null,null,'63060289','43',null,'1','C','-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','12','157','1',null,'071016a',null,null,null,'157',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','14','202','1',null,'071016a',null,null,null,'202',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','3','204','1',null,'071016a',null,null,'80766587','204',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','12','209','1',null,'071016a',null,null,'80766587','209',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values('10279314','451','3','703','1',null,'071016a',null,null,'80766588','703',null,'0',null,null,null,'0',null,null,'0',null,'V',null,null,null,null,null,null,null,null,'63060290','43',null,'1','C','-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','1','1','1',null,'071016a',null,null,'80766587','1',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','1','2','1',null,'071016a',null,null,'80766587','2',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','1','3','1',null,'071016a',null,null,'80766587','3',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','1','4','1',null,'071016a',null,null,'80766587','4',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','2','5','1',null,'071016a',null,null,'80766587','5',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','3','6','1',null,'071016a',null,null,'80766587','6',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','3','7','1',null,'071016a',null,null,'80766587','7',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','3','8','1',null,'071016a',null,null,'80766587','8',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','3','9','1',null,'071016a',null,null,'80766587','9',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,'63060287','43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','3','13','1',null,'071016a',null,null,'80766587','13',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','1','22','1',null,'071016a',null,null,null,'22',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','3','53','1',null,'071016a',null,null,'80766587','53',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,'63060288','43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','10','62','1',null,'071016a',null,null,null,'62',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','3','76','1',null,'071016a',null,null,null,null,null,'0',null,null,null,'0',null,null,'0',null,'V',null,null,null,null,null,null,null,null,'63060289','43',null,'1','C','-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','12','157','1',null,'071016a',null,null,null,'157',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','14','202','1',null,'071016a',null,null,null,'202',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','3','204','1',null,'071016a',null,null,'80766587','204',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','12','209','1',null,'071016a',null,null,'80766587','209',null,'0',null,null,null,'0',null,null,'0',null,null,null,null,null,null,null,null,null,null,null,'43',null,'1',null,'-307','APPINT','C',null,null,null,null);
insert into contr_services values ('10279314','463','3','703','1',null,'071016a',null,null,'80766588','703',null,'0',null,null,null,'0',null,null,'0',null,'V',null,null,null,null,null,null,null,null,'63060290','43',null,'1','C','-307','APPINT','C',null,null,null,null);

insert into MPULKFXO values ('13568', 0, 724, '00551981232000', 1, 1, 1);


-- FOR ACCOUNT16.BGH
insert into mpufftab (FFCODE, CO_ID, DES, SHDES, SCALEFACTOR, CLOSURE, REC_VERSION, USED_COUNTER, PRIORITY, SNCODE, FREEUNITS_IND, PRICE_VALUE, UMCODE)
values (9372493, '3664449', '41 SEM LIMITES', 'PALD', null, 'W', 1, null, 20, '1', 'Y', '0,0065', '1');

insert into mpufftab (FFCODE, CO_ID, DES, SHDES, SCALEFACTOR, CLOSURE, REC_VERSION, USED_COUNTER, PRIORITY, SNCODE, FREEUNITS_IND, PRICE_VALUE, UMCODE)
values (9374730, '3664449', '41 SEM LIMITES', 'PALD', null, 'W', 1, null, 20, '1', 'Y', '0,0065', '1');
