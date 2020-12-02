/* 
 * MIT License
 * 
 * Copyright (c) 2020 Igram, d.o.o.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
 
package rs.igram.kiribi.io;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

import static rs.igram.kiribi.io.ByteUtils.*;

public class ByteUtilsTest {
   @Test
   public void testCrop() throws IOException {
       byte[] src = bytes(0, 100);
       byte[] expected = bytes(0, 50);
       byte[] result = crop(src, 50);
       
       assertTrue(Arrays.equals(expected, result));
   }
   
   @Test
   public void testExtract() throws IOException {
       byte[] src = bytes(0, 100);
       byte[] expected = bytes(50, 10);
       byte[] result = extract(src, 50, 10);
       
       assertTrue(Arrays.equals(expected, result));
   }
   
   @Test
   public void testConcat() throws IOException {
       byte[] src1 = bytes(0, 100);
       byte[] src2 = bytes(100, 100);
       byte[] src3 = bytes(200, 100);
       byte[] expected = bytes(0, 300);
       byte[] result = concat(src1, src2, src3);
       
       assertTrue(Arrays.equals(expected, result));
       
       List<byte[]> src = List.of(src1, src2, src3);
       result = concat(src);
       assertTrue(Arrays.equals(expected, result));
   }
   
   @Test
   public void testReverse() throws IOException {
       byte[] src = bytes(0, 5);
       byte[] expected = new byte[] {0x04, 0x03, 0x02, 0x01, 0x00};
       byte[] result = reverse(src);
       
       assertTrue(Arrays.equals(expected, result));
   }
   
   @Test
   public void testLongToFromBytes() throws IOException {
       long src = -264887030433l;
       byte[] tmp = longToBytes(src);
       long result = bytesToLong(tmp);
       
       assertEquals(src, result);
   }
   
   @Test
   public void testUnsignedIntToFromBytes() throws IOException {
       long l = 778835l;
       int offset = 50;
       byte[] buf = bytes(0, 100);
       unsignedIntToBytes(l,buf, offset);
       long result = bytesToUnsignedInt(buf, offset);
       
       assertEquals(l, result);
       
       byte[] tmp = unsignedIntToBytes(l);
       result = bytesToUnsignedInt(tmp, 0);
       
       assertEquals(l, result);
   }
   
   @Test
   public void testInts() throws IOException {
       int[] src = new int[]{1,2,3,4,5};
       byte[] tmp = ByteUtils.bytes(src);
       int[] result = ints(tmp);
       
       assertTrue(Arrays.equals(src, result));
   }
   
   @Test
   public void testAnd() throws IOException {
       byte[] a = new byte[]{0x38, 0x12};
       byte[] b = new byte[]{0x02, 0x07, 0x44};
       byte[] expected = new byte[]{0x02 & 0x38, 0x07 & 0x12};
       
       and(a, b, 2);
       
       assertTrue(Arrays.equals(a, expected));
   }
   
   @Test
   public void testOr() throws IOException {
       byte[] a = new byte[]{0x38, 0x12};
       byte[] b = new byte[]{0x02, 0x07, 0x44};
       byte[] expected = new byte[]{0x02 | 0x38, 0x07 | 0x12};
       
       or(a, b, 2);
       
       assertTrue(Arrays.equals(a, expected));
   }
   
   @Test
   public void testXOR() throws IOException {
       byte[] a = new byte[]{0x38, 0x12};
       byte[] b = new byte[]{0x02, 0x07, 0x44};
       byte[] expected = new byte[]{0x02 ^ 0x38, 0x07 ^ 0x12};
       
       byte[] result = xor(a, b, 2);
       
       assertTrue(Arrays.equals(expected, result));
   }
   
   @Test
   public void testMatches() throws IOException {
       byte[] a = new byte[]{0x38, 0x12};
       byte[] b = new byte[]{0x02, 0x07, 0x44};
       
       assertFalse(matches(a, b, 2));
       
       or(a, b, 2);
       
       assertTrue(matches(a, b, 2));
   }
   
   @Test
   public void testPutGetInt() throws IOException {
       int i = -7788;
       int pos = 50;
       byte[] buf = bytes(0, 100);
       
   	   put(buf, pos, i);
   	   int result = getInt(buf, pos);
   	   
   	   assertEquals(i, result);
   }
   
   @Test
   public void testPutGetLong() throws IOException {
       long l = 778835l;
       int pos = 50;
       byte[] buf = bytes(0, 100);
       
   	   put(buf, pos, l);
   	   long result = getLong(buf, pos);
   	   
   	   assertEquals(l, result);
   }
   
   @Test
   public void testVarSize() throws IOException {
       long l = -100l;
       try {
       	   int size = varSize(l);
       	   assertTrue(false);
       } catch(IllegalArgumentException e) {
       	   assertTrue(true);
       }
       
       l = 0xFCl;
       int result = varSize(l);
       assertEquals(1, result);
       
       l = 0xFDl;
       result = varSize(l);
       assertEquals(2, result);
       
       l = 0xFFFEl;
       result = varSize(l);
       assertEquals(2, result);
       
       l = 0xFFFFl;
       result = varSize(l);
       assertEquals(2, result);
       
       l = 0x0001FFFFl;
       result = varSize(l);
       assertEquals(4, result);
       
       l = 0xFFFFFFFFl;
       result = varSize(l);
       assertEquals(4, result);
       
       l = 0x00000001FFFFFFFFl;
       result = varSize(l);
       assertEquals(8, result);
   }
  
   @Test
   public void testInet() throws Exception {
   	   int offset = 50;
       byte[] buf = bytes(0, 100);
       // wildcard
       SocketAddress src = new InetSocketAddress(7777);
       inet(buf, offset, src);
       SocketAddress result = inet(buf, offset);
       
       assertEquals(src, result);
       
       offset = 17;
       buf = bytes(0, 100);
       // ipv4 loopback
       InetAddress addr = InetAddress.getByName​("127.0.0.1");
       src = new InetSocketAddress(addr, 7778);
       inet(buf, offset, src);
       result = inet(buf, offset);
       
       assertEquals(src, result);
       
       offset = 61;
       buf = bytes(0, 100);
       // ipv6 loopback
       addr = InetAddress.getByName​("0:0:0:0:0:ffff:7f00:1");
       src = new InetSocketAddress(addr, 7779);
       inet(buf, offset, src);
       result = inet(buf, offset);
       
       assertEquals(src, result);
   }
   
   private static byte[] bytes(int s, int l) {
   	   byte[] b = new byte[l];
   	   int v = s;
   	   for(int i = 0; i < l; i++) b[i] = (byte)s++;
   	   return b;
   }
}