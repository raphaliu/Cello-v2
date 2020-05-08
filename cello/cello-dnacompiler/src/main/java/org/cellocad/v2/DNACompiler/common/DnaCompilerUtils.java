/*
 * Copyright (C) 2019 Boston University (BU)
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

package org.cellocad.v2.DNACompiler.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * The DNACompilerUtils class is class with utility methods for the <i>DNACompiler</i> application.
 *
 * @author Timothy Jones
 * @date 2019-02-20
 */
public class DnaCompilerUtils {

  /**
   * Gets the location of a resource as a {@link URL} object.
   *
   * @param resource A resource name.
   * @return The resource location as a {@link URL} object.
   */
  public static URL getResource(final String resource) {
    URL rtn = null;
    rtn = DnaCompilerUtils.class.getClassLoader().getResource(resource);
    return rtn;
  }

  /**
   * Reads the given resource as a string.
   *
   * @param resource A resource name.
   * @return The given resource as a string.
   * @throws IOException Unable to load the given resource.
   */
  public static String getResourceAsString(final String resource) throws IOException {
    String rtn = "";
    final InputStream is = DnaCompilerUtils.getResource(resource).openStream();
    final InputStreamReader isr = new InputStreamReader(is);
    final BufferedReader br = new BufferedReader(isr);
    final StringBuffer sb = new StringBuffer();
    String line;
    while ((line = br.readLine()) != null) {
      sb.append(line);
    }
    br.close();
    isr.close();
    is.close();
    rtn = sb.toString();
    return rtn;
  }

  /**
   * Returns the configuation for the <i>DNACompiler</i> application.
   *
   * @return The configuation for the <i>DNACompiler</i> application.
   * @throws IOException Unable to read application configuration.
   */
  public static String getApplicationConfiguration() throws IOException {
    String rtn = "";
    rtn = DnaCompilerUtils.getResourceAsString("Configuration.json");
    return rtn;
  }
}
