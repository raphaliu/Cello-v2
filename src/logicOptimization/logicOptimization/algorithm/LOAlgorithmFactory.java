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
package logicOptimization.algorithm;

import common.algorithm.AlgorithmFactory;
import logicOptimization.algorithm.maxFanout.maxFanout;

/**
 * The LOAlgorithmFactory is an algorithm factory for the <i>logicOptimization</i> stage. 
 * 
 * @author Vincent Mirian
 * 
 * @date 2018-05-21
 *
 */
public class LOAlgorithmFactory extends AlgorithmFactory<LOAlgorithm>{

	/**
	 *  Returns the LOAlgorithm that has the same name as the parameter <i>name</i> within this instance
	 *  
	 *  @param name string used for searching the LOAlgorithmFactory
	 *  @return LOAlgorithm instance if the LOAlgorithm exists within the LOAlgorithmFactory, otherwise null
	 */
	@Override
	protected LOAlgorithm getAlgorithm(final String name) {
		LOAlgorithm rtn = null;
		if (name.equals("maxFanout")){
			rtn = new maxFanout();
		}
		return rtn;
	}
	
}
