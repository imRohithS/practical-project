package com.flink.usecase.process;

import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.util.Collector;

import com.flink.usecase.main.FlinkIntegration;
import com.flink.usecase.model.CustomerTxn;
import com.flink.usecase.model.Query;
import com.flink.usecase.model.TxnDetails;

public class CustAcctTxnMap extends KeyedBroadcastProcessFunction<String, TxnDetails, CustomerTxn, TxnDetails> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void processBroadcastElement(CustomerTxn value, Context ctx, Collector<TxnDetails> out) throws Exception {
		ctx.getBroadcastState(FlinkIntegration.txnAccDescriptor).put(value.getAcctIntrlId(), value);

	}

	@Override
	public void processElement(TxnDetails arg0, ReadOnlyContext ctx, Collector<TxnDetails> out) throws Exception {
		CustomerTxn customerTxn = ctx.getBroadcastState(FlinkIntegration.txnAccDescriptor)
				.get(arg0.getAcc().getAcctIntrlId());
		if (customerTxn != null) {
			arg0.setCustTxn(customerTxn);
			out.collect(arg0);
		}

	}

}
