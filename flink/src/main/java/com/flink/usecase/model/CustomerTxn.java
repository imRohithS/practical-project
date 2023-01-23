package com.flink.usecase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerTxn {
	@JsonProperty("fo_trxn_seq_id")
	String foTrxnSeqId;
	@JsonProperty("trxn_intrl_ref_id")
	String trxnIntrlRefId;
	@JsonProperty("data_dump_dt")
	String dataDumpDt;
	@JsonProperty("src_sys_cd")
	String srcSysCd;
	@JsonProperty("dbt_cdt_cd")
	String dbtCdtCd;
	@JsonProperty("trxn_base_am")
	Double trxnBaseAm;
	@JsonProperty("acct_intrl_id")
	String acctIntrlId;
	@JsonProperty("trxn_exctn_dt")
	String trxnExctnDt;
	@JsonProperty("trxn_loc_nm")
	String trxnLocNm;
	@JsonProperty("trxn_loc_id")
	String trxnLocId;
	@JsonProperty("trxn_loc_id_type_cd")
	String trxnLocIdTypeCd;
	@JsonProperty("trxn_loc_addr_seq_id")
	String trxnLocAddrSeqId;
	@JsonProperty("trxn_chanl_cd")
	String trxnChanlCd;
	@JsonProperty("cash_trxn_ntity_risk_nb")
	String cashTrxnNtityRiskNb;
	@JsonProperty("cash_trxn_actvy_risk_nb")
	Integer cashTrxnActvyRiskNb;
	@JsonProperty("chanl_risk_nb")
	String chanlRiskNb;
	@JsonProperty("prdct_risk_nb")
	String prdctRiskNb;
	@JsonProperty("prcsng_batch_nm")
	String prcsngBatchNm;
	@JsonProperty("trxn_exctn_utc_dt")
	String trxnExctnUtcDt;
	@JsonProperty("trxn_actvy_am")
	String trxnActvyAm;
	@JsonProperty("trxn_actvy_crncy_cd")
	String trxnActvyCrncyCd;
	@JsonProperty("mantas_trxn_asset_class_cd")
	String mantasTrxnAssetClassCd;
	@JsonProperty("mantas_trxn_purp_cd")
	String mantasTrxnPurpCd;
	@JsonProperty("mantas_trxn_prdct_cd")
	String mantasTrxnPrdctCd;
	@JsonProperty("mantas_trxn_chanl_cd")
	String mantasTrxnChanlCd;
	@JsonProperty("cndtr_ntity_risk_nb")
	String cndtrNtityRiskNb;
	@JsonProperty("cndtr_actvy_risk_nb")
	String cndtrActvyRiskNb;
	@JsonProperty("trxn_ntity_risk_factr_tx")
	String trxnNtityRiskFactrTx;
	@JsonProperty("unrltd_party_fl")
	String unrltdPartyFl;
	@JsonProperty("mantas_trxn_chanl_risk_nb")
	String mantasTrxnChanlRiskNb;
	@JsonProperty("max_in_geo_risk_nb")
	String maxInGeoRiskNb;
	@JsonProperty("max_out_geo_risk_nb")
	String maxOutGeoRiskNb;
	@JsonProperty("fsdf_stg_src")
	String fsdfStgSrc;
	@JsonProperty("trxn_func_am")
	String trxnFuncAm;
	@JsonProperty("func_crncy_cd")
	String funcCrncyCd;
}
