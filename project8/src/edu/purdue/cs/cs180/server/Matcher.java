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
		}
	}
}
