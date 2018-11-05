package com.binarskugga.examples;

import com.binarskugga.*;

import java.util.*;

public class ExampleQRGenerator extends QRSerialGenerator<UUID> {

	@Override
	protected UUID nextValue(UUID previous) {
		return UUID.randomUUID();
	}

	@Override
	protected String toLabel(UUID current) {
		return "UUID v" + current.version();
	}

}
