import java.util.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */

// 200: lambda
// 201: closure
// 202: or
// 203: token recognized

public class Phase1 {
  Vector E ;// start with the assumption that I have E
  Vector Acceptor ;
  String ErrorMessage ;
  public Phase1(Vector x) {
    E = x ;
  }
  //---------------------------------------------------------
  void readSpace(String s, int startIndex)
  {
    char ch ;
    int i = startIndex ;
    int size = s.length() ;
    do
    {
      ch = s.charAt(i);
      i++ ;
    }while ((( ch == '\t')||( ch == ' '))&&(i<size));

  }
  //-----------------------------------------------------------
  void copy( Vector vFrom, Vector vTo )
  {
    int vSize = vFrom.size() ;
    for( int i=0 ; i<vSize ; i++ )
    {
      ArrayList lFrom = (ArrayList)vFrom.get(i);
      int lSize = lFrom.size();
      ArrayList lTo = new ArrayList(lSize);
      for( int j=0 ; j<lSize ; j++ )
      {
        State sFrom = (State)lFrom.get(j);
        State sTo = new State();
        sTo.ch = sFrom.ch ;
        sTo.nextState = sFrom.nextState ;
        lTo.add(sTo);
      }
      vTo.add(lTo);
    }
  }
  //-----------------------------------------------------------
  void replace( Vector v , int oldState, int newState )
  {
    int vSize = v.size() ;
    for( int i=0 ; i<vSize ; i++ )
    {
      ArrayList l = (ArrayList)v.get(i);
      int lSize = l.size() ;
      for( int j=0 ; j<lSize ; j++ )
      {
        State st = (State)l.get(j);
        if( st.nextState == oldState )
          st.nextState = newState ;
      }

    }
  }
  //-----------------------------------------------------------
  // adds inc to all states except final state( -1 )
  void add( Vector v , int inc )
  {
    int vSize = v.size() ;
    for( int i=0 ; i<vSize ; i++ )
    {
      ArrayList l = (ArrayList)v.get(i);
      int lSize = l.size() ;
      for( int j=0 ; j<lSize ; j++ )
      {
        State st = (State)l.get(j);
        if(( st.nextState != -1 )&&( st.ch != 203 ))
          st.nextState += inc ;
      }
    }
  }
  //------------------------------------------------------------
  // add vector v1 to v
  void addVector( Vector v , Vector v1 )
  {
    int v1Size = v1.size() ;
    for( int i=0 ; i<v1Size ; i++ )
    {
      ArrayList l = (ArrayList)v1.get(i);
      v.add(l);
    }

  }
  //-----------------------------------------------------------
  void getInitStates( Expression e )
  {
    int eSize = e.exprChar.size();
    for( int i=0 ; i<eSize ; i++ )
    {
      ExprChar ech = ((ExprChar)e.exprChar.get(i));
      if( ech.previousExpr >= 0 )
      {
        // set the linked list( ArrayList ) of this char to that of the previous expression
        Vector v ;
        Expression e1 = (Expression)E.get(((ExprChar)e.exprChar.get(i)).previousExpr) ;// get previous expression
        v = ((ExprChar)e1.exprChar.get(0)).v ;// get ArrayList of previous expression
        copy( v , ((ExprChar)e.exprChar.get(i)).v ) ; // set ArrayList of the current char in current expression = ArrayList of previous expression
      }
      else if(( ((ExprChar)e.exprChar.get(i)).ch != 201 )&&( ((ExprChar)e.exprChar.get(i)).ch != 202 ))
      {
        ArrayList l = new ArrayList();
        State st = new State();
        st.ch = ((ExprChar)e.exprChar.get(i)).ch;
        st.nextState = -1; // indicating that next state is final state ;
        l.add(st);
        ((ExprChar)e.exprChar.get(i)).v.add(l);
      }
    }
  }
  //------------------------------------------------------------
  void recognizeToken( Expression e )
  {
    int eSize = e.exprChar.size();
    if( eSize <= 0 )
      return ;
    ArrayList l = new ArrayList();
    State st = new State();
    st.ch = 203 ;
    st.nextState = E.indexOf(e);
    l.add(st);
    replace(((ExprChar)e.exprChar.get(0)).v, -1 , ((ExprChar)e.exprChar.get(0)).v.size() );
    ((ExprChar)e.exprChar.get(0)).v.add(l);
  }
  //------------------------------------------------------------
  boolean OrPass( Expression e )
  {
    int eSize = e.exprChar.size();
    Vector v = new Vector(1,1) ;
    ArrayList l = new ArrayList();
    v.add(l);
    int inc = 1 ;
    for( int i=0 ; i<eSize ; i++ )
    {
      if( ((ExprChar)e.exprChar.get(i)).v == null )
        continue ;
      if( ((ExprChar)e.exprChar.get(i)).ch == 202 )
      {
        if(( i == 0 )||( i == eSize-1 ))
        {
          ErrorMessage = "or can't be first or last symbol" ;
          return false ;
        }
        ((ExprChar)e.exprChar.get(i)).v = null ;
        continue ;
      }
      State st = new State();
      st.ch = 200 ;
      st.nextState = inc ;
      l.add(st);
      add( ((ExprChar)e.exprChar.get(i)).v , inc ) ;
      inc += ((ExprChar)e.exprChar.get(i)).v.size() ;
      addVector( v , ((ExprChar)e.exprChar.get(i)).v ) ;
      ((ExprChar)e.exprChar.get(i)).v = null ;
    }
    ((ExprChar)e.exprChar.get(0)).v = v ;

    return true ;
  }
  //------------------------------------------------------------
  // CONCATINATION OF VECTOR A TO V IS DONE BY:
  // inc = 0
  // add inc to all states except -1
  // inc += size of vector A
  // replace -1 with inc
  // add vector A to V
  boolean CatPass( Expression e )
  {
    int eSize = e.exprChar.size();
    Vector v = new Vector(1,1) ;
    int inc = 0 ;
    for( int i=0 ; i<eSize ; i++ )
    {
      if(((ExprChar)e.exprChar.get(i)).ch == 202)// or
      {
        if(( i == 0 )||( i == eSize-1 ))
        {
          ErrorMessage = "or can't be first or last symbol" ;
          return false ;
        }
        replace( v , inc , -1 );
        ((ExprChar)e.exprChar.get(i-1)).v = v ;
        v = new Vector(1,1) ;
        inc = 0 ;
      }
      else if(((ExprChar)e.exprChar.get(i)).v != null)
      {
        add( ((ExprChar)e.exprChar.get(i)).v , inc ) ;
        inc += ((ExprChar)e.exprChar.get(i)).v.size() ;
        replace( ((ExprChar)e.exprChar.get(i)).v , -1 , inc ) ;
        addVector( v, ((ExprChar)e.exprChar.get(i)).v ) ;
        ((ExprChar)e.exprChar.get(i)).v = null ;
      }
    }
    replace( v, inc , -1 ) ;
    ((ExprChar)e.exprChar.get(eSize-1)).v = v ;
    return true;
  }
  //------------------------------------------------------------
  // closure symbol is char 201
  // lambda synbol is char 200
  boolean ClosurePass( Expression e )
  {
    /*
     add a new state that goes to -1(new final state) with lambda
        and to start state with lambda
     replace all -1( old final state ) with the new state #
     start state goes to new added state with lambda
    */
   int eSize = e.exprChar.size();
   for( int i=0 ; i<eSize ; i++ )
   {
     // get the element containing closure "ch == 201"
     if( ((ExprChar)e.exprChar.get(i)).previousExpr >= 0 )
       continue ;
     if( ((ExprChar)e.exprChar.get(i)).ch != 201 )
       continue ;
     if( i == 0 )
     {
         ErrorMessage = "first symbol can't be closure" ;
         return false ;
     }
     for( int j=i-1 ; j>=0 ; j-- )
     {
       // if close previous elements contain closure then let this vector point to previous vector
       // and set previous vector to null
       if( ((ExprChar)e.exprChar.get(j)).ch == 202 )
       {
         ErrorMessage = "OR-CLOSURE is not allowed" ;
         return false ;
       }
       if( ((ExprChar)e.exprChar.get(j)).ch == 201 )
       {
         ((ExprChar)e.exprChar.get(i)).v = ((ExprChar)e.exprChar.get(j)).v;
         ((ExprChar)e.exprChar.get(j)).v = null ;
       }
       // here we have found the element to closure
       else
       {
         int size = ((ExprChar)e.exprChar.get(j)).v.size() ;
         if( size <= 0 )
           break ;
         // make a copy of the vector to closure and set vector to closure to null
         copy( ((ExprChar)e.exprChar.get(j)).v , ((ExprChar)e.exprChar.get(i)).v );
         ((ExprChar)e.exprChar.get(j)).v = null ;
         // replace old final state with new state
         replace( ((ExprChar)e.exprChar.get(i)).v , -1 , size ) ;
         // let start state go to final state under lambda
         ArrayList l = (ArrayList)((ExprChar)e.exprChar.get(i)).v.get(0);
         State st = new State() ;
         st.ch = 200 ;
         st.nextState = -1 ;
         l.add(st);
         // add the new state that goes to start state under lambda
         // and to final state under lambda
         l = new ArrayList();
         st = new State() ;
         st.ch = 200 ;
         st.nextState = 0 ;
         l.add(st);
         st = new State() ;
         st.ch = 200 ;
         st.nextState = -1 ;
         l.add(st);
         ((ExprChar)e.exprChar.get(i)).v.add(l) ;
         break ;
       }
     }
     //ArrayList l = e.chArr[i].a;

   }
   return true ;
  }
  //------------------------------------------------------------
  void OrAll()
  {
    Acceptor = new Vector(5,5);
    int ESize = E.size() ;
    ArrayList l = new ArrayList();
    Acceptor.add(l);
    int inc = 1 ;
    for( int i=0 ; i<ESize ; i++ )
    {
      Expression e = (Expression)E.get(i);
      if(( ((ExprChar)e.exprChar.get(0)).v == null )||(!e.tokenRecognized))
        continue ;

      State st = new State();
      st.ch = 200 ;
      st.nextState = inc ;
      l.add(st);

      add( ((ExprChar)e.exprChar.get(0)).v , inc ) ;
      inc += ((ExprChar)e.exprChar.get(0)).v.size() ;
      addVector( Acceptor , ((ExprChar)e.exprChar.get(0)).v ) ;
      ((ExprChar)e.exprChar.get(0)).v = null ;
    }
  }
  //-----------------------------------------------------------
  void testAcceptor()
  {
    if( Acceptor == null )
      System.err.println("null");
    for( int j=0 ; j<Acceptor.size() ; j++ )
    {
      ArrayList l = (ArrayList)Acceptor.get(j);
      System.err.println("list "+j);
      for(int k=0 ; k<l.size() ; k++ )
      {
        State st = (State)l.get(k) ;
          System.err.println("char: " + (int)(st.ch) + " next state: " + st.nextState);
      }
    }

  }
  //------------------------------------------------------------
  void test( Expression e )
  {
    int eSize = e.exprChar.size();
    for( int i=0 ; i<eSize ; i++ )
    {
      System.err.println(((ExprChar)e.exprChar.get(i)).ch);
      if(((ExprChar)e.exprChar.get(i)).v == null )
      {
        System.err.println("null");
        continue ;
      }
      for( int j=0 ; j<((ExprChar)e.exprChar.get(i)).v.size() ; j++ )
      {
        ArrayList l = (ArrayList)((ExprChar)e.exprChar.get(i)).v.get(j);
        System.err.println("list "+j);
        for(int k=0 ; k<l.size() ; k++ )
        {
          State st = (State)l.get(k) ;
          //if(( st.ch == 201 )|| ( st.ch == 200))
            System.err.println("char: " + (int)(st.ch) + " next state: " + st.nextState);
/*          else
          System.out.println("char: " + st.ch + " next state: " + st.nextState);
*/
        }
      }
    }
  }
  //------------------------------------------------------------
  void start()
  {/*
    E = new Vector(3,1) ;
    Expression e = new Expression();
    e.name = "letter" ;
    e.exprChar = new ArrayList(51);
    ExprChar ech = new ExprChar() ;
    ech.ch = 'a' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'b' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'c' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'd' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'e' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'f' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'g' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'h' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'i' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'j' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'k' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'l' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'm' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'n' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'o' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'p' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'q' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'r' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 's' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 't' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'u' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'v' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'w' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'x' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'y' ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 'z' ;
    e.exprChar.add(ech);
    E.add(e);

    e = new Expression();
    e.exprChar = new ArrayList(2);
//    e.chArr = new ExprChar[2];
    ech = new ExprChar() ;
    ech.previousExpr = 0 ;
    e.exprChar.add(ech);

    ech = new ExprChar() ;
    ech.ch = 201 ;
    e.exprChar.add(ech);
    e.tokenRecognized = true;
    E.add(e);

    e = new Expression();
    e.exprChar = new ArrayList(6);
//    e.chArr = new ExprChar[6];
    ech = new ExprChar() ;
    ech.ch = 'a' ;
    e.exprChar.add(ech);
    ech = new ExprChar() ;
    ech.ch = 'b' ;
    e.exprChar.add(ech);
    ech = new ExprChar() ;
    ech.ch = 201 ;
    e.exprChar.add(ech);
    ech = new ExprChar() ;
    ech.ch = 202 ;
    e.exprChar.add(ech);
    ech = new ExprChar() ;
    ech.ch = 'c' ;
    e.exprChar.add(ech);
    ech = new ExprChar() ;
    ech.ch = 201 ;
    e.exprChar.add(ech);
    e.tokenRecognized = true;
    E.add(e);
*/
    int ESize = E.size() ;
    for( int i=0 ; i<ESize ; i++ )
    {
      System.err.println("\nExpression "+i);
      getInitStates((Expression)E.get(i));
      test((Expression)E.get(i));
      ClosurePass((Expression)E.get(i));
      System.err.println("\nClosure\n");
      test((Expression)E.get(i));
      CatPass((Expression)E.get(i));
      System.err.println("\nCat\n");
      test((Expression)E.get(i));
      OrPass((Expression)E.get(i));
      System.err.println("\nOr\n");
      test((Expression)E.get(i));
      System.err.println("\nToken Recognized\n");
      if( ((Expression)E.get(i)).tokenRecognized )
        recognizeToken( (Expression)E.get(i) ) ;
      test((Expression)E.get(i));
    }
    System.err.println("\nAcceptor\n");
    OrAll() ;
    testAcceptor();

    ArrayList terminals = new ArrayList();

    //Fill here the array of terminals

    
    /*character terminal=new character();
    terminal.terminal = 'a';

    terminals.add(terminal);

    terminal=new character();
    terminal.terminal = 'b';

    terminals.add(terminal);*/
    
    

  }
  //------------------------------------------------------------
  public static void main(String[] args) {

    InputGUI i = new InputGUI();
    /*
    Phase1 phase1 = new Phase1();
    phase1.start();
*/
   /*
   char ch ;
   for( int i=0 ; i< 255 ; i++ )
   {
     ch = (char)i ;
     System.out.println(""+ i + " : " + ch);
   }*/
  }
}
class Expression{
  String name ;
  boolean tokenRecognized ;
  ArrayList exprChar = new ArrayList();
//  ExprChar[] chArr = null ;
}
class ExprChar{
   char ch ;
   int previousExpr = -1 ; // if >= 0 then it is an index to array of expressions E
   Vector v = null ;
//   ArrayList a = null;
   ExprChar()
   {
      v = new Vector(1,1);
   }
}
class State{
  char ch ;
  int nextState ;
}

class character{
     char terminal;
}
