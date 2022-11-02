package org.earthscope.seed.data;

import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.BTime;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.util.SeedByteBuffer;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Objects;

@Getter
@Setter
public class B200 extends DataBlockette {

	/*-
	 *  UBYTE: Event detection flags:
		[Bit 0] — If set: dilatation wave; if unset: compression
		[Bit 1] — If set: units above are after deconvolution
		(see Channel Identifier Blockette [52], field 8); if unset: digital counts
		[Bit 2] — When set, bit 0 is undetermined
		[Other bits reserved and must be zero.]
	 */
	public enum WaveType {
		DILATATIONAL(1), COMPRESSIONAL(0), UNDETERNIBED(-1);// dilatation wave; if unset: compression

		private int value;

		private WaveType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static WaveType from(int b) {
			return from((byte) b);
		}

		public static WaveType from(byte b) {
			return from(BitSet.valueOf(new byte[] { b }));
		}

		public static WaveType from(BitSet bs) {
			Objects.requireNonNull(bs, "BitSet cannot be null");
			if (bs.get(2)) {
				return UNDETERNIBED;
			} else if (bs.get(0)) {
				return DILATATIONAL;
			} else {
				return COMPRESSIONAL;
			}
		}
	}

	private float signalAmplitude;
	private float signalPeriod;
	private float backgroundEstimate;

	private WaveType waveType = WaveType.COMPRESSIONAL;
	private boolean isAfterDeconvolution;
	// private int eventDetectionFlags;
	private int reserved;
	private BTime signalOnsetTime;
	private String detectorName;

	public B200() {
		super(200, 52, "Generic Event Detection Blockette");
	}

	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		BitSet bs = new BitSet(8);
		if (this.waveType == WaveType.UNDETERNIBED) {
			bs.set(2);
		} else if (this.waveType == WaveType.DILATATIONAL) {
			bs.set(0);
		}
		if (this.isAfterDeconvolution) {
			bs.set(1);
		}
		byte[] array = bs.toByteArray();
		return SeedByteBuffer.allocate(52).order(byteOrder).putShort(200).putShort(this.getNextBlocketteByteNumber())
				.putFloat(signalAmplitude).putFloat(signalPeriod).putFloat(backgroundEstimate).putByte(array[0])
				.putByte((byte) reserved).putTime(signalOnsetTime).put(detectorName, StandardCharsets.US_ASCII, 24)
				.array();
	}

	public static B200Builder builder() {
		return new B200Builder();
	}

	@Override
	public String toString() {
		return "B200 [signalAmplitude=" + signalAmplitude + ", signalPeriod=" + signalPeriod + ", backgroundEstimate="
				+ backgroundEstimate + ", waveType=" + waveType + ", isAfterDeconvolution=" + isAfterDeconvolution
				+ ", reserved=" + reserved + ", signalOnsetTime=" + signalOnsetTime + ", detectorName=" + detectorName
				+ ", getNextBlocketteByteNumber()=" + getNextBlocketteByteNumber() + "]";
	}

	public static class B200Builder {
		private float signalAmplitude;
		private float signalPeriod;
		private float backgroundEstimate;
		private WaveType waveType;
		private boolean afterDeconvolution;
		private int reserved;
		private BTime signalOnsetTime;
		private String detectorName;

		public B200Builder signalAmplitude(float signalAmplitude) {
			this.signalAmplitude = signalAmplitude;
			return this;
		}

		public B200Builder signalPeriod(float signalPeriod) {
			this.signalPeriod = signalPeriod;
			return this;
		}

		public B200Builder backgroundEstimate(float backgroundEstimate) {
			this.backgroundEstimate = backgroundEstimate;
			return this;
		}

		public B200Builder reserved(int reserved) {
			this.reserved = reserved;
			return this;
		}

		public B200Builder waveType(WaveType waveType) {
			this.waveType = waveType;
			return this;
		}

		public B200Builder afterDeconvolution(boolean afterDeconvolution) {
			this.afterDeconvolution = afterDeconvolution;
			return this;
		}

		public B200Builder signalOnsetTime(BTime signalOnsetTime) {
			this.signalOnsetTime = signalOnsetTime;
			return this;
		}

		public B200Builder detectorName(String detectorName) {
			this.detectorName = detectorName;
			return this;
		}

		public B200 build() {
			B200 b = new B200();
			b.setSignalAmplitude(signalAmplitude);
			b.setSignalPeriod(signalPeriod);
			b.setBackgroundEstimate(backgroundEstimate);
			b.setWaveType(waveType);
			b.setAfterDeconvolution(afterDeconvolution);
			b.setSignalOnsetTime(signalOnsetTime);
			b.setDetectorName(detectorName);
			return b;
		}
	}
}
