package org.earthscope.seed.io;

import org.earthscope.seed.TestFile;
import org.earthscope.seed.data.DataBlocketteFactory;
import org.earthscope.seed.data.SeedDataHeader;
import org.earthscope.seed.util.DateTimeUtil;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.Instant;

public class SeedDataHeaderOnlyTest {

    @Test
    public void readHeader()throws Exception{
        try(InputStream inputStream=new FileInputStream(TestFile.getFile());
            SeedInputStream seedInputStream = new SeedInputStream(inputStream);) {
            while(true){
                byte[]recordBytes = seedInputStream.read();
                if(recordBytes==null){
                    break;
                }
                SeedDataHeader header = DataBlocketteFactory.createHeader(recordBytes);
                System.out.println(header.getNetwork()+"/"+header.getStation()+"/"+header.getLocation()+"/"+header.getChannel()+":"+
                        header.getStart().format());
                Instant endTime = DateTimeUtil.computeEndTime(header, null, null);
            }
        }

    }
}
