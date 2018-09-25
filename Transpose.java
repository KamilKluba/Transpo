//Nothing changed
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Transpose extends JFrame implements ChangeListener, ActionListener, KeyListener
{
	private static final long serialVersionUID = 1L;
	
	private String chordsTable;
	private ArrayList<String> dividedChordsTable;
	private ArrayList<String> transposedTable;
	private ArrayList<Integer> digitTable;
	private ArrayList<Integer> transposedDigitTable;
	private String tab1[] = { "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "H" };
	private String tab2[] = { "c", "c#", "d", "d#", "e", "f", "f#", "g", "g#", "a", "a#", "h" };
	
	private JButton copyButton = new JButton("Kopiuj wynik");
	private JMenu[] menu = { new JMenu("Program"),
							 new JMenu("Plik"),
							 new JMenu("O programie")};
	private JMenuItem[] items = { new JMenuItem("Podaj akordy"),
								  new JMenuItem("Poziom transpozycji"),
								  new JMenuItem("Zamknij"),
								  new JMenuItem("Kopiuj wynik"),
								  new JMenuItem("Zapisz do pliku"),
								  new JMenuItem("O programie")
								  };
	
	private	JTextField chords = new JTextField(20);
	
	private JLabel outChords = new JLabel("", JLabel.CENTER);
	private JLabel transposition = new JLabel("", JLabel.CENTER);
	
	private JSlider trans = new JSlider(JSlider.HORIZONTAL, min, max, start);
	
	private int transLevel;
	
	static final int min = -12;
	static final int max = 12;
	static final int start = 0;
	
	public Transpose(){
		super("Transpose your chords by Kolba");
		setSize(600,250);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		
		JLabel insChords = new JLabel("Wprowadz chwyty:", JLabel.CENTER);
		
		menu[0].add(items[0]);
		menu[0].add(items[2]);
		menu[1].add(items[3]);
		menu[1].add(items[4]);
		menu[2].add(items[5]);
		
		JMenuBar menuBar = new JMenuBar();
		for (JMenu currentMenu : menu)
			menuBar.add(currentMenu);
		setJMenuBar(menuBar);
		
		for (JMenuItem item : items)
			item.addActionListener(this);

		JPanel mainPanel = new JPanel(new BorderLayout(5,5));
		
		JPanel insertChords = new JPanel(new GridLayout(3,1));
		insertChords.setBorder(BorderFactory.createTitledBorder("Akordy wejsciowe"));
		insertChords.add(insChords);
		insertChords.add(chords);
		copyButton.addActionListener(this);
		insertChords.add(copyButton);
		//chords.
		mainPanel.add(insertChords, BorderLayout.WEST);
		
		JPanel transpositionLevel = new JPanel(new GridLayout(1,2));
		transpositionLevel.setBorder(BorderFactory.createTitledBorder("Poziom transpozycji"));
		trans.setMinorTickSpacing(1);
		trans.setMajorTickSpacing(4);
		trans.setPaintTicks(true);
		trans.setPaintLabels(true);
		trans.addChangeListener(this);
		transpositionLevel.add(trans);
		mainPanel.add(transpositionLevel, BorderLayout.SOUTH);
		
		JPanel outputChords = new JPanel(new GridLayout(2,1));
		outputChords.setBorder(BorderFactory.createTitledBorder("Akordy wyjsciowe"));
		outputChords.add(transposition);
		outputChords.add(outChords);
		mainPanel.add(outputChords, BorderLayout.CENTER);
	
		dividedChordsTable = new ArrayList<String>();
		digitTable = new ArrayList<Integer>();
		transposedDigitTable = new ArrayList<Integer>();
		transposedTable = new ArrayList<String>();
		chords.addKeyListener(this);
		
		setContentPane(mainPanel);
		setVisible(true);
	}
	

	@Override
	public void stateChanged(ChangeEvent arg0) {
		chordsTable = "";
		digitTable.clear();
		transposedDigitTable.clear();
		dividedChordsTable.clear();
		transposedTable.clear();
		transpose();	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source == items[0]){
			digitTable.clear();
			transposedDigitTable.clear();
			dividedChordsTable.clear();
			transposedTable.clear();
			chords.setText(JOptionPane.showInputDialog(this, "Podaj akordy:"));
			trans.setValue(Integer.parseInt(JOptionPane.showInputDialog(this, "Podaj poziom transpozycji:")));
			outChords.setText("" + trans.getValue());;
			transpose();	
		}
		if (source == items[2])
			System.exit(0);
		if (source == items[3] || source == copyButton){
			StringSelection text = new StringSelection(outChords.getText());
			Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
			clpbrd.setContents(text, null);
		}
		if (source == items[4]){
			
		}
		if (source == items[5])
			JOptionPane.showMessageDialog(this, "Autor: Kamil Kluba \n v1.0");
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}


	@Override
	public void keyReleased(KeyEvent arg0) {
		switch (arg0.getKeyCode()){
		case (KeyEvent.VK_ENTER):
			chordsTable = "";
			digitTable.clear();
			transposedDigitTable.clear();
			dividedChordsTable.clear();
			transposedTable.clear();
			transpose();
		}
	}


	@Override
	public void keyTyped(KeyEvent arg0) {	
	}
	
	public void transpose(){
		String currentNote = "";
		
		chordsTable = chords.getText();
		transposition.setText("Przetransponowano o " + trans.getValue());
		
		for (int dataCounter = 0; dataCounter < chordsTable.length(); dataCounter++){
			if(chordsTable.charAt(dataCounter) == ' ' || currentNote.endsWith("#")){	
				dividedChordsTable.add(currentNote);
				currentNote = "";
			}
			else if (dataCounter < chordsTable.length() - 1 && chordsTable.charAt(dataCounter + 1) != '#' && chordsTable.charAt(dataCounter + 1) != ' '){
				currentNote += chordsTable.charAt(dataCounter);	
				dividedChordsTable.add(currentNote);
				currentNote = "";
			} 	
			else
				currentNote += chordsTable.charAt(dataCounter);		
		}
		
		dividedChordsTable.add(currentNote);
		currentNote = "";		
			
		for (int dataCounter2 = 0; dataCounter2 < dividedChordsTable.size(); dataCounter2++)
			for (int dataCounter = 0; dataCounter < 12; dataCounter++){
				if (dividedChordsTable.get(dataCounter2).equals(tab1[dataCounter])){
					digitTable.add(dataCounter);
				}
				if (dividedChordsTable.get(dataCounter2).equals(tab2[dataCounter])){
					digitTable.add(dataCounter + 12);
				}
			}
		
		transLevel = trans.getValue();
		if (transLevel < 0) transLevel = 12 - transLevel * (-1);
				
		for (int dataCounter = 0; dataCounter < digitTable.size(); dataCounter++)
			if (digitTable.get(dataCounter) < 12) transposedDigitTable.add((digitTable.get(dataCounter) + transLevel) % 12);
			else transposedDigitTable.add(((digitTable.get(dataCounter) + transLevel - 12) % 12) + 12);
		
		for (int dataCounter = 0; dataCounter < transposedDigitTable.size(); dataCounter++){
			if (transposedDigitTable.get(dataCounter) < 12) transposedTable.add(tab1[transposedDigitTable.get(dataCounter)]);
			else transposedTable.add(tab2[transposedDigitTable.get(dataCounter) - 12]);
		}
		
		String finalText = "";
		
		for (String data : transposedTable)
			finalText = finalText + data + " ";
		
		outChords.setText(finalText);
	}

	public static void main(String[] args){
		new Transpose();
	}

}
