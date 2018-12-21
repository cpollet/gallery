package net.cpollet.gallery.utils;

import java.util.List;

public interface TagExtractor {
    List<String> extractTags(String text);
}
