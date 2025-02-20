/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.print;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;
import org.compiere.model.MRole;
import org.compiere.model.Query;
import org.adempiere.core.domains.models.X_AD_PrintFormatItem;
import org.compiere.util.CCache;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Evaluatee;
import org.compiere.util.Evaluator;
import org.compiere.util.Language;
import org.compiere.util.Util;

/**
 *	Print Format Item Model.
 * 	Caches Column Name
 *	(Add missing Items with PrintFormatUtil)
 *
 * 	@author 	Jorg Janke
 * 	@version 	$Id: MPrintFormatItem.java,v 1.3 2006/08/03 22:17:17 jjanke Exp $
 * 	@author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 * 	@See: https://github.com/adempiere/adempiere/issues/2873
 */
public class MPrintFormatItem extends X_AD_PrintFormatItem
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -679302944951915141L;

	/** Static Cache */
	private static CCache<Integer, MPrintFormatItem> printFormatItemCacheIds = new CCache<Integer, MPrintFormatItem>(Table_Name, 30);
	
	/**
	 *	Constructor
	 *  @param ctx context
	 *  @param AD_PrintFormatItem_ID AD_PrintFormatItem_ID
	 *	@param trxName transaction
	 */
	public MPrintFormatItem (Properties ctx, int AD_PrintFormatItem_ID, String trxName)
	{
		super (ctx, AD_PrintFormatItem_ID, trxName);
		//	Default Setting
		if (AD_PrintFormatItem_ID == 0)
		{
			setFieldAlignmentType(FIELDALIGNMENTTYPE_Default);
			setLineAlignmentType(LINEALIGNMENTTYPE_None);
			setPrintFormatType(PRINTFORMATTYPE_Text);
			setPrintAreaType(PRINTAREATYPE_Content);
			setShapeType(SHAPETYPE_NormalRectangle);
			//
			setIsCentrallyMaintained(true);
			setIsRelativePosition(true);
			setIsNextLine(false);
			setIsNextPage(false);
			setIsSetNLPosition(false);
			setIsFilledRectangle(false);
			setIsImageField(false);
			setXSpace(0);
			setYSpace(0);
			setXPosition(0);
			setYPosition(0);
			setMaxWidth(0);
			setIsFixedWidth(false);
			setIsHeightOneLine(false);
			setMaxHeight(0);
			setLineWidth(1);
			setArcDiameter(0);
			//
			setIsOrderBy(false);
			setSortNo(0);
			setIsGroupBy(false);
			setIsPageBreak(false);
			setIsSummarized(false);
			setIsAveraged(false);
			setIsCounted(false);
			setIsMinCalc(false);
			setIsMaxCalc(false);
			setIsVarianceCalc(false);
			setIsDeviationCalc(false);
			setIsRunningTotal(false);
			setImageIsAttached(false);
			setIsSuppressNull(false);
		}
	}	//	MPrintFormatItem

	/**
	 *	Constructor
	 *  @param ctx context
	 *  @param rs ResultSet
	 *	@param trxName transaction
	 */
	public MPrintFormatItem (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MPrintFormatItem

	/**	Locally cached column name			*/
	private String 					m_columnName = null;
	/** Locally cached label translations			*/
	private HashMap<String,String>	m_translationLabel;
	/** Locally cached suffix translations			*/
	private HashMap<String,String>	m_translationSuffix;

	private static CLogger		s_log = CLogger.getCLogger (MPrintFormatItem.class);

	
	
	/**
	 * Get/Load Print Format Item [CACHED]
	 * @param ctx context
	 * @param printFormatItemId
	 * @param trxName
	 * @return activity or null
	 */
	public static MPrintFormatItem getById(Properties ctx, int printFormatItemId, String trxName) {
		if (printFormatItemId <= 0)
			return null;

		MPrintFormatItem printFormatItem = printFormatItemCacheIds.get(printFormatItemId);
		if (printFormatItem != null && printFormatItem.get_ID() > 0)
			return printFormatItem;

		printFormatItem = new Query(ctx , Table_Name , COLUMNNAME_AD_PrintFormatItem_ID + "=?" , trxName)
				.setParameters(printFormatItemId)
				.first();
		if (printFormatItem != null && printFormatItem.get_ID() > 0) {
			printFormatItemCacheIds.put(printFormatItem.get_ID(), printFormatItem);
		}
		return printFormatItem;
	}
	
	/**************************************************************************
	 *	Get print name with language
	 * 	@param language language - ignored if IsMultiLingualDocument not 'Y'
	 * 	@return print name
	 */
	public String getPrintName (Language language)
	{
		if (language == null || Env.isBaseLanguage(language, "AD_PrintFormatItem"))
			return getPrintName();
		loadTranslations();
		String retValue = (String)m_translationLabel.get(language.getAD_Language());
		if (retValue == null || retValue.length() == 0)
			return getPrintName();
		return retValue;
	}	//	getPrintName

	/**
	 *	Get print name suffix with language
	 * 	@param language language - ignored if IsMultiLingualDocument not 'Y'
	 * 	@return print name suffix
	 */
	public String getPrintNameSuffix (Language language)
	{
		if (language == null || Env.isBaseLanguage(language, "AD_PrintFormatItem"))
			return getPrintNameSuffix();
		loadTranslations();
		String retValue = (String)m_translationSuffix.get(language.getAD_Language());
		if (retValue == null || retValue.length() == 0)
			return getPrintNameSuffix();
		return retValue;
	}	//	getPrintNameSuffix

	/**
	 * 	Load Translations
	 */
	private void loadTranslations()
	{
		if (m_translationLabel == null)
		{
			m_translationLabel = new HashMap<String,String>();
			m_translationSuffix = new HashMap<String,String>();
			String sql = "SELECT AD_Language, PrintName, PrintNameSuffix FROM AD_PrintFormatItem_Trl WHERE AD_PrintFormatItem_ID=?";
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = DB.prepareStatement(sql, get_TrxName());
				pstmt.setInt(1, get_ID());
				rs = pstmt.executeQuery();
				while (rs.next())
				{
					m_translationLabel.put (rs.getString (1), rs.getString (2));
					m_translationSuffix.put (rs.getString (1), rs.getString (3));
				}
			}
			catch (SQLException e)
			{
				log.log(Level.SEVERE, "loadTrl", e);
			}
			finally {
				DB.close(rs, pstmt);
				rs = null; pstmt = null;
			}
		}
	}	//	loadTranslations


	/**
	 * 	Type Field
	 *	@return true if field
	 */
	public boolean isTypeField()
	{
		return getPrintFormatType().equals(PRINTFORMATTYPE_Field);
	}
	/**
	 * 	Type Text
	 *	@return true if text
	 */
	public boolean isTypeText()
	{
		return getPrintFormatType().equals(PRINTFORMATTYPE_Text);
	}
	/**
	 * 	Type Print Format
	 *	@return true if print format
	 */
	public boolean isTypePrintFormat()
	{
		return getPrintFormatType().equals(PRINTFORMATTYPE_PrintFormat);
	}
	/**
	 * 	Type Image
	 *	@return true if image
	 */
	public boolean isTypeImage()
	{
		return getPrintFormatType().equals(PRINTFORMATTYPE_Image);
	}
	/**
	 * 	Type Box
	 *	@return true if box
	 */
	public boolean isTypeBox()
	{
		return getPrintFormatType().equals(PRINTFORMATTYPE_Line)
			|| getPrintFormatType().equals(PRINTFORMATTYPE_Rectangle);
	}

	/**
	 * 	Field Center
	 *	@return true if center
	 */
	public boolean isFieldCenter()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_Center);
	}
	/**
	 * 	Field Align Leading
	 *	@return true if leading
	 */
	public boolean isFieldAlignLeading()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_LeadingLeft);
	}
	/**
	 * 	Field Align Trailing
	 *	@return true if trailing
	 */
	public boolean isFieldAlignTrailing()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_TrailingRight);
	}
	/**
	 * 	Field Align Block
	 *	@return true if block
	 */
	public boolean isFieldAlignBlock()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_Block);
	}
	/**
	 * 	Field Align Default
	 *	@return true if default alignment
	 */
	public boolean isFieldAlignDefault()
	{
		return getFieldAlignmentType().equals(FIELDALIGNMENTTYPE_Default);
	}
	/**
	 * 	Line Align Center
	 *	@return true if center
	 */
	public boolean isLineAlignCenter()
	{
		return getLineAlignmentType().equals(LINEALIGNMENTTYPE_Center);
	}
	/**
	 * 	Line Align Leading
	 *	@return true if leading
	 */
	public boolean isLineAlignLeading()
	{
		return getLineAlignmentType().equals(LINEALIGNMENTTYPE_LeadingLeft);
	}
	/**
	 * 	Line Align Trailing
	 *	@return true if trailing
	 */
	public boolean isLineAlignTrailing()
	{
		return getLineAlignmentType().equals(LINEALIGNMENTTYPE_TrailingRight);
	}

	/**
	 * 	Header
	 *	@return true if area is header
	 */
	public boolean isHeader()
	{
		return getPrintAreaType().equals(PRINTAREATYPE_Header);
	}
	/**
	 * 	Content
	 *	@return true if area is centent
	 */
	public boolean isContent()
	{
		return getPrintAreaType().equals(PRINTAREATYPE_Content);
	}
	/**
	 * 	Footer
	 *	@return true if area is footer
	 */
	public boolean isFooter()
	{
		return getPrintAreaType().equals(PRINTAREATYPE_Footer);
	}
	
	/**
	 * 	Barcode
	 *	@return true if barcode selected
	 */
	public boolean isBarcode()
	{
		String s = getBarcodeType();
		return s != null && s.length() > 0;
	}
	
	
	/**************************************************************************
	 * 	String representation
	 * 	@return info
	 */
	public String toString()
	{
		StringBuffer sb = new StringBuffer("MPrintFormatItem[");
		sb.append("ID=").append(get_ID())
			.append(",Name=").append(getName())
			.append(",Print=").append(getPrintName())
			.append(", Seq=").append(getSeqNo())
			.append(",Sort=").append(getSortNo())
			.append(", Area=").append(getPrintAreaType())
			.append(", MaxWidth=").append(getMaxWidth())
			.append(",MaxHeight=").append(getMaxHeight())
			.append(",OneLine=").append(isHeightOneLine())
			.append(", Relative=").append(isRelativePosition());
		if (isRelativePosition())
			sb.append(",X=").append(getXSpace()).append(",Y=").append(getYSpace())
				.append(",LineAlign=").append(getLineAlignmentType())
				.append(",NewLine=").append(isNextLine())
				.append(",NewPage=").append(isPageBreak());
		else
			sb.append(",X=").append(getXPosition()).append(",Y=").append(getYPosition());
		sb.append(",FieldAlign=").append(getFieldAlignmentType());
		//
		sb.append(", Type=").append(getPrintFormatType());
		if (isTypeText())
			;
		else if (isTypeField())
			sb.append(",AD_Column_ID=").append(getAD_Column_ID());
		else if (isTypePrintFormat())
			sb.append(",AD_PrintFormatChild_ID=").append(getAD_PrintFormatChild_ID())
				.append(",AD_Column_ID=").append(getAD_Column_ID());
		else if (isTypeImage())
			sb.append(",ImageIsAttached=").append(isImageIsAttached()).append(",ImageURL=").append(getImageURL());
		//
		sb.append(", Printed=").append(isPrinted())
			.append(",SeqNo=").append(getSeqNo())
			.append(",OrderBy=").append(isOrderBy())
			.append(",SortNo=").append(getSortNo())
			.append(",Summarized=").append(isSummarized());
		sb.append("]");
		return sb.toString();
	}	//	toString


	/*************************************************************************/


	/**	Lookup Map of AD_Column_ID for ColumnName	*/
	private static CCache<Integer,String>	s_columns = new CCache<Integer,String>("AD_PrintFormatItem", 200);

	private static CCache<Integer,int[]>	s_processes = new CCache<Integer,int[]>("AD_PrintFormatItemProcess", 100);

	/**
	 * 	Get ColumnName from AD_Column_ID
	 *  @return ColumnName
	 */
	public String getColumnName()
	{
		if (m_columnName == null)	//	Get Column Name from AD_Column not index
			m_columnName = getColumnName (new Integer(getAD_Column_ID()));
		return m_columnName;
	}	//	getColumnName

	/**
	 * 	Get Column Name from AD_Column_ID.
	 *  Be careful not to confuse it with PO method getAD_Column_ID (index)
	 * 	@param AD_Column_ID column
	 * 	@return Column Name
	 */
	private static String getColumnName (Integer AD_Column_ID)
	{
		if (AD_Column_ID == null || AD_Column_ID.intValue() == 0)
			return null;
		//
		String retValue = (String)s_columns.get(AD_Column_ID);
		if (retValue == null)
		{
			String sql = "SELECT ColumnName FROM AD_Column WHERE AD_Column_ID=?";
			retValue = DB.getSQLValueString(null, sql, AD_Column_ID.intValue());
			if (retValue != null)
				s_columns.put(AD_Column_ID, retValue);
		}
		return retValue;
	}	//	getColumnName

	
	/**************************************************************************
	 * 	Create Print Format Item from Column
	 *  @param format parent
	 * 	@param AD_Column_ID column
	 *  @param seqNo sequence of display if 0 it is not printed
	 * 	@return Print Format Item
	 */
	public static MPrintFormatItem createFromColumn (MPrintFormat format, int AD_Column_ID, int seqNo)
	{
		MPrintFormatItem pfi = new MPrintFormatItem (format.getCtx(), 0, format.get_TrxName());
		pfi.setAD_PrintFormat_ID (format.getAD_PrintFormat_ID());
		pfi.setClientOrg(format);
		pfi.setAD_Column_ID(AD_Column_ID);
		pfi.setPrintFormatType(PRINTFORMATTYPE_Field);

		//	translation is done by trigger
		String sql = "SELECT c.ColumnName,e.Name,e.PrintName, "		//	1..3
			+ "c.AD_Reference_ID,c.IsKey,c.SeqNo "					//	4..6
			+ "FROM AD_Column c, AD_Element e "
			+ "WHERE c.AD_Column_ID=?"
			+ " AND c.AD_Element_ID=e.AD_Element_ID";
		//	translate base entry if single language - trigger copies to trl tables
		Language language = format.getLanguage();
		boolean trl = !Env.isMultiLingualDocument(format.getCtx()) && !language.isBaseLanguage();
		if (trl)
			sql = "SELECT c.ColumnName,e.Name,e.PrintName, "		//	1..3
				+ "c.AD_Reference_ID,c.IsKey,c.SeqNo "				//	4..6
				+ "FROM AD_Column c, AD_Element_Trl e "
				+ "WHERE c.AD_Column_ID=?"
				+ " AND c.AD_Element_ID=e.AD_Element_ID"
				+ " AND e.AD_Language=?";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, format.get_TrxName());
			pstmt.setInt(1, AD_Column_ID);
			if (trl)
				pstmt.setString(2, language.getAD_Language());
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				String ColumnName = rs.getString(1);
				pfi.setName(rs.getString(2));
				pfi.setPrintName(rs.getString(3));
				int displayType = rs.getInt(4);
				if (DisplayType.isNumeric(displayType))
					pfi.setFieldAlignmentType(FIELDALIGNMENTTYPE_TrailingRight);
				else if (displayType == DisplayType.Text || displayType == DisplayType.Memo )
					pfi.setFieldAlignmentType(FIELDALIGNMENTTYPE_Block);
				else
					pfi.setFieldAlignmentType(FIELDALIGNMENTTYPE_LeadingLeft);
				boolean isKey = "Y".equals(rs.getString(5));
				//
				if (isKey
					|| ColumnName.startsWith("Created") || ColumnName.startsWith("Updated")
					|| ColumnName.equals("AD_Client_ID") || ColumnName.equals("AD_Org_ID")
					|| ColumnName.equals("IsActive")
					|| displayType == DisplayType.Button || displayType == DisplayType.Binary
					|| displayType == DisplayType.ID || displayType == DisplayType.Image
					|| displayType == DisplayType.RowID
					|| seqNo == 0 )
				{
					pfi.setIsPrinted(false);
					pfi.setSeqNo(0);
				}
				else
				{
					pfi.setIsPrinted(true);
					pfi.setSeqNo(seqNo);
				}
				int idSeqNo = rs.getInt(6);	//	IsIdentifier SortNo
				if (idSeqNo > 0)
				{
					pfi.setIsOrderBy(true);
					pfi.setSortNo(idSeqNo);
				}
			}
			else
				s_log.log(Level.SEVERE, "Not Found AD_Column_ID=" + AD_Column_ID
					+ " Trl=" + trl + " " + language.getAD_Language());
		}
		catch (SQLException e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally {
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		if (!pfi.save())
			return null;
	//	pfi.dump();
		return pfi;
	}	//	createFromColumn

	/**
	 * 	Copy existing Definition To Client
	 * 	@param To_Client_ID to client
	 *  @param AD_PrintFormat_ID parent print format
	 * 	@return print format item
	 */
	public MPrintFormatItem copyToClient (int To_Client_ID, int AD_PrintFormat_ID)
	{
		MPrintFormatItem to = new MPrintFormatItem (p_ctx, 0, null);
		MPrintFormatItem.copyValues(this, to);
		to.setClientOrg(To_Client_ID, 0);
		to.setAD_PrintFormat_ID(AD_PrintFormat_ID);
		to.saveEx();
		
		if ( to.getAD_PrintFormatChild_ID() > 0 && to.getPrintFormatType().equals(MPrintFormatItem.PRINTFORMATTYPE_PrintFormat))
		{
			MPrintFormat child = (MPrintFormat) to.getAD_PrintFormatChild();
			if ( child != null )
			{
				MPrintFormat childCopy = MPrintFormat.copyToClient(p_ctx, to.getAD_PrintFormatChild_ID(), To_Client_ID);
				to.setAD_PrintFormatChild_ID(childCopy.getAD_PrintFormat_ID());
				to.saveEx();
			}
		}
		return to;
	}	//	copyToClient

	
	/**
	 * 	Before Save
	 *	@param newRecord
	 *	@return true if ok
	 */
	protected boolean beforeSave (boolean newRecord)
	{
		//	Order
		if (!isOrderBy())
		{
			setSortNo(0);
			setIsGroupBy(false);
			setIsPageBreak(false);
		}
		//	Rel Position
		if (isRelativePosition())
		{
			setXPosition(0);
			setYPosition(0);
		}
		else
		{
			setXSpace(0);
			setYSpace(0);
		}
		//	Image
		if (isImageField())
		{
			setImageIsAttached(false);
			setImageURL(null);
		}
		return true;
	}	//	beforeSave
	
	/**
	 * 	After Save
	 *	@param newRecord new
	 *	@param success success
	 *	@return success
	 */
	protected boolean afterSave (boolean newRecord, boolean success)
	{
		//	Set Translation from Element
		if (newRecord 
		//	&& MClient.get(getCtx()).isMultiLingualDocument()
			&& getPrintName() != null && getPrintName().length() > 0)
		{
			String sql = "UPDATE AD_PrintFormatItem_Trl trl "
				+ "SET PrintName = (SELECT e.PrintName "
					+ "FROM AD_Element_Trl e, AD_Column c "
					+ "WHERE e.AD_Language=trl.AD_Language"
					+ " AND e.AD_Element_ID=c.AD_Element_ID"
					+ " AND c.AD_Column_ID=" + getAD_Column_ID() + ") "
				+ "WHERE AD_PrintFormatItem_ID = " + get_ID()
				+ " AND EXISTS (SELECT * "
					+ "FROM AD_Element_Trl e, AD_Column c "
					+ "WHERE e.AD_Language=trl.AD_Language"
					+ " AND e.AD_Element_ID=c.AD_Element_ID"
					+ " AND c.AD_Column_ID=" + getAD_Column_ID()
					+ " AND trl.AD_PrintFormatItem_ID = " + get_ID() + ")"
				+ " AND EXISTS (SELECT * FROM AD_Client "
					+ "WHERE AD_Client_ID=trl.AD_Client_ID AND IsMultiLingualDocument='Y')";
			int no = DB.executeUpdate(sql, get_TrxName());
			log.fine("translations updated #" + no);
		}

		return success;
	}	//	afterSave
	
	public int[] getExecuteProcess ()
	{
		int[] p = s_processes.get(getAD_PrintFormatItem_ID());
		
		if (p == null) {
			String sql = "SELECT DISTINCT(AD_Process_ID)  " +
		             "FROM AD_ColumnProcess p " +
		             "INNER JOIN AD_PrintFormatItem pfi on pfi.ad_column_id = p.ad_column_id " +
		             "WHERE pfi.AD_PrintFormatItem_ID = ? " +
		             "AND EXISTS (SELECT * FROM AD_Process_Access WHERE AD_Process_ID = p.AD_Process_ID AND AD_Role_ID = ?)";
			p = DB.getIDsEx(get_TrxName(), sql  , getAD_PrintFormatItem_ID(), MRole.getDefault().getAD_Role_ID());
			s_processes.put(getAD_PrintFormatItem_ID(), p);
		}
		if (p.length == 0) {
			return null;
		}
		return p;
		
	}	//	getLines

	public boolean isDisplayed(PrintData printData) {
		if ( Util.isEmpty(getDisplayLogic() ))
			return true;
		boolean display = Evaluator.evaluateLogic(new Evaluatee() {

			@Override
			public String get_ValueAsString(String variableName) {
				Object obj = printData.getNode(variableName);
				if ( obj == null || !(obj instanceof PrintDataElement))
					return "";
				PrintDataElement data = (PrintDataElement) obj;
				if (data.isNull() )
					return "";
				return data.getValueAsString();
			}
		}, getDisplayLogic());

		return display;
	}
}	//	MPrintFormatItem
