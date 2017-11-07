package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.BorderFactory;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import java.awt.FlowLayout;

import net.miginfocom.swing.MigLayout;

import com.jgoodies.forms.layout.FormSpecs;

import java.awt.GridLayout;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JInternalFrame;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.TextArea;

import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import java.awt.Color;
import java.awt.SystemColor;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import java.awt.Component;

public class Frame {

	private JFrame frame;
	private JTextField nameDataTxt, minSupTxt, minConfTxt;
	private TextArea msgAreaTxt, rulesAreaTxt;
	private JTextField nameFileTxt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					Frame window = new Frame();
					window.frame.setVisible(true);
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Frame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 621, 440);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel cpParameterSetting = new JPanel();
		cpParameterSetting.setBounds(10, 11, 585, 132);
		cpParameterSetting.setBorder(BorderFactory.createTitledBorder("Apriori"));
		frame.getContentPane().add(cpParameterSetting);
		cpParameterSetting.setLayout(null);
		
		JPanel cpAprioriMining = new JPanel();
		cpAprioriMining.setBounds(10, 23, 160, 81);
		cpAprioriMining.setBorder(BorderFactory.createTitledBorder("Selecting Data Source"));
		cpParameterSetting.add(cpAprioriMining);
		cpAprioriMining.setLayout(null);
		
		JRadioButton db = new JRadioButton("Learning rules from db");
		db.setBounds(6, 25, 148, 23);
		cpAprioriMining.add(db);
		
		JRadioButton file = new JRadioButton("Reading rules from file");
		file.setBounds(6, 51, 148, 23);
		cpAprioriMining.add(file);
		
		JPanel cpAprioriInput = new JPanel();
		cpAprioriInput.setBounds(251, 23, 283, 81);
		cpAprioriInput.setBorder(BorderFactory.createTitledBorder("Input Parameters"));
		cpParameterSetting.add(cpAprioriInput);
		cpAprioriInput.setLayout(null);
		
		JLabel data = new JLabel("Data");
		data.setBounds(10, 27, 46, 14);
		cpAprioriInput.add(data);
		
		nameDataTxt = new JTextField();
		nameDataTxt.setBounds(39, 24, 86, 20);
		cpAprioriInput.add(nameDataTxt);
		nameDataTxt.setColumns(10);
		
		JLabel minSup = new JLabel("MinSup");
		minSup.setBounds(150, 27, 46, 14);
		cpAprioriInput.add(minSup);
		
		minSupTxt = new JTextField();
		minSupTxt.setBounds(206, 24, 39, 20);
		cpAprioriInput.add(minSupTxt);
		minSupTxt.setColumns(3);
		
		JLabel minConf = new JLabel("MinConf");
		minConf.setBounds(150, 56, 46, 14);
		cpAprioriInput.add(minConf);
		
		minConfTxt = new JTextField();
		minConfTxt.setColumns(3);
		minConfTxt.setBounds(206, 53, 39, 20);
		cpAprioriInput.add(minConfTxt);
		
		JLabel nameFile = new JLabel("Save");
		nameFile.setBounds(10, 52, 46, 14);
		cpAprioriInput.add(nameFile);
		
		nameFileTxt = new JTextField();
		nameFileTxt.setBounds(39, 52, 86, 20);
		cpAprioriInput.add(nameFileTxt);
		nameFileTxt.setColumns(10);
		
		JPanel cpRuleViewer = new JPanel();
		cpRuleViewer.setBounds(10, 195, 585, 196);
		cpRuleViewer.setBorder(BorderFactory.createTitledBorder("Pattern and Rules"));
		frame.getContentPane().add(cpRuleViewer);
		cpRuleViewer.setLayout(null);
		
		rulesAreaTxt = new TextArea();
		rulesAreaTxt.setBounds(10, 25, 565, 96);
		rulesAreaTxt.setEditable(false);
		cpRuleViewer.add(rulesAreaTxt);
		
		msgAreaTxt = new TextArea();
		msgAreaTxt.setBounds(10, 127, 565, 59);
		msgAreaTxt.setEditable(false);
		cpRuleViewer.add(msgAreaTxt);
		
		JPanel cpMiningCommand = new JPanel();
		cpMiningCommand.setBounds(10, 151, 585, 33);
		frame.getContentPane().add(cpMiningCommand);
		cpMiningCommand.setLayout(null);
		
		JButton aprioriConstructionBt = new JButton("MINE");
		aprioriConstructionBt.setBounds(132, 0, 140, 23);
		cpMiningCommand.add(aprioriConstructionBt);
		
		JButton aprioriPDFBt = new JButton("MINE & SAVE on PDF");
		aprioriPDFBt.setBounds(282, 0, 140, 23);
		cpMiningCommand.add(aprioriPDFBt);

		
	}
}
