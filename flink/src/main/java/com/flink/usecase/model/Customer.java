package com.flink.usecase.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {
	@JsonProperty("cust_intrl_id")
	String custIntrlId;
	@JsonProperty("data_dump_dt")
	String dataDumpDt;
	@JsonProperty("cust_seq_id")
	String custSeqId;
	@JsonProperty("fncl_prfl_last_updt_dt")
	String fnclPrflLastUpdtDt;
	@JsonProperty("cust_type_cd")
	String custTypeCd;
	@JsonProperty("cust_add_dt")
	String custAddDt;
	@JsonProperty("tax_id")
	String taxId;
	@JsonProperty("tax_id_frmt_cd")
	String taxIdFrmtCd;
	@JsonProperty("eqty_knldg_cd")
	String eqtyKnldgCd;
	@JsonProperty("bnd_knldg_cd")
	String bndKnldgCd;
	@JsonProperty("optn_knldg_cd")
	String optnKnldgCd;
	@JsonProperty("ovrall_knldg_cd")
	String ovrallKnldgCd;
	@JsonProperty("ovrall_exp_cd")
	String ovrallExpCd;
	@JsonProperty("eqty_exp_yr_qt")
	String eqtyExpYrQt;
	@JsonProperty("bnd_exp_yr_qt")
	String bndExpYrQt;
	@JsonProperty("optn_exp_yr_qt")
	String optnExpYrQt;
	@JsonProperty("annl_eqty_trd_qt")
	String annlEqtyTrdQt;
	@JsonProperty("annl_bnd_trd_qt")
	String annlBndTrdQt;
	@JsonProperty("annl_optn_trd_qt")
	String annlOptnTrdQt;
	@JsonProperty("last_nm")
	String lastNm;
	@JsonProperty("birth_dt")
	String birthDt;
	@JsonProperty("ctzshp_cntry1_cd")
	String ctzshpCntry1Cd;
	@JsonProperty("ctzshp_cntry2_cd")
	String ctzshpCntry2Cd;
	@JsonProperty("res_cntry_cd")
	String resCntryCd;
	@JsonProperty("mplyr_nm")
	String mplyrNm;
	@JsonProperty("mplyr_fincl_inst_fl")
	String mplyrFinclInstFl;
	@JsonProperty("mplmt_stat_cd")
	String mplmtStatCd;
	@JsonProperty("mrtl_stat_cd")
	String mrtlStat_cd;
	@JsonProperty("dpndt_qt")
	String dpndtQt;
	@JsonProperty("ocptn_nm")
	String ocptnNm;
	@JsonProperty("org_nm")
	String orgNm;
	@JsonProperty("age_yr_ct")
	String ageYrCt;
	@JsonProperty("ctzshp_stat_cd")
	String ctzshpStatCd;
	@JsonProperty("wlth_src_dscr_tx")
	String wlthSrcDscrTx;
	@JsonProperty("emp_fl")
	String empFl;
	@JsonProperty("src_sys_cd")
	String srcSysCd;
	@JsonProperty("org_lgl_struc_cd")
	String orgLglStrucCd;
	@JsonProperty("pwd_last_chg_dt")
	String pwdLastChgDt;
	@JsonProperty("alias_nm")
	String aliasNm;
	@JsonProperty("frgn_assets_fl")
	String frgnAssetsFl;
	@JsonProperty("full_nm")
	String fullNm;
	@JsonProperty("mplyr_indus_cd")
	String mplyrIndusCd;
	@JsonProperty("job_titl_nm")
	String jobTitlNm;
	@JsonProperty("cust_efctv_risk_nb")
	Integer custEfctvRiskNb;
	@JsonProperty("cust_bus_risk_nb")
	String custBusRiskNb;
	@JsonProperty("cust_geo_risk_nb")
	String custGeoRiskNb;
	@JsonProperty("cstm_risk1_nb")
	String cstmRisk1Nb;
	@JsonProperty("cstm_risk2_nb")
	String cstmRisk2Nb;
	@JsonProperty("day_trd_knldg_cd")
	String dayTrdKnldgCd;
	@JsonProperty("day_trd_exp_cd")
	String dayTrdExpCd;
	@JsonProperty("prcsng_batch_nm")
	String prcsngBatchNm;
	@JsonProperty("jrsdcn_cd")
	String jrsdcnCd;
	@JsonProperty("bus_dmn_list_tx")
	String busDmnListTx;
	@JsonProperty("mantas_cust_bus_type_cd")
	String mantasCustBusTypeCd;
	@JsonProperty("cust_efctv_risk_factr_tx")
	String custEfctvRiskFactrTx;
	@JsonProperty("cust_stat_cd")
	String custStatCd;
	@JsonProperty("taxtn_cntry_cd")
	String taxtnCntryCd;
	@JsonProperty("cust_indus_cd")
	String custIndusCd;
	@JsonProperty("publc_privt_cd")
	String publcPrivtCd;
	@JsonProperty("alt_cust_id")
	String altCustId;
	@JsonProperty("incm_rng_cd")
	String incmRngCd;
	@JsonProperty("dmcld_brch_org_id")
	String dmcldBrchOrgId;
	@JsonProperty("cust_sub_type_cd")
	String custSubTypeCd;
	@JsonProperty("branch_cd")
	String branchCd;
	@JsonProperty("cust_indus_cd_type")
	String custIndusCdType;
	@JsonProperty("first_nm")
	String firstNm;
}
