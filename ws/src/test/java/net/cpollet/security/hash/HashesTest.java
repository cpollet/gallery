package net.cpollet.security.hash;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HashesTest {
     @Test
   void repeatedSaltedHash_returnsHash() {
         // GIVEN
         Hash hash = new RepeatedHash(
                 new SaltedHash(
                         new NoHash(),
                         "_"::getBytes
                 ),
                 2
         );

         // WHEN
         byte[]  hashed = hash.hash("clear".getBytes());

         // THEN
         Assertions.assertArrayEquals("H(_H(_clear))".getBytes(), hashed);
     }
}
