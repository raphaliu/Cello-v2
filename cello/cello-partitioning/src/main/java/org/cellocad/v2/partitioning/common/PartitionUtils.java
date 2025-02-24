/*
 * Copyright (C) 2017 Massachusetts Institute of Technology (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.cellocad.v2.partitioning.common;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Utility methods for paritions.
 *
 * @author Vincent Mirian
 * @date Oct 31, 2017
 */
public class PartitionUtils {

  /**
   * Writes the Partition defined by parameter {@code p} in DOT (graph description language) format
   * to the file defined by {@code filename}.
   *
   * @param p The Partition.
   * @param filename The file.
   */
  public static void writeDotFileForPartition(final Partition p, final String filename) {
    try {
      final OutputStream outputStream = new FileOutputStream(filename);
      final Writer outputStreamWriter = new OutputStreamWriter(outputStream);
      p.printDot(outputStreamWriter);
      outputStreamWriter.close();
      outputStream.close();
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}
