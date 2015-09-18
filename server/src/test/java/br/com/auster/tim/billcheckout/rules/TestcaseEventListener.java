package br.com.auster.tim.billcheckout.rules;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.drools.event.ActivationCancelledEvent;
import org.drools.event.ActivationCreatedEvent;
import org.drools.event.AfterActivationFiredEvent;
import org.drools.event.AgendaEventListener;
import org.drools.event.BeforeActivationFiredEvent;
import org.drools.event.ObjectAssertedEvent;
import org.drools.event.ObjectModifiedEvent;
import org.drools.event.ObjectRetractedEvent;
import org.drools.event.WorkingMemoryEventListener;

public class TestcaseEventListener implements WorkingMemoryEventListener, AgendaEventListener {

	private static final Logger log = Logger.getLogger(TestcaseEventListener.class);


	private Map<String, Integer> assertCount = new HashMap<String, Integer>();
	private Map<String, Integer> agendaCount = new HashMap<String, Integer>();

	public void objectAsserted(ObjectAssertedEvent _event) {
		Integer counter = assertCount.get(_event.getObject().getClass().toString());
		if (counter == null) {
			counter = new Integer(0);
		}
		counter = new Integer(counter.intValue()+1);
		assertCount.put(_event.getObject().getClass().toString(), counter);
	}

	public void objectModified(ObjectModifiedEvent arg0) { }
	public void objectRetracted(ObjectRetractedEvent arg0) { }

	public void activationCancelled(ActivationCancelledEvent _event) {
		String ruleName = _event.getActivation().getRule().getName();
		Integer counter = agendaCount.get(ruleName);
		if (counter == null) {
			throw new IllegalStateException("cannot cancel activation that was not previously created!!!");
		}

		counter = new Integer(counter.intValue()-1);
		log.debug("Counter for rule " + ruleName + " subtracted by one. Now its " + counter);
		agendaCount.put(ruleName, counter);
	}

	public void activationCreated(ActivationCreatedEvent _event) {
		String ruleName = _event.getActivation().getRule().getName();
		Integer counter = agendaCount.get(ruleName);
		if (counter == null) {
			log.debug("No counter for rule " + ruleName + ". Initializing with zero.");
			counter = new Integer(0);
		}
		counter = new Integer(counter.intValue()+1);
		log.debug("Counter for rule " + ruleName + " added by one. Now its " + counter);
		agendaCount.put(ruleName, counter);
	}

	public void afterActivationFired(AfterActivationFiredEvent _event) {
		if (_event.getActivation().getActivationNumber() > 0) { return; }
		String ruleName = _event.getActivation().getRule().getName();
		Integer counter = agendaCount.get(ruleName);
		if (counter == null) {
			log.debug("No counter for rule " + ruleName + ". Initializing with zero.");
			counter = new Integer(0);
		}
		counter = new Integer(counter.intValue()+1);
		log.debug("Counter for rule " + ruleName + " added by one. Now its " + counter);
		agendaCount.put(ruleName, counter);
	}

	public void beforeActivationFired(BeforeActivationFiredEvent _event) {
	}


	public Map<String, Integer> getAgendaCount() {
		return Collections.unmodifiableMap(this.agendaCount);
	}

	public Map<String, Integer> getAssertCount() {
		return Collections.unmodifiableMap(this.assertCount);
	}
}
