package org.earthscope.seed.data;

import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.SeedException;

import java.nio.ByteOrder;

@Getter
@Setter
public class B202 extends DataBlockette {

	public B202() {
		super(202, 0, "Log-Z Event Detection Blockette");
	}

	@Override
	public byte[] toSeedBytes(ByteOrder byteOrder)throws SeedException {
		// TODO Auto-generated method stub
		return null;
	}
}
