package pl.edu.agh.toik.ajd.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;

import org.aspectj.lang.JoinPoint;

import pl.edu.agh.toik.ajd.debugger.Debugger;
import pl.edu.agh.toik.ajd.interfaces.Command;

import javax.swing.JTable;
public class BreakpointFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextArea textArea;
	
	private Command command;
	private JoinPoint joinpoint;
	private JTable table;
	
	public BreakpointFrame(JoinPoint joinpoint, Command command) {
		this.joinpoint = joinpoint;
		this.command = command;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(400, 200, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBorder(new EmptyBorder(5, 5, 5, 5));
		textArea.setEditable(false);
		textArea.setText(getJoinPointInfo(joinpoint));
		
		JScrollPane spBreakpoints = new JScrollPane(textArea);
		spBreakpoints.setBounds(5, 5, 400, 300);
		contentPane.add(spBreakpoints);
		
		try {
			createButtons();
		} catch (IOException e) {
			e.printStackTrace();
		}
		createTable(joinpoint);

		setVisible(true);
	}
	
	private void createButtons() throws IOException {
		BufferedImage btnIconNextBreakpoint = ImageIO.read(new File("images/next_breakpoint.png"));
		BufferedImage btnIconStepInto = ImageIO.read(new File("images/step_into.png"));
		BufferedImage btnIconStepOver = ImageIO.read(new File("images/step_over.png"));
		BufferedImage btnIconStepOut = ImageIO.read(new File("images/step_out.png"));
		BufferedImage btnIconExclude = ImageIO.read(new File("images/exclude.png"));
		
		JButton btnNextBreakpoint = new JButton(new ImageIcon(btnIconNextBreakpoint));
		btnNextBreakpoint.setToolTipText("Next breakpoint");
		btnNextBreakpoint.setBounds(10, 500, 40, 40);
		btnNextBreakpoint.setBorder(BorderFactory.createEmptyBorder());
		btnNextBreakpoint.setContentAreaFilled(false);
		btnNextBreakpoint.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Debugger.getInstance().setCustomArgs(getFunctionArgumentsFromTable());
				command.nextBreakpoint();
				dispose();
			}
		});
		contentPane.add(btnNextBreakpoint);
		
		JButton btnStepInto = new JButton(new ImageIcon(btnIconStepInto));
		btnStepInto.setToolTipText("Step into");
		btnStepInto.setBounds(70, 500, 40, 40);
		btnStepInto.setBorder(BorderFactory.createEmptyBorder());
		btnStepInto.setContentAreaFilled(false);
		btnStepInto.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Debugger.getInstance().setCustomArgs(getFunctionArgumentsFromTable());
				command.stepInto();
				dispose();
			}
		});
		contentPane.add(btnStepInto);
		
		JButton btnStepOver = new JButton(new ImageIcon(btnIconStepOver));
		btnStepOver.setToolTipText("Step over");
		btnStepOver.setBounds(130, 500, 40, 40);
		btnStepOver.setBorder(BorderFactory.createEmptyBorder());
		btnStepOver.setContentAreaFilled(false);
		btnStepOver.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Debugger.getInstance().setCustomArgs(getFunctionArgumentsFromTable());
				command.stepOver();
				dispose();
			}
		});
		contentPane.add(btnStepOver);
		
		JButton btnStepOut = new JButton(new ImageIcon(btnIconStepOut));
		btnStepOut.setToolTipText("Step out");
		btnStepOut.setBounds(190, 500, 40, 40);
		btnStepOut.setBorder(BorderFactory.createEmptyBorder());
		btnStepOut.setContentAreaFilled(false);
		btnStepOut.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Debugger.getInstance().setCustomArgs(getFunctionArgumentsFromTable());
				command.stepOut();
				dispose();
			}
		});
		contentPane.add(btnStepOut);
		
		JButton btnExclude = new JButton(new ImageIcon(btnIconExclude));
		btnExclude.setToolTipText("Exclude this joinpoint");
		btnExclude.setBounds(250, 500, 40, 40);
		btnExclude.setBorder(BorderFactory.createEmptyBorder());
		btnExclude.setContentAreaFilled(false);
		btnExclude.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String signature = joinpoint.getSignature().toLongString();
				signature = signature.substring(signature.indexOf(" ") + 1);
				Debugger.getInstance().addBreakpoint(signature);
			}
		});
		contentPane.add(btnExclude);
	}
	
	private void createTable(JoinPoint joinpoint) {
		String[] columnNames = {"Arg No.", "Arg Value"};
		Object[] args = joinpoint.getArgs();
		LinkedList<Integer> editableRows = new LinkedList<Integer>();
		
		String[][] data = new String[args.length][2];	
		for(int i=0; i<args.length; i++) {
			data[i][0] = "arg"+(i+1);
			data[i][1] = args[i].toString();
			
			String className = args[i].getClass().getName();
			String[] allowedClassNames = {String.class.getName(), Integer.class.getName(), Long.class.getName(),
					Float.class.getName(), Double.class.getName(), int.class.getName(), long.class.getName(),
					float.class.getName(), double.class.getName()};
			
			for(String cn : allowedClassNames)
				if(cn.equals(className))
					editableRows.add(i);
		}

		final LinkedList<Integer> editableRowsFinal = new LinkedList<Integer>(editableRows);
		
		table = new JTable(data, columnNames) {
	        private static final long serialVersionUID = 1L;

	        public boolean isCellEditable(int row, int column) {
	        	if(column == 1 && editableRowsFinal.contains(row)) {
	        		return true;
	        	}
	        	
	        	return false;
	        };
	    };
		JScrollPane spTable = new JScrollPane(table);
		spTable.setBounds(460, 5, 300, 300);
		contentPane.add(spTable);
	}
	
	private Object[] getFunctionArgumentsFromTable() {
		table.clearSelection();						//aby zapisalo w tabeli aktualnie wprowadzane zmiany
		if (table.isEditing())						//
    	    table.getCellEditor().stopCellEditing();//
		
		Object[] argValues = new Object[table.getRowCount()];
		
		for(int i=0; i<table.getRowCount(); i++) {
			argValues[i] = table.getValueAt(i, 1);
		}
		
		return argValues;
	}
	
	private String getJoinPointInfo(JoinPoint joinpoint) {
		StringBuilder pointcutInfo = new StringBuilder();
		pointcutInfo.append("JoinPoint " + joinpoint.toShortString()+"\n");
		pointcutInfo.append("\tKind: " + joinpoint.getKind()+"\n");
		pointcutInfo.append("\tSignature: " + joinpoint.getSignature()+"\n");
		pointcutInfo.append("\tSourceLocation: " + joinpoint.getSourceLocation()+"\n");
		pointcutInfo.append("\tArgs:\n");
		for(Object arg : joinpoint.getArgs())
			pointcutInfo.append("\tClass: " + arg.getClass().getName() + ",\tValue: " + arg.toString()+"\n");
		
		return pointcutInfo.toString()+"\nPAUSED\n";
	}
}
