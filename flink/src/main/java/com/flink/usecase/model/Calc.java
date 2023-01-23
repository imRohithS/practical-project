package com.flink.usecase.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder
public class Calc {
	@JsonProperty("name")
	String name;
	@JsonProperty("stream_id")
	String streamId;
	@JsonProperty("proc_seq_num")
	String procSeqNum;
	@JsonProperty("tables_to_connect")
	List<String> tablesToConnect;
	@JsonProperty("when")
	List<When> when;
	@JsonProperty("then")
	List<Then> then;

}
