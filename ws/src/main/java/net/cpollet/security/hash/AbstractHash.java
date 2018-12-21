package net.cpollet.security.hash;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public abstract class AbstractHash implements Hash {
    @Override
    public byte[] hash(char[] chars) {
        byte[] bytes = toBytes(chars);
        try {
            return hash(bytes);
        } finally {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = 0;
            }
        }
    }

    // from: https://stackoverflow.com/questions/5513144/converting-char-to-byte
    private byte[] toBytes(char[] chars) {
        CharBuffer charBuffer = CharBuffer.wrap(chars);
        ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(charBuffer);
        byte[] bytes = Arrays.copyOfRange(byteBuffer.array(), byteBuffer.position(), byteBuffer.limit());
        Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
        return bytes;
    }
}
