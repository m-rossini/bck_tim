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
 * Created on 24/04/2008
 */
package br.com.auster.tim.billcheckout.model;

import java.util.Set;
import java.util.TreeSet;

import br.com.auster.om.reference.CustomizableEntity;

/**
 * TODO What this class is responsible for
 *
 * @author William Soares
 * @version $Id$
 * @since JDK1.4
 */
public class ProgressiveDiscountPlans extends CustomizableEntity {


	/**
	 * Used to store the values of  <code>serialVersionUID</code>.
	 */
	private static final long serialVersionUID = 1L;


	// ---------------------------
	// atributes
	// ---------------------------
	private long discountUid;
	private Set plansUid;


	public ProgressiveDiscountPlans() {
		this.plansUid = new TreeSet();
	}


	public final long getDiscountUid() {
		return discountUid;
	}

	public final void setDicountUid(long _uid) {
		this.discountUid = _uid;
	}

	public void addPlan(String _uid) {
		this.plansUid.add(_uid);
	}

	public void clearPlans() {
		this.plansUid.clear();
	}

	public Set getPlans() {
		return this.plansUid;
	}

	public boolean containsPlanUid(long _uid) {
		return this.plansUid.contains( String.valueOf(_uid));
	}

}
