package flink;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.streaming.api.datastream.BroadcastStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main {

	public static void main(String[] args) throws Exception {
		flinkIntegartion("");
	}

	private static void flinkIntegartion(String string) throws Exception {

		System.err.close();
		System.setErr(System.out);

		String request = "[{\"name\":\"rohith\",\"id\":123,\"earnings\":100000.0,\"jrdn\":\"india\"},{\"name\":\"rohith\",\"id\":123,\"earnings\":100000.0,\"jrdn\":\"india\"},{\"name\":\"test\",\"id\":123,\"earnings\":10000.0,\"jrdn\":\"usa\"}]";
		String inputTopic = "sample_1";
		String address = "localhost:9092";
		String consumerGroup = "group-id";
		FlinkKafkaConsumer<String> flinkKafkaConsumer = createStringConsumerForTopic(inputTopic, address,
				consumerGroup);
		StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
		DataStream<String> stringInputStream = env.addSource(flinkKafkaConsumer);
//		List<Threshold> list = new ArrayList<>();
//		Threshold t = new Threshold();
//		t.setKey("india");
//		t.setValue(100000D);
//		list.add(t);
//		Threshold t1 = new Threshold();
//		t1.setKey("india");
//		t1.setValue(100000D);
//		list.add(t1);
//		Threshold t2 = new Threshold();
//		t2.setKey("india");
//		t2.setValue(100000D);
//		list.add(t2);

//		Map<String, Double> thresholdmap = new HashMap<>();
//		thresholdmap.put("india", 100000D);
//		thresholdmap.put("usa", 10000D);
//		thresholdmap.put("japan", 20000D);
//		DataStream<Map<String, Double>> thresoldStream = env.fromElements(thresholdmap);

//		ValueStateDescriptor<Map<String, Double>> descriptorMap = new ValueStateDescriptor<>("map",
//				TypeInformation.of(new TypeHint<Map<String, Double>>() {
//				}), thresholdmap);

//		MapStateDescriptor<String, Map<String, Double>> ruleStateDescriptor = new MapStateDescriptor<>(
//				"RulesBroadcastState", BasicTypeInfo.STRING_TYPE_INFO,
//				TypeInformation.of(new TypeHint<Map<String, Double>>() {
//				}));
//		BroadcastStream<Map<String, Double>> ruleBroadcastStream = stringInputStream.broadcast(ruleStateDescriptor);
//		DataStream<Threshold> threshold = env.fromCollection(list)
//				.flatMap(new FlatMapFunction<List<Threshold>, Threshold>() {
//
//					@Override
//					public void flatMap(List<Threshold> value, Collector<Threshold> out) throws Exception {
//						// TODO Auto-generated method stub
//						for (Threshold threshold : list) {
//							out.collect(threshold);
//						}
//					}
//
//				});

		DataStream<FlinkRequest> flinkGroup = stringInputStream.flatMap(new EarningsCount())
				.keyBy(jrdn -> jrdn.getJrdn());
		DataStream<Threshold> threshold = env.fromElements(new Threshold("india", 100D),
				new Threshold("usa", 200D), new Threshold("japan", 20000D)).keyBy(t -> t.getKey());
		flinkGroup.connect(threshold).process(new CalculateJuristiction()).print();

		
//		Pattern<FlinkRequest, ?> pattern=Pattern.<FlinkRequest>begin("first").subtype(FlinkRequest.class).where);
		
//				.process(new CalculateJuristiction()).print();

//		Gson g = new Gson();
//		Type flinkListType = new TypeToken<ArrayList<FlinkRequest>>() {
//		}.getType();
//		env.fromElements(new Threshold("India", 100000));

//		List<FlinkRequest> req = g.fromJson(request, flinkListType);
//		DataStream<List<FlinkRequest>> result = env.fromElements(req);
//		DataStream<Tuple3<String, String, Double>> out = stringInputStream.flatMap(new EarningsCount())
//				.keyBy(value -> value.f0);
//		stringInputStream.flatMap(new EarningsCount()).keyBy(new KeySelector<FlinkRequest, String>() {
//
//			/**
//			 * 
//			 */
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public String getKey(FlinkRequest value) throws Exception {
//				return value.getJrdn();
//			}
//		}).process(new CalculateJuristiction()).print();

//		stringInputStream.map((MapFunction<String, R>) new JsonToString()).print();
//		DataStream<String> output = stringInputStream.flatMap((String reqString, Collector<String> out) -> {
//
//			Gson g2 = new Gson();
//			Type type = new TypeToken<ArrayList<FlinkRequest>>() {
//			}.getType();
//			List<FlinkRequest> value = g2.fromJson(reqString, type);
//			System.out.println("--->req" + value);
//			value.stream().filter(f -> f.getJrdn().equalsIgnoreCase("India"))
//					.collect(
//							Collectors.groupingBy(fl -> fl.getName(), Collectors.summingDouble(fl -> fl.getEarnings())))
//					.forEach((name, ear) -> {
//						if (ear > 100000) {
//							System.out.println("India Limit exceeded for -->" + name + "-- earnings total-- " + ear);
//							out.collect("India Limit exceeded for -->" + name + "-- earnings total-- " + ear);
//						}
//					});
//			value.stream().filter(f -> f.getJrdn().equalsIgnoreCase("usa"))
//					.collect(
//							Collectors.groupingBy(fl -> fl.getName(), Collectors.summingDouble(fl -> fl.getEarnings())))
//					.forEach((name, ear) -> {
//						if (ear >= 20000) {
//							System.out.println("USA Limit exceeded for -->" + name + "-- earnings total-- " + ear);
//							out.collect("USA Limit exceeded for -->" + name + "-- earnings total-- " + ear);
//						}
//					});
//			value.stream().filter(f -> f.getJrdn().equalsIgnoreCase("japan"))
//					.collect(
//							Collectors.groupingBy(fl -> fl.getName(), Collectors.summingDouble(fl -> fl.getEarnings())))
//					.forEach((name, ear) -> {
//						if (ear > 100000) {
//							System.out.println("Japan Limit exceeded for -->" + name + "-- earnings total-- " + ear);
//							out.collect("USA Limit exceeded for -->" + name + "-- earnings total-- " + ear);
//						}
//					});
////			}
//		}).returns(Types.STRING);

//		output.print();
//		earningsCount.print();

//		DataStream<String> dataStream = env.fromElements("This is a first sentence",
//				"This is a second sentence with a one word");
//		out.print();
		env.execute();
		System.out.println("123");

	}

	public static FlinkKafkaConsumer<String> createStringConsumerForTopic(String topic, String kafkaAddress,
			String kafkaGroup) {

		Properties props = new Properties();
		props.setProperty("bootstrap.servers", kafkaAddress);
		props.setProperty("group.id", kafkaGroup);
		FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(topic, new SimpleStringSchema(), props);

		return consumer;
	}

}
