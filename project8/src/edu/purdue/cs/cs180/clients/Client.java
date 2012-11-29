package edu.purdue.cs.cs180.clients;

import edu.purdue.cs.cs180.channel.Channel;
import edu.purdue.cs.cs180.channel.MessageListener;

/**
 * An abstract class that represents any client of the SafeWalk application,
 * whether it is a Desktop or a mobile one.
 * 
 * @author fmeawad
 * 
 */
public abstract class Client implements MessageListener {

	/**
	 * All clients should have a channel.
	 */
	protected Channel channel;

	/**
	 * Constructor. Takes the channel, and sets this client as the message
	 * listener.
	 * 
	 * @param channel
	 */
	public Client(Channel channel) {
		assert (channel != null);
		this.channel = channel;
		this.channel.setMessageListener(this);
	}
}
