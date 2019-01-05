package net.cpollet.gallery.domain.albums;

import net.cpollet.gallery.domain.ValidationException;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public final class AlbumFilter implements Predicate<Album> {
    private static final String VALIDATION_TARGET = "album/filter";
    private static final Map<String, BiFunction<Album, String, Boolean>> PREDICATES = Map.of(
            "name.contains", (album, s) -> album.name().toString().contains(s),
            "published", (album, s) -> Boolean.valueOf(s).equals(album.published())
    );
    private final Predicate<Album> filter;

    public AlbumFilter(String filterName, String filterValue) {
        if (!PREDICATES.containsKey(filterName)) {
            throw ValidationException.isNotSupported(VALIDATION_TARGET, filterName);
        }

        if (filterValue == null) {
            throw ValidationException.isNull(String.format("%s/value", VALIDATION_TARGET));
        }

        if (filterValue.isEmpty()) {
            throw ValidationException.isEmpty(String.format("%s/value", VALIDATION_TARGET));
        }

        filter = album -> PREDICATES.get(filterName).apply(album, filterValue);
    }

    public AlbumFilter(Spec spec) {
        this(spec.filterName(), spec.filterValue());
    }

    @Override
    public boolean test(Album album) {
        return filter.test(album);
    }

    public static class Spec { // TODO move that to Payload deserialization
        private static final String VALIDATION_REGEX = "^[^:]+:[^:]+$";
        private static final String SPLIT_REGEX = ":";
        private final String spec;

        public Spec(String spec) {
            if (!spec.matches(VALIDATION_REGEX)) {
                throw ValidationException.isInvalid(VALIDATION_TARGET, spec);
            }

            this.spec = spec;
        }

        String filterName() {
            return spec.split(SPLIT_REGEX)[0];
        }

        String filterValue() {
            return spec.split(SPLIT_REGEX)[1];
        }
    }
}
