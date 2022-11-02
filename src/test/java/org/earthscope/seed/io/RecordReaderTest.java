package org.earthscope.seed.io;

import org.earthscope.seed.TestFile;
import org.earthscope.seed.data.DataRecord;
import org.earthscope.seed.data.SeedDataHeader;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecordReaderTest {

    @Test
    public void read()throws Exception{
        try(InputStream inputStream=new FileInputStream(TestFile.getFile()); RecordReader rr = new RecordReader(inputStream)){
            DataRecord dr = rr.read();
            SeedDataHeader header = dr.getHeader();
            assertEquals("BHZ", header.getChannel());
        }
    }
}
