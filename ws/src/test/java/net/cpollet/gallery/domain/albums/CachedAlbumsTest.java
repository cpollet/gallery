package net.cpollet.gallery.domain.albums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({
        "squid:S00100", // method names are fine ;)
        "squid:S1192" // we don't have constants on purpose
})
class CachedAlbumsTest {
    private static final WrappedAlbum WRAPPED_ALBUM = new WrappedAlbum();

    private Album wrapped;
    private CachedAlbum cachedAlbum;

    @BeforeEach
    void prepareWrappedAlbum() {
        wrapped = Mockito.mock(Album.class);
        cachedAlbum = new CachedAlbum(wrapped);
    }

    // id

    @Test
    void get_id_returnsAlbumId() {
        // GIVEN
        Mockito.when(wrapped.id()).thenReturn(1L);

        // WHEN
        long id = cachedAlbum.id();

        // THEN
        Assertions.assertEquals(wrapped.id(), id);
    }

    // name

    @Test
    void get_name_returnsCachedValue_whenCached() {
        // GIVEN
        Mockito.when(wrapped.name()).thenReturn(new AlbumName("wrapped_name"));
        CachedAlbum album = new CachedAlbum(wrapped, new AlbumName("name"), new AlbumDescription("description"), false);

        // WHEN
        AlbumName name = album.name();

        // THEN
        Assertions.assertEquals(new AlbumName("name"), name);
    }

    @Test
    void get_name_returnsWrappedValue_whenNotCached() {
        // GIVEN
        Mockito.when(wrapped.name()).thenReturn(new AlbumName("wrapped_name"));

        // WHEN
        AlbumName name = cachedAlbum.name();

        // THEN
        Assertions.assertEquals(wrapped.name(), name);
    }

    @Test
    void get_name_readsFromWrappedOnlyOnce() {
        // GIVEN
        Mockito.when(wrapped.name()).thenReturn(new AlbumName("wrapped_name"));

        // WHEN
        cachedAlbum.name();
        cachedAlbum.name();

        // THEN
        Mockito.verify(wrapped, Mockito.times(1)).name();
    }

    @Test
    void set_name_writesToWrapped() {
        // GIVEN
        Mockito.when(wrapped.name()).thenReturn(new AlbumName("wrapped_name"));

        // WHEN
        cachedAlbum.name(new AlbumName("name"));

        // THEN
        Mockito.verify(wrapped).name(new AlbumName("name"));
    }

    @Test
    void set_name_doesNotWriteToWrapped_whenValueDoesNotChange() {
        // GIVEN
        Mockito.when(wrapped.name()).thenReturn(new AlbumName("name"));
        cachedAlbum.name(); // warm up cache

        // WHEN
        cachedAlbum.name(new AlbumName("name"));

        // THEN
        Mockito.verify(wrapped, Mockito.never()).name(new AlbumName("name"));
    }

    @Test
    void set_name_updatesCache_whenNoExceptionsAreThrownByWrapped() {
        // GIVEN
        Mockito.when(wrapped.name()).thenReturn(new AlbumName("wrapped_name"));

        // WHEN
        cachedAlbum.name(new AlbumName("name"));
        AlbumName name = cachedAlbum.name();

        // THEN
        Assertions.assertEquals(new AlbumName("name"), name);
    }

    @Test
    void set_name_doesNotUpdateCache_whenExceptionIsThrownByWrapped() {
        // GIVEN
        Mockito.when(wrapped.name()).thenReturn(new AlbumName("wrapped_name"));
        Mockito.when(wrapped.name(new AlbumName("name"))).thenThrow(new RuntimeException());

        // WHEN
        try {
            cachedAlbum.name(new AlbumName("name"));
        } catch (RuntimeException e) {/*noop*/}

        AlbumName name = cachedAlbum.name();

        // THEN
        Assertions.assertEquals(new AlbumName("wrapped_name"), name);
    }

    @Test
    void set_name_doesNotReadFromWrapped_whenCachedValueIsNull() {
        // WHEN
        cachedAlbum.name(new AlbumName("name"));

        // THEN
        Mockito.verify(wrapped, Mockito.never()).name();
    }

    // description

    @Test
    void get_description_returnsCachedValue_whenCached() {
        // GIVEN
        Mockito.when(wrapped.description()).thenReturn(new AlbumDescription("wrapped_description"));
        CachedAlbum album = new CachedAlbum(wrapped, new AlbumName("name"), new AlbumDescription("description"), false);

        // WHEN
        AlbumDescription description = album.description();

        // THEN
        Assertions.assertEquals(new AlbumDescription("description"), description);
    }

    @Test
    void get_description_returnsWrappedValue_whenNotCached() {
        // GIVEN
        Mockito.when(wrapped.description()).thenReturn(new AlbumDescription("wrapped_description"));

        // WHEN
        AlbumDescription description = cachedAlbum.description();

        // THEN
        Assertions.assertEquals(wrapped.description(), description);
    }

    @Test
    void get_description_readsFromWrappedOnlyOnce() {
        // GIVEN
        Mockito.when(wrapped.description()).thenReturn(new AlbumDescription("wrapped_description"));

        // WHEN
        cachedAlbum.description();
        cachedAlbum.description();

        // THEN
        Mockito.verify(wrapped, Mockito.times(1)).description();
    }

    @Test
    void set_description_writesToWrapped() {
        // GIVEN
        Mockito.when(wrapped.description()).thenReturn(new AlbumDescription("wrapped_description"));

        // WHEN
        cachedAlbum.description(new AlbumDescription("description"));

        // THEN
        Mockito.verify(wrapped).description(new AlbumDescription("description"));
    }

    @Test
    void set_description_doesNotWriteToWrapped_whenValueDoesNotChange() {
        // GIVEN
        Mockito.when(wrapped.description()).thenReturn(new AlbumDescription("description"));
        cachedAlbum.description(); // warm up cache

        // WHEN
        cachedAlbum.description(new AlbumDescription("description"));

        // THEN
        Mockito.verify(wrapped, Mockito.never()).description(new AlbumDescription("description"));
    }

    @Test
    void set_description_updatesCache_whenNoExceptionsAreThrownByWrapped() {
        // GIVEN
        Mockito.when(wrapped.description()).thenReturn(new AlbumDescription("wrapped_description"));

        // WHEN
        cachedAlbum.description(new AlbumDescription("description"));
        AlbumDescription description = cachedAlbum.description();

        // THEN
        Assertions.assertEquals(new AlbumDescription("description"), description);
    }

    @Test
    void set_description_doesNotUpdateCache_whenExceptionIsThrownByWrapped() {
        // GIVEN
        Mockito.when(wrapped.description()).thenReturn(new AlbumDescription("wrapped_description"));
        Mockito.when(wrapped.description(new AlbumDescription("description"))).thenThrow(new RuntimeException());

        // WHEN
        try {
            cachedAlbum.description(new AlbumDescription("description"));
        } catch (RuntimeException e) {/*noop*/}

        AlbumDescription description = cachedAlbum.description();

        // THEN
        Assertions.assertEquals(new AlbumDescription("wrapped_description"), description);
    }

    @Test
    void set_description_doesNotReadFromWrapped_whenValueNotCached() {
        // WHEN
        cachedAlbum.description(new AlbumDescription("description"));

        // THEN
        Mockito.verify(wrapped, Mockito.never()).description();
    }

    // published

    @Test
    void get_published_returnsCachedValue_whenCached() {
        // GIVEN
        Mockito.when(wrapped.published()).thenReturn(false);
        CachedAlbum album = new CachedAlbum(wrapped, new AlbumName("name"), new AlbumDescription("description"), true);

        // WHEN
        boolean published = album.published();

        // THEN
        Assertions.assertTrue(published);
    }

    @Test
    void get_published_returnsWrappedValue_whenNotCached() {
        // GIVEN
        Mockito.when(wrapped.published()).thenReturn(false);

        // WHEN
        boolean published = cachedAlbum.published();

        // THEN
        Assertions.assertEquals(wrapped.published(), published);
    }

    @Test
    void get_published_readsFromWrappedOnlyOnce() {
        // GIVEN
        Mockito.when(wrapped.published()).thenReturn(false);

        // WHEN
        cachedAlbum.published();
        cachedAlbum.published();

        // THEN
        Mockito.verify(wrapped, Mockito.times(1)).published();
    }

    @Test
    void set_published_writesToWrapped() {
        // GIVEN
        Mockito.when(wrapped.published()).thenReturn(false);

        // WHEN
        cachedAlbum.published(true);

        // THEN
        Mockito.verify(wrapped).published(true);
    }

    @Test
    void set_published_doesNotWriteToWrapped_whenValueDoesNotChange() {
        // GIVEN
        Mockito.when(wrapped.published()).thenReturn(true);
        cachedAlbum.published(); // warm up cache

        // WHEN
        cachedAlbum.published(true);

        // THEN
        Mockito.verify(wrapped, Mockito.never()).published(true);
    }

    @Test
    void set_published_updatesCache_whenNoExceptionsAreThrownByWrapped() {
        // GIVEN
        Mockito.when(wrapped.published()).thenReturn(false);

        // WHEN
        cachedAlbum.published(true);
        boolean published = cachedAlbum.published();

        // THEN
        Assertions.assertTrue(published);
    }

    @Test
    void set_published_doesNotUpdateCache_whenExceptionIsThrownByWrapped() {
        // GIVEN
        Mockito.when(wrapped.published()).thenReturn(false);
        Mockito.when(wrapped.published(true)).thenThrow(new RuntimeException());

        // WHEN
        try {
            cachedAlbum.published(true);
        } catch (RuntimeException e) {/*noop*/}

        boolean published = cachedAlbum.published();

        // THEN
        Assertions.assertFalse(published);
    }

    @Test
    void set_published_doesNotReadFromWrapped_whenCachedValueIsNull() {
        // WHEN
        cachedAlbum.published(true);

        // THEN
        Mockito.verify(wrapped, Mockito.never()).published();
    }

    // tags

    @Test
    void get_tags_refreshesDescription() {
        // GIVEN
        Mockito.when(wrapped.description()).thenReturn(new AlbumDescription("wrapped_description"));
        CachedAlbum album = new CachedAlbum(wrapped, new AlbumName("name"), new AlbumDescription("description"), true);

        // WHEN
        album.tags();
        AlbumDescription description = album.description();

        // THEN
        Assertions.assertEquals(new AlbumDescription("wrapped_description"), description);
    }

    @Test
    void get_tags_returnsWrappedValue_whenNotCached() {
        // GIVEN
        List<String> expectedTags = Arrays.asList("tag1", "tag2");
        Mockito.when(wrapped.tags()).thenReturn(expectedTags);

        // WHEN
        List<String> tags = cachedAlbum.tags();

        // THEN
        Assertions.assertEquals(expectedTags, tags);
    }

    @Test
    void get_tags_readsFromWrappedOnlyOnce() {
        // GIVEN
        Mockito.when(wrapped.tags()).thenReturn(Collections.emptyList());

        // WHEN
        cachedAlbum.tags();
        cachedAlbum.tags();

        // THEN
        Mockito.verify(wrapped, Mockito.times(1)).tags();
    }

    // unwrap

    @Test
    void unwrap_returnsSelf_whenTargetIsCachedAlbum() {
        // WHEN
        CachedAlbum unwrapped = cachedAlbum.unwrap(CachedAlbum.class);

        // THEN
        Assertions.assertSame(cachedAlbum, unwrapped);
    }

    @Test
    void unwrap_callsRecursively_whenTargetIsNotCachedAlbum() {
        // GIVEN
        Mockito.when(wrapped.unwrap(WrappedAlbum.class)).thenReturn(WRAPPED_ALBUM);

        // WHEN
        Album unwrapped = cachedAlbum.unwrap(WrappedAlbum.class);

        // THEN
        Assertions.assertSame(WRAPPED_ALBUM, unwrapped);
    }

    private static class WrappedAlbum implements Album {
        @Override
        public long id() {
            return 0L;
        }

        @Override
        public AlbumName name() {
            return null;
        }

        @Override
        public Album name(AlbumName name) {
            return null;
        }

        @Override
        public AlbumDescription description() {
            return null;
        }

        @Override
        public Album description(AlbumDescription description) {
            return null;
        }

        @Override
        public boolean published() {
            return false;
        }

        @Override
        public Album published(boolean published) {
            return null;
        }

        @Override
        public <T extends Album> T unwrap(Class<T> clazz) {
            return null;
        }

        @Override
        public List<String> tags() {
            return Collections.emptyList();
        }
    }

    // cache

    @Test
    void evict_cleansCache() {
        // GIVEN
        CachedAlbum album = new CachedAlbum(wrapped, new AlbumName("name"), new AlbumDescription("description"), false);
        Mockito.when(wrapped.name()).thenReturn(new AlbumName("name"));
        Mockito.when(wrapped.description()).thenReturn(new AlbumDescription("description"));
        Mockito.when(wrapped.published()).thenReturn(true);

        // WHEN
        album.evict();
        album.name();
        album.description();
        album.published();

        // THEN
        Mockito.verify(wrapped, Mockito.times(1)).name();
        Mockito.verify(wrapped, Mockito.times(1)).description();
        Mockito.verify(wrapped, Mockito.times(1)).published();
    }

    @Test
    void direct_returnsWrappedAlbum() {
        // WHEN
        Album direct = cachedAlbum.direct();

        // THEN
        Assertions.assertSame(wrapped, direct);
    }
}
