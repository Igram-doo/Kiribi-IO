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

/**
 * Interface supporting reading and writing of byte arrays.
 *
 * @author Michael Sargent
 */
public interface ByteStream {
	/**
	 * Writes a byte array to the stream.
	 *
	 * @param b The byte array to be written.
	 * @throws IOException if there was a problem writing the byte array.
	 */	
	void write(byte[] b) throws IOException;

	/**
	 * Reads a byte array from the stream.
	 *
	 * @return The byte array read.
	 * @throws IOException if there was a problem reading the byte array.
	 */
	byte[] read() throws IOException;

	/**
	 * Creates a new <code>ByteStream</code> instance from the provided
	 * <code>VarInputStream</code> and <code>VarOutputStream</code>.
	 *
	 * @param in The VarInputStream to read from.
	 * @param out The VarOutputStream to write to.
	 * @return A new ByteStream instance.
	 *
	 * @see VarInputStream
	 * @see VarOutputStream
	 */	
	static ByteStream stream(VarInputStream in, VarOutputStream out) {
		return new IOStream(in, out);
	}
}
		
// Concrete implementation of ByteStream
class IOStream implements ByteStream {
	VarInputStream in;
	VarOutputStream out;
		
	IOStream(VarInputStream in, VarOutputStream out) {
		this.in = in;
		this.out = out;
	}
		
	@Override
	public void write(byte[] b) throws IOException {
		out.writeBytes(b);
		out.flush();
	}

	@Override
	public byte[] read() throws IOException {
		return in.readBytes();
	}
}
