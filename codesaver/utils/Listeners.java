package utils;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import obj.Snippet;
import gui.MainJFrame;

public class Listeners {
	
	public static final ActionListener ADD_CATEGORY_ACTION = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JFrame addCategoryFrame = new JFrame();
			addCategoryFrame.setAlwaysOnTop(true);
			addCategoryFrame.setTitle("Add a category");
			addCategoryFrame.setMinimumSize(new Dimension(400, 100));
			addCategoryFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			addCategoryFrame.setLocationRelativeTo(null);
			JPanel container = new JPanel();
			
			JTextField categoryNameForm = new JTextField();
			categoryNameForm.setColumns(20);
			JLabel enterLabel = new JLabel("Enter category name");
			JButton add = new JButton("Add");
			JButton cancel = new JButton("Cancel");
			add.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!categoryNameForm.getText().equals("")) {
						MainJFrame.getInstance().setVisible(false);
						MainJFrame.getInstance().addCategory(categoryNameForm.getText());
						MainJFrame.getInstance().setVisible(true);
						addCategoryFrame.dispose();
					}
				}
			});
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					addCategoryFrame.dispose();
				}
			});
			
			container.add(enterLabel);
			container.add(categoryNameForm);
			container.add(add);
			container.add(cancel);
			addCategoryFrame.add(container);
			addCategoryFrame.setVisible(true);
		
		}
	
	};

	public static final ActionListener CLEAR_FIELDS_ACTION = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			MainJFrame.getInstance().getNameField().setText("");
			MainJFrame.getInstance().getCodeField().setText("");
			MainJFrame.getInstance().getCommentField().setText("");
			MainJFrame.getInstance().getSyntaxField().setText("");
		}
	};

	public static final ActionListener SAVE_SNIPPET_ACTION = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			String name = MainJFrame.getInstance().getNameField().getText();
			String category = MainJFrame.getInstance().getCategoryField().getText();
			String code = MainJFrame.getInstance().getCodeField().getText();
			String syntax = MainJFrame.getInstance().getSyntaxField().getText();
			String comment = MainJFrame.getInstance().getCommentField().getText();
			Snippet newSnippet = new Snippet(name, category, code, comment, syntax);
			MainJFrame.getInstance().addSnippet(category, newSnippet);
		}
	};
}
