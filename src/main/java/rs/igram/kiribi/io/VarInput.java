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

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Defines methods to read <code>Encodable</code> objects.
 *
 * @author Michael Sargent
 */
public interface VarInput extends DataInput {	
	/**
	 * Wraps a <code>DataInput</code> as a <code>VarInput</code>.
	 * <p>
	 * The returned <code>VarInput</code> will delegate to the provided <code>DataInput</code>.
	 *
	 * @param inner The <code>DataInput</code> to delegate to.
	 * @return A new <code>VarInput</code>.
	 */
	default VarInput wrap(final DataInput inner) {
		return InputWrapper.wrap(inner);
	}

	/**
	 * Reads a byte array.
	 *
	 * @return The byte array read.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeBytes
	 */
    default byte[] readBytes() throws IOException {
    	int L = readInt();
    	byte[] b = new byte[L];
    	if(L > 0) readFully(b);
    	return b;
    }
/*    
    default byte[] readBytes(int length) throws IOException {
    	byte[] b = new byte[length];
    	readFully(b);
    	return b;
    }
  
    default byte[] readVarBytes() throws IOException {
    	int L = readVarInt();
    	return readBytes(L);
    }
 */   
 
	/**
	 * Reads a single byte as an unsigned <code>int</code>.
	 *
	 * @return A single byte read as an unsigned <code>int</code>.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeUInt8
	 */
	default int readUInt8() throws IOException {
		return readByte() & 0xFF;
	}
	
	
	/**
	 * Reads two bytes as an unsigned <code>int</code>.
	 *
	 * @return Two bytes read as an unsigned <code>int</code>.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeUInt16
	 */
	default int readUInt16() throws IOException {
		return (int)readULong16();
//		return (readByte() & 0xFF) 
//		| ((readByte() & 0xFF) << 8);
	}
	
	/**
	 * Reads four bytes as an unsigned <code>long</code>.
	 *
	 * @return Four bytes read as an unsigned <code>long</code>.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeUInt32
	 */
	default int readUInt32() throws IOException {
		return (int)readULong32();
	}
	
	/**
	 * Reads four bytes as an unsigned <code>long</code>.
	 *
	 * @return Four bytes read as an unsigned <code>long</code>.
	 * @throws IOException if there was a problem reading the data.
	 */
	default long readULong16() throws IOException {
		return (readByte() & 0xFFL) 
		| ((readByte() & 0xFFL) << 8);
	}
	
	/**
	 * Reads four bytes as an unsigned <code>long</code>.
	 *
	 * @return Four bytes read as an unsigned <code>long</code>.
	 * @throws IOException if there was a problem reading the data.
	 */
	default long readULong32() throws IOException {
		return (readByte() & 0xFFL) 
		| ((readByte() & 0xFFL) << 8) 
		| ((readByte() & 0xFFL) << 16) 
		| ((readByte() & 0xFFL) << 24);
	}
	
	/**
	 * Reads eight bytes as an unsigned <code>long</code>.
	 *
	 * @return Eight bytes read as an unsigned <code>long</code>.
	 * @throws IOException if there was a problem reading the data.
	 */
	default long readULong64() throws IOException {
		return (readByte() & 0xFFL) 
		| ((readByte() & 0xFFL) << 8) 
		| ((readByte() & 0xFFL) << 16) 
		| ((readByte() & 0xFFL) << 24)
		| ((readByte() & 0xFFL) << 32)
		| ((readByte() & 0xFFL) << 40) 
		| ((readByte() & 0xFFL) << 48) 
		| ((readByte() & 0xFFL) << 56);
	}
	
	/**
	 * Reads two bytes in bigendian byte order as an unsigned <code>int</code>.
	 *
	 * @return Two bytes read in bigendian order as an unsigned <code>int</code>.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeUInt16BE
	 */
	default int readUInt16BE() throws IOException {
		return ((readByte() & 0xFF) << 8) 
		| (readByte() & 0xFF);
	}
	
	/**
	 * Reads four bytes in bigendian byte order as an unsigned <code>int</code>.
	 *
	 * @return Four bytes read in bigendian order as an unsigned <code>int</code>.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeUInt32BE
	 */
	default int readUInt32BE() throws IOException {
		return (int)readULong32BE();
	}
	
	/**
	 * Reads four bytes in bigendian byte order as an unsigned <code>long</code>.
	 *
	 * @return Four bytes read in bigendian order as an unsigned <code>long</code>.
	 * @throws IOException if there was a problem reading the data.
	 */
	default long readULong32BE() throws IOException {
		return ((readByte() & 0xFFL) << 24) 
		| ((readByte() & 0xFFL) << 16) 
		| ((readByte() & 0xFFL) << 8) 
		| (readByte() & 0xFFL);
	}
	
	/**
	 * Reads eight bytes in bigendian byte order as an unsigned <code>long</code>.
	 *
	 * @return Eight bytes read in bigendian order as an unsigned <code>long</code>.
	 * @throws IOException if there was a problem reading the data.
	 */
	default long readULong64BE() throws IOException {
		return ((readByte() & 0xFFL) << 56)
		| ((readByte() & 0xFFL) << 48) 
		| ((readByte() & 0xFFL) << 40) 
		| ((readByte() & 0xFFL) << 32)
		| ((readByte() & 0xFFL) << 24)
		| ((readByte() & 0xFFL) << 16) 
		| ((readByte() & 0xFFL) << 8) 
		| (readByte() & 0xFFL);
	}

	/**
	 * Reads a VarInt as an <code>int</code>.
	 *
	 * @return Reads a VarInt as an <code>int</code>.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeVarInt
	 */
	default int readVarInt() throws IOException {
		return (int)readVarLong();
	}
	
	/**
	 * Reads a VarLong as a <code>long</code>.
	 *
	 * @return Reads a VarLong as an <code>long</code>.
	 * @throws IOException if there was a problem reading the data.
	 */
	default long readVarLong() throws IOException {
		int b = readUnsignedByte();
		switch(b){
		// X16	
		case 0xFD: return readULong16();
		// X32	
		case 0xFE: return readULong32();
		// X64	
		case 0xFF: return readULong64();
		// X8
		default: return (long)b;
		}
	}

	/**
	 * Reads a <code>BigInteger</code>.
	 * @return Reads a <code>BigInteger</code>.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeBigInteger
	 */
	default BigInteger readBigInteger() throws IOException {	
		return new BigInteger(readBytes());
	}
	
	/**
	 * Reads a <code>InetSocketAddress</code>.
	 *
	 * @return Reads a <code>InetSocketAddress</code>.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeAddress
	 */
	default InetSocketAddress readAddress() throws IOException {	
		//todo
		byte[] b = readBytes();
		int port = readUInt16BE();
		return new InetSocketAddress(InetAddress.getByAddress(b), port);
	}
	
	/**
	 * Reads a <code>InetSocketAddress</code>.
	 *
	 * @return Reads a <code>InetSocketAddress</code>.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeAddress
	 */
	default InetSocketAddress readSocketAddress() throws IOException {		
		// first 12 bytes of inet address. If this equals IPV4 then its an IPV4 address and only use last 4 bytes.
		// Otherwise its an IPV6 address and concat the first 12 bytes and last 4 bytes
		final byte[] a = new byte[12];
		readFully(a);
		// last 4 bytes of inet address
		final byte[] b = new byte[4];
		readFully(b);

		// null ?
		if(Arrays.equals(new byte[16], concat(a, b))) throw new IOException("Invalid address: null");
			
		final byte[] ipv4 = new byte[12];
		ipv4[10] = (byte)0xff;
		ipv4[11] = (byte)0xff;
		
		final InetAddress addr = Arrays.equals(a, ipv4) ?
			InetAddress.getByAddress(b) :
			InetAddress.getByAddress(concat(a, b));
		final int port = readUInt16BE();
		return new InetSocketAddress(addr, port);
	}

	/**
	 * Reads a <code>String</code>.
	 *
	 * @return Reads a <code>String</code>.
	 * @throws IOException if there was a problem reading the data.
	 */
	default String readVarChar() throws IOException {
		final StringBuilder buf = new StringBuilder();
		final int l = readVarInt();
		for(int i = 0; i < l; i++) buf.appendCodePoint(readByte()  & 0xFF);
		return buf.toString();
	}
	
	/**
	 * Reads an <code>Encodable</code> object.
	 *
	 * @param <T> The type of the object to be read.
	 * @param decoder The <code>Decoder</code> to decode the object.
	 * @return Reads an <code>Encodable</code> object.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#write(Encodable)
	 */
	default <T> T read(Decoder<T> decoder) throws IOException {
		return decoder.read(this);
	}
	
	/**
	 * Reads a collection of <code>Encodable</code> objects.
	 *
	 * @param <T> The type of the objects to be read.
	 * @param collection The collection to put the read and decoded objects into.
	 * @param decoder The <code>Decoder</code> to decode the objects read.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#write(Collection)
	 */
	default <T> void read(Collection<T> collection, Decoder<T> decoder) throws IOException {
		int L = readVarInt();
		if(decoder == null) return;
		for(int i = 0; i < L; i++) collection.add(decoder.read(this));
	}
	
	/**
	 * Reads a collection of <code>Long</code> objects.
	 *
	 * @param collection The collection to put the read <code>Long</code> objects into.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeLong
	 */
	default void readLongs(Collection<Long> collection) throws IOException {
		int L = readVarInt();
		for(int i = 0; i < L; i++) collection.add(readLong());
	}
	
	/**
	 * Reads a collection of <code>String</code> objects.
	 *
	 * @param collection The collection to put the read <code>String</code> objects into.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeStrings
	 */
	default void readStrings(Collection<String> collection) throws IOException {
		int L = readVarInt();
		for(int i = 0; i < L; i++) collection.add(readUTF());
	}
	
	/**
	 * Reads a map of <code>String</code> objects.
	 *
	 * @param map The map to put the read map entries into.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#write(Map)
	 */
	default void read(Map<String,String> map) throws IOException {
		int L = readVarInt();
		for(int i = 0; i < L; i++){
			String key = readUTF();
			String value = readUTF();
			map.put(key, value);
		}
	}
	
	/**
	 * Reads a collection of <code>Byte</code> objects.
	 *
	 * @param collection The collection to put the read <code>Byte</code> objects into.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeBytes
	 */
	default void readBytes(Collection<Byte> collection) throws IOException {
		int L = readVarInt();
		for(int i = 0; i < L; i++) collection.add(readByte());
	}
	
	/**
	 * Reads a collection of <code>Integer</code> objects.
	 *
	 * @param collection The collection to put the read <code>Integer</code> objects into.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeInts
	 */
	default void readInts(Collection<Integer> collection) throws IOException {
		int L = readVarInt();
		for(int i = 0; i < L; i++) collection.add(readInt());
	}
/*	
	default void readByteArrays(Collection<byte[]> collection, int l) throws IOException {
		int L = readVarInt();
		for(int i = 0; i < L; i++) collection.add(readBytes(l));
	}
*/	

	/**
	 * Reads an <code>Enum</code>.
	 *
	 * @param <E> The type of the enum.
	 * @param c The class of the enum.
	 * @return The enum read.
	 * @throws IOException if there was a problem reading the data.
	 * @see VarOutput#writeEnum
	 */
	default <E extends Enum> E readEnum(Class<E> c) throws IOException {
		return c.getEnumConstants()[readVarInt()];
	}

	/**
	* Concatenates a list of byte arrays into a single byte array.
	*
	* @param chunks The byte arrays to concatenate into a single byte array.
	* @return The concatenated byte array.
	* @throws RuntimeException if there was a problem concatenating the byte arrays.
	*/
	default  byte[] concat(byte[]... chunks) {
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for(byte[] chunk : chunks) baos.write(chunk);
			return baos.toByteArray();
		}catch(IOException e){
			// shouldn't happen
			throw new RuntimeException("WTF");
		}
	}
}

// Wraps a DataInput instance into a VarInput instance
class InputWrapper {
	private static Set<Method> methods = new HashSet<>();
	static {
		for(Method m : DataInput.class.getDeclaredMethods()) methods.add(m);
	}
	private InputWrapper(){}
		
	static VarInput wrap(final DataInput inner){
		return (VarInput)Proxy.newProxyInstance(
			VarInput.class.getClassLoader(),
			new Class<?>[] {VarInput.class},
            new InvocationHandler() {
            	public Object invoke(Object proxy, Method method, Object[] args) {
               		try{
               			return methods.contains(method) ? 
                           	method.invoke(inner, args) :
                           method.invoke(proxy, args);
                    }catch(Exception e){
                       	e.printStackTrace();
                       throw new RuntimeException(e);
                    }
                }
            });
        }
    }
    
