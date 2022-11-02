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
public class B300 extends DataBlockette {
	private BTime beginningOfCalibrationTime;
	private int numberOfStepCalibrationsInSequence;
	/*-
	 * Calibration flags: 
	 * [Bit 0] — If set: first pulse is positive 
	 * [Bit 1] — If set: calibration’s alternate sign 
	 * [Bit 2] — If set: calibration was automatic; if unset: manual 
	 * [Bit 3] — If set: calibration continued from previous record(s) 
	 * [Other bits reserved and must be zero.]
	 */
	private BitSet calibrationFlags;
	private long stepDuration;
	private long intervalDuration;
	private float calibrationSignalAmplitude;
	private String channelWithCalibrationInput;
	private int reserved;
	private float referenceAmplitude;
	private String coupling;
	private String rolloff;

	public B300() {
		super(300, 60, "Step Calibration Blockette");
	}
	/*-
	 * 	1 Blockette type — 300 B 2
		2 Next blockette’s byte number B 2
		3 Beginning of calibration time B 10
		4 Number of step calibrations B 1
		5 Calibration flags B 1
		6 Step duration B 4
		7 Interval duration B 4
		8 Calibration signal amplitude B 4
		9 Channel with calibration input A 3
		10 Reserved byte B 1
		11 Reference amplitude B 4
		12 Coupling A 12
		13 Rolloff A 12
	 */
	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		validate();
		return SeedByteBuffer.allocate(60).order(byteOrder).putShort(300).putShort(this.getNextBlocketteByteNumber())
				.putTime(beginningOfCalibrationTime).putByte((byte) numberOfStepCalibrationsInSequence)
				.putByte(calibrationFlags == null ? 0
						: (calibrationFlags.toByteArray().length == 0 ? 0 : calibrationFlags.toByteArray()[0]))
				.putInt(stepDuration).putInt(intervalDuration).putFloat(calibrationSignalAmplitude)
				.put(channelWithCalibrationInput, StandardCharsets.US_ASCII, 3).putByte((byte) reserved)
				.putFloat(referenceAmplitude).put(coupling, StandardCharsets.US_ASCII, 12)
				.put(rolloff, StandardCharsets.US_ASCII, 12).array();
	}

	public void validate() throws SeedException {

	}

	public static B300Builder builder() {
		return new B300Builder();
	}

	public static class B300Builder {
		private int nextBlocketteByteNumber;
		private BTime beginningOfCalibrationTime;
		private int numberOfStepCalibrationsInSequence;

		private BitSet calibrationFlags;
		private long stepDuration;
		private long intervalDuration;
		private float calibrationSignalAmplitude;
		private String channelWithCalibrationInput;
		private int reserved;
		private float referenceAmplitude;
		private String coupling;
		private String rolloff;

		public B300Builder calibrationFlags(BitSet calibrationFlags) {
			this.calibrationFlags = calibrationFlags;
			return this;
		}

		public B300Builder nextBlocketteByteNumber(int nextBlocketteByteNumber) {
			this.nextBlocketteByteNumber = nextBlocketteByteNumber;
			return this;
		}

		public B300Builder beginningOfCalibrationTime(BTime beginningOfCalibrationTime) {
			this.beginningOfCalibrationTime = beginningOfCalibrationTime;
			return this;
		}

		public B300Builder numberOfStepCalibrationsInSequence(int numberOfStepCalibrationsInSequence) {
			this.numberOfStepCalibrationsInSequence = numberOfStepCalibrationsInSequence;
			return this;
		}

		public B300Builder stepDuration(long stepDuration) {
			this.stepDuration = stepDuration;
			return this;
		}

		public B300Builder intervalDuration(long intervalDuration) {
			this.intervalDuration = intervalDuration;
			return this;
		}

		public B300Builder calibrationSignalAmplitude(float calibrationSignalAmplitude) {
			this.calibrationSignalAmplitude = calibrationSignalAmplitude;
			return this;
		}

		public B300Builder channelWithCalibrationInput(String channelWithCalibrationInput) {
			this.channelWithCalibrationInput = channelWithCalibrationInput;
			return this;
		}

		public B300Builder reserved(int reserved) {
			this.reserved = reserved;
			return this;
		}

		public B300Builder referenceAmplitude(float referenceAmplitude) {
			this.referenceAmplitude = referenceAmplitude;
			return this;
		}

		public B300Builder coupling(String coupling) {
			this.coupling = coupling;
			return this;
		}

		public B300Builder rolloff(String rolloff) {
			this.rolloff = rolloff;
			return this;
		}

		public B300 build() throws SeedException {
			B300 b = new B300();
			b.setNextBlocketteByteNumber(nextBlocketteByteNumber);
			b.setBeginningOfCalibrationTime(beginningOfCalibrationTime);
			b.setNumberOfStepCalibrationsInSequence(numberOfStepCalibrationsInSequence);
			b.setCalibrationFlags(calibrationFlags);
			b.setStepDuration(stepDuration);
			b.setIntervalDuration(intervalDuration);
			b.setCalibrationSignalAmplitude(calibrationSignalAmplitude);
			b.setChannelWithCalibrationInput(channelWithCalibrationInput);
			b.setReserved(reserved);
			b.setReferenceAmplitude(referenceAmplitude);
			b.setCoupling(coupling);
			b.setRolloff(rolloff);
			b.validate();
			return b;
		}
	}
}
