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
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;

/**
 * Concrete implementation of VarOutput
 *
 * @author Michael Sargent
 */
public class VarOutputStream extends DataOutputStream implements VarOutput {
	/**
	 * Initializes a newly created <code>VarOutputStream</code> object
	 * so that it writes to provided <code>VarOutput</code>.
	 *
	 * @param os The output stream to write to.
	 */
	public VarOutputStream(OutputStream os) {
		super(os);
	}
	
	/**
	 * Initializes a newly created <code>VarOutputStream</code> object
	 * so that it writes to an internal <code>ByteArrayOutputStream</code>. The 
	 * byte array can be accesed by calling <code>toByteArray()</code>
	 */	
	public  VarOutputStream() {
		super(new ByteArrayOutputStream());
	}

	/**
	 * Returns the byte array written to if this instance was not initialized with a <code>OutputStream</code>.
	 *
	 * @return The byte array written to.
	 * @throws IllegalStateException If this instance was initialized with a <code>OutputStream</code>.
	 */
	public byte[] toByteArray() {
		if(! (out instanceof ByteArrayOutputStream)) throw new IllegalStateException("Not in byte array mode");
		try{
			flush();
			close();
		}catch(IOException e){
			// shouldn't happen - ignore
		}
		return ((ByteArrayOutputStream)out).toByteArray();
	}
	
	/**
	 * Convenience method to convert a <code>long</code> as a VarInt to a byte array.
	 * <p>This is equivalent to:
	 * 
	 * <pre>
	 * VarOutputStream out = new VarOutputStream();
	 * out.writeVarInt(l);
	 * return out.toByteArray();
	 * </pre>
	 *
	 * @param l The <code>long</code> to write.
	 * @return A byte array containing the byte of the <code>long</code> as a VarInt.
	 * @throws IOException If there was a problem writing the data.
	 */	
	public static byte[] varIntToBytes(long l) throws IOException {
		VarOutputStream out = new VarOutputStream();
		out.writeVarInt(l);
		return out.toByteArray();
	}	
}
