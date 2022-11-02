package org.earthscope.seed.codec;

import java.util.HashMap;
import java.util.Map;

public enum EncodingFormat {


	ASCII("ASCII text", 0), SHORT("16 bit integers", 1), INT24("24 bit integers", 2), INTEGER("32 bit integers", 3),
	FLOAT("IEEE floating point", 4), DOUBLE("IEEE double precision floating point", 5),
	STEIM1("STEIM (1) Compression", 10), STEIM2("STEIM (2) Compression", 11),
	STEIM3("STEIM (3) Compression", 12), CDSN("CDSN 16 bit gain ranged", 16),
	SRO("SRO Format", 30), DWWSSN("DWWSSN Gain Ranged Format", 32);

	private int value;

	private static Map<Integer, EncodingFormat> map = new HashMap<Integer, EncodingFormat>();
	static {
		for (EncodingFormat format : EncodingFormat.values()) {
			map.put(format.value, format);
		}
	}

	EncodingFormat(String dexcription, int value) {
		this.value = value;
	}

	public static EncodingFormat valueOf(int format) {
		return map.get(format);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}