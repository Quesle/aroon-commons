package org.aroon.commons.socket.example.parser;

import java.util.Iterator;

import org.aroon.commons.socket.context.StdProtocolController;

public class MyProtocolController extends StdProtocolController{

	@Override
	public void processRequest(Object object) {
		if(object instanceof MyProtocol){
			MyProtocol protocol = (MyProtocol) object;
			Iterator<Integer> iterator = protocol.getProtocolSegments().keySet().iterator();
			while (iterator.hasNext()) {
				int key = iterator.next();
				System.out.println("Type: "+ key +";\tvalue: "+ protocol.getProtocolSegments().get(key));
			}
			this.processResponse(object, 3502);
		}
	}


	@Override
	public void processResponse(Object clas, int port) {
		
	}
	
}
