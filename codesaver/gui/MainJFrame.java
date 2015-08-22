package gui;

import utils.Constants;
import utils.Listeners;
import obj.Category;
import obj.Snippet;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import org.jdesktop.swingx.JXLabel;
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
	private JXTaskPane selectedCategory;
	private JLabel selectedSnippet;
	
	public MainJFrame() {
		categoryMap = new HashMap<String, Category>();
	
		//SETTING UP MAIN COMPONENTS OF MAINJFRAME
		snippetSource = new JPanel(new BorderLayout());
		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		tpContainer = new JXTaskPaneContainer();
		
		//#########################
		//## SNIPPETSOURCE SETUP ##
		//#########################
		
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
		mainPanel.setBackground(Color.WHITE);
		
		
		//######################
		//## PUTTING TOGETHER ##
		//######################
		//CREATING MAIN SPLITPANE
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, snippetSource, mainPanel);
		splitPane.setDividerLocation(200);
		splitPane.setEnabled(false);
		splitPane.setDividerSize(2);
		//FINALIZING MAINJFRAME
		setTitle(Constants.APP_NAME);
		setMinimumSize(new Dimension(700, 500));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		add(splitPane);
		setLocationRelativeTo(null);
		setVisible(true);
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
		categoryMap.get(categoryName).taskPane().add(snippetLabel);
		
		dbms.addSnippet(s);
		
	}
	
	public static MainJFrame getInstance() {
		return mainFrame;
	}
	
	public static void main(String[] args) {
		MainJFrame.getInstance();
	}
}
