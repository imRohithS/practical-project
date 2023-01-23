package com.flink.usecase.model;

import java.util.List;

import lombok.Data;

@Data
public class CalculateExp {
	String whenExp;
	String thenExp;
	List<String> variablesWhen;
	List<String> variablesThen;
	String alertMsg;
}
