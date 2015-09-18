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
 * Created on 25/03/2010
 */
package br.com.auster.tim.billcheckout.util;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import br.com.auster.billcheckout.caches.CacheableKey;
import br.com.auster.billcheckout.caches.ReferenceDataCache;
import br.com.auster.tim.billcheckout.param.PlansVO;
import br.com.auster.tim.billcheckout.param.RateTimeZoneVO;
import br.com.auster.tim.billcheckout.param.ServicePlanVO;
import br.com.auster.tim.billcheckout.param.ServicesVO;
import br.com.auster.tim.billcheckout.param.TariffZoneUsageGroupVO;
import br.com.auster.tim.billcheckout.param.TariffZoneVO;
import br.com.auster.tim.billcheckout.param.UsageGroupLDVO;
import br.com.auster.tim.billcheckout.param.UsageGroupVO;
import br.com.auster.tim.om.invoice.ContractInfoSection;
import br.com.auster.tim.om.invoice.ContractUsageGroupSubsection;
import br.com.auster.tim.om.invoice.TIMUsageDetail;

/**
 * @author anardo
 */
public class GuidingHelper {

	private static final Logger log = Logger.getLogger(GuidingHelper.class);

	protected ReferenceDataCache serviceCache;
	protected ReferenceDataCache rateplanCache;
	protected ReferenceDataCache tariffZoneCache;
	protected ReferenceDataCache rateZoneCache;
	protected ReferenceDataCache usageGroupCache;
	protected ReferenceDataCache serviceAndPlanCache;
	protected ReferenceDataCache tariffAndUsageGroupCache;
	protected ReferenceDataCache usageGroupLDCache;
	
	protected static ThreadLocal<GuidingHelper> singleton = new ThreadLocal<GuidingHelper>();
	
	protected static ThreadLocal<Lock> singletonLock = new ThreadLocal<Lock>() {
		protected synchronized Lock initialValue() {
			return new ReentrantLock();
		}
	};
	
	public static GuidingHelper getInstance(ReferenceDataCache _serviceCache, ReferenceDataCache _rateplanCache, ReferenceDataCache _tariffZoneCache, 
			 	ReferenceDataCache _rateZoneCache, ReferenceDataCache _usageGroupCache, ReferenceDataCache _serviceAndPlanCache, 
			 	ReferenceDataCache _tariffAndUsageGroupCache, ReferenceDataCache _usageGroupLDCache) {
		
		Lock lock = singletonLock.get();
		if (singleton.get() == null) {
			try {
				lock.lockInterruptibly();
				// checking twice to lock code only when extremally necessary 
				if (singleton.get() == null) {
					singleton.set(new GuidingHelper(_serviceCache, _rateplanCache, _tariffZoneCache, _rateZoneCache,
							                     _usageGroupCache, _serviceAndPlanCache, _tariffAndUsageGroupCache,
							                     _usageGroupLDCache));
				}
			} catch (InterruptedException ie) {
				throw new RuntimeException(ie);
			} finally {
				lock.unlock();
			}
		}
		return singleton.get();
	}
	
	
	protected GuidingHelper(ReferenceDataCache _serviceCache, ReferenceDataCache _rateplanCache, ReferenceDataCache _tariffZoneCache, 
						 ReferenceDataCache _rateZoneCache, ReferenceDataCache _usageGroupCache, ReferenceDataCache _serviceAndPlanCache, 
						 ReferenceDataCache _tariffAndUsageGroupCache, ReferenceDataCache _usageGroupLDCache) {
		
		this.serviceCache = _serviceCache;
		this.rateplanCache = _rateplanCache;
		this.tariffZoneCache = _tariffZoneCache;
		this.rateZoneCache = _rateZoneCache;
		this.usageGroupCache = _usageGroupCache;
		this.serviceAndPlanCache = _serviceAndPlanCache;
		this.tariffAndUsageGroupCache = _tariffAndUsageGroupCache;
		this.usageGroupLDCache = _usageGroupLDCache;
	}
	
	public boolean validateDetailService(TIMUsageDetail _detail) {
		ServicesVO svc = (ServicesVO) this.serviceCache.getFromCache(ServicesVO.createAlternateKey(_detail.getSvcId()));
		if (svc != null) { 
			_detail.setServiceDescription(svc.getDescription());
		}
		return (svc != null);
	}

	public boolean validateDetailTariffZone(TIMUsageDetail _detail) {
		TariffZoneVO vo = (TariffZoneVO)this.tariffZoneCache.getFromCache(TariffZoneVO.createAlternateKey(_detail.getTariff()));
		if (vo == null) {
			return false;
		}
		if (vo.getTariffType() == null) {
			// do nothing
		} else if (vo.getTariffType().equals("D")) {
			_detail.markAsDataUsage();	
		} else if (vo.getTariffType().equals("V")) {
			_detail.markAsVoiceUsage();
		} else if (vo.getTariffType().equals("E")) {
			_detail.markAsEventUsage();
		}
		return true;
	}
	
	public boolean validateDetailRateZone(TIMUsageDetail _detail) {
		return (this.rateZoneCache.getFromCache(RateTimeZoneVO.createAlternateKey(_detail.getTariffClass())) != null);
	}
	
	public boolean validateRateplan(ContractInfoSection _contractInfo) {
		return (this.rateplanCache.getFromCache(PlansVO.createAlternateKey(_contractInfo.getRatePlan(), _contractInfo.getCarrierState())) != null);
	}
	
	public boolean validateUsageGroup(ContractUsageGroupSubsection _usageGroupSection) {
		return (this.usageGroupCache.getFromCache(UsageGroupVO.createAlternateKey(_usageGroupSection.getSectionName())) != null);
	}	
	
	public boolean isServiceAllowedInPlan(TIMUsageDetail _detail, ContractInfoSection _contractInfo) {
		if ((_detail.getSvcId() == null) || (_contractInfo.getRatePlan() == null)) {
			return false;
		}
		CacheableKey key = ServicePlanVO.createAlternateKey(_detail.getSvcId(), _contractInfo.getRatePlan(), _contractInfo.getInvoice().getAccount().getCarrierState());
		ServicePlanVO cachedVO = (ServicePlanVO)this.serviceAndPlanCache.getFromCache(key);
		return ((cachedVO != null) && cachedVO.isContained());
	}
	
	public boolean isTariffZoneAllowedInUsageGroup(TIMUsageDetail _detail, ContractUsageGroupSubsection _usageGroup) {
		if ((_detail.getTariff() == null) || (_usageGroup.getSectionName() == null)) {
			return false;
		}
		CacheableKey key = TariffZoneUsageGroupVO.createAlternateKey(_detail.getTariff(), _usageGroup.getSectionName());
		TariffZoneUsageGroupVO cachedVO = (TariffZoneUsageGroupVO) this.tariffAndUsageGroupCache.getFromCache(key);
		return ((cachedVO != null) && cachedVO.isAllowed());
	}

	public boolean isTariffZoneAllowedInLDUsageGroup(TIMUsageDetail detail, ContractUsageGroupSubsection usageGroup) {
		Object cachedObj = this.usageGroupLDCache.getFromCache(UsageGroupLDVO.createAlternateKey(usageGroup.getSectionName(), detail.getCarrierCode()));
		return (cachedObj != null);
	}
}
