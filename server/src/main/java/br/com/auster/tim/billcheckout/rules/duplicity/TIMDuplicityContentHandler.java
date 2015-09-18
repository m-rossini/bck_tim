/*
 * Copyright (c) 2004-2006 Auster Solutions. All Rights Reserved.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on 02/02/2007
 */
package br.com.auster.tim.billcheckout.rules.duplicity;

import java.util.Calendar;

import org.w3c.dom.Element;
import org.xml.sax.Attributes;

import br.com.auster.billcheckout.rules.duplicity.DuplicityContentHandler;
import br.com.auster.billcheckout.rules.duplicity.DuplicityContentHandlerFilter;
import br.com.auster.billcheckout.rules.duplicity.UsageFileMetadata;
import br.com.auster.billcheckout.rules.duplicity.UsageRecord;
import br.com.auster.common.xml.sax.DefaultSAXTemplate;
import br.com.auster.common.xml.sax.SAXStylesheet;
import br.com.auster.common.xml.sax.XMLPath;
import br.com.auster.tim.bgh.sax.BGHDataFormats;

/**
 * TODO: class comments
 *
 * @author rbarone
 * @version $Id$
 */
public class TIMDuplicityContentHandler extends DuplicityContentHandler {
  
  protected static final int CENTURY = Calendar.getInstance().get(Calendar.YEAR) / 100; 

  protected static final XMLPath ROOT_PATH = XMLPath.createRoot();
  static {
    XMLPath child = ROOT_PATH.addChild("IFD");
    child = child.addChild("GeneralInformation").addTemplate( new GeneralInformationTemplate() );
    child = child.addChild("Details");
    child = child.addChild("ContractInfo").addTemplate( new ContractInfoTemplate() );
    child = child.addChild("UsageGroup");
    child = child.addChild("OneCall").addTemplate( new OneCallTemplate() );
  }
  
  protected String accountId;
  
  protected String currentSubscriptionId;
  
  protected String currentSubscriberNumber;
  
  protected UsageFileMetadata currentMetadata;

  public TIMDuplicityContentHandler(Element config, DuplicityContentHandlerFilter filter) {
    super(config, filter);
    setRoot(ROOT_PATH);
  }
  
  public void onStartDocument() {
    this.currentMetadata = null;
    this.accountId = null;
    this.currentSubscriptionId = null;
    this.currentSubscriberNumber = null;
  }
  
  private static int getDateAsInteger(String date) {
    if (date == null || date.length() != 8) {
      throw new IllegalArgumentException("Invalid date: '" + date + "' - must in 'dd/MM/yy' format");
    }
    StringBuilder b = new StringBuilder();
    b.append(CENTURY);
    b.append(date.substring(6));
    b.append(date.substring(3, 5));
    b.append(date.substring(0, 2));
    return Integer.valueOf(b.toString());
  }
  
  private static int getSecondsFromTime(String time) {
    if (time == null || time.length() != 8) {
      throw new IllegalArgumentException("Invalid time: '" + time + "' - must in 'HH:mm:ss' format");
    }
    int hour = Integer.valueOf(time.substring(0, 2)) * 3600;
    int min = Integer.valueOf(time.substring(3, 5)) * 60;
    int sec = Integer.valueOf(time.substring(6));
    return hour + min + sec;
  }
  
  private static long getDuration(String value) {
    return BGHDataFormats.buildUnitCounter(value).getUnits();
  }

  
  //##########################################
  // SAX Templates
  //##########################################
  
  /**
   * [template match="/IFD/GeneralInformation"]
   */
  static class GeneralInformationTemplate extends DefaultSAXTemplate {
    public void onStartElement(SAXStylesheet stylesheet, String uri, String localName, 
                               String qName, Attributes atts) {
      TIMDuplicityContentHandler h = (TIMDuplicityContentHandler) stylesheet;
      h.accountId = atts.getValue("customerID");
    }
  }
  
  /**
   * [template match="/IFD/GeneralInformation/Details/ContractInfo"]
   */
  static class ContractInfoTemplate extends DefaultSAXTemplate {
    public void onStartElement(SAXStylesheet stylesheet, String uri, String localName, 
                               String qName, Attributes atts) {
      TIMDuplicityContentHandler h = (TIMDuplicityContentHandler) stylesheet;
      h.currentSubscriptionId = atts.getValue("contractNumber");
      h.currentSubscriberNumber = atts.getValue("directoryNumber");
    }
  }
  
  /**
   * [template match="/IFD/GeneralInformation/Details/ContractInfo/UsageGroup/OneCall"]
   */
  static class OneCallTemplate extends DefaultSAXTemplate {
    public void onStartElement(SAXStylesheet stylesheet, String uri, String localName, 
                               String qName, Attributes atts) {
      TIMDuplicityContentHandler h = (TIMDuplicityContentHandler) stylesheet;
      int startDate = getDateAsInteger( atts.getValue("callDate") );
      if (h.currentMetadata == null || 
          !h.currentMetadata.getAccountId().equals(h.accountId) ||
          h.currentMetadata.getDate() != startDate) {
          h.currentMetadata = new UsageFileMetadata(h.accountId, 
                                                    startDate);
      }
      
      UsageRecord u = new UsageRecord();
      u.setStartSecondsWithinDay( getSecondsFromTime(atts.getValue("callTime")) );
      u.setUsageDuration( getDuration(atts.getValue("callDuration")) );
      u.setFromNumber( h.currentSubscriberNumber );
      u.setToNumber( atts.getValue("destinationNumber") );
      u.setService( atts.getValue("serviceShortDescr") );
      h.filter.get().addUsageRecord(h.currentMetadata, h.currentSubscriptionId, u);
    }
  }
  
}
