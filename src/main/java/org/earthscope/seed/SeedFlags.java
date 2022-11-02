package org.earthscope.seed;

public class SeedFlags {
	private int b;

	public SeedFlags() {
		this(0);
	}

	public SeedFlags(int... bits) {
		if (bits == null) {
			throw new IllegalArgumentException("bits array cannot be null");
		}
		if (bits.length > 8) {
			throw new IllegalArgumentException("Expected at most 8 elements but received " + bits.length);
		}
		for (int i = 0; i < bits.length; i++) {
			this.set(i, (bits[i] == 1));
		}
	}

	public SeedFlags(boolean... booleans) {
		if (booleans == null) {
			throw new IllegalArgumentException("Array cannot be null");
		}
		if (booleans.length > 8) {
			throw new IllegalArgumentException("Expected at most 8 elements but received " + booleans.length);
		}
		this.b = 0;
		for (int i = 0; i < booleans.length; i++) {
			this.set(i, (booleans[i]));
		}
	}

	public SeedFlags(int b) {
		this.b = b;
	}

	public void set(int index, boolean value) {
		if (value) {
			setTrue(index);
		} else {
			setFalse(index);
		}
	}

	public void setTrue(int index) {
		if (index < 0 || index > 7) {
			throw new IndexOutOfBoundsException("" + index);
		}
		this.b |= 1 << index;
	}

	public void setFalse(int index) {
		if (index < 0 || index > 7) {
			throw new IndexOutOfBoundsException("" + index);
		}
		this.b &= ~(1 << index);
	}

	public short toByte() {
		return ((short) (b & 0xff));
	}


	public void trunOn(int index) {
		setTrue(index);
	}

	public boolean isOn(int index) {
		if (index < 0 || index > 7) {
			throw new IndexOutOfBoundsException("" + index);
		}
		return (b & (0x01 << index)) != 0;
	}

	public void trunOff(int index) {
		setFalse(index);
	}

	public boolean isOff(int index) {
		if (index < 0 || index > 7) {
			throw new IndexOutOfBoundsException("" + index);
		}
		return (b & (0x01 << index)) == 0;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			builder.append(this.isOn(i)?"1":"0");
		}
		return builder.toString();
	}
}
