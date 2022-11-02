package org.earthscope.seed.data;

import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.util.SeedByteBuffer;

import java.nio.ByteOrder;
import java.util.Arrays;

@Getter
@Setter
public class B100 extends DataBlockette {

	private float actualSampleRate;
	private int flags;
	private byte[] reserved;

	public B100() {
		super(100, 12, "Sample Rate Blockette");
	}

	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		return SeedByteBuffer.allocate(12, byteOrder).putShort(100).putShort(this.getNextBlocketteByteNumber())
				.putFloat(actualSampleRate).putByte((byte) flags).putBytesOrSkipIfNull(reserved, 3).array();
	}

	@Override
	public String toString() {
		return "B100{" +
				"actualSampleRate=" + actualSampleRate +
				", flags=" + flags +
				", reserved=" + Arrays.toString(reserved) +
				" nextBlocketteByteNumber="+getNextBlocketteByteNumber() +
				'}';
	}

	public static B100Builder builder() {
		return new B100Builder();
	}

	public static class B100Builder {
		private float actualSampleRate;
		private int flags;
		private byte[] reserved;

		public B100Builder actualSampleRate(float actualSampleRate) {
			this.actualSampleRate = actualSampleRate;
			return this;
		}

		public B100 build() {
			B100 b = new B100();
			b.setActualSampleRate(actualSampleRate);
			return b;
		}
	}
}
