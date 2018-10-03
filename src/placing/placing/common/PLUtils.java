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
package placing.common;


/**
 * The PLUtils class is class with utility methods for the <i>placing</i> stage.
 * 
 * @author Vincent Mirian
 * 
 * @date 2018-05-21
 *
 */
public class PLUtils {

	/**
	 * Returns the path of the ClassLoader
	 * 
	 * @return the path of the ClassLoader
	 *
	 */
	static public String getFilepath(){
		String rtn = "";
		rtn = PLUtils.class.getClassLoader().getResource(".").getPath();
		return rtn;
	}

	/**
	 * Returns the path of the Resources directory for the <i>placing</i> stage
	 * 
	 * @return the path of the Resources directory for the <i>placing</i> stage
	 *
	 */	
	static public String getResourcesFilepath(){
		String rtn = "";
		rtn += PLUtils.getFilepath();
		rtn += "resources-";
		rtn += "placing";
		return rtn;
	}
	
}
