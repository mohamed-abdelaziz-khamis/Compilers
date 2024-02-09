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

/*class Group {
  ArrayList statesList = new ArrayList();
  ArrayList inputChar = new ArrayList();
  int tokenRecognized;
  int parent = -1;
}*/

class Out {
  int start;
  int end;
  int token;
}

public class MFSA {

  // groups vector is to store the minimul finite state acceptor
  // initial size is 0, increment is 1
  Vector groups = new Vector(0, 1);
  // Recognized Tokens
  //ArrayList recognizedToken = new ArrayList();
  ArrayList inputCharacters;
  Vector visitedGroups = new Vector(1, 1);
  public ArrayList output = new ArrayList();
  //Problem p = new Problem();
  Vector NDFSA = new Vector();
  ArrayList terminals = new ArrayList();
  
  //
  int counter = 0;
  // constructor
  String text;
  public MFSA(String text) {
  	
  	Main.prepareInput(NDFSA,terminals);
    DFSA obj = new DFSA(NDFSA, terminals);
    
    this.text=text;
    // note: that section will be removed after finish the program.
    //this.recognizedToken.add("a");
    //this.recognizedToken.add("abb");
    //this.recognizedToken.add("a*b+");
    //------------------------------------------------------
    
    //DFSAGUI dfsaGUI = new DFSAGUI(p.problemVector,p.inputCharacter);
    this.inputCharacters = terminals;
    for(int i=0;i<obj.DFSA.size();i++){
    	((Group)obj.DFSA.get(i)).statesList.add(new Integer(i));
    }
    DFSAGUI dfsagui = new DFSAGUI(obj.DFSA,terminals);
    
    
    this.devide(obj.DFSA);
    //this.printVector(groups);
    this.setGroupsDistinations(groups);
    this.minimizeFSA(groups);
    
    MFSAGUI mfsagui = new MFSAGUI(groups);
    
    //MFSAGUI mfsaGUI = new MFSAGUI(groups);
    //this.printVector(groups);
    //System.out.println("vector length = " + groups.size());

    this.move(this.text,
              (Group) ( (Group) groups.get(0)).statesList.get(0));

  }

//---------------------------------------------------------------------------
  //devide the FSA to groups of tokens
  private void devide(Vector v) {
    int lenght = v.size();
    int i = 0;
    while (counter < lenght) {
      if (!visitedGroups.contains("" + i)) {
        Group group = new Group();
        group.tokenRecognized = ( (Group) v.get(i)).tokenRecognized;
        getSameGroup(v, group.tokenRecognized, i, group);
        groups.add(group);
      }
      i++;
    }
  }

//--------------------------------------------------------------------------
  private void getSameGroup(Vector v, int token, int start, Group group) {

    for (int i = start; i < v.size(); i++) {
      if ( (int) ( (Group) v.get(i)).tokenRecognized == token) {
        counter++;
        Group g = (Group) v.get(i);
        visitedGroups.add("" + i);
        group.statesList.add(g);
      }
    }

//---------------------------------------------------------------------------
  }

  private void printVector(Vector v) {
    for (int i = 0; i < v.size(); i++) {
      System.out.println("State " + i);
      if ( ( (Group) v.get(i)).tokenRecognized == -1) {
        System.out.println("Token Regognized none");
      }
      else {
        /*System.out.println("Token Regognized " +
                           this.recognizedToken.get( ( (Group) v.get(i)).
            tokenRecognized));*/
      }
      for (int j = 0; j < ( (Group) v.get(i)).statesList.size(); j++) {
        System.out.println( ( (Group) ( (Group) v.get(i)).statesList.get(j)).
                           statesList.toString());
      }
      for (int j = 0; j < ( (Group) v.get(i)).inputChar.size(); j++) {
        System.out.println("under " + this.inputCharacters.get(j) + " go to " +
                           ( (Group) v.get(i)).inputChar.get(j));
      }
      System.out.println("---------------------------------------");
    }

  }

//---------------------------------------------------------------------------
  public void minimizeFSA(Vector v) {
    boolean flag;
    do {
      flag = false;
      for (int i = 0; i < v.size(); i++) {
        Group group = (Group) v.get(i);
        for (int j = 0; j < group.statesList.size(); j++) {
          Group element = (Group) group.statesList.get(j);
          ArrayList temp = this.getDistination(element, v);
          for (int k = 0; k < temp.size(); k++) {
            if (! ( (String) temp.get(k)).equals( ( (String) group.inputChar.
                get(k)))) {
              group.statesList.remove(j);
              flag = true;
              element.parent = i;
              if (!this.searchForSameGroup(element, v)) {
                Group newGroup = new Group();
                newGroup.parent = i;
                newGroup.tokenRecognized = element.tokenRecognized;
                newGroup.statesList.add(element);
                v.add(newGroup);
              }
              this.setGroupsDistinations(v);
              break;
            }
          }
        }
      }
    }
    while (flag == true);
  }

//----------------------------------------------------------------------------
  public void setGroupsDistinations(Vector v) {
    for (int i = 0; i < v.size(); i++) {
      Group group = (Group) v.get(i);
      Group firstElement = ( (Group) group.statesList.get(0));
      group.inputChar = this.getDistination(firstElement, v);
    }
  }

//----------------------------------------------------------------------------
  public ArrayList getDistination(Group element, Vector v) {
    ArrayList temp = new ArrayList();
    for (int i = 0; i < element.inputChar.size(); i++) {
      String s = "" + element.inputChar.get(i);
      for (int j = 0; j < v.size(); j++) {
        if (search(s, j, v)) {
          temp.add("" + j);
          break;
        }
      }
    }
    return temp;
  }

//----------------------------------------------------------------------------
  public boolean search(String Element, int group, Vector v) {
    Group list = (Group) v.get(group);
    ArrayList statesList = list.statesList;
    for (int i = 0; i < statesList.size(); i++) {
      Group element = (Group) statesList.get(i);
      if (Element.equals( ( (Group) statesList.get(i)).statesList.get(0) + "")) {
        return true;
      }
    }
    return false;
  }

//---------------------------------------------------------------------------
  public Group searchForState(String Element, Vector v) {
    for (int j = 0; j < v.size(); j++) {
      Group list = (Group) v.get(j);
      ArrayList statesList = list.statesList;
      for (int i = 0; i < statesList.size(); i++) {
        Group element = (Group) statesList.get(i);
        if (Element.equals( ( (Group) statesList.get(i)).statesList.get(0) + "")) {
          return (Group) statesList.get(i);
        }
      }
    }
    return null;
  }

//----------------------------------------------------------------------------
  public boolean searchForSameGroup(Group element, Vector v) {
    for (int i = 0; i < v.size(); i++) {
      Group group = (Group) v.get(i);
      if (group.parent == element.parent) {
        ArrayList temp = getDistination(element, v);
        for (int j = 0; j < temp.size(); j++) {
          if (! ( (String) temp.get(j)).equals("" + group.inputChar.get(j))) {
            return false;
          }
        }
        group.statesList.add(element);
        return true;
      }
    }
    return false;
  }

//--------------------------------------------------------------------------
  public void move(String stream, Group startState) {
    System.out.println("" + stream.length());
    Group currentState = startState;
    Group lastAcceptedState = null;
    String buffer = "";
    int location = 0;
    int start = 0, end = 0;
    int errorIndex = -1;
    for (int i = 0; i < stream.length(); i++) {
      // the input character
      char character = stream.charAt(i);
      // the index of the input character
      
      int index =-1;
      //int index = this.inputCharacters.indexOf(character);
      for(int z=0;z<this.inputCharacters.size();z++){
      	if(character==((character)this.inputCharacters.get(z)).terminal){
      		index = z;
      		break;
      	}
      }
      
      //System.err.println("Index" + index);
      
      if (index == -1) {

        if (currentState.tokenRecognized != -1) {
          Out out = new Out();
          out.start = start;
          out.end = end;
          out.token = currentState.tokenRecognized;
          output.add(out);
          /*System.out.println("< " + stream.substring(start, end) + " , " +
                             this.recognizedToken.
                             get(currentState.tokenRecognized) + " >");*/

        }
        else {

          if (lastAcceptedState != null && end > start) {
            i = location;
            end = i + 1;
            Out out = new Out();
            out.start = start;
            out.end = end;
            out.token = lastAcceptedState.tokenRecognized;
            output.add(out);
            /*System.out.println("< " + stream.substring(start, end) + " , " +
                               this.recognizedToken.
                               get(lastAcceptedState.tokenRecognized) + " >");*/
          }
        }
        currentState = startState;
        start = i + 1;
      }
      else {

        Integer nextState = (Integer) (currentState.inputChar.get(index));
        end++;
        if (nextState.intValue() == -1) { // next state is the error state
          i--;
          if (currentState.tokenRecognized != -1) {
            end--;
            Out out = new Out();
            out.start = start;
            out.end = end;
            out.token = currentState.tokenRecognized;
            output.add(out);

            /*System.out.println("< " + stream.substring(start, end) + " , " +
                               this.recognizedToken.
                               get(currentState.tokenRecognized) + " >");*/
          }
          currentState = startState;
          start = i + 1;
        }
        else {

          currentState = this.searchForState("" + nextState, this.groups);

          if (currentState.tokenRecognized != -1) {
            lastAcceptedState = currentState;
            location = i;
            end = location + 1;
          }

          if (i == stream.length() - 1) {
            i = location;
            end = i + 1;
            Out out = new Out();
            out.start = start;
            out.end = end;
            out.token = lastAcceptedState.tokenRecognized;
            output.add(out);

            /*System.out.println("< " + stream.substring(start, end) + " , " +
                               this.recognizedToken.
                               get(lastAcceptedState.tokenRecognized) + " >");*/
            currentState = startState;
            start = i + 1;
          }

        }
      }
    }
  }

//----------------------------------------------------------------------------
  /*public void move(String stream, Group startState) {
    Group state = startState;
    Group previousState = startState;
    Group lastAcceptedState;
    int location;
    for (int i = 0; i < stream.length(); i++) {
      char character = stream.charAt(i);
      int index = this.inputCharacters.indexOf("" + character);
      // check for invalid chracter
      if (index == -1) {
        System.out.println("Error unexpecting character " + character);
        System.out.println("the input character will be ignored.");
      }
      // character is valid
      else {
        System.out.println("character: " + character);
        if (state.tokenRecognized != -1) {
          System.out.println("                        " +
                             this.recognizedToken.get(state.tokenRecognized));
        }
        Integer nextState = (Integer) (state.inputChar.get(index));
        System.out.println("nextState " + nextState);
        if (nextState.intValue() == -1) {
          if (state.tokenRecognized != -1) {
            System.out.println("" +
       this.recognizedToken.get(state.tokenRecognized));
          }
          i--;
          state = startState;
        }
        else if (i == stream.length() - 1) {
          if (state.tokenRecognized != -1) {
            System.out.println("" +
       this.recognizedToken.get(state.tokenRecognized));
          }
          return;
        }
        // go to valid state
        else {
          if (state.tokenRecognized != -1) {
            System.out.println("last accept state  token recognized   " +
                               state.tokenRecognized);
            lastAcceptedState = state;
            location = i;
          }
          state = this.searchForState("" + nextState, this.groups);
        }
      }
    }
     }*/
} // end MFSA class