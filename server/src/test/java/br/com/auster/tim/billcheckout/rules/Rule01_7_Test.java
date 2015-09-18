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

import java.util.HashMap;
import java.util.HashSet;

import br.com.auster.billcheckout.consequence.Consequence;



/**
 * @author framos
 * @version $Id$
 *
 */
public class Rule01_7_Test extends BaseRuleTest {



	private String[] RULES = { "src/main/conf/rules/R1.7-usage-charge-info-validation.drl" };

	/**
	 * This is the correct file
	 */
	public void testAccount1() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r01_7/ACCOUNT1.BGH"));
			this.workingMemory.fireAllRules();
			// running over results list to check if consequences where ok
			HashMap<String, Integer>  counters = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				String k = c.getDescription();
				if (! counters.containsKey(k)) {
					counters.put(k, new Integer(0));
				}
				Integer d = counters.get(k);
				counters.put(k, new Integer(d.intValue() + 1));
			}
			assertEquals(6, this.results.size());
			assertNotNull(counters.get("Uso com origem inválido."));
			assertEquals(1, counters.get("Uso com origem inválido.").intValue());
			assertNotNull(counters.get("Uso com destino inválido."));
			assertEquals(2, counters.get("Uso com destino inválido.").intValue());
			assertNotNull(counters.get("Uso sem data de realização."));
			assertEquals(1, counters.get("Uso sem data de realização.").intValue());
			assertNotNull(counters.get("Uso sem número chamado."));
			assertEquals(2, counters.get("Uso sem número chamado.").intValue());

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	/**
	 * This is the correct file
	 */
	public void testAccount2() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r01_7/ACCOUNT2.BGH"));
			this.workingMemory.fireAllRules();
			// building set of messages
			HashSet<String> messages = new HashSet<String>();
			for (Consequence c : this.results) {
				messages.add(c.getDescription());
			}
			assertEquals(8, this.results.size());
			assertFalse(messages.contains("Uso sem número chamador."));
			assertTrue(messages.contains("Uso sem número chamado."));
			assertTrue(messages.contains("Uso sem data de realização."));
			assertTrue(messages.contains("Uso sem horário de realização."));
			assertTrue(messages.contains("Uso com destino inválido."));
			assertTrue(messages.contains("Uso com valor inválido."));
			assertTrue(messages.contains("Uso com serviço inválido."));
			assertTrue(messages.contains("Uso com classe tarifária inválida."));
			assertTrue(messages.contains("Uso com origem inválido."));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}


	public void testAccount3() {
		try {
			// firing rules
			this.startupRuleEngine(RULES);
			this.assertAccount(this.loadBGHTestFile("bgh/r01_7/ACCOUNT3.BGH"));
			this.workingMemory.fireAllRules();
			// building set of messages
			HashMap<String, Integer> messages = new HashMap<String, Integer>();
			for (Consequence c : this.results) {
				if (!messages.containsKey(c.getDescription())) {
					messages.put(c.getDescription(), new Integer(1));
				} else {
					Integer i = messages.get(c.getDescription());
					messages.put(c.getDescription(), new Integer(i.intValue() + 1));
				}

			}
			assertEquals(13, this.results.size());
			assertTrue(messages.containsKey("Uso sem número chamador."));
			assertEquals(5, messages.get("Uso sem número chamador.").intValue());
			assertTrue(messages.containsKey("Uso sem data de realização."));
			assertEquals(2, messages.get("Uso sem data de realização.").intValue());
			assertTrue(messages.containsKey("Uso sem horário de realização."));
			assertEquals(1, messages.get("Uso sem horário de realização.").intValue());
			assertTrue(messages.containsKey("Uso com destino inválido."));
			assertEquals(1, messages.get("Uso com destino inválido.").intValue());
			assertTrue(messages.containsKey("Uso com valor inválido."));
			assertEquals(1, messages.get("Uso com valor inválido.").intValue());
			assertTrue(messages.containsKey("Uso com classe tarifária inválida."));
			assertEquals(1, messages.get("Uso com classe tarifária inválida.").intValue());
			assertTrue(messages.containsKey("Uso com origem inválido."));
			assertEquals(1, messages.get("Uso com origem inválido.").intValue());
			assertTrue(messages.containsKey("Uso com serviço inválido."));
			assertEquals(1, messages.get("Uso com serviço inválido.").intValue());
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
