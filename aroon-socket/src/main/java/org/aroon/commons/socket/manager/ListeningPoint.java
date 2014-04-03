package org.aroon.commons.socket.manager;

import java.net.InetAddress;

public interface ListeningPoint {
	
	/**
	 * Get local listening port.
	 * @return local listening port.
	 */
	public int getLocalPort();
	
	/**
	 * Get local host address.
	 * @return local host address.
	 */
	public String getLocalAddress();
	
	/**
	 * Get the remote accept port.
	 * @return the remote accept port
	 */
	public int getPeerPort();
	
	/**
	 * Set the remote accept port.
	 */
	public void setPeerPort(int port) throws Exception ;
	
	/**
	 * Set bind the remote accept port.
	 * @param port
	 */
	public void setBindPort(int port) throws Exception ;
	
	/**
	 * Get the remote accept IP address.
	 * @return the remote accept IP address
	 */
	public String getPeerAddress();
	
	/**
	 * Set the remote accept IP address.
	 * This is used to send message to the address.
	 * @param peerAddress
	 */
	public void setPeerAddress(String peerAddress);
	
	/**
	 * Get the transport.
	 * @return the transport.
	 */
	public String getTransport();
	
	/**
	 * Get the key of local listening point.
	 * Example : LocalListeningPoint:<localhost:listeningPort>
	 * @return
	 */
	public String getLocalListeningPointName();
	
	/**
	 * Get the key of remote listening point.
	 * Example : PeerListeningPoint:<localhsot:listeningPort:peerPort>
	 * @return
	 */
	public String getPeerListeningPointName();
	
	public InetAddress getLocalInetAddress();
	/**
     * Transport constant: TCP
     */
    public static final String TCP = "TCP";

    /**
     * Transport constant: UDP
     */
    public static final String UDP = "UDP";

    /**
     * Transport constant: SCTP
     *
     */
    public static final String SCTP = "SCTP";

    /**
     * Transport constant: TLS over TCP 
     */
    public static final String TLS = "TLS";
    
    /**
     * Port Constant: Default port 5060. This constant should only be used
     * when the transport of the ListeningPoint is set to UDP, TCP or SCTP.
     */
    public static final int PORT_5060 = 5060;

    /**
     * Port Constant: Default port 5061. This constant should only be used
     * when the transport of the Listening Point is set to TLS over TCP or TCP 
     * assuming the scheme is "sips".
     */
    public static final int PORT_5061 = 5061;

}
