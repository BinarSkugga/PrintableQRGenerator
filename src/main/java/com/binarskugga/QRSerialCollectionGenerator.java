package com.binarskugga;

import lombok.*;

import java.util.*;

public abstract class QRSerialCollectionGenerator<T> extends QRSerialGenerator<T> {

	@Getter
	private List<T> items;
	private Iterator<T> iterator;

	public QRSerialCollectionGenerator(List<T> items) {
		this.items = items;
		this.iterator = this.items.iterator();
		this.batchSize = items.size();
	}

	@Override
	protected T nextValue(T previous) {
		return this.iterator.next();
	}

	@Override
	public QRSerialGenerator<T> setBatchSize(int batchSize) {
		throw new IllegalStateException("Cannot change batch size in a collection generator.");
	}
}
