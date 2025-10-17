package uz.sirius.jwt_authentication_project.exception;

public class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
}