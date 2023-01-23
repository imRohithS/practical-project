package com.flink.usecase.function;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flink.usecase.model.CustomerAccnt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CustomerAccntObject implements FlatMapFunction<String, CustomerAccnt> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1452412414630359350L;

	@Override
	public void flatMap(String value, Collector<CustomerAccnt> out) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();

//		System.out.println(value);
//		Gson g2 = new Gson();
//		Type type = new TypeToken<ArrayList<CustomerAccnt>>() {
//		}.getType();
		List<CustomerAccnt> req = objectMapper.readValue(value, new TypeReference<ArrayList<CustomerAccnt>>() {
		});
//		List<CustomerAccnt> req = g2.fromJson(value, type);

		for (CustomerAccnt f : req) {
			out.collect(f);
		}

	}

}
