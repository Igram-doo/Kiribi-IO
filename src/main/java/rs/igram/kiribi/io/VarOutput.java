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

import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Defines methods to write <code>Encodable</code> objects.
 *
 * @author Michael Sargent
 */
public interface VarOutput extends DataOutput {
	/**
	 * Wraps a <code>DataOutput</code> as a <code>VarOutput</code>.
	 * <p>
	 * The returned <code>VarOutput</code> will delegate to the provided <code>DataOutput</code>.
	 *
	 * @param inner The <code>DataOut</code> to delegate to.
	 * @return A new <code>VarOut</code> instance which will delegate to the provided 
	 * <code>DataOut</code>.
	 */
	default VarOutput wrap(final DataOutput inner) {
		return OutputWrapper.wrap(inner);
	}
	
	/**
	 * Writes a byte array.
	 *
	 * @param b The byte array to be written.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readBytes
	 */
	default void writeBytes(byte[] b) throws IOException {
		writeInt(b.length);
		if(b.length > 0) write(b);
	}
	
	/**
	 * Writes a long as a single unsigned <code>byte</code>.
	 *
	 * @param v A long to be written as a single unsigned <code>byte</code>.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readUInt8
	 */	
	default void writeUInt8(long v) throws IOException {
		write((byte)(0xFFL & v));
	}
	
	/**
	 * Writes a long as two unsigned <code>byte</code>s.
	 *
	 * @param v A long to be written as two unsigned <code>byte</code>s.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readUInt16
	 */	
	default void writeUInt16(long v) throws IOException {
		write((byte)(0xFFL & v));
		write((byte)(0xFFL & (v >> 8)));
	}
	
	/**
	 * Writes a long as four unsigned <code>byte</code>s.
	 *
	 * @param v A long to be written as four unsigned <code>byte</code>s.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readUInt32
	 */		
	default void writeUInt32(long v) throws IOException {
		write((byte)(0xFFL & v));
		write((byte)(0xFFL & (v >> 8)));
		write((byte)(0xFFL & (v >> 16)));
		write((byte)(0xFFL & (v >> 24)));
	}
	
	/**
	 * Writes a long as eight unsigned <code>byte</code>s.
	 *
	 * @param v A long to be written as eight unsigned <code>byte</code>s.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readULong64
	 */		
	default void writeUInt64(long v) throws IOException {
		write((byte)(0xFFL & v));
		write((byte)(0xFFL & (v >> 8)));
		write((byte)(0xFFL & (v >> 16)));
		write((byte)(0xFFL & (v >> 24)));
		write((byte)(0xFFL & (v >> 32)));
		write((byte)(0xFFL & (v >> 40)));
		write((byte)(0xFFL & (v >> 48)));
		write((byte)(0xFFL & (v >> 56)));
	}
	
	/**
	 * Writes a long as two unsigned <code>byte</code>s in bigendian byte order.
	 *
	 * @param v A long to be written as two unsigned <code>byte</code>s in bigendian byte order.
	 * @throws IOException if there was a problem writing the data
	 * @see VarInput#readUInt16BE
	 */	
	default void writeUInt16BE(long v) throws IOException {
		write((byte)(0xFFL & (v >> 8)));
		write((byte)(0xFFL & v));
	}
	
	/**
	 * Writes a long as four unsigned <code>byte</code>s in bigendian byte order.
	 *
	 * @param v A long to be written as four unsigned <code>byte</code>s in bigendian byte order.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readUInt32BE
	 */		
	default void writeUInt32BE(long v) throws IOException {
		write((byte)(0xFFL & (v >> 24)));
		write((byte)(0xFFL & (v >> 16)));
		write((byte)(0xFFL & (v >> 8)));
		write((byte)(0xFFL & v));
	}
	
	/**
	 * Writes a long as eight unsigned <code>byte</code>s in bigendian byte order.
	 *
	 * @param v A long to be written as eight unsigned <code>byte</code>s in bigendian byte order.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readULong64BE
	 */		
	default void writeUInt64BE(long v) throws IOException {
		write((byte)(0xFFL & (v >> 56)));
		write((byte)(0xFFL & (v >> 48)));
		write((byte)(0xFFL & (v >> 40)));
		write((byte)(0xFFL & (v >> 32)));
		write((byte)(0xFFL & (v >> 24)));
		write((byte)(0xFFL & (v >> 16)));
		write((byte)(0xFFL & (v >> 8)));
		write((byte)(0xFFL & v));
	}
	
	/**
	 * Writes a long as a VarInt.
	 *
	 * @param v A long to be written as a VarInt.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readVarInt
	 */	
	default void writeVarInt(long v) throws IOException {
		if(v < 0l) throw new IllegalArgumentException("Input must be non-negative: "+v);
		if(v < 0xFDl) {
			writeUInt8(v);
		}else if(v <= 0xFFFFl) {
			write(0xFD);
			writeUInt16(v);
		}else if(v <= 0xFFFFFFFFl) {
			write(0xFE);
			writeUInt32(v);
		}else {
			write(0xFF);
			writeUInt64(v);
		}
	}
	
	/**
	 * Writes a <code>BigInteger</code>.
	 *
	 * @param value A <code>BigInteger</code> to be written.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readBigInteger
	 */		
	default void writeBigInteger(BigInteger value) throws IOException {	
		writeBytes(value.toByteArray());
	}
	
	/**
	 * Writes a <code>InetSocketAddress</code>.
	 *
	 * @param addr A <code>InetSocketAddress</code> to be written.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readAddress
	 */			
	default void writeAddress(InetSocketAddress addr) throws IOException {	
		//todo
		var b = addr.getAddress().getAddress();
		writeInt(b.length);
		write(b);
		writeUInt16BE(addr.getPort());
	}
	
	/**
	 * Writes a <code>Encodable</code>.
	 *
	 * @param encodable A <code>Encodable</code> to be written.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#read(Decoder)
	 */			
	default void write(Encodable encodable) throws IOException {
		encodable.write(this);
	}
	
	/**
	 * Writes a <code>Collection</code> of <code>Encodable</code> objects.
	 *
	 * @param collection A <code>Collection</code> of <code>Encodable</code> objects to be written.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#read(Collection, Decoder)
	 */		
	default void write(Collection<? extends Encodable> collection) throws IOException {
		writeVarInt(collection.size());
		for(Encodable v : collection) v.write(this);
	}
	
	/**
	 * Writes a <code>Collection</code> of <code>Long</code> objects.
	 *
	 * @param collection A <code>Collection</code> of <code>Long</code> objects to be written.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readLongs
	 */		
	default void writeLongs(Collection<Long> collection) throws IOException {
		writeVarInt(collection.size());
		for(Long v : collection) writeLong(v);
	}
	
	/**
	 * Writes a <code>Collection</code> of <code>String</code> objects.
	 *
	 * @param collection A <code>Collection</code> of <code>String</code> objects to be written.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readStrings
	 */		
	default void writeStrings(Collection<String> collection) throws IOException {
		writeVarInt(collection.size());
		for(String v : collection) writeUTF(v);
	}
	
	/**
	 * Writes a map of strings.
	 *
	 * @param map The map of strings to be written.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#read(Map)
	 */		
	default void write(Map<String,String> map) throws IOException {
		writeVarInt(map.size());
		for(Map.Entry<String,String> entry : map.entrySet()){
			writeUTF(entry.getKey());
			writeUTF(entry.getValue() == null ? "" : entry.getValue());
		}
	}
	
	/**
	 * Writes a <code>Collection</code> of <code>Byte</code> objects.
	 *
	 * @param collection A <code>Collection</code> of <code>Byte</code> objects to be written.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readBytes
	 */		
	default void writeBytes(Collection<Byte> collection) throws IOException {
		writeVarInt(collection.size());
		for(Byte v : collection) writeByte(v);
	}
	
	/**
	 * Writes a <code>Collection</code> of <code>Integer</code> objects.
	 *
	 * @param collection A <code>Collection</code> of <code>Integer</code> objects to be written.
	 * @throws IOException if there was a problem writing the data.
	 * @see VarInput#readInts
	 */		
	default void writeInts(Collection<Integer> collection) throws IOException {
		writeVarInt(collection.size());
		for(Integer v : collection) writeInt(v);
	}
	
	/**
	 * Writes a <code>Collection</code> of byte arrays.
	 *
	 * @param collection A <code>Collection</code> of byte arrays to be written.
	 * @throws IOException if there was a problem writing the data.
	 */		
	default void writeByteArrays(Collection<byte[]> collection) throws IOException {
		writeVarInt(collection.size());
		for(byte[] v : collection) write(v);
	}	
	
	/**
	 * Writes an <code>Enum</code>.
	 *
	* @param <E> The type of the enum.
	* @param e The <code>Enum</code> to be written.
	* @throws IOException if there was a problem writing the data.
	* @see VarInput#readEnum
	*/	
	default <E extends Enum> void writeEnum(E e) throws IOException {
		writeVarInt(e.ordinal());
	}
}

// Wraps a DataOutput instance into a VarOutput instance
class OutputWrapper {
	private static Set<Method> methods = new HashSet<>();
	static {
		for(Method m : DataOutput.class.getDeclaredMethods()) methods.add(m);
	}
	
	private OutputWrapper(){}
		
	static VarOutput wrap(final DataOutput inner) {
		return (VarOutput)Proxy.newProxyInstance(
			VarOutput.class.getClassLoader(),
			new Class<?>[] {VarOutput.class},               
			new InvocationHandler() {
				@Override
                public Object invoke(Object proxy, Method method, Object[] args) {
                	try{
                		return methods.contains(method) ? 
                           	method.invoke(inner, args) :
                           	method.invoke(proxy, args);
                    }catch(Exception e){
                       	//e.printStackTrace();
                        throw new RuntimeException(e);
                    }
                }
        });
    }
}
