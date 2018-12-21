package net.cpollet.gallery.domain.tags;

import java.util.List;

public interface Tags {
    void tagsOnAlbum(Long id, List<String> tags);
}
