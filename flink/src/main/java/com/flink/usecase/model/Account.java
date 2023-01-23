package com.flink.usecase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
	@JsonProperty("acct_intrl_id")
	String acctIntrlId;
	@JsonProperty("acct_seq_id")
	String acctSeqId;
	@JsonProperty("prcsng_batch_nm")
	String prcsngBatchNm;
	@JsonProperty("data_dump_dt")
	String dataDumpDt;
	@JsonProperty("rgstn_type_cd")
	String rgstnTypeCd;
	@JsonProperty("acct_type1_cd")
	String acctType1Cd;
	@JsonProperty("acct_open_dt")
	String acctOpenDt;
	@JsonProperty("acct_tax_id")
	String acctTaxId;
	@JsonProperty("tax_id_frmt_cd")
	String taxIdFrmtCd;
	@JsonProperty("tax_wthld_cd")
	String taxWthldCd;
	@JsonProperty("dscrn_fl")
	String dscrnFl;
	@JsonProperty("emp_acct_fl")
	String emp_acctFl;
	@JsonProperty("test_acct_fl")
	String testAcctFl;
	@JsonProperty("acct_stat_cd")
	String acctStatCd;
	@JsonProperty("acct_stat_dt")
	String acctStatDt;
	@JsonProperty("last_actvy_dt")
	String lastActvyDt;
	@JsonProperty("src_sys_cd")
	String srcSysCd;
	@JsonProperty("acct_dsply_nm")
	String acctDsplyNm;
	@JsonProperty("acct_bus_risk_nb")
	String acctBusRiskNb;
	@JsonProperty("acct_cust_risk_nb")
	String acctCustRiskNb;
	@JsonProperty("acct_geo_risk_nb")
	String acctGeoRiskNb;
	@JsonProperty("stmt_cust_qt")
	String stmtCustQt;
	@JsonProperty("last_stmt_dt")
	String lastStmtDt;
	@JsonProperty("stmt_supr_fl")
	String stmtSuprFl;
	@JsonProperty("notfy_ltr_supr_fl")
	String notfyLtrSuprFl;
	@JsonProperty("legal_ntity_id")
	String legalNtityId;
	@JsonProperty("globl_rlshp_fl")
	String globlRlshpFl;
	@JsonProperty("acct_efctv_risk_nb")
	String acctEfctvRiskNb;
	@JsonProperty("cstm_risk1_nb")
	String cstmRisk1Nb;
	@JsonProperty("cstm_risk2_nb")
	String cstmRisk2Nb;
	@JsonProperty("alt_acct_id")
	String altAcctId;
	@JsonProperty("mantas_acct_holdr_type_cd")
	String mantasAcctHoldrTypeCd;
	@JsonProperty("mantas_acct_bus_type_cd")
	String mantasAcctBusTypeCd;
	@JsonProperty("mantas_acct_ownrshp_type_cd")
	String mantasAcctOwnrshpTypeCd;
	@JsonProperty("mantas_acct_purp_cd")
	String mantasAcctPurpCd;
	@JsonProperty("retrmt_acct_fl")
	String retrmtAcctFl;
	@JsonProperty("bus_dmn_list_tx")
	String busDmnListTx;
	@JsonProperty("jrsdcn_cd")
	String jrsdcnCd;
	@JsonProperty("cash_rpt_exmpt_fl")
	String cashRptExmptFl;
	@JsonProperty("acct_efctv_risk_factr_tx")
	String acctEfctvRiskFactrTx;
	@JsonProperty("acct_peer_grp_intrl_id")
	String acctPeerGrpIntrlId;
	@JsonProperty("high_prfl_fl")
	String highPrflFl;
	@JsonProperty("src_init_funds")
	String srcInitFunds;
	@JsonProperty("mthd_init_funds")
	String mthdInitFunds;
	@JsonProperty("mthd_acct_opng")
	String mthdAcctOpng;
	@JsonProperty("cpcty_cd")
	String cpctyCd;
	@JsonProperty("bllng_prdct_cd")
	String bllngPrdctCd;
	@JsonProperty("mgr_nm")
	String mgrNm;
	@JsonProperty("lead_acct_fl")
	String leadAcctFl;
	@JsonProperty("lead_track_cd")
	String leadTrackCd;
	@JsonProperty("cpi_fl")
	String cpiFl;
	@JsonProperty("extrl_nvsmt_acct_fl")
	String extrlNvsmtAcctFl;
	@JsonProperty("fsdf_stg_src")
	String fsdfStgSrc;
	@JsonProperty("tax_payr_cust_intrl_id")
	String taxPayrCustIntrlId;
	@JsonProperty("hh_acct_grp_id")
	String hhAcctGrpId;
	@JsonProperty("dmcld_brch_cd")
	String dmcldBrch_cd;
	@JsonProperty("prmry_cust_intrl_id")
	String prmryCustIntrlId;
	@JsonProperty("func_crncy_cd")
	String funcCrncyCd;
	@JsonProperty("sgmnt_id")
	String sgmntId;
	@JsonProperty("mktg_camp_cd")
	String mktgCampCd;
}
