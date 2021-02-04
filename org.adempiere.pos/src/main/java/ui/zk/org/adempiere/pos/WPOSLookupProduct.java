/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2014 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Raul Muñoz www.erpcya.com					              *
 *****************************************************************************/

package org.adempiere.pos;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.adempiere.pos.service.CPOS;
import org.adempiere.pos.service.POSLookupProductInterface;
import org.adempiere.webui.component.AutoComplete;
import org.adempiere.webui.window.FDialog;
import org.compiere.model.MProduct;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelMap;
import org.zkoss.zul.Timer;
import org.zkoss.zul.event.TreeDataEvent;
import org.zkoss.zul.event.TreeDataListener;

/**
 * Component allows to show product lookup search key , name , quantity available , price standard and price list
 * @author Mario Calderon, mario.calderon@westfalia-it.com, Systemhaus Westfalia, http://www.westfalia-it.com
 * @author Raul Muñoz, rmunoz@erpcya.com, ERPCYA http://www.erpcya.com
 */
public class WPOSLookupProduct extends AutoComplete implements EventListener {

	private static final long serialVersionUID = -2303830709901143774L;
    private POSLookupProductInterface lookupProductInterface = null;
    private boolean selectLock = false;
    private AutoComplete productLookupComboBox = null;
    private Integer priceListId = 0;
    private Integer warehouseId = 0;
    private Integer partnerId = 0;
    static private Integer PRODUCT_VALUE_LENGTH = 14;
    static private Integer PRODUCT_NAME_LENGTH = 50;
    static private Integer PRODUCT_UPC_LENGTH = 50;
    // static private Integer QUANTITY_LENGTH = 16;

    private String separator = "|";
    private String productValueTitle   = String.format("%1$-" + PRODUCT_VALUE_LENGTH + "s", Msg.parseTranslation(Env.getCtx() , "@ProductValue@"));
    private String productTitle        = String.format("%1$-" + PRODUCT_NAME_LENGTH + "s", Msg.parseTranslation(Env.getCtx() , "@M_Product_ID@"));
    private String productUPCTitle        = String.format("%1$-" + PRODUCT_UPC_LENGTH + "s", Msg.parseTranslation(Env.getCtx() , "@UPC@"));
    //  private String availableTitle      = String.format("%1$" + QUANTITY_LENGTH + "s", Msg.parseTranslation(Env.getCtx() , "@QtyAvailable@"));
    //  private String priceStdTitle       = String.format("%1$" + QUANTITY_LENGTH + "s", Msg.parseTranslation(Env.getCtx() , "@PriceStd@"));
    //  private String priceListTile       = String.format("%1$" + QUANTITY_LENGTH + "s", Msg.parseTranslation(Env.getCtx() , "@PriceList@"));
    private String title = "";

    private ArrayList<Integer> recordId = new ArrayList<Integer>();
    private int productId = -1;

	private static final int PopupDelayMillis = 900;

	private final Timer timer = new Timer(PopupDelayMillis);
	ListModel model = new ListModelMap();
	private String ignoreChar = "";
	private BigDecimal weight = Env.ONE;
    
	public WPOSLookupProduct (POSLookupProductInterface lookupProductInterface, WPOSTextField fieldProductName, long lastKeyboardEvent, String ignoreChar)
    {
        super();
        this.lookupProductInterface = lookupProductInterface;
        productLookupComboBox = new AutoComplete();
        this.setClass("input-search");
        this.setButtonVisible(false);
        this.addEventListener(Events.ON_FOCUS, this);
        this.addEventListener(Events.ON_SELECT, this);
       // this.addEventListener(Events.ON_OK, this);
        this.addEventListener(Events.ON_CHANGING, this);
        this.addEventListener(Events.ON_CHANGE, this);
        this.ignoreChar = ignoreChar;
        this.setModel(model);
        setFillingComponent(productLookupComboBox);
        productLookupComboBox.setStyle("Font-size:medium; font-weight:bold");
    }

    /**
     * Set Filling Component
     */
    public void setFillingComponent(AutoComplete productLookupComboBox) {
        this.productLookupComboBox = productLookupComboBox;
        char[] charArray = new char[200];
        Arrays.fill(charArray,' ');
        this.title = new StringBuffer()
                .append(productValueTitle).append(separator)
                .append(productTitle).append(separator)
                .append(productUPCTitle).append(separator).toString();
               //  .append(availableTitle).append(separator)
               // .append(priceStdTitle).append(separator)
               // .append(priceListTile)
        this.setText(this.title);
    }

    /**
     * Set Price List Version ID
     * @param priceListId
     */
    public void setPriceListId(int priceListId) {
        this.priceListId = priceListId;
    }

    /**
     * Set Warehouse ID
     * @param warehouseId
     */
    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    /**
     * Set Warehouse ID
     * @param warehouseId
     */
    public void setPartnerId(int partnerId) {
        this.partnerId = partnerId;
    }


	@Override
	public void onEvent(Event e) throws Exception {
		  if(e.getName().equals(Events.ON_CHANGE)){

          	if(this.getItemCount() == 1) {
          		this.setSelectedProductId(0);
          	}
	            if(this.getSelectedProductId() >= 0) {
	            	this.setSelectLock(false);
	            	this.captureProduct();
	            }
	          /*  else {
	            	//findProduct(true, lookupProduct.getValue());
	            }*/
          }

		if(e.getName().equals(Events.ON_FOCUS)) {
			setSelectionRange(0, getText().length());
		}
		else if(e.getName().equals(Events.ON_OK)) {
			//executeQuery(this.getValue());
			captureProduct();
		}
		else if(e.getName().equals(Events.ON_SELECT)){
			int index = this.getSelectedIndex();
			if(recordId.size() > index
					&& index >= 0) {
				productId = recordId.get(index);
			}
		}
	}
	
	/**
	 * Set Selected Record
	 * @param int index
	 */
	public void setSelectedProductId(int index) {
		if(recordId.size() > index
				&& index >= 0) {
			productId = recordId.get(index);
		}
		
	}
	/**
	 * Get Selected Record
	 * @return int ID
	 */
	public int getSelectedProductId() {
		return productId;
	}
	
	/**
	 * @param event
	 * @see TreeDataListener#onChange(TreeDataEvent)
	 */
	public void onChanging(InputEvent event) {
		showPopupDelayed();
		String value = event.getValue();
		StringBuffer weightBuffer;
		weight = Env.ONE;
		Event on_Ok = null;
		setSelectLock(true);
		
		if(event.getValue().startsWith(ignoreChar) && event.getValue().length() > 12) {
			value = event.getValue().substring(0, 7);
			weightBuffer = new StringBuffer(event.getValue().substring(7, 13)).insert(2, ".");
			weight = new BigDecimal(weightBuffer.toString());
			on_Ok = new Event(Events.ON_OK, this); 
		}
		if(event.getValue().length() > 12) {
		  setSelectLock(false);
		  if(on_Ok == null) {
			  on_Ok = new Event(Events.ON_OK, this);
		  }
		}
		
		if(!event.isChangingBySelectBack()){
        	executeQuery(value);
        	if(on_Ok != null) {
        		try {
    				onEvent(on_Ok);
    			} catch (Exception e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    			}
        	}
        }
        super.onChanging(event);
	}


	private void showPopupDelayed()
	{
		timer.setRepeats(false);
		timer.start();
	}

	/**
	 * Select Text
	 */
	public void selectText() {
		this.setSelectionRange(0, getText().length());
	}
	
    public void captureProduct() {
    	if(productId > 0 && !selectLock) {
    		this.setSelectLock(true);
            String productValue = MProduct.get(Env.getCtx(), productId).getName();
            this.setText(productValue + ".");
    		selectText();
    		this.setFocus(true);

            try {
                lookupProductInterface.findProduct(false, productId,weight);
                recordId.clear();
                productId = -1;
            } catch (Exception exception) {
                FDialog.error(0 ,exception.getLocalizedMessage());
            }
        }
        
    }

    /**
     * Execute Query
     * @param value
     */
    private void executeQuery(String value) {
        this.setOpen(false);
        
        if(value.trim().length() < 3) {
            return;
        }
        if(value.length() <= 0) {
            this.setText(title);
            this.removeAllItems();
            return;
        }

        productLookupComboBox.removeAllItems();

        recordId = new ArrayList<Integer>();
        productId = -1;
        Map<String,Integer> line = new TreeMap<String,Integer>();

        for (java.util.Vector<Object> columns : CPOS.getQueryProduct(productId, value, warehouseId, priceListId, partnerId))
        {
            
            String productValue = (String)columns.elementAt(1);
            String productName = (String)columns.elementAt(2);
            String productUPC = (String)columns.elementAt(3);
            /**  String qtyAvailable = (String)columns.elementAt(4);
            String priceStd =  (String)columns.elementAt(5);
            String priceList = (String)columns.elementAt(6); 
            **/
            StringBuilder lineString = new StringBuilder();
            lineString.append(String.format("%1$-" + PRODUCT_VALUE_LENGTH + "s", productValue)).append(separator)
              .append(String.format("%1$-" + PRODUCT_NAME_LENGTH + "s", productName)).append(separator)
              .append(String.format("%1$-" + PRODUCT_UPC_LENGTH + "s", productUPC)).append(separator);
           /** .append(String.format("%1$" + QUANTITY_LENGTH + "s", qtyAvailable)).append(separator)
              .append(String.format("%1$" + QUANTITY_LENGTH + "s", priceStd)).append(separator)
              .append(String.format("%1$" + QUANTITY_LENGTH + "s", priceList));
            **/
            line.put(lineString.toString(), (Integer)columns.elementAt(0));
        }

        String[] searchValues = new String[line.size()];
        String[] searchDescription = new String[line.size()];
        // Issue 137
        Iterator<String> it = line.keySet().iterator();
        int i = 0;
        while(it.hasNext()){
          String key = it.next();
          recordId.add(line.get(key));
          searchValues[i] = key;
          searchDescription[i] = " ";
          i++;
        }
        
        this.removeAllItems();
        model = new ListModelMap(line);
       
        this.setDict(searchValues);
        this.setDescription(searchDescription);
        this.setModel(model);
        this.open();
        
        if(line.size() > 1) {
        	setSelectLock(true);
        }
        if(line.size() == 1) {
        	setSelectedProductId(0);
        	//captureProduct();
        	//setSelectLock(false);
        	recordId.clear();
        }
    }
    
    public void setSelectLock(boolean selectLock) {
    	this.selectLock = selectLock;
    }


}