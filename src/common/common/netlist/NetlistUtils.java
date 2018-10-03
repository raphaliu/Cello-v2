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
package common.netlist;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import common.Utils;
import common.JSON.JSONUtils;
import common.runtime.environment.RuntimeEnv;

/**
 * The NetlistUtils class is class with utility methods for <i>Netlist</i> instances.
 * 
 * @author Vincent Mirian
 * 
 * @date Nov 21, 2017
 *
 */
public class NetlistUtils {

	/**
	 *  Initializes a newly created Netlist using the RuntimeEnv, <i>runEnv</i>, and,
	 *  the string referencing command line argument for the inputNetlist file, <i>inputNetlist</i>.
	 *  
	 *  @param runEnv the RuntimeEnv to extract the inputNetlist file, <i>inputNetlist</i>
	 *  @param inputNetlist the string referencing command line argument for the inputNetlist file
	 *  @return the Netlist if created successfully, otherwise null
	 *  @throws RuntimeException if: <br>
	 *  Any of the parameters are null<br>
	 *  Error accessing <i>inputNetlist</i><br>
	 *  Error parsing <i>inputNetlist</i><br>
	 */
	static public Netlist getNetlist(final RuntimeEnv runEnv, final String inputNetlist){
		Utils.isNullRuntimeException(runEnv, "runEnv");
		Utils.isNullRuntimeException(inputNetlist, "inputNetlist");
		Netlist rtn = null;
		String inputNetlistFilename = runEnv.getOptionValue(inputNetlist);
	    Reader inputNetlistReader = null;
		JSONObject jsonTop = null;
		// Create File Reader
		try {
			inputNetlistReader = new FileReader(inputNetlistFilename);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Error with file: " + inputNetlistFilename);
		}
		// Create JSON object from File Reader
		JSONParser parser = new JSONParser();
        try{
        	jsonTop = (JSONObject) parser.parse(inputNetlistReader);
	    } catch (IOException e) {
	        throw new RuntimeException("File IO Exception for: " + inputNetlistFilename + ".");
	    } catch (ParseException e) {
	        throw new RuntimeException("Parser Exception for: " + inputNetlistFilename + ".");
	    }
		// Create TargetInfo object
        rtn = new Netlist(jsonTop);
	    try {
	    	inputNetlistReader.close();
		} catch (IOException e) {
			throw new RuntimeException("Error with file: " + inputNetlistFilename);
		}
	    return rtn;
	}

	/**
	 *  Writes the netlist defined by parameter <i>netlist</i> to file defined by <i>filename</i>
	 *  
	 *  @param netlist the netlist
	 *  @param filename the file to write the netlist
	 *  @throws RuntimeException if: <br>
	 *  Any of the parameters are null<br>
	 */
	static public void writeJSONForNetlist(final Netlist netlist, final String filename){
		Utils.isNullRuntimeException(netlist, "netlist");
		Utils.isNullRuntimeException(filename, "filename");
		try {
			OutputStream outputStream = new FileOutputStream(filename);
			Writer outputStreamWriter = new OutputStreamWriter(outputStream);
			outputStreamWriter.write(JSONUtils.getStartEntryString());
			netlist.writeJSON(1, outputStreamWriter);
			outputStreamWriter.write(JSONUtils.getEndEntryString());
			outputStreamWriter.close();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
