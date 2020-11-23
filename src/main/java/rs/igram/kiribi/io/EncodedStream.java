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
 * This interface provides methods to read and write <code>Encodable</code> objects.
 *
 * @see Encodable
 * @see Decoder
 * @author Michael Sargent
 */
public interface EncodedStream {	
	/**
	 * Writes an encodable object.
	 *
	 * @param data The encodable object to write.
	 * @throws IOException if there was a problem writing the data.
	 */
	void write(Encodable data) throws IOException;
	
	/**
	 * Reads an encodable object.
	 *
	 * @param <T> The type of the object to decode.
	 * @param decoder The decoder used to decode the object.
	 * @return The decoded object.
	 * @throws IOException if there was a problem reading the data.
	 */
	<T> T read(Decoder<T> decoder) throws IOException;
}