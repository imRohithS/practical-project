package com.flink.usecase.service;

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

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flink.usecase.model.Calc;
import com.flink.usecase.model.CalculateExp;
import com.flink.usecase.model.Query;
import com.flink.usecase.model.TxnDetails;

public class DSLService {

	public static boolean evaluateAlertExp(String customerRiskLevel, Query query, Map<String, CalculateExp> expression,
			TxnDetails value, Double txnAmnt, Integer txnCount) throws Exception {
		String exp = "";
		List<String> variables = new ArrayList<>();
		exp = expression.get(customerRiskLevel).getThenExp();
		variables = expression.get(customerRiskLevel).getVariablesThen();

		boolean eval = evaluateThenExpression(exp, variables, value, txnAmnt, txnCount);

		return eval;

	}

	public static String getRiskLevel(TxnDetails next, Map<String, CalculateExp> expression, Query query)
			throws Exception {
		List<String> exp = query.getCalc().get(0).getWhen().get(0).getMust().get(0).getExp();
		for (Map.Entry<String, CalculateExp> entry : expression.entrySet()) {
			String key = entry.getKey();
			String expr = entry.getValue().getWhenExp();
			List<String> variables = entry.getValue().getVariablesWhen();
			boolean risk = false;
			if (expr != null && variables != null) {
				risk = evaluateExpression(expr, variables, next);
			}
			if (risk)
				return key;
		}
		return "MR";

	}

	public static boolean evaluateThenExpression(String exp, List<String> variables, TxnDetails next, Double txnAmnt,
			Integer txnCount) throws Exception {

		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("nashorn");
		SimpleBindings binding = new SimpleBindings();
		ObjectMapper mapper = new ObjectMapper();

		JsonNode node = mapper.convertValue(next, JsonNode.class);

		for (String v : variables) {
			if (v.trim().equalsIgnoreCase("trxn_base_am")) {
				binding.put(v.trim(), txnAmnt);
			} else if (v.trim().equalsIgnoreCase("Trans_Ct")) {
				binding.put(v.trim(), txnCount);
			} else {
				binding.put(v.trim(), node.findValue(v.trim().toLowerCase()));
			}
		}

		boolean evalExp = (boolean) engine.eval(exp, binding);

		return evalExp;

	}

	public static boolean evaluateExpression(String exp, List<String> variables, TxnDetails next)
			throws ScriptException {

		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("nashorn");
		SimpleBindings binding = new SimpleBindings();
		ObjectMapper mapper = new ObjectMapper();

		JsonNode node = mapper.convertValue(next, JsonNode.class);

		for (String v : variables) {
			binding.put(v.trim(), node.findValue(v.trim().toLowerCase()));
		}

		boolean evalExp = (boolean) engine.eval(exp, binding);

		return evalExp;

	}

	public static List<String> getVariables(List<String> exp) throws Exception {

		List<String> expVariables = new ArrayList<>();
		for (int i = 0; i < exp.size(); i++) {
			String s[] = exp.get(i).split("[<>]=?|=|[0-9]");
//			String s[] = exp.get(i).split("[<>]=?|=");
			expVariables.addAll(Arrays.asList(s));
		}
		return expVariables;
	}

	public static Map<String, CalculateExp> getExpression(Query query) throws Exception {
		List<Calc> calcList = query.getCalc();
		Map<String, CalculateExp> expWithKey = new HashMap<>();

		for (Calc calc : calcList) {
			StringBuilder expression = new StringBuilder();
			StringBuilder expressionThen = new StringBuilder();
			CalculateExp c = new CalculateExp();
			if (calc.getWhen() != null) {
				List<String> exp = calc.getWhen().get(0).getMust().get(0).getExp();
				for (int i = 0; i < exp.size(); i++) {
					expression.append(exp.get(i));
					if (i != exp.size() - 1) {
						expression.append(" && ");
					}
				}
				c.setWhenExp(expression.toString());
				c.setVariablesWhen(getVariables(exp));
			}
			if (calc.getThen() != null) {
				List<String> expThen = calc.getThen().get(0).getMust().get(0).getExp();
				for (int i = 0; i < expThen.size(); i++) {
					expressionThen.append(expThen.get(i));
					if (i != expThen.size() - 1) {
						expressionThen.append(" && ");
					}
				}
				c.setThenExp(expressionThen.toString());
				c.setVariablesThen(getVariables(expThen));
			}
			c.setAlertMsg(calc.getThen().get(0).getAlert());

			expWithKey.put(calc.getName(), c);
		}

		return expWithKey;
	}
}
