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

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.text.*;
import javax.swing.event.*;
//import javax.swing.border.*;
import javax.swing.text.html.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
//import javax.swing.JButton;
//import javax.swing.JToggleButton;

public class lexGUI
    extends JFrame {
  public static final String APP_NAME = "Lex Compiler";
  protected JTextPane m_editor;
  protected StyleSheet m_context;
  protected HTMLDocument m_doc;
  protected HTMLEditorKit m_kit;
  protected JTextPane output;
  protected HTMLDocument m_doc_output;
  protected HTMLEditorKit m_kit_output;

  //protected SimpleFilter m_htmlFilter;
  protected JToolBar m_toolBar;
  protected JFileChooser m_chooser;
  protected File m_currentFile;
  protected boolean m_textChanged = false;
  UIManager.LookAndFeelInfo looks[];
  JSplitPane jSplitPane1 = new JSplitPane();
  protected int m_xStart = -1;
  protected int m_xFinish = -1;
  ArrayList outputs;

  public lexGUI() {
    super(APP_NAME);
    setSize(650, 400);

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

    m_editor = new JTextPane();
    m_kit = new HTMLEditorKit();
    m_editor.setEditorKit(m_kit);

    output = new JTextPane();
    m_kit_output = new HTMLEditorKit();
    output.setEditorKit(m_kit_output);

    JScrollPane ps2 = new JScrollPane(output);

    JScrollPane ps = new JScrollPane(m_editor);
    jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
    jSplitPane1.setPreferredSize(new Dimension(10, 3));
    jSplitPane1.setDividerSize(3);
    this.getContentPane().add(jSplitPane1, BorderLayout.CENTER);
    jSplitPane1.add(ps, JSplitPane.TOP);
    jSplitPane1.add(ps2, JSplitPane.BOTTOM);
    jSplitPane1.setDividerLocation(200);

    JMenuBar menuBar = createMenuBar();
    setJMenuBar(menuBar);
    m_chooser = new JFileChooser();
    newDocument();
    WindowListener wndCloser = new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        if (!promptToSave())
          return;
        System.exit(0);
      }

      public void windowActivated(WindowEvent e) {
        m_editor.requestFocus();
      }
    };
    addWindowListener(wndCloser);
    this.show();
  }

  protected String getDocumentName() {
    return m_currentFile == null ? "Untitled" :
        m_currentFile.getName();
  }

  protected void newDocument() {
    m_doc = (HTMLDocument) m_kit.createDefaultDocument();
    m_context = m_doc.getStyleSheet();
    m_editor.setDocument(m_doc);
    m_currentFile = null;
    setTitle(APP_NAME + " [" + getDocumentName() + "]");
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        m_editor.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
        m_doc.addDocumentListener(new UpdateListener());
        m_textChanged = false;
      }
    });
  }

  protected boolean promptToSave() {
    if (!m_textChanged)
      return true;
    int result = JOptionPane.showConfirmDialog(this,
                                               "Save changes to " +
                                               getDocumentName() + "?",
                                               APP_NAME,
                                               JOptionPane.YES_NO_CANCEL_OPTION,
                                               JOptionPane.INFORMATION_MESSAGE);
    switch (result) {
      case JOptionPane.YES_OPTION:
        if (!saveFile(false))
          return false;
        return true;
      case JOptionPane.NO_OPTION:
        return true;
      case JOptionPane.CANCEL_OPTION:
        return false;
    }
    return true;
  }

  protected boolean saveFile(boolean saveAs) {
    if (!saveAs && !m_textChanged)
      return true;
    if (saveAs || m_currentFile == null) {
      if (m_chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
        return false;
      File f = m_chooser.getSelectedFile();
      if (f == null)
        return false;
      m_currentFile = f;
      setTitle(APP_NAME + " [" + getDocumentName() + "]");
    }
    this.setCursor(
        Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    try {
      OutputStream out = new FileOutputStream(m_currentFile);
      m_kit.write(out, m_doc, 0, m_doc.getLength());
      out.close();
      m_textChanged = false;
    }
    catch (Exception ex) {
      showError(ex, "Error saving file " + m_currentFile);
    }
    this.setCursor(Cursor.getPredefinedCursor(
        Cursor.DEFAULT_CURSOR));
    return true;
  }

  public void showError(Exception ex, String message) {
    ex.printStackTrace();
    JOptionPane.showMessageDialog(this,
                                  message, APP_NAME,
                                  JOptionPane.WARNING_MESSAGE);
  }

  class UpdateListener
      implements DocumentListener {
    public void insertUpdate(DocumentEvent e) {
      m_textChanged = true;
    }

    public void removeUpdate(DocumentEvent e) {
      m_textChanged = true;
    }

    public void changedUpdate(DocumentEvent e) {
      m_textChanged = true;
    }
  }

  protected JMenuBar createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    JMenu mFile = new JMenu("File");
    mFile.setMnemonic('f');
    Action actionNew = new AbstractAction("New") {
      public void actionPerformed(ActionEvent e) {
        if (!promptToSave())
          return;
        newDocument();
      }
    };
    JMenuItem item = new JMenuItem(actionNew);
    item.setMnemonic('n');
    item.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_N, InputEvent.CTRL_MASK));
    mFile.add(item);
    Action actionOpen = new AbstractAction("Open") {
      public void actionPerformed(ActionEvent e) {
        if (!promptToSave())
          return;
        openDocument();
      }
    };
    item = new JMenuItem(actionOpen);
    item.setMnemonic('o');
    item.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_O, InputEvent.CTRL_MASK));
    mFile.add(item);
    Action actionSave = new AbstractAction("Save") {
      public void actionPerformed(ActionEvent e) {
        saveFile(false);
      }
    };
    item = new JMenuItem(actionSave);
    item.setMnemonic('s');
    item.setAccelerator(KeyStroke.getKeyStroke(
        KeyEvent.VK_S, InputEvent.CTRL_MASK));
    mFile.add(item);
    Action actionSaveAs =
        new AbstractAction("Save As") {
      public void actionPerformed(ActionEvent e) {
        saveFile(true);
      }
    };
    item = new JMenuItem(actionSaveAs);
    item.setMnemonic('a');
    mFile.add(item);
    mFile.addSeparator();
    Action actionExit = new AbstractAction("Exit") {
      public void actionPerformed(ActionEvent e) {
        System.exit(0);
      }
    };
    item = mFile.add(actionExit);
    item.setMnemonic('x');
    menuBar.add(mFile);
    return menuBar;

  }

  protected void openDocument() {
    if (m_chooser.showOpenDialog(this) !=
        JFileChooser.APPROVE_OPTION)
      return;
    File f = m_chooser.getSelectedFile();
    if (f == null || !f.isFile())
      return;
    m_currentFile = f;
    setTitle(APP_NAME + " [" + getDocumentName() + "]");
    this.setCursor(
        Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
    try {
      InputStream in = new FileInputStream(m_currentFile);
      m_doc = (HTMLDocument) m_kit.createDefaultDocument();
      m_kit.read(in, m_doc, 0);
      m_context = m_doc.getStyleSheet();
      m_editor.setDocument(m_doc);
      in.close();
      String s = m_editor.getText();
      s=s.substring(44,s.length()-17);
      System.out.println(s);
      MFSA mfsa = new MFSA(s);
      outputs = mfsa.output;

      output.setText(s);
      for (int i = 0; i < outputs.size(); i++) {
        int fontSize = ( (Out) outputs.get(i)).token + 40;

        MutableAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setFontSize(attr, fontSize);
        m_doc.setCharacterAttributes(((Out) outputs.get(i)).start , ((Out) outputs.get(i)).end - ((Out) outputs.get(i)).start,
                                     attr, false);
      }

    }
    catch (Exception ex) {
      showError(ex, "Error reading file " + m_currentFile);
    }
    this.setCursor(Cursor.getPredefinedCursor(
        Cursor.DEFAULT_CURSOR));
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        m_editor.setCaretPosition(1);
        m_editor.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
        m_doc.addDocumentListener(new UpdateListener());
        m_textChanged = false;
      }
    });
  }

}