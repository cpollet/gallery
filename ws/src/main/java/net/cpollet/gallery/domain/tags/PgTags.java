package net.cpollet.gallery.domain.tags;

import lombok.RequiredArgsConstructor;
import net.cpollet.gallery.database.Database;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public final class PgTags implements Tags {
    private final Database database;

    @Override
    public void tagsOnAlbum(Long id, List<String> tags) {
        database.query("DELETE FROM tags WHERE target_type=:type AND target_id=:id" + (tags.isEmpty() ? "" : " AND tag NOT IN (:tags)"))
                .with(Map.of(
                        "type", "album",
                        "id", id,
                        "tags", tags
                ))
                .execute();

        tags.forEach(
                t -> database.query("INSERT INTO tags (tag, target_type, target_id) VALUES (:tag, :type, :id) ON CONFLICT DO NOTHING")
                        .with(
                                Map.of(
                                        "tag", t,
                                        "type", "album",
                                        "id", id
                                ))
                        .execute()
        );
    }
}
