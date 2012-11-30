package edu.purdue.cs.cs180.server;

import java.util.ArrayList;

import edu.purdue.cs.cs180.channel.Channel;
import edu.purdue.cs.cs180.channel.ChannelException;
import edu.purdue.cs.cs180.channel.MessageListener;
import edu.purdue.cs.cs180.channel.TCPChannel;
import edu.purdue.cs.cs180.common.Message;

/**
 * The server class, a console application, that receives messages from Requests
 * and Responses, regardless of their location and sends replies accordingly.
 * 
 * The protocol as follow: <tt>When it receives a
 * Request: 
 * 	When there are no volunteers available, it replies with a message
 *   that says Searching: 
 * 	When there is a volunteer available, it will send 2
 *   messages, the first to the volunteer as follows: Assigned:The location to go
 *   to, and the second to the student requesting the ride as follows:
 *   Assigned:Help Team X 
 * 
 * Similarly, when it receives a Response message: 
 * 	When there are no students requesting rides, it replies with a message that says
 *   Searching: 
 *  When there is a student requesting a ride, it will send 2
 *   messages, the first to the volunteer as follows: Assigned:The location to go
 *   to, and the second to the student requesting the ride as follows:
 *   Assigned:Help Team X
 * </tt>
 * 
 * @author fmeawad
 * 
 */
public class Server implements MessageListener {

	/**
	 * The server channel.
	 */
	private Channel channel = null;

	/**
	 * The server constructor only needs a port.
	 * 
	 * @param port
	 */
	public Server(int port, String matchtype, long sleep) {
		channel = new TCPChannel(port);
		channel.setMessageListener(this);
		(new Matcher(feeder, sleep, matchtype, channel)).start();
	}

	/**
	 * Maintains the pending requests.
	 */
	private ArrayList<Message> pendingRequesters = new ArrayList<Message>();
	/**
	 * Maintains the pending responses.
	 */
	private ArrayList<Message> pendingResponders = new ArrayList<Message>();

	private DataFeeder feeder = new DataFeeder() {
			public Message getFirstRequest() {
				synchronized (pendingRequesters) {
					return pendingRequesters.get(0);
				}
			}
			public Message getFirstResponse() {
				synchronized (pendingResponders) {
					return pendingResponders.get(0);
				}
			}
			public Message getLastRequest() {
				synchronized (pendingRequesters) {
					return pendingRequesters.get(pendingRequesters.size()-1);
				}
			}
			public Message getLastResponse() {
				synchronized (pendingResponders) {
					return pendingResponders.get(pendingResponders.size()-1);
				}
			}
			public boolean hasNextRequest() {
				synchronized (pendingRequesters) {
					return !pendingRequesters.isEmpty();
				}
			}
			public boolean hasNextResponse() {
				synchronized (pendingResponders) {
					return !pendingResponders.isEmpty();
				}
			}
			public void removeFirstRequest() {
				synchronized (pendingRequesters) {
					pendingRequesters.remove(0);
				}
			}
			public void removeFirstResponse() {
				synchronized (pendingResponders) {
					pendingResponders.remove(0);
				}
			}
			public void removeLastRequest() {
				synchronized (pendingRequesters) {
					pendingRequesters.remove(pendingRequesters.size()-1);
				}
			}
			public void removeLastResponse() {
				synchronized (pendingResponders) {
					pendingResponders.remove(pendingResponders.size()-1);
				}
			}
		};

	/**
	 * Handle messages received.
	 */
	@Override
	public void messageReceived(String messageString, int clientID) {
		assert (messageString != null);
		System.out.println(clientID + ": " + messageString); // For debugging
		                                                     // only, not
		                                                     // required.
		Message message = new Message(messageString, clientID);
		switch (message.getType()) {
		case Request:
			synchronized (pendingRequesters) {
				pendingRequesters.add(message);
			}
			try {
				channel.sendMessage(new Message(
						Message.Type.Searching,
						"",
						message.getClientID()).toString(),
						message.getClientID());
			} catch (ChannelException e) {
				e.printStackTrace();
				System.exit(1);
			}
			break;
		case Response:
			synchronized (pendingResponders) {
				pendingResponders.add(message);
			}
			try {
				channel.sendMessage(new Message(
						Message.Type.Searching,
						"",
						message.getClientID()).toString(),
						message.getClientID());
			} catch (ChannelException e) {
				e.printStackTrace();
				System.exit(1);
			}
			break;
		default:
			System.err.println("Unexpected message of type "
					+ message.getType());
			break;
		}
	}

	/**
	 * The server expects a port number as an argument.
	 * 
	 * @param args Expects a port number.
	 */
	public static void main(String[] args) {		
		new Server(Integer.parseInt(args[0]), args[1], Long.parseLong(args[2]));
	}
 }
