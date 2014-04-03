package org.aroon.commons.socket.example.parser;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.aroon.commons.socket.context.StdProtocolController;

public class ClientProtocolController extends StdProtocolController{
	Logger logger = Logger.getLogger(ClientProtocolController.class);
	@Override
	public void processRequest(Object object) {
		if(object instanceof MyProtocol){
			MyProtocol protocol = (MyProtocol) object;
			Iterator<Integer> iterator = protocol.getProtocolSegments().keySet().iterator();
			while (iterator.hasNext()) {
				int key = iterator.next();
				logger.info("Type: "+ key +";\tvalue: "+ protocol.getProtocolSegments().get(key));
			}
		}
		
		/*MyProtocol protocol = new MyProtocol();
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(333, "It's OK!");
		protocol.setProtocolSegments(map);
		this.sendBytesMessage(protocol);*/
	}


	@Override
	public void processResponse(Object clas) {
		
	}
}
