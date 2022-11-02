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
public class B310 extends DataBlockette {

	private BTime beginningOfCalibrationTime;
	private int reserved1;
	/**
	 * Calibration flags: [Bit 0] — If set: first pulse is positive [Bit 1] — If
	 * set: calibration’s alternate sign [Bit 2] — If set: calibration was
	 * automatic; if unset: manual [Bit 3] — If set: calibration continued from
	 * previous record(s) [Other bits reserved and must be zero.]
	 */
	private BitSet calibrationFlags;
	private long calibrationDuration;
	private float periodOfSignalInSeconds;

	private float amptitudeOfSignal;
	private String channelWithCalibrationInput;
	private int reserved2;
	private float referenceAmplitude;
	private String coupling;
	private String rolloff;

	public B310() {
		super(310, 60, "Sine Calibration Blockette ");
	}

	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		validate();
		return SeedByteBuffer.allocate(60).order(byteOrder).putShort(310).putShort(this.getNextBlocketteByteNumber())
				.putTime(beginningOfCalibrationTime).putByte((byte) reserved1)
				.putByte(calibrationFlags == null ? 0
						: (calibrationFlags.toByteArray().length == 0 ? 0 : calibrationFlags.toByteArray()[0]))
				.putInt(calibrationDuration).putFloat(periodOfSignalInSeconds).putFloat(amptitudeOfSignal)
				.put(channelWithCalibrationInput, StandardCharsets.US_ASCII, 3).putByte((byte) reserved2)
				.putFloat(referenceAmplitude).put(coupling, StandardCharsets.US_ASCII, 12)
				.put(rolloff, StandardCharsets.US_ASCII, 12).array();
	}

	public void validate() throws SeedException {

	}

	public static B310Builder builder() {
		return new B310Builder();
	}

	public static class B310Builder {

		private int nextBlocketteByteNumber;
		private BTime beginningOfCalibrationTime;
		private BitSet calibrationFlags;
		private long calibrationDuration;
		private float periodOfSignalInSeconds;

		private float amptitudeOfSignal;
		private String channelWithCalibrationInput;
		private int reserved1;
		private int reserved2;
		private float referenceAmplitude;
		private String coupling;
		private String rolloff;

		public B310Builder nextBlocketteByteNumber(int nextBlocketteByteNumber) {
			this.nextBlocketteByteNumber = nextBlocketteByteNumber;
			return this;
		}

		public B310Builder beginningOfCalibrationTime(BTime beginningOfCalibrationTime) {
			this.beginningOfCalibrationTime = beginningOfCalibrationTime;
			return this;
		}

		public B310Builder calibrationFlags(BitSet calibrationFlags) {
			this.calibrationFlags = calibrationFlags;
			return this;
		}

		public B310Builder calibrationDuration(long calibrationDuration) {
			this.calibrationDuration = calibrationDuration;
			return this;
		}

		public B310Builder periodOfSignalInSeconds(float periodOfSignalInSeconds) {
			this.periodOfSignalInSeconds = periodOfSignalInSeconds;
			return this;
		}

		public B310Builder amptitudeOfSignal(float amptitudeOfSignal) {
			this.amptitudeOfSignal = amptitudeOfSignal;
			return this;
		}

		public B310Builder channelWithCalibrationInput(String channelWithCalibrationInput) {
			this.channelWithCalibrationInput = channelWithCalibrationInput;
			return this;
		}

		public B310Builder reserved1(int reserved1) {
			this.reserved1 = reserved1;
			return this;
		}

		public B310Builder reserved2(int reserved2) {
			this.reserved2 = reserved2;
			return this;
		}

		public B310Builder referenceAmplitude(float referenceAmplitude) {
			this.referenceAmplitude = referenceAmplitude;
			return this;
		}

		public B310Builder coupling(String coupling) {
			this.coupling = coupling;
			return this;
		}

		public B310Builder rolloff(String rolloff) {
			this.rolloff = rolloff;
			return this;
		}

		public B310 build() {
			B310 b = new B310();
			b.setBeginningOfCalibrationTime(beginningOfCalibrationTime);
			b.setNextBlocketteByteNumber(nextBlocketteByteNumber);
			b.setCalibrationFlags(calibrationFlags);
			b.setCalibrationDuration(calibrationDuration);
			b.setPeriodOfSignalInSeconds(periodOfSignalInSeconds);
			b.setAmptitudeOfSignal(amptitudeOfSignal);
			b.setChannelWithCalibrationInput(channelWithCalibrationInput);
			b.setReserved1(reserved1);
			b.setReserved2(reserved2);
			b.setReferenceAmplitude(referenceAmplitude);
			b.setCoupling(coupling);
			b.setRolloff(rolloff);
			return b;
		}
	}
}
