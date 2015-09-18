CREATE TABLE ORDER_INVOICE (
 	CUSTOMER_ID VARCHAR(30),
 	OHXACT VARCHAR(30),
 	INVOICE_ID VARCHAR(30),
 	TIM_VALUE NUMBER(16,8)
)
/
CREATE TABLE ORDERHDR_ALL (
    OHXACT VARCHAR(30),
    OHDUEDATE DATE
)
/
CREATE TABLE ORDER_CSP_INVOICE (
    INVOICE_ID VARCHAR(30),
    CSP_ID VARCHAR(30),
    INVOICE_VALUE NUMBER(16,8)
)
/

/* Inserts */




insert into order_invoice values ('407727', 'ohxact_1', '001', 2525);
insert into order_invoice values ('25768', 'ohxact_2', '96270449', 98.50 );
insert into order_invoice values ('2705954', 'ohxact_3', '002', 56.50 );
insert into order_invoice values ('2705954', 'ohxact_3.A', '002.A', 100.00 );

insert into orderhdr_all values ('ohxact_1', to_date('15-Jan-2008', 'DD-MON-YYYY'));
insert into orderhdr_all values ('ohxact_2',  to_date('10-Ago-2007', 'DD-MON-YYYY'));
insert into orderhdr_all values ('ohxact_3',  to_date('10-Nov-2006', 'DD-MON-YYYY'));
insert into orderhdr_all values ('ohxact_3.A',  to_date('10-Dez-2006', 'DD-MON-YYYY'));

insert into order_csp_invoice values ('001', '21', 6.5);
insert into order_csp_invoice values ('001', '15', 66);
insert into order_csp_invoice values ('002', '41', 20.33);
insert into order_csp_invoice values ('002.A', '14', 4.04);

