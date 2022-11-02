package org.earthscope.seed.io;

import org.earthscope.seed.TestFile;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeedInputStreamTest {

    @Test
    public void read()throws Exception{
        try(InputStream inputStream=new FileInputStream(TestFile.getFile())) {
            SeedInputStream seedInputStream = new SeedInputStream(inputStream);
            int recordLength = seedInputStream.getRecordLength();
            assertEquals(512, recordLength);
            int cnt = 0;
            while(true){
                byte[]bytes=seedInputStream.read();
                if(bytes==null){
                    break;
                }
                cnt++;
            }
            assertEquals(1243, cnt);
        }
    }
}
