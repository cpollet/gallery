package net.cpollet.gallery.domain.logins.exceptions;

public final class LoginNotFoundException extends RuntimeException {
    public LoginNotFoundException(long id) {
        super(String.format("Login [%d] does not exist", id));
    }
}
