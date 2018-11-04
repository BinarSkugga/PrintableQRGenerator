package com.binarskugga.examples;

import com.binarskugga.*;

import java.util.*;

public class ExampleQRGenerator extends QRSerialGenerator {

	@Override
	protected String nextValue(String previous) {
		return UUID.randomUUID().toString();
	}

}
