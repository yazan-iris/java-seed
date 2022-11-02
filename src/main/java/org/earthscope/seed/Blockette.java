package org.earthscope.seed;

import java.nio.ByteOrder;

public interface Blockette {
    public int getType();
    public String getDescription();
    public byte[]toSeedBytes(ByteOrder byteOrder)throws SeedException;
    public int getSize();
}
