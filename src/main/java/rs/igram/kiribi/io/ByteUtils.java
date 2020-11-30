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
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * This class contains various static utility functions to manipulate byte arrays.
 *
 * @author Michael Sargent
 */
public abstract class ByteUtils {	
	private ByteUtils() {}
	
	/**
	 * Reverses the order of bytes in a byte array.
	 *
	 * @param b The source byte arrays.
	 * @return A byte array containing the bytes of the source byte array in reverse order.
	 */		
	public static byte[] reverse(byte[] b) {
		final int l = b.length;
		final byte[] buf = new byte[l];
		for (int i = 0; i < l; i++) buf[i] = b[l - 1 - i];
		return buf;
	}
	
	/**
	 * Concatenates an array of byte arrays into a single byte array.
	 *
	 * @param chunks The array of byte arrays to concatenate.
	 * @return The concatenated byte array.
	 * @see concat(List)
	 */	
	public static byte[] concat(byte[]... chunks) {
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for(byte[] chunk : chunks) baos.write(chunk);
			return baos.toByteArray();
		}catch(IOException e){
			// shouldn't happen
			throw new RuntimeException("WTF");
		}
	}
	
	/**
	 * Concatenates an list of byte arrays into a single byte array.
	 *
	 * @param chunks The list of byte arrays to concatenate.
	 * @return The concatenated byte array.
	 * @see concat(byte[]...)
	 */	
	public static byte[] concat(List<byte[]> chunks) {
		try{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			for(byte[] chunk : chunks) baos.write(chunk);
			return baos.toByteArray();
		}catch(IOException e){
			// shouldn't happen
			throw new RuntimeException("WTF");
		}
	}
	
	/**
	 * Performs logical <b>or</b> on two byte arrays.
	 *
	 * @param a The target byte arrays.
	 * @param b The test byte arrays.
	 * @param L The number of bytes to logically <b>or</b>.
	 * @throws ArrayIndexOutOfBoundsException if the length of a or b is less than L.
	 * @see and
	 * @see xor
	 * @see matches
	 */	
	public static void or(byte[] a, byte[] b, int L){
		for(int i = 0; i < L; i++) a[i] |= b[i];
	}
	
	/**
	 * Performs logical <b>and</b> or on two byte arrays.
	 *
	 * @param a The target byte arrays.
	 * @param b The test byte arrays.
	 * @param L The number of bytes to logically <b>and</b>.
	 * @throws ArrayIndexOutOfBoundsException if the length of a or b is less than L.
	 * @see or
	 * @see xor
	 * @see matches
	 */	
	public static void and(byte[] a, byte[] b, int L){
		for(int i = 0; i < L; i++) a[i] &= b[i];
	}
	
	/**
	 * Performs logical <b>xor</b> on two byte arrays.
	 *
	 * @param a The target byte arrays.
	 * @param b The test byte arrays.
	 * @param L The number of bytes to logically <b>xor</b>.
	 * @return A byte array containg the results of logically <b>xor</b> <code>a</code> and <code>b</code>.
	 * @throws ArrayIndexOutOfBoundsException if the length of a or b is less than L.
	 * @see or
	 * @see and
	 * @see matches
	 */		
	public static byte[] xor(byte[] a, byte[] b, int L){
		byte[] c = new byte[L];
		for(int i = 0; i < L; i++) c[i] = (byte)(a[i] ^ b[i]);
		return c;
	}

	// a & b = b	
	/**
	 * Returns <code>true</code> if for each of the first L bytes the followings holds: <code>a[i] &amp; b[i] = b[i]</code>,
	 * <code>false</code> otherwise.
	 *
	 * @param a The target byte arrays.
	 * @param b The test byte arrays.
	 * @param L The number of bytes to test.
	 * @return <code>true</code> if for each of the first L bytes the followings holds: <code>a[i] &amp; b[i] = b[i]</code>,
	 * <code>false</code> otherwise.
	 * @throws ArrayIndexOutOfBoundsException if the length of a or b is less than L.
	 * @see or
	 * @see and
	 * @see xor
	 */		
	public static boolean matches(byte[] a, byte[] b, int L){
		for(int i = 0; i < L; i++) if((a[i] & b[i]) != b[i]) return false;
		return true;
	}

	/**
	 * Returns a byte array containing the first L bytes of the provided array.
	 *
	 * @param data The byte arrays to crop.
	 * @param L The number of bytes to copy.
	 * @return a byte array containing the first L bytes of the provided array.
	 * @throws ArrayIndexOutOfBoundsException if the length of data is less than L.
	 */		
	public static byte[] crop(byte[] data, int L){
		final byte[] b = new byte[L];
		System.arraycopy(data, 0, b, 0, L);
		return b;
	}
		
	/**
	 * Returns the var size of a <code>long</code>.
	 *
	 * @param v The <code>long</code> whose size is to be determined.
	 * @return the var size of a <code>long</code>.
	 * @throws IllegalArgumentException if the provided <code>long</code> is negative.
	 */		
	public static int varSize(long v) {
		if(v < 0l) throw new IllegalArgumentException("Input must be non-negative: "+v);
		if(v < 0xFD) return 1;
		if(v <= 0xFFFF) return 2;
		if(v <= 0xFFFFFFFF) return 4;
		return 8;
	}
	
	/**
	 * Copies the bytes of an <code>int</code> into a byte array.
	 *
	 * @param b The byte array into which to copy the <code>int</code>.
	 * @param pos The position in the byte array to copy the <code>int</code>.
	 * @param v The <code>int</code> to copy.
	 */	        	
	public static void put(byte[] b, int pos, int v) {
		ByteBuffer buf = ByteBuffer.wrap(b);
		buf.putInt(pos, v);
	}
	
	/**
	 * Copies the bytes of a <code>long</code> into a byte array.
	 *
	 * @param b The byte array into which to copy the <code>long</code>.
	 * @param pos The position in the byte array to copy the <code>long</code>.
	 * @param v The <code>long</code> to copy.
	 */		
	public static void put(byte[] b, int pos, long v) {
		ByteBuffer buf = ByteBuffer.wrap(b);
		buf.putLong(pos, v);
	}
	
	/**
	 * Converts <code>long</code> to a byte array.
	 *
	 * @param v The <code>long</code> to convert.
	 * @return a byte array containing the <code>long</code>.
	 */	        
	public static byte[] longToBytes(long v) {
		byte[] b = new byte[8];
		put(b, 0, v);
		return b;
	}
	
	/**
	 * Extracts a <code>long</code> from a byte array.
	 *
	 * @param b The source byte array.
	 * @return a <code>long</code> from the provided byte array.
	 */	  	
	public static long bytesToLong(byte[] b) {
		return getLong(b, 0);
	}
	
	/**
	 * Extracts an <code>int</code> from a byte array.
	 *
	 * @param b The source byte array.
	 * @param pos The position in the source byte array to begin the extraction.
	 * @return an <code>int</code> from the provided byte array.
	 */		
	public static int getInt(byte[] b, int pos) {
		ByteBuffer buf = ByteBuffer.wrap(b);
		return buf.getInt(pos);
	}
	
	/**
	 * Extracts a <code>long</code> from a byte array.
	 *
	 * @param b The source byte array.
	 * @param pos The position in the source byte array to begin the extraction.
	 * @return a <code>long</code> from the provided byte array.
	 */	  
	public static long getLong(byte[] b, int pos) {
		ByteBuffer buf = ByteBuffer.wrap(b);
		return buf.getLong(pos);
	}
	
	/**
	 * Copies the bytes of a <code>SocketAddress</code> into a byte array.
	 *	 
	 * @param b The byte array into which to copy the <code>SocketAddress</code>.
	 * @param offset The position in the byte array to copy the <code>SocketAddress</code>.
	 * @param address The <code>SocketAddress</code> to copy.
	 * @see inet(byte[], int)
	 */			
	public static void inet(byte[] b, int offset, SocketAddress address) {
		InetSocketAddress inet = ((InetSocketAddress)address);
		if(inet.getAddress() instanceof Inet6Address){
			System.arraycopy(inet.getAddress().getAddress(), 0, b, offset, 16);
		}else{
			b[offset] = (byte)0xff;
			b[offset + 1] = (byte)0xff;
			System.arraycopy(inet.getAddress().getAddress(), 0, b, offset + 2, 4);
			for(int i = offset + 6; i < offset + 16; i++) b[i] = 0;
		}
		put(b, offset + 16, inet.getPort());
	}
	    
	/**
	 * Extracts a <code>SocketAddress</code> from a byte array.
	 *
	 * @param src The source byte array.
	 * @param offset The position in the source byte array to begin the extraction.
	 * @return a <code>SocketAddress</code> from the provided byte array.
	 * @throws UnknownHostException if there was a problem performing the operation.
	 * @see inet(byte[], int, SocketAddress)
	 */	    				
	public static SocketAddress inet(byte[] src, int offset) throws UnknownHostException {
		byte[] inet = null;
		if(src[offset] == (byte)0xff && src[offset + 1] == (byte)0xff){
			inet = extract(src, offset + 2, 4);
		}else{
			inet = extract(src, offset, 16);
		}
		InetAddress add = InetAddress.getByAddress(inet);
		int port = getInt(src, offset + 16);
		return new InetSocketAddress(add, port);
	}
    
	/**
	 * Extracts a byte array from a byte array.
	 *
	 * @param src The source byte array.
	 * @param pos The position in the source byte array to begin the extraction.
	 * @param length The length of the byte array to extract.
	 * @return a byte array from the provided byte array.
	 */	    		
	public static byte[] extract(byte[] src, int pos, int length) {
		byte[] dst = new byte[length];
		System.arraycopy(src, pos, dst, 0, length);
		return dst;
	}
	
	// unsigned int as long	
	/**
	 * Copies the bytes of a <code>long</code> representing an unsigned int into a byte array.
	 *	 
	 * @param l The <code>long</code> to copy.
	 * @param buf The byte array into which to copy the <code>long</code>.
	 * @param offset The position in the byte array to copy the <code>long</code>.
	 */		
	public static void unsignedIntToBytes(long l, byte[] buf, int offset) {
        buf[offset] = (byte) ((l & 0xFF000000L) >>> 24);
        buf[offset + 1] = (byte) ((l & 0x00FF0000L) >>> 16);
        buf[offset + 2] = (byte) ((l & 0x0000FF00L) >>> 8);
        buf[offset + 3] = (byte) ((l & 0x000000FFL));
    }

	// unsigned int as long    
	/**
	 * Converts an unsigned int to a byte array.
	 *
	 * @param l The unsigned int to convert.
	 * @return a byte array containing the unsigned int.
	 */	        
	public static byte[] unsignedIntToBytes(long l) {
		byte[] b = new byte[4];
		unsignedIntToBytes(l, b, 0);
		return b;
    }
    
	/**
	 * Extracts an unsigned int from a byte array.
	 *
	 * @param buf The byte array to extract from.
	 * @param offset The offset in the byte array to extract from.
	 * @return an unsigned int as a <code>long</code>.
	 */	        
    public static long bytesToUnsignedInt(byte[] buf, int offset) {
    	int l1, l2, l3, l4;
    	l1 = (0x000000FF & ((int)buf[offset]));
        l2 = (0x000000FF & ((int)buf[offset+1]));
        l3 = (0x000000FF & ((int)buf[offset+2]));
        l4 = (0x000000FF & ((int)buf[offset+3]));
        
        long l= ((long) (l1 << 24 | l2 << 16 | l3 << 8 | l4)) & 0xFFFFFFFFL;
        return l;
    }
    
	/**
	 * Returns an int array from a byte array.
	 *
	 * @param b The byte array to convert.
	 * @return an int array from the provided byte array.
	 */	    
    public static int[] ints(byte[] b) {
    	if(b.length % 4 != 0) throw new IllegalArgumentException();
    	IntBuffer buf =  ByteBuffer.wrap(b).asIntBuffer();
    	int L = b.length / 4;
    	int[] ints = new int[L];
    	for(int i = 0; i < L; i++) ints[i] = buf.get();
    	return ints;
    }
    
	/**
	 * Returns a byte array from a char array.
	 *
	 * @param chars The char array to convert.
	 * @return a byte array from the provided char array.
	 */	    
    public static byte[] bytes(char... chars) {
    	ByteBuffer buf = ByteBuffer.allocate(2 * chars.length);
    	for(int i = 0; i < chars.length; i++){
    		buf.putChar(chars[i]);
    	}
    	
    	return buf.array();
    }

	/**
	 * Returns a byte array from an int array.
	 *
	 * @param ints The int array to convert.
	 * @return a byte array from the provided int array.
	 */		   
    public static byte[] bytes(int... ints) {
    	ByteBuffer buf = ByteBuffer.allocate(4 * ints.length);
    	for(int i = 0; i < ints.length; i++){
    		buf.putInt(ints[i]);
    	}
    	
    	return buf.array();
    }

	/**
	 * Returns a <code>CharSquence</code> from an int array.
	 *
	 * @param ints The int array to convert.
	 * @return a <code>CharSquence</code> from an int array.
	 */		    
    public static CharSequence charseq(int... ints) {
    	ByteBuffer buf = ByteBuffer.allocate(4 * ints.length);
    	for(int i = 0; i < ints.length; i++){
    		buf.putInt(ints[i]);
    	}
    	
    	return buf.asCharBuffer();
    }

	/**
	 * Returns a char array from an int array.
	 *
	 * @param ints The int array to convert.
	 * @return a char array from the provided int array.
	 */		
    public static char[] chars(int... ints) {
    	ByteBuffer buf = ByteBuffer.allocate(4 * ints.length);
    	for(int i = 0; i < ints.length; i++){
    		buf.putInt(ints[i]);
    	}
    	
    	CharBuffer cb = buf.asCharBuffer();
    	
    	int L = cb.length();
    	char[] chars = new char[L];
    	for(int i = 0; i < L; i++) chars[i] = cb.get();
    	return chars;
    }
}
