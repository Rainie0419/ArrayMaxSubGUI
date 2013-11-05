package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import utils.Utils;

import model.MaxSubArray;

public class MainWindow {

	private JFrame frame;
	private JTextField txtfdFilePath;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnGetMax = new JButton("Get Max");
		btnGetMax.setBounds(331, 229, 93, 23);
		frame.getContentPane().add(btnGetMax);
		
		txtfdFilePath = new JTextField();
		txtfdFilePath.setBounds(73, 230, 248, 21);
		frame.getContentPane().add(txtfdFilePath);
		txtfdFilePath.setColumns(10);
		
		JLabel lblFilePath = new JLabel("File Path:");
		lblFilePath.setBounds(9, 233, 67, 15);
		frame.getContentPane().add(lblFilePath);
		
		final JTabbedPane pnlDataTab = new JTabbedPane();
		pnlDataTab.setBounds(10, 10, 311, 210);
		frame.getContentPane().add(pnlDataTab);
		
		final JCheckBox chckbxHorizontally = new JCheckBox("Horizontally");
		chckbxHorizontally.setBounds(327, 35, 103, 23);
		frame.getContentPane().add(chckbxHorizontally);
		
		final JCheckBox chckbxVertically = new JCheckBox("Vertically");
		chckbxVertically.setBounds(327, 70, 103, 23);
		frame.getContentPane().add(chckbxVertically);
		
		JLabel lblMaxSumIs = new JLabel("Max Sum is:");
		lblMaxSumIs.setBounds(331, 128, 73, 15);
		frame.getContentPane().add(lblMaxSumIs);
		
		final JLabel lblResult = new JLabel("unknown");
		lblResult.setVerticalAlignment(SwingConstants.TOP);
		lblResult.setBounds(331, 153, 93, 66);
		frame.getContentPane().add(lblResult);
		
		btnGetMax.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String filename=txtfdFilePath.getText();
				String maxResult="Invalid Input.";
				boolean horSelected=chckbxHorizontally.isSelected();
				//System.out.println("Horizontally? "+horSelected);
				boolean verSelected=chckbxVertically.isSelected();
				//System.out.println("Vertically? "+verSelected);
				
				if(!horSelected&&!verSelected)
					maxResult = MaxSubArray.getMaxSum(filename,'r');
				else if(!horSelected&&verSelected)
					maxResult = MaxSubArray.getMaxSum(filename,'v');
				else if(horSelected&&!verSelected)
					maxResult = MaxSubArray.getMaxSum(filename,'h');
				else if(horSelected&&verSelected)
					maxResult = MaxSubArray.getMaxSum(filename,'b');
				
				lblResult.setText(maxResult);
				
				if(!maxResult.equals("Invalid input.")){
					int[][] dataArray = MaxSubArray.getArrayInFile(filename);
					Object[][] array = Utils.toObjectArray(dataArray);
					int[] indexNote=MaxSubArray.getIndexNote();
					pnlDataTab.add(filename,createContent(filename,array,indexNote));
					pnlDataTab.setTabComponentAt(pnlDataTab.getTabCount()-1, new ButtonTabComponent(pnlDataTab));
					pnlDataTab.setSelectedIndex(pnlDataTab.getTabCount()-1);
				}
				 			
				
			}

			private Component createContent(String tabname, Object[][] array, final int[] indexNote) {
				//maybe JScrollPane better
		       JPanel panel = new JPanel(new GridLayout(1,1));
		       String[] header = new String[array[0].length];
		       for(int i=0;i<header.length;i++){
		    	   header[i]="Column"+i;
		       }
//		       System.out.print("Here is indexNote:");
//		       for(int i:indexNote){
//		    	   System.out.print(i+" ");
//		       }
//		       System.out.println();
		       DefaultTableModel model = new DefaultTableModel(array, header) {
		    	   public boolean isCellEditable(int row, int column) {
		    	       return false;
		    	   }
		       };
		       
		       DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() { 
		    	   public Component getTableCellRendererComponent(JTable table, 
		    	     Object value, boolean isSelected, boolean hasFocus, 
		    	     int row, int column) { 
		    		   int startRow=indexNote[0];
		    		   int endRow=indexNote[1];
		    		   int startCol=indexNote[2];
		    		   int endCol=indexNote[3];
		    		   if(startRow<=endRow){
		    			   if(startCol<=endCol){
		    				   if(row>=startRow&&row<=endRow&&column>=startCol&&column<=endCol) 
				    			   setBackground(Color.YELLOW); 
				    		   else 
				    			   setBackground(Color.WHITE); 
		    			   }else{
		    				   if(row>=startRow&&row<=endRow&&(column<=endCol||column>=startCol))
		    					   setBackground(Color.YELLOW); 
		    				   else
		    					   setBackground(Color.WHITE); 
		    			   }
		    		   }else{
		    			   if(startCol<=endCol){
		    				   if((row<=endRow||row>=startRow)&&column>=startCol&&column<=endCol) 
				    			   setBackground(Color.YELLOW); 
				    		   else 
				    			   setBackground(Color.WHITE); 
		    			   }else{
		    				   if((row<=endRow||row>=startRow)&&(column<=endCol||column>=startCol)) 
				    			   setBackground(Color.YELLOW); 
				    		   else 
				    			   setBackground(Color.WHITE); 
		    			   }
		    		   }
		    		   
		    		   return super.getTableCellRendererComponent(table, value, 
		    				   isSelected, hasFocus, row, column); 
		    	   } 
		       }; 
		       JTable table=new JTable(model);
		       for(int i=0;i<header.length;i++){
		    	   table.getColumn(header[i]).setCellRenderer(tcr);
		       }
		       
		       panel.add(table);
		       return panel;
			}
		});
	}
}
