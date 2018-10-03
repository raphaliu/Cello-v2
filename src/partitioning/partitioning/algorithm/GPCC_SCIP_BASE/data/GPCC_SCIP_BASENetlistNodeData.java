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
package partitioning.algorithm.GPCC_SCIP_BASE.data;

import java.io.IOException;
import java.io.Writer;

import partitioning.algorithm.GPCC_BASE.data.GPCC_BASENetlistNodeData;

/**
 * The GPCC_SCIP_BASENetlistNodeData class contains all data for a node used within the <i>GPCC_SCIP_BASE</i> algorithm class of the <i>partitioning</i> stage.
 * 
 * @author Vincent Mirian
 * 
 * @date 2018-05-21
 *
 */
public class GPCC_SCIP_BASENetlistNodeData extends GPCC_BASENetlistNodeData{

	private void setDefault() {
	}
	
	/**
	 *  Initializes a newly created GPCC_SCIP_BASENetlistNodeData
	 */
	public GPCC_SCIP_BASENetlistNodeData(){
		super();
		this.setDefault();
	}

	/**
	 *  Writes this instance in JSON format to the writer defined by parameter <i>os</i> with the number of indents equivalent to the parameter <i>indent</i>
	 *  @param indent the number of indents
	 *  @param os the writer
	 *  @throws IOException If an I/O error occurs
	 */
	public void writeJSON(int indent, final Writer os) throws IOException {
		
	}

}
