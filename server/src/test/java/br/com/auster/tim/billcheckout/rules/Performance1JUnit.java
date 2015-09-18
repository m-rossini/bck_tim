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
 * Created on 12/12/2006
 */
package br.com.auster.tim.billcheckout.rules;




/**
 * This JUnit test is meant to be run only on development environment.
 *
 * IMPORANT: Do not change the class name to run it on release time due to the
 *    amount of time it takes to finish.
 *
 * @author framos
 * @version $Id$
 *
 */
public class Performance1JUnit extends BasePerformanceJUnit {


	private static final String PERFORMANCE_BGH_1 = "bgh/PERFORMANCE1.BGH";



    /***************************************************
                   PERFORMANCE_BGH_1 file
     ***************************************************/
	public void testPerformance_PERFORMANCE1_R01_12() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_12, PERFORMANCE_BGH_1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE1_R01_12", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE1_R01_18() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_18, PERFORMANCE_BGH_1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE1_R01_18", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE1_R02_3() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R02_3, PERFORMANCE_BGH_1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE1_R02_3", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE1_R04_5() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R04_5, PERFORMANCE_BGH_1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE1_R04_5", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE1_R04_6() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R04_6, PERFORMANCE_BGH_1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE1_R04_6", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE1_R15_1() {
		long init = System.currentTimeMillis();
		try {
			this.needTaxRate = true;
			runTest(RULES_R15_1, PERFORMANCE_BGH_1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.needTaxRate = false;
			this.times.put("testPerformance_PERFORMANCE1_R15_1", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	/**
	 * Rules from F3
	 */
	public void testPerformance_PERFORMANCE1_R01_11() {
		long init = System.currentTimeMillis();
		try {
			this.needTaxRate = true;
			runTest(RULES_R01_11, PERFORMANCE_BGH_1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.needTaxRate = false;
			this.times.put("testPerformance_PERFORMANCE1_R01_11", String.valueOf(System.currentTimeMillis() - init));
		}
	}

}
