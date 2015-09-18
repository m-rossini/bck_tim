create table ACC_PDA_ELEGIBILIDADE (
    COD_PROMO               NUMBER NOT NULL,
    IND_ESTRUTURA           VARCHAR2(1) NOT NULL,
    COD_PLANO_INDIVIDUAL    VARCHAR2(300),
    COD_SERV_INDIVIDUAL     VARCHAR2(5),
    COD_PCTE_INDIVIDUAL     VARCHAR2(5),
    COD_PLANO_LA            VARCHAR2(300),
    COD_SERV_LA             VARCHAR2(5),
    COD_PCTE_LA             VARCHAR2(5),
    COD_SERV_DESVIO         VARCHAR2(30) NOT NULL,
    VLR_DESVIO              NUMBER NOT NULL,
    DATA_INICIO_VIGENCIA    DATE,
    DATA_FIM_VIGENCIA       DATE
);

CREATE TABLE CONTRACT_ALL 
   (CO_ID NUMBER NOT NULL ENABLE, 
	CUSTOMER_ID NUMBER NOT NULL ENABLE, 
	TYPE VARCHAR2(1), 
	OHXACT NUMBER, 
	PLCODE NUMBER NOT NULL ENABLE, 
	SCCODE NUMBER NOT NULL ENABLE, 
	SUBM_ID NUMBER, 
	CO_SIGNED DATE, 
	CO_EQU_TYPE VARCHAR2(20), 
	CO_REP_BILL VARCHAR2(40), 
	CO_REP VARCHAR2(40), 
	CO_REP_BILL_IDNO VARCHAR2(30), 
	CO_REP_IDNO VARCHAR2(30), 
	CO_INSTALLED DATE, 
	CO_ITEMIZED_BILL VARCHAR2(1), 
	CO_IB_CATEGORIES NUMBER, 
	CO_IB_THRESHOLD FLOAT(126), 
	CO_ARCHIVE VARCHAR2(1), 
	CO_DIR_ENTRY VARCHAR2(1), 
	CO_OPERATOR_DIR VARCHAR2(1), 
	CO_PSTN_DIR VARCHAR2(1), 
	CO_CALLS_ANONYM VARCHAR2(1), 
	CO_ASS_SERV VARCHAR2(1), 
	CO_ASS_EQU VARCHAR2(1), 
	CO_ASS_CBB VARCHAR2(1), 
	CO_CRD_CHECK VARCHAR2(1), 
	CO_CRD_CHK_END DATE, 
	CO_CRD_CHK_START DATE, 
	CO_CRD_CLICKS NUMBER, 
	CO_CRD_CLICKS_DAY NUMBER, 
	CO_CRD_DAYS NUMBER, 
	CO_COMMENT VARCHAR2(60), 
	CO_DURATION FLOAT(126), 
	CO_RESERVED DATE, 
	CO_EXPIR_DATE DATE, 
	CO_ACTIVATED DATE, 
	CO_ENTDATE DATE, 
	CO_MODDATE DATE, 
	CO_USERLASTMOD VARCHAR2(16), 
	CO_TOLLRATING VARCHAR2(1), 
	TMCODE NUMBER, 
	TMCODE_DATE DATE, 
	CO_CRD_D_TR1 NUMBER, 
	CO_CRD_D_TR2 NUMBER, 
	CO_CRD_D_TR3 NUMBER, 
	CO_CRD_P_TR1 NUMBER, 
	CO_CRD_P_TR2 NUMBER, 
	CO_CRD_P_TR3 NUMBER, 
	ECCODE_LDC NUMBER, 
	PENDING_ECCODE_LDC NUMBER, 
	ECCODE_LEC NUMBER, 
	PENDING_ECCODE_LEC NUMBER, 
	CO_REQUEST NUMBER, 
	SVP_CONTRACT VARCHAR2(1), 
	DEALER_ID NUMBER, 
	NOT_VALID VARCHAR2(1), 
	CONTR_CURR_ID NUMBER NOT NULL ENABLE, 
	CONVRATETYPE_CONTRACT NUMBER NOT NULL ENABLE, 
	ARPCODE NUMBER, 
	CO_ADDR_ON_IBILL VARCHAR2(1), 
	CO_CRD_AMOUNT FLOAT(126), 
	CO_CRD_AMOUNT_DAY FLOAT(126), 
	PRODUCT_HISTORY_DATE DATE, 
	CO_CONFIRM VARCHAR2(1), 
	CO_TIMM_MODIFIED VARCHAR2(1), 
	CO_EXT_CSUIN VARCHAR2(50), 
	TRIAL_END_DATE DATE, 
	CO_IB_CDR_FLAG VARCHAR2(1), 
	CURRENCY NUMBER NOT NULL ENABLE, 
	SEC_CONTR_CURR_ID NUMBER, 
	SEC_CONVRATETYPE_CONTRACT NUMBER, 
	REC_VERSION NUMBER DEFAULT (0) NOT NULL ENABLE, 
	CO_INPREPAY VARCHAR2(2000), 
	CO_INPREPAY_PENDING VARCHAR2(2000), 
	MAKE_CALLS_DATE DATE 
   );
 
   ALTER TABLE CONTRACT_ALL ADD CONSTRAINT PKCONTRACT_ALL PRIMARY KEY (CO_ID);
 
   COMMENT ON COLUMN CONTRACT_ALL.CO_ID IS 'Internal Key';
   COMMENT ON COLUMN CONTRACT_ALL.CUSTOMER_ID IS 'The subscriber who signed the contract. Fkey to CUSTOMER.CUSTOMER_ID';
   COMMENT ON COLUMN CONTRACT_ALL.TYPE IS 'Type of the contract. S=sales,R=rental';
   COMMENT ON COLUMN CONTRACT_ALL.OHXACT IS 'The contract based bill. Fkey to ORDERHDR.OHXACT';
   COMMENT ON COLUMN CONTRACT_ALL.PLCODE IS 'The network. Fkey to MPDHPLMN.PLCODE';
   COMMENT ON COLUMN CONTRACT_ALL.SCCODE IS 'The market. Fkey to MPDSCTAB.SCCODE';
   COMMENT ON COLUMN CONTRACT_ALL.SUBM_ID IS 'The sub-market. Fkey to SUB_MARKET-SUBM_ID';
   COMMENT ON COLUMN CONTRACT_ALL.CO_SIGNED IS 'date when contract has been signed';
   COMMENT ON COLUMN CONTRACT_ALL.CO_EQU_TYPE IS 'type of equipment used';
   COMMENT ON COLUMN CONTRACT_ALL.CO_REP_BILL IS 'representative for bill';
   COMMENT ON COLUMN CONTRACT_ALL.CO_REP IS 'representative for contract';
   COMMENT ON COLUMN CONTRACT_ALL.CO_REP_BILL_IDNO IS 'identity-no of bill-to representytive';
   COMMENT ON COLUMN CONTRACT_ALL.CO_REP_IDNO IS 'identity-no of contract representative';
   COMMENT ON COLUMN CONTRACT_ALL.CO_INSTALLED IS 'installation date';
   COMMENT ON COLUMN CONTRACT_ALL.CO_ITEMIZED_BILL IS 'X= print itemized bill';
   COMMENT ON COLUMN CONTRACT_ALL.CO_IB_CATEGORIES IS 'Grouping categories for itemized bill';
   COMMENT ON COLUMN CONTRACT_ALL.CO_IB_THRESHOLD IS 'Threshold amount for calls on itemized bill';
   COMMENT ON COLUMN CONTRACT_ALL.CO_ARCHIVE IS 'X= archive itemized bill';
   COMMENT ON COLUMN CONTRACT_ALL.CO_DIR_ENTRY IS 'address for directory entry. C=contract,B=bill-to';
   COMMENT ON COLUMN CONTRACT_ALL.CO_OPERATOR_DIR IS 'Y=list on operators directory';
   COMMENT ON COLUMN CONTRACT_ALL.CO_PSTN_DIR IS 'Y=list on PSTN directory';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CALLS_ANONYM IS 'X = YES / Every other case = NO';
   COMMENT ON COLUMN CONTRACT_ALL.CO_ASS_SERV IS 'Y=services assigned';
   COMMENT ON COLUMN CONTRACT_ALL.CO_ASS_EQU IS 'Y=equipment sold, V=equipment vended';
   COMMENT ON COLUMN CONTRACT_ALL.CO_ASS_CBB IS 'Y=contract based bill printed';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_CHECK IS 'X=Credit check is enabled';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_CHK_END IS 'date when curedit check ends';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_CHK_START IS 'date when credit check starts';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_CLICKS IS 'credit limit in clicks, for periodical check';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_CLICKS_DAY IS 'credit limit in clicks for daily check';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_DAYS IS 'period in days for periodical check';
   COMMENT ON COLUMN CONTRACT_ALL.CO_COMMENT IS 'comment, if co_status = failed';
   COMMENT ON COLUMN CONTRACT_ALL.CO_DURATION IS 'duration of this contract in minutes (co_cancelled-co_signed)';
   COMMENT ON COLUMN CONTRACT_ALL.CO_RESERVED IS 'date, until when thus contract is reserved (rental billing only)';
   COMMENT ON COLUMN CONTRACT_ALL.CO_EXPIR_DATE IS 'the date, when the contract woll be canceled prospectivly';
   COMMENT ON COLUMN CONTRACT_ALL.CO_ACTIVATED IS 'date of first activation of this contract';
   COMMENT ON COLUMN CONTRACT_ALL.CO_ENTDATE IS 'date entered';
   COMMENT ON COLUMN CONTRACT_ALL.CO_MODDATE IS 'date of last modification';
   COMMENT ON COLUMN CONTRACT_ALL.CO_USERLASTMOD IS 'user, who made last modification';
   COMMENT ON COLUMN CONTRACT_ALL.CO_TOLLRATING IS 'Mode of toll rating.O= on behalf of,E= external';
   COMMENT ON COLUMN CONTRACT_ALL.TMCODE IS 'Pointer to actual valid rateplan in table MPULKNXG';
   COMMENT ON COLUMN CONTRACT_ALL.TMCODE_DATE IS 'Date since the rateplan is valid';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_D_TR1 IS 'Daily Threshold 1. FK to MPUTRTAB.TR_ID';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_D_TR2 IS 'Daily Threshold 2. FK to MPUTRTAB.TR_ID';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_D_TR3 IS 'Daily Threshold 3. FK to MPUTRTAB.TR_ID';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_P_TR1 IS 'Periodic Threshold 1. FK to MPUTRTAB.TR_ID';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_P_TR2 IS 'Periodic Threshold 2. FK to MPUTRTAB.TR_ID';
   COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_P_TR3 IS 'Periodic Threshold 3. FK to MPUTRTAB.TR_ID';
   COMMENT ON COLUMN CONTRACT_ALL.ECCODE_LDC IS 'Preferred long distance carrier. FK MPDECTAB.ECCODE';
   COMMENT ON COLUMN CONTRACT_ALL.PENDING_ECCODE_LDC IS 'the requested preferred long distance carrier. FK to MPDECTAB.ECCODE.';
   COMMENT ON COLUMN CONTRACT_ALL.ECCODE_LEC IS 'Local Exchange carrier code. FK MPDECTAB.ECCODE';
   COMMENT ON COLUMN CONTRACT_ALL.PENDING_ECCODE_LEC IS 'the requested local exchange carrier. FK to MPDECTAB.ECCODE.';
   COMMENT ON COLUMN CONTRACT_ALL.CO_REQUEST IS 'request handle. / logical FK to MDSRRTAB.REQUEST';
   COMMENT ON COLUMN CONTRACT_ALL.SVP_CONTRACT IS 'Set to X for dummy contracts of service provider, otherwise NULL.';
COMMENT ON COLUMN CONTRACT_ALL.DEALER_ID IS 'Dealer who the data is assigned to-. Logical FK: CUSTOMER_ALL.CUSTOMER_ID.';
COMMENT ON COLUMN CONTRACT_ALL.NOT_VALID IS 'Indicates, that data have not been validated by the application (=X).
An X is only allowed if CURR_CO_STATUS.CH_STATUS=o.';
COMMENT ON COLUMN CONTRACT_ALL.CONTR_CURR_ID IS 'This is the contract`s currency.
FK: FORCURR.FC_ID.';
COMMENT ON COLUMN CONTRACT_ALL.CONVRATETYPE_CONTRACT IS 'The contract`s currency convertion type.
FK: CONVRATETYPES.CONVRATETYPE_ID.';
COMMENT ON COLUMN CONTRACT_ALL.ARPCODE IS 'Tariff Model Code of Alternate Rateplan.';
COMMENT ON COLUMN CONTRACT_ALL.CO_ADDR_ON_IBILL IS 'Flag indicating whether the User/Installation Address is to be printed on the itemized bill. Domain:
X=Print User/Installation Address on itemized bill (CCONTACT where CO_ID = this CO_ID),
NULL=Print Itemized Bill Address on itemized bill (CCONTACT where CUSTOMER_ID = this CUSTOMER_ID and CCBILLDETAILS = X.';
COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_AMOUNT IS 'credit limit as flat amounts, for periodical check.';
COMMENT ON COLUMN CONTRACT_ALL.CO_CRD_AMOUNT_DAY IS 'credit limit as flat amounts, for daily check.';
COMMENT ON COLUMN CONTRACT_ALL.PRODUCT_HISTORY_DATE IS 'Date the product message (EDIFACT) was generated.';
COMMENT ON COLUMN CONTRACT_ALL.CO_CONFIRM IS 'X=indicates that the document for contract confirmation is already generated.';
COMMENT ON COLUMN CONTRACT_ALL.CO_TIMM_MODIFIED IS 'X=indicates that the contract is modified and ready to be generated as TIMM-Document.';
COMMENT ON COLUMN CONTRACT_ALL.CO_EXT_CSUIN IS 'External contract reference number.
Returned from external provisioning subsystem.';
COMMENT ON COLUMN CONTRACT_ALL.TRIAL_END_DATE IS 'The last day the contract will be on trail';
COMMENT ON COLUMN CONTRACT_ALL.CO_IB_CDR_FLAG IS 'Itemized bill high volume CDR flag. Domain:
X=a lot of bill items expected for the itemized bill,
NULL=regular itemized bill.
If this flag is set then:
1) Itemized bills of this contract are processed using the high volume
   capability feature.
2) Depending on BCH feature code 10,
   CDR information may be stored exclusively in XCD attachment files,
3) Promotions will be calculated on sum items instead of on single item';
COMMENT ON COLUMN CONTRACT_ALL.CURRENCY IS 'Currency of fields: CONTRACT_ALL.CO_CRD_AMOUNT, CONTRACT_ALL.CO_CRD_AMOUNT_DAY,
 CO_IB_THRESHOLD, MPUFDTAB.UNBILLED_PERIODIC, MPUFDTAB.UNBILLED_DAY FK: FORCURR';
COMMENT ON COLUMN CONTRACT_ALL.SEC_CONTR_CURR_ID IS 'Secondary Contract Currency. FK: FORCURR';
COMMENT ON COLUMN CONTRACT_ALL.SEC_CONVRATETYPE_CONTRACT IS 'Conversion Rate Type for Secondary Contract Currency. FK: CONVRATETYPES';
COMMENT ON COLUMN CONTRACT_ALL.REC_VERSION IS 'Counter for multiuser access';
COMMENT ON COLUMN CONTRACT_ALL.CO_INPREPAY IS 'Parameters indicate current status of contract IN.';
COMMENT ON COLUMN CONTRACT_ALL.CO_INPREPAY_PENDING IS 'Pending parameters passed to GMD for IN.';
COMMENT ON COLUMN CONTRACT_ALL.MAKE_CALLS_DATE IS 'Date through which calls may be made with prepaid subscrber card. Calculated as date of activation + MPUTMTAB.MAKE_CALLS_DAYS. May be updated with a new date when subscriber purchases a new card..';
COMMENT ON TABLE CONTRACT_ALL  IS 'A legally binding document that specifies the services being subscribed to';


--DDL da tabela CUSTOMER_ALL
--drop table CUSTOMER_ALL;

CREATE TABLE CUSTOMER_ALL 
   (CUSTOMER_ID NUMBER NOT NULL ENABLE, 
	CUSTOMER_ID_HIGH NUMBER, 
	CUSTCODE VARCHAR2(24) NOT NULL ENABLE, 
	CSST VARCHAR2(2), 
	CSTYPE VARCHAR2(1), 
	CSACTIVATED DATE, 
	CSDEACTIVATED DATE, 
	CUSTOMER_DEALER VARCHAR2(1), 
	CSTYPE_DATE DATE, 
	CSTAXABLE VARCHAR2(1), 
	CSLEVEL VARCHAR2(2), 
	CSCUSTTYPE VARCHAR2(1), 
	CSLVLNAME VARCHAR2(10), 
	TMCODE NUMBER, 
	PRGCODE VARCHAR2(10), 
	TERMCODE NUMBER(4,0), 
	CSCLIMIT FLOAT(126), 
	CSCURBALANCE FLOAT(126), 
	CSDEPDATE DATE, 
	BILLCYCLE VARCHAR2(2), 
	CSTESTBILLRUN VARCHAR2(1), 
	BILL_LAYOUT NUMBER, 
	PAYMNTRESP VARCHAR2(1), 
	TARGET_REACHED NUMBER, 
	PCSMETHPAYMNT VARCHAR2(1) DEFAULT 'P' -- COMPENSAÇÃO BANCARIA (Direct Debit)
, 
	PASSPORTNO VARCHAR2(30), 
	BIRTHDATE DATE, 
	DUNNING_FLAG VARCHAR2(1), 
	COMM_NO VARCHAR2(20), 
	POS_COMM_TYPE NUMBER, 
	BTX_PASSWORD VARCHAR2(8), 
	BTX_USER VARCHAR2(14), 
	SETTLES_P_MONTH VARCHAR2(12), 
	CASHRETOUR NUMBER, 
	CSTRADECODE VARCHAR2(10), 
	CSPASSWORD VARCHAR2(20), 
	CSPROMOTION VARCHAR2(1), 
	CSCOMPREGNO VARCHAR2(20), 
	CSCOMPTAXNO VARCHAR2(20), 
	CSREASON NUMBER, 
	CSCOLLECTOR VARCHAR2(16), 
	CSCONTRESP VARCHAR2(1), 
	CSDEPOSIT FLOAT(126), 
	CSCREDIT_DATE DATE, 
	CSCREDIT_REMARK VARCHAR2(2000), 
	SUSPENDED DATE, 
	REACTIVATED DATE, 
	PREV_BALANCE FLOAT(126), 
	LBC_DATE DATE, 
	EMPLOYEE VARCHAR2(1), 
	COMPANY_TYPE VARCHAR2(1), 
	CRLIMIT_EXC VARCHAR2(1), 
	AREA_ID NUMBER, 
	COSTCENTER_ID NUMBER, 
	CSFEDTAXID VARCHAR2(5), 
	CREDIT_RATING NUMBER, 
	CSCREDIT_STATUS VARCHAR2(2), 
	DEACT_CREATE_DATE DATE, 
	DEACT_RECEIP_DATE DATE, 
	EDIFACT_ADDR VARCHAR2(32), 
	EDIFACT_USER_FLAG VARCHAR2(1), 
	EDIFACT_FLAG VARCHAR2(1), 
	CSDEPOSIT_DUE_DATE DATE, 
	CALCULATE_DEPOSIT VARCHAR2(1), 
	TMCODE_DATE DATE, 
	CSLANGUAGE NUMBER, 
	CSRENTALBC VARCHAR2(2), 
	ID_TYPE NUMBER, 
	USER_LASTMOD VARCHAR2(16), 
	CSENTDATE DATE DEFAULT (sysdate) NOT NULL ENABLE, 
	CSMODDATE DATE, 
	CSMOD VARCHAR2(1), 
	CSNATIONALITY NUMBER, 
	CSBILLMEDIUM NUMBER, 
	CSITEMBILLMEDIUM NUMBER, 
	CUSTOMER_ID_EXT VARCHAR2(10), 
	CSRESELLER VARCHAR2(1), 
	CSCLIMIT_O_TR1 NUMBER, 
	CSCLIMIT_O_TR2 NUMBER, 
	CSCLIMIT_O_TR3 NUMBER, 
	CSCREDIT_SCORE VARCHAR2(40), 
	CSTRADEREF VARCHAR2(2000), 
	CSSOCIALSECNO VARCHAR2(20), 
	CSDRIVELICENCE VARCHAR2(20), 
	CSSEX VARCHAR2(1), 
	CSEMPLOYER VARCHAR2(40), 
	CSTAXABLE_REASON VARCHAR2(40), 
	WPID NUMBER, 
	CSPREPAYMENT VARCHAR2(1), 
	CSREMARK_1 VARCHAR2(40), 
	CSREMARK_2 VARCHAR2(40), 
	MA_ID NUMBER, 
	CSSUMADDR VARCHAR2(1), 
	BILL_INFORMATION VARCHAR2(1), 
	DEALER_ID NUMBER, 
	DUNNING_MODE VARCHAR2(4), 
	NOT_VALID VARCHAR2(1), 
	CSCRDCHECK_AGREED VARCHAR2(1), 
	MARITAL_STATUS NUMBER, 
	EXPECT_PAY_CURR_ID NUMBER, 
	CONVRATETYPE_PAYMENT NUMBER, 
	REFUND_CURR_ID NUMBER, 
	CONVRATETYPE_REFUND NUMBER, 
	SRCODE NUMBER, 
	CURRENCY NUMBER NOT NULL ENABLE, 
	PRIMARY_DOC_CURRENCY NUMBER, 
	SECONDARY_DOC_CURRENCY NUMBER, 
	PRIM_CONVRATETYPE_DOC NUMBER, 
	SEC_CONVRATETYPE_DOC NUMBER, 
	REC_VERSION NUMBER DEFAULT (0) NOT NULL ENABLE, 
	CUSTOMER_ADI VARCHAR2(25), 
	FER_ID NUMBER, 
	LAST_EXEMPTION_UPDATE DATE, 
	DUNNING_COUNT NUMBER(*,0) DEFAULT 0
   );
   
ALTER TABLE CUSTOMER_ALL ADD CONSTRAINT CHK_CUSTOMER_ALL_CSSUMADDR CHECK (CSSUMADDR IN ('B','C')) ENABLE;
ALTER TABLE CUSTOMER_ALL ADD CONSTRAINT CHK_CUSTALL_CUSTOMER_ID    CHECK (CUSTOMER_ID != 0) ENABLE;
ALTER TABLE CUSTOMER_ALL ADD CONSTRAINT PKCUSTOMER_ALL       PRIMARY KEY (CUSTOMER_ID);
ALTER TABLE CUSTOMER_ALL ADD CONSTRAINT CKCUSTOMER_ALL_DUNNING_FLAG CHECK (DUNNING_FLAG = 'X' OR DUNNING_FLAG IS NULL) ENABLE;

COMMENT ON COLUMN CUSTOMER_ALL.CUSTOMER_ID IS 'Customer Identifier.DAB constraint CUSTOMER_ID !=0,Logical foreign key to CUSTOMER_BASE. Dealer do not reference to CUSTOMER_BASE.';
COMMENT ON COLUMN CUSTOMER_ALL.CUSTOMER_ID_HIGH IS 'pointer to customer of upper hierarchy level';
COMMENT ON COLUMN CUSTOMER_ALL.CUSTCODE IS 'customer code in form Kd.AK.KSt.TN Eg.: 1.203984, 2.10, 2.45.10.10.100067, 3.123.12.15.190023 7.2398764.13.17.100968';
COMMENT ON COLUMN CUSTOMER_ALL.CSST IS 'state';
COMMENT ON COLUMN CUSTOMER_ALL.CSTYPE IS 'type: a=active d=deactive i=interested s=suspended (g=gesperrt) (b=beantragt)';
COMMENT ON COLUMN CUSTOMER_ALL.CSACTIVATED IS 'date of activation';
COMMENT ON COLUMN CUSTOMER_ALL.CSDEACTIVATED IS 'date of deactivation';
COMMENT ON COLUMN CUSTOMER_ALL.CUSTOMER_DEALER IS 'C-customer, D-dealer';
COMMENT ON COLUMN CUSTOMER_ALL.CSTYPE_DATE IS 'date when CSTYPE was changed';
COMMENT ON COLUMN CUSTOMER_ALL.CSTAXABLE IS 'is subject to taxing or not';
COMMENT ON COLUMN CUSTOMER_ALL.CSLEVEL IS 'Customer Level The domain splits in the following parts: Value for business partner:
0- External Carrier or Roaming Partner Values for Customers and Service providers: 10- 20- 30- 40- Values for Dealers: All values defined for Sales Agents.
FK: CUSTOMER_LEVEL';
COMMENT ON COLUMN CUSTOMER_ALL.CSCUSTTYPE IS 'Customer Type: C = Consumer, B = Business';
COMMENT ON COLUMN CUSTOMER_ALL.CSLVLNAME IS 'name of Kd, AK, or KSt on this level';
COMMENT ON COLUMN CUSTOMER_ALL.TMCODE IS 'pointer to active tariff model';
COMMENT ON COLUMN CUSTOMER_ALL.PRGCODE IS 'price group code';
COMMENT ON COLUMN CUSTOMER_ALL.TERMCODE IS 'payment terms';
COMMENT ON COLUMN CUSTOMER_ALL.CSCLIMIT IS 'credit limit';
COMMENT ON COLUMN CUSTOMER_ALL.CSCURBALANCE IS 'Current balance of the customers account. Not including deposits and interests for deposits';
COMMENT ON COLUMN CUSTOMER_ALL.CSDEPDATE IS 'Date Deposit Paid';
COMMENT ON COLUMN CUSTOMER_ALL.BILLCYCLE IS 'bill cycle in form 01 - 10';
COMMENT ON COLUMN CUSTOMER_ALL.CSTESTBILLRUN IS 'entry for special control group, range A to Z';
COMMENT ON COLUMN CUSTOMER_ALL.BILL_LAYOUT IS 'Bill-layout for Customer (link to FORM_LAYOUT)';
COMMENT ON COLUMN CUSTOMER_ALL.PAYMNTRESP IS 'payment responsibility if not space';
COMMENT ON COLUMN CUSTOMER_ALL.TARGET_REACHED IS 'Target reached value';
COMMENT ON COLUMN CUSTOMER_ALL.PCSMETHPAYMNT IS 'method of payment; S = Scheck, B = BAR';
COMMENT ON COLUMN CUSTOMER_ALL.PASSPORTNO IS 'Passport number';
COMMENT ON COLUMN CUSTOMER_ALL.BIRTHDATE IS 'birthdate';
COMMENT ON COLUMN CUSTOMER_ALL.DUNNING_FLAG IS 'X = no dunning for this customer';
COMMENT ON COLUMN CUSTOMER_ALL.COMM_NO IS 'number of the communication port';
COMMENT ON COLUMN CUSTOMER_ALL.BTX_PASSWORD IS 'password for BTX';
COMMENT ON COLUMN CUSTOMER_ALL.BTX_USER IS 'BTX-user';
COMMENT ON COLUMN CUSTOMER_ALL.SETTLES_P_MONTH IS 'this field contains one character for each month of the year.  values for each character: 0=payment within 1 month, 1=payment one month too late, 2=payment two months too late, 3=payment three months too late.';
COMMENT ON COLUMN CUSTOMER_ALL.CASHRETOUR IS 'Number of bounced checks and retoured direct debits';
COMMENT ON COLUMN CUSTOMER_ALL.CSTRADECODE IS 'fkey to table TRADE';
COMMENT ON COLUMN CUSTOMER_ALL.CSPASSWORD IS 'password for customer info';
COMMENT ON COLUMN CUSTOMER_ALL.CSPROMOTION IS 'Y=responded to promotion';
COMMENT ON COLUMN CUSTOMER_ALL.CSCOMPREGNO IS 'registration number';
COMMENT ON COLUMN CUSTOMER_ALL.CSCOMPTAXNO IS 'tax number';
COMMENT ON COLUMN CUSTOMER_ALL.CSREASON IS 'Reason for status-change (link to REASONSTATUS_ALL)';
COMMENT ON COLUMN CUSTOMER_ALL.CSCOLLECTOR IS 'cashcollector';
COMMENT ON COLUMN CUSTOMER_ALL.CSCONTRESP IS 'responsible for contract';
COMMENT ON COLUMN CUSTOMER_ALL.CSDEPOSIT IS 'initial deposit amount';
COMMENT ON COLUMN CUSTOMER_ALL.CSCREDIT_DATE IS 'date of last credit scoring.';
COMMENT ON COLUMN CUSTOMER_ALL.CSCREDIT_REMARK IS 'remark for last credit-check';
COMMENT ON COLUMN CUSTOMER_ALL.SUSPENDED IS 'date of suspension';
COMMENT ON COLUMN CUSTOMER_ALL.REACTIVATED IS 'date of reactivation';
COMMENT ON COLUMN CUSTOMER_ALL.LBC_DATE IS 'last billcycle date';
COMMENT ON COLUMN CUSTOMER_ALL.EMPLOYEE IS 'X=customer is an employee';
COMMENT ON COLUMN CUSTOMER_ALL.COMPANY_TYPE IS 'Type of Company:- F Foreign, R Related, I Internal';
COMMENT ON COLUMN CUSTOMER_ALL.CRLIMIT_EXC IS 'X = Credit Limit Exceeded';
COMMENT ON COLUMN CUSTOMER_ALL.AREA_ID IS 'region which is attached to this Customer (fk to AREA)';
COMMENT ON COLUMN CUSTOMER_ALL.COSTCENTER_ID IS 'costcenter which is attached to this customer (fk to COSTCENTER)';
COMMENT ON COLUMN CUSTOMER_ALL.CSFEDTAXID IS 'Federal Tax ID';
COMMENT ON COLUMN CUSTOMER_ALL.CREDIT_RATING IS 'Credit Rating 0 to 100';
COMMENT ON COLUMN CUSTOMER_ALL.CSCREDIT_STATUS IS 'Credit Status';
COMMENT ON COLUMN CUSTOMER_ALL.DEACT_CREATE_DATE IS 'Date of Deactivation (Date on Customer Letter)';
COMMENT ON COLUMN CUSTOMER_ALL.DEACT_RECEIP_DATE IS 'Receipt of Written Deactivation Notification';
COMMENT ON COLUMN CUSTOMER_ALL.EDIFACT_ADDR IS 'EDIFACT Address';
COMMENT ON COLUMN CUSTOMER_ALL.EDIFACT_USER_FLAG IS 'X = Edifact User;';
COMMENT ON COLUMN CUSTOMER_ALL.EDIFACT_FLAG IS 'X indicates if an EDIFACT user';
COMMENT ON COLUMN CUSTOMER_ALL.CSDEPOSIT_DUE_DATE IS 'date when the deposit have to be paid';
COMMENT ON COLUMN CUSTOMER_ALL.CALCULATE_DEPOSIT IS 'flag for calculate the deposit by billing';
COMMENT ON COLUMN CUSTOMER_ALL.TMCODE_DATE IS 'date since when the tmcode is valid';
COMMENT ON COLUMN CUSTOMER_ALL.CSLANGUAGE IS 'customers language. fkey to LANGUAGE.LNG_ID';
COMMENT ON COLUMN CUSTOMER_ALL.CSRENTALBC IS 'the rental-bill bill-cycle';
COMMENT ON COLUMN CUSTOMER_ALL.ID_TYPE IS 'fkey to table ID_TYPE, type of entered ID number in field PASSPORTNO';
COMMENT ON COLUMN CUSTOMER_ALL.USER_LASTMOD IS 'name of user who made the last modification of this record';
COMMENT ON COLUMN CUSTOMER_ALL.CSENTDATE IS 'start date';
COMMENT ON COLUMN CUSTOMER_ALL.CSMODDATE IS 'date last file maint change';
COMMENT ON COLUMN CUSTOMER_ALL.CSMOD IS 'record modified flag';
COMMENT ON COLUMN CUSTOMER_ALL.CSNATIONALITY IS 'nationality of the customer (fkey to table country)';
COMMENT ON COLUMN CUSTOMER_ALL.CSBILLMEDIUM IS 'Medium for the monthly-bill of the customer (fkey to table BILL_MEDIUM)';
COMMENT ON COLUMN CUSTOMER_ALL.CSITEMBILLMEDIUM IS 'Medium for the itemized-bill of the customer (fkey to table BILL_MEDIUM)';
COMMENT ON COLUMN CUSTOMER_ALL.CUSTOMER_ID_EXT IS 'convert the PPC-file dealer code to BSCS dealer_id';
COMMENT ON COLUMN CUSTOMER_ALL.CSRESELLER IS 'X = customer is reseller';
COMMENT ON COLUMN CUSTOMER_ALL.CSCLIMIT_O_TR1 IS 'Open Amount Threshold 1, FK to MPUTRTAB';
COMMENT ON COLUMN CUSTOMER_ALL.CSCLIMIT_O_TR2 IS 'Open Amount Threshold 2,FK to MPUTRTAB';
COMMENT ON COLUMN CUSTOMER_ALL.CSCLIMIT_O_TR3 IS 'Open Amount Threshold 3,FK to MPUTRTAB';
COMMENT ON COLUMN CUSTOMER_ALL.CSCREDIT_SCORE IS 'Credit score';
COMMENT ON COLUMN CUSTOMER_ALL.CSTRADEREF IS 'Trade reference for credit scoring';
COMMENT ON COLUMN CUSTOMER_ALL.CSSOCIALSECNO IS 'Social security number.';
COMMENT ON COLUMN CUSTOMER_ALL.CSDRIVELICENCE IS 'Driving licence number.';
COMMENT ON COLUMN CUSTOMER_ALL.CSSEX IS 'Gender (M=male,F=female).';
COMMENT ON COLUMN CUSTOMER_ALL.CSEMPLOYER IS 'Name of the employer.';
COMMENT ON COLUMN CUSTOMER_ALL.CSTAXABLE_REASON IS 'Reason for non taxable customers.';
COMMENT ON COLUMN CUSTOMER_ALL.WPID IS 'Welcome Procedure / FK to WELCPME_PROC.WPID';
COMMENT ON COLUMN CUSTOMER_ALL.CSPREPAYMENT IS 'X = this customer should prepay.';
COMMENT ON COLUMN CUSTOMER_ALL.CSREMARK_1 IS 'Remark 1 about the customer.';
COMMENT ON COLUMN CUSTOMER_ALL.CSREMARK_2 IS 'Remark 2 about the customer.';
COMMENT ON COLUMN CUSTOMER_ALL.MA_ID IS 'Register a Marketing-Action-ID for a Sales-Force-Member, FK to MARKETING_ACTION.MA_ID';
COMMENT ON COLUMN CUSTOMER_ALL.CSSUMADDR IS 'C=the contract address will be used for sum sheet, B=the bill address will';
COMMENT ON COLUMN CUSTOMER_ALL.BILL_INFORMATION IS 'X=the respective customer has to be processed in bill information runs. Default is NULL.';
COMMENT ON COLUMN CUSTOMER_ALL.DEALER_ID IS 'Dealer who the data is assigned to-. Logical FK: CUSTOMER_ALL.CUSTOMER_ID.';
COMMENT ON COLUMN CUSTOMER_ALL.DUNNING_MODE IS 'Dunning mode as proposed by ext. Risk Management System.';
COMMENT ON COLUMN CUSTOMER_ALL.NOT_VALID IS 'Indicates, that data have not been validated by the application (=X).
An X is only allowed if CUSTOMER_ALL.CSTYPE=i.';
COMMENT ON COLUMN CUSTOMER_ALL.CSCRDCHECK_AGREED IS 'X=agreement to external credit check.';
COMMENT ON COLUMN CUSTOMER_ALL.MARITAL_STATUS IS 'Marital Status. FK: MARITAL_STATUS.MAS_ID.';
COMMENT ON COLUMN CUSTOMER_ALL.EXPECT_PAY_CURR_ID IS 'The currency this customer is supposed to pay his invoice in.
Will not be used in case this customer is not payment responsible.
Will be set to NULL in case this customer`s expected payment currency is configured to be the home currency or in case no expected payment currency is specified for this customer.
FK: FORCURR.FC_ID.';
COMMENT ON COLUMN CUSTOMER_ALL.CONVRATETYPE_PAYMENT IS 'The conversion rate type to be used when payments coming in from this customer are registered.
Note that this conversion rate type must be set if an expected payment currency
is specified for this customer, but in any case a payment comes in from this customer. FK: CONVRATETYPES.CONVRATETYPE_ID.';
COMMENT ON COLUMN CUSTOMER_ALL.REFUND_CURR_ID IS 'The currency this customer is supposed to receive refunds in.
Will not be used in case this customer is not payment responsible.
Will be set to NULL in case customer`s refunding currency is configured to be the home currency. FK: FORCURR.FC_ID.';
COMMENT ON COLUMN CUSTOMER_ALL.CONVRATETYPE_REFUND IS 'The conversion rate type to be used when this customer is refunded.
Mandatory , if REFUND_CURR_ID is not NULL. FK: CONVRATETYPES.CONVRATETYPE_ID.';
COMMENT ON COLUMN CUSTOMER_ALL.SRCODE IS 'Identifier for a subscription fee reduction action.
FK to MPUSRTAB.SRCODE.';
COMMENT ON COLUMN CUSTOMER_ALL.PRIMARY_DOC_CURRENCY IS 'Primary Document Currency. Not NULL for payment responsible customers.
FK: FORCURR';
COMMENT ON COLUMN CUSTOMER_ALL.SECONDARY_DOC_CURRENCY IS 'Secondary Document Currency. FK: FORCURR';
COMMENT ON COLUMN CUSTOMER_ALL.PRIM_CONVRATETYPE_DOC IS 'Conversion Rate Type to be used for the Primary Document Currency.
 Mandatory, if PRIMARY_DOC_CURRENCY is not NULL. FK: CONVRATETYPES';
COMMENT ON COLUMN CUSTOMER_ALL.SEC_CONVRATETYPE_DOC IS 'Conversion Rate Type to be used for the Secondary Document Currency.
 Mandatory, if SEC_DOC_CURRENCY is not NULL. FK: CONVRATETYPES';
COMMENT ON COLUMN CUSTOMER_ALL.REC_VERSION IS 'Counter for multiuser access';
COMMENT ON COLUMN CUSTOMER_ALL.CUSTOMER_ADI IS 'Customer ADI. DEFAULT VALUE (0)';
COMMENT ON TABLE "CUSTOMER_ALL"  IS 'customer/dealer names and other information';

CREATE TABLE MPUTMTAB 
   (TMCODE NUMBER NOT NULL ENABLE, 
	VSCODE NUMBER NOT NULL ENABLE, 
	VSDATE DATE NOT NULL ENABLE, 
	STATUS VARCHAR2(1) NOT NULL ENABLE, 
	TMIND VARCHAR2(1), 
	DES VARCHAR2(30), 
	SHDES VARCHAR2(5), 
	PLCODE NUMBER, 
	PLMNNAME VARCHAR2(30), 
	TMRC NUMBER DEFAULT (0) NOT NULL ENABLE, 
	APDATE DATE, 
	TMGLOBAL VARCHAR2(1) NOT NULL ENABLE, 
	CURRENCY NUMBER NOT NULL ENABLE, 
	REC_VERSION NUMBER DEFAULT (0) NOT NULL ENABLE, 
	PRERATED_TAP_RP_IND VARCHAR2(1) DEFAULT 'N' NOT NULL ENABLE 
);
 
ALTER TABLE MPUTMTAB ADD CONSTRAINT CC_MPUTMTAB_PRE_TAP_RP_IND CHECK (PRERATED_TAP_RP_IND in ('Y','N')) ENABLE;
ALTER TABLE MPUTMTAB ADD CONSTRAINT PK_MPUTMTAB PRIMARY KEY (TMCODE, VSCODE);

COMMENT ON COLUMN MPUTMTAB.TMCODE IS 'Internal Key';
COMMENT ON COLUMN MPUTMTAB.VSCODE IS 'Version of TM';
COMMENT ON COLUMN MPUTMTAB.VSDATE IS 'Effective Date of Version';
COMMENT ON COLUMN MPUTMTAB.STATUS IS 'Status, W= under work, S= ready for simulation, P = ready for production';
COMMENT ON COLUMN MPUTMTAB.TMIND IS 'C = TM Indicator for Combi Rate Plan C (default is blank)';
COMMENT ON COLUMN MPUTMTAB.DES IS 'Visual Description';
COMMENT ON COLUMN MPUTMTAB.SHDES IS 'Visual Description to be printed on Bill';
COMMENT ON COLUMN MPUTMTAB.TMRC IS 'Error Code for TM-Check';
COMMENT ON COLUMN MPUTMTAB.APDATE IS 'Date of approve';
COMMENT ON COLUMN MPUTMTAB.TMGLOBAL IS 'Flag for global TM , Y=global TM, N=normal TM. Only one TM can be global.';
COMMENT ON COLUMN MPUTMTAB.CURRENCY IS 'Currency.FK: FORCURR';
COMMENT ON COLUMN MPUTMTAB.REC_VERSION IS 'Counter for multiuser access';
COMMENT ON COLUMN MPUTMTAB.PRERATED_TAP_RP_IND IS 'Prerated TAP Tariff Indicator. Domain: Y- this is a prerated TAP tariff model, N- this is not a prerated TAP tariff model';
COMMENT ON TABLE MPUTMTAB  IS 'Tariff Models';
