package org.earthscope.seed.data;

import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.BTime;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.util.SeedByteBuffer;

import java.nio.ByteOrder;

@Getter
@Setter
public class B395 extends DataBlockette {

	private BTime endOfCalibrationTime;
	private int reserved;

	public B395() {
		super(395, 16, "Calibration Abort Blockette");
	}

	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
		validate();
		return SeedByteBuffer.allocate(16).order(byteOrder).putShort(395).putShort(this.getNextBlocketteByteNumber())
				.putTime(endOfCalibrationTime).putShort(reserved).array();
	}

	public void validate() throws SeedException {

	}

	public static B395Builder builder() {
		return new B395Builder();
	}

	public static class B395Builder {
		private int nextBlocketteByteNumber;
		private BTime endOfCalibrationTime;
		private int reserved;

		public B395Builder nextBlocketteByteNumber(int nextBlocketteByteNumber) {
			this.nextBlocketteByteNumber = nextBlocketteByteNumber;
			return this;
		}

		public B395Builder endOfCalibrationTime(BTime endOfCalibrationTime) {
			this.endOfCalibrationTime = endOfCalibrationTime;
			return this;
		}

		public B395Builder reserved(int reserved) {
			this.reserved = reserved;
			return this;
		}

		public B395 build() {
			B395 b = new B395();
			b.setNextBlocketteByteNumber(nextBlocketteByteNumber);
			b.setEndOfCalibrationTime(endOfCalibrationTime);
			b.setReserved(reserved);
			return b;
		}
	}
}
