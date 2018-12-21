package net.cpollet.kozan.print;

public interface Printable {
    <P> P print(Media<P> media);
}
