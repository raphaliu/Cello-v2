/**
 * Copyright (C) 2018 Boston University (BU)
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
package placing.algorithm.Eugene;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cidarlab.eugene.dom.Device;
import org.cidarlab.eugene.dom.NamedElement;
import org.cidarlab.eugene.dom.imp.container.EugeneArray;
import org.cidarlab.eugene.dom.imp.container.EugeneCollection;
import org.cidarlab.eugene.exception.EugeneException;
import org.cidarlab.eugene.util.DeviceUtils;

import common.CObjectCollection;
import common.Utils;
import common.graph.algorithm.SinkBFS;
import placing.algorithm.PLAlgorithm;
import placing.algorithm.Eugene.data.EugeneDataUtils;
import placing.algorithm.Eugene.data.EugeneNetlistData;
import placing.algorithm.Eugene.data.EugeneNetlistEdgeData;
import placing.algorithm.Eugene.data.EugeneNetlistNodeData;
import placing.algorithm.Eugene.data.ucf.CasetteParts;
import placing.algorithm.Eugene.data.ucf.Gate;
import placing.algorithm.Eugene.data.ucf.GateParts;
import placing.algorithm.Eugene.data.ucf.InputSensor;
import placing.algorithm.Eugene.data.ucf.OutputReporter;
import placing.algorithm.Eugene.data.ucf.Part;
import placing.algorithm.Eugene.data.ucf.ResponseFunction;
import placing.algorithm.Eugene.data.ucf.Rules;
import placing.runtime.environment.PLArgString;
import results.logicSynthesis.LSResults;
import results.logicSynthesis.LSResultsUtils;
import results.netlist.Netlist;
import results.netlist.NetlistEdge;
import results.netlist.NetlistNode;
import results.placing.placement.Placement;

/**
 * The Eugene class implements the <i>Eugene</i> algorithm in the <i>placing</i> stage.
 * 
 * @author Timothy Jones
 * 
 * @date 2018-06-06
 *
 */
public class Eugene extends PLAlgorithm{

	/**
	 *  Returns the <i>EugeneNetlistNodeData</i> of the <i>node</i>
	 *
	 *  @param node a node within the <i>netlist</i> of this instance
	 *  @return the <i>EugeneNetlistNodeData</i> instance if it exists, null otherwise
	 */
	protected EugeneNetlistNodeData getEugeneNetlistNodeData(NetlistNode node){
		EugeneNetlistNodeData rtn = null;
		rtn = (EugeneNetlistNodeData) node.getNetlistNodeData();
		return rtn;
	}

	/**
	 *  Returns the <i>EugeneNetlistEdgeData</i> of the <i>edge</i>
	 *
	 *  @param edge an edge within the <i>netlist</i> of this instance
	 *  @return the <i>EugeneNetlistEdgeData</i> instance if it exists, null otherwise
	 */
	protected EugeneNetlistEdgeData getEugeneNetlistEdgeData(NetlistEdge edge){
		EugeneNetlistEdgeData rtn = null;
		rtn = (EugeneNetlistEdgeData) edge.getNetlistEdgeData();
		return rtn;
	}

	/**
	 *  Returns the <i>EugeneNetlistData</i> of the <i>netlist</i>
	 *
	 *  @param netlist the netlist of this instance
	 *  @return the <i>EugeneNetlistData</i> instance if it exists, null otherwise
	 */
	protected EugeneNetlistData getEugeneNetlistData(Netlist netlist){
		EugeneNetlistData rtn = null;
		rtn = (EugeneNetlistData) netlist.getNetlistData();
		return rtn;
	}

	/**
	 *  Gets the Constraint data from the NetlistConstraintFile
	 */
	@Override
	protected void getConstraintFromNetlistConstraintFile() {

	}

	/**
	 *  Gets the data from the UCF
	 */
	@Override
	protected void getDataFromUCF() {
		this.setGates(EugeneDataUtils.getGates(this.getTargetData()));
		this.setParts(EugeneDataUtils.getParts(this.getTargetData()));
		this.setInputSensors(EugeneDataUtils.getInputSensors(this.getTargetData()));
		this.setOutputReporters(EugeneDataUtils.getOutputReporters(this.getTargetData()));
		this.setRules(EugeneDataUtils.getRules(this.getTargetData()));
	}

	/**
	 *  Set parameter(s) value(s) of the algorithm
	 */
	@Override
	protected void setParameterValues() {
		String outputDir = this.getRuntimeEnv().getOptionValue(PLArgString.OUTPUTDIR);
		String inputFilename = this.getNetlist().getInputFilename();
		String filename = outputDir + Utils.getFileSeparator() + Utils.getFilename(inputFilename) + "_eugeneScript.eug";
		this.setEugeneScriptFilename(filename);
	}

	/**
	 *  Validate parameter value of the algorithm
	 */
	@Override
	protected void validateParameterValues() {
		
	}
	
	private CObjectCollection<Part> getInputPromoters(final NetlistNode node) {
		CObjectCollection<Part> rtn = new CObjectCollection<>();
		for (int i = 0; i < node.getNumInEdge(); i++) {
			NetlistEdge e = node.getInEdgeAtIdx(i);
			NetlistNode src = e.getSrc();
			String promoter = "";
			String gateType = src.getResultNetlistNodeData().getGateType();
			if (LSResultsUtils.isAllInput(src)) {
				promoter = gateType;
			} else {
				Gate gate = this.getGates().findCObjectByName(gateType);
				if (gate == null) {
					new RuntimeException("Unknown gate.");
				}
				promoter = gate.getGateParts().getPromoter();
			}	
			Part part = this.getParts().findCObjectByName(promoter);
			rtn.add(part);
		}
		return rtn;
	}
	
	private CObjectCollection<Part> getInputSensorParts(final NetlistNode node) {
		CObjectCollection<Part> rtn = new CObjectCollection<>();
		String gateType = node.getResultNetlistNodeData().getGateType();
		InputSensor sensor = this.getInputSensors().findCObjectByName(gateType);
		if (sensor == null) {
			new RuntimeException("Unknown input sensor.");
		}
		for (int i = 0; i < sensor.getNumParts(); i++) {
			Part part = sensor.getPartAtIdx(i);
			if (part == null) {
				throw new RuntimeException("Unknown part.");
			}
			rtn.add(part);
		}
		return rtn;
	}
	
	private CObjectCollection<Part> getOutputReporterParts(final NetlistNode node) {
		CObjectCollection<Part> rtn = new CObjectCollection<>();
		String gateType = node.getResultNetlistNodeData().getGateType();
		OutputReporter reporter = this.getOutputReporters().findCObjectByName(gateType);
		if (reporter == null) {
			new RuntimeException("Unknown input sensor.");
		}
		for (int i = 0; i < reporter.getNumParts(); i++) {
			Part part = reporter.getPartAtIdx(i);
			if (part == null) {
				throw new RuntimeException("Unknown part.");
			}
			rtn.add(part);
		}
		return rtn;
	}
	
	private CObjectCollection<Part> getCasetteParts(final NetlistNode node) {
		CObjectCollection<Part> rtn = new CObjectCollection<>();
		String gateType = node.getResultNetlistNodeData().getGateType();
		Gate gate = this.getGates().findCObjectByName(gateType);
		if (gate == null) {
			throw new RuntimeException("Unknown gate.");
		}
		GateParts gateParts = gate.getGateParts();
		ResponseFunction rf = gate.getResponseFunction();
		for (int i = 0; i < rf.getNumVariable(); i++) {
			String variable = rf.getVariableAtIdx(i).getName();
			CasetteParts casetteParts = gateParts.getCasetteParts(variable);
			for (int j = 0; j < casetteParts.getNumParts(); j++) {
				Part part = casetteParts.getPartAtIdx(j);
				if (part == null) {
					throw new RuntimeException("Unknown part.");
				}
				rtn.add(part);
			}
		}
		return rtn;
	}
	
	private CObjectCollection<Part> getDevice(final NetlistNode node) {
		CObjectCollection<Part> rtn = new CObjectCollection<>();
		if (LSResultsUtils.isPrimaryInput(node)) {
			rtn.addAll(this.getInputSensorParts(node));
		}
		else if (LSResultsUtils.isPrimaryOutput(node)) {
			rtn.addAll(this.getInputPromoters(node));
			rtn.addAll(this.getOutputReporterParts(node));
		}
		else if (!LSResultsUtils.isAllInput(node) && !LSResultsUtils.isAllOutput(node)) {
			rtn.addAll(this.getInputPromoters(node));
			rtn.addAll(this.getCasetteParts(node));
		}
		
		return rtn;
	}
	
	private void setDevices() {
		SinkBFS<NetlistNode, NetlistEdge, Netlist> BFS = new SinkBFS<NetlistNode, NetlistEdge, Netlist>(this.getNetlist());
		NetlistNode node = null;
		node = BFS.getNextVertex();
		while (node != null) {
			this.getDeviceMap().put(node, getDevice(node));
			node = BFS.getNextVertex();
		}
	}
	
	private Collection<String> getPartTypes() {
		Set<String> rtn = new HashSet<String>();
		for (List<Part> parts : this.getDeviceMap().values()) {
			for (Part p : parts) {
				String type = String.format("PartType %s;", p.getPartType());
				rtn.add(type);
			}
		}
		return rtn;
	}
	
	private Collection<String> getPartSequences() {
		Set<String> rtn = new HashSet<String>();
		for (List<Part> parts : this.getDeviceMap().values()) {
			for (Part p : parts) {
				String seq = String.format("%s %s(.SEQUENCE(\"%s\"));", p.getPartType(), p.getName(), p.getDNASequence());
				rtn.add(seq);
			}
		}
		return rtn;
	}
	
	private String getGateBaseName(NetlistNode node) {
		String rtn = null;
		String gateType = node.getResultNetlistNodeData().getGateType();
		if (LSResultsUtils.isPrimaryInput(node)) {
			rtn = gateType;
		}
		else if (LSResultsUtils.isPrimaryOutput(node)) {
			rtn = gateType;
		}
		else if (!LSResultsUtils.isPrimaryInput(node) && !LSResultsUtils.isPrimaryOutput(node)) {
			Gate gate = this.getGates().findCObjectByName(gateType);
			if (gate == null) {
				throw new RuntimeException("Unknown gate.");
			}
			rtn = gate.getRegulator();
		}
		else {
			throw new RuntimeException("Unknown gateType.");
		}
		return rtn;
	}
	
	private String getGateName(NetlistNode node) {
		String rtn = "";
		rtn += "gate_";
		rtn += this.getGateBaseName(node);
		return rtn;
	}
	
	private String getGateDeviceName(NetlistNode node) {
		String rtn = "";
		rtn += this.getGateBaseName(node);
		rtn += "_device";
		return rtn;
	}
	
	private Collection<String> getDeviceDefinitions() {
		Collection<String> rtn = new ArrayList<String>();
		for (NetlistNode node : this.getDeviceMap().keySet()) {
			String str = "";
			str += String.format("Device %s(",this.getGateDeviceName(node));
			str += Utils.getNewLine();
			for (Part part : this.getDeviceMap().get(node)) {
				String item = "";
				if (part.getPartType().equalsIgnoreCase(Eugene.S_PROMOTER)) {
					item = part.getPartType();
				} else {
					item = part.getName();
				}
				str += Utils.getTabCharacter();
				str += String.format("%s,",item);
				str += Utils.getNewLine();
			}
			str = str.substring(0, str.length() - 2);
			str += Utils.getNewLine();
			str += ");";
			rtn.add(str);
		}
		return rtn;
	}
	
	private Collection<String> getNamesFromRule(String rule) {
		Collection<String> rtn = new HashSet<>();
		Collection<String> keywords = Arrays.asList(Rules.ValidRuleKeywords);
		StringTokenizer st = new StringTokenizer(rule, " \t\n\r\f,");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (!keywords.contains(token) && token.substring(0, 1).matches("[a-z,A-Z]")) {
				rtn.add(token);
			}
		}
		return rtn;
	}
	
	private Collection<String> getPartNamesFromParts(CObjectCollection<Part> parts) {
		Collection<String> rtn = new ArrayList<>();
		for (Part part : parts) {
			rtn.add(part.getName());
		}
		return rtn;
	}
	
	private Collection<String> getDeviceRules() {
		Collection<String> rtn = new ArrayList<>();
		for (NetlistNode node : this.getDeviceMap().keySet()) {
			// rule
			String str = "";
			str += String.format("Rule %s_rules ( ON %s:",this.getGateBaseName(node),this.getGateDeviceName(node));
			str += Utils.getNewLine();
			CObjectCollection<Part> device = this.getDeviceMap().get(node);
			Collection<String> parts = this.getPartNamesFromParts(device);
			for (Part part : device) {
				if (part.getPartType().equalsIgnoreCase(Eugene.S_PROMOTER)) {
					str += Utils.getTabCharacter();
					str += String.format("%s %s %s",Rules.S_CONTAINS,part.getName(),Rules.S_AND);
					str += Utils.getNewLine();
				}
				for (String r : this.getRules().getPartRules()) {
					Collection<String> ruleParts = this.getNamesFromRule(r);
					if (parts.containsAll(ruleParts)) {
						if (!str.toLowerCase().contains(Rules.S_STARTSWITH.toLowerCase())) {
							str += Utils.getTabCharacter();
							str += String.format("%s %s",r,Rules.S_AND);
							str += Utils.getNewLine();
						} else {
							// TODO log something about duplicate startswith
						}
					}
				}
			}
			str += Utils.getTabCharacter();
			str += "ALL_FORWARD";
			str += Utils.getNewLine();
			str += ");";
			str += Utils.getNewLine();
			rtn.add(str);
		}
		return rtn;
	}
	
	private Collection<String> getProducts() {
		Collection<String> rtn = new ArrayList<>();
		for (NetlistNode node : this.getDeviceMap().keySet()) {
			String str = "";
			str += String.format("%s_devices = product(%s);",this.getGateBaseName(node),this.getGateDeviceName(node));
			rtn.add(str);
		}
		return rtn;
	}
	
	private Collection<String> getGateDeviceNames() {
		Collection<String> rtn = new ArrayList<>();
		for (NetlistNode node : this.getDeviceMap().keySet()) {
			rtn.add(this.getGateName(node));
		}
		return rtn;
	}
	
	private Collection<String> getGateDefinitions() {
		Collection<String> rtn = new ArrayList<>();
		Collection<String> names = this.getGateDeviceNames();
		for (String name : names) {
			String str = "";
			str += String.format("Device %s();",name);
			rtn.add(str);
		}
		return rtn;
	}
	
	private String getCircuitDeclaration() {
		String rtn = "";
		rtn += "Device circuit();";
		rtn += Utils.getNewLine() + Utils.getNewLine();
		return rtn;
	}
	
	private String getCircuitInstantiation() {
		String rtn = "";
		rtn += "Device circuit(";
		for (String str : this.getGateDeviceNames()) {
			rtn += Utils.getNewLine();
			rtn += Utils.getTabCharacter();
			rtn += str;
			rtn += ",";
		}
		rtn = rtn.substring(0, rtn.length() - 1);
		rtn += Utils.getNewLine();
		rtn += ");";
		return rtn;
	}
	
	private String getCircuitRules() {
		String rtn = "";
		Collection<String> gates = this.getGateDeviceNames();
		rtn += "Rule allRules( ON circuit:";
		for (String str : gates) {
			rtn += Utils.getNewLine();
			rtn += Utils.getTabCharacter();
			rtn += String.format("%s %s 1 %s", str, Rules.S_EXACTLY, Rules.S_AND);
		}
		for (String str : this.getRules().getGateRules()) {
			Collection<String> devices = getNamesFromRule(str);
			if (gates.containsAll(devices)) {
				rtn += Utils.getNewLine();
				rtn += Utils.getTabCharacter();
				rtn += String.format("%s %s",str,Rules.S_AND);
			}
		}
		rtn = rtn.replaceAll(" " + Rules.S_AND + "$","");
		rtn += Utils.getNewLine();
		rtn += ");";
		rtn += Utils.getNewLine() + Utils.getNewLine();
		return rtn;
	}
	
	private String getResults() {
		String rtn = "";
		rtn += "Array allResults;";
		rtn += Utils.getNewLine() + Utils.getNewLine();
		List<NetlistNode> nodes = new ArrayList<>();
		nodes.addAll(this.getDeviceMap().keySet());
		for (int j = 0; j < nodes.size(); j++) {
			NetlistNode node = nodes.get(j);
			int i = j + 1;
			rtn += String.format("for(num i%d = 0; i%d < sizeof(%s_devices); i%d = i%d + 1) {",i,i,this.getGateBaseName(node),i,i);
			rtn += Utils.getNewLine();
		}
		rtn += Utils.getNewLine();
		for (int j = 0; j < nodes.size(); j++) {
			NetlistNode node = nodes.get(j);
			int i = j + 1;
			rtn += String.format("%s = %s_devices[i%d];",this.getGateName(node),this.getGateBaseName(node),i);
			rtn += Utils.getNewLine();
		}
		rtn += Utils.getNewLine();
		rtn += this.getCircuitInstantiation();
		rtn += Utils.getNewLine() + Utils.getNewLine();
		rtn += "result = permute(circuit);";
		rtn += Utils.getNewLine() + Utils.getNewLine();
		rtn += "allResults = allResults + result;";
		rtn += Utils.getNewLine() + Utils.getNewLine();
		for (int j = 0; j < nodes.size(); j++) {
			rtn += "}";
			rtn += Utils.getNewLine();
		}
		return rtn;
	}
	
	private String getBlock(Collection<String> str) {
		String rtn = "";
		rtn += String.join(Utils.getNewLine(), str);
		rtn += Utils.getNewLine() + Utils.getNewLine();
		return rtn;
	}

	/**
	 *  Perform preprocessing
	 */
	@Override
	protected void preprocessing() {
		this.setDeviceMap(new HashMap<NetlistNode,CObjectCollection<Part>>());
		
		// devices
		this.setDevices();
		
		logInfo("building Eugene input script");
		
		String script = "";
		
		// part types
		script += this.getBlock(this.getPartTypes());
		// part sequences
		script += this.getBlock(this.getPartSequences());
		// device definitions
		script += this.getBlock(this.getDeviceDefinitions());
		// device rules
		script += this.getBlock(this.getDeviceRules());
		// products
		script += this.getBlock(this.getProducts());
		// gate definitions
		script += this.getBlock(this.getGateDefinitions());
		// circuit definition
		script += this.getCircuitDeclaration();
		// circuit rules
		script += this.getCircuitRules();
		// results
		script += this.getResults();
	
		this.setEugeneScript(script);
		Utils.writeToFile(script,this.getEugeneScriptFilename());
	}

	/**
	 *  Run the (core) algorithm
	 */
	@Override
	protected void run() {
		logInfo("running Eugene");
		try {
			org.cidarlab.eugene.Eugene eugene = new org.cidarlab.eugene.Eugene();

			File cruft = new File(Utils.getWorkingDirectory()
					+ Utils.getFileSeparator()
					+ "exports");
			(new File(cruft.getPath()
					+ Utils.getFileSeparator()
					+ "pigeon")).delete();
			cruft.delete();

			EugeneCollection ec = eugene.executeScript(this.getEugeneScript());
			this.setEugeneResults((EugeneArray) ec.get("allResults"));
		} catch ( EugeneException e ) {
			e.printStackTrace();
		}
	}
	
	private NetlistNode getNetlistNodeByGateName(String name) {
		NetlistNode rtn = null;
		name = name.replaceAll("^gate_","");
		for (NetlistNode node : this.getDeviceMap().keySet()) {
			String gateType = node.getResultNetlistNodeData().getGateType();
			if (gateType.contains(name)) {
				rtn = node;
				break;
			}
		}
		return rtn;
	}

	/**
	 *  Perform postprocessing
	 */
	@Override
	protected void postprocessing() {
		logInfo("processing Eugene output");
		
		EugeneArray plasmids = this.getEugeneResults();
		
		if (plasmids == null) {
			throw new RuntimeException("Eugene error!");
		}
		
		for (int i = 0; i < plasmids.getElements().size(); i++) {
			NamedElement plasmid = null;
			
			try {
				plasmid = plasmids.getElement(i);
			} catch (EugeneException e) {
				e.printStackTrace();
			}

			if (plasmid instanceof org.cidarlab.eugene.dom.Device) {
				
				List<NamedElement> components = ((Device)plasmid).getComponentList();
				
				for (int j = 0; j < components.size(); j++) {
					NamedElement el = components.get(j);
					if (el instanceof Device) {
						String name = el.getName();
						
						NetlistNode node = this.getNetlistNodeByGateName(name);
						Placement placement = new Placement(true,false);
						placement.setDirection(true);
						placement.setIdx(j);

						String o = "";
						try {
							o = ((Device)plasmid).getOrientations(j).toString();
						} catch (EugeneException e) {
							e.printStackTrace();
						}

						if (o.contains(Rules.S_REVERSE)) {
							placement.setDirection(false);
							try {
								Device reverse = DeviceUtils.flipAndInvert((Device)el);
								el = reverse;
							} catch (EugeneException e) {
								e.printStackTrace();
							}
						}

						for (NamedElement part : ((Device)el).getComponentList()) {
							placement.addPartToPlacement(part.getName());
						}
						
						node.getResultNetlistNodeData().getPlacements().addPlacement(placement);
					}
				}
			}
		}
	}

	/**
	 *  Returns the Logger for the <i>Eugene</i> algorithm
	 *
	 *  @return the logger for the <i>Eugene</i> algorithm
	 */
	@Override
	protected Logger getLogger() {
		return Eugene.logger;
	}
	
	private static final Logger logger = LogManager.getLogger(Eugene.class.getSimpleName());
	
	/**
	 * Getter for <i>eugeneResults</i>
	 * @return value of <i>eugeneResults</i>
	 */
	protected EugeneArray getEugeneResults() {
		return eugeneResults;
	}

	/**
	 * Setter for <i>eugeneResults</i>
	 * @param eugeneResults the value to set <i>eugeneResults</i>
	 */
	protected void setEugeneResults(final EugeneArray eugeneResults) {
		this.eugeneResults = eugeneResults;
	}
	
	/**
	 * Getter for <i>eugeneScript</i>
	 * @return value of <i>eugeneScript</i>
	 */
	protected String getEugeneScript() {
		return eugeneScript;
	}

	/**
	 * Setter for <i>eugeneScript</i>
	 * @param eugeneScript the value to set <i>eugeneScript</i>
	 */
	protected void setEugeneScript(final String eugeneScript) {
		this.eugeneScript = eugeneScript;
	}

	/**
	 * Getter for <i>eugeneScriptFilename</i>
	 * @return value of <i>eugeneScriptFilename</i>
	 */
	protected String getEugeneScriptFilename() {
		return eugeneScriptFilename;
	}

	/**
	 * Setter for <i>eugeneScriptFilename</i>
	 * @param eugeneScriptFilename the value to set <i>eugeneScriptFilename</i>
	 */
	protected void setEugeneScriptFilename(final String eugeneScriptFilename) {
		this.eugeneScriptFilename = eugeneScriptFilename;
	}

	/**
	 * @return the deviceMap
	 */
	public Map<NetlistNode,CObjectCollection<Part>> getDeviceMap() {
		return deviceMap;
	}

	/**
	 * @param deviceMap the deviceMap to set
	 */
	public void setDeviceMap(Map<NetlistNode,CObjectCollection<Part>> deviceMap) {
		this.deviceMap = deviceMap;
	}

	/**
	 * Getter for <i>gates</i>
	 * @return value of <i>gates</i>
	 */
	protected CObjectCollection<Gate> getGates() {
		return gates;
	}

	/**
	 * Setter for <i>gates</i>
	 * @param gates the value to set <i>gates</i>
	 */
	protected void setGates(final CObjectCollection<Gate> gates) {
		this.gates = gates;
	}

	/**
	 * Getter for <i>parts</i>
	 * @return value of <i>parts</i>
	 */
	protected CObjectCollection<Part> getParts() {
		return parts;
	}

	/**
	 * Setter for <i>parts</i>
	 * @param parts the value to set <i>parts</i>
	 */
	protected void setParts(final CObjectCollection<Part> parts) {
		this.parts = parts;
	}
	
	/*
	 * InputSensors
	 */
	/**
	 * Getter for <i>sensors</i>
	 * @return value of <i>sensors</i>
	 */
	public CObjectCollection<InputSensor> getInputSensors() {
		return sensors;
	}

	/**
	 * Setter for <i>sensors</i>
	 * @param sensors the value to set <i>sensors</i>
	 */
	protected void setInputSensors(final CObjectCollection<InputSensor> sensors) {
		this.sensors = sensors;
	}
	
	/**
	 * Getter for <i>reporters</i>
	 * @return value of <i>reporters</i>
	 */
	public CObjectCollection<OutputReporter> getOutputReporters() {
		return reporters;
	}

	/**
	 * Setter for <i>reporters</i>
	 * @param reporters the value to set <i>reporters</i>
	 */
	protected void setOutputReporters(final CObjectCollection<OutputReporter> reporters) {
		this.reporters = reporters;
	}

	/**
	 * Getter for <i>rules</i>
	 * @return value of <i>rules</i>
	 */
	public Rules getRules() {
		return rules;
	}

	/**
	 * Setter for <i>rules</i>
	 * @param rules the value to set rules
	 */
	public void setRules(Rules rules) {
		this.rules = rules;
	}

	private EugeneArray eugeneResults;
	private String eugeneScript;
	private String eugeneScriptFilename;
	private Map<NetlistNode,CObjectCollection<Part>> deviceMap;
	private CObjectCollection<Gate> gates;
	private CObjectCollection<Part> parts;
	private CObjectCollection<InputSensor> sensors;
	private CObjectCollection<OutputReporter> reporters;
	private Rules rules;
	
	static private String S_PROMOTER = "promoter";
}
