/*
 * Created on May 1, 2005
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

import java.awt.BorderLayout;
import java.awt.Container;
import java.util.*;

import javax.swing.*;

public class DFSAGUI extends JFrame{
	JTable dfsaTable ;
	Vector rowData = new Vector(0,1);
	Vector columnHeader = new Vector(0,1);
	Vector row;
	Vector dfsa;
	ArrayList terminals;
	public DFSAGUI (Vector dfsa,ArrayList input){
		super("Lexical Analyzer [Deterministic Fininte State Acceptor]");
		this.dfsa = dfsa;
		this.terminals = input;
		// setup table's header
		columnHeader.add("State");
		for(int i=0;i<terminals.size();i++){
			columnHeader.add(" "+input.get(i)+" ");
		}
		columnHeader.add("Token Recognized");
		
		// add table's data
		for(int i=0;i<dfsa.size();i++){
			row = new Vector(0,1);
			row.add(" "+i+" ");
			Group g = (Group)dfsa.get(i);
			for(int j=0;j<g.inputChar.size();j++){
				if(((Integer)g.inputChar.get(j)).intValue()==-1){
					row.add(" - ");
				}
				else{
				row.add(" "+g.inputChar.get(j)+" ");
				}
			}
			if(g.tokenRecognized==-1){
				row.add("none");
			}
			else{
				//System.out.println(""+group.tokenRecognized);
				int x = g.tokenRecognized;
				row.add(" "+x);
			}
			
			rowData.add(row);
		}
		
		this.dfsaTable = new JTable(rowData,columnHeader);
		JScrollPane scrollPane = new JScrollPane(this.dfsaTable);
		Container container = this.getContentPane();
		container.setLayout(new BorderLayout());
		container.add(dfsaTable.getTableHeader(),BorderLayout.PAGE_START);
		container.add(scrollPane,BorderLayout.CENTER);
		this.setDefaultCloseOperation(this.HIDE_ON_CLOSE);
		this.setSize(400,300);
		this.show();
	}
	
}
