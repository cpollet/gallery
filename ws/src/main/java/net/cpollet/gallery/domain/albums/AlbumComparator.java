package net.cpollet.gallery.domain.albums;

import com.google.common.collect.ImmutableMap;
import net.cpollet.gallery.domain.ValidationException;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public final class AlbumComparator implements Comparator<Album> {
    private static final String VALIDATION_TARGET = "album/comparator";
    private static final Map<String, Comparator<Album>> COMPARATORS = ImmutableMap.of(
            "name:asc", Comparator.comparing(Album::name),
            "name:desc", Collections.reverseOrder(Comparator.comparing(Album::name)),
            "id:asc", Comparator.comparing(Album::id),
            "id:desc", Comparator.comparing(Album::id).reversed()
    );

    private final Comparator<Album> comparator;

    public AlbumComparator(Comparator<Album> comparator) {
        this.comparator = comparator;
    }

    public AlbumComparator(String comparatorName) {
        if (!COMPARATORS.containsKey(comparatorName)) {
            throw ValidationException.isNotSupported(VALIDATION_TARGET, comparatorName);
        }

        this.comparator = COMPARATORS.get(comparatorName);
    }

    @Override
    public int compare(Album a, Album b) {
        return comparator.compare(a, b);
    }
}
