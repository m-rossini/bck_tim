/*
 * Copyright (c) 2004-2010 Auster Solutions. All Rights Reserved.
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
 * Created on 30/04/2010
 */
package br.com.auster.tim.billcheckout.tariff.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author framos
 */
public class MeuSonhoTariff {

	private String state;
	private String packageName;
	private List tariffZoneList;
	private Date loadedDate;
	private double ppm;
	
	
	
	
	public MeuSonhoTariff(String packageName, Date loadedDate, double ppm, String state ) {
		super();
		this.state = state;
		this.packageName = packageName;
		this.tariffZoneList = new ArrayList();
		this.loadedDate = loadedDate;
		this.ppm = ppm;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	
	public void setTarrifZoneList(List _tariffZoneList) {
		this.tariffZoneList = _tariffZoneList;
	}
	public List getTariffZoneList() {
		return tariffZoneList;
	}
	public void addTariffZone(String tariffZone) {
		this.tariffZoneList.add(tariffZone);
	}
	public Date getLoadedDate() {
		return loadedDate;
	}
	public void setLoadedDate(Date loadedDate) {
		this.loadedDate = loadedDate;
	}
	public double getPpm() {
		return ppm;
	}
	public void setPpm(double ppm) {
		this.ppm = ppm;
	}
	
	
	public boolean isSameKey(MeuSonhoTariff _other) {
		if (_other == null) {
			return false;
		}
		
		boolean result = true;
		// check package name
		if (_other.getPackageName() != null) {
			result = result && _other.getPackageName().equals(this.getPackageName());
		} else {
			result = result && (this.getPackageName() == null);
		}
		// check loaded date
		if (_other.getLoadedDate() != null) {
			result = result && _other.getLoadedDate().equals(this.getLoadedDate());
		} else {
			result = result && (this.getLoadedDate() == null);
		}
		result = result && (_other.getPpm()== this.getPpm());
		return result;
	}
	
}
