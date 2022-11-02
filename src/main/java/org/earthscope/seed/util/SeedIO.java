package org.earthscope.seed.util;


import org.earthscope.seed.BTime;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.data.DataBlockette;
import org.earthscope.seed.data.DataRecord;
import org.earthscope.seed.data.SeedDataHeader;
import org.earthscope.seed.io.RecordReader;
import org.earthscope.seed.io.SeedInputStream;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SeedIO {
    private SeedIO() {
    }

    public static int determineRecordSize(final InputStream inputStream) throws IOException {
        try (SeedInputStream sis = new SeedInputStream(inputStream)) {
            return sis.getRecordLength();
        }
    }

    public static ByteOrder determineByteOrder(final InputStream inputStream) throws IOException {
        try (SeedInputStream sis = new SeedInputStream(inputStream)) {
            byte[] bytes = sis.read();
            return BTime.determineByteOrder(bytes, 20);
        } catch (SeedException e) {
            throw new IOException(e);
        }
    }

    public static int countRecords(InputStream inputStream) throws IOException {
        try (SeedInputStream sis = new SeedInputStream(inputStream)) {
            int count = 0;
            while (true) {
                byte[] bytes = sis.read();
                if (bytes == null) {
                    break;
                }
                //make sure no exceptions
                SeedDataHeader.from(bytes);
                count++;
            }
            return count;
        } catch (SeedException e) {
            throw new IOException(e);
        }
    }

    public static List<SeedDataHeader> readHeaders(InputStream inputStream) throws IOException {
        try (SeedInputStream sis = new SeedInputStream(inputStream)) {
            List<SeedDataHeader> headers = new ArrayList<>();
            while (true) {
                byte[] bytes = sis.read();
                if (bytes == null) {
                    break;
                }
                headers.add(SeedDataHeader.from(bytes));
            }
            return headers;
        } catch (SeedException e) {
            throw new IOException(e);
        }
    }

    public static List<DataRecord> read(InputStream inputStream) throws IOException {
        try (RecordReader rr = new RecordReader(inputStream);) {
            List<DataRecord> records = new ArrayList<>();
            while (true) {
                DataRecord dataRecord = rr.read();
                if (dataRecord == null) {
                    break;
                }
                records.add(dataRecord);
            }
            return records;
        }
    }

    /**
     * Returns an Iterator for the lines in an {@code InputStream}, using
     * the character encoding specified (or default encoding if null).
     * <p>
     * {@code LineIterator} holds a reference to the open
     * {@code InputStream} specified here. When you have finished with
     * the iterator you should close the stream to free internal resources.
     * This can be done by closing the stream directly, or by calling
     * {@link RecordIterator#close()} or {@link RecordIterator#closeQuietly(RecordIterator)}.
     * <p>
     * The recommended usage pattern is:
     * <pre>
     * try {
     *   LineIterator it = IOUtils.lineIterator(stream, charset);
     *   while (it.hasNext()) {
     *     String line = it.nextLine();
     *     /// do something with line
     *   }
     * } finally {
     *   IOUtils.closeQuietly(stream);
     * }
     * </pre>
     *
     * @param inputStream the {@code InputStream} to read from, not null
     * @return an Iterator of the lines in the reader, never null
     * @throws IllegalArgumentException if the input is null
     * @throws IOException              Never thrown.
     */
    public static RecordIterator iterate(InputStream inputStream) throws IOException {
        Objects.requireNonNull(inputStream);
        return new RecordIterator(inputStream);
    }


    public static void print(InputStream inputStream, OutputStream outputStream) throws IOException {
        try (RecordReader rr = new RecordReader(inputStream); PrintWriter pw = new PrintWriter(outputStream);) {
            while (true) {
                DataRecord dataRecord = rr.read();
                if (dataRecord == null) {
                    break;
                }
                SeedDataHeader sdh = dataRecord.getHeader();
                pw.print(sdh);
                List<DataBlockette> dataBlockettes = dataRecord.getAll();
                if(dataBlockettes!=null){
                    for(DataBlockette db:dataBlockettes){
                        pw.print(db.toString());
                    }
                }
            }
        }
    }
}
