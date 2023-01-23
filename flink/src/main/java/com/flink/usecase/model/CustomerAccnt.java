package com.flink.usecase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAccnt {
	@JsonProperty("cust_intrl_id")
	String custIntrlId;
	@JsonProperty("acct_intrl_id")
	String acctIntrlId;
	@JsonProperty("cust_acct_role_cd")
	String custAcctRoleCd;
	@JsonProperty("prcsng_batch_nm")
	String prcsngBatchNm;
	@JsonProperty("data_dump_dt")
	String dataDumpDt;
	@JsonProperty("cust_acct_seq_id")
	String custAcctSeqId;
	@JsonProperty("src_sys_cd")
	String srcSysCd;
}
