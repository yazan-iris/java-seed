package org.earthscope.seed.data;


import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.Blockette;
import org.earthscope.seed.SeedException;

import java.nio.ByteOrder;

@Getter
@Setter
public abstract class DataBlockette implements Blockette {

    private final int type;
    private final int size;
    private String description;
    private int nextBlocketteByteNumber;

    public DataBlockette(int type, int size, String description) {
        this.type = type;
        this.size=size;
        this.description = description;
    }

    @Override
    public int getType(){
        return this.type;
    }

    @Override
    public int getSize(){
        return this.size;
    }

    @Override
    public String getDescription(){
        return this.description;
    }

    public int getNextBlocketteByteNumber(){
        return nextBlocketteByteNumber;
    }

    public byte[] toSeedBytes() throws SeedException {
        return toSeedBytes(ByteOrder.BIG_ENDIAN);
    }
    public static class DataBlocketteBuilder {
        private int nextBlocketteByteNumber;

        void nextBlocketteByteNumber(int nextBlocketteByteNumber) {
            this.nextBlocketteByteNumber = nextBlocketteByteNumber;
        }
    }
}
