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
public class B201 extends DataBlockette {

	private float signalAmplitude;
	private float signalPeriod;
	private float backgroundEstimate;
	private B200.WaveType waveType = B200.WaveType.COMPRESSIONAL;
	private int reserved;
	private BTime signalOnsetTime;

	private int[] signalToNoiseRatioValues;
	// 0,1,2
	private int lookbackValue;
	// 0,1
	private int pickAlgorithm;
	private String detectorName;

	public B201() {
		super(201, 60, "Murdock Event Detection Blockette");
	}

	/*-
	 * 	Note Field name Type Length Mask or Flags
		1 Blockette type — 201 B 2
		2 Next blockette’s byte number B 2
		3 Signal amplitude B 4
		4 Signal period B 4
		5 Background estimate B 4
		6 Event detection flags B 1
		7 Reserved byte B 1
		8 Signal onset time B 10
		9 Signal-to-noise ratio values B 6
		10 Lookback value B 1
		11 Pick algorithm B 1
		12 Detector name A 24
	 */
	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		validate();
		BitSet bs = new BitSet(8);

		if (this.waveType == B200.WaveType.DILATATIONAL) {
			bs.set(0);
		}
		byte[] array = bs.toByteArray();
		return SeedByteBuffer.allocate(60).order(byteOrder).putShort(201).putShort(this.getNextBlocketteByteNumber())
				.putFloat(signalAmplitude).putFloat(signalPeriod).putFloat(backgroundEstimate).putByte(array[0])
				.putByte((byte) reserved).putTime(signalOnsetTime)
				.putBytes((signalToNoiseRatioValues == null ? new int[6] : signalToNoiseRatioValues))
				.putByte((byte) lookbackValue).putByte((byte) pickAlgorithm).put(detectorName, StandardCharsets.US_ASCII, 24)
				.array();
	}

	public void validate() throws SeedException {
		SeedException.SeedExceptionBuilder builder = SeedException.builder();
		builder.context("Validating B" + this.getType());
		if (this.signalToNoiseRatioValues != null && signalToNoiseRatioValues.length > 6) {
			builder.add("Invalid SNR array size, expected 6 but was" + signalToNoiseRatioValues.length,
					"Only 6 bytes should be present");
		}
		if (this.lookbackValue < 0 || this.lookbackValue > 2) {
			builder.add("Invalid lookbackValue [" + lookbackValue + "]", "Expected 0,1,2");
		}

		if (this.pickAlgorithm != 0 && this.pickAlgorithm != 1) {
			builder.add("Invalid pickAlgorithm [" + pickAlgorithm + "]", "Expected 0,1");
		}

		if (!builder.isEmpty()) {
			throw builder.build();
		}
	}

	public static B201Builder builder() {
		return new B201Builder();
	}

	public static class B201Builder {
		private int nextBlocketteByteNumber;
		private float signalAmplitude;
		private float signalPeriod;
		private float backgroundEstimate;
		private B200.WaveType waveType;
		private int reserved;
		private BTime signalOnsetTime;

		private int[] signalToNoiseRatioValues;
		// 0,1,2
		private int lookbackValue;
		// 0,1
		private int pickAlgorithm;
		private String detectorName;

		public B201Builder nextBlocketteByteNumber(int nextBlocketteByteNumber) {
			this.nextBlocketteByteNumber = nextBlocketteByteNumber;
			return this;
		}

		public B201Builder signalAmplitude(float signalAmplitude) {
			this.signalAmplitude = signalAmplitude;
			return this;
		}

		public B201Builder signalPeriod(float signalPeriod) {
			this.signalPeriod = signalPeriod;
			return this;
		}

		public B201Builder backgroundEstimate(float backgroundEstimate) {
			this.backgroundEstimate = backgroundEstimate;
			return this;
		}

		public B201Builder reserved(int reserved) {
			this.reserved = reserved;
			return this;
		}

		public B201Builder waveType(B200.WaveType waveType) {
			this.waveType = waveType;
			return this;
		}

		public B201Builder signalOnsetTime(BTime signalOnsetTime) {
			this.signalOnsetTime = signalOnsetTime;
			return this;
		}

		public B201Builder signalToNoiseRatioValues(int[] signalToNoiseRatioValues) {
			this.signalToNoiseRatioValues = signalToNoiseRatioValues;
			return this;
		}

		public B201Builder pickAlgorithm(int pickAlgorithm) {
			this.pickAlgorithm = pickAlgorithm;
			return this;
		}

		public B201Builder lookbackValue(int lookbackValue) {
			this.lookbackValue = lookbackValue;
			return this;
		}

		public B201Builder detectorName(String detectorName) {
			this.detectorName = detectorName;
			return this;
		}

		public B201 build() throws SeedException {
			B201 b = new B201();
			b.setNextBlocketteByteNumber(nextBlocketteByteNumber);
			b.setSignalAmplitude(signalAmplitude);
			b.setSignalPeriod(signalPeriod);
			b.setBackgroundEstimate(backgroundEstimate);
			b.setWaveType(waveType);
			b.setSignalOnsetTime(signalOnsetTime);
			b.setSignalToNoiseRatioValues(signalToNoiseRatioValues);
			b.setLookbackValue(lookbackValue);
			b.setPickAlgorithm(pickAlgorithm);
			b.setDetectorName(detectorName);
			b.validate();
			return b;
		}
	}
}
