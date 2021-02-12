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
       var src = bytes(0, 100);
       var expected = bytes(0, 50);
       var result = crop(src, 50);
       
       assertTrue(Arrays.equals(expected, result));
   }
   
   @Test
   public void testExtract() throws IOException {
       var src = bytes(0, 100);
       var expected = bytes(50, 10);
       var result = extract(src, 50, 10);
       
       assertTrue(Arrays.equals(expected, result));
   }
   
   @Test
   public void testConcat() throws IOException {
       var src1 = bytes(0, 100);
       var src2 = bytes(100, 100);
       var src3 = bytes(200, 100);
       var expected = bytes(0, 300);
       var result = concat(src1, src2, src3);
       
       assertTrue(Arrays.equals(expected, result));
       
       var src = List.of(src1, src2, src3);
       result = concat(src);
       assertTrue(Arrays.equals(expected, result));
   }
   
   @Test
   public void testReverse() throws IOException {
       var src = bytes(0, 5);
       var expected = new byte[] {0x04, 0x03, 0x02, 0x01, 0x00};
       var result = reverse(src);
       
       assertTrue(Arrays.equals(expected, result));
   }
   
   @Test
   public void testLongToFromBytes() throws IOException {
       var src = -264887030433l;
       var tmp = longToBytes(src);
       var result = bytesToLong(tmp);
       
       assertEquals(src, result);
   }
   
   @Test
   public void testUnsignedIntToFromBytes() throws IOException {
       var l = 778835l;
       var offset = 50;
       var buf = bytes(0, 100);
       unsignedIntToBytes(l,buf, offset);
       var result = bytesToUnsignedInt(buf, offset);
       
       assertEquals(l, result);
       
       var tmp = unsignedIntToBytes(l);
       result = bytesToUnsignedInt(tmp, 0);
       
       assertEquals(l, result);
   }
   
   @Test
   public void testInts() throws IOException {
       var src = new int[]{1,2,3,4,5};
       var tmp = ByteUtils.bytes(src);
       var result = ints(tmp);
       
       assertTrue(Arrays.equals(src, result));
   }
   
   @Test
   public void testAnd() throws IOException {
       var a = new byte[]{0x38, 0x12};
       var b = new byte[]{0x02, 0x07, 0x44};
       var expected = new byte[]{0x02 & 0x38, 0x07 & 0x12};
       
       and(a, b, 2);
       
       assertTrue(Arrays.equals(a, expected));
   }
   
   @Test
   public void testOr() throws IOException {
       var a = new byte[]{0x38, 0x12};
       var b = new byte[]{0x02, 0x07, 0x44};
       var expected = new byte[]{0x02 | 0x38, 0x07 | 0x12};
       
       or(a, b, 2);
       
       assertTrue(Arrays.equals(a, expected));
   }
   
   @Test
   public void testXOR() throws IOException {
       var a = new byte[]{0x38, 0x12};
       var b = new byte[]{0x02, 0x07, 0x44};
       var expected = new byte[]{0x02 ^ 0x38, 0x07 ^ 0x12};
       
       var result = xor(a, b, 2);
       
       assertTrue(Arrays.equals(expected, result));
   }
   
   @Test
   public void testMatches() throws IOException {
       var a = new byte[]{0x38, 0x12};
       var b = new byte[]{0x02, 0x07, 0x44};
       
       assertFalse(matches(a, b, 2));
       
       or(a, b, 2);
       
       assertTrue(matches(a, b, 2));
   }
   
   @Test
   public void testPutGetInt() throws IOException {
       var i = -7788;
       var pos = 50;
       var buf = bytes(0, 100);
       
   	   put(buf, pos, i);
   	   var result = getInt(buf, pos);
   	   
   	   assertEquals(i, result);
   }
   
   @Test
   public void testPutGetLong() throws IOException {
       var l = 778835l;
       var pos = 50;
       var buf = bytes(0, 100);
       
   	   put(buf, pos, l);
   	   var result = getLong(buf, pos);
   	   
   	   assertEquals(l, result);
   }
   
   @Test
   public void testVarSize() throws IOException {
       var l = -100l;
       try {
       	   var size = varSize(l);
       	   assertTrue(false);
       } catch(IllegalArgumentException e) {
       	   assertTrue(true);
       }
       
       l = 0xFCl;
       var result = varSize(l);
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
   	   var offset = 50;
       var buf = bytes(0, 100);
       // wildcard
       var src = new InetSocketAddress(7777);
       inet(buf, offset, src);
       var result = inet(buf, offset);
       
       assertEquals(src, result);
       
       offset = 17;
       buf = bytes(0, 100);
       // ipv4 loopback
       var addr = InetAddress.getByName​("127.0.0.1");
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
   	   var b = new byte[l];
   	   var v = s;
   	   for(int i = 0; i < l; i++) b[i] = (byte)s++;
   	   return b;
   }
}