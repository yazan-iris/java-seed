package org.earthscope.seed.io;

import lombok.extern.slf4j.Slf4j;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.data.*;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

@Slf4j
public class RecordReader implements Closeable {
    private SeedInputStream inputStream;
    private int recordLength;
    private int offset;
    private int sampleFromPreviousRecord;
    public RecordReader(byte[]bytes) throws IOException {
        this(new ByteArrayInputStream(bytes));
    }
    public RecordReader(InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream);
        this.inputStream=new SeedInputStream(inputStream);
        this.recordLength=this.inputStream.getRecordLength();
        //this.recordLength=recordLength;
    }


    public DataRecord read() throws IOException {
        try {
            byte[] bytes = this.inputStream.read();
            if(bytes==null){
                return null;
            }
            SeedDataHeader header = DataBlocketteFactory.createHeader(bytes);
            log.debug("Header:{}",header.toString());
            DataRecord.DataRecordBuilder builder = DataRecord.builder(header);
            int numberOfFollowingBlockettes = header.getNumberOfFollowingBlockettes();
            B1000 b1000 = null;

            if(numberOfFollowingBlockettes>0) {
                log.debug("Try to read {} data blockettes.",numberOfFollowingBlockettes);
                int offset = header.getFirstDataBlockette();
                while(offset>0) {
                    log.debug("Try to read data blockette at offset:{}.",offset);
                    DataBlockette db = DataBlocketteFactory.create(bytes, offset, header.getByteOrder());
                    builder.add(db);
                    offset = db.getNextBlocketteByteNumber();
                    if(db.getType()==1000){
                        b1000 = (B1000) db;
                    }
                }
            }
            if(b1000==null){
                throw new SeedException("Expected b1000 but received none.");
            }
            ByteBuffer bb = ByteBuffer.wrap(bytes, header.getBeginningOfData(),
                    bytes.length-header.getBeginningOfData()).order(ByteOrder.BIG_ENDIAN);
            int i=0;
            while(bb.remaining()>=4){
                //System.out.println("bb: "+i+"  "+bb.getInt());
                bb.getInt();
                i+=4;
            }
            /*int[] samples = Codec.decoder(b1000.getEncodingFormat()).byteOrder(b1000.getByteOrder())
                    .lastSampleFromPreviousRecord(sampleFromPreviousRecord).
                            expectedNumberOfSamples(header.getNumberOfSamples()).
                    decode(bytes, header.getBeginningOfData());
            sampleFromPreviousRecord = samples[samples.length-1];
            return builder.add(samples).build();
            */
            return builder.build();
        } catch (SeedException e) {
            throw new IOException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if(this.inputStream!=null){
            this.inputStream.close();
        }
    }
}
