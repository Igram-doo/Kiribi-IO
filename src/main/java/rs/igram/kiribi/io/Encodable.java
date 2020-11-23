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
 * Classes which wish to be encoded with this framework should implement this interface.
 *
 * @author Michael Sargent
 */
public interface Encodable {
	/**
	 * This method should be invoked to write the object's persistent data.
	 *
	 * @param out The <code>VarOutput</code> to write the implementing class' persistent data to.
	 * @throws IOException if there was a problem writing the data.
	 */
	void write(VarOutput out) throws IOException;

	/**
	 * This method should be invoked to write the object's persistent data.
	 *
	 * @param out The <code>VarOutput</code> to write the implementing class' persistent data to.
	 * @throws UncheckedIOException an UncheckedIOException wrapping the original IOException.
	 */
	default void writeUnchecked(VarOutput out) {
		try{
			write(out);
		}catch(IOException e){
			throw new UncheckedIOException(e);
		}
	}

	/**
	 * Encodes this object's persistent data into a byte array.
	 *
	 * @return A byte array containing this object's persistent data.
	 * @throws IOException if there was a problem writing the data.
	 */
	default byte[] encode() throws IOException {
		try(VarOutputStream out = new VarOutputStream()){
			write(out);
			return out.toByteArray();
		}
	}


	/**
	 * Encodes this object's persistent data into a byte array.
	 *
	 * @return A byte array containing this object's persistent data.
	 * @throws UncheckedIOException an UncheckedIOException wrapping the original IOException.
	 */
	default byte[] encodeUnchecked() {
		try{
			return encode();
		}catch(IOException e){
			throw new UncheckedIOException(e);
		}
	}

	/**
   	 * Creates a deep copy of this object.
	 *
	 * @param <T> The type of this object.
	 * @param decoder The <code>Decoder</code> for this object.
	 * @return A deep copy of this object.
	 * @throws IOException if there was a problem writing the data.
	 * @see Decoder
	 */
	default <T> T copy(Decoder<T> decoder) throws IOException {
		try(VarInputStream in = new VarInputStream(encode())){
			return decoder.read(in);
		}
	}

	/**
   	 * Creates a deep copy of this object.
	 *
	 * @param <T> The type of this object.
	 * @param decoder The <code>Decoder</code> for this object.
	 * @return A deep copy of this object.
	 * @throws UncheckedIOException an UncheckedIOException wrapping the original IOException.
	 * @see Decoder
	 */
	default <T> T copyUnchecked(Decoder<T> decoder) {
		try(VarInputStream in = new VarInputStream(encode())){
			return decoder.read(in);
		}catch(IOException e){
			throw new UncheckedIOException(e);
		}
	}
}
