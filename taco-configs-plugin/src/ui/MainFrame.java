package ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.UIManager;

import tacoconfigsplugin.popup.actions.Config;
import tacoconfigsplugin.popup.actions.Config.ConfigType;
import tacoconfigsplugin.popup.actions.ParseConfigurations;

@SuppressWarnings("serial")
public class MainFrame extends JFrame implements ActionListener {

	// TODO: Get class name/path from eclipse plugin
	private String testFile = "C:/Users/Federico/git/itba/avmc/taco-configuration/taco-configs-plugin/src/tacoconfigsplugin/popup/actions/TestClass.java";
	private List<JSlider> sliders = new ArrayList<>();
	private List<JTextField> textFields = new ArrayList<>();
	private List<JCheckBox> checkboxes = new ArrayList<>();
	private ParseConfigurations parseConfig;
	private Map<String, List<Config>> configMap;

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
		JPanel labelsPanel = new JPanel();
		labelsPanel.setLayout(new BoxLayout(labelsPanel, BoxLayout.Y_AXIS));
		JButton button = new JButton("Save");
		labelsPanel.add(button);
		button.addActionListener(this);
		parseConfig = new ParseConfigurations(testFile);
		configMap = parseConfig.configurations();
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
					checkBox.setName(label.getText());
					checkboxes.add(checkBox);
				} else if (config.isInteger()) {
					JSlider slider = new JSlider();
					slider.setPaintLabels(true);
					slider.setPaintTicks(true);
					slider.setSize(new Dimension(200, 50));
					slider.setMajorTickSpacing(1);
					// TODO: Setear bien máximo
					slider.setMaximum(5);
					slider.setMinimum(0);
					slider.setMinorTickSpacing(1);
					slider.setValue(config.intValue());
					rowPanel.add(slider);
					slider.setName(label.getText());
					sliders.add(slider);
				} else if (config.isDouble() || config.isString()) {
					JTextField textField = new JTextField(45);
					label.setLabelFor(textField);
					textField.setText(config.value());
					rowPanel.add(textField);
					textField.setName(label.getText());
					textFields.add(textField);
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
		Map<String, List<Config>> updatedConfigMap = new HashMap<>();
		for (String method : configMap.keySet()) {
			updatedConfigMap.put(method, new ArrayList<>());
			List<Config> config = updatedConfigMap.get(method);
			for (JCheckBox jCheckBox : checkboxes) {
				config.add(new Config(jCheckBox.getName(), jCheckBox.isSelected(), ConfigType.Boolean));
				// System.out.println(jCheckBox.getName() + ": " + jCheckBox.isSelected());
			}
			for (JSlider jSlider : sliders) {
				config.add(new Config(jSlider.getName(), jSlider.getValue(), ConfigType.Integer));
				// System.out.println(jSlider.getName() + ": " + jSlider.getValue());
			}
			for (JTextField jTextField : textFields) {
				config.add(new Config(jTextField.getName(), jTextField.getText(), ConfigType.String));
				// System.out.println(jTextField.getName() + ": " + jTextField.getText());
			}
		}
		new ParseConfigurations(testFile).setConfigurations(updatedConfigMap);
	}

}
