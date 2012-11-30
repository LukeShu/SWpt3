package edu.purdue.cs.cs180.server;

import edu.purdue.cs.cs180.channel.Channel;
import edu.purdue.cs.cs180.common.Message;

public class Matcher extends Thread {
	
	private long sleepTime;
	private DataFeeder feeder;
	private String matchingType;
	private Channel channel;
	public Matcher(DataFeeder f, long sleep, String matchingType, Channel channel) {
		feeder = f;
		sleepTime = sleep;
		this.matchingType = matchingType;
		this.channel = channel;
	}
	
	@Override
	public void run() {
		while (true) {
			//
			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {}

			if ((feeder.hasNextRequest() && feeder.hasNextResponse()))
				Message responderMessage, requesterMessage;
 				if (matchingType.equals("FCFS")) {
					responderMessage = feeder.getFirstResponse();
					requesterMessage = feeder.getFirstRequest();
					feeder.removeFirstResponse();
					feeder.removeFirstRequest();

				} else {
					responderMessage = feeder.getLastResponse();
					requesterMessage = feeder.getLastRequest();
					feeder.removeLastResponse();
					feeder.removeLastRequest();

				}
				
			try {
				channel.sendMessage(
					new Message(Message.Type.Assigned,
				  		requesterMessage.getInfo(),
						responderMessage.getClientID()).toString(),
					responderMessage.getClientID());

				channel.sendMessage(
					new Message(Message.Type.Assigned,
						responderMessage.getInfo(),
						requesterMessage.getClientID()).toString(),
					requesterMessage.getClientID());

			} 
			catch (ChannelException e) {
				e.printStackTrace();
				System.exit(1);
			}

			else {

			}
				
		}	

	}
	
}

