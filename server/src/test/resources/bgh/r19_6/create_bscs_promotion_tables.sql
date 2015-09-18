/* create table DWPROMO_CAT */
CREATE TABLE DWPROMO_CAT (
    DAT_FIM_PROMO DATE NOT NULL ,
    DAT_INICIO_PROMO DATE NOT NULL ,
    COD_PROMOCAO_OLTP NUMBER(19,0) NOT NULL
)
/

/* insert in DWPROMO_CAT */
insert into DWPROMO_CAT values('27-jun-2007', '25-jun-2007', 001)
/

/* insert in DWPROMO_MC - created in R23.1 */
insert into DWPROMO_MC (COD_PROMOCAO_OLTP,SHDES) values(001,'DPN')
/