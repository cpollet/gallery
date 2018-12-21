package net.cpollet.gallery.domain.tags;

import java.util.List;

public interface Tagged {
    /**
     * @return the list of tags, in lower case
     */
    List<String> tags();
}
