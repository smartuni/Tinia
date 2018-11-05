import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MonitorFrame extends JFrame {
	
	private JLabel label = new JLabel("Berechne...");
	private BorderLayout borderLayout = new BorderLayout();
	private JPanel minMaxPanel = new JPanel();
	private JLabel minLabel = new JLabel("Min: 0 km/h");
	private JLabel maxLabel = new JLabel("Max: 0 km/h");
	private int minValue = Integer.MAX_VALUE;
	private int maxValue = 0;
	
	public MonitorFrame() {
		setTitle("Windgeschwindigkeit");
		setLayout(borderLayout);
		add(label, BorderLayout.CENTER);
		minMaxPanel.setLayout(new FlowLayout());
		minMaxPanel.add(minLabel);
		minMaxPanel.add(maxLabel);
		add(minMaxPanel, BorderLayout.PAGE_END);
		setSize(300, 300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void updateSpeed(Integer integer) {
		System.out.println(integer);
		label.setText(integer.toString() + " km/h");
		if(integer < minValue) {
			minValue = integer;
		}
		if(integer > maxValue) {
			maxValue = integer;
		}
		minLabel.setText("Min: "+ minValue +" km/h");
		maxLabel.setText("Max: "+ maxValue +" km/h");
		repaint();
	}
}
