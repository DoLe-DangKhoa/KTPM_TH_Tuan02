/*
 * Đỗ Lê Đăng Khoa - 18053791
 */
package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.BasicConfigurator;

public class GuiReceiver extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiReceiver frame = new GuiReceiver();
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
	public GuiReceiver() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 775, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Nhan Duoc Tin Nhan");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblNewLabel.setBounds(132, 10, 400, 52);
		contentPane.add(lblNewLabel);

		textPane = new JTextPane();
		textPane.setBounds(20, 61, 694, 422);
		contentPane.add(textPane);
		textPane.setText("Khoa was listened on queue... \n" );
		try {
			Receiver();		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void Receiver()throws Exception {

		//thiết lập môi trường cho JMS
		BasicConfigurator.configure();
		//thiết lập môi trường cho JJNDI
		Properties settings=new Properties();
		settings.setProperty(Context.INITIAL_CONTEXT_FACTORY,
				"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
		//tạo context
		Context ctx=new InitialContext(settings);
		//lookup JMS connection factory
		Object obj=ctx.lookup("ConnectionFactory");
		ConnectionFactory factory=(ConnectionFactory)obj;
		//lookup destination
		Destination destination
		=(Destination) ctx.lookup("dynamicQueues/doledangkhoa");
		//tạo connection
		Connection con=factory.createConnection("admin","admin");
		//nối đến MOM
		con.start();
		//tạo session
		Session session=con.createSession(
				/*transaction*/false,
				/*ACK*/Session.CLIENT_ACKNOWLEDGE
				);
		//tạo consumer
		MessageConsumer receiver = session.createConsumer(destination);
		//blocked-method for receiving message - sync
		//receiver.receive();
		//Cho receiver lắng nghe trên queue, chừng có message thì notify - async
		System.out.println("Khoa was listened on queue...");
		receiver.setMessageListener(new MessageListener() {
			@Override
			//có message đến queue, phương thức này được thực thi
			public void onMessage(Message msg) {//msg là message nhận được
				try {
					if(msg instanceof TextMessage){

						TextMessage tm=(TextMessage)msg;
						textPane.setText("Message nhận được: "+tm.getText() );	
						msg.acknowledge();//gửi tín hiệu ack
					}
					else if(msg instanceof ObjectMessage){
						ObjectMessage om=(ObjectMessage)msg;
						textPane.setText("Message nhận được: "+om );
						System.out.println(om);
					}
					//others message type....
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

