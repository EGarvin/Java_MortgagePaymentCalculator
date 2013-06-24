/*

  Learning Team B:  Eric Garvin, Zachary Dean, Norma Jordan, 
  Daniel   Mcleod, Michael Campbell, and Todd Cabrera 
  Course:  PRG 421 - Java Programming II
  Instructor: Ruth Tucker Bogart Assignment:  Week 5 - Final Project:
  Last update:  Jan 30 2012 

References: 

University of Phoenix. (2010). Big Java, 4th edition. Retrieved from University of Phoenix, PRG/421 - Java Programming II website.

University of Phoenix. (2005). Ivor Horton�s Beginning Java 2TM, JDKTM, Fifth Edition. Retrieved from University of Phoenix, PRG/421 - Java Programming II website.                                         

University of Phoenix. (2012). Ruth Tucker Bogart. For instruction, guidance, and example code.
 
Displays a Java program with a graphical user interface that calculates the mortgage payment amount from user input of the mortgage amount and the user's selection from a menu of available mortgage loans: 

- 7 years at 5.35%
- 15 years at 5.5%
- 30 years at 5.75%

The program includes an array for the mortgage data for the different loans, reads the interest rates to fill the array from a sequential file,  and displays the mortgage payment amount, loan balance, total amount paid, and interest paid for each payment over the term of the loan, and includes a chart graphic of the mortgage information.

*/

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.lang.Throwable;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Component.*;
import javax.swing.border.*;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory.*;




public class MortgagePaymentCalculator extends JFrame implements ActionListener {
	    //Zack, Eric; Labels for application
	    private JLabel label1, label2, label3;
            
	    //Zack, Eric; TextField for application
	    private JTextField amount_txt, term_txt, rate_txt;

	    // JTextArea for application
	    private JTextArea textarea_output ;

	    //Eric, Zack; Buttons for application
	    public JButton resetButton, quitButton, calcButton, chartButton;

	    //menus for application
	    private JMenuBar menuBar ;
            private JMenu fileMenu, loanTypeMenu;
            private JMenuItem newLoan, exit, loanType1, loanType2, loanType3;

            //arrays for storing loan data
            private double rates [];
            private int years [];
            
            //Eric; public variables
            public int loanyear;
            public double loanrate; 
            //Eric; Font style
            Font fontStyle;
            Font amt_txt;
        //chart 
        private LoanChart chart;
        
	    //default constructor
	    public MortgagePaymentCalculator ()
	    {
	        //settign title of frame
	        setTitle("Mortgage Payment Calculator");
	    	//set default layout of frame
	    	setLayout(new BorderLayout());
	    	//default closing action
	    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Zack, Eric; initializing GUI components
		label1 = new JLabel("Enter loan amount:         ");
		amount_txt = new JTextField("200000", 10);              
                label2 = new JLabel("Enter loan term: ");
                term_txt = new JTextField("", 3);
                term_txt.setForeground(Color.BLACK);                
                label3 = new JLabel("Enter loan rate: ");
                rate_txt = new JTextField("");
                rate_txt.setForeground(Color.BLACK);                
                calcButton = new JButton("Calculate");

		//Zack, Eric; amt panel, style, components
		JPanel amtpanel = new JPanel(new GridLayout(5, 3, 5, 5)); //Eric
                amtpanel.setBorder(new TitledBorder(
                        new EtchedBorder(),
                        "Loan Entries"));
                //Row1
                amtpanel.add(Box.createVerticalStrut(1));
                amtpanel.add(Box.createVerticalStrut(1));
                amtpanel.add(Box.createVerticalStrut(1));           
                //Row2
		amtpanel.add(label1);
		amtpanel.add(amount_txt);
                amtpanel.add(Box.createVerticalStrut(1));
                //Row3
                amtpanel.add(label2);   //Eric
                amtpanel.add(term_txt); //Eric
                amtpanel.add(Box.createVerticalStrut(1));
                //Row4
                amtpanel.add(label3);   //Eric
                amtpanel.add(rate_txt); //Eric
		amtpanel.add(calcButton);   //Eric
                //Row5
                amtpanel.add(Box.createVerticalStrut(1));
                amtpanel.add(Box.createVerticalStrut(1));
                amtpanel.add(Box.createVerticalStrut(1));
	       
	    	//Create new panel, set border, and add components
	        JPanel inputPanel = new JPanel();
	        inputPanel.setLayout(new BorderLayout());

	        inputPanel.add(amtpanel, BorderLayout.NORTH);
	        
	        // Window menu bar
	        menuBar = new JMenuBar(); 
	        
	        // Create File menu
	        fileMenu = new JMenu("File");
	        //adding file menu items
	    	newLoan = fileMenu.add("New");
	        fileMenu.addSeparator();
	        exit = fileMenu.add("Exit");

	        // Add the file menu
	        menuBar.add(fileMenu);


	        // Create loan Type menu
	        loanTypeMenu = new JMenu("Select Loan Term");
	        //adding loan menu items
	    	loanType1 = loanTypeMenu.add("7 years at 5.35%");
	        loanTypeMenu.addSeparator();
	        loanType2 = loanTypeMenu.add("15 years at 5.5%");
	        loanTypeMenu.addSeparator();
	        loanType3 = loanTypeMenu.add("30 years at 5.75%");

	        // Add the file loan type menu
	        menuBar.add(loanTypeMenu);

	        //Zack, Eric; adding action listeners to all menu items
	        newLoan.addActionListener(this);
	        exit.addActionListener(this);
	        loanType1.addActionListener(this);
	        loanType2.addActionListener(this);
	        loanType3.addActionListener(this);
                calcButton.addActionListener(this); //Eric
                
	        // Add the menu bar to the main frame
	        setJMenuBar(menuBar);

	        //add the io panel to the frame
	        add(inputPanel, BorderLayout.NORTH);
                
                //Eric, Zack; Create new panel, set border, and add components
	        JPanel resultPanel = new JPanel();
                
                resultPanel.setBorder(new TitledBorder(
                        new EtchedBorder(),
                        "Results"));

	    	textarea_output =  new JTextArea(16, 33);
	        textarea_output.setEditable(false);

	    	JScrollPane scrollpane = new JScrollPane( textarea_output );
	    	//add the table to panel
	    	resultPanel.add(scrollpane);

	    	//adding the resultPanel into the content pane of the frame
	    	add(resultPanel , BorderLayout.CENTER);

	        //Eric, Zack; initializing the buttons
	         chartButton = new JButton("Chart");
	         resetButton = new JButton("Reset");
	         quitButton = new JButton("Exit");

	         chartButton.addActionListener(this);
	         resetButton.addActionListener(this);
	         quitButton.addActionListener(this);

	         //Zack, Eric; create new panel, set border, and add components
                 JPanel buttonPanel = new JPanel();
                 buttonPanel.setBorder(new CompoundBorder(
                         BorderFactory.createLineBorder(Color.black, 1),
                         BorderFactory.createBevelBorder(BevelBorder.RAISED)));
	         buttonPanel.add(chartButton);
	         buttonPanel.add(resetButton);
	         buttonPanel.add(quitButton);

	         //adding the buttonPanel into the content pane of the frame
	    	add( buttonPanel , BorderLayout.SOUTH);

	    	//initializing arrays
	    	rates = new double[3];
	    	years= new int[3]; 
	    	readLoan();  	
	       
	        
	        chart = new LoanChart();
	        
		setSize(500,600);   //Eric enlarged it
                setResizable(false);   //Eric                
	        setVisible(true);
	    }
	    
	    public void actionPerformed(ActionEvent ae) {
	        if (ae.getSource() == chartButton)
	         {	
	        	showChart();          
	         }
	         else
	         
	    	
	    	if (ae.getSource() == resetButton || ae.getSource() == newLoan)
	    	{
	    		resetButtonAction();
	    		
	    	}
	    	else
	    		if(ae.getSource() == quitButton || ae.getSource() == exit)
	    		{
	    			exitButtonAction();
	    		}
	    		else
	    		if(ae.getSource() == loanType1) {
					//calcButtonAction(rates[0], years[0]);
                                        //Eric; Number to string conversion Oracle, http://docs.oracle.com/javase/tutorial/java/data/converting.html
                                        String y = Integer.toString(years[0]);
                                        term_txt.setText(y);
                                        String r = Double.toString(rates[0]);
                                        rate_txt.setText(r);
	                         }
	                         else
	                             if(ae.getSource() == loanType2) {
						//calcButtonAction(rates[1], years[1]);
                                                //Eric; Number to string conversion
                                                String y = Integer.toString(years[1]);
                                                term_txt.setText(y);
                                                String r = Double.toString(rates[1]);
                                                rate_txt.setText(r);                                         
	                             }
	                             else
	                                 if(ae.getSource() == loanType3) {
							//calcButtonAction(rates[2], years[2]);
		                                        //Eric; Number to string conversion
                                                        String y = Integer.toString(years[2]);
                                                        term_txt.setText(y);
                                                        String r = Double.toString(rates[2]);
                                                        rate_txt.setText(r);                                                 
	                                 }
                                                //Eric, Danny, Zack; string conversions and call to calcButtonAction and error handling
                                                else
                                                    if(ae.getSource() == calcButton) {

                                                        try {
                                                        String years = term_txt.getText();
                                                        loanyear = Integer.parseInt(years); 
                                                        System.out.println("Years entered: " + loanyear);
                                                        }catch (NumberFormatException e)
                                                        {
                                                            JOptionPane.showMessageDialog(this, "Enter loan term: please enter only numbers.", "Error: Loan Term", JOptionPane.ERROR_MESSAGE);
                                                        }
                                                       
                                                        try { 
                                                        String apr = rate_txt.getText();
                                                        loanrate = Double.parseDouble(apr);

                                                        System.out.println("Get loan years from text field= " + loanyear + " Get apr= " + loanrate); //successfull
                                                         }catch (NumberFormatException e)
                                                        {
                                                            JOptionPane.showMessageDialog(this, "Enter loan rate: please enter only numbers.", "Error: Loan rate", JOptionPane.ERROR_MESSAGE);
                                                        }

                                                        if(loanyear <= 0) {
                                                            JOptionPane.showMessageDialog(null, "Enter loan term: please enter a number greater than zero.", "Error: Loan Term!", JOptionPane.ERROR_MESSAGE);
                                                            throw new IllegalArgumentException("Invalid years entry");
                                                        }
                                                        if(loanrate <= 0) {
                                                            JOptionPane.showMessageDialog(null, "Enter loan rate: please enter a number greater than zero.", "Error: Loan rate", JOptionPane.ERROR_MESSAGE);
                                                            throw new IllegalArgumentException("Invalid rate entry");
                                                        }
                                                        calcButtonAction(loanrate, loanyear);

                                                    }                                         
	                     
	                 }
	    
	    
	    private void calcButtonAction(double loanrate, int loanyear)
	    {
	    	 double amount = 0.0;
	       	 double rate = loanrate;
	       	 int year = loanyear;
                 amount = Double.parseDouble(amount_txt.getText().trim());                 

	       	//calling the calculate function
	       	double monthly_payment = calculate(amount, year, rate );
	       	 
	       	double monthrate = (loanrate/12.0)/100.0;
	       	int months = loanyear*12;
	       	 
	       	NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
	       	 //setting the result
	       	textarea_output.setText("Payment #\tLoan Balance\t   Amount Paid\t   Intrest Paid");
                textarea_output.setForeground(Color.BLACK);
                
	       	
                
	       	double totalloan = 0;
	       	double totalintrest = 0;
	       	int counter = 1;
	        while (counter <= months)
	        {
	            // calculate interest paid
	            double interest = amount * monthrate;
	            totalintrest = totalintrest + interest;
	            // calculate loan balance paid
	            double loan = monthly_payment - interest;
	            totalloan = totalloan + loan;
	            // calculate left loan amount
	            amount -= loan;

	           // display loan balance and interest paid for each payment
	           textarea_output.append( "\n"+counter+"\t"+currency.format(amount)+"\t   "+currency.format(loan)+"\t   "+currency.format(interest));
	           counter++;
	        }
	        
	        chart.update(totalloan, totalintrest);	        
	       	 	 
	    }
	    
	    private void resetButtonAction()
	    {
	    	//WORK IN PROGRESS(Eric)Reset and format all the input and output fields upon reset: work in progress
                /*Referencing:  Leepoint, http://leepoint.net/notes-java/GUI-appearance/fonts/10font.html
                 *              Dream.In.Code, http://www.dreamincode.net/forums/topic/45662-change-text-color/
                 *              stackoverflow, http://stackoverflow.com/questions/1738966/java-jtextfield-with-input-hint
                */
                amount_txt.setText("200000");
                term_txt.setText(""); 
                //term_txt.setForeground(Color.LIGHT_GRAY);
                rate_txt.setText("");
                //rate_txt.setForeground(Color.LIGHT_GRAY);
                textarea_output.setText("");
	    }
	    
	    private void exitButtonAction()
	    {
	    	int confirm = JOptionPane.showConfirmDialog(this, "Are you sure, want to Exit?", "Exit?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if(confirm  == JOptionPane.YES_OPTION)
                System.exit(0);
            else
                return;
	    }
	    private double calculate(double amount, int years, double rate )
		{	    	
			double monthly_intrest= rate/(12.0*100.0); //monthly interest		
			int payments = years * 12 ; //total number of payments 
			return (amount * monthly_intrest) / (1 - (Math.pow( (1 + monthly_intrest), -payments )) ) ;
		}
	    
	    
	    private void  readLoan()
	    {
	    	try
	    	{
	    		FileReader fin = new FileReader("loans.txt");
	    		Scanner src = new Scanner(fin);
	    		int i = 0;
	    	    while (src.hasNextLine())
	    		{
	    	    	double rate = src.nextDouble();
	    	    	rates[i] = rate;
	    	    	
	    	    	int year = src.nextInt();	
	    	        years[i] =year;
	    	        
	    	        i++;
	    		}
	   
	    	}
	    	catch(Exception e)
	    	{
	    		JOptionPane.showMessageDialog(this, "Error: "+ e.getMessage(), "Error?", JOptionPane.ERROR_MESSAGE);
	       		 return;	    		
	    	}
	    }
	    private void showChart()
	    {
	    	final JDialog chartDialog = new JDialog();
        	chartDialog.setTitle("Mortgage Loan Chart");
        	chartDialog.setAlwaysOnTop(true);
        	chartDialog.setLayout(new BorderLayout());
        	JButton okbutton  = new JButton("OK");
        	okbutton.addActionListener(
        			new ActionListener()
        		    {
        		        public void actionPerformed( ActionEvent e )
        		        {
        		        	chartDialog.dispose();
        		        }
        		    }
        			);
        	
        	JPanel p = new JPanel(new FlowLayout());
        	p.add(okbutton);
        	chartDialog.add(p,BorderLayout.SOUTH);
        	chartDialog.add(chart, BorderLayout.CENTER);
        	chartDialog.setSize(400,400);
        
        	
        	chartDialog.setVisible(true);
        	
	    }
	    
	     // main method
	    public static void main(String[] args)
	    {
	           new MortgagePaymentCalculator (); //changed IMortgagePaymentCalculator to MortgagePaymentCalculator (Todd)
	    }

	
}

//class for chart 
class LoanChart extends JComponent
{
    double loanPaid;
    double interestPaid;
    NumberFormat fmt = NumberFormat.getCurrencyInstance();
    
    //Constructor   
    public LoanChart()
    {
        loanPaid = 0;
        interestPaid = 0;
    }    

    //Update the data
    public void update(double loan, double interest)
    {
        loanPaid = loan;
        interestPaid = interest;
        repaint();
    }

    //Paint method
    public void paint(Graphics g)
    {
        if (loanPaid == 0)
            return;
        
        Graphics2D g2D = (Graphics2D)g; // Get a Java 2D device context
        double total = loanPaid + interestPaid;
        double loanPer = loanPaid / total;
        double interestPer = 1.0 - loanPer;
        
        g2D.setColor(Color.BLACK);
        g2D.drawOval(74, 74, 222, 222);
        g2D.drawString(String.format("Balance Paid: %s (%.2f%%)", fmt.format(loanPaid), loanPer * 100), 90, 20);
        g2D.drawString(String.format("Interest Paid: %s (%.2f%%)", fmt.format(interestPaid), interestPer * 100), 90, 40);
        g2D.drawString(String.format("Total Paid: %s ", fmt.format(total)), 90, 60);
        
        g2D.setColor(Color.GREEN);
        g2D.fillRect(70, 10, 10, 10);
        g2D.fillArc(75, 75, 220, 220, 0, 360);            
        
        g2D.setColor(Color.RED);
        g2D.fillRect(70, 30, 10, 10); 
        g2D.fillArc(75, 75, 220, 220, 0, (int)(360 * interestPer));
       
    }

}


 