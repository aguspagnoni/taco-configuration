package ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SelectFileDialog extends JDialog {
	
	private JLabel fileNameLabel = new JLabel();
	private JButton confirmButton = new JButton("Confirm");
	private JButton cancelButton = new JButton("Cancel");
	
	public SelectFileDialog(JFrame parent, String title) {
		super(parent, title);
		this.setSize(800, 200);
		
		JPanel outerPanel = new JPanel();
		
		JPanel labelPanel = new JPanel(new FlowLayout());
		labelPanel.add(fileNameLabel);
		JPanel buttonsPanel = new JPanel(new FlowLayout());
		buttonsPanel.add(confirmButton);
		buttonsPanel.add(cancelButton);

		
		BoxLayout boxLayout = new BoxLayout(outerPanel, BoxLayout.Y_AXIS);
		outerPanel.setLayout(boxLayout);
		outerPanel.add(labelPanel);
		outerPanel.add(buttonsPanel);
		
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SelectFileDialog.this.setVisible(false);
			}
		});
		
		add(outerPanel);
	}
	
	public void addConfirmActionListener(ActionListener listener) {
		confirmButton.addActionListener(listener);
	}
	
	public void setFileName(String fileName) {
		fileNameLabel.setText("Confirma ejecuci—n sobre el archivo " + fileName + " ?");
	}
	
}
