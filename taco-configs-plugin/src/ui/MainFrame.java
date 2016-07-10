package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import tacoconfigsplugin.popup.actions.Config;
import tacoconfigsplugin.popup.actions.ParseConfigurations;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements WindowListener, ActionListener, ChangeListener {

	private final JFileChooser fc = new JFileChooser();
	private SelectFileDialog selectFileDialog = new SelectFileDialog(this, "Select File Dialog");
	//TODO: Get class name/path from eclipse plugin
	private String testFile = "C:/Users/Federico/git/itba/avmc/taco-configuration/taco-configs-plugin/src/tacoconfigsplugin/popup/actions/TestClass.java";

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MainFrame mainFrame = new MainFrame("Taco Dressing Chooser");
		mainFrame.setSize(800, 768);
		mainFrame.setVisible(true);
	}

	public MainFrame(String title) {
		super(title);
		setLayout(new FlowLayout());
		addWindowListener(this);
		JPanel labelsPanel = new JPanel();
		labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
		JButton button = new JButton("Save");
		labelsPanel.add(button);
		button.addActionListener(this);
		selectFileDialog.addConfirmActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO: Guardar archivo modificado
			}
		});
		selectFileDialog.setVisible(false);
		HashMap<String, List<Config>> configMap = new ParseConfigurations(testFile).configurations();
		for (String method : configMap.keySet()) {
			for (Config config : configMap.get(method)) {
				JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
				JLabel label = new JLabel(config.name());
				rowPanel.add(label);
				if (config.isBoolean()) {
					JCheckBox checkBox = new JCheckBox();
					checkBox.setSelected(config.booleanValue());
					label.setLabelFor(checkBox);
					rowPanel.add(checkBox);
				} else if (config.isInteger()) {
					JSlider slider = new JSlider();
					slider.addChangeListener(this);
					slider.setPaintLabels(true);
					slider.setPaintTicks(true);
					slider.setSize(new Dimension(200, 50));
					slider.setMajorTickSpacing(1);
					slider.setMaximum(5);
					slider.setMinimum(0);
					slider.setMinorTickSpacing(1);
					slider.setValue(config.intValue());
					rowPanel.add(slider);
				} else if (config.isDouble() || config.isString()) {
					JTextField tField = new JTextField(30);
					label.setLabelFor(tField);
					tField.setText(config.value());
					rowPanel.add(tField);
				}
				labelsPanel.add(rowPanel);
			}
		}
		JScrollPane scrollPane = new JScrollPane(labelsPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(50, 30, 500, 600);
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(500, 400));
        contentPane.add(scrollPane);
        setContentPane(contentPane);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
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
