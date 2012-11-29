package edu.purdue.cs.cs180.clients;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JLabel;

import edu.purdue.cs.cs180.channel.ChannelException;
import edu.purdue.cs.cs180.common.Message;

/**
 * DesktopResponse, should handle Response specific GUI and message handling
 * 
 * @author fmeawad
 * 
 */
public class DesktopResponse extends DesktopClient {
	// the ready button.
	private JButton readyButton;
	// the label.
	private JLabel statusLabel;

	/**
	 * Constructor expects a hostname and a port number.
	 * 
	 * @param hostname
	 * @param port
	 * @throws ChannelException
	 */
	public DesktopResponse(String hostname, int port) throws ChannelException {
		super(hostname, port);
		drawGUI();
	}

	/**
	 * GUI initialization.
	 */
	private void drawGUI() {
		this.jframe.setSize(400, 300);
		this.jframe.setTitle("Responder: " + this.clientID);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Dimension dim = toolkit.getScreenSize();

		this.jframe.setLocation(dim.width - 300, this.clientID * 400);
		Container contentPane = this.jframe.getContentPane();
		contentPane.setLayout(new FlowLayout());

		readyButton = new JButton("Ready");
		readyButton.addActionListener(this);
		contentPane.add(readyButton);
		statusLabel = new JLabel();
		contentPane.add(statusLabel);

		this.jframe.setVisible(true);
	}

	/**
	 * JFrame actionPerformed handler.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		assert (e.getSource() == readyButton);

		readyButton.setEnabled(false);
		statusLabel.setText("Sending Request");
		try {
			this.channel.sendMessage(new Message(Message.Type.Response,
					"Help Team " + this.clientID, this.clientID).toString());
		} catch (ChannelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * When a message is received.
	 */
	@Override
	public void messageReceived(String messageString, int clientID) {
		assert (messageString != null);
		// create a message object.
		Message message = new Message(messageString, clientID);
		// Act according to the message type.
		switch (message.getType()) {
		case Assigned:
			statusLabel.setText("Assigned: " + message.getInfo());
			readyButton.setEnabled(true);
			break;
		case Searching:
			statusLabel.setText("Searching");
			break;
		default:
			System.err.println("Unexpected message of type "
					+ message.getType());
			break;
		}
	}

	/**
	 * The clients expects a hostname where the server is located as well as a
	 * port number as an argument.
	 * 
	 * @param args
	 *            Expects a hostname and port number.
	 */
	public static void main(String[] args) {
		try {
			new DesktopResponse(args[0], Integer.parseInt(args[1]));
		} catch (ChannelException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
