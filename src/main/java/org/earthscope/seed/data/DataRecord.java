package org.earthscope.seed.data;

import org.earthscope.seed.BTime;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.SeedRecord;
import org.earthscope.seed.SeedRecordType;
import org.earthscope.seed.codec.EncodingFormat;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class DataRecord implements SeedRecord {

    private Map<Integer, DataBlockette> map = new TreeMap<>();
    private SeedDataHeader dataHeader;
    private int[] samples;

    public DataRecord(SeedDataHeader dataHeader) {
        this.dataHeader = dataHeader;
    }

    @Override
    public DataBlockette add(DataBlockette dataBlockette) throws SeedException {
        if (dataBlockette == null) {
            return null;
        }
        int type = dataBlockette.getType();
        map.putIfAbsent(type, dataBlockette);

        SeedDataHeader header = (SeedDataHeader) this.getHeader();
        header.setNumberOfFollowingBlockettes(map.size());

        return dataBlockette;
    }



    public int[] getSamples() {
        return samples;
    }

    public void addAll(int[] samples){
        Objects.requireNonNull(samples);
        if(samples.length==0){
            return;
        }
        if(this.samples==null){
            this.samples=samples;
        }else{
            int newLength = this.samples.length+samples.length;
            int[]newSampleArray = new int[newLength];
            System.arraycopy(this.samples, 0, newSampleArray, 0, this.samples.length);
            System.arraycopy(samples, 0, newSampleArray, this.samples.length, samples.length);
            this.samples=newSampleArray;
        }
    }
    public void setSamples(int[] samples) {
        this.samples = samples;
    }

    public DataBlockette get(int type){
        return map.get(type);
    }

    public List<DataBlockette>getAll(){
        return new ArrayList<>(map.values());
    }
    public B100 getB100() {
        DataBlockette blockette = map.get(100);
        if (blockette == null) {
            return null;
        }
        return (B100) blockette;
    }

    public B1000 getB1000() {
        DataBlockette blockette = map.get(1000);
        if (blockette == null) {
            return null;
        }
        return (B1000) blockette;
    }

    public B1001 getB1001() {
        DataBlockette blockette = map.get(1001);
        if (blockette == null) {
            return null;
        }
        return (B1001) blockette;
    }

    public int getNumberOfSamples() {
        if(dataHeader==null){
            return 0;
        }
        return dataHeader.getNumberOfSamples();
    }

    public double getSampleRate() {
        if (dataHeader == null) {
            return 0;
        }
        return dataHeader.getSampleRateFactor();
    }

    public Instant getStartTime() {
        if (dataHeader == null) {
            return null;
        }
        BTime btime = dataHeader.getStart();
        if (btime == null) {
            return null;
        }
        return btime.toInstant();
    }

    @Override
    public SeedDataHeader getHeader() {
        return this.dataHeader;
    }

    public int getSequence() {
        if (dataHeader == null) {
            return 0;
        }
        return dataHeader.getSequence();
    }

    public SeedRecordType getType() {
        if (dataHeader == null) {
            return null;
        }
        return dataHeader.getRecordType();
    }

    public Instant getCorrectedStartTime() {
        Instant instant = getStartTime();
        if (instant == null) {
            return null;
        }
        SeedDataHeader dataHeader = this.getHeader();
        if (dataHeader == null) {
            return null;
        }
        B1001 b1001 = this.getB1001();
        if (b1001 != null) {
            instant = instant.plus(b1001.getMicroSeconds(), ChronoUnit.MICROS);
        }
        if (!dataHeader.getActivityFlags().isTimeCorrectionApplied()) {
            instant = instant.plusMillis(dataHeader.getTimeCorrection() * 10L);
        }
        return instant;
    }

    public Instant computeEndTime() {
        Instant startTime = getCorrectedStartTime();
        if (startTime == null) {
            return null;
        }
        SeedDataHeader dataHeader = this.getHeader();
        if (dataHeader == null) {
            return null;
        }

        float sampleRate = dataHeader.getSampleRateFactor();
        DataBlockette blockette = map.get(100);
        if (blockette != null) {
            B100 b100 = (B100) blockette;
            sampleRate = b100.getActualSampleRate();
        }
        Duration duration = Duration.ofNanos(Math
                .round(((dataHeader.getNumberOfSamples() - 1) / (double) sampleRate) * TimeUnit.SECONDS.toNanos(1)));
        return startTime.plus(duration);
    }

    public Instant computeExpectedNextSampleTime() {
        Instant instant = getCorrectedStartTime();
        if (instant == null) {
            return null;
        }
        // BTime expectedTime = startTime.toBuilder().build();
        SeedDataHeader dataHeader = this.getHeader();
        if (dataHeader == null) {
            return null;
        }
        float sampleRate = dataHeader.getSampleRateFactor();

        DataBlockette blockette = map.get(100);
        if (blockette != null) {
            B100 b100 = (B100) blockette;
            sampleRate = b100.getActualSampleRate();
        }

        Duration duration = Duration.ofSeconds(Math.round(dataHeader.getNumberOfSamples() / (double) sampleRate));
        return instant.plus(duration);
    }

    public QualityFlags getQualityIndicator() {
        SeedDataHeader header = this.getHeader();
        if (header == null) {
            return null;
        }
        return header.getQualityIndicator();
    }

    public EncodingFormat getEncodingFormat() {
        B1000 b1000 = this.getB1000();
        if (b1000 == null) {
            return null;
        }
        return b1000.getEncodingFormat();
    }

    public static DataRecordBuilder builder(SeedDataHeader dataHeader) {
        return new DataRecordBuilder(dataHeader);
    }

    public static class DataRecordBuilder {
        private SeedDataHeader dataHeader;
        private int[]samples;
        private List<DataBlockette>blockettes = new ArrayList<>();

        private DataRecordBuilder(SeedDataHeader dataHeader) {
            this.dataHeader = dataHeader;
        }

        public DataRecordBuilder add(int[]samples){
            this.samples=samples;
            return this;
        }
        public DataRecordBuilder add(DataBlockette blockette){
            Objects.requireNonNull(blockette);
            this.blockettes.add(blockette);
            return this;
        }

        public DataRecord build() throws SeedException {
            if (this.dataHeader == null) {
                throw new SeedException("header cannot be null.");
            }
            DataRecord dr = new DataRecord(this.dataHeader);
            dr.setSamples(this.samples);
            for(DataBlockette db:blockettes) {
                dr.add(db);
            }
            return dr;
        }

    }
}