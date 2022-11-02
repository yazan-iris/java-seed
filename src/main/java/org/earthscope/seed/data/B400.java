package org.earthscope.seed.data;

import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.util.SeedByteBuffer;

import java.nio.ByteOrder;

/**
 * This blockette is used to specify how the beam indicated by the corresponding
 * Beam Configuration Blockette [35] was formed for this data record. For beams
 * formed by non-plane waves, the Beam Delay Blockette [405] should be used to
 * determine the beam delay for each component referred to in the Beam
 * Configuration Blockette [35].
 * 
 * @author Suleiman
 *
 */
/*-
 * 	Note Field name Type Length Mask or Flags
	1 Blockette type — 400 B 2
	2 Next blockette’s byte number B 2
	3 Beam azimuth (degrees) B 4
	4 Beam slowness (sec/degree) B 4
	5 Beam configuration B 2
	6 Reserved bytes B 2
 */
@Getter
@Setter
public class B400 extends DataBlockette {

	private float beamAzimuthInDegrees;
	private float beamSlownessInSecDegree;
	private int beamConfiguration;
	private int reserved;

	public B400() {
		super(400, 16, "Beam Blockette");
	}

	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		return SeedByteBuffer.allocate(16).order(byteOrder).putShort(400).putShort(this.getNextBlocketteByteNumber())
				.putFloat(beamAzimuthInDegrees).putFloat(beamSlownessInSecDegree).putShort(beamConfiguration)
				.putShort(reserved).array();
	}

	public static B400Builder builder() {
		return new B400Builder();
	}

	public static class B400Builder {
		private int nextBlocketteByteNumber;
		private float beamAzimuthInDegrees;
		private float beamSlownessInSecDegree;
		private int beamConfiguration;
		private int reserved;

		public B400Builder nextBlocketteByteNumber(int nextBlocketteByteNumber) {
			this.nextBlocketteByteNumber = nextBlocketteByteNumber;
			return this;
		}

		public B400Builder beamSlownessInSecDegree(float beamSlownessInSecDegree) {
			this.beamSlownessInSecDegree = beamSlownessInSecDegree;
			return this;
		}
		public B400Builder beamAzimuthInDegrees(float beamAzimuthInDegrees) {
			this.beamAzimuthInDegrees = beamAzimuthInDegrees;
			return this;
		}
		public B400Builder beamConfiguration(int beamConfiguration) {
			this.beamConfiguration = beamConfiguration;
			return this;
		}

		public B400Builder reserved(int reserved) {
			this.reserved = reserved;
			return this;
		}

		public B400 build() {
			B400 b = new B400();
			b.setNextBlocketteByteNumber(nextBlocketteByteNumber);
			b.setBeamAzimuthInDegrees(beamAzimuthInDegrees);
			b.setBeamConfiguration(beamConfiguration);
			b.setBeamSlownessInSecDegree(beamSlownessInSecDegree);
			b.setReserved(reserved);
			return b;
		}
	}
}
