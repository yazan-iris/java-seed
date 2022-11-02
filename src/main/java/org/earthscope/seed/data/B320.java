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
public class B320 extends DataBlockette {

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

	private float peakToPeakAmplitudeOfSteps;
	private String channelWithCalibrationInput;
	private int reserved2;
	private float referenceAmplitude;
	private String coupling;
	private String rolloff;
	private String noiseType;

	public B320() {
		super(320, 64, " Pseudo-random Calibration Blockette");
	}

	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		validate();
		return SeedByteBuffer.allocate(64).order(byteOrder).putShort(320).putShort(this.getNextBlocketteByteNumber())
				.putTime(beginningOfCalibrationTime).putByte((byte) reserved1)
				.putByte(calibrationFlags == null ? 0
						: (calibrationFlags.toByteArray().length == 0 ? 0 : calibrationFlags.toByteArray()[0]))
				.putInt(calibrationDuration).putFloat(peakToPeakAmplitudeOfSteps)
				.put(channelWithCalibrationInput, StandardCharsets.US_ASCII, 3).putByte((byte) reserved2)
				.putFloat(referenceAmplitude).put(coupling, StandardCharsets.US_ASCII, 12)
				.put(rolloff, StandardCharsets.US_ASCII, 12).put(noiseType, StandardCharsets.US_ASCII, 8).array();
	}

	public void validate() throws SeedException {

	}

	public static B320Builder builder() {
		return new B320Builder();
	}

	public static class B320Builder {
		private int nextBlocketteByteNumber;
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
		private float peakToPeakAmplitudeOfSteps;
		private String channelWithCalibrationInput;
		private int reserved2;
		private float referenceAmplitude;
		private String coupling;
		private String rolloff;
		private String noiseType;

		public B320Builder nextBlocketteByteNumber(int nextBlocketteByteNumber) {
			this.nextBlocketteByteNumber = nextBlocketteByteNumber;
			return this;
		}

		public B320Builder beginningOfCalibrationTime(BTime beginningOfCalibrationTime) {
			this.beginningOfCalibrationTime = beginningOfCalibrationTime;
			return this;
		}

		public B320Builder calibrationFlags(BitSet calibrationFlags) {
			this.calibrationFlags = calibrationFlags;
			return this;
		}

		public B320Builder reserved1(int reserved1) {
			this.reserved1 = reserved1;
			return this;
		}

		public B320Builder calibrationDuration(long calibrationDuration) {
			this.calibrationDuration = calibrationDuration;
			return this;
		}

		public B320Builder peakToPeakAmplitudeOfSteps(float peakToPeakAmplitudeOfSteps) {
			this.peakToPeakAmplitudeOfSteps = peakToPeakAmplitudeOfSteps;
			return this;
		}

		public B320Builder channelWithCalibrationInput(String channelWithCalibrationInput) {
			this.channelWithCalibrationInput = channelWithCalibrationInput;
			return this;
		}

		public B320Builder reserved2(int reserved2) {
			this.reserved2 = reserved2;
			return this;
		}

		public B320Builder referenceAmplitude(float referenceAmplitude) {
			this.referenceAmplitude = referenceAmplitude;
			return this;
		}

		public B320Builder coupling(String coupling) {
			this.coupling = coupling;
			return this;
		}

		public B320Builder rolloff(String rolloff) {
			this.rolloff = rolloff;
			return this;
		}

		public B320Builder noiseType(String noiseType) {
			this.noiseType = noiseType;
			return this;
		}

		public B320 build() {
			B320 b = new B320();
			b.setNextBlocketteByteNumber(nextBlocketteByteNumber);
			b.setBeginningOfCalibrationTime(beginningOfCalibrationTime);
			b.setReserved1(reserved1);
			b.setCalibrationFlags(calibrationFlags);
			b.setCalibrationDuration(calibrationDuration);
			b.setPeakToPeakAmplitudeOfSteps(peakToPeakAmplitudeOfSteps);
			b.setChannelWithCalibrationInput(channelWithCalibrationInput);
			b.setReserved2(reserved2);
			b.setReferenceAmplitude(referenceAmplitude);
			b.setCoupling(coupling);
			b.setRolloff(rolloff);
			b.setNoiseType(noiseType);
			return b;
		}
	}
}
