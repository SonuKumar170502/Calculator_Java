package MyCalcy;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
public class Calculator extends JFrame implements ActionListener {
	JTextField txtResult;
	JPanel panBottom;
	String strLabels[]= {"%","CE","C","\u232b"," 1/x","x2","sqrt","/","7","8","9","*","4","5","6","-","1","2","3","+","\u2213","0",".","="};
	JButton btn[]=new JButton[24];
	boolean concat=true;
	double old=0.0;
	char op='\u0000';
	Calculator() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		txtResult=new JTextField("0");
		txtResult.setEditable(false);
		txtResult.setFont(new Font("arial",Font.PLAIN,40));
		txtResult.setHorizontalAlignment(SwingConstants.RIGHT);
		add(txtResult,"North");
		panBottom=new JPanel();
		panBottom.setLayout(new GridLayout(6,4,0,0));
		for(int i=0;i<btn.length;i++) {
			btn[i]=new JButton(strLabels[i]);
			btn[i].addActionListener(this);
			//btn[i].setBorderPainted(false);
			//btn[i].setContentAreaFilled(false);
			if(strLabels[i].equals("="))
				btn[i].setBackground(Color.blue);
			else if(Character.isDigit(strLabels[i].charAt(0)))
				btn[i].setBackground(Color.white);
			else
				btn[i].setBackground(new Color(240,240,240));
			btn[i].addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent me) {
					JButton btn=(JButton)me.getSource();
					btn.setBackground(Color.DARK_GRAY);
				}
				public void mouseExited(MouseEvent me) {
					JButton btn=(JButton)me.getSource();
					if(btn.getText().equals("="))
						btn.setBackground(Color.blue);
					else if(Character.isDigit(btn.getText().charAt(0)))
						btn.setBackground(Color.white);
					else
						btn.setBackground(new Color(240,240,240));
				}
			});
			btn[i].setBorder(BorderFactory.createLineBorder(Color.lightGray));
			btn[i].setFont(new Font(Font.SANS_SERIF,Font.PLAIN,20));
			panBottom.add(btn[i]);
		}
		add(panBottom);
		setSize(340,550);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		JButton b1=(JButton)ae.getSource();
		String s1=b1.getText();
		String current=txtResult.getText();
		if(Character.isDigit(s1.charAt(0))) {//0 to 9
			if(concat==true)
				txtResult.setText(filter(current+s1.charAt(0)));//concat
			else {
				txtResult.setText(filter(s1));//overlap
				concat=true;
			}
		}
		else if(isOperator(s1.charAt(0))) {//+ - * /
			if(op!='\u0000') {
				double n=Double.parseDouble(current);
				if(op=='+')
					old=old+n;
				else if(op=='-')
					old=old-n;
				else if(op=='*')
					old=old*n;
				else if(op=='/')
					old=old/n;
				txtResult.setText(filter(old+""));
				current=txtResult.getText();
			}
			concat=false;
			op=s1.charAt(0);
			old=Double.parseDouble(current);
		}
		else if(s1.equals("=")) {//=
			double n=Double.parseDouble(current);
			if(op=='+')
				txtResult.setText(filter(old+n+""));
			else if(op=='-')
				txtResult.setText(filter(old-n+""));
			else if(op=='*')
				txtResult.setText(filter(old*n+""));
			else if(op=='/')
				txtResult.setText(filter(old/n+""));
		}
		else if(s1.equals("%")) {//=
			
		}
		else if(s1.equals("CE")) {//=
			txtResult.setText("0");
		}
		else if(s1.equals("C")) {//=
			txtResult.setText("0");
			old=0;
			op='\u0000';
		}
		else if(s1.equals("\u232b")) {//=
			txtResult.setText(filter(current.substring(0,current.length()-1)));
		}
		else if(s1.equals(" 1/x")) {//=
			txtResult.setText(filter(1/Double.parseDouble(current)+""));
		}
		else if(s1.equals("x2")) {//=
			txtResult.setText(filter(Double.parseDouble(current)*Double.parseDouble(current)+""));
		}
		else if(s1.equals("sqrt")) {//=
			txtResult.setText(filter(Math.sqrt(Double.parseDouble(current))+""));
		}
		else if(s1.equals("\u2213")) {//+/-
			txtResult.setText(filter(-Double.parseDouble(current)+""));
		}
		else if(s1.equals(".")) {
			if(s1.indexOf('.')==-1)
				txtResult.setText(current+".");
		}		
	}
	public static void main(String[] args) {
		new Calculator();
	}
	boolean isOperator(char ch) {
		if(ch=='+' || ch=='-' || ch=='*' ||ch=='/')
			return true;
		return false;
	}
	String filter(String value) {
		if(value.isEmpty())
			return "0";
		double n=Double.parseDouble(value);
		if(n==(int)n)
			return (int)n+"";
		else
			return n+"";
	}
}