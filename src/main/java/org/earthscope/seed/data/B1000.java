package org.earthscope.seed.data;


import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.codec.EncodingFormat;
import org.earthscope.seed.util.SeedByteBuffer;

import java.nio.ByteOrder;

@Getter
@Setter
public class B1000 extends DataBlockette {

	private EncodingFormat encodingFormat;
	private int recordLengthExponent;
	private ByteOrder byteOrder;
	private int reserved;

	public B1000() {
		super(1000, 8, "Data Only SEED Blockette");
	}

	/*-
	 * 	1 Blockette type — 1000 B 2
		2 Next blockette’s byte number B 2
		3 Encoding Format B 1
		4 Word order B 1
		5 Data Record Length B 1
		6 Reserved B 1
	 */
	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		validate();
		return SeedByteBuffer.allocate(8).order(byteOrder).putShort(1000).putShort(this.getNextBlocketteByteNumber())
				.putByte((byte) encodingFormat.getValue()).putByte((byte) (byteOrder == ByteOrder.LITTLE_ENDIAN ? 0 : 1))
				.putByte((byte) recordLengthExponent).putByte((byte) reserved).array();
	}

	@Override
	public String toString() {
		return "B1000 [encodingFormat=" + encodingFormat + ", recordLengthExponent=" + recordLengthExponent
				+ ", byteOrder=" + byteOrder + ", reserved=" + reserved + ", getNextBlocketteByteNumber()="
				+ getNextBlocketteByteNumber() + "]";
	}

	public void validate() throws SeedException {

		SeedException.SeedExceptionBuilder builder = SeedException.builder();
		builder.context("Validating B" + this.getType());
		if (this.recordLengthExponent < 8 || this.recordLengthExponent > 256) {
			builder.add("Invalid recordLengthExponent value [" + recordLengthExponent + "]",
					"Expected value between 8 and 256");
		}

		if (!builder.isEmpty()) {
			throw builder.build();
		}
	}

	public static B1000Builder builder() {
		return new B1000Builder();
	}

	public static class B1000Builder {
		private int nextBlocketteByteNumber;
		private EncodingFormat encodingFormat;
		private int recordLengthExponent;
		private ByteOrder byteOrder;
		private int reserved;

		public B1000Builder nextBlocketteByteNumber(int nextBlocketteByteNumber) {
			this.nextBlocketteByteNumber = nextBlocketteByteNumber;
			return this;
		}

		public B1000Builder encodingFormat(EncodingFormat encodingFormat) {
			this.encodingFormat = encodingFormat;
			return this;
		}

		public B1000Builder recordLengthExponent(int recordLengthExponent) {
			this.recordLengthExponent = recordLengthExponent;
			return this;
		}

		public B1000Builder byteOrder(ByteOrder byteOrder) {
			this.byteOrder = byteOrder;
			return this;
		}

		public B1000Builder reserved(int reserved) {
			this.reserved = reserved;
			return this;
		}

		public B1000 build() {
			B1000 b = new B1000();
			b.setByteOrder(byteOrder);
			b.setEncodingFormat(encodingFormat);
			b.setNextBlocketteByteNumber(nextBlocketteByteNumber);
			b.setRecordLengthExponent(recordLengthExponent);
			b.setReserved(reserved);
			return b;
		}
	}
}
