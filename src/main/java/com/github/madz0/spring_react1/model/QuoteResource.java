package com.github.madz0.spring_react1.model;

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
public class QuoteResource {

	private String type;
    private Quote value;
}
