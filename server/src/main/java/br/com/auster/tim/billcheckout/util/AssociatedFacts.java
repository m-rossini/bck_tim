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
 * Created on 27/12/2006
 */
package br.com.auster.tim.billcheckout.util;

import java.io.Serializable;

/**
 * @author framos
 * @version $Id$
 *
 */
public class AssociatedFacts implements Serializable {
	
	
	public static final int MAX_SIZE = 5;
	
	private String name;
	private Object[] facts;
	

	public AssociatedFacts(String _name) {
		this.name = _name;
		this.facts = new Object[MAX_SIZE];
	}
	
	public String getName() {
		return this.name;
	}
	
	public Object getObject1() {
		return this.facts[0];
	}
	public void setObject1(Object _obj) {
		this.facts[0] = _obj;
	}

	public Object getObject2() {
		return this.facts[1];
	}
	public void setObject2(Object _obj) {
		this.facts[1] = _obj;
	}

	public Object getObject3() {
		return this.facts[2];
	}
	public void setObject3(Object _obj) {
		this.facts[2] = _obj;
	}
	
	public Object getObject4() {
		return this.facts[3];
	}
	public void setObject4(Object _obj) {
		this.facts[3] = _obj;
	}

	public Object getObject5() {
		return this.facts[4];
	}
	public void setObject5(Object _obj) {
		this.facts[4] = _obj;
	}

	
}
