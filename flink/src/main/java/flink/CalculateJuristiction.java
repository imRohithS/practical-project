package flink;

import java.util.HashMap;
import java.util.Map;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedBroadcastProcessFunction;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;

public class CalculateJuristiction extends KeyedCoProcessFunction<String, FlinkRequest, Threshold, String> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private transient ValueState<Double> sum;
	private transient ValueState<Double> sumThreshold;
//	private transient ValueState<Map<String, Double>> threshold;

//	@Override
//	public void processElement(FlinkRequest arg0, Context arg1, Collector<String> output) throws Exception {
//		double india = sum.value();
//		double updated = india + arg0.getEarnings();
//		sum.update(updated);
//		if (updated > threshold.value().get(arg1.getCurrentKey())) {
//			output.collect(arg1.getCurrentKey() + " Limit exceeded -->" + sum.value());
//		}
//
//	}

	@Override
	public void open(Configuration config) {
		ValueStateDescriptor<Double> descriptor = new ValueStateDescriptor<>("average",
				TypeInformation.of(new TypeHint<Double>() {
				}), 0D);
		ValueStateDescriptor<Double> descriptortotal = new ValueStateDescriptor<>("thres",
				TypeInformation.of(new TypeHint<Double>() {
				}), 0D);
		sum = getRuntimeContext().getState(descriptor);
		sumThreshold = getRuntimeContext().getState(descriptortotal);
//		Map<String, Double> thresholdmap = new HashMap<>();
//		thresholdmap.put("india", 100000D);
//		thresholdmap.put("usa", 10000D);
//		thresholdmap.put("japan", 20000D);
//		ValueStateDescriptor<Map<String, Double>> descriptorMap = new ValueStateDescriptor<>("map",
//				TypeInformation.of(new TypeHint<Map<String, Double>>() {
//				}), thresholdmap);
//		threshold = getRuntimeContext().getState(descriptorMap);
	}

//	@Override
//	public void processBroadcastElement(Threshold arg0,
//			KeyedBroadcastProcessFunction<String, FlinkRequest, Threshold, String>.Context arg1, Collector<String> output)
//			throws Exception {
//		// TODO Auto-generated method stub
//		System.out.println("------Threshold Object key--------" + arg0.getKey());
//		System.out.println("------Threshold Object value--------" + arg0.getValue());
//		System.out.println("------Threshold Object sum value--------" + sum.value());
//		if (sum.value() > arg0.getValue()) {
//			output.collect(arg0.getKey() + " Limit exceeded -->" + sum.value());
//		}
//	}
//
//	@Override
//	public void processElement(FlinkRequest arg0,
//			KeyedBroadcastProcessFunction<String, FlinkRequest, Threshold, String>.ReadOnlyContext arg1,
//			Collector<String> arg2) throws Exception {
//		// TODO Auto-generated method stub
//		double india = sum.value();
//		double updated = india + arg0.getEarnings();
//		sum.update(updated);
//		System.out.println("------Flink Object Jrdn--------" + arg0.getJrdn());
//		System.out.println("------Flink Object earnings--------" + arg0.getEarnings());
//		System.out.println("------Flink Object sum value--------" + sum.value());
//
//	}

	@Override
	public void processElement1(FlinkRequest arg0, Context arg1, Collector<String> output) throws Exception {
		double india = sum.value();
		double updated = india + arg0.getEarnings();
		sum.update(updated);
//		arg1.output(null, updated);
//		arg1.
		if (sum.value() > sumThreshold.value()) {
			output.collect(arg1.getCurrentKey() + " Limit exceeded -->" + sum.value());
		}

	}

	@Override
	public void processElement2(Threshold arg0, Context arg1, Collector<String> output) throws Exception {
		sumThreshold.update(arg0.getValue());

	}

}
