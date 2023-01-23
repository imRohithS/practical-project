package com.flink.usecase.function;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.util.Collector;

import com.flink.usecase.model.CustomerTxn;
import com.flink.usecase.model.TxnDetails;

public class CalculateHR extends KeyedProcessFunction<String, TxnDetails, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient ValueState<Double> txnAmnt;

	@Override
	public void processElement(TxnDetails value, Context arg1, Collector<String> out) throws Exception {
		double sum = txnAmnt.value();
		sum = sum + value.getCustTxn().getTrxnBaseAm();
		txnAmnt.update(sum);
		if (sum > 1000) {
			out.collect("HR Customer NO-->" + value.getCust().getCustIntrlId() + "Account NO-->"
					+ value.getAcc().getAcctIntrlId() + "--txn amnt-->" + sum);
		}
	}

	@Override
	public void open(Configuration config) {
		ValueStateDescriptor<Double> descriptor = new ValueStateDescriptor<>("txnAmnt",
				TypeInformation.of(new TypeHint<Double>() {
				}), 0D);
		txnAmnt = getRuntimeContext().getState(descriptor);
	}

}
