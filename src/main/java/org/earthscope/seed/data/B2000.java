package org.earthscope.seed.data;

import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.util.SeedByteBuffer;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class B2000 extends DataBlockette {
	private int numberOfOpaqueHeaderFields;
	private int totalBlocketteLengthInBytes;
	private int offsetToOpaqueData;
	private long recordNumber;
	private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
	private BitSet opaqueDataFlags;

	private String[] opaqueHeaders;
	private List<String[]> opaqueData = new ArrayList<>();

	/**
	 * OPAQUE: Opaque Data - bytes of opaque data. Total length of opaque data in
	 * bytes is blockette_length - 15 - length (opaque_data_header_string)
	 */

	public B2000() {
		super(2000, -1, "Variable Length Opaque Data Blockette");
	}

	public void setOpaqueHeaders(String[] opaqueHeaders) {
		if (opaqueHeaders != null) {
			this.opaqueHeaders = opaqueHeaders;
			this.numberOfOpaqueHeaderFields = opaqueHeaders.length;
		}
	}

	public void addData(String... strings) {
		Objects.requireNonNull(strings, "B2000:opaqueData cannot be null");
		this.opaqueData.add(strings);
	}

	public int computeTotalBlocketteLengthInBytes() {
		int totalLength = 15;
		if (opaqueHeaders != null) {
			for (String s : opaqueHeaders) {
				totalLength += (s.length() + 1);

			}
		}
		for (String[] l : opaqueData) {
			for (String s : l) {
				totalLength += ((s == null) ? 1 : (s.length() + 1));
			}
		}
		return totalLength;
	}

	/*-
	 *  1 Blockette type — 2000 B 2 0
		2 Next blockette’s byte number B 2 2
		3 Total blockette length in bytes B 2 4
		4 Offset to Opaque Data B 2 6
		5 Record number B 4 8
		6 Data Word order B 1 12
		7 Opaque Data flags B 1 13
		8 Number of Opaque Header fields B 1 14
		9 Opaque Data Header fields V V 15
			a Record type
			b Vendor type
			c Model type
			d Software
			e Firmware
		10 Opaque Data Opaque
	 */
	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		validate();
		int totalLength = computeTotalBlocketteLengthInBytes();

		SeedByteBuffer seedByteArray = SeedByteBuffer.allocate(totalLength).order(byteOrder).putShort(2000)
				.putShort(this.getNextBlocketteByteNumber()).putShort(totalLength).putShort(offsetToOpaqueData)
				.putInt(recordNumber).putByte((byte) (byteOrder == ByteOrder.LITTLE_ENDIAN ? 0 : 1))
				.putByte(opaqueDataFlags == null ? (byte) 0
						: (opaqueDataFlags.toByteArray().length > 0 ? opaqueDataFlags.toByteArray()[0] : (byte) 0))
				.putByte((byte) (opaqueHeaders == null ? 0 : opaqueHeaders.length));
		for (String s : opaqueHeaders) {
			seedByteArray.putAndTerminate(s, StandardCharsets.US_ASCII, '~');
		}
		for (String[] l : opaqueData) {
			for (String s : l) {
				seedByteArray.putAndTerminate(s == null ? "" : s, StandardCharsets.US_ASCII, '~');
			}

		}
		return seedByteArray.array();
	}

	public void validate() throws SeedException {

	}

	public static B2000Builder builder() {
		return new B2000Builder();
	}

	public static class B2000Builder {
		private int nextBlocketteByteNumber;
		private int totalBlocketteLengthInBytes;
		private int offsetToOpaqueData;
		private int recordNumber;
		private ByteOrder byteOrder;
		private BitSet opaqueDataFlags;
		private String[] opaqueHeaders;
		private List<String[]> opaqueData = new ArrayList<>();

		public B2000Builder nextBlocketteByteNumber(int nextBlocketteByteNumber) {
			this.nextBlocketteByteNumber = nextBlocketteByteNumber;
			return this;
		}

		public B2000Builder totalBlocketteLengthInBytes(int totalBlocketteLengthInBytes) {
			this.totalBlocketteLengthInBytes = totalBlocketteLengthInBytes;
			return this;
		}

		public B2000Builder offsetToOpaqueData(int offsetToOpaqueData) {
			this.offsetToOpaqueData = offsetToOpaqueData;
			return this;
		}

		public B2000Builder recordNumber(int recordNumber) {
			this.recordNumber = recordNumber;
			return this;
		}

		public B2000Builder byteOrder(ByteOrder byteOrder) {
			this.byteOrder = byteOrder;
			return this;
		}

		public B2000Builder opaqueDataFlags(BitSet opaqueDataFlags) {
			this.opaqueDataFlags = opaqueDataFlags;
			return this;
		}

		public B2000Builder header(String... headers) {
			Objects.requireNonNull(headers, "headers for opaque data cannot be null");
			this.opaqueHeaders = headers;
			return this;
		}

		public B2000Builder addData(String... strings) {
			Objects.requireNonNull(strings, "opaqueData cannot be null");
			opaqueData.add(strings);
			return this;
		}

		public B2000 build() throws SeedException {
			B2000 b = new B2000();
			b.setNextBlocketteByteNumber(nextBlocketteByteNumber);
			b.setTotalBlocketteLengthInBytes(totalBlocketteLengthInBytes);
			b.setOffsetToOpaqueData(offsetToOpaqueData);
			b.setRecordNumber(recordNumber);
			b.setByteOrder(byteOrder);
			b.setOpaqueDataFlags(opaqueDataFlags);
			if (this.opaqueHeaders != null) {
				b.setOpaqueHeaders(opaqueHeaders);
			}
			if (opaqueData != null && !opaqueData.isEmpty()) {
				for (String[] array : opaqueData) {
					if (array.length != opaqueHeaders.length) {
						throw SeedException.builder().context("Creating B2000")
								.add("opaqueHeader is of length " + opaqueHeaders.length
										+ ", but this data row is of length " + array.length,
										"opaqueData row should contain the same number of columns as the header.")
								.build();
					}
					b.addData(array);
				}
			}
			return b;
		}
	}
}
