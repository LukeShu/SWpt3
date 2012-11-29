package edu.purdue.cs.cs180.server;

public interface DataFeeder {

	public Message getFirstRequest();
	public Message getFirstResponse();
	public Message getLastRequest();
	public Message getLastResponse();
	public boolean hasNextRequest();
	public boolean hasNextResponse();
	public void removeFirstRequest();
	public void removeFirstResponse();
	public void removeLastRequest();
	public void removeLastResponse();
}