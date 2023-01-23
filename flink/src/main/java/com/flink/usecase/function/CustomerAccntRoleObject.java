package com.flink.usecase.function;

import java.util.ArrayList;
import java.util.List;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flink.usecase.model.CustomerAccntRole;

public class CustomerAccntRoleObject implements FlatMapFunction<String, CustomerAccntRole> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3823035895667512575L;

	@Override
	public void flatMap(String value, Collector<CustomerAccntRole> out) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		List<CustomerAccntRole> req = objectMapper.readValue(value, new TypeReference<ArrayList<CustomerAccntRole>>() {
		});
		for (CustomerAccntRole f : req) {
			out.collect(f);
		}

	}

}
