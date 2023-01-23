package com.flink.usecase.process.function;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

import com.flink.usecase.main.FlinkIntegration;
import com.flink.usecase.model.CustomerAccnt;
import com.flink.usecase.model.CustomerAccntRole;
import com.flink.usecase.model.TxnDetails;

public class CustAcctRole extends KeyedBroadcastProcessFunction<String, CustomerAccnt, CustomerAccntRole, TxnDetails> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient ValueState<CustomerAccntRole> acctRoleDetails;

	@Override
	public void open(Configuration config) {

		acctRoleDetails = getRuntimeContext().getState(new ValueStateDescriptor<>("acctRoleDetails", CustomerAccntRole.class));
	}

	@Override
	public void processBroadcastElement(CustomerAccntRole custValue, Context ctx, Collector<TxnDetails> out)
			throws Exception {
		ctx.getBroadcastState(FlinkIntegration.custRoleDescriptor).put(custValue.getCustAcctRoleCd(), custValue);
	}

	@Override
	public void processElement(CustomerAccnt custAcntValue, ReadOnlyContext ctx, Collector<TxnDetails> out)
			throws Exception {
		TxnDetails txn = new TxnDetails();
		txn.setAcctRole(
				ctx.getBroadcastState(FlinkIntegration.custRoleDescriptor).get(custAcntValue.getCustAcctRoleCd()));
		txn.setCustomerAccnt(custAcntValue);
		
		out.collect(txn);
	}

}
