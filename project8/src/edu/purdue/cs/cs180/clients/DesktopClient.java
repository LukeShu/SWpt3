package edu.purdue.cs.cs180.clients;

import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import edu.purdue.cs.cs180.channel.ChannelException;
import edu.purdue.cs.cs180.channel.TCPChannel;

/**
 * An abstract class for all Desktop clients.
 * 
 * @author fmeawad
 * 
 */
public abstract class DesktopClient extends Client implements ActionListener {

	/**
	 * A desktop client uses a JFrame.
	 */
	protected JFrame jframe;

	/**
	 * Each client has a unique id, the id will be set after the channel is
	 * initialized.
	 */
	protected int clientID = -1;

	/**
	 * Constructor, all desktop clients need a server to connect to as well as a
	 * port.
	 * 
	 * @param hostname
	 * @param port
	 * @throws ChannelException
	 */
	public DesktopClient(String hostname, int port) throws ChannelException {
		// Create a client connection.
		super(new TCPChannel(hostname, port));
		// and set the message listener.
		this.channel.setMessageListener(this);
		// initialize the JFrame.
		this.jframe = new JFrame();
		// and make sure that the program exit when the client is closed.
		this.jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// assign the client id to be the channel ID.
		this.clientID = this.channel.getID();

		// The purpose of adding this window listener is to be able to detect
		// when the Desktop Client is "closing" and close the channel.
		// We create an Anonymous class here since it is not going to reused
		// anywhere else.
		jframe.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				// Nothing to be done.
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// Nothing to be done.
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// Nothing to be done.
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				// Nothing to be done.
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// when the window is closing, close the channel.
				try {
					channel.close();
				} catch (ChannelException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// Nothing to be done.
			}

			@Override
			public void windowActivated(WindowEvent arg0) {
				// Nothing to be done.
			}
		});
	}

}
