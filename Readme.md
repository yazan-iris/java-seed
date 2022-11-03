A Java library to read seed records from a stream.  SeedInputStream determines the record length and read SEED records one by one.
If no more records are available a null is returned.  
SeedInputStream.read returns a byte array containing the SEED packets it does not unpack them.

RecordReader on the other hand reads the bytes from SeedInputStream into DataRecord, it contains all the data blockettes in addition to the samples which are disabled at this time.
```java
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
                System.out.println(header.getNetwork()+"/"+header.getStation()+"/"+header.getLocation()+"/"+header.getChannel()+":"+header.getStart().format());
        Instant endTime = DateTimeUtil.computeEndTime(header, null, null);
            }
        }
```

```java
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
```