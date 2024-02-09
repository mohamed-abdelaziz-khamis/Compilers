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
import java.util.*;

class Node {
    char symbol;
    int nextState;
}

class character{
    char terminal;
}

public class Main {
	    public static void prepareInput(Vector NDFSA, ArrayList terminals) {

	        character ch;

	        ch=new character();
	        ch.terminal='a';
	        terminals.add(ch);

	        ch=new character();
	        ch.terminal='b';
	        terminals.add(ch);

	        ArrayList arrayList;
	        Node node;

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 200;
	        node.nextState = 1;

	        arrayList.add(node);

	        node = new Node();
	        node.symbol = 200;
	        node.nextState = 4;

	        arrayList.add(node);

	        node = new Node();
	        node.symbol = 200;
	        node.nextState = 9;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 200;
	        node.nextState = 2;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 'a';
	        node.nextState = 3;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 203;
	        node.nextState = 0;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 200;
	        node.nextState = 5;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 'a';
	        node.nextState = 6;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 'b';
	        node.nextState = 7;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 'b';
	        node.nextState = 8;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 203;
	        node.nextState = 1;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 200;
	        node.nextState = 10;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 'a';
	        node.nextState = 11;

	        arrayList.add(node);

	        node = new Node();
	        node.symbol = 200;
	        node.nextState = 12;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 200;
	        node.nextState = 10;

	        arrayList.add(node);

	        node = new Node();
	        node.symbol = 200;
	        node.nextState = 12;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 'b';
	        node.nextState = 13;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 'b';
	        node.nextState = 14;

	        arrayList.add(node);

	        node = new Node();
	        node.symbol = 200;
	        node.nextState = 15;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 200;
	        node.nextState = 13;

	        arrayList.add(node);

	        node = new Node();
	        node.symbol = 200;
	        node.nextState = 15;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	        arrayList = new ArrayList();

	        node = new Node();
	        node.symbol = 203;
	        node.nextState = 3;

	        arrayList.add(node);

	        NDFSA.add(arrayList);

	  }
	  
	  public static void main(String[] args) {

        

	    //MFSA mfsa = new MFSA("ahmed Ragab");
	    //GUI gui = new GUI();
	    //documentGUI doc = new documentGUI();
	    //lexGUI lexgui = new lexGUI();
	    //InputGUI in = new InputGUI();
	  	OutputGUI out = new OutputGUI();
	  }

	}