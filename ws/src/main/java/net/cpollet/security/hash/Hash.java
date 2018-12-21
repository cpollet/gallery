package net.cpollet.security.hash;

public interface Hash {
    /**
     * Hashes the chars given in input
     *
     * @param chars the char array to hash
     * @return the resulting hash
     */
    byte[] hash(char[] chars);

    /**
     * Hashes the bytes given in input
     *
     * @param bytes the byte array to hash
     * @return the resulting hash
     */
    byte[] hash(byte[] bytes);
}
