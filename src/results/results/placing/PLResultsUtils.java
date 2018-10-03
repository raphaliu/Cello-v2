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
package results.placing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import common.CObjectCollection;
import results.logicSynthesis.LSResults;
import results.netlist.Netlist;
import results.netlist.NetlistNode;

/**
 * The PLResultUtils class is class with utility methods for the result of the <i>placing</i> stage.
 * 
 * @author Timothy Jones
 * 
 * @date 2018-05-21
 *
 */
public class PLResultsUtils {

	/**
	 * Returns a CObjectCollection of NetlistNode from the Netlist defined by <i>netlist</i>
	 * 
	 * @param netlist the Netlist
	 * @return a CObjectCollection of NetlistNode from the Netlist defined by <i>netlist</i>
	 */
	public static CObjectCollection<NetlistNode> getLogicNodes(final Netlist netlist){
		CObjectCollection<NetlistNode> rtn = null;
		rtn = new CObjectCollection<NetlistNode>();
		for (int i = 0; i < netlist.getNumVertex(); i++) {
			NetlistNode node = netlist.getVertexAtIdx(i);
			String nodeType = node.getResultNetlistNodeData().getNodeType();
			if (!nodeType.equals(LSResults.S_PRIMARYINPUT)
					&&
					!nodeType.equals(LSResults.S_INPUT)
					&&
					!nodeType.equals(LSResults.S_PRIMARYOUTPUT)
					&&
					!nodeType.equals(LSResults.S_OUTPUT)) {
				rtn.add(node);
			}
		}
		return rtn;
	}
	
}
