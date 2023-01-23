package com.flink.usecase.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
public class Then {
	@JsonProperty("must")
	List<Must> must;
	@JsonProperty("alert")
	String alert;
}
