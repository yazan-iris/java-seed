package org.earthscope.seed.data;


import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.BTime;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.util.SeedByteBuffer;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

@Getter
@Setter
public class B500 extends DataBlockette {

	private float vocCorrection;
	private BTime timeOfException;
	private int clockTime;
	private int receptionQuality;
	private int exceptionCount;
	private String exceptionType;
	private String clockModel;
	private String clockStatus;

	public B500() {
		super(500, 200, "Timing Blockette");
	}

	/*-
	 * 	1 Blockette type — 500 B 2
		2 Next blockette offset B 2
		3 VCO correction B 4
		4 Time of exception B 10
		5 µsec B 1
		6 Reception Quality B 1
		7 Exception count B 4
		8 Exception type A 16
		9 Clock model A 32
		10 Clock status A 128
	 */
	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		validate();
		SeedByteBuffer array = SeedByteBuffer.allocate(200).order(byteOrder).putShort(500);
		array.putShort(this.getNextBlocketteByteNumber());
		array.putFloat(vocCorrection);
		array.putTime(timeOfException);
		array.putByte((byte) clockTime);
		array.putByte((byte) receptionQuality);
		array.putFloat(exceptionCount);
		array.put(exceptionType, StandardCharsets.US_ASCII, 16);
		array.put(clockModel, StandardCharsets.US_ASCII, 32);
		array.put(clockStatus, StandardCharsets.US_ASCII, 128);
		return array.array();
	}

	public void validate() throws SeedException {
		SeedException.SeedExceptionBuilder builder = SeedException.builder();
		builder.context("Validating B" + this.getType());
		if (this.vocCorrection > 100 || this.vocCorrection < 0) {
			builder.add("Invalid VCO value [" + vocCorrection + "]",
					"Expected a floating point percentage from 0.0 to 100.0% [0.0 is slowest, 100.0% is fastest");
		}
		if (this.clockTime > 99 || this.vocCorrection < 0) {
			builder.add("Invalid clock time value [" + clockTime + "]",
					"Value may be from 0 to +99 µsecs");
		}

		if (this.receptionQuality > 100 || this.receptionQuality < 0) {
			builder.add("Invalid Reception quality value [" + receptionQuality + "]",
					"Reception quality is a number from 0 to 100%");
		}
		if (!builder.isEmpty()) {
			throw builder.build();
		}
	}

	public static B500Builder builder() {
		return new B500Builder();
	}

	public static class B500Builder {
		private int nextBlocketteByteNumber;
		private float vocCorrection;
		private BTime timeOfException;
		private int clockTime;
		private int receptionQuality;
		private int exceptionCount;
		private String exceptionType;
		private String clockModel;
		private String clockStatus;

		public B500Builder nextBlocketteByteNumber(int nextBlocketteByteNumber) {
			this.nextBlocketteByteNumber = nextBlocketteByteNumber;
			return this;
		}

		public B500Builder vocCorrection(float vocCorrection) {
			this.vocCorrection = vocCorrection;
			return this;
		}

		public B500Builder timeOfException(BTime timeOfException) {
			this.timeOfException = timeOfException;
			return this;
		}

		public B500Builder clockTime(int clockTime) {
			this.clockTime = clockTime;
			return this;
		}

		public B500Builder receptionQuality(int receptionQuality) {
			this.receptionQuality = receptionQuality;
			return this;
		}

		public B500Builder exceptionCount(int exceptionCount) {
			this.exceptionCount = exceptionCount;
			return this;
		}

		public B500Builder exceptionType(String exceptionType) {
			this.exceptionType = exceptionType;
			return this;
		}

		public B500Builder clockModel(String clockModel) {
			this.clockModel = clockModel;
			return this;
		}

		public B500Builder clockStatus(String clockStatus) {
			this.clockStatus = clockStatus;
			return this;
		}

		public B500 build() throws SeedException {
			B500 b = new B500();
			b.setNextBlocketteByteNumber(nextBlocketteByteNumber);
			b.setVocCorrection(vocCorrection);
			b.setTimeOfException(timeOfException);
			b.setClockTime(clockTime);
			b.setReceptionQuality(receptionQuality);
			b.setExceptionCount(exceptionCount);
			b.setExceptionType(exceptionType);
			b.setClockModel(clockModel);
			b.setClockStatus(clockStatus);
			b.validate();
			return b;
		}
	}
}
