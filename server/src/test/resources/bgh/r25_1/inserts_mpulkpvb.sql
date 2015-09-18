-- Create table
create table MPULKPVB
(
  PV_COMBI_ID NUMBER not null,
  VSCODE      NUMBER not null,
  SET_ID      NUMBER not null,
  DES         VARCHAR2(30) not null,
  SUBSCRIPT   FLOAT not null,
  ACCESSFEE   FLOAT not null,
  REC_VERSION NUMBER default (0) not null
);

alter table MPULKPVB
  add constraint PKMPULKPVB primary key (PV_COMBI_ID,VSCODE,SET_ID);


INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(15162,5,1,'Promo��o Tarifa Zero',14.9,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(15162,6,1,'Promo��o Tarifa Zero',14.9,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(15162,7,1,'Promo��o Tarifa Zero',14.9,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(11033,2,2,'Pacote TIM Casa Flex - 200 min',0,29.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(11033,3,2,'Pacote TIM Casa Flex - 200 min',0,29.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(11033,4,2,'Pacote TIM Casa Flex - 200 min',0,29.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(11033,5,2,'Pacote TIM Casa Flex - 200 min',0,29.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(8433,2,24,' 60 Min Gr�tis Livre 12',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(8433,3,24,' 60 Min Gr�tis Livre 12',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(8433,4,24,' 60 Min Gr�tis Livre 12',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(8433,5,24,' 60 Min Gr�tis Livre 12',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(6815,3,12,'  25 Min Gr�tis TIM-TIM 06',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(6815,4,12,'  25 Min Gr�tis TIM-TIM 06',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(6815,5,12,'  25 Min Gr�tis TIM-TIM 06',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(6815,6,12,'  25 Min Gr�tis TIM-TIM 06',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(6815,7,12,'  25 Min Gr�tis TIM-TIM 06',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(2001,2,2,'TIM Meia Tarifa 10',0,24.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(2001,3,2,'TIM Meia Tarifa 10',0,24.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(2001,4,2,'TIM Meia Tarifa 10',0,26.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(2001,5,2,'TIM Meia Tarifa 10',0,26.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(2001,6,2,'TIM Meia Tarifa 10',0,27.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(2001,7,2,'TIM Meia Tarifa 10',0,27.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3980,1,2,'Pct. 50 Torpedos Promocionais',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3980,3,2,'Pct. 50 Torpedos Promocionais',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3980,4,2,'Pct. 50 Torpedos Promocionais',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3980,5,2,'Pct. 50 Torpedos Promocionais',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3981,1,1,'Pacote TIM BRASIL 60 min',0,56.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3981,2,1,'Pacote TIM BRASIL 60 min',0,57.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3981,3,1,'Pacote TIM BRASIL 60 min',0,57.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3981,4,1,'Pacote TIM BRASIL 60 min',0,59.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3981,5,1,'Pacote TIM BRASIL 60 min',0,59.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3960,1,1,'Pacote TIM BRASIL 60 min',0,47.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3960,2,1,'Pacote TIM BRASIL 60 min',0,47.18,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3960,3,1,'Pacote TIM BRASIL 60 min',0,49.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3960,4,1,'Pacote TIM BRASIL 60 min',0,46.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3960,5,1,'Pacote TIM BRASIL 60 min',0,46.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3960,6,1,'Pacote TIM BRASIL 60 min',0,48.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(3960,7,1,'Pacote TIM BRASIL 60 min',0,48.9,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(8274,4,5,'  25 Min Gr�tis TIM-TIM 06',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(8274,5,5,'  25 Min Gr�tis TIM-TIM 06',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(8274,6,5,'  25 Min Gr�tis TIM-TIM 06',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(8274,7,5,'  25 Min Gr�tis TIM-TIM 06',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(8274,8,5,'  25 Min Gr�tis TIM-TIM 06',0,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(15308,3,1,'Promo��o Tarifa Zero',14.9,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(15308,4,1,'Promo��o Tarifa Zero',14.9,0,1)
/
INSERT INTO MPULKPVB
(PV_COMBI_ID,VSCODE,SET_ID,DES,SUBSCRIPT,ACCESSFEE,REC_VERSION)
VALUES
(15308,5,1,'Promo��o Tarifa Zero',14.9,0,1)
/