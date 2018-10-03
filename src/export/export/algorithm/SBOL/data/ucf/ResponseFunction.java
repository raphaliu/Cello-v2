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
package export.algorithm.SBOL.data.ucf;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.CObject;
import common.CObjectCollection;
import common.profile.ProfileUtils;

/**
 * The ResponseFunction is class representing a Response Function for the gate assignment in the <i>SimulatedAnnealing</i> algorithm.
 * 
 * @author Vincent Mirian
 * 
 * @date 2018-05-21
 *
 */
public class ResponseFunction extends CObject{

	private void parseGateName(final JSONObject JObj){
		String value = ProfileUtils.getString(JObj, "gate_name");
		this.setGateName(value);
	}
	
	private void parseEquation(final JSONObject JObj){
		String value = ProfileUtils.getString(JObj, "equation");
		this.setEquation(value);
	}

	private void parseVariables(final JSONObject JObj){
		CObjectCollection<ResponseFunctionVariable> variables = this.getVariables();
		JSONArray jArr = (JSONArray) JObj.get("variables");
		for (int i = 0; i < jArr.size(); i++) {
			JSONObject jObj = (JSONObject) jArr.get(i);
			ResponseFunctionVariable variable = new ResponseFunctionVariable(jObj);
			variables.add(variable);
		}
	}
	
	private void parseParameters(final JSONObject JObj){
		Map<String, Double> parameters = this.getParameters();
		JSONArray jArr = (JSONArray) JObj.get("parameters");
		for (int i = 0; i < jArr.size(); i++) {
			JSONObject jObj = (JSONObject) jArr.get(i);
			String name = ProfileUtils.getString(jObj, "name");
			if (name == null) {
				continue;
			}
			Double value = ProfileUtils.getDouble(jObj, "value");
			if (value == null) {
				continue;
			}
			parameters.put(name, value);
		}
	}
		
	private void parseResponseFunction(final JSONObject jObj) {
		this.parseGateName(jObj);
		this.parseEquation(jObj);
		this.parseVariables(jObj);
		this.parseParameters(jObj);
    }
	
	private void init() {
		this.parameters = new HashMap<String, Double>();
		this.variables = new CObjectCollection<ResponseFunctionVariable>();
	}
	
	public ResponseFunction(final JSONObject jobj) {
		this.init();
		this.parseResponseFunction(jobj);
	}

	/*
	 * GateName
	 */
	private void setGateName(final String gateName){
		this.setName(gateName);
	}
	
	public String getGateName(){
		return this.getName();
	}
	
	/*
	 * Gate
	 */
	public void setGate(final Gate gate) {
		this.gate = gate;
	}
	
	public Gate getGate() {
		return this.gate;
	}
	
	private Gate gate;
	
	/*
	 * Equation
	 */
	private void setEquation(final String equation){
		this.equation = equation;
	}
	
	public String getEquation(){
		return this.equation;
	}
	
	private String equation;
	
	/*
	 * ResponseFunctionVariable
	 */
	public ResponseFunctionVariable getVariableByName(final String name){
		return this.getVariables().findCObjectByName(name);
	}
	
	public ResponseFunctionVariable getVariableAtIdx(final int index){
		ResponseFunctionVariable rtn = null;
		if (
				(0 <= index)
				&&
				(index < this.getNumVariable())
				) {
			rtn = this.getVariables().get(index);
		}
		return rtn;
	}
	
	public int getNumVariable(){
		return this.getVariables().size();
	}
	
	private CObjectCollection<ResponseFunctionVariable> getVariables(){
		return this.variables;
	}
	
	private CObjectCollection<ResponseFunctionVariable> variables;
	
	/*
	 * Parameter
	 */
	public Double getParameterValueByName(final String name) {
		Double rtn = null;
		if (name != null) {
			rtn = this.getParameters().get(name);
		}
		return rtn;
	}
	
	private Map<String, Double> getParameters(){
		return this.parameters;
	}
	
	private Map<String, Double> parameters;
	
}
