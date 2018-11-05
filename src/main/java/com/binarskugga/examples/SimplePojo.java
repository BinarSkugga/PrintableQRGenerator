package com.binarskugga.examples;

import lombok.*;

import java.util.*;

@AllArgsConstructor @NoArgsConstructor
public class SimplePojo {

	@Getter @Setter
	private UUID id;

	@Getter @Setter
	private String name;

	@Getter @Setter
	private String lastName;

}
