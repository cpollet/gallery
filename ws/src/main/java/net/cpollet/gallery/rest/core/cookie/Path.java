package net.cpollet.gallery.rest.core.cookie;

public final class Path {
    private static final Path ROOT = new Path("/");
    private static final Path SAME = new Path();

    private final String location;

    private Path(String location) {
        this.location = location;
    }

    private Path() {
        this(null);
    }

    public static Path same() {
        return SAME;
    }

    public static Path root() {
        return ROOT;
    }

    public static Path path(String path) {
        if ("/".equals(path)) {
            return root();
        }
        if (path == null) {
            return same();
        }
        return new Path(path);
    }

    public boolean isSet() {
        return location != null && !location.isEmpty();
    }

    public String toString() {
        if (!isSet()) {
            throw new IllegalStateException("No path configured");
        }
        return location;
    }
}
