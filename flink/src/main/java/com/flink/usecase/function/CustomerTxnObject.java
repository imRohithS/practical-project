package com.flink.usecase.function;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flink.usecase.model.CustomerTxn;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CustomerTxnObject implements FlatMapFunction<String, CustomerTxn> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1349363999724530822L;

	@Override
	public void flatMap(String value, Collector<CustomerTxn> out) throws Exception {
//		System.out.println(value);
//		Gson g2 = new Gson();
//		Type type = new TypeToken<ArrayList<CustomerTxn>>() {
//		}.getType();
//		List<CustomerTxn> req = g2.fromJson(value, type);

		ObjectMapper objectMapper = new ObjectMapper();
		List<CustomerTxn> req = objectMapper.readValue(value, new TypeReference<ArrayList<CustomerTxn>>() {
		});
		for (CustomerTxn f : req) {
			out.collect(f);
		}

	}

}
