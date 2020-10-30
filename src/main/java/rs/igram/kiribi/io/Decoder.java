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
import java.io.UncheckedIOException;

/**
 * Functional interface used to decode encoded <code>Encodable</code> objects.
 *
 * <p>Typically, this is used as follows:
 *
 * <pre>
 * public Class Foo implements Encodeable {
 *     public Foo() {}
 *
 *     public Foo(VarInput in) throws IOException {
 *         ...
 *     }
 *
 *     &#64;Override
 *     public void write(VarOutput out) throws IOException {
 *         ...
 *     }
 *     ...
 * }
 *
 * ....
 *
 * VarInput in = ...
 * Foo = in.read(Foo::new);
 * </pre>
 * 
 * @param <T> the type
 * @see Encodable
 * @see VarInput
 * @author Michael Sargent
 */
@FunctionalInterface
public interface Decoder<T> {
	/**
	 * Reads an object of type <code>T</code> from a <code>VarInput</code>.
	 *
	 * @param in The <code>VarInput</code> to read from.
	 * @return An object of type <code>T</code>.
	 * @throws IOException If there was a problem reading the data.
	 * @see VarInput
	 */	
	T read(VarInput in) throws IOException;

	/**
	 * Reads an object of type <code>T</code> from a <code>VarInput</code>.
	 *
	 * @param in The <code>VarInput</code> to read from.
	 * @return An object of type <code>T</code>.
	 * @throws  UncheckedIOException Throws an UncheckedIOException wrapping the original IOException.
	 * @see VarInput
	 */	
	default T readUnchecked(VarInput in) {
		try{
			return read(in);
		}catch(IOException e){
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Reads an object of type <code>T</code> from a byte array.
	 *
	 * @param b The byte array to read from.
	 * @return An object of type <code>T</code>.
	 * @throws IOException If there was a problem reading the data.
	 */	
	default T decode(byte[] b) throws IOException {
		return read(new VarInputStream(b));
	}

	/**
	 * Reads an object of type <code>T</code> from a byte array.
	 *
	 * @param b The byte array to read from.
	 * @return An object of type <code>T</code>.
	 * @throws UncheckedIOException Throws an UncheckedIOException wrapping the original IOException.
	 */	
	default T decodeUnchecked(byte[] b) {
		return readUnchecked(new VarInputStream(b));
	}
}