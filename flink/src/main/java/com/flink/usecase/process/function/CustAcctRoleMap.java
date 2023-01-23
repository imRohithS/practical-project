package com.flink.usecase.process.function;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;

import com.flink.usecase.model.CustomerAccnt;
import com.flink.usecase.model.CustomerAccntRole;
import com.flink.usecase.model.TxnDetails;

public class CustAcctRoleMap extends KeyedCoProcessFunction<String, CustomerAccnt, CustomerAccntRole, TxnDetails> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient ValueState<CustomerAccntRole> customerAccntRole;
	private transient ValueState<CustomerAccnt> customerAccnt;

	@Override
	public void open(Configuration config) {

		customerAccntRole = getRuntimeContext()
				.getState(new ValueStateDescriptor<>("acctRoleDetails", CustomerAccntRole.class));
		customerAccnt = getRuntimeContext().getState(new ValueStateDescriptor<>("CustomerAccnt", CustomerAccnt.class));
	}

	@Override
	public void processElement1(CustomerAccnt customerAcct, Context arg1, Collector<TxnDetails> out) throws Exception {
		if (customerAccntRole.value() != null) {
			TxnDetails txn = new TxnDetails();
			txn.setAcctRole(customerAccntRole.value());
			txn.setCustomerAccnt(customerAcct);
			out.collect(txn);
		} else {
			customerAccnt.update(customerAcct);

		}
	}

	@Override
	public void processElement2(CustomerAccntRole customerAcctRole, Context arg1, Collector<TxnDetails> out)
			throws Exception {
		if (customerAccnt.value() != null) {
			TxnDetails txn = new TxnDetails();
			txn.setAcctRole(customerAcctRole);
			txn.setCustomerAccnt(customerAccnt.value());
			out.collect(txn);
		} else {
			customerAccntRole.update(customerAcctRole);

		}
	}

}
