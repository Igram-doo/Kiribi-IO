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
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class VarTest {
	static final SecureRandom random = new SecureRandom();
	
	public static void random(byte[] bytes) {
		random.nextBytes(bytes);
	}

	public static int random(int bound) {
		return random.nextInt(bound);
	}

	public static long random() {
		return random.nextLong();
	}

   @Test
   public void testReadWriteBytes() throws IOException {
       VarOutputStream out = new VarOutputStream();
       byte[] b1 = new byte[137];
       random(b1);
       out.writeBytes(b1);
       byte[] b2 = in(out).readBytes();
       assertTrue(Arrays.equals(b1,b2));
   }    

   @Test
   public void testReadWriteUInt8() throws IOException {
       VarOutputStream out = new VarOutputStream();
       long l1 = 87l;
       out.writeUInt8(l1);
       long l2 = in(out).readUInt8();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteUInt16() throws IOException {
       VarOutputStream out = new VarOutputStream();
       long l1 = 487l;
       out.writeUInt16(l1);
       long l2 = in(out).readUInt16();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteUInt32() throws IOException {
       VarOutputStream out = new VarOutputStream();
       long l1 = 65530l;
       out.writeUInt32(l1);
       long l2 = in(out).readUInt32();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteUInt64() throws IOException {
       VarOutputStream out = new VarOutputStream();
       long l1 = 274211l;
       out.writeUInt64(l1);
       long l2 = in(out).readULong64();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteUInt16BE() throws IOException {
       VarOutputStream out = new VarOutputStream();
       long l1 = 487l;
       out.writeUInt16BE(l1);
       long l2 = in(out).readUInt16BE();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteUInt32BE() throws IOException {
       VarOutputStream out = new VarOutputStream();
       long l1 = 65530l;
       out.writeUInt32BE(l1);
       long l2 = in(out).readUInt32BE();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteUInt64BE() throws IOException {
       VarOutputStream out = new VarOutputStream();
       long l1 = 274211l;
       out.writeUInt64BE(l1);
       long l2 = in(out).readULong64BE();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteVarInt() throws IOException {
       VarOutputStream out = new VarOutputStream();
       long l = -100l;
       try {
       	   out.writeVarInt(l);
       	   assertTrue(false);
       } catch(IllegalArgumentException e) {
       	   assertTrue(true);
       }
       
       l = 0x1Cl;
       out = new VarOutputStream();
       out.writeVarInt(l);
       long l2 = in(out).readVarLong();
       assertEquals(l, l2);
       
       l = 0xFCl;
       out = new VarOutputStream();
       out.writeVarInt(l);
       l2 = in(out).readVarLong();
       assertEquals(l, l2);
       
       l = 0xFDl;
       out = new VarOutputStream();
       out.writeVarInt(l);
       l2 = in(out).readVarLong();
       assertEquals(l, l2);
       
       l = 0xFFFEl;
       out = new VarOutputStream();
       out.writeVarInt(l);
       l2 = in(out).readVarLong();
       assertEquals(l, l2);
       
       l = 0xFFFFl;
       out = new VarOutputStream();
       out.writeVarInt(l);
       l2 = in(out).readVarLong();
       assertEquals(l, l2);
       
       l = 0x0001FFFFl;
       out = new VarOutputStream();
       out.writeVarInt(l);
       l2 = in(out).readVarLong();
       assertEquals(l, l2);
       
       l = 0xFFFFFFFFl;
       out = new VarOutputStream();
       out.writeVarInt(l);
       l2 = in(out).readVarLong();
       assertEquals(l, l2);
       
       l = 0x00000001FFFFFFFFl;
       out = new VarOutputStream();
       out.writeVarInt(l);
       l2 = in(out).readVarLong();
       assertEquals(l, l2);

   }     

   @Test
   public void testReadWriteBigInteger() throws IOException {
       VarOutputStream out = new VarOutputStream();
       byte[] b = new byte[137];
       random(b);
       BigInteger i1 = new BigInteger(b);
       out.writeBigInteger(i1);
       BigInteger i2 = in(out).readBigInteger();
       assertEquals(i1, i2);
   }    

   @Test
   public void testReadWriteSocketAddress() throws IOException {
       // wildcard
       VarOutputStream out = new VarOutputStream();
       InetSocketAddress src = new InetSocketAddress(7777);
       out.writeAddress(src);
       InetSocketAddress result = in(out).readAddress();     
       assertEquals(src, result);
       
       // ipv4 loopback
       out = new VarOutputStream();
       InetAddress addr = InetAddress.getByName​("127.0.0.1");
       src = new InetSocketAddress(addr, 7778);
       out.writeAddress(src);
       result = in(out).readAddress();     
       assertEquals(src, result);
       
       // ipv6 loopback
       out = new VarOutputStream();
       addr = InetAddress.getByName​("0:0:0:0:0:ffff:7f00:1");
       src = new InetSocketAddress(addr, 7779);
       out.writeAddress(src);
       result = in(out).readAddress();     
       assertEquals(src, result);
   }

   @Test
   public void testReadWriteEncodable() throws IOException {
       VarOutputStream out = new VarOutputStream();
       TestEncodable t1 = new TestEncodable();
       out.write(t1);
       Object t2 = in(out).read(TestEncodable::new);
       assertEquals(t1, t2);
   }    

   @Test
   public void testReadWriteEncodableCollection() throws IOException {
       VarOutputStream out = new VarOutputStream();
       Set<TestEncodable> se1 = new HashSet<>();
       for(int i = 0; i < 10; i++) se1.add(new TestEncodable());
       out.write(se1);
       Set<TestEncodable> se2 = new HashSet<>();
       in(out).read(se2, TestEncodable::new);
       assertEquals(se1, se2);      
   }    

   @Test
   public void testReadWriteLongCollection() throws IOException {
       VarOutputStream out = new VarOutputStream();
       Set<Long> s1 = new HashSet<>();
       for(int i = 0; i < 10; i++) s1.add(random());
       out.writeLongs(s1);
       Set<Long> s2 = new HashSet<>();
       in(out).readLongs(s2);
       assertEquals(s1, s2);
   }    

   @Test
   public void testReadWriteStringCollection() throws IOException {
       VarOutputStream out = new VarOutputStream();
       Set<String> st1 = new HashSet<>();
       st1.add("dog");
       st1.add("cat");
       st1.add("pig");
       out.writeStrings(st1);
       Set<String> st2 = new HashSet<>();
       in(out).readStrings(st2);
       assertEquals(st1, st2);
   }    

   @Test
   public void testReadWriteStringStringMap() throws IOException {
       VarOutputStream out = new VarOutputStream();
       Map<String,String> m1 = new HashMap<>();
       m1.put("a", "dog");
       m1.put("b", "cat");
       m1.put("c", "pig");
       out.write(m1);
       Map<String,String> m2 = new HashMap<>();
       in(out).read(m2);
       assertEquals(m1, m2);
   }    

   @Test
   public void testReadWriteByteCollection() throws IOException {
       VarOutputStream out = new VarOutputStream();
       Set<Byte> sb1 = new HashSet<>();
       sb1.add((byte)-45);
       sb1.add((byte)38);
       sb1.add((byte)111);
       out.writeBytes(sb1);
       Set<Byte> sb2 = new HashSet<>();
       in(out).readBytes(sb2);
       assertEquals(sb1, sb2);
   }    

   @Test
   public void testReadWriteIntegerCollection() throws IOException {
       VarOutputStream out = new VarOutputStream();
       Set<Integer> si1 = new HashSet<>();
       si1.add(2245);
       si1.add(-238);
       si1.add(3111);
       out.writeInts(si1);
       Set<Integer> si2 = new HashSet<>();
       in(out).readInts(si2);
       assertEquals(si1, si2);
   }    
      
   @Test
   public void testReadWriteEnum() throws IOException {
       VarOutputStream out = new VarOutputStream();
       out.writeEnum(TestEnum.B);
       Object e2 = in(out).readEnum(TestEnum.class);
       assertEquals(TestEnum.B, e2);
   }
   
   private static VarInputStream in(VarOutputStream out) {
   	   return new VarInputStream(out.toByteArray());
   }
   
   private static enum TestEnum {A,B,C}
   
   private static class TestEncodable implements Encodable {
   	   private long l;
   	   private byte[] b;
   	   
   	   TestEncodable() {
   	   	   l = random();
   	   	   int L = 10 + random(10);
   	   	   b = new byte[L];
   	   	   random(b);
   	   }
   	   
   	   TestEncodable(VarInput in) throws IOException {
   	   	   l = in.readLong();
   	   	   b = in.readBytes();
   	   }
   	   
   	   @Override
   	   public void write(VarOutput out) throws IOException {
   	   	   out.writeLong(l);
   	   	   out.writeBytes(b);
   	   }
   	   
   	   @Override
   	   public int hashCode() {return (int)l;}
   	   @Override
   	   public boolean equals(Object o) {
   	   	   if(o == null || !(o instanceof TestEncodable)) return false;
   	   	   TestEncodable t = (TestEncodable)o;
   	   	   return l == t.l && Arrays.equals(b, t.b);
   	   }
   }
}