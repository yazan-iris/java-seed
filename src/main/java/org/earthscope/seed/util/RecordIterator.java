package org.earthscope.seed.util;


import org.earthscope.seed.data.DataRecord;
import org.earthscope.seed.io.RecordReader;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Similar to Apache IO LineIterator
 */
public class RecordIterator implements Closeable, Iterator<DataRecord>, Iterable<DataRecord> {

    private boolean finished = false;
    private RecordReader reader;
    private DataRecord cachedRecord;

    public RecordIterator(final InputStream inputStream) throws IllegalArgumentException, IOException {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream must not be null");
        }
        if (inputStream instanceof BufferedInputStream) {
            reader = new RecordReader(inputStream);
        } else {
            reader = new RecordReader(new BufferedInputStream(inputStream));
        }
    }

    @Override
    public Iterator<DataRecord> iterator() {
        return null;
    }

    @Override
    public boolean hasNext() {
        if (cachedRecord != null) {
            return true;
        }
        if (finished) {
            return false;
        }
        try {
            while (true) {
                final DataRecord record = reader.read();
                if (record == null) {
                    finished = true;
                    return false;
                }
                if (isValidLine(record)) {
                    cachedRecord = record;
                    return true;
                }
            }
        } catch (final IOException ioe) {
            ioe.printStackTrace();
            this.close();
            throw new IllegalStateException(ioe);
        }
    }

    /**
     * Overridable method to validate each line that is returned.
     * This implementation always returns true.
     *
     * @param record the record that is to be validated
     * @return true if valid, false to remove from the iterator
     */
    protected boolean isValidLine(final DataRecord record) {
        return true;
    }

    @Override
    public DataRecord next() {
        return nextRecord();
    }

    /**
     * Returns the next line in the wrapped {@code Reader}.
     *
     * @return the next line from the input
     * @throws NoSuchElementException if there is no line to return
     */
    public DataRecord nextRecord() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more lines");
        }
        final DataRecord currentRecord = cachedRecord;
        cachedRecord = null;
        return currentRecord;
    }

    /**
     * Closes the underlying <code>Reader</code> quietly.
     * This method is useful if you only want to process the first few
     * lines of a larger file. If you do not close the iterator
     * then the <code>Reader</code> remains open.
     * This method can safely be called multiple times.
     */
    @Override
    public void close() {
        finished = true;
        try{
            reader.close();
        }catch (IOException e){

        }
        cachedRecord = null;
    }

    //-----------------------------------------------------------------------

    /**
     * Closes the iterator, handling null and ignoring exceptions.
     *
     * @param iterator the iterator to close
     */
    public static void closeQuietly(final RecordIterator iterator) {
        if (iterator != null) {
            iterator.close();
        }
    }
}
