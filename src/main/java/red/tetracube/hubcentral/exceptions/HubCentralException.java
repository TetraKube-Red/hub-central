package red.tetracube.hubcentral.exceptions;

public sealed class HubCentralException extends Exception permits
        HubCentralException.RepositoryException,
        HubCentralException.EntityNotFoundException,
        HubCentralException.UnauthorizedException {

    public static final class RepositoryException extends HubCentralException {
        public RepositoryException(Throwable throwable) {
            super(throwable);
        }
    }

    public static final class EntityNotFoundException extends HubCentralException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public static final class UnauthorizedException extends HubCentralException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }

    protected HubCentralException(Throwable throwable) {
        super(throwable);
    }

    protected HubCentralException(String message) {
        super(message);
    }

}
