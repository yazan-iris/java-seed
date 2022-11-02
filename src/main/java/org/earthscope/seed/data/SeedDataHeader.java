package org.earthscope.seed.data;


import lombok.Getter;
import lombok.Setter;
import org.earthscope.seed.BTime;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.SeedHeader;
import org.earthscope.seed.SeedRecordType;
import org.earthscope.seed.util.SeedByteBuffer;

import java.nio.ByteOrder;
import java.util.Objects;

@Getter
@Setter
public class SeedDataHeader implements SeedHeader {

    private String description = "Fixed Section of Data Header";

    private int sequence;
    private final SeedRecordType recordType;
    private final boolean continuation = false;
    private char reserved;
    private String network;
    private String station;
    private String location;
    private String channel;
    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    private BTime start;
    private int numberOfSamples;
    private int sampleRateMultiplier;
    private ActivityFlags activityFlags;
    private int ioClockFlag;
    private QualityFlags qualityIndicator;
    private int numberOfFollowingBlockettes;
    private int sampleRateFactor;
    /**
     * This field contains a value that may modify the field 8 record start time.
     * Depending on the setting of bit 1 in field 12, the record start time may have
     * already been adjusted. The units are in 0.0001 seconds.
     */
    private int timeCorrection;
    private int beginningOfData;
    private int firstDataBlockette;

    public SeedDataHeader(int sequence, SeedRecordType recordType, char reserved) {
        this.sequence = sequence;
        this.recordType = recordType;
        this.reserved = reserved;
    }

    @Override
    public String toString() {
        return "DataHeader [description=" + description + ", sequence=" + sequence + ", recordType=" + recordType
                + ", continuation=" + continuation + ", reserved=" + reserved + ", network=" + network + ", station="
                + station + ", location=" + location + ", channel=" + channel + ", start="
                + start + ", numberOfSamples=" + numberOfSamples + ", sampleRateMultiplier=" + sampleRateMultiplier
                + ", activityFlag=" + activityFlags + ", ioClockFlag=" + ioClockFlag + ", dataQualityFlag="
                + qualityIndicator + ", numberOfFollowingBlockettes=" + numberOfFollowingBlockettes
                + ", sampleRateFactor=" + sampleRateFactor + ", timeCorrection=" + timeCorrection + ", beginingOfData="
                + beginningOfData + ", firstDataBlockette=" + firstDataBlockette + "]";
    }

    public byte[] toSeedBytes(ByteOrder byteOrder) throws SeedException {
        SeedByteBuffer dataByteArray = SeedByteBuffer.allocate(48).order(byteOrder).putSequence(this.sequence)
                .putOneByteChar(this.recordType.valueAsChar()).putOneByteChar(this.reserved).putStationCode(station)
                .putLocationCode(location).putChannelCode(channel).putNetworkCode(network).putTime(start)
                .putShort(this.numberOfSamples).putShort(this.sampleRateFactor)
                .putShort(this.sampleRateMultiplier).putByte((byte) this.activityFlags.toByte())
                .putByte((byte) this.ioClockFlag).putByte((byte) this.qualityIndicator.toByte())
                .putByte((byte) this.numberOfFollowingBlockettes).putFloat(this.timeCorrection)
                .putShort(this.beginningOfData).putShort(this.firstDataBlockette);

        return dataByteArray.array();
    }



    public static SeedDataHeader from(byte[]bytes) throws SeedException {
        return from(bytes, 0);
    }
    public static SeedDataHeader from(byte[]bytes, int offset) throws SeedException {
        return from(bytes, offset, 48);
    }
    public static SeedDataHeader from(byte[]bytes, int offset, int length) throws SeedException {
        Objects.requireNonNull(bytes);
        if(offset<0||offset>bytes.length){

        }
        if(length!=48){

        }

        if(bytes.length-offset<48){

        }
        ByteOrder byteOrder = BTime.determineByteOrder(bytes, offset+20);
        SeedByteBuffer dba = SeedByteBuffer.wrap(bytes, offset, length, byteOrder);
        return SeedDataHeader.builder(dba.getSequence()
                ,SeedRecordType.from((char)dba.getByte()), (char)dba.getByte()).station(dba.getStationCode()).
                location(dba.getLocationCode()).
                channel(dba.getChannelCode()).
                network(dba.getNetworkCode()).start(dba.getTime()).numberOfSamples(dba.getShort()).sampleRateFactor(dba.getShort()).
                sampleRateMultiplier(dba.getShort()).activityFlags(ActivityFlags.valueOf(dba.getByte())).
                ioClockFlag(dba.getByte()).dataQualityFlag(dba.getByte()).numberOfFollowingBlockettes(dba.getByte()).
                timeCorrection(dba.getInt()).beginingOfData(dba.getShort()).
                firstDataBlockette(dba.getShort()).build();
    }
    public static SeedDataHeaderBuilder builder(SeedRecordType type) {
        SeedDataHeaderBuilder dhb = new SeedDataHeaderBuilder();
        dhb.type = type;
        return dhb;
    }

    public static SeedDataHeaderBuilder builder(int sequence, SeedRecordType type, char reserved) {
        return new SeedDataHeaderBuilder(sequence, type, reserved);
    }

    public static class SeedDataHeaderBuilder {

        private int sequence;
        private SeedRecordType type;
        private char reserved = ' ';

        private String network;
        private String station;
        private String location;
        private String channel;
        private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        private BTime start;
        private int numberOfSamples;
        private int sampleRateMultiplier;
        private ActivityFlags activityFlags;
        private int ioClockFlag;
        private QualityFlags qualityIndicator;
        private int numberOfFollowingBlockettes;
        private int sampleRateFactor;
        private int timeCorrection;
        private int beginingOfData;
        private int firstDataBlockette;

        private SeedDataHeaderBuilder() {
        }

        private SeedDataHeaderBuilder(SeedRecordType type) {
            this.type = type;
        }

        private SeedDataHeaderBuilder(int sequence, SeedRecordType type, char reserved) {
            this.sequence = sequence;
            this.type = type;
            this.reserved = reserved;
        }

        public SeedDataHeaderBuilder network(String network) {
            this.network = network;
            return this;
        }

        public SeedDataHeaderBuilder station(String station) {
            this.station = station;
            return this;
        }

        public SeedDataHeaderBuilder location(String location) {
            this.location = location;
            return this;
        }

        public SeedDataHeaderBuilder channel(String channel) {
            this.channel = channel;
            return this;
        }

        public SeedDataHeaderBuilder start(BTime start) {
            this.start = start;
            return this;
        }

        public SeedDataHeaderBuilder numberOfSamples(int numberOfSamples) {
            this.numberOfSamples = numberOfSamples;
            return this;
        }

        public SeedDataHeaderBuilder sampleRateMultiplier(int sampleRateMultiplier) {
            this.sampleRateMultiplier = sampleRateMultiplier;
            return this;
        }

        public SeedDataHeaderBuilder activityFlags(ActivityFlags activityFlags) {
            this.activityFlags = activityFlags;
            return this;
        }

        public SeedDataHeaderBuilder ioClockFlag(int ioClockFlag) {
            this.ioClockFlag = ioClockFlag;
            return this;
        }

        public SeedDataHeaderBuilder dataQualityFlag(int dataQualityFlag) {
            return dataQualityFlag(QualityFlags.valueOf(dataQualityFlag));
        }

        public SeedDataHeaderBuilder dataQualityFlag(QualityFlags qualityIndicator) {
            this.qualityIndicator = qualityIndicator;
            return this;
        }

        public SeedDataHeaderBuilder numberOfFollowingBlockettes(int numberOfFollowingBlockettes) {
            this.numberOfFollowingBlockettes = numberOfFollowingBlockettes;
            return this;
        }

        public SeedDataHeaderBuilder sampleRateFactor(int sampleRateFactor) {
            this.sampleRateFactor = sampleRateFactor;
            return this;
        }

        public SeedDataHeaderBuilder timeCorrection(int timeCorrection) {
            this.timeCorrection = timeCorrection;
            return this;
        }

        public SeedDataHeaderBuilder beginingOfData(int beginingOfData) {
            this.beginingOfData = beginingOfData;
            return this;
        }

        public SeedDataHeaderBuilder firstDataBlockette(int firstDataBlockette) {
            this.firstDataBlockette = firstDataBlockette;
            return this;
        }

        public SeedDataHeader build() throws SeedException {
            SeedDataHeader header = new SeedDataHeader(sequence, type, reserved);
            header.network = network;
            header.station = station;
            header.location = location;
            header.channel = channel;
            header.start = start;
            header.numberOfSamples = numberOfSamples;
            header.sampleRateMultiplier = sampleRateMultiplier;
            header.activityFlags = activityFlags==null?ActivityFlags.valueOf(0):activityFlags;
            header.ioClockFlag = ioClockFlag;
            header.qualityIndicator = qualityIndicator==null?QualityFlags.valueOf(0):qualityIndicator;
            header.numberOfFollowingBlockettes = numberOfFollowingBlockettes;
            header.sampleRateFactor = sampleRateFactor;
            header.timeCorrection = timeCorrection;
            header.beginningOfData = beginingOfData;
            header.firstDataBlockette = firstDataBlockette;
            return header;
        }
    }
}
