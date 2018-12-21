package net.cpollet.gallery.utils;

import com.twitter.twittertext.Extractor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public final class TwitterTagExtractor implements TagExtractor {
    private final Extractor extractor;

    @Override
    public List<String> extractTags(String text) {
        return extractor.extractHashtags(text);
    }
}
