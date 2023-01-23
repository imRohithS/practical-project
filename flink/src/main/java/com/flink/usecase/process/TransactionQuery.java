package com.flink.usecase.process;

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
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flink.usecase.main.FlinkIntegration;
import com.flink.usecase.model.Account;
import com.flink.usecase.model.Calc;
import com.flink.usecase.model.CalculateExp;
import com.flink.usecase.model.Customer;
import com.flink.usecase.model.CustomerAccnt;
import com.flink.usecase.model.CustomerAccntRole;
import com.flink.usecase.model.CustomerTxn;
import com.flink.usecase.model.Query;
import com.flink.usecase.model.TxnDetails;
import com.flink.usecase.service.DSLService;

public class TransactionQuery extends KeyedBroadcastProcessFunction<String, TxnDetails, Query, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient ValueState<Double> txnAmnt;
	private transient ValueState<Integer> txnCount;
	private transient ValueState<Integer> getMaxActivityRisk;
	private transient ValueState<Integer> getCustEffectiveRisk;
	private transient ValueState<Query> getQuery;

	@Override
	public void processBroadcastElement(Query value, Context ctx, Collector<String> out) throws Exception {
		ctx.getBroadcastState(FlinkIntegration.txnQueryDescriptor).put("query", value);

	}

	@Override
	public void processElement(TxnDetails value, ReadOnlyContext ctx, Collector<String> out) throws Exception {

		Query query = ctx.getBroadcastState(FlinkIntegration.txnQueryDescriptor).get("query");

		double currentTxnAmount = txnAmnt.value();
		getMaxActivityRisk.update(Math.max(value.getCustTxn().getCashTrxnActvyRiskNb(), getMaxActivityRisk.value()));
		value.getCustTxn().setCashTrxnActvyRiskNb(
				Math.max(value.getCustTxn().getCashTrxnActvyRiskNb(), getMaxActivityRisk.value()));

		getCustEffectiveRisk.update(Math.max(value.getCust().getCustEfctvRiskNb(), getCustEffectiveRisk.value()));
		value.getCust()
				.setCustEfctvRiskNb(Math.max(value.getCust().getCustEfctvRiskNb(), getCustEffectiveRisk.value()));

		currentTxnAmount = currentTxnAmount + value.getCustTxn().getTrxnBaseAm();
		txnAmnt.update(currentTxnAmount);
		int count = txnCount.value() + 1;
		txnCount.update(count);

		Map<String, CalculateExp> expression = DSLService.getExpression(query);
		String customerRiskLevel = DSLService.getRiskLevel(value, expression, query);

		boolean riskLevel = DSLService.evaluateAlertExp(customerRiskLevel, query, expression, value, txnAmnt.value(),
				txnCount.value());
		String role = (value.getAcctRole() != null) ? value.getAcctRole().getCustAcctRoleCd() : "";

		CustomerTxn custTxn = value.getCustTxn();
		Account acc = value.getAcc();
		Customer cust = value.getCust();
		CustomerAccnt customerAccnt = value.getCustomerAccnt();
		CustomerAccntRole acctRole = value.getAcctRole();
		List<String> tableConnected = query.getCalc().get(0).getTablesToConnect();

		if (riskLevel) {
			out.collect(expression.get(customerRiskLevel).getAlertMsg() + value.getCust().getCustIntrlId()
					+ "' Acc Number '" + value.getAcc().getAcctIntrlId() + "'" + "Cust Type '" + role + "'"
					+ " total transaction amount ' " + txnAmnt.value() + "' txn count '" + txnCount.value());

		} else {
			out.collect("Customer under NO RISK " + value.getCust().getCustIntrlId() + "' Acc Number '"
					+ value.getAcc().getAcctIntrlId() + "'" + "Cust Type '" + role + "'" + "total transaction amount ' "
					+ txnAmnt.value() + " txn count '" + txnCount.value());
		}

	}

	@Override
	public void open(Configuration config) throws IOException {

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

		ValueStateDescriptor<Query> descriptorQuery = new ValueStateDescriptor<>("Query",
				TypeInformation.of(new TypeHint<Query>() {
				}), new Query());
		getQuery = getRuntimeContext().getState(descriptorQuery);

	}

}
