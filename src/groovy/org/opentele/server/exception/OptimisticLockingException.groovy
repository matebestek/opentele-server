package org.opentele.server.exception

class OptimisticLockingException extends RuntimeException {
    List<String> messageArguments

    OptimisticLockingException() {
        super()
    }

    OptimisticLockingException(String s) {
        super(s)
    }


    OptimisticLockingException(String s, List<String> messageArguments) {
        super(s)
        this.messageArguments = messageArguments
    }
}
