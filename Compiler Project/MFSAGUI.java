/*
 * Created on Apr 30, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Ahmed Ragab
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.*;
public class MFSAGUI extends JFrame{
	JTable mfsaTable ;
	Vector rowData = new Vector(0,1);
	Vector columnHeader = new Vector(0,1);
	Vector row;
	Vector mfsa;
	public MFSAGUI(Vector mfsa){
		super("Lexical Analyzer [Minimum Fininte State Acceptor]");
		this.mfsa = mfsa;
		// setup table's header
		columnHeader.add("Group");
		columnHeader.add("States");
		columnHeader.add("Token Recognized");
		
		
		// add table's data
		for(int i=0;i<this.mfsa.size();i++){
			Group group =(Group) this.mfsa.get(i);
			row = new Vector(0,1);
			row.add("Groups "+i+" ");
			
			String s="";
			for(int j=0;j<group.statesList.size();j++){
				Group g = (Group)group.statesList.get(j);
				s = " {" + g.statesList.get(0)+ "} ";
			}
			row.add(s);
			
			if(group.tokenRecognized==-1){
				row.add("none");
			}
			else{
				//System.out.println(""+group.tokenRecognized);
				int x = group.tokenRecognized;
				row.add(" "+x);
			}
			
			rowData.add(row);
		}
		
		this.mfsaTable = new JTable(rowData,columnHeader);
		JScrollPane scrollPane = new JScrollPane(this.mfsaTable);
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		container.add(mfsaTable.getTableHeader(),BorderLayout.PAGE_START);
		container.add(scrollPane,BorderLayout.CENTER);
		this.setDefaultCloseOperation(this.HIDE_ON_CLOSE);
		this.setSize(400,300);
		this.show();
		
		
	}
	
	
}
