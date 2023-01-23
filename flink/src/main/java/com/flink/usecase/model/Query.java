package com.flink.usecase.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
public class Query {
	@JsonProperty("calc")
	private List<Calc> calc;
}
