package com.flink.usecase.function;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flink.usecase.model.Account;

public class AccountObject implements FlatMapFunction<String, Account> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 142587450353246456L;

	@Override
	public void flatMap(String value, Collector<Account> out) throws Exception {
//		System.out.println(value);
//		Gson g2 = new Gson();
//		Type type = new TypeToken<ArrayList<Account>>() {
//		}.getType();
//		List<Account> req = g2.fromJson(value, type);
		ObjectMapper objectMapper = new ObjectMapper();
		List<Account> req = objectMapper.readValue(value, new TypeReference<ArrayList<Account>>() {
		});
		for (Account f : req) {
			out.collect(f);
		}

	}

}
