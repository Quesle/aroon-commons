package org.aroon.commons.socket.manager;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ListeningPointImpl implements ListeningPoint{
	private int localPort;
	private String localAddress;
	private String transport;
	
	private int peerPort;
	private String peerAddress;
	
	public ListeningPointImpl(String ipAddress, int port, String transport){
		this.localPort = port;
		this.localAddress = ipAddress;
		this.transport = transport.toUpperCase();
	}
	@Override
	public String getTransport() {
		return transport;
	}
	@Override
	public InetAddress getLocalInetAddress() {
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(this.localAddress);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return inetAddress;
	}

	@Override
	public int getLocalPort() {
		return localPort;
	}

	@Override
	public String getLocalAddress() {
		return localAddress;
	}

	@Override
	public int getPeerPort() {
		return peerPort;
	}
	
	
	@Override
	public void setPeerPort(int port) throws Exception {
		if(!isBind){
			this.peerPort = port;
		}else{
			if(!(peerPort == port)){
				throw new Exception("Have bind one port, can't set other port.");
			}
		}
	}
	
	private boolean isBind = false;
	
	@Override
	public void setBindPort(int port) throws Exception {
		if(!isBind){
			isBind = true;
			peerPort = port;
		}else{
			throw new Exception("Have bind one port, can't do this again.");
		}
	}

	@Override
	public String getPeerAddress() {
		return peerAddress;
	}
	
	@Override
	public void setPeerAddress(String peerAddress){
		this.peerAddress = peerAddress;
	}
	
	/**
	 * Get the key of local listening point.
	 * Example : LocalListeningPoint:<localhost:listeningPort>
	 * @return
	 */
	@Override
	public String getLocalListeningPointName() {
		return "LocalListeningPoint:<"+localAddress+":"+localPort+">";
	}
	
	/**
	 * Get the key of remote listening point.
	 * Example : PeerListeningPoint:<localhsot:listeningPort:peerPort>
	 * @return
	 */
	@Override
	public String getPeerListeningPointName() {
		if(peerPort == 0){
			return getLocalListeningPointName();
		}
		return "PeerListeningPoint:<"+localAddress+":"+localPort+":"+peerPort+">";
	}
}
