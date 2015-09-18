/*
 * Copyright (c) 2004-2007 Auster Solutions. All Rights Reserved.
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
 * Created on 28/09/2007
 */
package br.com.auster.tim.billcheckout.util;

import java.util.Comparator;

import org.apache.log4j.Logger;

import br.com.auster.common.datastruct.IntRangeList.IntRangeNode;
import br.com.auster.tim.om.invoice.TIMUsageDetail;

/**
 * @author framos
 * @version $Id$
 *
 */
public class UsageOverlapComparator implements Comparator<IntRangeNode> {


	private static final Logger log = Logger.getLogger(UsageOverlapComparator.class);


	/**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
    public int compare(IntRangeNode node1, IntRangeNode node2) {
		try {
			int comparison = Integer.valueOf(node1.getFrom()).compareTo(Integer.valueOf(node2.getFrom()));
			if (comparison != 0) {
				return comparison;
			}
			// if from is equal, check to
			comparison = Integer.valueOf(node1.getTo()).compareTo(Integer.valueOf(node2.getTo()));
			if (comparison != 0) {
				return comparison;
			}
			// if from & to are equal, check called number
			TIMUsageDetail detail1 = (TIMUsageDetail) node1.getValue();
			TIMUsageDetail detail2 = (TIMUsageDetail) node2.getValue();
			comparison = detail1.getCalledNumber().compareTo(detail2.getCalledNumber());
			if (comparison != 0) {
				return comparison;
			}
			// if still equals, compare service
			return detail1.getSvcId().compareTo(detail2.getSvcId());
		} catch (Exception e) {
			log.info("Exception while running node comparator", e);
		}
		return 0;
	}
}
