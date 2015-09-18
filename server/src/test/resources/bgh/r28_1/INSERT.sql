
CREATE TABLE RATEPLAN_HIST (
	CO_ID NUMBER,
	SEQNO NUMBER,
	TMCODE NUMBER,
	TMCODE_DATE DATE,
	USERLASTMOD VARCHAR2(50),
	REC_VERSION NUMBER
)
/

INSERT INTO RATEPLAN_HIST
(CO_ID,SEQNO,TMCODE,TMCODE_DATE,USERLASTMOD,REC_VERSION)
VALUES
(4618517,1,100,TO_DATE('22/10/2005', 'DD/MM/YYYY'),'GMD',1)
/
INSERT INTO RATEPLAN_HIST
(CO_ID,SEQNO,TMCODE,TMCODE_DATE,USERLASTMOD,REC_VERSION)
VALUES
(4618517,2,303,TO_DATE('14/03/2008', 'DD/MM/YYYY'),'F8025809',0)
/
INSERT INTO RATEPLAN_HIST
(CO_ID,SEQNO,TMCODE,TMCODE_DATE,USERLASTMOD,REC_VERSION)
VALUES
(6262940,1,354,TO_DATE('7/06/2006', 'DD/MM/YYYY'),'GMD',1)
/
INSERT INTO RATEPLAN_HIST
(CO_ID,SEQNO,TMCODE,TMCODE_DATE,USERLASTMOD,REC_VERSION)
VALUES
(6262940,2,463,TO_DATE('12/05/2008', 'DD/MM/YYYY'),'F8023212',0)
/
INSERT INTO RATEPLAN_HIST
(CO_ID,SEQNO,TMCODE,TMCODE_DATE,USERLASTMOD,REC_VERSION)
VALUES
(6350146,1,105,TO_DATE('20/06/2006', 'DD/MM/YYYY'),'GMD',1)
/
INSERT INTO RATEPLAN_HIST
(TMCODE, TMCODE_DATE, CO_ID)
VALUES
(498, '20-dez-2007', '51467551')
/
-- account7.bgh
INSERT INTO RATEPLAN_HIST
(TMCODE, TMCODE_DATE, CO_ID)
VALUES
(257, '8-dez-2005', '4945475')
/
-- account8.bgh
INSERT INTO RATEPLAN_HIST
(TMCODE, TMCODE_DATE, CO_ID)
VALUES
(846,  to_date('9/1/08', 'DD/MM/YY'), '52148233')
/
INSERT INTO RATEPLAN_HIST
(TMCODE, TMCODE_DATE, CO_ID)
VALUES
(846,  to_date('9/1/08', 'DD/MM/YY'), '52148228')
/

insert into qlf_plans_packages values ( (select objid from qlf_plans where tmcode = 257), 4);
insert into qlf_plans_packages values ( (select objid from qlf_plans where tmcode = 257), 3);
insert into qlf_plans_packages values ( (select objid from qlf_plans where tmcode = 257), 7);

-- account8.bgh
insert into qlf_plans
(objid, tmcode, description, short_desc, plan_name, state, custom_1, custom_2, custom_3, pack_mandatory)
values (93410573, 846, 'Plano TIM Office - SP', 'TOFSP','Plano TIM Office', 'SP', null, null, null, 'T')
/
insert into qlf_package (objid, short_desc, description) values (564, 'TOI08','Pct 800 min Local TIM Office')
/
insert into qlf_package (objid, short_desc, description) values (562, 'TOI04','Pct 400 min Local TIM Office')
/
insert into qlf_plans_packages values (93410573, 564)
/
insert into qlf_plans_packages values (93410573, 562)
/
