package net.cpollet.gallery.domain.logins;

public final class CachedLogin implements Login {
    private final Login login;

    CachedLogin(Login login) {
        this.login = login;
    }

    @Override
    public boolean passwordMatches(char[] password) {
        return login.passwordMatches(password);
    }
}
