package com.binarskugga.examples;

import com.binarskugga.*;

import java.util.*;

public class ExampleQRGeneratorCollection extends QRSerialCollectionGenerator<SimplePojo> {

	public ExampleQRGeneratorCollection(List<SimplePojo> items) {
		super(items);
	}

	@Override
	protected String toValue(SimplePojo current) {
		return "SIMPLE-" + current.getId().toString();
	}

	@Override
	protected String toLabel(SimplePojo current) {
		return current.getName() + " " + current.getLastName().charAt(0) + ".";
	}

}
