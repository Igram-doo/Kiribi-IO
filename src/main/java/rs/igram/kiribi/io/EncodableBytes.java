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
import java.util.Arrays;

public class EncodableBytes implements Encodable {
	private final byte[] bytes;
   	   
	/**
	 * Initializes a newly created <code>EncodableBytes</code> object
	 *  from the provided byte array.
	 *
	 * @param bytes The byte array to initialize from.
	 */
   	public EncodableBytes(byte[] bytes) {
   		this.bytes = ByteUtils.copy(bytes);
   	}
   	      	
	/**
	 * Initializes a newly created <code>EncodableBytes</code> object
	 * so that it reads from the provided <code>VarInput</code>.
	 *
	 * @param in The input stream to read from.
	 * @throws IOException if there was a problem reading from the provided 
	 * <code>VarInputStream</code>.
	 */
   	public EncodableBytes(VarInput in) throws IOException {
   		bytes = in.readBytes();
   	}
   	  
   	/**
	 * Returns a copy of the byte array.
	 *
	 * @return A copy of the byte array.
	 */
   	public byte[] bytes() {
   		return ByteUtils.copy(bytes);
   	}
   	
   	@Override
    public void write(VarOutput out) throws IOException {
    	out.writeBytes(bytes);
   	}
   	   
   	@Override
   	public int hashCode() {return Arrays.hashCode(bytes);}
   	
   	@Override   	
   	public boolean equals(Object o) {
   		if(o == null || !(o instanceof EncodableBytes)) return false;
   		var t = (EncodableBytes)o;
   		return Arrays.equals(bytes, t.bytes);
   	}
}