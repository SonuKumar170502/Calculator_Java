package MyCalcy;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
public class Calc extends JFrame implements ActionListener{
	JPanel panTop,panMemory,panBottom;
	JLabel lblResult,lblHistory;
	Font fnt;
	JButton btnMemory[],btnOthers[];
	String lblMemory[]= {"MC","MR","M+","M-","MS"};
	String lblOthers[]= {"%","\u221A","X\u00B2"," 1/x","CE","C","\u232b","\u00F7","7","8","9","\u00D7","4","5","6","-","1","2","3","+","\u2213","0",".","="};
	boolean overlapFlag=false;
	double history,currentN,memory;
	char op='\u0000';
	Calc(){
		//Top Panel 
		panTop=new JPanel();
		panTop.setLayout(new GridLayout(3,1,10,10));
		lblResult=new JLabel("0");
		lblHistory=new JLabel("");
		lblResult.setHorizontalAlignment(SwingConstants.RIGHT);
		lblHistory.setHorizontalAlignment(SwingConstants.RIGHT);
		fnt=new Font(Font.SERIF,Font.PLAIN,30);
		lblHistory.setFont(fnt);
		fnt=new Font(Font.SERIF,Font.PLAIN,40);
		lblResult.setFont(fnt);
		panMemory=new JPanel();
		panMemory.setLayout(new GridLayout(1,5,10,10));
		btnMemory=new JButton[5];
		for(int i=0;i<btnMemory.length;i++) {
			btnMemory[i]=new JButton(lblMemory[i]);
			btnMemory[i].setBackground(new Color(228,234,242));
			btnMemory[i].setBorder(BorderFactory.createEmptyBorder());
			btnMemory[i].setFocusPainted(false);
			btnMemory[i].addActionListener(this);
			panMemory.add(btnMemory[i]);
		}

		panTop.add(lblHistory);
		panTop.add(lblResult);
		panTop.add(panMemory);
		add(panTop,BorderLayout.NORTH);
		
		//Bottom Panel
		panBottom=new JPanel();
		panBottom.setLayout(new GridLayout(6,4,2,2));
		btnOthers=new JButton[24];
		for(int i=0;i<btnOthers.length;i++) {
			btnOthers[i]=new JButton(lblOthers[i]);
			if(Character.isDigit(lblOthers[i].charAt(0)))
				btnOthers[i].setBackground(new Color(255,255,255));
			else
				btnOthers[i].setBackground(new Color(228,234,242));
			btnOthers[i].setBorder(BorderFactory.createEmptyBorder());
			btnOthers[i].setFocusPainted(false);
			btnOthers[i].addActionListener(this);
			btnOthers[i].addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					JButton btn=(JButton)e.getSource();
					btn.setBackground(Color.cyan);
				}				
				public void mouseExited(MouseEvent e) {
					JButton btn=(JButton)e.getSource();
					if(Character.isDigit(btn.getText().charAt(0)))
						btn.setBackground(new Color(255,255,255));
					else
						btn.setBackground(new Color(228,234,242));
				}								
			});
			panBottom.add(btnOthers[i]);
		}
		add(panBottom,BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(400,600);
		setVisible(true);
	}
	public static void main(String[] args) {
		new Calc();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b1=(JButton)e.getSource();
		String s1=b1.getText();
		String current=lblResult.getText();
		if(current.equals("Cannot divide by zero"))
			currentN=0;
		else
			currentN=Double.parseDouble(current);
		if(Character.isDigit(s1.charAt(0))) {//Digits
			if(overlapFlag==false)
				lblResult.setText(filter(current+s1));//concat
			else {
				lblResult.setText(filter(s1));//overlap
				overlapFlag=false;
			}
		}
		else if(isOperator(s1.charAt(0))) {//operator
			if(op=='\u0000') {
				overlapFlag=true;
				lblHistory.setText(current+s1.charAt(0));
				history=Double.parseDouble(current);
				op=s1.charAt(0);
			}
			else {
				overlapFlag=true;
				double ans=solve(history,Double.parseDouble(current),op);
				history=ans;
				lblHistory.setText(lblHistory.getText()+current+s1.charAt(0));
				lblResult.setText(filter(ans+""));
				op=s1.charAt(0);
			}
		}
		else if(s1.equals("=")) {
			if(op=='\u00F7' && currentN==0) {
				lblResult.setText("Cannot divide by zero");
			}
			else {
				double ans=solve(history,currentN,op);
				lblResult.setText(filter(ans+""));
			}
			history=0;
			op='\u0000';
			lblHistory.setText("");
			overlapFlag=true;
		}
		else if(s1.equals("\u2213")) { 		//+/-
			lblResult.setText(filter(currentN*-1+""));
			overlapFlag=true;
		}
		else if(s1.equals("\u232b")) {		//backspace
			lblResult.setText(filter(current.substring(0,current.length()-1)));
		}
		else if(s1.equals(" 1/x")) {
			lblResult.setText(filter(1/currentN+""));
			overlapFlag=true;
		}
		else if(s1.equals("X\u00B2")) {
			lblResult.setText(filter(currentN*currentN+""));
			overlapFlag=true;
		}
		else if(s1.equals("\u221A")) {		//sqrt
			lblResult.setText(filter(Math.sqrt(currentN)+""));
			overlapFlag=true;
		}
		else if(s1.equals("C")) {
			lblResult.setText("0");
			lblHistory.setText("");
			history=0;
			op='\u0000';
			currentN=0;
			overlapFlag=false;
		}
		else if(s1.equals("CE")) {
			lblResult.setText("");
		}
		else if(s1.equals("%")) {
			double ans=history*currentN/100;
			lblResult.setText(filter(ans+""));
			lblHistory.setText(lblHistory.getText()+filter(ans+""));
		}
		else if(s1.equals(".")) {
			if(current.indexOf(".")==-1)
				lblResult.setText(current+".");
		}
		else if(s1.equals("MS")) {
			memory=currentN;
		}
		else if(s1.equals("MC")) {
			memory=0;
		}
		else if(s1.equals("MR")) {
			lblResult.setText(filter(memory+""));
		}
		else if(s1.equals("M+")) {
			memory+=currentN;
		}
		else if(s1.equals("M-")) {
			memory-=currentN;
		}		
	}
	boolean isOperator(char ch) {
		if(ch=='+' || ch=='-' || ch=='\u00D7' || ch=='\u00F7')
			return true;
		else
			return false;
	}
	String filter(String s1) {
		if(s1.equals(""))
			return "0";
		double n=Double.parseDouble(s1);
		if(n==(int)n)
			return (int)n+"";
		else
			return n+"";
	}
	double solve(double a,double b,char op) {
		switch(op) {
			case '+':return a+b;
			case '-':return a-b;
			case '\u00D7':return a*b;
			case '\u00F7':return a/b;
			default:return 0;
		}
	}
}