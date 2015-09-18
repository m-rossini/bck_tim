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
 * @author framos
 * @version $Id$
 *
 */
public class ReteDumpJUnit extends BasePerformanceJUnit {


	private static final String RETEDUMP_BGH = "bgh/RETEDUMP.BGH";


    /***************************************************
    	RETEDUMP_BGH file
     ***************************************************/

	public void testPerformance_RETEDUMP_R01_4() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_4, RETEDUMP_BGH, "target/r01-4.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_RETEDUMP_R01_4", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_RETEDUMP_R01_5_AND_6() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_5_AND_6, RETEDUMP_BGH, "target/r01-5_6.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_RETEDUMP_R01_5_AND_6", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_RETEDUMP_R01_7() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_7, RETEDUMP_BGH, "target/r01-7.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_RETEDUMP_R01_7", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_RETEDUMP_R01_8() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_8, RETEDUMP_BGH, "target/r01-8.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_RETEDUMP_R01_8", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_RETEDUMP_R12_1() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R12_1, RETEDUMP_BGH, "target/r12-1.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_RETEDUMP_R12_1", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_RETEDUMP_R01_12() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_12, RETEDUMP_BGH, "target/r01-12.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_RETEDUMP_R01_12", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_RETEDUMP_R01_18() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_18, RETEDUMP_BGH, "target/r01-18.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_RETEDUMP_R01_18", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_RETEDUMP_R02_3() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R02_3, RETEDUMP_BGH, "target/r02-3.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_RETEDUMP_R02_3", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_RETEDUMP_R04_5() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R04_5, RETEDUMP_BGH, "target/r04-5.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_RETEDUMP_R04_5", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_RETEDUMP_R04_6() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R04_6, RETEDUMP_BGH, "target/r04-6.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_RETEDUMP_R04_6", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_RETEDUMP_R15_1() {
		long init = System.currentTimeMillis();
		try {
			this.needTaxRate = true;
			runTest(RULES_R15_1, RETEDUMP_BGH, "target/r015-1.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.needTaxRate = false;
			this.times.put("testPerformance_RETEDUMP_R15_1", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_RETEDUMP_R01_9() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R01_9, RETEDUMP_BGH, "target/r01-9.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_RETEDUMP_R01_9", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	public void testPerformance_RETEDUMP_RX_1() {
		long init = System.currentTimeMillis();
		try {
			runTest(RULES_R10_x, RETEDUMP_BGH, "target/rX-1.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.times.put("testPerformance_RETEDUMP_RX_1", String.valueOf(System.currentTimeMillis() - init));
		}
	}

	/**
	 * - Business Rules (Assert / Fire) : (~109s / ~2s)
	 * - Total : ~170s
	 */
	public void testPerformance_RETEDUMP_ALL_RULES() {
		long init = System.currentTimeMillis();
		try {
			this.needTaxRate = true;
			runTest(RULES_ALL, RETEDUMP_BGH, "target/all.dump");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		} finally {
			this.needTaxRate = false;
			this.times.put("testPerformance_RETEDUMP_ALL", String.valueOf(System.currentTimeMillis() - init));
		}
	}

}
