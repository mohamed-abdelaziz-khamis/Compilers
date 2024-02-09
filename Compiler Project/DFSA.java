
import java.util.*;

//////////////////////////////////////////////////////////////////////////////

//The ArrayList of terminal symbols:

/*
 ArrayList of characters
 */

//////////////////////////////////////////////////////////////////////////////

//The Vector of the NDFSA:
//It is the output of phase1 = input of phase2

/*
 Vector of ArrayLists:
  Each ArrayList of objects of type Node:
    Each Node has:
       symbol: character
       nextState: integer

 Note:
 if "symbol" = 203, then
    1) There is no "symbol", because the current state is the final state.
    2) The "nextState" is the tokenRecognized,
     = The index of the regular expression in the regualr expressions array.

 else
    1) The "symbol" is the terminal symbol.
       Equals 200 in case of Lambda.
    2) The "nextState" is the id of the next state.
 */



//////////////////////////////////////////////////////////////////////////////

//The Vector of the equivalent DFSA transition table:
//It is the output of phase2 = input of phase3

/*
 Vector of Groups:
 Each Group has:
    transitionsUnderTerminals: ArrayList
    tokenRecognized: integer

 Note:
 1) The "transitionsUnderTerminals" is the transitions under all terminals:
    Each transition represents the corresponding index in the DFSA
    transition table.

 2) The "tokenRecognized" is the recognized token:
    It is the index of the regular expression in the regualr expressions
    array.
    Equals -1 if no token is recognized.
 */

class Group {
	  ArrayList statesList = new ArrayList();
	  ArrayList inputChar = new ArrayList();
	  int tokenRecognized;
	  int parent = -1;
	}

//////////////////////////////////////////////////////////////////////////////
class State2 {
    ArrayList LambdaClosure = new ArrayList();
    ArrayList transitionsUnderTerminals = new ArrayList();
}


//////////////////////////////////////////////////////////////////////////////

public class DFSA {

    Vector DFSA = new Vector();

    private void Union(ArrayList x, ArrayList y) {
        int i, j;
        for (i = 0; i < y.size(); i++) {
            for (j = 0; j < x.size(); j++){
                if (((Integer)y.get(i)).intValue() == ((Integer)x.get(j)).intValue()){
                    break;
                }
            }
            if (j == x.size()){
                x.add(y.get(i));
            }
        }
    }

    private ArrayList LambdaClosureState(int state, Vector NDFSA) {
        ArrayList x = new ArrayList();
        x.add(new Integer(state));
        for (int i = 0; i < ((ArrayList) NDFSA.get(state)).size(); i++) {

            if (((Node) (((ArrayList) NDFSA.get(state)).get(i))).symbol == 200) {
                Union(x,
                      LambdaClosureState(((Node) (((ArrayList) NDFSA.get(state)).
                                                  get(i))).nextState, NDFSA));
            }
        }
        return x;
    }

    private void fillStates(ArrayList states, Vector NDFSA, ArrayList terminals) {
        State2 state;
        int i, j, k;
        for (i = 0; i < NDFSA.size(); i++) {
            state = new State2();
            state.LambdaClosure = LambdaClosureState(i, NDFSA);
            for (j = 0; j < terminals.size(); j++) {
                for (k = 0; k < ((ArrayList) NDFSA.get(i)).size(); k++) {
                    if (((Node) ((ArrayList) NDFSA.get(i)).get(k)).symbol ==
                        ((character) terminals.get(j)).terminal) {
                        ArrayList z = new ArrayList();
                        z.add(new Integer(((Node) ((ArrayList) NDFSA.get(i)).get(k)).nextState));
                        state.transitionsUnderTerminals.add(z);
                    }
                    else{
                        ArrayList z = new ArrayList();
                        state.transitionsUnderTerminals.add(z);
                    }
                }
            }
            states.add(state);
        }
    }

    private ArrayList move(ArrayList LambdaClosure, int i, ArrayList states) {
        ArrayList x = new ArrayList();
        int Index ;
        for (int k = 0; k < LambdaClosure.size(); k++) {
            Index = ((Integer)LambdaClosure.get(k)).intValue();

            //System.out.println("int val "+Index);

            State2 s =(State2) states.get(Index);

            ArrayList al= s.transitionsUnderTerminals;

            if(!al.isEmpty()){
                //System.out.println(""+ al.get(i));
                ArrayList a = (ArrayList) al.get(i);
                if(!a.isEmpty()){
                    Union(x, a);
                }
            }

        }
        //System.out.println("---------------------");

        return x;
    }

    private ArrayList LambdaClosureList(ArrayList arrList, ArrayList states) {
        ArrayList x = new ArrayList();
        for (int k = 0; k < arrList.size(); k++)
            Union(x,
                  ((State2) states.get(((Integer) (arrList.get(k))).intValue())).
                  LambdaClosure);
        return x;
    }

    private void sortTransition(ArrayList transition) { //ArrayList of Integer
        //Bubble sort algorithm
        int i, j, flag;
        int n = transition.size();
        Integer temp = new Integer(0);
        for (i = 0; i < n - 1; i++) {
            flag = 0;
            for (j = n - 1; j >= i + 1; j--) {
                if (((Integer) (transition.get(j))).intValue() <
                    ((Integer) (transition.get(j - 1))).intValue()) {
                    temp = (Integer) transition.get(j);
                    transition.add(j, transition.get(j - 1));
                    transition.add(j - 1, temp);
                    flag = 1; // A swap has happened
                }
            }
            if (flag == 0)break; //There is no swap has happened in previous pass,
        } // so the List is sorted
    }

    private int findToken(ArrayList transitions, Vector NDFSA) {
        //return index on array of expressions

        for (int i = 0; i < transitions.size(); i++)
            if (((Node) ((ArrayList) (NDFSA.get(((Integer) (transitions.get(i))).
                                                intValue()))).get(0)).symbol ==
                203)
                return ((Node) ((ArrayList) (NDFSA.get(((Integer) (transitions.
                        get(i))).intValue()))).get(0)).nextState;
        return -1;
    }

    public DFSA(Vector NDFSA, ArrayList terminals) {
        ArrayList states = new ArrayList();
        fillStates(states, NDFSA, terminals);
        Group group = new Group();
        group.tokenRecognized = -1;
        DFSA.add(group);
        ArrayList transitionsArray = new ArrayList(); //ArrayList of transition
        ArrayList LambdaClosure = new ArrayList(); //ArrayList of Integer
        ArrayList transition = new ArrayList(); //ArrayList of Integer
        int k = 0, i, index;

        transitionsArray.add(((State2) states.get(k)).LambdaClosure);
        while (k != DFSA.size()) {
            LambdaClosure = (ArrayList) transitionsArray.get(k);
            for (i = 0; i < terminals.size(); i++) {
                transition = LambdaClosureList(move(LambdaClosure, i, states),
                                               states);
                sortTransition(transition);
                index = transitionsArray.indexOf(transition);
                if (index != -1)
                    ((Group) (DFSA.get(k))).inputChar.add(new
                            Integer(index));
                else {
                    transitionsArray.add(transition);
                    ((Group) (DFSA.get(k))).inputChar.add(new
                            Integer(DFSA.size()));
                    group = new Group();
                    group.tokenRecognized = findToken(transition, NDFSA);
                    DFSA.add(group);
                }
            }
            k++;
        }

    }
}
//////////////////////////////////////////////////////////////////////////////
