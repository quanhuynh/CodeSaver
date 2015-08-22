package gui;

import utils.Constants;
import utils.Listeners;
import obj.Category;
import obj.Snippet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;

import data.DBMS;

public class MainJFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JSplitPane splitPane;
	private JPanel snippetSource;
	private JPanel mainPanel;
	private JXTaskPaneContainer tpContainer;
	private Map<String, Category> categoryMap;
	private static DBMS dbms = DBMS.getInstance();
	private static MainJFrame mainFrame = new MainJFrame();
	private JTextField nameField;
	private JTextField categoryField;
	private JTextArea codeField;
	private JTextField syntaxField;
	private JTextArea commentField;
	
	public MainJFrame() {
		categoryMap = new HashMap<String, Category>();
	
		//SETTING UP MAIN COMPONENTS OF MAINJFRAME
		snippetSource = new JPanel(new BorderLayout());
		mainPanel = new JPanel();
		tpContainer = new JXTaskPaneContainer();
		
		//#########################
		//## SNIPPETSOURCE SETUP ##
		//#########################
		snippetSource.setBorder(new TitledBorder("Snippet Explorer"));
		//BUTTON CONTAINER AT BOTTOM OF SNIPPETSOURCE
		JPanel buttonContainer = new JPanel(new GridLayout(2, 1));
		JButton addSnippetButton = new JButton("New Snippet");
		JButton addCategoryButton = new JButton("New Category");
		addCategoryButton.addActionListener(Listeners.ADD_CATEGORY_ACTION);
		buttonContainer.add(addSnippetButton);
		buttonContainer.add(addCategoryButton);
		snippetSource.add(buttonContainer, BorderLayout.SOUTH);
		//ADDING DEFAULT CATEGORY AND SNIPPET
		snippetSource.add(new JScrollPane(tpContainer));
		addDefault();
		
		//#####################
		//## MAINPANEL SETUP ##
		//#####################
		mainPanel.setLayout(new GridBagLayout());
		mainPanel.setBorder(new TitledBorder("Snippet Manager"));
		GridBagConstraints c = new GridBagConstraints();
		
		//NAME
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.insets = new Insets(5, 10, 5, 10);
		mainPanel.add(new JLabel("Name:"), c);
		
		c.gridx++;
		nameField = new JTextField();
		nameField.setColumns(Constants.FORM_COLUMN_WIDTH);
		mainPanel.add(nameField, c);
		
		//CATEGORY
		gbcWrap(c);
		mainPanel.add(new JLabel("Category:"), c);
		
		c.gridx++;
		c.anchor = GridBagConstraints.CENTER;
		categoryField = new JTextField();
		categoryField.setColumns(Constants.FORM_COLUMN_WIDTH);
		mainPanel.add(categoryField, c);
		
		//CODE
		gbcWrap(c);
		c.anchor = GridBagConstraints.NORTHEAST;
		mainPanel.add(new JLabel("Code:"), c);
		
		c.gridx++;
		c.anchor = GridBagConstraints.CENTER;
		codeField = new JTextArea(14, Constants.FORM_COLUMN_WIDTH);
		codeField.setLineWrap(true);
		codeField.setWrapStyleWord(true);
		codeField.setTabSize(2);
		JScrollPane codeFieldScroll = new JScrollPane(codeField);
		mainPanel.add(codeFieldScroll, c);
		
		//SYNTAX
		gbcWrap(c);
		mainPanel.add(new JLabel("Syntax:"), c);
		
		c.gridx++;
		c.anchor = GridBagConstraints.CENTER;
		syntaxField = new JTextField();
		syntaxField.setColumns(Constants.FORM_COLUMN_WIDTH);
		mainPanel.add(syntaxField, c);
		
		//COMMENT
		gbcWrap(c);
		mainPanel.add(new JLabel("Comment:"), c);
		
		c.gridx++;
		c.anchor = GridBagConstraints.CENTER;
		commentField = new JTextArea(3, Constants.FORM_COLUMN_WIDTH);
		commentField.setLineWrap(true);
		commentField.setTabSize(2);
		commentField.setWrapStyleWord(true);
		JScrollPane commentFieldScroll = new JScrollPane(commentField);
		mainPanel.add(commentFieldScroll, c);
		
		//BUTTONS
		c.gridy++;
		JPanel buttons = new JPanel();
		JButton saveBtn = new JButton("Save");
		saveBtn.addActionListener(Listeners.SAVE_SNIPPET_ACTION);
		JButton clearBtn = new JButton("Clear");
		clearBtn.addActionListener(Listeners.CLEAR_FIELDS_ACTION);
		buttons.add(saveBtn);
		buttons.add(clearBtn);
		mainPanel.add(buttons, c);
		
		
		//######################
		//## PUTTING TOGETHER ##
		//######################
		//CREATING MAIN SPLITPANE
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, snippetSource, mainPanel);
		splitPane.setDividerLocation(200);
		splitPane.setEnabled(false);
		splitPane.setDividerSize(0);
		//FINALIZING MAINJFRAME
		setTitle(Constants.APP_NAME);
		setMinimumSize(new Dimension(700, 500));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		add(splitPane);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void gbcWrap(GridBagConstraints c) {
		c.gridx = 0;
		c.gridy++;
		c.anchor = GridBagConstraints.EAST;
	}
	
	public void addDefault() {
		addCategory("Default Category");
		Snippet sampleSnippet = new Snippet("Default Snippet", "Default Category", "//foo bar", "This is a default snippet", "Java");
		addSnippet("Default Category", sampleSnippet);
	}
	
	public void addCategory(String name) {
		JXTaskPane categoryPane = new JXTaskPane();
		Category newCategory = new Category(name);
		
		categoryPane.setTitle(name);
		newCategory.setTaskPane(categoryPane);

		categoryMap.put(name, newCategory);
		tpContainer.add(categoryPane);
	}

	public void addSnippet(String categoryName, Snippet s) {
		categoryMap.get(categoryName).addSnippet(s);
		JLabel snippetLabel = new JLabel(s.name());
		setVisible(false);
		categoryMap.get(categoryName).taskPane().add(snippetLabel);
		setVisible(true);
		
		dbms.addSnippet(s);
		
	}
	
	public JTextField getNameField() {
		return nameField;
	}
	
	public JTextField getCategoryField() {
		return categoryField;
	}
	
	public JTextField getSyntaxField() {
		return syntaxField;
	}
	
	public JTextArea getCodeField() {
		return codeField;
	}
	
	public JTextArea getCommentField() {
		return commentField;
	}
	
	public static MainJFrame getInstance() {
		return mainFrame;
	}
	
	public static void main(String[] args) {
		MainJFrame.getInstance();
	}
}
