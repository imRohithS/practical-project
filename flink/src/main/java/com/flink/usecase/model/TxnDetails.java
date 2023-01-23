package com.flink.usecase.model;

import lombok.Data;

@Data
public class TxnDetails {
	private CustomerTxn custTxn;
	private Account acc;
	private Customer cust;
	private CustomerAccnt customerAccnt;
	private CustomerAccntRole acctRole;
}
