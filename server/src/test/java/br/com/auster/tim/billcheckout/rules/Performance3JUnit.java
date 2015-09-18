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
 * For this file, we expect the following times:
 *
 *    - Loading: ~25s
 *    - Guiding (Assert / Fire) : (~30s / ~8s)
 *    - Business Rules (Assert / Fire) : (~28s / ~ <1s)
 *    - Total : ~92s
 *
 * @author framos
 * @version $Id$
 *
 */
public class Performance3JUnit extends BasePerformanceJUnit {


	private static final String PERFORMANCE_BGH_3 = "bgh/PERFORMANCE3.BGH";


    /***************************************************
    	PERFORMANCE_BGH_2 file
     ***************************************************/

	public void testPerformance_PERFORMANCE3_R01_4() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_4, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE3_R01_4", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE3_R01_5_AND_6() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_5_AND_6, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE3_R01_5_AND_6", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE3_R01_7() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_7, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE3_R01_7", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE3_R01_8() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_8, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE3_R01_8", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE3_R12_1() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R12_1, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE3_R12_1", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE3_R01_12() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_12, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE3_R01_12", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE3_R01_18() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_18, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE3_R01_18", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE3_R02_3() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R02_3, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE3_R02_3", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE3_R04_5() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R04_5, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE3_R04_5", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE3_R04_6() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R04_6, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE3_R04_6", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE3_R15_1() {
		long init = System.currentTimeMillis();
		try {
			this.needTaxRate = true;
			runTest(RULES_R15_1, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.needTaxRate = false;
			this.times.put("testPerformance_PERFORMANCE3_R15_1", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE3_R01_9() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_9, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE3_R01_9", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_PERFORMANCE3_RX_1() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R10_x, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_PERFORMANCE3_RX_1", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	/**
	 * - Business Rules (Assert / Fire) : (~109s / ~2s)
	 * - Total : ~170s
	 */
	public void testPerformance_PERFORMANCE3_ALL_RULES() {
		long init = System.currentTimeMillis();
		try {
			this.needTaxRate = true;
			runTest(RULES_ALL, PERFORMANCE_BGH_3);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.needTaxRate = false;
			this.times.put("testPerformance_PERFORMANCE3_ALL", String.valueOf(System.currentTimeMillis() - init));
		}
	}

}
