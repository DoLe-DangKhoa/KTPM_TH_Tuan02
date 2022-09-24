/*
 * Đỗ Lê Đăng Khoa - 18053791
 */
package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.BasicConfigurator;

import data.Person;
import helper.XMLConvert;

public class GuiSender extends JFrame  implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtMaSo;
	private JButton btnSen;
	private JTextField txtHoTen;
	private JTextField txtNgaySinh;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiSender frame = new GuiSender();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GuiSender() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 407);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Mssv:");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblNewLabel.setBounds(20, 10, 88, 38);
		contentPane.add(lblNewLabel);
		
		txtMaSo = new JTextField();
		txtMaSo.setFont(new Font("Times New Roman", Font.BOLD, 24));
		txtMaSo.setBounds(151, 10, 270, 38);
		contentPane.add(txtMaSo);
		txtMaSo.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Ho ten: ");
		lblNewLabel_1.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblNewLabel_1.setBounds(20, 92, 88, 38);
		contentPane.add(lblNewLabel_1);

		txtHoTen = new JTextField();
		txtHoTen.setFont(new Font("Times New Roman", Font.BOLD, 24));
		txtHoTen.setBounds(151, 92, 270, 33);
		contentPane.add(txtHoTen);
		txtHoTen.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Ngay sinh:");
		lblNewLabel_2.setFont(new Font("Times New Roman", Font.BOLD, 24));
		lblNewLabel_2.setBounds(20, 184, 116, 38);
		contentPane.add(lblNewLabel_2);

		txtNgaySinh = new JTextField();
		txtNgaySinh.setFont(new Font("Times New Roman", Font.BOLD, 24));
		txtNgaySinh.setBounds(151, 184, 250, 38);
		contentPane.add(txtNgaySinh);
		txtNgaySinh.setColumns(10);

		btnSen = new JButton("Send Message");
		btnSen.setFont(new Font("Times New Roman", Font.BOLD, 24));
		btnSen.setBounds(10, 299, 200, 38);
		contentPane.add(btnSen);
		
		btnSen.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object object = e.getSource();
		if (object.equals(btnSen)) {
			try {
				send();

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();


			}
		}

	}

	private void send()  throws Exception {
		long maSo = Long.parseLong(txtMaSo.getText().trim());
		String name = txtHoTen.getText().trim();
		LocalDate ns = LocalDate.parse(txtNgaySinh.getText());

		BasicConfigurator.configure();

		Properties settings = new Properties();
		settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

		Context ctx = new InitialContext(settings);

		ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");

		Destination destination = (Destination) ctx.lookup("dynamicQueues/doledangkhoa");

		Connection con = factory.createConnection("admin", "admin");

		con.start();

		Session session = con.createSession(/* transaction */false, /* ACK */Session.AUTO_ACKNOWLEDGE);

		MessageProducer producer = session.createProducer(destination);

		Message msg = session.createTextMessage("alo alo, this mesage from ActiveMQ");
		producer.send(msg);

		Person person = new Person(maSo, name, ns);
//		String xml = new XMLConvert<Person>(person).object2XML(person);
//		TextMessage msg1 = session.createTextMessage(xml);
//		producer.send(msg1);

		session.close();
		con.close();
		System.out.println("Finished...");

	}
}
