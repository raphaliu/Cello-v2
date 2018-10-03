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
package results.netlist.data;

import java.io.IOException;
import java.io.Writer;

import org.json.simple.JSONObject;

import common.application.data.ApplicationNetlistData;

/**
 * The ResultNetlistData class contains all the data for a netlist used within the project.
 * 
 * @author Vincent Mirian
 * 
 * @date 2018-05-21
 *
 */
public class ResultNetlistData extends ApplicationNetlistData{

	private void setDefault() {
	}

	/**
	 *  Initializes a newly created ResultNetlistData 
	 */
	public ResultNetlistData(){
		super();
		this.setDefault();
	}

	/**
	 *  Initializes a newly created ResultNetlistData with its parameters cloned from those of parameter <i>other</i>.
	 *  
	 *  @param other the other ResultNetlistData
	 */
	public ResultNetlistData(ResultNetlistData other){
		super();
		this.setDefault();
	}

	/**
	 *  Initializes a newly created ResultNetlistData using the parameter <i>JObj</i>.
	 *  
	 *  @param JObj the JavaScript Object Notation (JSON) representation of the ResultNetlistData Object
	 */
	public ResultNetlistData(final JSONObject JObj){
		super();
		this.setDefault();
		this.parse(JObj);
	}
	
	/**
	 *  Writes this instance in JSON format to the writer defined by parameter <i>os</i> with the number of indents equivalent to the parameter <i>indent</i>
	 *  @param indent the number of indents
	 *  @param os the writer
	 *  @throws IOException If an I/O error occurs
	 */
	public void writeJSON(int indent, Writer os) throws IOException{
	}

	/**
	 *  Parses the data attached to this instance
	 *  
	 *  @param JObj the JavaScript Object Notation (JSON) representation of the Project NetlistData Object
	 */
	public void parse(final JSONObject JObj){
	}

}
