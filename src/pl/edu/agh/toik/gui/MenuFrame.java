package pl.edu.agh.toik.gui;

import java.awt.EventQueue;
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

import org.reflections.Reflections;
import org.reflections.ReflectionsException;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class MenuFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTree tree;
	private DefaultMutableTreeNode treeRoot;
	private JTextField txtFldPackageSearch;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuFrame frame = new MenuFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MenuFrame() throws IOException {
		setTitle("AJDebugger");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		treeRoot = new DefaultMutableTreeNode("Root");
		createPackageTree("pl");
		tree = new JTree(treeRoot);
		tree.setRootVisible(false);
		tree.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setBounds(15, 45, 400, 600);
		contentPane.add(scrollPane);
		
		txtFldPackageSearch = new JTextField();
		txtFldPackageSearch.setText("pl");
		txtFldPackageSearch.setBounds(150, 16, 235, 25);
		contentPane.add(txtFldPackageSearch);
		
		JLabel lblNewLabel = new JLabel("Look in package");
		lblNewLabel.setBounds(15, 16, 150, 20);
		contentPane.add(lblNewLabel);
		
		BufferedImage buttonIcon = ImageIO.read(new File("images/search.png"));
		
		JButton btnLookFor = new JButton(new ImageIcon(buttonIcon));
		btnLookFor.setBorder(BorderFactory.createEmptyBorder());
		btnLookFor.setContentAreaFilled(false);
		btnLookFor.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
				
				root.removeAllChildren();
				createPackageTree(txtFldPackageSearch.getText());
				model.reload(root);
			}
		});
		btnLookFor.setBounds(390, 15, 25, 25);
		contentPane.add(btnLookFor);
	}
	
	public void createPackageTree(String packageName) {
		List<String> packs = findPackageNamesStartingWith(packageName);
		
		for(String pack : packs) {
			DefaultMutableTreeNode packageItem = new DefaultMutableTreeNode(pack);
			
			Set<Class<? extends Object>> allClasses = getAllClasses(pack);

			for(Class<? extends Object> clazz : allClasses) {
				
				if(!clazz.getSimpleName().equals("")) {
				
					String clazzString = pack+"."+clazz.getSimpleName();
					
					DefaultMutableTreeNode classItem = new DefaultMutableTreeNode(clazz.getSimpleName());
					
					Method[] methods = getMethods(clazzString);
					for(Method method : methods) {
						Class<?>[] paramTypes = method.getParameterTypes();
						String params = "(";
						for(Class<?> type : paramTypes){
							params += type.getSimpleName();
						}
						
						String returnType = method.getReturnType().getCanonicalName();
						String functionString = returnType+" "+method.getName()+params+")";
						
						classItem.add(new DefaultMutableTreeNode(functionString));
					}
					packageItem.add(classItem);
				}
			}
			treeRoot.add(packageItem);
		}
	}
	
	public static Set<Class<? extends Object>> getAllClasses(String packageName) {
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
	
	public static Method[] getMethods(String className) {
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
	
	public List<String> findPackageNamesStartingWith(String prefix) {
	    List<String> result = new ArrayList<String>();
	    for(Package p : Package.getPackages()) {
	        if (prefix == null || prefix.equals("") || p.getName().startsWith(prefix)) {
	           result.add(p.getName());
	        }
	    }
	    return result;
	}
}
