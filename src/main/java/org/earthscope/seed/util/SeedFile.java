package org.earthscope.seed.util;


import org.earthscope.seed.data.DataRecord;
import org.earthscope.seed.data.SeedDataHeader;

import java.io.*;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Objects;

public class SeedFile {
    private SeedFile() {
    }

    public static int determineRecordSize(final File file) throws IOException {
        Objects.requireNonNull(file);
        try (InputStream inputStream = new FileInputStream(file)) {
            return SeedIO.determineRecordSize(inputStream);
        }
    }

    public static ByteOrder determineByteOrder(final File file) throws IOException {
        Objects.requireNonNull(file);
        try (InputStream inputStream = new FileInputStream(file)) {
            return SeedIO.determineByteOrder(inputStream);
        }
    }

    public static int countRecords(final File file) throws IOException {
        Objects.requireNonNull(file);
        try (InputStream inputStream = new FileInputStream(file)) {
            return SeedIO.countRecords(inputStream);
        }
    }

    public static List<SeedDataHeader> readHeaders(final File file) throws IOException {
        Objects.requireNonNull(file);
        try (InputStream inputStream = new FileInputStream(file)) {
            return SeedIO.readHeaders(inputStream);
        }
    }
    public static List<DataRecord> read(final File file) throws IOException {
        Objects.requireNonNull(file);
        try (InputStream inputStream = new FileInputStream(file)) {
            return SeedIO.read(inputStream);
        }
    }
    /**
     * Returns an Iterator for the records in a {@code File}.
     * <p>
     * This method opens an {@code InputStream} for the file.
     * When you have finished with the iterator you should close the stream
     * to free internal resources. This can be done by calling the
     * {@link RecordIterator#close()} or
     * {@link RecordIterator#closeQuietly(RecordIterator)} method.
     * </p>
     * <p>
     * The recommended usage pattern is:
     * </p>
     * <pre>
     * RecordIterator it = SeedFile.iterator(file);
     * try {
     *   while (it.hasNext()) {
     *     DataRecord record = it.nextRecord();
     *     /// do something with record
     *   }
     * } finally {
     *   RecordIterator.closeQuietly(iterator);
     * }
     * </pre>
     * <p>
     * If an exception occurs during the creation of the iterator, the
     * underlying stream is closed.
     * </p>
     *
     * @param file     the file to open for input, must not be {@code null}
     * @return an Iterator of the records in the file, never {@code null}
     * @throws NullPointerException if file is {@code null}.
     * @throws FileNotFoundException if the file does not exist, is a directory rather than a regular file, or for some
     *         other reason cannot be opened for reading.
     * @throws IOException if an I/O error occurs.
     * @since 1.2
     */
    public static RecordIterator iterate(final File file) throws IOException {
        Objects.requireNonNull(file);
        return SeedIO.iterate(new FileInputStream(file));
    }

    public static void write(File file, DataRecord dataRecord) throws IOException {
        write(file, dataRecord, ByteOrder.BIG_ENDIAN);
    }

    public static void print(File file, OutputStream outputStream) throws IOException {
        Objects.requireNonNull(file);
        try (InputStream inputStream = new FileInputStream(file)) {
            SeedIO.print(inputStream, outputStream);
        }
    }

    public static void write(File file, DataRecord dataRecord, ByteOrder byteOrder) throws IOException {

    }

    public static void write(File file, List<DataRecord> dataRecords) throws IOException {
        write(file, dataRecords, ByteOrder.BIG_ENDIAN);
    }

    public static void write(File file, List<DataRecord> dataRecords, ByteOrder byteOrder) throws IOException {
        for (DataRecord dr : dataRecords) {
            write(file, dr, byteOrder);
        }
    }

    public static void resize(File file, ByteOrder byteOrder) throws IOException {

    }

    public static void resize(File file, int recordLength) throws IOException {
    }

    public static void resize(File file, ByteOrder byteOrder, int recordLength) throws IOException {
    }
}
