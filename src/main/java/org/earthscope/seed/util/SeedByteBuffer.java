package org.earthscope.seed.util;

import org.earthscope.seed.BTime;
import org.earthscope.seed.SeedException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Objects;

public class SeedByteBuffer {

    private final ByteBuffer bb;

    SeedByteBuffer(int capacity) {
        this(capacity, ByteOrder.BIG_ENDIAN);
    }

    SeedByteBuffer(int capacity, ByteOrder byteOrder) {
        bb = ByteBuffer.allocate(capacity).order(byteOrder);
    }

    SeedByteBuffer(byte[] bytes, ByteOrder byteOrder) {
        this(bytes, byteOrder, 0, bytes.length);
    }

    SeedByteBuffer(byte[] bytes, ByteOrder byteOrder, int offset, int length) {
        bb = ByteBuffer.wrap(bytes, offset, length).order(byteOrder);
    }

    public SeedByteBuffer order(ByteOrder byteOrder){
        bb.order(byteOrder);
        return this;
    }

    public ByteOrder order(){
        return bb.order();
    }

    public SeedByteBuffer checkType(int... types) throws SeedException {
        int actual = getUnsignedShort();
        for (int type : types) {
            if (type == actual) {
                return this;
            }
        }
        throw new SeedException("Invalid type, expected {} but found {}", Arrays.toString(types), actual);
    }

    public SeedByteBuffer checkSize(int expected) throws SeedException {
        if (bb.remaining()< expected) {
            throw new SeedException("Invalid type: expected at least {} but received {}", expected, bb.remaining());
        }
        return this;
    }

    /*public int length() {
        Objects.requireNonNull(this.bb, "bytes:null");
        bb.capacity();
        return this.bytes.length - this.otherOffset;
    }*/

    public int getSequence() {
        String sequence=this.read(6);
        if(sequence==null) {

        }
        return Integer.parseInt(sequence.trim());
    }


    public SeedByteBuffer putSequence(int sequence) {
        byte[] bytes = String.format("%06d", sequence).getBytes(StandardCharsets.US_ASCII);

        for (int i = 0; i < bytes.length; i++) {
            putByte(bytes[i]);
        }
        return this;
    }

    public String getUntil(char character) {
        StringBuilder b = new StringBuilder();
        while(bb.remaining()>1) {
            char c = (char) getByte();
            if(c == character){
                break;
            }else{
                b.append(c);
            }
        }
        return b.toString();
    }
    public String getNetworkCode(){
        return read(2);
    }

    public SeedByteBuffer putNetworkCode(String code) {
        return putCode(code, 2);
    }

    public String getStationCode(){
        return read(5);
    }

    public SeedByteBuffer putStationCode(String code) {
        return putCode(code, 5);
    }

    public String getChannelCode(){
        return read(3);
    }

    public SeedByteBuffer putChannelCode(String code) {
        return putCode(code, 3);
    }

    public String getLocationCode(){
        byte[] bytes = new byte[2];
        bb.get(bytes);
        return new String(bytes, StandardCharsets.US_ASCII);
    }

    public SeedByteBuffer putLocationCode(String code) {
        return putCode(code, 2);
    }

    public SeedByteBuffer putOneByteChar(char value) {
        bb.put((byte) ((value & 0xff)));
        return this;
    }

    public SeedByteBuffer putCode(String code, int length) {
        this.put(code, StandardCharsets.US_ASCII, length);
        return this;
    }
    public SeedByteBuffer put(String value, Charset charset, int length) {
        Objects.requireNonNull(value, "value cannot be null");
        byte[] src = value.getBytes(charset);
        byte[] dest = src;
        int l = length;
        if(src.length!=length) {
            l = length < src.length ? length : src.length;
            dest = new byte[length];
            System.arraycopy(src, 0, dest, 0, src.length);
        }
        bb.put(dest);
        return this;
    }

    public SeedByteBuffer putAndTerminate(String value, Charset charSet, char character) {
        Objects.requireNonNull(value, "DataByteArray: cannot write null value");
        //System.out.println(value+"    "+character);
        put(value, charSet, value.length()).putOneByteChar(character);
        return this;
    }

    public BTime getTime() throws SeedException {
        int year = getUnsignedShort();
        int dayOfYear = getUnsignedShort();

        int hour = getUnsignedByte();
        int minute = getUnsignedByte();
        int second = getUnsignedByte();
        // 27 unused
        // offset++;
        skip();
        int tenthMilliSecond = getUnsignedShort();
        return BTime.valueOf(year, dayOfYear, hour, minute, second, tenthMilliSecond);
    }

    public SeedByteBuffer putTime(BTime time) throws SeedException {
        Objects.requireNonNull(time);
        putUnsignedShort(time.getYear());
        putUnsignedShort(time.getDayOfYear());
        putUnsignedByte(time.getHour());
        putUnsignedByte(time.getMinute());
        putUnsignedByte(time.getSecond());
        // 27 unused
        // offset++;
        skip();
        putUnsignedShort(time.getTenthMilliSecond());
        return this;
    }

    public SeedByteBuffer rewind(int length){
        bb.position(bb.position()-length);
        return this;
    }

    public SeedByteBuffer skip(){
        return skip(1);
    }

    public SeedByteBuffer skip(int length){
        bb.position(bb.position()+length);
        return this;
    }

    public String read(int length) {
        return read(length, StandardCharsets.US_ASCII);
    }
    public String read(int length, Charset charset) {
        if (length < 0) {
            throw new IllegalArgumentException("Length cannot be negative");
        }
        if (length == 0) {
            return null;
        }
        byte[] bytes = new byte[length];
        bb.get(bytes);
        return new String(bytes, charset).trim();
    }

    public byte getByte() {
        return bb.get();
    }

    public SeedByteBuffer putByte(byte b) {
        bb.put(b);
        return this;
    }

    public SeedByteBuffer putBytes(int[] bytes) {
        Objects.requireNonNull(bytes);
        for(int b:bytes) {
            bb.put((byte) b);
        }
        return this;
    }

    public SeedByteBuffer putBytesOrSkipIfNull(byte[] bytes, int length) {
        if (bytes == null) {
            this.skip(length);
            return this;
        } else {
            this.checkRemaining(bytes.length);
            byte[] var3 = bytes;
            int var4 = bytes.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                byte b = var3[var5];
                this.putByte(b);
            }
            return this;
        }
    }

    public short getUnsignedByte() {
        return ((short) (bb.get() & 0xff));
    }

    public void putUnsignedByte(int value) {
        bb.put((byte) (value & 0xff));
    }

    public short getUnsignedByte(int position) {
        return ((short) (bb.get(position) & (short) 0xff));
    }

    public void putUnsignedByte(int position, int value) {
        bb.put(position, (byte) (value & 0xff));
    }

    public int getShort() {
        return bb.getShort();
    }

    public SeedByteBuffer putShort(int value) {
        bb.putShort((short) value);
        return this;
    }

    public int getUnsignedShort() {
        return (bb.getShort() & 0xffff);
    }

    public SeedByteBuffer putUnsignedShort(int value) {
        bb.putShort((short) (value & 0xffff));
        return this;
    }

    public int getUnsignedShort(int position) {
        return (bb.getShort(position) & 0xffff);
    }

    public SeedByteBuffer putUnsignedShort(int position, int value) {
        bb.putShort(position, (short) (value & 0xffff));
        return this;
    }

    public int getInt() {
        return bb.getInt();
    }

    public SeedByteBuffer putInt(long value) {
        bb.putInt((int) value);
        return this;
    }

    public SeedByteBuffer putInt(int value) {
        bb.putInt(value);
        return this;
    }

    public long getUnsignedInt() {
        return ((long) bb.getInt() & 0xffffffffL);
    }

    public SeedByteBuffer putUnsignedInt(long value) {
        bb.putInt((int) (value & 0xffffffffL));
        return this;
    }

    public long getUnsignedInt(int position) {
        return ((long) bb.getInt(position) & 0xffffffffL);
    }

    public SeedByteBuffer putUnsignedInt(int position, long value) {
        bb.putInt(position, (int) (value & 0xffffffffL));
        return this;
    }

    public SeedByteBuffer putFloat(float value){
        bb.putFloat(value);
        return this;
    }

    public float getFloat(){
        return bb.getFloat();
    }

    public byte[] array(){
        return bb.array();
    }

    private void checkRemaining(int cnt) {
        Objects.requireNonNull(this.bb, "bytes cannot be null");
        if (cnt < 0) {
            throw new IllegalArgumentException("Invalid value " + cnt);
        } else if(bb.remaining()<cnt){
            throw new ArrayIndexOutOfBoundsException(bb.position() + cnt - 1);
        }
    }

    public static SeedByteBuffer allocate(int capacity) {
        return allocate(capacity, ByteOrder.BIG_ENDIAN);
    }

    public static SeedByteBuffer allocate(int capacity, ByteOrder byteOrder) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Invalid capacity value " + capacity);
        }
        return new SeedByteBuffer(new byte[capacity], byteOrder);
    }

    public static SeedByteBuffer wrap(byte[] bytes) {
        Objects.requireNonNull(bytes, "bytes cannot be null");
        return wrap(bytes, 0, bytes.length, ByteOrder.BIG_ENDIAN);
    }
    public static SeedByteBuffer wrap(byte[] bytes, ByteOrder byteOrder) {
        Objects.requireNonNull(bytes, "bytes cannot be null");
        return wrap(bytes, 0, bytes.length, byteOrder);
    }

    public static SeedByteBuffer wrap(byte[] bytes, int offset) {
        Objects.requireNonNull(bytes, "bytes cannot be null");
        return wrap(bytes, offset, bytes.length, ByteOrder.BIG_ENDIAN);
    }

    public static SeedByteBuffer wrap(byte[] bytes, int offset, ByteOrder byteOrder) {
        Objects.requireNonNull(bytes, "bytes cannot be null");
        return wrap(bytes, offset, bytes.length-offset, byteOrder);
    }

    public static SeedByteBuffer wrap(byte[] bytes, int offset, int length, ByteOrder byteOrder) {
        return new SeedByteBuffer(bytes, byteOrder, offset, length);
    }
}
