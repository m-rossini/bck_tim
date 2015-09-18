--
-- SINCE MELHORIAS 03 (MAR/2010)
--

-- ACCOUNT1.BGH
INSERT INTO service_rates VALUES ( 
 (SELECT objid FROM qlf_plans WHERE plan_name = 'Plano T Você GSM PR'),
 (SELECT objid FROM qlf_services WHERE svc_code = 76),
 TO_DATE('01/01/2006', 'DD/MM/YYYY'),  SYSDATE, null, null, 0, null, null, null);

-- ACCOUNT3.BGH
INSERT INTO service_rates VALUES ( 
 (SELECT objid FROM qlf_plans WHERE plan_name = 'Plano T Você GSM PR'),
 (SELECT objid FROM qlf_services WHERE svc_code = 604),
 TO_DATE('01/01/2006', 'DD/MM/YYYY'),  SYSDATE, null, null, 14.9, null, null, null); 
 
-- ACCOUNT4.BGH
INSERT INTO CONTR_SERVICES
(CO_ID,TMCODE,SPCODE,SNCODE,CS_SEQNO,CS_CHANNEL_NUM,CS_STAT_CHNG,CS_ON_CBB,CS_DATE_BILLED,CS_REQUEST,SN_CLASS,CS_OVW_SUBSCR,CS_SUBSCRIPT,CS_OVW_ACCESS,CS_OVW_ACC_PRD,CS_OVW_ACC_FIRST,CS_ACCESS,CS_PENDING_STATE,CS_CHANNEL_EXCL,CS_DIS_SUBSCR,CS_ADV_CHARGE,CS_SRV_TYPE,SUBPAYER,USGPAYER,ACCPAYER,CS_ENTDATE,CS_OVW_LAST,INSTALL_DATE,TRIAL_END_DATE,CS_ADV_CHARGE_END_DATE,PRM_VALUE_ID,CURRENCY,CS_ADV_CHARGE_CURRENCY,REC_VERSION,SRV_SUBTYPE,DEALER_ID,USERNAME,INITIATOR_TYPE,BACKUP_CS_OVW_ACCESS,BACKUP_CS_OVW_ACC_PRD,BACKUP_CS_OVW_ACC_FIRST,BACKUP_CS_ACCESS)
VALUES
(4618512,351,3,77,1,NULL,'051022a',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,0,NULL,'V',NULL,NULL,NULL,TO_DATE('22/10/2005','DD/MM/YYYY'),NULL,NULL,NULL,NULL,22368369,43,NULL,36,'C',-9076,'DAGONL','C',NULL,NULL,NULL,NULL)
/

-- ACCOUNT5.BGH

-- ACCOUNT6.BGH
INSERT INTO CONTR_SERVICES
(CO_ID,TMCODE,SPCODE,SNCODE,CS_SEQNO,CS_CHANNEL_NUM,CS_STAT_CHNG,CS_ON_CBB,CS_DATE_BILLED,CS_REQUEST,SN_CLASS,CS_OVW_SUBSCR,CS_SUBSCRIPT,CS_OVW_ACCESS,CS_OVW_ACC_PRD,CS_OVW_ACC_FIRST,CS_ACCESS,CS_PENDING_STATE,CS_CHANNEL_EXCL,CS_DIS_SUBSCR,CS_ADV_CHARGE,CS_SRV_TYPE,SUBPAYER,USGPAYER,ACCPAYER,CS_ENTDATE,CS_OVW_LAST,INSTALL_DATE,TRIAL_END_DATE,CS_ADV_CHARGE_END_DATE,PRM_VALUE_ID,CURRENCY,CS_ADV_CHARGE_CURRENCY,REC_VERSION,SRV_SUBTYPE,DEALER_ID,USERNAME,INITIATOR_TYPE,BACKUP_CS_OVW_ACCESS,BACKUP_CS_OVW_ACC_PRD,BACKUP_CS_OVW_ACC_FIRST,BACKUP_CS_ACCESS)
VALUES
(4618513,100,3,77,1,NULL,'051022a',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,0,NULL,'V',NULL,NULL,NULL,TO_DATE('22/10/2005','DD/MM/YYYY'),NULL,NULL,NULL,NULL,22368369,43,NULL,36,'C',-9076,'DAGONL','C',NULL,NULL,NULL,NULL)
/

INSERT INTO service_rates VALUES ( 
 (SELECT objid FROM qlf_plans WHERE plan_name = 'Plano T Você GSM PR'),
 (SELECT objid FROM qlf_services WHERE svc_code = 77),
 TO_DATE('01/01/2006', 'DD/MM/YYYY'),  SYSDATE, null, null, 2, null, null, null)
/

-- ACCOUNT8.BGH (ticket #377)

-- insert into qlf_plans values(4, 1091, 'Plano Infinity 120-AC', 'INFAC', 'Plano Infinity 120', 'AC', null, null, null, 'F');
-- insert into qlf_services values (270, 911, 'Promo Infinity R$ 0,25_3M', 'INFAC', null, null, null);
INSERT INTO CONTR_SERVICES
(CO_ID,TMCODE,SPCODE,SNCODE,CS_SEQNO,CS_CHANNEL_NUM,CS_STAT_CHNG,CS_ON_CBB,CS_DATE_BILLED,CS_REQUEST,SN_CLASS,CS_OVW_SUBSCR,CS_SUBSCRIPT,CS_OVW_ACCESS,CS_OVW_ACC_PRD,CS_OVW_ACC_FIRST,CS_ACCESS,CS_PENDING_STATE,CS_CHANNEL_EXCL,CS_DIS_SUBSCR,CS_ADV_CHARGE,CS_SRV_TYPE,SUBPAYER,USGPAYER,ACCPAYER,CS_ENTDATE,CS_OVW_LAST,INSTALL_DATE,TRIAL_END_DATE,CS_ADV_CHARGE_END_DATE,PRM_VALUE_ID,CURRENCY,CS_ADV_CHARGE_CURRENCY,REC_VERSION,SRV_SUBTYPE,DEALER_ID,USERNAME,INITIATOR_TYPE,BACKUP_CS_OVW_ACCESS,BACKUP_CS_OVW_ACC_PRD,BACKUP_CS_OVW_ACC_FIRST,BACKUP_CS_ACCESS)
VALUES
(54426594,1091,3,911,1,NULL,'091113a',NULL,NULL,NULL,NULL,NULL,0,NULL,NULL,NULL,0,NULL,NULL,0,NULL,'V',NULL,NULL,NULL,TO_DATE('22/10/2005','DD/MM/YYYY'),NULL,NULL,NULL,NULL,22368369,43,NULL,36,'C',-9076,'DAGONL','C',NULL,NULL,NULL,NULL)
/

INSERT INTO service_rates VALUES ( 
 (SELECT objid FROM qlf_plans WHERE tmcode = 1091),
 (SELECT objid FROM qlf_services WHERE svc_code = 911),
 TO_DATE('01/01/2006', 'DD/MM/YYYY'),  SYSDATE, null, null, 9, null, null, null)
/
