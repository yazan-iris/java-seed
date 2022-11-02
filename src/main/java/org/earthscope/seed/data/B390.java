package org.earthscope.seed.data;

import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.BTime;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.util.SeedByteBuffer;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;

@Getter
@Setter
public class B390 extends DataBlockette {

	private BTime beginningOfCalibrationTime;
	private int reserved1;
	/*-
	 * Calibration flags: 
	 * [Bit 0] — If set: first pulse is positive 
	 * [Bit 1] — If set: calibration’s alternate sign 
	 * [Bit 2] — If set: calibration was automatic; if unset: manual 
	 * [Bit 3] — If set: calibration continued from previous record(s) 
	 * [Other bits reserved and must be zero.]
	 */
	private BitSet calibrationFlags;
	private long calibrationDuration;
	private float calibrationSignalAmplitude;

	private String channelWithCalibrationInput;
	private int reserved2;

	public B390() {
		super(390, 28, "Generic Calibration Blockette ");
	}

	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		validate();
		return SeedByteBuffer.allocate(28).order(byteOrder).putShort(390).putShort(this.getNextBlocketteByteNumber())
				.putTime(beginningOfCalibrationTime).putByte((byte) reserved1)
				.putByte(calibrationFlags == null ? 0
						: (calibrationFlags.toByteArray().length == 0 ? 0 : calibrationFlags.toByteArray()[0]))
				.putInt(calibrationDuration).putFloat(calibrationSignalAmplitude)
				.put(channelWithCalibrationInput, StandardCharsets.US_ASCII, 3).putByte((byte) reserved2).array();
	}

	public void validate() throws SeedException {

	}

	public static B390Builder builder() {
		return new B390Builder();
	}

	public static class B390Builder {
		private int nextBlocketteByteNumber;
		private BTime beginningOfCalibrationTime;
		private int reserved1;
		private BitSet calibrationFlags;
		private long calibrationDuration;
		private float calibrationSignalAmplitude;

		private String channelWithCalibrationInput;
		private int reserved2;

		public B390Builder nextBlocketteByteNumber(int nextBlocketteByteNumber) {
			this.nextBlocketteByteNumber = nextBlocketteByteNumber;
			return this;
		}

		public B390Builder beginningOfCalibrationTime(BTime beginningOfCalibrationTime) {
			this.beginningOfCalibrationTime = beginningOfCalibrationTime;
			return this;
		}

		public B390Builder reserved1(int reserved1) {
			this.reserved1 = reserved1;
			return this;
		}

		public B390Builder calibrationFlags(BitSet calibrationFlags) {
			this.calibrationFlags = calibrationFlags;
			return this;
		}

		public B390Builder calibrationDuration(long calibrationDuration) {
			this.calibrationDuration = calibrationDuration;
			return this;
		}

		public B390Builder calibrationSignalAmplitude(float calibrationSignalAmplitude) {
			this.calibrationSignalAmplitude = calibrationSignalAmplitude;
			return this;
		}

		public B390Builder channelWithCalibrationInput(String channelWithCalibrationInput) {
			this.channelWithCalibrationInput = channelWithCalibrationInput;
			return this;
		}

		public B390Builder reserved2(int reserved2) {
			this.reserved2 = reserved2;
			return this;
		}

		public B390 build() {
			B390 b = new B390();
			b.setNextBlocketteByteNumber(nextBlocketteByteNumber);
			b.setBeginningOfCalibrationTime(beginningOfCalibrationTime);
			b.setCalibrationDuration(calibrationDuration);
			b.setCalibrationFlags(calibrationFlags);
			b.setCalibrationSignalAmplitude(calibrationSignalAmplitude);
			b.setChannelWithCalibrationInput(channelWithCalibrationInput);
			b.setReserved1(reserved1);
			b.setReserved2(reserved2);
			return b;
		}
	}
}
