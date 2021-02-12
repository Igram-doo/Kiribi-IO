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
       var out = new VarOutputStream();
       var b1 = new byte[137];
       random(b1);
       out.writeBytes(b1);
       var b2 = in(out).readBytes();
       assertTrue(Arrays.equals(b1,b2));
   }    

   @Test
   public void testReadWriteUInt8() throws IOException {
       var out = new VarOutputStream();
       var l1 = 87l;
       out.writeUInt8(l1);
       var l2 = in(out).readUInt8();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteUInt16() throws IOException {
       var out = new VarOutputStream();
       var l1 = 487l;
       out.writeUInt16(l1);
       var l2 = in(out).readUInt16();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteUInt32() throws IOException {
       var out = new VarOutputStream();
       var l1 = 65530l;
       out.writeUInt32(l1);
       var l2 = in(out).readUInt32();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteUInt64() throws IOException {
       var out = new VarOutputStream();
       var l1 = 274211l;
       out.writeUInt64(l1);
       var l2 = in(out).readULong64();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteUInt16BE() throws IOException {
       var out = new VarOutputStream();
       var l1 = 487l;
       out.writeUInt16BE(l1);
       var l2 = in(out).readUInt16BE();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteUInt32BE() throws IOException {
       var out = new VarOutputStream();
       var l1 = 65530l;
       out.writeUInt32BE(l1);
       var l2 = in(out).readUInt32BE();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteUInt64BE() throws IOException {
       var out = new VarOutputStream();
       var l1 = 274211l;
       out.writeUInt64BE(l1);
       var l2 = in(out).readULong64BE();
       assertEquals(l1, l2);
   }     

   @Test
   public void testReadWriteVarInt() throws IOException {
       var out = new VarOutputStream();
       var l = -100l;
       try {
       	   out.writeVarInt(l);
       	   assertTrue(false);
       } catch(IllegalArgumentException e) {
       	   assertTrue(true);
       }
       
       l = 0x1Cl;
       out = new VarOutputStream();
       out.writeVarInt(l);
       var l2 = in(out).readVarLong();
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
       var out = new VarOutputStream();
       var b = new byte[137];
       random(b);
       var i1 = new BigInteger(b);
       out.writeBigInteger(i1);
       var i2 = in(out).readBigInteger();
       assertEquals(i1, i2);
   }    

   @Test
   public void testReadWriteSocketAddress() throws IOException {
       // wildcard
       var out = new VarOutputStream();
       var src = new InetSocketAddress(7777);
       out.writeAddress(src);
       var result = in(out).readAddress();     
       assertEquals(src, result);
       
       // ipv4 loopback
       out = new VarOutputStream();
       var addr = InetAddress.getByName​("127.0.0.1");
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
       var out = new VarOutputStream();
       var t1 = new TestEncodable();
       out.write(t1);
       var t2 = in(out).read(TestEncodable::new);
       assertEquals(t1, t2);
   }    

   @Test
   public void testReadWriteEncodableCollection() throws IOException {
       var out = new VarOutputStream();
       var se1 = new HashSet<TestEncodable>();
       for(int i = 0; i < 10; i++) se1.add(new TestEncodable());
       out.write(se1);
       var se2 = new HashSet<TestEncodable>();
       in(out).read(se2, TestEncodable::new);
       assertEquals(se1, se2);      
   }    

   @Test
   public void testReadWriteLongCollection() throws IOException {
       var out = new VarOutputStream();
       var s1 = new HashSet<Long>();
       for(int i = 0; i < 10; i++) s1.add(random());
       out.writeLongs(s1);
       var s2 = new HashSet<Long>();
       in(out).readLongs(s2);
       assertEquals(s1, s2);
   }    

   @Test
   public void testReadWriteStringCollection() throws IOException {
       var out = new VarOutputStream();
       var st1 = new HashSet<String>();
       st1.add("dog");
       st1.add("cat");
       st1.add("pig");
       out.writeStrings(st1);
       var st2 = new HashSet<String>();
       in(out).readStrings(st2);
       assertEquals(st1, st2);
   }    

   @Test
   public void testReadWriteStringStringMap() throws IOException {
       var out = new VarOutputStream();
       var m1 = new HashMap<String,String>();
       m1.put("a", "dog");
       m1.put("b", "cat");
       m1.put("c", "pig");
       out.write(m1);
       var m2 = new HashMap<String,String>();
       in(out).read(m2);
       assertEquals(m1, m2);
   }    

   @Test
   public void testReadWriteByteCollection() throws IOException {
       var out = new VarOutputStream();
       var sb1 = new HashSet<Byte>();
       sb1.add((byte)-45);
       sb1.add((byte)38);
       sb1.add((byte)111);
       out.writeBytes(sb1);
       var sb2 = new HashSet<Byte>();
       in(out).readBytes(sb2);
       assertEquals(sb1, sb2);
   }    

   @Test
   public void testReadWriteIntegerCollection() throws IOException {
       var out = new VarOutputStream();
       var si1 = new HashSet<Integer>();
       si1.add(2245);
       si1.add(-238);
       si1.add(3111);
       out.writeInts(si1);
       var si2 = new HashSet<Integer>();
       in(out).readInts(si2);
       assertEquals(si1, si2);
   }    
      
   @Test
   public void testReadWriteEnum() throws IOException {
       var out = new VarOutputStream();
       out.writeEnum(TestEnum.B);
       var e2 = in(out).readEnum(TestEnum.class);
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
   	   	   var t = (TestEncodable)o;
   	   	   return l == t.l && Arrays.equals(b, t.b);
   	   }
   }
}