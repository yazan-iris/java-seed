package org.earthscope.seed.data;

import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.util.SeedByteBuffer;

import java.nio.ByteOrder;

@Getter
@Setter
public class B405 extends DataBlockette {

	private int arrayOfDelayValues;

	public B405() {
		super(405, 6, "Beam Delay Blockette");
	}

	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		return SeedByteBuffer.allocate(6).order(byteOrder).putShort(405).putShort(this.getNextBlocketteByteNumber())
				.putShort(arrayOfDelayValues).array();
	}

	public void validate() throws SeedException {
	}
	
	public static B405Builder builder() {
		return new B405Builder();
	}

	public static class B405Builder {
		private int nextBlocketteByteNumber;
		private int arrayOfDelayValues;

		public B405Builder nextBlocketteByteNumber(int nextBlocketteByteNumber) {
			this.nextBlocketteByteNumber = nextBlocketteByteNumber;
			return this;
		}


		public B405Builder arrayOfDelayValues(int arrayOfDelayValues) {
			this.arrayOfDelayValues = arrayOfDelayValues;
			return this;
		}

		public B405 build() {
			B405 b = new B405();
			b.setNextBlocketteByteNumber(nextBlocketteByteNumber);
			b.setArrayOfDelayValues(arrayOfDelayValues);
			return b;
		}
	}
}
