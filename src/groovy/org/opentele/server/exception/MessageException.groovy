package org.opentele.server.exception

class MessageException extends RuntimeException {

	/**
	 * Used to indicated there's a problem with storing and/or retrieving messages
	 */
	public MessageException() {
		super()
	}
	
	public MessageException (String message ) {
		super(message)
	}
}
