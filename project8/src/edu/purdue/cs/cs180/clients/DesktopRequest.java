package edu.purdue.cs.cs180.clients;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import edu.purdue.cs.cs180.channel.ChannelException;
import edu.purdue.cs.cs180.common.Message;

/**
 * DesktopRequest, should handle Request specific GUI and message handling.
 * 
 * @author fmeawad
 * 
 */
public class DesktopRequest extends DesktopClient {

	// the locations.
	private JComboBox locationComboBox;
	// the submit button.
	private JButton submitButton;

	// the status information.
	private JLabel statusLabel;

	// maintain the locations list.
	// TODO: it should be moved to a better place for easy update.
	private static String[] locations = { "CL50 - Class of 1950 Lecture Hall",
			"EE - Electrical Engineering Building",
			"LWSN - Lawson Computer Science Building",
			"PMU - Purdue Memorial Union",
			"PUSH - Purdue University Student Health Center" };

	/**
	 * Constructor, needs the hostname and the port of the server to connect to.
	 * 
	 * @param hostname
	 * @param port
	 * @throws ChannelException
	 */
	public DesktopRequest(String hostname, int port) throws ChannelException {
		super(hostname, port);
		drawGUI();
	}

	/**
	 * GUI initialization.
	 */
	private void drawGUI() {
		this.jframe.setSize(400, 300);
		this.jframe.setTitle("Request: " + this.clientID);
		this.jframe.setLocation(0, this.clientID * 400);
		Container contentPane = this.jframe.getContentPane();
		contentPane.setLayout(new FlowLayout());

		locationComboBox = new JComboBox(locations);

		contentPane.add(new JLabel("Pick up location:"));
		contentPane.add(locationComboBox);
		submitButton = new JButton("Submit");
		submitButton.addActionListener(this);
		contentPane.add(submitButton);
		statusLabel = new JLabel();
		contentPane.add(statusLabel);
		this.jframe.setVisible(true);

	}

	/**
	 * JFrame actionPerformed handler.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// make sure it is coming from the button, since it is the only UI item
		// that can trigger action performed at the moment.
		assert (e.getSource() == submitButton);
		locationComboBox.setEnabled(false);
		submitButton.setEnabled(false);
		statusLabel.setText("Sending Request"); // not really needed by the
												// requirement, but does not
												// harm as it does not change
												// any of the expected results,
												// also it is only visible if no
												// reply received from the
												// server in a timely manner.
		try {
			// send a message.
			this.channel.sendMessage(new Message(Message.Type.Request,
					(String) locationComboBox.getSelectedItem(), this.clientID)
					.toString());
		} catch (ChannelException ex) {
			ex.printStackTrace();
			System.exit(1);
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
		// check its type, and perform appropriate action.
		switch (message.getType()) {
		case Assigned:
			statusLabel.setText("Assigned: " + message.getInfo());
			locationComboBox.setEnabled(true);
			submitButton.setEnabled(true);
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
			new DesktopRequest(args[0], Integer.parseInt(args[1]));
		} catch (ChannelException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
}
