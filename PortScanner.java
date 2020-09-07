import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.lang.*;
import javax.swing.*;
import javax.swing.event.*;
public class PortScanner extends JFrame implements ActionListener, ChangeListener {
	//Final variables
		private static final long serialVersionUID = 2884600754343147821L;
		private static final int WIDTH = 250;
		private static final int HEIGHT = 400;

		//Flags
		private boolean displayAll = false;

		//Compoents
		private JTextField ipAddress, lowerPort, higherPort;
		private JTextArea output;
		private JScrollPane outputScroller;
		private JCheckBox toggleDisplayAll;
		private JButton scanPorts;
		private JPanel settingsPanel, outputPanel;

		
		public PortScanner() {
			super( "Port Scanner" );

			initComponents();

			super.setLayout( new FlowLayout() );
			super.setSize( WIDTH, HEIGHT );
			super.setLocationRelativeTo( null );
			super.setResizable( false );
			super.setVisible( true );
			super.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		}

		
		private final void initComponents() {
			//Text fields
			this.ipAddress = new JTextField( 12 );
			this.lowerPort = new JTextField( 5 );
			this.higherPort = new JTextField( 5 );

			//TextArea & ScrollPane
			this.output = new JTextArea( 10, 20 );
			this.output.setEditable( false );
			this.output.setLineWrap( true );
			this.outputScroller = new JScrollPane( this.output );

			//Check box
			this.toggleDisplayAll = new JCheckBox( "Display all results (open & closed)" );
			this.toggleDisplayAll.addChangeListener( this );

			//Buttons
			this.scanPorts = new JButton( "Scan" );
			this.scanPorts.addActionListener( this );

			//JPanels
			this.settingsPanel = new JPanel( new FlowLayout() );
			this.settingsPanel.setBorder( BorderFactory.createTitledBorder( "Scan information" ) );
			this.settingsPanel.setPreferredSize( new Dimension( 230, 135 ) );
			this.settingsPanel.add( new JLabel( "IP Address: " ) );
			this.settingsPanel.add( this.ipAddress );
			this.settingsPanel.add( new JLabel( "Port range: " ) );
			this.settingsPanel.add( this.lowerPort );
			this.settingsPanel.add( new JLabel( "-" ) );
			this.settingsPanel.add( this.higherPort );
			this.settingsPanel.add( this.toggleDisplayAll );
			this.settingsPanel.add( this.scanPorts );

			this.outputPanel = new JPanel( new FlowLayout() );
			this.outputPanel.setBorder( BorderFactory.createTitledBorder( "Results: " ) );
			this.outputPanel.add( outputScroller );	

			//add components
			super.add( this.settingsPanel );
			super.add( this.outputPanel );
		}

		public void actionPerformed(ActionEvent ae ) {
			if( ae.getSource() == this.scanPorts ) {
				this.output.setText("Starting scan..." + System.lineSeparator() );
				scan( this.ipAddress.getText(), this.lowerPort.getText(), this.higherPort.getText());
				this.output.append( "Scan finished." );
			}
		}

		
		public void stateChanged(ChangeEvent ce) {
			if( ce.getSource() == toggleDisplayAll ) {
				this.displayAll = this.toggleDisplayAll.isSelected();
			}

		}

		
		private final void scan( String ipAddress, String lowPort, String highPort) {
			int start, end;

			//verify port numbers
			try {
				start = Integer.parseInt( lowPort );
				end = Integer.parseInt( highPort );

				if( end <= start ) {
					this.output.append( "The second port must be higher than the first port" + System.lineSeparator() );
					return;
				}
			}
			catch( NumberFormatException nfe ) {
				this.output.append( "Please enter valid port numbers." + System.lineSeparator() );
				return;
			}

			//Scan ports in range
			for( int current = start; current <= end; current++ ) {
				try {
					Socket s = new Socket(ipAddress,current);
					//s.connect( new InetSocketAddress( ipAddress, current )); //attempt a connection
					//s.close();
					try{ 
						Class.forName("com.mysql.cj.jdbc.Driver");
						Connection con=DriverManager.getConnection(
						"jdbc:mysql://localhost:3306/port1","root","zeronsec@123");
						Statement stmt=con.createStatement();
						stmt.executeUpdate("INSERT INTO xyz1"+" VALUES("+current+",'"+ipAddress+"');");
						ResultSet rs=stmt.executeQuery("SELECT * FROM abc");
						while(rs.next())  
						System.out.println(rs.getInt(1)+"  "+rs.getString(2));  
						con.close();  
						}catch(Exception e){ System.out.println(e);}  
					this.output.append( "Open port: " + current + System.lineSeparator() );
				}
				catch( IOException ioe ) { //connection failed
					if( this.displayAll ) {
						this.output.append( "Closed port: " + current + System.lineSeparator() );
					}
				}
			}
		}

		
		public static void main( String[] args ) {
			@SuppressWarnings("unused")
			PortScanner psg = new PortScanner();
		}
}
