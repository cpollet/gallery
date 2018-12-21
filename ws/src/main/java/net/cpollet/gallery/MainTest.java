package net.cpollet.gallery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.cpollet.gallery.database.JdbcDatabase;
import net.cpollet.gallery.domain.albums.Album;
import net.cpollet.gallery.domain.albums.AlbumDescription;
import net.cpollet.gallery.domain.albums.AlbumName;
import net.cpollet.gallery.domain.albums.CachedAlbum;
import net.cpollet.gallery.domain.albums.ObservableAlbum;
import net.cpollet.gallery.domain.albums.ObservableAlbums;
import net.cpollet.gallery.domain.albums.events.AlbumEventsListener;
import net.cpollet.gallery.domain.albums.events.AlbumEventsListenerChain;
import net.cpollet.gallery.domain.albums.events.AlbumEventsListenerTemplate;
import net.cpollet.gallery.domain.albums.events.AlbumsEventsListener;
import net.cpollet.gallery.domain.gallery.Gallery;
import net.cpollet.gallery.domain.gallery.PgGallery;
import net.cpollet.gallery.rest.api.response.RestAlbum;
import net.cpollet.liquibase.LiquibaseMigration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
public final class MainTest {
    public static void main(String[] args) throws JsonProcessingException {
        DataSource dataSource = new DriverManagerDataSource("jdbc:postgresql://localhost:5432/gallery", "postgres", "password");
        NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);

        new LiquibaseMigration(dataSource).execute();


        final Gallery gallery = new PgGallery(new JdbcDatabase(jdbcTemplate));

        AlbumEventsListener loggingAlbumEventsListener = new AlbumEventsListener() {
            @Override
            public void nameUpdated(Album album, AlbumName oldName) {
                log.info("{}'s name: [{}] -> [{}]", album.id(), oldName, album.name());
            }

            @Override
            public void descriptionUpdated(Album album, AlbumDescription oldDescription) {
                log.info("{}'s description: [{}] -> [{}]", album.id(), oldDescription, album.description());
            }

            @Override
            public void publishedUpdated(Album album, boolean oldPublished) {
                log.info("{}'s published: {} -> {}", album.id(), oldPublished, album.published());
            }

            @Override
            public void tagsUpdated(Album album, List<String> oldTags) {
                log.info("{}'s extractTags: {} -> {}", album.id(), oldTags, album.tags());
            }
        };
        AlbumEventsListener taggingAlbumEventsListener = new AlbumEventsListenerTemplate() {
            @Override
            public void tagsUpdated(Album album, List<String> oldTags) {
                log.info("--> must update tags on {} with {}", album.id(), album.tags());
                gallery.tags().tagsOnAlbum(album.id(), album.tags());
            }
        };
        AlbumEventsListener albumEventsListenerChain = new AlbumEventsListenerChain(Arrays.asList(
                loggingAlbumEventsListener, taggingAlbumEventsListener
        ));
        AlbumsEventsListener albumsEventsListener = album -> log.info("{} created: {}", album.id(), album.name());

        ObservableAlbums albums = new ObservableAlbums(gallery.albums(), albumsEventsListener);

        // New album
        ObservableAlbum newAlbum = new ObservableAlbum(
                albums.create(new AlbumName("name")),
                albumEventsListenerChain
        );
        newAlbum.name(new AlbumName("updated name"));
        newAlbum.description(new AlbumDescription("#winter in #Geneva"));
        newAlbum.published(true);

        log.info("{}", newAlbum.tags());
        log.info("{}", new ObjectMapper().writeValueAsString(new RestAlbum(newAlbum)));

        // Existing album
        ObservableAlbum existingAlbum = new ObservableAlbum(gallery.albums().loadById(1L), albumEventsListenerChain);
        log.info("{}", new ObjectMapper().writeValueAsString(new RestAlbum(existingAlbum)));
        log.info("{}", existingAlbum.tags());
        existingAlbum.description(new AlbumDescription("#winter in #Geneva"));

        // Playing with cache
        CachedAlbum a1 = gallery.albums().loadById(2L).unwrap(CachedAlbum.class);
        CachedAlbum a2 = gallery.albums().loadById(2L).unwrap(CachedAlbum.class);

        a1.name(new AlbumName(UUID.randomUUID().toString()));
        log.info("a1: {}", a1.name());
        log.info("a2: {}", a2.name());
        log.info("a2.direct(): {}", a2.direct().name());
        log.info("a2: {}", a2.name());
        log.info("a2.evict(): {}", a2.evict().name());

        Album result = transactionTemplate.execute(transactionStatus -> {
            Album album = new ObservableAlbum(gallery.albums().loadById(1L), taggingAlbumEventsListener);
            album.description(new AlbumDescription("new #tag #lol"));
            return album;
        });

        log.info("result: {}", result.name());

        log.info("end");
    }
}
