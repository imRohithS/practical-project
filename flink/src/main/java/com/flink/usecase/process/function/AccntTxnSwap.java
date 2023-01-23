package com.flink.usecase.process.function;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.co.CoProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;

import com.flink.usecase.model.CustomerTxn;
import com.flink.usecase.model.TxnDetails;

public class AccntTxnSwap extends KeyedCoProcessFunction<String, CustomerTxn, TxnDetails, TxnDetails> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient ValueState<TxnDetails> txnDetails;
	private transient ValueState<CustomerTxn> custTxnList;

	@Override
	public void open(Configuration config) {
		txnDetails = getRuntimeContext().getState(new ValueStateDescriptor<>("txnDetails", TxnDetails.class));
		custTxnList = getRuntimeContext().getState(new ValueStateDescriptor<>("custTxnList", CustomerTxn.class));
	}

	@Override
	public void processElement1(CustomerTxn arg0, Context arg1, Collector<TxnDetails> out) throws Exception {
		TxnDetails txn = txnDetails.value();
//		txn.setCustTxn(arg0);
//		txnDetails.update(txn);
//		out.collect(txnDetails.value());
		if (txn != null) {
			TxnDetails txnNew = new TxnDetails();
			txnNew.setAcc(txn.getAcc());
			txnNew.setCust(txn.getCust());
			txnNew.setCustTxn(arg0);
			out.collect(txnNew);
		} else {
			custTxnList.update(arg0);
		}
	}

	@Override
	public void processElement2(TxnDetails arg0, Context arg1, Collector<TxnDetails> out) throws Exception {
//		TxnDetails txn = txnDetails.value();
//		txn.setCust(arg0.getCust());
//		txn.setAcc(arg0.getAcc());
//		txnDetails.update(txn);
//		out.collect(txnDetails.value());
		if (custTxnList.value() != null) {
			arg0.setCustTxn(custTxnList.value());
			txnDetails.update(arg0);
			out.collect(arg0);
		} else {
			txnDetails.update(arg0);
		}
	}

}
