package com.flink.usecase.process.function;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;

import com.flink.usecase.model.Account;
import com.flink.usecase.model.Customer;
import com.flink.usecase.model.CustomerTxn;
import com.flink.usecase.model.TxnDetails;

public class CustomerAcctMapping extends KeyedCoProcessFunction<String, Customer, Account, TxnDetails> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient ValueState<TxnDetails> txnDetails;

	@Override
	public void processElement1(Customer arg0, Context arg1, Collector<TxnDetails> out) throws Exception {
		TxnDetails txn = txnDetails.value();
		if (txn != null) {
			txn.setCust(arg0);
			txnDetails.update(txn);
			out.collect(txnDetails.value());
		} else {
			TxnDetails txnNew = new TxnDetails();
			txnNew.setCust(arg0);
			txnDetails.update(txnNew);
		}
	}

	@Override
	public void processElement2(Account arg0, Context arg1, Collector<TxnDetails> out) throws Exception {
//		TxnDetails txn = txnDetails.value();
//		txn.setAcc(arg0);
//		txnDetails.update(txn);
//		out.collect(txnDetails.value());
		TxnDetails txn = txnDetails.value();
		if (txn != null) {
			txn.setAcc(arg0);
			txnDetails.update(txn);
			out.collect(txnDetails.value());
		} else {
			TxnDetails txnNew = new TxnDetails();
			txnNew.setAcc(arg0);
			txnDetails.update(txnNew);
		}
	}

	@Override
	public void open(Configuration config) {

		txnDetails = getRuntimeContext().getState(new ValueStateDescriptor<>("txnDetails", TxnDetails.class));
	}

}
