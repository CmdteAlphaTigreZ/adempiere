/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2003-2015 e-Evolution Consultants. All Rights Reserved.      *
 * Copyright (C) 2003-2015 Victor Pérez Juárez 								  *
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
 * Contributor(s): Victor Pérez Juárez  (victor.perez@e-evolution.com)		  *
 * Sponsors: e-Evolution Consultants (http://www.e-evolution.com/)            *
 *****************************************************************************/

package org.compiere.util;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBPartner;
import org.compiere.model.MBPartnerLocation;
import org.compiere.model.MProduct;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.Query;

/**
 * A util class for keep worked functionality
 */
public class RefactoryUtil {
	public static final int		DD_Order_Table_ID = 53037;
	public static final int		DD_OrderLine_Table_ID = 53038;
	public static final String	DD_Order_Table_Name = "DD_Order";
	public static final String	DD_OrderLine_Table_Name = "DD_OrderLine";
	public static final int		A_Asset_Addition_Table_ID = 53137;
	public static final String	A_Asset_Addition_Table_Name = "A_Asset_Addition";
	public static final int		A_Depreciation_Entry_Table_ID = 53121;
	public static final String	A_Depreciation_Entry_Table_Name = "A_Depreciation_Entry";
	public static final int		A_Asset_Disposed_Table_ID = 53127;
	public static final String	A_Asset_Disposed_Table_Name = "A_Asset_Disposed";
	public static final int		A_Asset_Table_ID = 539;
	public static final String	A_Asset_Table_Name = "A_Asset";
	public static final int		PP_Cost_Collector_Table_ID = 53035;
	public static final String	PP_Cost_Collector_Table_Name = "PP_Cost_Collector";
	public static final int		PP_Order_Table_ID = 53027;
	public static final String	PP_Order_Table_Name = "PP_Order";
	public static final int		PP_Order_BOMLine_Table_ID = 53025;
	public static final String	PP_Order_BOMLine_Table_Name = "PP_Order_BOMLine";
	public static final int		M_ForecastLine_Table_ID = 722;
	public static final String	M_ForecastLine_Table_Name = "M_ForecastLine";
	public static final int		PP_Product_BOMLine_Table_ID = 53019;
	public static final String	PP_Product_BOMLine_Table_Name = "PP_Product_BOMLine";
	/** Column name PP_Product_BOM_ID */
    public static final String PP_Product_BOMLine_PP_Product_BOM_ID = "PP_Product_BOM_ID";
	
	/** CostCollectorType AD_Reference_ID=53287 */
	public static final int COSTCOLLECTORTYPE_AD_Reference_ID=53287;
	/** Material Receipt = 100 */
	public static final String COSTCOLLECTORTYPE_MaterialReceipt = "100";
	/** Component Issue = 110 */
	public static final String COSTCOLLECTORTYPE_ComponentIssue = "110";
	/** Usege Variance = 120 */
	public static final String COSTCOLLECTORTYPE_UsegeVariance = "120";
	/** Method Change Variance = 130 */
	public static final String COSTCOLLECTORTYPE_MethodChangeVariance = "130";
	/** Rate Variance = 140 */
	public static final String COSTCOLLECTORTYPE_RateVariance = "140";
	/** Mix Variance = 150 */
	public static final String COSTCOLLECTORTYPE_MixVariance = "150";
	/** Activity Control = 160 */
	public static final String COSTCOLLECTORTYPE_ActivityControl = "160";
	
	/**
	 * Get BOM Lines from product
	 * @param product
	 * @return
	 */
	public static List<PO> getBOMLines(MProduct product) {
		final String whereClause = PP_Product_BOMLine_PP_Product_BOM_ID 
				+ " IN ( SELECT PP_PRODUCT_BOM_ID FROM PP_PRODUCT_BOM WHERE M_PRODUCT_ID = " + product.getM_Product_ID() + ")";
		return new Query(product.getCtx(), PP_Product_BOMLine_Table_Name, whereClause, null)
					.setClient_ID()
					.list();
	}
	
	/**
	 * Get BOMs for given product
	 * @param product
	 * @param isComponent
	 * @return list of MProductBOM
	 */
	public static List<PO> getBOMs(MProduct product) {
		StringBuffer whereClause = new StringBuffer();
		whereClause.append(PP_Product_BOMLine_PP_Product_BOM_ID);
		whereClause.append(" IN ( SELECT PP_Product_BOM_ID FROM PP_Product_BOM ");
		whereClause.append(" WHERE M_Product_ID = " + product.get_ID() + " ) ");
		
		return new Query(product.getCtx(), PP_Product_BOMLine_Table_Name, whereClause.toString(), null)
									.setOnlyActiveRecords(true)
									.setOrderBy("Line")
									.list();
	}
	
	/**
	 * Get a instance of Cost Collector
	 * @param context
	 * @param costCollectorId
	 * @param transactionName
	 * @return
	 */
	public static PO getCostCollector(Properties context, int costCollectorId, String transactionName) {
		return MTable.get(context, PP_Cost_Collector_Table_ID).getPO(costCollectorId, transactionName);
	}
	
	/**
	 * Get a instance of Distribution Order
	 * @param context
	 * @param distributionOrderId
	 * @param transactionName
	 * @return
	 */
	public static PO getDistributionOrder(Properties context, int distributionOrderId, String transactionName) {
		return MTable.get(context, DD_Order_Table_ID).getPO(distributionOrderId, transactionName);
	}
	
	/**
	 * Get Instance of Distribution Order Line
	 * @param context
	 * @param distributionOrderLineId
	 * @param transactionName
	 * @return
	 */
	public static PO getDistributionOrderLine(Properties context, int distributionOrderLineId, String transactionName) {
		return MTable.get(context, DD_OrderLine_Table_ID).getPO(distributionOrderLineId, transactionName);
	}
	
	/**
	 * Get Instance of Distribution Order Line from Distribution Order
	 * @param distributionOrder
	 * @return
	 */
	public static PO getDistributionOrderLineInstanceFromParent(PO distributionOrder) {
		PO distributionOrderLine = getDistributionOrderLine(distributionOrder.getCtx(), 0, distributionOrder.get_TrxName());
		distributionOrderLine.set_ValueOfColumn("DD_Order_ID", distributionOrder.get_ID());
		distributionOrderLine.setAD_Org_ID(distributionOrder.getAD_Org_ID());
		distributionOrderLine.set_ValueOfColumn("DateOrdered", distributionOrder.get_Value("DateOrdered"));
		distributionOrderLine.set_ValueOfColumn("DatePromised", distributionOrder.get_Value("DatePromised"));
		return distributionOrderLine;
	}
	
	/**
	 * Set Business Partner Reference
	 * @param referenceToSet
	 * @param bp
	 */
	public static void setBusinessPartner(PO referenceToSet, MBPartner bp) {
		if (bp == null)
			return;

		referenceToSet.set_ValueOfColumn("C_BPartner_ID", bp.getC_BPartner_ID());
		//	Defaults Payment Term
		int ii = 0;
		if (referenceToSet.get_ValueAsBoolean("IsSOTrx"))
			ii = bp.getC_PaymentTerm_ID();
		else
			ii = bp.getPO_PaymentTerm_ID();
		
		//	Default Price List
		if (referenceToSet.get_ValueAsBoolean("IsSOTrx"))
			ii = bp.getM_PriceList_ID();
		else
			ii = bp.getPO_PriceList_ID();
		//	Default Delivery/Via Rule
		String ss = bp.getDeliveryRule();
		if (ss != null)
			referenceToSet.set_ValueOfColumn("DeliveryRule", ss);
		ss = bp.getDeliveryViaRule();
		if (ss != null)
			referenceToSet.set_ValueOfColumn("DeliveryViaRule", ss);
		//	Default Invoice/Payment Rule
		ss = bp.getInvoiceRule();

		if (referenceToSet.get_ValueAsInt("SalesRep_ID") == 0)
		{
			ii = Env.getAD_User_ID(referenceToSet.getCtx());
			if (ii != 0)
				referenceToSet.set_ValueOfColumn("SalesRep_ID", ii);
		}

		List<MBPartnerLocation> partnerLocations = Arrays.asList(bp.getLocations(false));
		// search the Ship To Location
		MBPartnerLocation partnerLocation = partnerLocations.stream() 			// create steam
				.filter( pl -> pl.isShipTo()).reduce((first , last ) -> last) 	// get of last Ship to location
				.orElseGet(() -> partnerLocations.stream() 								// if not exist Ship to location else get first partner location
							.findFirst()										// if not exist partner location then throw an exception
							.orElseThrow(() -> new AdempiereException("@IsShipTo@ @NotFound@"))
				);

		referenceToSet.set_ValueOfColumn("C_BPartner_Location_ID", partnerLocation.getC_BPartner_Location_ID());
		//	
		Arrays.asList(bp.getContacts(false))
				.stream()
				.findFirst()
				.ifPresent(user -> referenceToSet.set_ValueOfColumn("AD_User_ID", user.getAD_User_ID()));
	}
	
}    //	MovementGenerate
