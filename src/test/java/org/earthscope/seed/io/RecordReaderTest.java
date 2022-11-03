package org.earthscope.seed.io;

import org.earthscope.seed.Blockette;
import org.earthscope.seed.TestFile;
import org.earthscope.seed.data.B1000;
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
            while(true) {
                DataRecord dr = rr.read();
                if(dr == null){
                    break;
                }
                Blockette blockette = dr.get(1000);
                B1000 b1000 = dr.getB1000();
                SeedDataHeader header = dr.getHeader();
                dr.computeEndTime();
                assertEquals("BHZ", header.getChannel());
            }
        }
    }
}
