/**
 * Copyright (C) 2017 Massachusetts Institute of Technology (MIT)
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package partitioning.netlist.data.utils;

import partitioning.netlist.data.PTStageNetlistNodeData;
import results.netlist.Netlist;
import results.netlist.NetlistNode;


/**
 * The PTStageNetlistNodeDataUtils class is class with utility methods for PTStageNetlistNodeData instances in the <i>partitioning</i> stage.
 * 
 * @author Vincent Mirian
 * 
 * @date 2018-05-21
 *
 */
public class PTStageNetlistNodeDataUtils {

	/**
	 * Resets the stage data for all nodes in the netlist instance defined by parameter <i>netlist</i>
	 *
	 * @param netlist the Netlist
	 *
	 */
	static public void resetStageNetlistNodeData(Netlist netlist){
		for (int i = 0; i < netlist.getNumVertex(); i++) {
			NetlistNode node = netlist.getVertexAtIdx(i);
			PTStageNetlistNodeDataUtils.resetStageNetlistNodeData(node);
		}
	}

	/**
	 * Resets the stage data for a NetlistNode instance
	 *
	 * @param node the NetlistNode
	 *
	 */
	static public void resetStageNetlistNodeData(NetlistNode node){
		PTStageNetlistNodeData data = new PTStageNetlistNodeData();
		node.setStageNetlistNodeData(data);
	}
	
}
