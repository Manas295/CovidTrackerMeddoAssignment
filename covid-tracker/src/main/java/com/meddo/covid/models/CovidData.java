package com.meddo.covid.models;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CovidData {

	public CovidData() {
		super();
	}

	private Integer cured;
	private Integer deaths;
	private Integer noOfCases;
	private Integer activeCases;
	private String state;
}
