package org.earthscope.seed;


import org.earthscope.seed.data.DataBlockette;
import org.earthscope.seed.data.SeedDataHeader;

public interface SeedRecord {

    public SeedDataHeader getHeader();
    public DataBlockette add(DataBlockette blockette)throws SeedException ;
}
