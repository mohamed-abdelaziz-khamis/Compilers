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

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.KeyEvent;
import javax.swing.text.*;

class _Expression {
  String name;
  boolean tokenRecognized;
  ArrayList exprChar = new ArrayList();
  //ExprChar[] chArr = null;
}

class ExprChar {
  char ch;
  int previousExpr = -1; // if >= 0 then it is an index to array of expressions E
  Vector v = null;
  ExprChar() {
    v = new Vector();
  }
}

class State {
  char ch;
  int nextState;
}

public class InputGUI
    extends JFrame {
  JTextField definition = new JTextField();
  JScrollPane jScrollPane1 = new JScrollPane();
  JScrollPane jScrollPane2 = new JScrollPane();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JSplitPane jSplitPane1 = new JSplitPane();
  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel();
  JComboBox jComboBox1 = new JComboBox();
  JTextField symbol = new JTextField();
  TitledBorder titledBorder1;
  ButtonGroup token = new ButtonGroup();
  JRadioButton isToken = new JRadioButton();
  JRadioButton isNonToken = new JRadioButton();
  JButton next = new JButton();
  JButton finish = new JButton();
  UIManager.LookAndFeelInfo looks[];
  DefaultComboBoxModel defaultComboBoxModel;

  ArrayList expressions = new ArrayList();

  ArrayList exprChar = new ArrayList();

  JButton addToDef = new JButton();
  JButton current = new JButton();

  public InputGUI() {
    try {
      jbInit();
      looks = UIManager.getInstalledLookAndFeels();
      try {
        UIManager.setLookAndFeel(looks[2].getClassName());
      }
      catch (UnsupportedLookAndFeelException ex) {
      }
      catch (IllegalAccessException ex) {
      }
      catch (InstantiationException ex) {
      }
      catch (ClassNotFoundException ex) {
      }
      SwingUtilities.updateComponentTreeUI(this);

      defaultComboBoxModel = (DefaultComboBoxModel) jComboBox1.getModel();
      definition.addKeyListener(new KeyListener() {
        public void keyTyped(KeyEvent keyEvent) {}

        public void keyPressed(KeyEvent keyEvent) {
          ExprChar _exprChar = new ExprChar();

          if (keyEvent.getKeyCode() == 8) {
            ExprChar temp = new ExprChar();
            if (exprChar.size() == 0)
              return;
            temp = (ExprChar) exprChar.remove(exprChar.size() - 1);
            if (temp.previousExpr != -1) { // clear previous expression
              if (temp.previousExpr == expressions.size() + 1) {
                try {
                  definition.setText(definition.getText(0,
                      (definition.getText().length() - symbol.getText().length() -
                       1)));
                }
                catch (BadLocationException ex) {
                  ex.printStackTrace();
                }
              }
              else {
                String s = ( (_Expression) expressions.get(temp.previousExpr)).
                    name;
                try {
                  definition.setText(definition.getText(0,
                      (definition.getText().length() - s.length() - 1)));
                }
                catch (BadLocationException ex) {
                  ex.printStackTrace();
                }
              }
            }
            else if (temp.ch == 157 || temp.ch == 156) { // clear *
              try {
                definition.setText(definition.getText(0,
                    definition.getText().length() - 2));
              }
              catch (BadLocationException ex1) {
                ex1.printStackTrace();
              }
            }
          }
          else if (keyEvent.isControlDown()) {
            if (keyEvent.getKeyChar() == 42) { // add *
              definition.setText(definition.getText() + " * ");
              _exprChar.ch = 157;
              exprChar.add(_exprChar);
            }
            else if (keyEvent.getKeyCode() == 92) {
              definition.setText(definition.getText() + " | ");
              _exprChar.ch = 156;
              exprChar.add(_exprChar);
            }
          }
          else if (keyEvent.getKeyCode() >= 33) { // add any character
            if (keyEvent.getKeyCode() <= 126) {
              _exprChar.ch = keyEvent.getKeyChar();
              exprChar.add(_exprChar);
            }
          }
        }

        public void keyReleased(KeyEvent keyEvent) {}
      });
      setSize(480, 400);
      show();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    titledBorder1 = new TitledBorder("");
    jSplitPane1.setBounds(new Rectangle(11, 37, 456, 234));
    this.getContentPane().setLayout(null);
    definition.setBorder(null);
    definition.setDebugGraphicsOptions(0);
    definition.setSelectionStart(0);
    definition.setText("");
    jScrollPane1.setBorder(null);
    jScrollPane1.setBounds(new Rectangle(170, 46, 116, 41));
    jScrollPane2.setBorder(null);
    jScrollPane2.setBounds(new Rectangle(38, 47, 111, 40));
    jLabel1.setText("Symbol");
    jLabel1.setBounds(new Rectangle(10, 24, 71, 24));
    jLabel2.setRequestFocusEnabled(true);
    jLabel2.setText("Definition");
    jLabel2.setBounds(new Rectangle(153, 22, 66, 29));
    jPanel1.setLayout(null);
    jPanel2.setDebugGraphicsOptions(0);
    jPanel2.setLayout(null);
    jComboBox1.setBounds(new Rectangle(6, 48, 137, 25));
    symbol.setBorder(null);
    symbol.setSelectionStart(0);
    symbol.setText("");
    isToken.setSelected(true);
    isToken.setText("Token");
    isToken.setBounds(new Rectangle(114, 109, 91, 23));
    isNonToken.setSelected(false);
    isNonToken.setText("Non-Token");
    isNonToken.setBounds(new Rectangle(114, 137, 91, 23));
    next.setBounds(new Rectangle(126, 322, 73, 25));
    next.setText("Next");
    next.addActionListener(new InputGUI_next_actionAdapter(this));
    finish.setBounds(new Rectangle(254, 322, 73, 25));
    finish.setToolTipText("");
    finish.setSelected(false);
    finish.setText("Finsih");
    finish.addActionListener(new InputGUI_finish_actionAdapter(this));
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setResizable(false);
    addToDef.setBounds(new Rectangle(19, 116, 120, 25));
    addToDef.setText("Add to Definition");
    addToDef.addActionListener(new InputGUI_addToDef_actionAdapter(this));
    current.setBounds(new Rectangle(91, 178, 123, 25));
    current.setText("Add to Definition");
    current.addActionListener(new InputGUI_current_actionAdapter(this));
    jPanel1.add(jLabel2, null);
    jPanel1.add(jLabel1, null);
    jPanel1.add(jScrollPane1, null);
    jPanel1.add(jScrollPane2, null);
    jPanel1.add(isToken, null);
    jPanel1.add(isNonToken, null);
    jPanel1.add(current, null);
    jScrollPane2.getViewport().add(symbol, null);
    jScrollPane1.getViewport().add(definition, null);
    jPanel2.add(jComboBox1, null);
    jPanel2.add(addToDef, null);
    jSplitPane1.add(jPanel1, JSplitPane.LEFT);
    jSplitPane1.add(jPanel2, JSplitPane.RIGHT);
    this.getContentPane().add(next, null);
    this.getContentPane().add(finish, null);
    this.getContentPane().add(jSplitPane1, null);
    jSplitPane1.setDividerLocation(300);
    token.add(isNonToken);
    token.add(isToken);
  }

  void finish_actionPerformed(ActionEvent e) {
    if (!symbol.getText().equals("") && !definition.getText().equals("")) {
      if (defaultComboBoxModel.getIndexOf("" + symbol.getText()) == -1) {
        defaultComboBoxModel.addElement(symbol.getText());
        _Expression expression = new _Expression();
        expression.name = symbol.getText(); // set name
        expression.tokenRecognized = isToken.isSelected(); // set token or not
        for (int i = 0; i < exprChar.size(); i++) {
          expression.exprChar.add(exprChar.get(i));
        }
        expressions.add(expression);
        symbol.setText("");
        definition.setText("");
        this.exprChar.clear();
      }
    }
    // send expressions array to Shahira's part
  }

  void next_actionPerformed(ActionEvent e) {
    if (!symbol.getText().equals("") && !definition.getText().equals("")) {
      if (defaultComboBoxModel.getIndexOf("" + symbol.getText()) == -1) {
        defaultComboBoxModel.addElement(symbol.getText());
        _Expression expression = new _Expression();
        expression.name = symbol.getText(); // set name
        expression.tokenRecognized = isToken.isSelected(); // set token or not
        for (int i = 0; i < exprChar.size(); i++) {
          expression.exprChar.add(exprChar.get(i));
        }
        //expression.exprChar = exprChar; // set exprChar
        expressions.add(expression);
        symbol.setText("");
        definition.setText("");
        this.exprChar.clear();
       // System.out.println("size " + expression.exprChar.size());
      }
    }
  }

  void addToDef_actionPerformed(ActionEvent e) {

    String selectedDefinition;
    if (defaultComboBoxModel.getSelectedItem() != null) {
      ExprChar _exprChar = new ExprChar();
      selectedDefinition = " " + defaultComboBoxModel.getSelectedItem() + " ";
      definition.setText(definition.getText() + selectedDefinition);
      _exprChar.previousExpr = defaultComboBoxModel.getIndexOf(
          defaultComboBoxModel.getSelectedItem());
      exprChar.add(_exprChar);
    }
  }

  void current_actionPerformed(ActionEvent e) {
    if (!symbol.getText().equals("")) {
      ExprChar _exprChar = new ExprChar();
      definition.setText(definition.getText() + " " + symbol.getText() + " ");
      _exprChar.previousExpr = defaultComboBoxModel.getSize() + 1;
      exprChar.add(_exprChar);
    }
  }

}

class InputGUI_finish_actionAdapter
    implements java.awt.event.ActionListener {
  InputGUI adaptee;

  InputGUI_finish_actionAdapter(InputGUI adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.finish_actionPerformed(e);
  }
}

class InputGUI_next_actionAdapter
    implements java.awt.event.ActionListener {
  InputGUI adaptee;

  InputGUI_next_actionAdapter(InputGUI adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.next_actionPerformed(e);
  }
}

class InputGUI_addToDef_actionAdapter
    implements java.awt.event.ActionListener {
  InputGUI adaptee;

  InputGUI_addToDef_actionAdapter(InputGUI adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.addToDef_actionPerformed(e);
  }
}

class InputGUI_current_actionAdapter
    implements java.awt.event.ActionListener {
  InputGUI adaptee;

  InputGUI_current_actionAdapter(InputGUI adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.current_actionPerformed(e);
  }
}