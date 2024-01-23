package com.e3ps.change.editor;

import java.awt.event.*;

import javax.swing.*;

import java.awt.*;

import javax.swing.event.*;
import javax.swing.table.*;

import com.e3ps.common.iba.IBAUtil;

import java.rmi.RemoteException;
import java.util.*;
import java.net.*;

import wt.query.*;
import wt.fc.*;
import wt.part.*;
import wt.util.WTException;
import wt.vc.wip.*;
import wt.vc.*;


public class PData  extends AbstractTableModel{
    protected Vector m_vector;
	protected boolean m_sortAsc;
	protected int  m_sortCol;
	
    static final public ColumnData m_columns[]={
    new ColumnData( "Number", 160, JLabel.LEFT ),
    new ColumnData( "Name", 220, JLabel.LEFT ),
    new ColumnData( "Version", 100, JLabel.CENTER ),
    //new ColumnData( "Type", 80, JLabel.CENTER ),
	new ColumnData( "Creator", 80, JLabel.CENTER ),
    new ColumnData( "LastUpdated", 160, JLabel.CENTER )
  };
  protected int m_columnsCount = m_columns.length;
  public PData(){
	  m_vector=new Vector();
  }
  public int getRowCount(){
    return m_vector==null ? 0 : m_vector.size(); 
  }
  public void removeAll(){
	  m_vector.removeAllElements();
  }
  public String getColumnName(int column) {
    String str = m_columns[column].m_title;
    return str;
  }
  public int getColumnCount() { 
    return m_columns.length; 
  }
  public void addData(WTPart data){
     m_vector.add(new WTPartData(data));
  }
  public boolean isCellEditable(int nRow, int nCol) {
    return false;
  }
  public WTPart getEPM(int nRow){
	  WTPartData row = (WTPartData)m_vector.elementAt(nRow);
	  return row.part;
  }
   public WTPartData getData(int nRow){
	 return (WTPartData)m_vector.elementAt(nRow);
  }
   public Object getValueAt(int nRow, int nCol) {
    if (nRow < 0 || nRow>=getRowCount())
      return "";
    WTPartData row = (WTPartData)m_vector.elementAt(nRow);
    switch (nCol) {
	  case 0: return row.number;
      case 1: return row.name;
      case 2: return row.version;
     // case 3: return row.type;
	 case 3: return row.creator;
      case 4: return row.lastupdate;
    }
    return "";
  }
}

class WTPartData
{    
	 WTPart part;
     public IconData number;
	 String name;
	 String version;
	 String type;
	 String lastupdate;
	 String creator;
	 ImageIcon icon;
	public static ImageIcon icon1;
	public static ImageIcon icon2;
    public static ImageIcon icon3;
    static{
	 try{
	 icon1=new ImageIcon(new URL(BEContext.host+"extcore/kores/pdm/images/bomeditor/part.gif"));
	 icon2=new ImageIcon(new URL(BEContext.host+"extcore/kores/pdm/images/bomeditor/partcheckoutcopy.gif"));
     icon3=new ImageIcon(new URL(BEContext.host+"extcore/kores/pdm/images/bomeditor/partcheckout.gif"));
	 }catch(Exception ex){
	 }
 
	}
     public WTPartData(WTPart part){
       this.part=part;
	   icon=icon1;
	   
	   String number = part.getNumber();
	   number = number.indexOf("_") > -1 ? number.substring(0, number.lastIndexOf("_")) : number;
	   
	   this.number= new IconData(icon,number);
		try{
           if(WorkInProgressHelper.isCheckedOut(part)){
			  if(WorkInProgressHelper.isWorkingCopy(part)){
			      icon=icon2;
			  }else{
			      icon=icon3;
			  }
		     
		 } 
//		this.version=VersionControlHelper.getQualifiedIdentifier(part).getSeries().getValue();
		this.version = part.getVersionIdentifier().getValue();
		}catch(Exception ex){}
		this.type=part.getPartType().toString();
		this.lastupdate=part.getModifyTimestamp().toString();
		this.creator = part.getCreator().getFullName();
		//name= part.getName();
		name = BEContext.getName(part);
	  }
};
class ColumnData
{
  public String  m_title;
  public int     m_width;
  public int     m_alignment;

  public ColumnData(String title, int width, int alignment) {
    m_title = title;
    m_width = width;
    m_alignment = alignment;
  }
}
class ColoredTableCellRenderer extends DefaultTableCellRenderer
{
  public void setValue(Object value) 
  {
    if (value instanceof ColorData) {
      ColorData cvalue = (ColorData)value;
	  if(cvalue.b_color!=null){
		  setBackground(cvalue.b_color);
	  }
      setForeground(cvalue.m_color);
      setText(cvalue.m_data.toString());
    }
    else if (value instanceof IconData) {
      IconData ivalue = (IconData)value;
      setIcon(ivalue.m_icon);
      setText(ivalue.m_data.toString());
    }
    else
      super.setValue(value);
  }
}