package br.com.auster.tim.billcheckout.bscs.infobus;


/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class FakeXMLForInfobusTest {

	//this response contains a customer id
	public static final String CUSTID_BY_CUSTCODE_RESPONSE_XML = 
		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
		"<ib-msg>" +
		"<ib-info>" +
		"<msg-version>MWIB.MSG.XML-01.00-STD</msg-version>" +
		"<msg-type>REPLY</msg-type>" +
		"<t-info>" +
		"<tid>7E799A4247E26B5B7454DFE2</tid>" +
		"<flow>BCBSCCIDQRY</flow>" +
		"</t-info>" +
		"<result>" +
		"<svc>BCBSCCIDQRY</svc>" +
		"<timestamp>Thu Mar 20 10:49:18.549 2008</timestamp>" +
		"<type>IBCODE</type>" +
		"<code>1</code>" +
		"</result>" +
		"</ib-info>" +
		"<src-info>" +
		"<hostname>rjohpu12</hostname>" +
		"<ipaddr>10.112.51.39</ipaddr>" +
		"<name>dbAdapter2</name>" +
		"</src-info>" +
		"<data>" +
		"<customer>" +
		"<id>465464</id>" +
		"</customer>" +
		"</data>" +
		"</ib-msg>";
	
	//this response does not contain a customer id
//	public static final String CUSTID_BY_CUSTCODE_RESPONSE_XML = 
//		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
//		"<ib-msg>" +
//		"<ib-info>" +
//		"<msg-version>MWIB.MSG.XML-01.00-STD</msg-version>" +
//		"<msg-type>REPLY</msg-type>" +
//		"<t-info>" +
//		"<tid>7E799A4247E269F049B9AF09</tid>" +
//		"<flow>BCBSCCIDQRY</flow>" +
//		"</t-info>" + 
//		"<result>" +
//		"<svc>BCBSCCIDQRY</svc>" +
//		"<timestamp>Thu Mar 20 10:43:15.556 2008</timestamp>" +
//		"<type>IBCODE</type>" + 
//		"<code>1</code>" +
//		"</result>" +
//		"</ib-info>" +
//		"<src-info>" +
//		"<hostname>rjohpu12</hostname>" +
//		"<ipaddr>10.112.51.39</ipaddr>" +
//		"<name>dbAdapter2</name>" +
//		"</src-info>" +
//		"<data/>" +
//		"</ib-msg>";

	//this response contains client info
	public static final String CLIENT_INFO_RESPONSE_XML =
		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>																					"+
		"<ib-msg>	                                                                            "+
		"<ib-info>                                                                            "+
		"<msg-version>MWIB.MSG.XML-01.00-STD</msg-version>                                    "+
		"<msg-type>REPLY</msg-type>                                                           "+
		"<t-info>                                                                             "+
		"<tid>2AB4418447EAA2A025856D93</tid>                                                  "+
		"<flow>BCBSCCQUERY</flow>                                                             "+
		"</t-info>                                                                            "+
		"<result>                                                                             "+
		"<svc>BCBSCCQUERY</svc>                                                               "+
		"<timestamp>Wed Mar 26 16:23:13.363 2008</timestamp>                                  "+
		"<type>IBCODE</type>																													        "+
		"<code>1</code>                                                                       "+
		"<desc>Success</desc>                                                                 "+
		"</result>                                                                            "+
		"</ib-info>                                                                           "+
		"<src-info>                                                                           "+
		"<hostname>rjohpu12</hostname>                                                        "+
		"<ipaddr>10.112.51.39</ipaddr>                                                        "+
		"<name>dbAdapter2</name>                                                              "+
		"</src-info>                                                                          "+
		"<data>                                                                               "+
		"<customer>                                                                           "+
		"<code>5.15157.00.00.100000</code>                                                    "+
		"<id>405112</id>                                                                      "+
		"<contract index=\"0\">                                                                 "+
		"<msisdn>1981223276</msisdn>                                                          "+
		"<status>a</status>                                                                   "+
		"<id>572407</id>                                                                      "+
		"</contract>                                                                          "+
		"<contract index=\"1\">                                                                 "+
		"<msisdn>1981223273</msisdn>                                                          "+
		"<status>a</status>                                                                   "+
		"<id>572420</id>                                                                      "+
		"</contract>                                                                          "+
		"<contract index=\"2\">                                                                 "+
		"<msisdn>1981223279</msisdn>                                                          "+
		"<status>a</status>                                                                   "+
		"<id>572428</id>                                                                      "+
		"</contract>                                                                          "+
		"<contract index=\"3\">                                                                 "+
		"<msisdn>1981223283</msisdn>                                                          "+
		"<status>a</status>                                                                   "+
		"<id>572438</id>                                                                      "+
		"</contract>                                                                          "+
		"<contract index=\"4\">                                                                 "+
		"<msisdn>1981223278</msisdn>                                                          "+
		"<status>a</status>                                                                   "+
		"<id>572448</id>                                                                      "+
		"</contract>                                                                          "+
		"<contract-size>5</contract-size>                                                     "+
		"<address>                                                                            "+
		"<street-type/>                                                                       "+
		"<district>AT</district>                                                              "+
		"<street>R ALFREDO GUEDES</street>                                                    "+
		"<complement>SL 906</complement>                                                      "+
		"<state>SP</state>                                                                    "+
		"<zip>13416901</zip>                                                                  "+
		"<special-street/>                                                                    "+
		"<street-no>1949</street-no>                                                          "+
		"<full-name>GEOGRAPHIC ENG. AVAL. GEOMATICA S/C LTDA</full-name>                      "+
		"<city>PIRACICABA</city>                                                              "+
		"<country>                                                                            "+
		"<code>1</code>                                                                       "+
		"</country>                                                                           "+
		"</address>                                                                           "+
		"</customer>                                                                          "+
		"</data>                                                                              "+
		"</ib-msg>                                                                            ";	
	
	//this response does not contains cliente info
//	public static final String CLIENT_INFO_RESPONSE_XML =
//		"<?xml version="+"\"1.0\""+ " encoding="+"\"ISO-8859-1\""+"?>                  "
//		+"<ib-msg>                                                                     "
//		+"   infobus-info                                                              "
//		+"   source-info                                                               "
//		+"   <data/>                                                                   "
//		+"</ib-msg>    																   ";
	
	//this response contains 26 OCCs
	public static final String OCC_LIST_RESPONSE_XML = 
		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>                            "+
		"<ib-msg>                                                                   "+
		"<ib-info>                                                                  "+
		"<msg-version>MWIB.MSG.XML-01.00-STD</msg-version>                          "+
		"<msg-type>REPLY</msg-type>                                                 "+
		"<t-info>                                                                   "+
		"<tid>2FEE4CFF47E28D6751A69903</tid>                                        "+
		"<flow>BCBSBCOCCQRY</flow>                                                  "+
		"</t-info>                                                                  "+
		"<result>                                                                   "+
		"<svc>BCBSBCOCCQRY</svc>                                                    "+
		"<timestamp>Thu Mar 20 13:14:35.411 2008</timestamp>                        "+
		"<type>IBCODE</type>                                                        "+
		"<code>1</code>                                                             "+
		"<desc>Success</desc>                                                       "+
		"</result>                                                                  "+
		"</ib-info>                                                                 "+
		"<src-info>                                                                 "+
		"<hostname>rjohpu12</hostname>                                              "+
		"<ipaddr>10.112.51.39</ipaddr>                                              "+
		"<name>dbAdapter2</name>                                                    "+
		"</src-info>                                                                "+
		"<data>                                                                     "+
		"<occ index=\"0\">                                                          "+
		"<value>0</value>                                                           "+
		"<qtd-not-billed/>                                                          "+
		"<qtd-billed/>                                                              "+
		"<desc>Fatura Detalhada</desc>                                              "+
		"</occ>                                                                     "+
		"<occ index=\"1\">                                                          "+
		"<value>0</value>                                                           "+
		"<qtd-not-billed/>                                                          "+
		"<qtd-billed/>                                                              "+
		"<desc>Fatura Detalhada</desc>                                              "+
		"</occ>                                                                     "+
		"<occ index=\"2\">                                                          "+
		"<value>0</value>                                                           "+
		"<qtd-not-billed/>                                                          "+
		"<qtd-billed/>                                                              "+
		"<desc>Fatura Detalhada</desc>                                              "+
		"</occ>                                                                     "+
		"<occ index=\"3\">                                                          "+
		"<value>0</value>                                                           "+
		"<qtd-not-billed/>                                                          "+
		"<qtd-billed/>                                                              "+
		"<desc>Fatura Detalhada</desc>                                              "+
		"</occ>                                                                     "+
		"<occ index=\"4\">                                                          "+
		"<value>0</value>                                                           "+
		"<qtd-not-billed/>                                                          "+
		"<qtd-billed/>                                                              "+
		"<desc>Fatura Detalhada</desc>                                              "+
		"</occ>                                                                     "+
		"<occ index=\"5\">                                                          "+
		"<value>50</value>                                                          "+
		"<qtd-not-billed/>                                                          "+
		"<qtd-billed/>                                                              "+
		"<desc>Altera��o Golden Number</desc>                                   "+
		"</occ>                                                                     "+
		"<occ-size>26</occ-size>                                                    "+
		"</data>                                                                    "+
		"</ib-msg>                                                                  ";
	
	//this response contains an error -40000
//	public static final String OCC_LIST_RESPONSE_XML = 
//		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
//		"<ib-msg>" +
//		"<ib-info>" +
//		"<msg-version>MWIB.MSG.XML-01.00-STD</msg-version>" +
//		"<msg-type>REPLY</msg-type>" +
//		"<t-info>" +
//		"<tid>6A22C47F47E28AD51F7077E4</tid>" +
//		"<flow>BCBSBCOCCQRY</flow>" +
//		"</t-info>" +
//		"<result>" +
//		"<svc>BCBSBCOCCQRY</svc>" +
//		"<timestamp>Thu Mar 20 13:03:36.608 2008</timestamp>" +
//		"<type>APPCODE</type>" +
//		"<code>-40000</code>" +
//		"<desc>Contract not found or deactivated</desc>" +
//		"</result>" +
//		"</ib-info>" +
//		"<src-info>" +
//		"<hostname>rjohpu12</hostname>" +
//		"<ipaddr>10.112.51.39</ipaddr>" +
//		"<name>dbAdapter2</name>" +
//		"</src-info>" +
//		"<data/>" +
//		"</ib-msg>";
	
	//this response does not contain any OCC 
//	public static final String OCC_LIST_RESPONSE_XML = 
//		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
//		"<ib-msg>" +
//		"<ib-info>" + 
//		"<msg-version>MWIB.MSG.XML-01.00-STD</msg-version>" +
//		"<msg-type>REPLY</msg-type>" +
//		"<t-info>" +
//		"<tid>1FCE40EA47E274D04216E4C7</tid>" +
//		"<flow>BCBSBCOCCQRY</flow>" +
//		"</t-info>" +
//		"<result>" +
//		"<svc>BCBSBCOCCQRY</svc>" +
//		"<timestamp>Thu Mar 20 11:29:39.355 2008</timestamp>" +
//		"<type>IBCODE</type>" +
//		"<code>1</code>" +
//		"<desc>Success</desc>" +
//		"</result>" +
//		"</ib-info>" +
//		"<src-info>" +
//		"<hostname>rjohpu12</hostname>" +
//		"<ipaddr>10.112.51.39</ipaddr>" +
//		"<name>dbAdapter2</name>" +
//		"</src-info>" +
//		"<data>" +
//		"<occ index=\"0\">" +
//		"<value/>" +
//		"<qtd-not-billed/>" +
//		"<qtd-billed/>" +
//		"<desc/>" +
//		"</occ>" +
//		"<occ-size>1</occ-size>" +
//		"</data>" +
//		"</ib-msg>";
	
	//this response contains four accounts levels 
	public static final String ACCOUNT_STRUCTURE_RESPONSE_XML = 
		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"+
		"<INFOBUS_OUT_PARAMETERS>"+
		"<PARAM name=\"SRV_NAME\" value=\"db\"/>"+
		"<PARAM name=\"SRV_HOST_NAME\" value=\"rjohpu12\"/>"+
		"<PARAM name=\"SRV_IP_ADDRESS\" value=\"10.112.51.39\"/>"+
		"<PARAM name=\"SRV_RESP_TIMESTAMP\" value=\"Thu Mar 20 13:34:07.466 2008\"/>"+
		"<PARAM name=\"SERVICE\" value=\"BSDBHVLAQRY\"/>"+
		"<PARAM name=\"SRV_RET_CODE\" value=\"0\"/>"+
		"<PARAM name=\"SRV_RET_DESCRIPTION\" value=\"All services successfully executed\"/>"+
		"<RECORD_GROUP name=\"LAH_LEVEL1\" size=\"1\"/>"+
		"<RECORD name=\"LAH_LEVEL_1\" index=\"0\">"+
		"<PARAM name=\"ADR_FNAME\" value=\"TESTE SGT EJB\"/>"+
		"<PARAM name=\"CS_CODE\" value=\"6.169181\"/>"+
		"<PARAM name=\"CS_ID\" value=\"2420728\"/>"+
		"<PARAM name=\"CO_ID\" value=\"\"/>"+
		"<PARAM name=\"CS_PAYMNT_RESP\" value=\"Y\"/>"+
		"<PARAM name=\"CS_LEVEL\" value=\"10\"/>"+
		"<RECORD_GROUP name=\"LAH_LEVEL2\" size=\"1\"/>"+
		"<RECORD name=\"LAH_LEVEL_2\" index=\"0\">"+
		"<PARAM name=\"ADR_FNAME\" value=\"TESTE SGT EJB\"/>"+
		"<PARAM name=\"CS_CODE\" value=\"6.169181.10\"/>"+
		"<PARAM name=\"CS_ID\" value=\"2420729\"/>"+
		"<PARAM name=\"CO_ID\" value=\"\"/>"+
		"<PARAM name=\"CS_PAYMNT_RESP\" value=\"N\"/>"+
		"<PARAM name=\"CS_LEVEL\" value=\"20\"/>"+
		"<RECORD_GROUP name=\"LAH_LEVEL3\" size=\"2\"/>"+
		"<RECORD name=\"LAH_LEVEL_3\" index=\"0\">"+
		"<PARAM name=\"ADR_FNAME\" value=\"TESTE SGT EJB\"/>"+
		"<PARAM name=\"CS_CODE\" value=\"6.169181.10.10\"/>"+
		"<PARAM name=\"CS_ID\" value=\"2420730\"/>"+
		"<PARAM name=\"CO_ID\" value=\"2937648\"/>"+
		"<PARAM name=\"CS_PAYMNT_RESP\" value=\"N\"/>"+
		"<PARAM name=\"CS_LEVEL\" value=\"30\"/>"+
		"<RECORD_GROUP name=\"LAH_LEVEL4\" size=\"2\"/>"+
		"<RECORD name=\"LAH_LEVEL_4\" index=\"0\">"+
		"<PARAM name=\"COSC_DIRNUM\" value=\"2481190758\"/>"+
		"<PARAM name=\"ADR_FNAME\" value=\"TESTE SGT EJB\"/>"+
		"<PARAM name=\"CS_CODE\" value=\"6.169181.10.10.100000\"/>"+
		"<PARAM name=\"CS_ID\" value=\"2420731\"/>"+
		"<PARAM name=\"CO_ID\" value=\"2937649\"/>"+
		"<PARAM name=\"CS_PAYMNT_RESP\" value=\"N\"/>"+
		"<PARAM name=\"CS_LEVEL\" value=\"40\"/>"+
		"</RECORD>"+
		"<RECORD name=\"LAH_LEVEL_4\" index=\"1\">"+
		"<PARAM name=\"COSC_DIRNUM\" value=\"2481127311\"/>"+
		"<PARAM name=\"ADR_FNAME\" value=\"TESTE SGT EJB\"/>"+
		"<PARAM name=\"CS_CODE\" value=\"6.169181.10.10.100001\"/>"+
		"<PARAM name=\"CS_ID\" value=\"2420732\"/>"+
		"<PARAM name=\"CO_ID\" value=\"2937650\"/>"+
		"<PARAM name=\"CS_PAYMNT_RESP\" value=\"N\"/>"+
		"<PARAM name=\"CS_LEVEL\" value=\"40\"/>"+
		"</RECORD>"+
		"</RECORD>"+
		"<RECORD name=\"LAH_LEVEL_3\" index=\"1\">"+
		"<PARAM name=\"ADR_FNAME\" value=\"TESTE SGT EJB\"/>"+
		"<PARAM name=\"CS_CODE\" value=\"6.169181.10.11\"/>"+
		"<PARAM name=\"CS_ID\" value=\"2420733\"/>"+
		"<PARAM name=\"CO_ID\" value=\"\"/>"+
		"<PARAM name=\"CS_PAYMNT_RESP\" value=\"N\"/>"+
		"<PARAM name=\"CS_LEVEL\" value=\"30\"/>"+
		"<RECORD_GROUP name=\"LAH_LEVEL4\" size=\"2\"/>"+
		"<RECORD name=\"LAH_LEVEL_4\" index=\"0\">"+
		"<PARAM name=\"COSC_DIRNUM\" value=\"2181939587\"/>"+
		"<PARAM name=\"ADR_FNAME\" value=\"TESTE SGT EJB\"/>"+
		"<PARAM name=\"CS_CODE\" value=\"6.169181.10.11.100000\"/>"+
		"<PARAM name=\"CS_ID\" value=\"2420734\"/>"+
		"<PARAM name=\"CO_ID\" value=\"2937651\"/>"+
		"<PARAM name=\"CS_PAYMNT_RESP\" value=\"N\"/>"+
		"<PARAM name=\"CS_LEVEL\" value=\"40\"/>"+
		"</RECORD>"+
		"<RECORD name=\"LAH_LEVEL_4\" index=\"1\">"+
		"<PARAM name=\"COSC_DIRNUM\" value=\"2181230363\"/>"+
		"<PARAM name=\"ADR_FNAME\" value=\"TESTE SGT EJB\"/>"+
		"<PARAM name=\"CS_CODE\" value=\"6.169181.10.11.100001\"/>"+
		"<PARAM name=\"CS_ID\" value=\"2420735\"/>"+
		"<PARAM name=\"CO_ID\" value=\"2937652\"/>"+
		"<PARAM name=\"CS_PAYMNT_RESP\" value=\"N\"/>"+
		"<PARAM name=\"CS_LEVEL\" value=\"40\"/>"+
		"</RECORD>"+
		"</RECORD>"+
		"</RECORD>"+
		"</RECORD>"+
		"</INFOBUS_OUT_PARAMETERS>";
	
	//this response does not contains any account level
//	public static final String ACCOUNT_STRUCTURE_RESPONSE_XML = 
//		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"+
//		"<INFOBUS_OUT_PARAMETERS>"+
//		"<PARAM name=\"SRV_NAME\" value=\"db\"/>"+
//		"<PARAM name=\"SRV_HOST_NAME\" value=\"rjohpu12\"/>"+
//		"<PARAM name=\"SRV_IP_ADDRESS\" value=\"10.112.51.39\"/>"+
//		"<PARAM name=\"SRV_RESP_TIMESTAMP\" value=\"Thu Mar 20 13:34:07.466 2008\"/>"+
//		"<PARAM name=\"SERVICE\" value=\"BSDBHVLAQRY\"/>"+
//		"<PARAM name=\"SRV_RET_CODE\" value=\"0\"/>"+
//		"<PARAM name=\"SRV_RET_DESCRIPTION\" value=\"All services successfully executed\"/>"+
//		"<RECORD_GROUP/>"+	
//		"</INFOBUS_OUT_PARAMETERS>";
	
	//this response contains an error -20011
//	public static final String ACCOUNT_STRUCTURE_RESPONSE_XML = 
//		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"+
//		"<ib-msg>"+
//		"<ib-info>"+
//		"<msg-version>MWIB.MSG.XML-01.00-STD</msg-version>"+
//		"<msg-type>REPLY</msg-type>"+
//		"<t-info>"+
//		"<tid>7C6D541A47E279116F4CEF61</tid>"+
//		"<flow>BCBSHVLAQRY</flow>"+
//		"</t-info>"+
//		"<result>"+
//		"<svc>BCBSHVLAQRY</svc>"+
//		"<timestamp>Thu Mar 20 11:47:48.405 2008</timestamp>"+
//		"<type>IBCODE</type>"+
//		"<code>-20011</code>"+
//		"<desc>An exception occurred in the Sequencer object of the [db] adapter during the [BSDBHVLAQRY] operation. Additional context information = [Sequencer.executeCallerCommand()].</desc>"+
//		"<exception>"+
//		"<name>Sequencer</name>"+
//		"<msg>An exception occurred in the Sequencer object of the [db] adapter during the [BSDBHVLAQRY] operation. Additional context information = [Sequencer.executeCallerCommand()].</msg>"+
//		"<cause count=\"0\">"+
//		"<type>br.com.timbrasil.infobus.exception.CallerIBException</type>"+
//		"<msg>An exception occurred in the Caller object of the [DBAdapterTSW] adapter during the [BSDBHVLAQRY] operation. Additional context information = [executing TSW].</msg>"+
//		"</cause>"+
//		"<cause count=\"1\">"+
//		"<type>br.com.timbrasil.infobus.exception.MissingMandatoryArgumentIBException</type>"+
//		"<msg>The operation [convInParam] failed because the mandatory argument [/IB/adapter/db/tsw/methods/BSDBHVLAQRY/input-params/CS_CODE] was missed. Additional context information = [convertion, missed a mandatory input parameter].</msg>"+
//		"</cause>"+
//		"</exception>"+
//		"</result>"+
//		"</ib-info>"+
//		"<src-info>"+
//		"<hostname>rjohpu12</hostname>"+
//		"<ipaddr>10.112.51.39</ipaddr>"+
//		"<name>dbAdapter2</name>"+
//		"</src-info>"+
//		"<data/>"+
//		"</ib-msg>";
	
	//this response contains a contract info
	public static final String CONTRACT_INFO_RESPONSE_XML =
		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>																																											"+
		"<ib-msg>                                                                                                                         "+
		"<ib-info>                                                                                                                        "+
		"<msg-version>MWIB.MSG.XML-01.00-STD</msg-version>                                                                                "+
		"<msg-type>REPLY</msg-type>                                                                                                       "+
		"<t-info>                                                                                                                         "+
		"<tid>2AB4418447EAA29F29262697</tid>                                                                                              "+
		"<flow>BCBSBCCOQRY</flow>                                                                                                         "+
		"</t-info>                                                                                                                        "+
		"<result>                                                                                                                         "+
		"<svc>BCBSBCCOQRY</svc>                                                                                                           "+
		"<timestamp>Wed Mar 26 16:23:12.322 2008</timestamp>                                                                              "+
		"<type>IBCODE</type>                                                                                                              "+
		"<code>1</code>                                                                                                                   "+
		"<desc>Success</desc>                                                                                                             "+
		"</result>                                                                                                                        "+
		"</ib-info>                                                                                                                       "+
		"<src-info>                                                                                                                       "+
		"<hostname>rjohpu12</hostname>                                                                                                    "+
		"<ipaddr>10.112.51.39</ipaddr>                                                                                                    "+
		"<name>dbAdapter2</name>                                                                                                          "+
		"</src-info>                                                                                                                      "+
		"<data>                                                                                                                           "+
		"<contract>                                                                                                                       "+
		"<activation-date>26-AUG-03</activation-date>                                                                                     "+
		"<msisdn>1981223276</msisdn>                                                                                                      "+
		"<status>a</status>                                                                                                               "+
		"<service index=\"0\">                                                                                                              "+
		"<code>23</code>                                                                                                                  "+
		"<desc>Tarifa Zero</desc>                                                                                                         "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>030917a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"1\">                                                                                                              "+
		"<code>485</code>                                                                                                                 "+
		"<desc>Prediletos41 Serv 3 - FIXO</desc>                                                                                          "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>050621a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"2\">                                                                                                              "+
		"<code>62</code>                                                                                                                  "+
		"<desc>Deslocamento</desc>                                                                                                        "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>030826a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"3\">                                                                                                              "+
		"<code>157</code>                                                                                                                 "+
		"<desc>Servi�o B�sico GPRS</desc>                                                                                             "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>031005a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"4\">                                                                                                              "+
		"<code>156</code>                                                                                                                 "+
		"<desc>TIM Multim�dia-Fotomensagem</desc>                                                                                       "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>031005a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"5\">                                                                                                              "+
		"<code>208</code>                                                                                                                 "+
		"<desc>TIM TV</desc>                                                                                                              "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>030826a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"6\">                                                                                                              "+
		"<code>207</code>                                                                                                                 "+
		"<desc>TIM V�deo Mensagem</desc>                                                                                                "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>030826a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"7\">                                                                                                              "+
		"<code>202</code>                                                                                                                 "+
		"<desc>TIM Fotomensagem</desc>                                                                                                    "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>030826a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"8\">                                                                                                              "+
		"<code>38</code>                                                                                                                  "+
		"<desc>Fatura Detalhada Mensal</desc>                                                                                             "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>030826a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"9\">                                                                                                              "+
		"<code>1</code>                                                                                                                   "+
		"<desc>Servi�o de Voz</desc>                                                                                                    "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>030826a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"10\">                                                                                                             "+
		"<code>2</code>                                                                                                                   "+
		"<desc>TIM Torpedo Recebido </desc>                                                                                               "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>030826a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"11\">                                                                                                             "+
		"<code>3</code>                                                                                                                   "+
		"<desc>TIM Torpedo Originado</desc>                                                                                               "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>040922a|041013d|041022a|041112d|041117a|041213d|041218a|050113d|050118a|050214d|050221a</status-hist>               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"12\">                                                                                                             "+
		"<code>4</code>                                                                                                                   "+
		"<desc>Fax</desc>                                                                                                                 "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>040922a|041013d|041022a|041112d|041117a|041213d|041218a|050113d|050118a|050214d|050221a</status-hist>               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"13\">                                                                                                             "+
		"<code>22</code>                                                                                                                  "+
		"<desc>Adicional por chamada</desc>                                                                                               "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>030826a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"14\">                                                                                                             "+
		"<code>5</code>                                                                                                                   "+
		"<desc>TIM Connect / TIM Wap</desc>                                                                                               "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>040922a|041013d|041022a|041112d|041117a|041213d|041218a|050113d|050118a|050214d|050221a</status-hist>               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"15\">                                                                                                             "+
		"<code>6</code>                                                                                                                   "+
		"<desc>Caixa Postal (criar/excluir)</desc>                                                                                        "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>040922a|041013d|041022a|041112d|041117a|041213d|041218a|050113d|050118a|050214d|050221a</status-hist>               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"16\">                                                                                                             "+
		"<code>7</code>                                                                                                                   "+
		"<desc>Chamada em Espera</desc>                                                                                                   "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>040922a|041013d|041022a|041112d|041117a|041213d|041218a|050113d|050118a|050214d|050221a</status-hist>               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"17\">                                                                                                             "+
		"<code>8</code>                                                                                                                   "+
		"<desc>STRING1 STRING248</desc>                                                                                                   "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>040922a|041013d|041022a|041112d|041117a|041213d|041218a|050113d|050118a|050214d|050221a</status-hist>               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"18\">                                                                                                             "+
		"<code>9</code>                                                                                                                   "+
		"<desc>Siga-me</desc>                                                                                                             "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>040922a|041013d|041022a|041112d|041117a|041213d|041218a|050113d|050118a|050214d|050221a</status-hist>               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"19\">                                                                                                             "+
		"<code>13</code>                                                                                                                  "+
		"<desc>Reten��o de Chamada</desc>                                                                                             "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>040922a|041013d|041022a|041112d|041117a|041213d|041218a|050113d|050118a|050214d|050221a</status-hist>               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"20\">                                                                                                             "+
		"<code>14</code>                                                                                                                  "+
		"<desc>Confer�ncia</desc>                                                                                                       "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>040922a|041013d|041022a|041112d|041117a|041213d|041218a|050113d|050118a|050214d|050221a</status-hist>               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service index=\"21\">                                                                                                             "+
		"<code>53</code>                                                                                                                  "+
		"<desc>Operadora de Longa Dist�ncia</desc>                                                                                      "+
		"<contract-svc>                                                                                                                   "+
		"<status-hist>030826a</status-hist>                                                                                               "+
		"</contract-svc>                                                                                                                  "+
		"</service>                                                                                                                       "+
		"<service-size>22</service-size>                                                                                                  "+
		"<rate-plan>                                                                                                                      "+
		"<code>1</code>                                                                                                                   "+
		"<desc>Plano Nosso Modo-G01</desc>                                                                                                "+
		"</rate-plan>                                                                                                                     "+
		"</contract>                                                                                                                      "+
		"<customer>                                                                                                                       "+
		"<id>405112</id>                                                                                                                  "+
		"<address>                                                                                                                        "+
		"<contact-name>GEOGRAPHIC ENG. AVAL. GEOMATICA S/C LTDA</contact-name>                                                            "+
		"</address>                                                                                                                       "+
		"</customer>                                                                                                                      "+
		"</data>                                                                                                                          "+
		"</ib-msg>                                                                                                                        ";

	//this response does not contains a contract info 
//	public static final String CONTRACT_INFO_RESPONSE_XML =
//		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>" +
//		"<ib-msg>" +
//		   "infobus-info" +
//		   "source-info" +
//		   "<data/>" +
//		"</ib-msg>";
	
	//this response contains one package
	public static final String PACKAGES_LIST_RESPONSE_XML =	
		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>							"+
		"<ib-msg>                                                 "+
		"<ib-info>                                                "+
		"<msg-version>MWIB.MSG.XML-01.00-STD</msg-version>				"+
		"<msg-type>REPLY</msg-type>                               "+
		"<t-info>                                                 "+
		"<tid>48CF747B47EA8C8A333EBA7D</tid>                      "+
		"<flow>BCBSPACKQRY</flow>                                 "+
		"</t-info>                                                "+
		"<result>                                                 "+
		"<svc>BCBSPACKQRY</svc>                                   "+
		"<timestamp>Wed Mar 26 14:48:59.685 2008</timestamp>      "+
		"<type>IBCODE</type>                                      "+
		"<code>1</code>                                           "+
		"<desc>Success</desc>                                     "+
		"</result>                                                "+
		"</ib-info>                                               "+
		"<src-info>                                               "+
		"<hostname>rjohpu12</hostname>                            "+
		"<ipaddr>10.112.51.39</ipaddr>                            "+
		"<name>dbAdapter2</name>                                  "+
		"</src-info>                                              "+
		"<data>                                                   "+
		"<fu-pack index=\"0\">                                      "+
		"<activation-date/>                                       "+
		"<id>159</id>                                             "+
		"<desc>Pacote 40+ 40 min Promocionais</desc>              "+
		"</fu-pack>                                               "+
		"<fu-pack index=\"1\">                                      "+
		"<activation-date/>                                       "+
		"<id>159</id>                                             "+
		"<desc>Pacote 40+ 40 min Promocionais</desc>              "+
		"</fu-pack>                                               "+
		"<fu-pack index=\"2\">                                      "+
		"<activation-date/>                                       "+
		"<id>168</id>                                             "+
		"<desc>Pacote 40 min Promocionais</desc>                  "+
		"</fu-pack>                                               "+
		"<fu-pack-size>3</fu-pack-size>                           "+
		"</data>                                                  "+
		"</ib-msg>                                                ";
	
	//this response does not contain any package
//	public static final String PACKAGES_LIST_RESPONSE_XML =		
//		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"+
//		"<ib-msg>"+
//		"<ib-info>"+
//		"<msg-version>MWIB.MSG.XML-01.00-STD</msg-version>"+
//		"<msg-type>REPLY</msg-type>"+
//		"<t-info>"+
//		"<tid>1FCE40EA47E274704A021B92</tid>"+
//		"<flow>BCBSPACKQRY</flow>"+
//		"</t-info>"+
//		"<result>"+
//		"<svc>BCBSPACKQRY</svc>"+
//		"<timestamp>Thu Mar 20 11:28:03.879 2008</timestamp>"+
//		"<type>APPCODE</type>"+
//		"<code>1</code>"+
//		"<desc>Success</desc>"+
//		"</result>"+
//		"</ib-info>"+
//		"<src-info>"+
//		"<hostname>rjohpu12</hostname>"+
//		"<ipaddr>10.112.51.39</ipaddr>"+
//		"<name>dbAdapter2</name>"+
//		"</src-info>"+
//		"<data/>"+
//		"</ib-msg>";	
	
	//this response contains an erros -40000
//	public static final String PACKAGES_LIST_RESPONSE_XML =		
//		"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>"+
//		"<ib-msg>"+
//		"<ib-info>"+
//		"<msg-version>MWIB.MSG.XML-01.00-STD</msg-version>"+
//		"<msg-type>REPLY</msg-type>"+
//		"<t-info>"+
//		"<tid>1FCE40EA47E274704A021B92</tid>"+
//		"<flow>BCBSPACKQRY</flow>"+
//		"</t-info>"+
//		"<result>"+
//		"<svc>BCBSPACKQRY</svc>"+
//		"<timestamp>Thu Mar 20 11:28:03.879 2008</timestamp>"+
//		"<type>APPCODE</type>"+
//		"<code>-40000</code>"+
//		"<desc>Contract not found or deactivated</desc>"+
//		"</result>"+
//		"</ib-info>"+
//		"<src-info>"+
//		"<hostname>rjohpu12</hostname>"+
//		"<ipaddr>10.112.51.39</ipaddr>"+
//		"<name>dbAdapter2</name>"+
//		"</src-info>"+
//		"<data/>"+
//		"</ib-msg>";
	
}
