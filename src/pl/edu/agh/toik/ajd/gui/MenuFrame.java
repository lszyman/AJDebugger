package pl.edu.agh.toik.ajd.gui;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import pl.edu.agh.toik.ajd.debugger.Debugger;
import pl.edu.agh.toik.ajd.debugger.DebuggerMode;

public class MenuFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private final String defaultPackage = "pl";
	
	private JPanel contentPane;
	private JTree treePackages;
	private DefaultMutableTreeNode treePackagesRoot;
	private JTextField txtFldPackageSearch;
	private JList<String> jlistBreakpoints;
	private DefaultListModel<String> breakpoints = new DefaultListModel<String>();
	private final ButtonGroup modeButtonGroup = new ButtonGroup();

	public MenuFrame() throws IOException {
		setTitle("AJDebugger");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		createLabels();
		createButtons();
		
		treePackagesRoot = new DefaultMutableTreeNode("Root");
		createPackageTree(defaultPackage);
		treePackages = new JTree(treePackagesRoot);
		treePackages.setRootVisible(false);
		treePackages.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JScrollPane spTreePackages = new JScrollPane(treePackages);
		spTreePackages.setBounds(15, 50, 400, 600);
		contentPane.add(spTreePackages);
		
		txtFldPackageSearch = new JTextField();
		txtFldPackageSearch.setText(defaultPackage);
		txtFldPackageSearch.setBounds(140, 15, 235, 30);
		contentPane.add(txtFldPackageSearch);
		
		jlistBreakpoints = new JList<String>(breakpoints);
		jlistBreakpoints.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JScrollPane spBreakpoints = new JScrollPane(jlistBreakpoints);
		spBreakpoints.setBounds(465, 50, 400, 600);
		contentPane.add(spBreakpoints);
		
		JPanel panel = new JPanel();
		panel.setBounds(540, 668, 138, 60);
		contentPane.add(panel);
		
		JLabel lblModes = new JLabel("Modes:");
		panel.add(lblModes);
		
		JSplitPane splitPane = new JSplitPane();
		panel.add(splitPane);
		
		JRadioButton rdbtnInclusiveMode = new JRadioButton("Include");
		rdbtnInclusiveMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debugger.getInstance().setMode(DebuggerMode.INCLUSIVE);
			}
		});
		modeButtonGroup.add(rdbtnInclusiveMode);
		rdbtnInclusiveMode.setSelected(true);
		splitPane.setLeftComponent(rdbtnInclusiveMode);
		
		JRadioButton rdbtnExclusiveMode = new JRadioButton("Exclude");
		rdbtnExclusiveMode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Debugger.getInstance().setMode(DebuggerMode.EXCLUSIVE);
			}
		});
		modeButtonGroup.add(rdbtnExclusiveMode);
		splitPane.setRightComponent(rdbtnExclusiveMode);
	}
	
	private void createLabels() {
		JLabel lblLookInPackage = new JLabel("Look in package:");
		lblLookInPackage.setBounds(15, 15, 150, 30);
		contentPane.add(lblLookInPackage);
		
		JLabel lblYourBreakpoints = new JLabel("Your breakpoints:");
		lblYourBreakpoints.setBounds(470, 15, 150, 30);
		contentPane.add(lblYourBreakpoints);
	}
	
	private void createButtons() throws IOException {
		BufferedImage btnIconSearch = ImageIO.read(new File("images/search.png"));
		BufferedImage btnIconForward = ImageIO.read(new File("images/forward.png"));
		BufferedImage btnIconBack = ImageIO.read(new File("images/back.png"));
		BufferedImage btnIconRefresh = ImageIO.read(new File("images/refresh.png"));
		
		JButton btnLookFor = new JButton(new ImageIcon(btnIconSearch));
		btnLookFor.setBounds(383, 12, 32, 32);
		btnLookFor.setContentAreaFilled(false);
		btnLookFor.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				DefaultTreeModel model = (DefaultTreeModel)treePackages.getModel();
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
				
				root.removeAllChildren();
				createPackageTree(txtFldPackageSearch.getText());
				model.reload(root);
			}
		});
		contentPane.add(btnLookFor);
		
		JButton btnSetBreakpoint = new JButton(new ImageIcon(btnIconForward));
		btnSetBreakpoint.setBounds(420, 220, 40, 40);
		btnSetBreakpoint.setContentAreaFilled(false);
		//btnSetBreakpoint.setBorderPainted(false);
		btnSetBreakpoint.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				TreePath treePath = treePackages.getSelectionPath();
				if(treePath != null) {
					String newElement = treePath.getLastPathComponent().toString();
					if(!breakpoints.contains(newElement))
						breakpoints.addElement(newElement);
						Debugger.getInstance().addBreakpoint(newElement);
				}
			}
		});
		contentPane.add(btnSetBreakpoint);//breakpoints
		
		JButton btnUnsetBreakpoint = new JButton(new ImageIcon(btnIconBack));
		btnUnsetBreakpoint.setBounds(420, 275, 40, 40);
		btnUnsetBreakpoint.setContentAreaFilled(false);
		//btnUnsetBreakpoint.setBorderPainted(false);
		btnUnsetBreakpoint.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				int index = jlistBreakpoints.getSelectedIndex();
				if(index != -1) {
					String removed = breakpoints.remove(index);
					Debugger.getInstance().removeBreakpoint(removed);
				}
			}
		});
		contentPane.add(btnUnsetBreakpoint);
		
		JButton btnRefreshBreakpoints = new JButton(new ImageIcon(btnIconRefresh));
		btnRefreshBreakpoints.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(String signature : Debugger.getInstance().getBreakpointSignatrues()) {
					if(!breakpoints.contains(signature))
						breakpoints.addElement(signature);
				}
			}
		});
		btnRefreshBreakpoints.setContentAreaFilled(false);
		btnRefreshBreakpoints.setBounds(420, 326, 40, 40);
		contentPane.add(btnRefreshBreakpoints);
	}
	
	private void createPackageTree(String packageName) {
		List<String> packs = findPackageNamesStartingWith(packageName);
		
		for(String pack : packs) {
			DefaultMutableTreeNode packageItem = new DefaultMutableTreeNode(pack);
			
			Set<Class<? extends Object>> allClasses = getAllClasses(pack);

			for(Class<? extends Object> clazz : allClasses) {
				
				if(!clazz.getSimpleName().equals("")) {
				
					String clazzString = pack+"."+clazz.getSimpleName();
					
					DefaultMutableTreeNode classItem = new DefaultMutableTreeNode(clazz.getSimpleName());
					
					Method[] methods = getMethods(clazzString);
					if(methods != null)
						for(Method method : methods) {
							String signature = method.toString();
							String withoutAccessModifier = signature.substring(signature.indexOf(method.getReturnType().getCanonicalName()));
							classItem.add(new DefaultMutableTreeNode(withoutAccessModifier));
						}
					packageItem.add(classItem);
				}
			}
			treePackagesRoot.add(packageItem);
		}
	}
	
	private Set<Class<? extends Object>> getAllClasses(String packageName) {
		try {
			List<ClassLoader> classLoadersList = new LinkedList<ClassLoader>();
			classLoadersList.add(ClasspathHelper.contextClassLoader());
			classLoadersList.add(ClasspathHelper.staticClassLoader());
			
			Reflections reflections = new Reflections(new ConfigurationBuilder()
		    .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
		    .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
		    .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));
			
			 Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);
			 return allClasses;
		} catch(ReflectionsException e) {
			return null;
		}
	}
	
	private Method[] getMethods(String className) {
		Class<? extends Object> aClass = null;
		Method[] methods = null;
		try {
			aClass = Class.forName(className);
			methods = aClass.getDeclaredMethods();
		} catch (ClassNotFoundException e) {
			return null;
		}
		
		return methods;
	}
	
	private List<String> findPackageNamesStartingWith(String prefix) {
	    List<String> result = new ArrayList<String>();
	    for(Package p : Package.getPackages()) {
	        if (prefix == null || prefix.equals("") || p.getName().startsWith(prefix)) {
	           result.add(p.getName());
	        }
	    }
	    return result;
	}
}
