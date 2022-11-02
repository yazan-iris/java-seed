package org.earthscope.seed.data;

import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.util.SeedByteBuffer;

import java.nio.ByteOrder;

@Getter
@Setter
public class B1001 extends DataBlockette {

	private int timingQuality;
	private int microSeconds;
	private int reserved;
	private int frameCount;

	public B1001() {
		super(1001, 8, "Data Extension Blockette");
	}

	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		return SeedByteBuffer.allocate(8).order(byteOrder).putShort(1001).putShort(this.getNextBlocketteByteNumber())
				.putByte((byte) timingQuality).putByte((byte) microSeconds).putByte((byte) reserved).
						putByte((byte) frameCount).array();
	}

	public static B1001Builder builder() {
		return new B1001Builder();
	}

	public static class B1001Builder {
		private int nextBlocketteByteNumber;
		private int timingQuality;
		private int microSeconds;
		private int reserved;
		private int frameCount;

		public B1001Builder nextBlocketteByteNumber(int nextBlocketteByteNumber) {
			this.nextBlocketteByteNumber = nextBlocketteByteNumber;
			return this;
		}

		public B1001Builder timingQuality(int timingQuality) {
			this.timingQuality = timingQuality;
			return this;
		}

		public B1001Builder microSeconds(int microSeconds) {
			this.microSeconds = microSeconds;
			return this;
		}

		public B1001Builder reserved(int reserved) {
			this.reserved = reserved;
			return this;
		}

		public B1001Builder frameCount(int frameCount) {
			this.frameCount = frameCount;
			return this;
		}

		public B1001 build() {
			B1001 b = new B1001();
			b.setFrameCount(frameCount);
			b.setMicroSeconds(microSeconds);
			b.setNextBlocketteByteNumber(nextBlocketteByteNumber);
			b.setReserved(reserved);
			b.setTimingQuality(timingQuality);
			return b;
		}
	}
}
