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
 * Created on 23/02/2007
 */
package br.com.auster.tim.billcheckout.rules.duplicity;

import java.util.Calendar;

import org.apache.log4j.Logger;

import br.com.auster.billcheckout.rules.duplicity.UsageFileMetadata;
import br.com.auster.billcheckout.rules.duplicity.UsageHandler;
import br.com.auster.billcheckout.rules.duplicity.UsageHandlerFactory;
import br.com.auster.billcheckout.rules.duplicity.UsageHandlerFactoryException;
import br.com.auster.billcheckout.rules.duplicity.UsageRecord;
import br.com.auster.common.log.LogFactory;
import br.com.auster.tim.om.invoice.TIMUsageDetail;

/**
 * This is a wrapper class to a 
 * {@link br.com.auster.billcheckout.rules.duplicity.UsageHandler}. 
 * It uses a {@code TIMUsageDetail} to create an UsageFileMetadata and UsageRecord.
 *
 * @author rbarone
 * @version $Id$
 */
public class TIMDuplicityHandler {
  
  private static final Logger log = LogFactory.getLogger(TIMDuplicityHandler.class); 
  
  private static UsageHandler HANDLER;
  static {
    try {
      HANDLER = UsageHandlerFactory.getUsageHandlerInstance();
    } catch (UsageHandlerFactoryException e) {
      log.fatal("Could not instantiate the UsageHandler - duplicity rule will not work", e);
    }
  }
  
  public static boolean isDuplicated (String accountId, 
                                      String contractNumber, 
                                      TIMUsageDetail usage) {
    if (HANDLER == null) {
      throw new IllegalStateException("UsageHandler not initialized - cannot check for duplicated usages");
    }
    
    Calendar date = Calendar.getInstance();
    date.setTime( usage.getUsageDate() );
    int startDate = (date.get(Calendar.YEAR) * 10000) +
                    ((date.get(Calendar.MONTH)+1) * 100) +
                    date.get(Calendar.DAY_OF_MONTH);
    
    UsageFileMetadata metadata = new UsageFileMetadata(accountId, startDate);
    
    UsageRecord record = new UsageRecord();
    record.setStartSecondsWithinDay(usage.getUsageTimeInSeconds());
    record.setUsageDuration(usage.getUsageDuration().getSeconds());
    record.setFromNumber(usage.getChannelId());
    record.setToNumber(usage.getCalledNumber());
    record.setService(usage.getSvcId());
    
    return HANDLER.search(metadata, contractNumber, record) == null ? false : true;
  }
  
}
