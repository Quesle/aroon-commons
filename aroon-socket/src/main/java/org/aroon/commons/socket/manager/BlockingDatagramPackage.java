package org.aroon.commons.socket.manager;

import java.net.DatagramPacket;
import java.util.Date;

public class BlockingDatagramPackage {
	private DatagramPacket datagramPacket;
	private Date receiveDate;
	
	public BlockingDatagramPackage(){
		receiveDate = new Date();
	}
	
	public Date getReceiveDate(){
		return receiveDate;
	}
	
	public DatagramPacket getDatagramPacket() {
		return datagramPacket;
	}
	public void setDatagramPacket(DatagramPacket datagramPacket) {
		this.datagramPacket = datagramPacket;
	}
}
