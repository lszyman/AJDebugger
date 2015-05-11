package pl.edu.agh.toik.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import pl.edu.agh.toik.interfaces.Command;

public class BreakpointFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textArea;
	private JButton btnNextBreakpoint;
	private JButton btnNextPointcut;
	
	private Command command;
	
	public BreakpointFrame(String breakpointInfo, Command command) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(400, 200, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
		textArea.setEditable(false);
		textArea.setText(breakpointInfo);
		
		JScrollPane spBreakpoints = new JScrollPane(textArea);
		spBreakpoints.setBounds(5, 5, 400, 300);
		contentPane.add(spBreakpoints);
		
		try {
			createButtons();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setVisible(true);
		
		this.command = command;
	}
	
	private void createButtons() throws IOException {
		BufferedImage btnIconNextPointcut = ImageIO.read(new File("images/next_pointcut.png"));
		BufferedImage btnIconNextBreakPoint = ImageIO.read(new File("images/next_breakpoint.png"));
		
		btnNextPointcut = new JButton(new ImageIcon(btnIconNextPointcut));
		btnNextPointcut.setToolTipText("Next pointcut");
		btnNextPointcut.setBounds(10, 500, 40, 40);
		btnNextPointcut.setBorder(BorderFactory.createEmptyBorder());
		btnNextPointcut.setContentAreaFilled(false);
		btnNextPointcut.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				command.nextJoinpoint();
				dispose();
			}
		});
		contentPane.add(btnNextPointcut);
		
		btnNextBreakpoint = new JButton(new ImageIcon(btnIconNextBreakPoint));
		btnNextBreakpoint.setToolTipText("Next breakpoint");
		btnNextBreakpoint.setBounds(70, 500, 40, 40);
		btnNextBreakpoint.setBorder(BorderFactory.createEmptyBorder());
		btnNextBreakpoint.setContentAreaFilled(false);
		btnNextBreakpoint.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				command.nextBreakpoint();
				dispose();
			}
		});
		contentPane.add(btnNextBreakpoint);
	}
}
