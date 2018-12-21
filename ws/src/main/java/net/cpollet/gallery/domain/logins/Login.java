package net.cpollet.gallery.domain.logins;

public interface Login {
    boolean passwordMatches(char[] password);
}
