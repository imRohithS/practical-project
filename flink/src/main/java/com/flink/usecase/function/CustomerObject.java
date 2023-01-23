package com.flink.usecase.function;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flink.usecase.model.Customer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class CustomerObject implements FlatMapFunction<String, Customer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void flatMap(String value, Collector<Customer> out) throws Exception {
//		System.out.println(value);
//		Gson g2 = new Gson();
//		Type type = new TypeToken<ArrayList<Customer>>() {
//		}.getType();
//		List<Customer> req = g2.fromJson(value, type);
		ObjectMapper objectMapper = new ObjectMapper();
		List<Customer> req = objectMapper.readValue(value, new TypeReference<ArrayList<Customer>>() {
		});
		for (Customer f : req) {
			out.collect(f);
		}

	}

}
