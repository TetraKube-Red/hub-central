package red.tetracube.hubcentral.exceptions;

import red.tetracube.hubcentral.exceptions.HubCentralException.EntityNotFoundException;
import red.tetracube.hubcentral.exceptions.HubCentralException.RepositoryException;
import red.tetracube.hubcentral.exceptions.HubCentralException.UnauthorizedException;

public sealed class HubCentralException extends Exception permits
        RepositoryException, 
        EntityNotFoundException, 
        UnauthorizedException {

    public final class RepositoryException extends HubCentralException { 
        public RepositoryException(Exception parentException) {
            super(parentException);
        }
    }

    public final class EntityNotFoundException extends HubCentralException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public final class UnauthorizedException extends HubCentralException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
    
    protected HubCentralException(Exception parentException) {
        super(parentException);
    }

    protected HubCentralException(String message) {
        super(message);
    }

}
