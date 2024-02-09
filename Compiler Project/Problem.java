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

public class Problem {

  // problem vector
  public Vector problemVector = new Vector(2, 1);
  public ArrayList inputCharacter = new ArrayList();

  // sequence of input characters is 0,1

  // constructor
  public Problem() {

  /*  inputCharacter.add("a");
    inputCharacter.add("b");
    inputCharacter.add("c");
    inputCharacter.add("d");
    inputCharacter.add("e");
    inputCharacter.add("f");
    inputCharacter.add("g");
    inputCharacter.add("h");
    inputCharacter.add("i");
    inputCharacter.add("j");
    inputCharacter.add("k");
    inputCharacter.add("l");
    inputCharacter.add("m");
    inputCharacter.add("n");
    inputCharacter.add("o");
    inputCharacter.add("p");
    inputCharacter.add("q");
    inputCharacter.add("r");
    inputCharacter.add("s");
    inputCharacter.add("t");
    inputCharacter.add("u");
    inputCharacter.add("v");
    inputCharacter.add("w");
    inputCharacter.add("x");
    inputCharacter.add("y");
    inputCharacter.add("z");
    inputCharacter.add("0");
    inputCharacter.add("1");
    inputCharacter.add("2");
    inputCharacter.add("3");
    inputCharacter.add("4");
    inputCharacter.add("5");
    inputCharacter.add("6");
    inputCharacter.add("7");
    inputCharacter.add("8");
    inputCharacter.add("9");
    inputCharacter.add(">");
    inputCharacter.add("<");
    inputCharacter.add("=");

    Group g1 = new Group();
    g1.statesList.add(new Integer(0));
    g1.tokenRecognized = -1;
    g1.inputChar.add(new Integer(1));
    g1.inputChar.add(new Integer(2));
*/

    inputCharacter.add("a");
    inputCharacter.add("b");

    Group g1 = new Group();
    g1.statesList.add(new Integer(0));
    g1.tokenRecognized = -1;
    g1.inputChar.add(new Integer(1));
    g1.inputChar.add(new Integer(2));

    Group g6 = new Group();
    g6.statesList.add(new Integer(1));
    g6.tokenRecognized = 0;
    g6.inputChar.add(new Integer(3));
    g6.inputChar.add(new Integer(4));


    Group g7 = new Group();
    g7.statesList.add(new Integer(2));
    g7.tokenRecognized = 2;
    g7.inputChar.add(new Integer(-1));
    g7.inputChar.add(new Integer(2));


    Group g8 = new Group();
    g8.statesList.add(new Integer(3));
    g8.tokenRecognized = -1;
    g8.inputChar.add(new Integer(3));
    g8.inputChar.add(new Integer(2));


    Group g2 = new Group();
    g2.statesList.add(new Integer(4));
    g2.tokenRecognized = 2;
    g2.inputChar.add(new Integer(-1));
    g2.inputChar.add(new Integer(5));


    Group g3 = new Group();
    g3.statesList.add(new Integer(5));
    g3.tokenRecognized = 1;
    g3.inputChar.add(new Integer(-1));
    g3.inputChar.add(new Integer(2));


    /*Group g4 = new Group();
    g4.statesList.add(new Integer(-1));
    g4.tokenRecognized = -1;
    g4.inputChar.add(new Integer(-1));
    g4.inputChar.add(new Integer(-1));*/

    problemVector.add(g1);
    problemVector.add(g6);
    problemVector.add(g7);
    //problemVector.add(g4);
    //problemVector.add(g5);
    problemVector.add(g8);
    problemVector.add(g2);
    problemVector.add(g3);


    /*
     inputCharacter.add("0");
     inputCharacter.add("1");
     Group g1 = new Group();
    g1.statesList.add("ABE");
    g1.tokenRecognized = "output";
    g1.inputChar.add("ABE");
    g1.inputChar.add("ACE");

    Group g6 = new Group();
    g6.statesList.add("ACE");
    g6.tokenRecognized = "output";
    g6.inputChar.add("ABE");
    g6.inputChar.add("AE");

    Group g7 = new Group();
    g7.statesList.add("ADE");
    g7.tokenRecognized = "output";
    g7.inputChar.add("ABE");
    g7.inputChar.add("AE");

    Group g8 = new Group();
    g8.statesList.add("AE");
    g8.tokenRecognized = "output";
    g8.inputChar.add("ABE");
    g8.inputChar.add("AE");


    Group g2 = new Group();
    g2.statesList.add("A");
    g2.tokenRecognized = "input";
    g2.inputChar.add("AB");
    g2.inputChar.add("A");

    Group g3 = new Group();
    g3.statesList.add("AB");
    g3.tokenRecognized = "input";
    g3.inputChar.add("AB");
    g3.inputChar.add("AC");

    Group g4 = new Group();
    g4.statesList.add("AC");
    g4.tokenRecognized = "input";
    g4.inputChar.add("AB");
    g4.inputChar.add("AD");

    Group g5 = new Group(); ;
    g5.statesList.add("AD");
    g5.tokenRecognized = "input";
    g5.inputChar.add("ABE");
    g5.inputChar.add("A");

    problemVector.add(g1);
    problemVector.add(g2);
    problemVector.add(g3);
    problemVector.add(g4);
    problemVector.add(g5);
    problemVector.add(g6);
    problemVector.add(g7);
    problemVector.add(g8);*/
  }
}