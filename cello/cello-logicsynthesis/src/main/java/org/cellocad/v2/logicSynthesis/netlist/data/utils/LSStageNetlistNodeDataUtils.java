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

import org.cellocad.v2.logicSynthesis.netlist.data.LSStageNetlistNodeData;
import org.cellocad.v2.results.netlist.Netlist;
import org.cellocad.v2.results.netlist.NetlistNode;

/**
 * The LSStageNetlistNodeDataUtils class is class with utility methods for LSStageNetlistNodeData
 * instances in the <i>logicSynthesis</i> stage.
 *
 * @author Vincent Mirian
 * @date 2018-05-21
 */
public class LSStageNetlistNodeDataUtils {

  /**
   * Resets the stage data for all nodes in the netlist instance defined by parameter {@code
   * netlist}.
   *
   * @param netlist The {@link Netlist}.
   */
  public static void resetStageNetlistNodeData(final Netlist netlist) {
    for (int i = 0; i < netlist.getNumVertex(); i++) {
      final NetlistNode node = netlist.getVertexAtIdx(i);
      LSStageNetlistNodeDataUtils.resetStageNetlistNodeData(node);
    }
  }

  /**
   * Resets the stage data for a NetlistNode instance.
   *
   * @param node The {@link NetlistNode}.
   */
  public static void resetStageNetlistNodeData(final NetlistNode node) {
    final LSStageNetlistNodeData data = new LSStageNetlistNodeData();
    node.setStageNetlistNodeData(data);
  }
}
