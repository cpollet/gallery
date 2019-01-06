package net.cpollet.gallery.utils;

import com.twitter.twittertext.Extractor;

import java.util.List;

public final class TwitterTagExtractor implements TagExtractor {
    private final Extractor extractor;

    public TwitterTagExtractor(Extractor extractor) {
        this.extractor = extractor;
    }

    @Override
    public List<String> extractTags(String text) {
        return extractor.extractHashtags(text);
    }
}
