package gui;

import utils.Constants;
import utils.Listeners;
import obj.Category;
import obj.Snippet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
	//Split pane contains list of snippets on left and main panel on right
	private JSplitPane splitPane;
	
	//Source of snippets and category list
	private JPanel snippetSource;
	
	//Main panel on the right of the GUI
	private JPanel mainPanel;
	
	//Container of categories
	private JXTaskPaneContainer tpContainer;
	
	//Map from String to Category object
	private Map<String, Category> categoryMap;
	
	//DBMS instance for pushing to and pulling from SQL database
	private static DBMS dbms = DBMS.getInstance();
	
	//JFrame instance
	private static MainJFrame mainFrame = new MainJFrame();
	
	//Information fields from main panel
	private JTextField nameField;
	private JComboBox<String> categoryField;
	private JTextArea codeField;
	private JTextField syntaxField;
	private JTextArea commentField;
	
	//Selected components
	private JButton selectedSnippet;
	
	/**
	 * Constructor of MainJFrame, creates all necessary components and displays them
	 */
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
		addSnippetButton.addActionListener(Listeners.ADD_SNIPPET_ACTION);
		addCategoryButton.addActionListener(Listeners.ADD_CATEGORY_ACTION);
		buttonContainer.add(addSnippetButton);
		buttonContainer.add(addCategoryButton);
		snippetSource.add(buttonContainer, BorderLayout.SOUTH);
		//ADDING DEFAULT CATEGORY AND SNIPPET
		snippetSource.add(new JScrollPane(tpContainer));
		
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
		c.fill = GridBagConstraints.NONE;
		DefaultComboBoxModel<String> dlm = new DefaultComboBoxModel<String>();
		for (String categoryName : categoryMap.keySet()) {
			dlm.addElement(categoryName);
		}
		categoryField = new JComboBox<String>(dlm);
		categoryField.setPreferredSize(nameField.getPreferredSize());
		mainPanel.add(categoryField, c);
		
		//CODE
		gbcWrap(c);
		c.fill = GridBagConstraints.HORIZONTAL;
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
		loadCategories();
		loadSnippets();
		setVisible(true);
	}
	
	/*
	 * Go to the next line of the GridBagConstraints
	 */
	public void gbcWrap(GridBagConstraints c) {
		c.gridx = 0;
		c.gridy++;
		c.anchor = GridBagConstraints.EAST;
	}
	
	/**
	 * Add the default category and snippet
	 */
	public void addDefault() {
		addCategory("Default Category");
		Snippet sampleSnippet = new Snippet("Sample Snippet", "Default Category", "//foo bar", "This is a sample snippet", "Java");
		addSnippet("Default Category", sampleSnippet);
	}
	
	public void loadCategories() {
		Set<String> categories = dbms.getAllCategories();
		for (String cat : categories) {
			addCategory(cat);
		}
	}
	
	public void loadSnippets() {
		Set<Snippet> snippets = dbms.getAllSnippets();
		for (Snippet s : snippets) {
			categoryMap.get(s.category()).addSnippet(s);
			JButton snippetLabel = new JButton(s.name());
			snippetLabel.setBorder(null);
			snippetLabel.setBackground(Constants.TRANSPARENT_COLOR);
			snippetLabel.addActionListener(Listeners.LOAD_SNIPPET_ACTION);
			snippetLabel.setFocusPainted(false);
			categoryMap.get(s.category()).taskPane().add(snippetLabel);
			repaint();
		}
	}
	
	/**
	 * Add a category
	 * @param name Name of category
	 */
	public void addCategory(String name) {
		JXTaskPane categoryPane = new JXTaskPane();
		Category newCategory = new Category(name);
		
		categoryPane.setTitle(name);
		newCategory.setTaskPane(categoryPane);

		categoryMap.put(name, newCategory);
		tpContainer.add(categoryPane);
		categoryField.addItem(name);
	}

	/**
	 * Add a snippet
	 * @param categoryName Name of category
	 * @param s Snippet object to be added
	 */
	public void addSnippet(String categoryName, Snippet s) {
		if (!dbms.addSnippet(s)) {
			JOptionPane.showMessageDialog(MainJFrame.getInstance(), "Failed to add snippet");
		} else {
			categoryMap.get(categoryName).addSnippet(s);
			JButton snippetLabel = new JButton(s.name());
			snippetLabel.setBorder(null);
			snippetLabel.setBackground(Constants.TRANSPARENT_COLOR);
			snippetLabel.addActionListener(Listeners.LOAD_SNIPPET_ACTION);
			snippetLabel.setFocusPainted(false);
			categoryMap.get(categoryName).taskPane().add(snippetLabel);
			repaint();
		}
	}
	
	/**
	 * Getter for name field
	 * @return JTextField object of name
	 */
	public JTextField getNameField() {
		return nameField;
	}
	
	/**
	 * Getter for category field
	 * @return JComboBox object of category
	 */
	public JComboBox<String> getCategoryField() {
		return categoryField;
	}
	
	/**
	 * Getter for syntax field
	 * @return JTextField object of syntax
	 */
	public JTextField getSyntaxField() {
		return syntaxField;
	}
	
	/**
	 * Getter for code field
	 * @return JTextArea object of code
	 */
	public JTextArea getCodeField() {
		return codeField;
	}
	
	/**
	 * Getter for comment field
	 * @return JTextArea object of comment
	 */
	public JTextArea getCommentField() {
		return commentField;
	}
	
	/**
	 * Set selectedSnippet JButton to be specified JButton
	 * @param selected JButton to set
	 */
	public void setSelectedSnippet(JButton selected) {
		if (selectedSnippet != null) {
			selectedSnippet.setBackground(Constants.TRANSPARENT_COLOR);
			repaint();
		}
		selectedSnippet = selected;
		if (selected != null) {
			selected.setBackground(new Color(153, 204, 255));
		}
	}
	
	/*
	 * Return the instance of MainJFrame
	 */
	public static MainJFrame getInstance() {
		return mainFrame;
	}
	
	public static void main(String[] args) {
		MainJFrame.getInstance();
	}
}
