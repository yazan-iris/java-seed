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
            }
        }
```