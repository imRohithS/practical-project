package com.flink.usecase.process.function;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.script.SimpleBindings;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flink.usecase.model.Calc;
import com.flink.usecase.model.CalculateExp;
import com.flink.usecase.model.Query;
import com.flink.usecase.model.TxnDetails;

public class CalculateTXNQuery extends CoProcessFunction<TxnDetails, Query, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	private transient MapStateDescriptor<String, String> queryMap;
	private transient ValueState<Double> txnAmnt;
	private transient ValueState<Integer> txnCount;
	private transient ValueState<Integer> getMaxActivityRisk;
	private transient ValueState<Integer> getCustEffectiveRisk;
	private transient ValueState<Query> getQuery;

	@Override
	public void processElement1(TxnDetails arg0, Context arg1, Collector<String> out) throws Exception {
		TxnDetails value = arg0;
		int cashTxnMaxActvityRisk = getMaxActivityRisk.value();
		int custEffectiveRisk = getCustEffectiveRisk.value();
		Query query = getQuery.value();
		Map<String, CalculateExp> expression = getExpression(query);
		String customerRiskLevel = getRiskLevel(arg0, expression, query);

		double currentTxnAmount = txnAmnt.value();
//		if (cashTxnMaxActvityRisk < value.getCustTxn().getCashTrxnActvyRiskNb()) {
//			cashTxnMaxActvityRisk = value.getCustTxn().getCashTrxnActvyRiskNb();
//			getMaxActivityRisk.update(cashTxnMaxActvityRisk);
//		}
//
//		if (custEffectiveRisk < value.getCust().getCustEfctvRiskNb()) {
//			custEffectiveRisk = value.getCust().getCustEfctvRiskNb();
//			getCustEffectiveRisk.update(custEffectiveRisk);
//		}

		currentTxnAmount = currentTxnAmount + value.getCustTxn().getTrxnBaseAm();
		txnAmnt.update(currentTxnAmount);
		int count = txnCount.value() + 1;
		txnCount.update(count);

		boolean riskLevel = evaluateAlertExp(customerRiskLevel, query, expression, value);

//		if (customerRiskLevel.equals("HR") && currentTxnAmount > 1000 && count > 2) {
//			out.collect("Alert!!! Custmer at High Risk Level -->Customer Number '" + value.getCust().getCustIntrlId()
//					+ "' Acc Number '" + value.getAcc().getAcctIntrlId() + "'");
//		} else if (customerRiskLevel.equals("MR") && currentTxnAmount > 1000 && count > 2) {
//			out.collect("Alert!!! Custmer at Medium Risk Level -->Customer Number '" + value.getCust().getCustIntrlId()
//					+ "' Acc Number '" + value.getAcc().getAcctIntrlId() + "'");
//		} else if (customerRiskLevel.equals("RR") && currentTxnAmount > 1000 && count > 2) {
//			out.collect("Warning!!! Custmer at Regular Risk Level -->Customer Number '"
//					+ value.getCust().getCustIntrlId() + "' Acc Number '" + value.getAcc().getAcctIntrlId() + "'");
//		}

		if (riskLevel) {
			out.collect("Alert!!! Custmer at " + customerRiskLevel + " Level -->Customer Number '"
					+ value.getCust().getCustIntrlId() + "' Acc Number '" + value.getAcc().getAcctIntrlId() + "'");

		}

	}

	@Override
	public void processElement2(Query arg0, Context arg1, Collector<String> arg2) throws Exception {
		getQuery.update(arg0);
	}

	private boolean evaluateAlertExp(String customerRiskLevel, Query query, Map<String, CalculateExp> expression,
			TxnDetails value) throws Exception {
		String exp = expression.get(customerRiskLevel).getThenExp();
		List<String> expGetVariable = query.getCalc().get(0).getThen().get(0).getMust().get(0).getExp();

		List<String> variables = getVariables(expGetVariable);

		boolean eval = evaluateExpression(exp, variables, value);

		return eval;

	}

	private String getRiskLevel(TxnDetails next, Map<String, CalculateExp> expression, Query query) throws Exception {
		List<String> exp = query.getCalc().get(0).getWhen().get(0).getMust().get(0).getExp();
		List<String> variables = getVariables(exp);
		for (Map.Entry<String, CalculateExp> entry : expression.entrySet()) {
			String key = entry.getKey();
			String expr = entry.getValue().getWhenExp();
			boolean risk = evaluateExpression(expr, variables, next);
			if (risk)
				return key;
		}
		return "RR";

	}

	private boolean evaluateExpression(String exp, List<String> variables, TxnDetails next) throws ScriptException {

		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("nashorn");
//		engine.eval("print('Hello, World!');");

//		boolean eval = (boolean) engine.eval("1<2");
		SimpleBindings binding = new SimpleBindings();
		ObjectMapper mapper = new ObjectMapper();

		JsonNode node = mapper.convertValue(next, JsonNode.class);

		for (String v : variables) {
			binding.put(v.trim(), node.findValue(v.trim().toLowerCase()));
		}
		binding.put("Effctv_Risk_Cutoff_Lvl", 2);
		binding.put("Actvty_Risk_Cutoff_Lvl", 2);

		boolean evalExp = (boolean) engine.eval(exp, binding);

		return evalExp;

	}

	private List<String> getVariables(List<String> exp) throws Exception {

		List<String> expVariables = new ArrayList<>();
		for (int i = 0; i < exp.size(); i++) {
			String s[] = exp.get(i).split("[<>]=?|=");
			expVariables.addAll(Arrays.asList(s));
		}
		return expVariables;
	}

	private Map<String, CalculateExp> getExpression(Query query) throws Exception {
//		Query query = readJson();
		List<Calc> calcList = query.getCalc();
		Map<String, CalculateExp> expWithKey = new HashMap<>();

		for (Calc calc : calcList) {
			StringBuilder expression = new StringBuilder();
			StringBuilder expressionThen = new StringBuilder();
			List<String> exp = calc.getWhen().get(0).getMust().get(0).getExp();
			for (int i = 0; i < exp.size(); i++) {
				expression.append(exp.get(i));
				if (i != exp.size() - 1) {
					expression.append(" && ");
				}
			}
			List<String> expThen = calc.getThen().get(0).getMust().get(0).getExp();
			for (int i = 0; i < expThen.size(); i++) {
				expressionThen.append(exp.get(i));
				if (i != exp.size() - 1) {
					expressionThen.append(" && ");
				}
			}
			CalculateExp c = new CalculateExp();
			c.setWhenExp(expression.toString());
			c.setThenExp(expressionThen.toString());
			expWithKey.put(calc.getName(), c);
		}

		return expWithKey;
	}

	@Override
	public void open(Configuration config) throws IOException {
//		MapStateDescriptor<String, String> mapStateDescriptor = new MapStateDescriptor<>("queryMap", String.class,
//				String.class);

		txnAmnt = getRuntimeContext()
				.getState(new ValueStateDescriptor<>("txnAmnt", TypeInformation.of(new TypeHint<Double>() {
				}), 0D));
		txnCount = getRuntimeContext()
				.getState(new ValueStateDescriptor<>("txnCount", TypeInformation.of(new TypeHint<Integer>() {
				}), 0));
		getMaxActivityRisk = getRuntimeContext()
				.getState(new ValueStateDescriptor<>("getMaxActivityRisk", TypeInformation.of(new TypeHint<Integer>() {
				}), 0));
		getCustEffectiveRisk = getRuntimeContext().getState(
				new ValueStateDescriptor<>("getCustEffectiveRisk", TypeInformation.of(new TypeHint<Integer>() {
				}), 0));
//		txnAmnt = getRuntimeContext().getState(new ValueStateDescriptor<>("query", Double.class));
//		txnCount = getRuntimeContext().getState(new ValueStateDescriptor<>("query", Integer.class));
//		getMaxActivityRisk = getRuntimeContext().getState(new ValueStateDescriptor<>("query", Integer.class));
//		getCustEffectiveRisk = getRuntimeContext().getState(new ValueStateDescriptor<>("query", Integer.class));
//		txnAmnt.update(0D);
//		txnCount.update(0);
//		getMaxActivityRisk.update(0);
//		getCustEffectiveRisk.update(0);
		ValueStateDescriptor<Query> descriptorQuery = new ValueStateDescriptor<>("Query",
				TypeInformation.of(new TypeHint<Query>() {
				}), new Query());
		getQuery = getRuntimeContext().getState(descriptorQuery);

	}

//	@Override
//	public void flatMap1(TxnDetails value, Collector<String> out) throws Exception {
////		TxnDetails value = value;
//		int cashTxnMaxActvityRisk = getMaxActivityRisk.value();
//		int custEffectiveRisk = getCustEffectiveRisk.value();
//		Query query = getQuery.value();
//		Map<String, CalculateExp> expression = getExpression(query);
//		String customerRiskLevel = getRiskLevel(value, expression, query);
//
//		double currentTxnAmount = txnAmnt.value();
////		if (cashTxnMaxActvityRisk < value.getCustTxn().getCashTrxnActvyRiskNb()) {
////			cashTxnMaxActvityRisk = value.getCustTxn().getCashTrxnActvyRiskNb();
////			getMaxActivityRisk.update(cashTxnMaxActvityRisk);
////		}
////
////		if (custEffectiveRisk < value.getCust().getCustEfctvRiskNb()) {
////			custEffectiveRisk = value.getCust().getCustEfctvRiskNb();
////			getCustEffectiveRisk.update(custEffectiveRisk);
////		}
//
//		currentTxnAmount = currentTxnAmount + value.getCustTxn().getTrxnBaseAm();
//		txnAmnt.update(currentTxnAmount);
//		int count = txnCount.value() + 1;
//		txnCount.update(count);
//
//		boolean riskLevel = evaluateAlertExp(customerRiskLevel, query, expression, value);
//
////		if (customerRiskLevel.equals("HR") && currentTxnAmount > 1000 && count > 2) {
////			out.collect("Alert!!! Custmer at High Risk Level -->Customer Number '" + value.getCust().getCustIntrlId()
////					+ "' Acc Number '" + value.getAcc().getAcctIntrlId() + "'");
////		} else if (customerRiskLevel.equals("MR") && currentTxnAmount > 1000 && count > 2) {
////			out.collect("Alert!!! Custmer at Medium Risk Level -->Customer Number '" + value.getCust().getCustIntrlId()
////					+ "' Acc Number '" + value.getAcc().getAcctIntrlId() + "'");
////		} else if (customerRiskLevel.equals("RR") && currentTxnAmount > 1000 && count > 2) {
////			out.collect("Warning!!! Custmer at Regular Risk Level -->Customer Number '"
////					+ value.getCust().getCustIntrlId() + "' Acc Number '" + value.getAcc().getAcctIntrlId() + "'");
////		}
//
//		if (riskLevel) {
//			out.collect("Alert!!! Custmer at " + customerRiskLevel + " Level -->Customer Number '"
//					+ value.getCust().getCustIntrlId() + "' Acc Number '" + value.getAcc().getAcctIntrlId() + "'");
//
//		}
//
//	}
//
//	@Override
//	public void flatMap2(Query value, Collector<String> out) throws Exception {
////		if (getQuery.value() != null) {
//		getQuery.update(value);
////		}
//	}
}
