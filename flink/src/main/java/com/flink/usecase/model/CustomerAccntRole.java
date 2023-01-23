package com.flink.usecase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerAccntRole {
	@JsonProperty("cust_acct_role_cd")
	String custAcctRoleCd;
	@JsonProperty("prcsng_batch_nm")
	String prcsngBatchNm;
	@JsonProperty("data_dump_dt")
	String dataDumpDt;
	@JsonProperty("cust_acct_role_seq_id")
	String custAcctRoleSeqId;
	@JsonProperty("src_sys_cd")
	String srcSysCd;
	@JsonProperty("wdrwl_auth_fl")
	String wdrwlAuthFl;
	@JsonProperty("poa_fl")
	String poaFl;
	@JsonProperty("acct_rlshp_fl")
	String acctRlshpFl;
	@JsonProperty("trdng_auth_fl")
	String trdngAuthFl;
	@JsonProperty("ownrshp_fl")
	String ownrshpFl;
	@JsonProperty("grdnshp_fl")
	String grdnshpFl;
	@JsonProperty("cust_acct_role_desc_tx")
	String custAcctRoleDescTx;
}
