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
import javax.swing.*;
import javax.swing.event.DocumentEvent;



public class OutputGUI extends JFrame {
	public static final String APP_NAME = "Lexical Analyzer";
	protected JTextPane m_editor;
	protected EditorKit m_kit;
	protected DefaultStyledDocument doc;
	
	protected JTextPane m_editor_out;
	protected EditorKit m_kit_out;
	protected DefaultStyledDocument doc_out;
	
	protected JToolBar m_toolBar;
	protected JFileChooser m_chooser;
	protected File m_currentFile;
	protected boolean m_textChanged = false;
	
	JSplitPane jSplitPane1 = new JSplitPane();
	ArrayList outputs;
	MutableAttributeSet attr;
	int I;
	
	public OutputGUI() throws HeadlessException {
		super(APP_NAME);
		setSize(650, 400);
		//doc = new DefaultStyledDocument();
		m_editor = new JTextPane();
		m_kit = m_editor.getEditorKit();
		JScrollPane ps = new JScrollPane(m_editor);
		getContentPane().add(ps, BorderLayout.CENTER);
		
		//doc_out = new DefaultStyledDocument();
		m_editor_out = new JTextPane();
		m_kit_out = m_editor_out.getEditorKit();
		JScrollPane ps2 = new JScrollPane(m_editor_out);
		getContentPane().add(ps2, BorderLayout.CENTER);
		
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
		doc_out =(DefaultStyledDocument) m_kit_out.createDefaultDocument();	    
		m_editor_out.setDocument(doc_out);
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
		doc =(DefaultStyledDocument) m_kit.createDefaultDocument();	    
		m_editor.setDocument(doc);
		m_currentFile = null;
		setTitle(APP_NAME + " [" + getDocumentName() + "]");
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				m_editor.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
				doc.addDocumentListener(new UpdateListener());
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
			m_kit.write(out, doc, 0, doc.getLength());
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
			try {
				String s = e.getDocument().getText(0,e.getDocument().getLength());
				
				//doc =(DefaultStyledDocument) e.getDocument();
				System.out.println(s);
				MFSA mfsa = new MFSA(s);
				attr = new SimpleAttributeSet();
				outputs = mfsa.output;
				
				SwingUtilities.invokeLater(new write(attr,doc,outputs,doc_out)) ;
	
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		public void removeUpdate(DocumentEvent e) {
			m_textChanged = true;
			try {
				String s = e.getDocument().getText(0,e.getDocument().getLength());
				
				//doc =(DefaultStyledDocument) e.getDocument();
				System.out.println(s);
				MFSA mfsa = new MFSA(s);
				attr = new SimpleAttributeSet();
				outputs = mfsa.output;
				SwingUtilities.invokeLater(new write(attr,doc,outputs,doc_out)) ;
	
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
		public void changedUpdate(DocumentEvent e) {
			m_textChanged = true;
			/*try {
				String s = e.getDocument().getText(0,e.getDocument().getLength());
				
				//doc =(DefaultStyledDocument) e.getDocument();
				System.out.println(s);
				MFSA mfsa = new MFSA(s);
				attr = new SimpleAttributeSet();
				outputs = mfsa.output;
				
				SwingUtilities.invokeLater(new write(attr,doc,outputs)) ;
	
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
		}
	}
	
	protected JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu option = new JMenu("Option");
		option.setMnemonic('o');
		
		Action actionDFSA = new AbstractAction("Show DFSA"){
			public void actionPerformed(ActionEvent e){
				
				//JMenuItem item = new JMenuItem(actionDFSA);
				
			}
		};
		JMenuItem item1 = new JMenuItem(actionDFSA);
		item1.setMnemonic('d');
		item1.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_D, InputEvent.CTRL_MASK));
		option.add(item1);
		
		Action actionNDFSA = new AbstractAction("Show NDFSA"){
			public void actionPerformed(ActionEvent e){
				
				//JMenuItem item = new JMenuItem(actionDFSA);
			}
		};
		item1 = new JMenuItem(actionNDFSA);
		item1.setMnemonic('F');
		item1.setAccelerator(KeyStroke.getKeyStroke(
				KeyEvent.VK_F, InputEvent.CTRL_MASK));
		option.add(item1);
		
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
		menuBar.add(option);
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
			doc = (DefaultStyledDocument) m_kit.createDefaultDocument();
			m_kit.read(in, doc, 0);
			m_editor.setDocument(doc);
			in.close();
			String s = m_editor.getText();
			//s=s.substring(44,s.length()-17);
			System.out.println(s);
			MFSA mfsa = new MFSA(s);
			ArrayList outputs = mfsa.output;
			
			for (int i = 0; i < outputs.size(); i++) {
				MutableAttributeSet attr = new SimpleAttributeSet();
				if(i %2 ==0){
					//StyleConstants.setFontSize(attr, fontSize);
					StyleConstants.setForeground(attr,Color.RED);
					StyleConstants.setBold(attr,true);
					StyleConstants.setItalic(attr,true);
					
				}
				else{
					StyleConstants.setForeground(attr,Color.BLUE);
					StyleConstants.setBold(attr,true);
					StyleConstants.setItalic(attr,true);
					
				}
				
				doc.setCharacterAttributes(((Out) outputs.get(i)).start , ((Out) outputs.get(i)).end - ((Out) outputs.get(i)).start,
						attr, false);
				StyleConstants.setForeground(attr,Color.BLACK);
				StyleConstants.setBold(attr,false);
				StyleConstants.setItalic(attr,false);
			}
		}
		catch (Exception ex) {
			showError(ex, "Error reading file " + m_currentFile);
		}
		this.setCursor(Cursor.getPredefinedCursor(
				Cursor.DEFAULT_CURSOR));
		SwingUtilities.invokeLater (new Runnable() {
			public void run() {
				m_editor.setCaretPosition(1);
				m_editor.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
				doc.addDocumentListener(new UpdateListener());
				m_textChanged = false;
			}
		});
		
		
	}
}
class write implements Runnable{
	int i;
	MutableAttributeSet attr;
	DefaultStyledDocument doc;
	DefaultStyledDocument doc_out;
	ArrayList outputs;
	MutableAttributeSet attr2;
	
	write(MutableAttributeSet attr,DefaultStyledDocument doc,ArrayList outputs,DefaultStyledDocument doc_out){
		this.attr = attr;
		this.doc = doc;
		this.doc_out = doc_out;
		this.outputs = outputs;
	}
	public void run() {
		try {
			doc_out.remove(0,doc_out.getLength());
		} catch (BadLocationException e2) {
			e2.printStackTrace();
		}
		StyleConstants.setForeground(attr,Color.BLACK);
		StyleConstants.setBold(attr,false);
		StyleConstants.setItalic(attr,false);
		for (int i = 0; i < outputs.size(); i++) {
			StyleConstants.setForeground(attr,Color.BLACK);
			StyleConstants.setBold(attr,false);
			StyleConstants.setItalic(attr,false);
			if(i %2 ==0){
				
				StyleConstants.setForeground(attr,Color.RED);
				StyleConstants.setBold(attr,true);
				StyleConstants.setItalic(attr,true);
				
			}
			else{
				StyleConstants.setForeground(attr,Color.BLUE);
				StyleConstants.setBold(attr,true);
				StyleConstants.setItalic(attr,true);
				
			}
		doc.setCharacterAttributes(((Out) outputs.get(i)).start , ((Out) outputs.get(i)).end - ((Out) outputs.get(i)).start,
				attr, true);
		String s="";
		try {
			s = "< "+doc.getText(((Out) outputs.get(i)).start , ((Out) outputs.get(i)).end - ((Out) outputs.get(i)).start)+" , "+((Out) outputs.get(i)).token+" >";
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		try {
			doc_out.insertString(0,"\n",attr);
			doc_out.insertString(0,s,attr);
			
		} catch (BadLocationException e1) {
			e1.printStackTrace();	
		}

		StyleConstants.setForeground(attr,Color.BLACK);
		StyleConstants.setBold(attr,false);
		StyleConstants.setItalic(attr,false);
	}
		//outputs.removeAll(outputs);

	}
	
}
