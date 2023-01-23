package flink;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EarningsCount implements FlatMapFunction<String, FlinkRequest> {

	@Override
	public void flatMap(String value, Collector<FlinkRequest> out) throws Exception {
		Gson g2 = new Gson();
		Type type = new TypeToken<ArrayList<FlinkRequest>>() {
		}.getType();
		List<FlinkRequest> req = g2.fromJson(value, type);

		for (FlinkRequest f : req) {
			out.collect(f);
		}

	}

}
