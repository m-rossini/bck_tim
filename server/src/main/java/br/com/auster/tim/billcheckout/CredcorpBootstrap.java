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
 * Created on 09/08/2006
 */
package br.com.auster.tim.billcheckout;

import org.w3c.dom.Element;


/**
 * A bootstrap class for Billcheckout's Credcorp loader.
 *
 * @author framos
 * @version $Id$
 */
public class CredcorpBootstrap extends br.com.auster.dware.Bootstrap {

  private static final String VERSION = "1.0";

  /**
   * @param configRoot
   */
  public CredcorpBootstrap() throws java.rmi.RemoteException {
    super();
  }

  /**
   * This method is called in order to check if the resource manage by this run
   * of data-aware is for the same product as this main class
   *
   * @return Returns 0 if the productID is the same or -1 otherwise
   */
  @Override
  protected int check() {
  	String productID = this.dataAware.getLicense().getProductID();
  	if (productID.charAt(0) == 'T' && productID.charAt(1) == 'I'
  			&& productID.charAt(2) == 'M' && productID.charAt(3) == 'B'
  			&& productID.charAt(4) == 'I' && productID.charAt(5) == 'L'
  			&& productID.charAt(6) == 'L' && productID.charAt(7) == 'C'
  			&& productID.charAt(8) == 'H' && productID.charAt(9) == 'E'
  			&& productID.charAt(10) == 'C' && productID.charAt(11) == 'K'
  			&& productID.charAt(12) == 'O' && productID.charAt(13) == 'U'
  			&& productID.charAt(14) == 'T' && productID.charAt(15) == '#'
  			&& productID.charAt(16) == 'V' && productID.charAt(17) == '3'
  			&& productID.charAt(18) == '.' && productID.charAt(19) == 'x') {
  		return 0;
  	} else {
  		return -1;
  	}
  }

  @Override
  protected final void preInit(Element config) throws Exception {
    log.info("AUSTER BILLCHECKOUT CREDCORP LOADER v" + VERSION + " - INITIALIZING");
    super.preInit(config);
  }

  public static void main(String[] args) throws Exception {
    CredcorpBootstrap boot = new CredcorpBootstrap();
    try {
      boot.execute(args);
    } catch (Throwable e) {
      e.printStackTrace();
      if (boot.dataAware != null) {
        boot.dataAware.shutdown(true);
      }
      System.exit(1);
    }
    log.info("AUSTER BILLCHECKOUT CREDCORP LOADER v" + VERSION + " - SHUTTING DOWN");
    System.exit(0);
  }

}
