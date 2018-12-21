package net.cpollet.gallery.rest.core.cookie;

public final class Domain {
    private static final Domain SAME = new Domain();

    private final String domainName;

    private Domain(String domainName) {
        this.domainName = domainName;
    }

    private Domain() {
        this(null);
    }

    public static Domain same() {
        return SAME;
    }

    public static Domain domain(String domain) {
        if (domain == null) {
            return same();
        }
        return new Domain(domain);
    }

    public boolean isSet() {
        return domainName != null && !domainName.isEmpty();
    }

    @Override
    public String toString() {
        if (!isSet()) {
            throw new IllegalStateException("No domain configured");
        }
        return domainName;
    }
}
