package com.flink.usecase.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flink.usecase.model.CustomerTxn;
import com.flink.usecase.model.Query;

public class QueryMapper implements FlatMapFunction<String, Query> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1349363999724530822L;

	@Override
	public void flatMap(String value, Collector<Query> out) throws Exception {

		ObjectMapper objectMapper = new ObjectMapper();
		Query req = objectMapper.readValue(value, new TypeReference<Query>() {
		});
		out.collect(req);
		
		System.out.println("Tables are connected and queries are inserted and ready to execute" + req);

	}

}
