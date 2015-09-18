/*
 * Copyright (c) 2004-2008 Auster Solutions. All Rights Reserved.
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
 * Created on 24/03/2008
 */
package br.com.auster.tim.billcheckout.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.TestCase;

/**
 * @author framos
 * @version $Id$
 *
 */
public class CycleCalculationTest extends TestCase {


	public void testCycleCalculation01() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		try {
			Date init = formatter.parse("01/03/08");
			Date startDate = CycleDates.calculateStartCycle(formatter.parse("02/03/08"), init);
			assertEquals(formatter.parse("01/03/08"), startDate);
			assertEquals(formatter.parse("31/03/08"), CycleDates.calculateEndCycle(startDate));
			startDate = CycleDates.calculateStartCycle(formatter.parse("29/03/08"), init);
			assertEquals(formatter.parse("01/03/08"), startDate);
			assertEquals(formatter.parse("31/03/08"), CycleDates.calculateEndCycle(startDate));
			startDate = CycleDates.calculateStartCycle(formatter.parse("03/04/08"), init);
			assertEquals(formatter.parse("01/04/08"), startDate);
			assertEquals(formatter.parse("30/04/08"), CycleDates.calculateEndCycle(startDate));
			startDate = CycleDates.calculateStartCycle(formatter.parse("06/02/08"), init);
			assertEquals(formatter.parse("01/02/08"), startDate);
			assertEquals(formatter.parse("29/02/08"), CycleDates.calculateEndCycle(startDate));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void testCycleCalculation02() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		try {
			Date init = formatter.parse("04/03/08");
			Date startDate = CycleDates.calculateStartCycle(formatter.parse("02/03/08"), init);
			assertEquals(formatter.parse("04/02/08"), startDate);
			assertEquals(formatter.parse("03/03/08"), CycleDates.calculateEndCycle(startDate));
			startDate = CycleDates.calculateStartCycle(formatter.parse("29/03/08"), init);
			assertEquals(formatter.parse("04/03/08"), startDate);
			assertEquals(formatter.parse("03/04/08"), CycleDates.calculateEndCycle(startDate));
			startDate = CycleDates.calculateStartCycle(formatter.parse("03/04/08"), init);
			assertEquals(formatter.parse("04/03/08"), startDate);
			assertEquals(formatter.parse("03/04/08"), CycleDates.calculateEndCycle(startDate));
			startDate = CycleDates.calculateStartCycle(formatter.parse("06/02/08"), init);
			assertEquals(formatter.parse("04/02/08"), startDate);
			assertEquals(formatter.parse("03/03/08"), CycleDates.calculateEndCycle(startDate));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void testCycleCalculation03() {
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
		try {
			Date init = formatter.parse("22/03/08");
			Date startDate = CycleDates.calculateStartCycle(formatter2.parse("02/03/08 22:02:01"), init);
			assertEquals(formatter.parse("22/02/08"), startDate);
			assertEquals(formatter.parse("21/03/08"), CycleDates.calculateEndCycle(startDate));
			startDate = CycleDates.calculateStartCycle(formatter2.parse("29/03/08 00:00:01"), init);
			assertEquals(formatter.parse("22/03/08"), startDate);
			assertEquals(formatter.parse("21/04/08"), CycleDates.calculateEndCycle(startDate));
			startDate = CycleDates.calculateStartCycle(formatter2.parse("03/04/08 10:33:01"), init);
			assertEquals(formatter.parse("22/03/08"), startDate);
			assertEquals(formatter.parse("21/04/08"), CycleDates.calculateEndCycle(startDate));
			startDate = CycleDates.calculateStartCycle(formatter.parse("06/02/08"), init);
			assertEquals(formatter.parse("22/01/08"), startDate);
			assertEquals(formatter.parse("21/02/08"), CycleDates.calculateEndCycle(startDate));
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
