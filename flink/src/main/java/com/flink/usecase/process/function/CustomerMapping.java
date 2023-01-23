package com.flink.usecase.process.function;

import java.util.Map;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;

import com.flink.usecase.main.FlinkIntegration;
import com.flink.usecase.model.CustomerAccnt;
import com.flink.usecase.model.CustomerAccntRole;
import com.flink.usecase.model.CustomerTxn;
import com.flink.usecase.model.TxnDetails;

public class CustomerMapping extends KeyedBroadcastProcessFunction<String, TxnDetails, TxnDetails, TxnDetails> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void processBroadcastElement(TxnDetails custRoleVal, Context ctx, Collector<TxnDetails> arg2)
			throws Exception {
		ctx.getBroadcastState(FlinkIntegration.custAcctRoleDescriptor)
				.put(custRoleVal.getCustomerAccnt().getCustIntrlId(), custRoleVal);
	}

	@Override
	public void processElement(TxnDetails custVal, ReadOnlyContext ctx, Collector<TxnDetails> out) throws Exception {
		TxnDetails txn = custVal;
		TxnDetails brodcast = ctx.getBroadcastState(FlinkIntegration.custAcctRoleDescriptor)
				.get(custVal.getCust().getCustIntrlId());

		txn.setAcctRole(brodcast != null ? brodcast.getAcctRole() : null);
		txn.setCustomerAccnt(brodcast != null ? brodcast.getCustomerAccnt() : null);
		out.collect(txn);
	}

}
