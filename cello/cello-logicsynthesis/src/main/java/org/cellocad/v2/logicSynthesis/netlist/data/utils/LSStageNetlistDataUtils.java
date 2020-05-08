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

package org.cellocad.v2.logicSynthesis.netlist.data.utils;

import org.cellocad.v2.logicSynthesis.netlist.data.LSStageNetlistData;
import org.cellocad.v2.results.netlist.Netlist;

/**
 * The LSStageNetlistDataUtils class is class with utility methods for LSStageNetlistData instances
 * in the <i>logicSynthesis</i> stage.
 *
 * @author Vincent Mirian
 * @date 2018-05-21
 */
public class LSStageNetlistDataUtils {

  /**
   * Resets the stage data for a netlist instance defined by parameter {@code netlist}.
   *
   * @param netlist The {@link Netlist}.
   */
  public static void resetStageNetlistData(final Netlist netlist) {
    final LSStageNetlistData data = new LSStageNetlistData();
    netlist.setStageNetlistData(data);
  }
}
