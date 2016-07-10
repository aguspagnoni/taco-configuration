package ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tacoconfigsplugin.popup.actions.ParseConfigurations;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements WindowListener, ActionListener, ChangeListener {

	private final JFileChooser fc = new JFileChooser();
	private JButton button;
	private SelectFileDialog selectFileDialog = new SelectFileDialog(this, "Select File Dialog");
	private String testFile = "C:/Users/Federico/git/itba/avmc/taco-configuration/taco-configs-plugin/src/tacoconfigsplugin/popup/actions/TestClass.java";
	
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MainFrame mainFrame = new MainFrame("Taco Dressing Chooser");
		mainFrame.setSize(800, 600);
		mainFrame.setVisible(true);
	}
	
	public MainFrame(String title) {
		super(title);
		setLayout(new FlowLayout());
		addWindowListener(this);
		button = new JButton("Click me");
		add(button);
		button.addActionListener(this);
		selectFileDialog.addConfirmActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: action to be executed on file select confirmation event
			}
		});
		selectFileDialog.setVisible(false);
		HashMap<String, HashMap<String, String>> configMap = new ParseConfigurations(testFile).configurations();
		for (String method : configMap.keySet()) {
			HashMap<String, String> configurations = configMap.get(method);
			for (String configuration : configurations.keySet()) {
				JLabel label = new JLabel(configuration);
				add(label);
			}
		}
		JTextField exampleTF = new JTextField(60);
		exampleTF.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: action to be executed on file select confirmation event
				System.out.println(exampleTF.getText());
			}
		});
		add(exampleTF);
		JSlider slider = new JSlider();
		slider.addChangeListener(this);
		slider.setPaintLabels(true);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		add(slider);
		JCheckBox checkBox = new JCheckBox();
		add(checkBox);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int returnVal = fc.showOpenDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			// XXX: open the file
			selectFileDialog.setFileName(file.getName());
			selectFileDialog.setVisible(true);
		} else {
			// XXX: selection cancelled by user
		}
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// XXX: close app on main window closing
		dispose();
		System.exit(0);
	}

	@Override
	public void windowOpened(WindowEvent e) { 
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void stateChanged(ChangeEvent event) {
		JSlider source = (JSlider) event.getSource();
        if (!source.getValueIsAdjusting()) {
        	System.out.println(source.getValue());
        }
	};
	
}
