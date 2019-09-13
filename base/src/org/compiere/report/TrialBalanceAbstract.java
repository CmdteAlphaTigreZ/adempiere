/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.										*
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/

package org.compiere.report;

import java.sql.Timestamp;
import org.compiere.process.SvrProcess;

/** Generated Process for (Trial Balance)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.2
 */
public abstract class TrialBalanceAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "FinTrialBalance";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Trial Balance";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 310;
	/**	Parameter Name for Accounting Schema	*/
	public static final String C_ACCTSCHEMA_ID = "C_AcctSchema_ID";
	/**	Parameter Name for Period	*/
	public static final String C_PERIOD_ID = "C_Period_ID";
	/**	Parameter Name for Account Date	*/
	public static final String DATEACCT = "DateAcct";
	/**	Parameter Name for Organization	*/
	public static final String AD_ORG_ID = "AD_Org_ID";
	/**	Parameter Name for Account	*/
	public static final String ACCOUNT_ID = "Account_ID";
	/**	Parameter Name for Account Key	*/
	public static final String ACCOUNTVALUE = "AccountValue";
	/**	Parameter Name for Business Partner 	*/
	public static final String C_BPARTNER_ID = "C_BPartner_ID";
	/**	Parameter Name for Product	*/
	public static final String M_PRODUCT_ID = "M_Product_ID";
	/**	Parameter Name for Project	*/
	public static final String C_PROJECT_ID = "C_Project_ID";
	/**	Parameter Name for Activity	*/
	public static final String C_ACTIVITY_ID = "C_Activity_ID";
	/**	Parameter Name for Sales Region	*/
	public static final String C_SALESREGION_ID = "C_SalesRegion_ID";
	/**	Parameter Name for Campaign	*/
	public static final String C_CAMPAIGN_ID = "C_Campaign_ID";
	/**	Parameter Name for Posting Type	*/
	public static final String POSTINGTYPE = "PostingType";
	/**	Parameter Name for Reporting Hierarchy	*/
	public static final String PA_HIERARCHY_ID = "PA_Hierarchy_ID";
	/**	Parameter Value for Accounting Schema	*/
	private int acctSchemaId;
	/**	Parameter Value for Period	*/
	private int periodId;
	/**	Parameter Value for Account Date	*/
	private Timestamp dateAcct;
	/**	Parameter Value for Account Date(To)	*/
	private Timestamp dateAcctTo;
	/**	Parameter Value for Organization	*/
	private int orgId;
	/**	Parameter Value for Account	*/
	private int accountId;
	/**	Parameter Value for Account Key	*/
	private String accountValue;
	/**	Parameter Value for Account Key(To)	*/
	private String accountValueTo;
	/**	Parameter Value for Business Partner 	*/
	private int bPartnerId;
	/**	Parameter Value for Product	*/
	private int productId;
	/**	Parameter Value for Project	*/
	private int projectId;
	/**	Parameter Value for Activity	*/
	private int activityId;
	/**	Parameter Value for Sales Region	*/
	private int salesRegionId;
	/**	Parameter Value for Campaign	*/
	private int campaignId;
	/**	Parameter Value for Posting Type	*/
	private String postingType;
	/**	Parameter Value for Reporting Hierarchy	*/
	private int hierarchyId;

	@Override
	protected void prepare() {
		acctSchemaId = getParameterAsInt(C_ACCTSCHEMA_ID);
		periodId = getParameterAsInt(C_PERIOD_ID);
		dateAcct = getParameterAsTimestamp(DATEACCT);
		dateAcctTo = getParameterToAsTimestamp(DATEACCT);
		orgId = getParameterAsInt(AD_ORG_ID);
		accountId = getParameterAsInt(ACCOUNT_ID);
		accountValue = getParameterAsString(ACCOUNTVALUE);
		accountValueTo = getParameterToAsString(ACCOUNTVALUE);
		bPartnerId = getParameterAsInt(C_BPARTNER_ID);
		productId = getParameterAsInt(M_PRODUCT_ID);
		projectId = getParameterAsInt(C_PROJECT_ID);
		activityId = getParameterAsInt(C_ACTIVITY_ID);
		salesRegionId = getParameterAsInt(C_SALESREGION_ID);
		campaignId = getParameterAsInt(C_CAMPAIGN_ID);
		postingType = getParameterAsString(POSTINGTYPE);
		hierarchyId = getParameterAsInt(PA_HIERARCHY_ID);
	}

	/**	 Getter Parameter Value for Accounting Schema	*/
	protected int getAcctSchemaId() {
		return acctSchemaId;
	}

	/**	 Setter Parameter Value for Accounting Schema	*/
	protected void setAcctSchemaId(int acctSchemaId) {
		this.acctSchemaId = acctSchemaId;
	}

	/**	 Getter Parameter Value for Period	*/
	protected int getPeriodId() {
		return periodId;
	}

	/**	 Setter Parameter Value for Period	*/
	protected void setPeriodId(int periodId) {
		this.periodId = periodId;
	}

	/**	 Getter Parameter Value for Account Date	*/
	protected Timestamp getDateAcct() {
		return dateAcct;
	}

	/**	 Setter Parameter Value for Account Date	*/
	protected void setDateAcct(Timestamp dateAcct) {
		this.dateAcct = dateAcct;
	}

	/**	 Getter Parameter Value for Account Date(To)	*/
	protected Timestamp getDateAcctTo() {
		return dateAcctTo;
	}

	/**	 Setter Parameter Value for Account Date(To)	*/
	protected void setDateAcctTo(Timestamp dateAcctTo) {
		this.dateAcctTo = dateAcctTo;
	}

	/**	 Getter Parameter Value for Organization	*/
	protected int getOrgId() {
		return orgId;
	}

	/**	 Setter Parameter Value for Organization	*/
	protected void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	/**	 Getter Parameter Value for Account	*/
	protected int getAccountId() {
		return accountId;
	}

	/**	 Setter Parameter Value for Account	*/
	protected void setAccountId(int accountId) {
		this.accountId = accountId;
	}

	/**	 Getter Parameter Value for Account Key	*/
	protected String getAccountValue() {
		return accountValue;
	}

	/**	 Setter Parameter Value for Account Key	*/
	protected void setAccountValue(String accountValue) {
		this.accountValue = accountValue;
	}

	/**	 Getter Parameter Value for Account Key(To)	*/
	protected String getAccountValueTo() {
		return accountValueTo;
	}

	/**	 Setter Parameter Value for Account Key(To)	*/
	protected void setAccountValueTo(String accountValueTo) {
		this.accountValueTo = accountValueTo;
	}

	/**	 Getter Parameter Value for Business Partner 	*/
	protected int getBPartnerId() {
		return bPartnerId;
	}

	/**	 Setter Parameter Value for Business Partner 	*/
	protected void setBPartnerId(int bPartnerId) {
		this.bPartnerId = bPartnerId;
	}

	/**	 Getter Parameter Value for Product	*/
	protected int getProductId() {
		return productId;
	}

	/**	 Setter Parameter Value for Product	*/
	protected void setProductId(int productId) {
		this.productId = productId;
	}

	/**	 Getter Parameter Value for Project	*/
	protected int getProjectId() {
		return projectId;
	}

	/**	 Setter Parameter Value for Project	*/
	protected void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	/**	 Getter Parameter Value for Activity	*/
	protected int getActivityId() {
		return activityId;
	}

	/**	 Setter Parameter Value for Activity	*/
	protected void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	/**	 Getter Parameter Value for Sales Region	*/
	protected int getSalesRegionId() {
		return salesRegionId;
	}

	/**	 Setter Parameter Value for Sales Region	*/
	protected void setSalesRegionId(int salesRegionId) {
		this.salesRegionId = salesRegionId;
	}

	/**	 Getter Parameter Value for Campaign	*/
	protected int getCampaignId() {
		return campaignId;
	}

	/**	 Setter Parameter Value for Campaign	*/
	protected void setCampaignId(int campaignId) {
		this.campaignId = campaignId;
	}

	/**	 Getter Parameter Value for Posting Type	*/
	protected String getPostingType() {
		return postingType;
	}

	/**	 Setter Parameter Value for Posting Type	*/
	protected void setPostingType(String postingType) {
		this.postingType = postingType;
	}

	/**	 Getter Parameter Value for Reporting Hierarchy	*/
	protected int getHierarchyId() {
		return hierarchyId;
	}

	/**	 Setter Parameter Value for Reporting Hierarchy	*/
	protected void setHierarchyId(int hierarchyId) {
		this.hierarchyId = hierarchyId;
	}

	/**	 Getter Parameter Value for Process ID	*/
	public static final int getProcessId() {
		return ID_FOR_PROCESS;
	}

	/**	 Getter Parameter Value for Process Value	*/
	public static final String getProcessValue() {
		return VALUE_FOR_PROCESS;
	}

	/**	 Getter Parameter Value for Process Name	*/
	public static final String getProcessName() {
		return NAME_FOR_PROCESS;
	}
}