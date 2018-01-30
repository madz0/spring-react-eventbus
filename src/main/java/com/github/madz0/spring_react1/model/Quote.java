package com.github.madz0.spring_react1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Mohamad Zeinali
 *
 * Jan 24, 2018
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown=true)
public class Quote {

	 private Long id;
	 private String quote;
}
