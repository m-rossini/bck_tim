/*
* Copyright (c) 2004-2005 Auster Solutions do Brasil. All Rights Reserved.
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
* Created on 29/08/2006
*/
//TODO Comment this Class
package br.com.auster.tim.billcheckout.util;

import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import br.com.auster.om.reference.PKEnabledEntity;


/**
 * @author mtengelm
 * @version $Id: CycleDates.java 1613 2008-08-19 16:01:35Z framos $
 */
public abstract class CycleDates extends PKEnabledEntity {

	private static final Logger log = Logger.getLogger(CycleDates.class);


	public static Date calculateStartCycle(Date _date, Date _init) {
		Calendar c = Calendar.getInstance();
		c.setTime(_init);
		int startDay = c.get(Calendar.DAY_OF_MONTH);
		c.setTime(_date);
		int currDay =  c.get(Calendar.DAY_OF_MONTH);
		if (startDay > currDay) {
			c.add(Calendar.MONTH, -1);
		}
		c.set(Calendar.DAY_OF_MONTH, startDay);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		log.debug("Calculated " + c.getTime() + " as startDate for " + _date);
		return c.getTime();
	}

	public static Date calculateEndCycle(Date _init) {
		Calendar c = Calendar.getInstance();
		c.setTime(_init);
		c.add(Calendar.MONTH, 1);
		c.add(Calendar.DAY_OF_YEAR, - 1);
		return c.getTime();
	}

}
