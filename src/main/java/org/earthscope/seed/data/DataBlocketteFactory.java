package org.earthscope.seed.data;

import lombok.extern.slf4j.Slf4j;
import org.earthscope.seed.SeedException;
import org.earthscope.seed.SeedRecordType;
import org.earthscope.seed.codec.EncodingFormat;
import org.earthscope.seed.util.SeedByteBuffer;

import java.nio.ByteOrder;
import java.util.BitSet;
import java.util.Objects;

@Slf4j
public class DataBlocketteFactory {

	private DataBlocketteFactory() {
	}

	public static DataBlockette create(byte[] bytes) throws SeedException {
		return create(bytes, 0);
	}

	public static DataBlockette create(byte[] bytes, ByteOrder byteOrder) throws SeedException {
		return create(bytes, 0, byteOrder);
	}

	public static DataBlockette create(byte[] bytes, int offset) throws SeedException {
		return create(bytes, offset, ByteOrder.BIG_ENDIAN);
	}

	public static DataBlockette create(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "bytes cannot be null");
		if (offset + 2 >= bytes.length) {
			throw new SeedException("Expected at least 2 bytes but was {}, index={}", bytes.length, offset);
		}
		int type = 0;
		try {
			type = SeedByteBuffer.wrap(bytes, offset, 2, byteOrder).getUnsignedShort();
			return create(bytes, type, offset, byteOrder);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			throw new SeedException("Error parsing bytes {} at offset {}", type, offset, e);
		}
	}

	public static SeedDataHeader createHeader(byte[] bytes) throws SeedException {
		if (bytes == null) {
			throw new SeedException("No data to read from buffer, NULL ");
		}

		if (bytes.length < 48) {
			throw new SeedException("Invalid bytes size, must be 48 ");
		}
		SeedByteBuffer byteBufferReader = SeedByteBuffer.wrap(bytes);
		SeedDataHeader header = new SeedDataHeader(byteBufferReader.getSequence(), SeedRecordType.from((char) byteBufferReader.getByte()),
				(char) byteBufferReader.getByte());

		header.setStation(clean(byteBufferReader.getStationCode()));
		header.setLocation(byteBufferReader.getLocationCode());
		header.setChannel(clean(byteBufferReader.getChannelCode()));
		header.setNetwork(clean(byteBufferReader.getNetworkCode()));

		header.setStart(byteBufferReader.getTime());
		int year = header.getStart().getYear();
		if (year < 1900 || year > 2050) {
			byteBufferReader.rewind(10);
			byteBufferReader.order(ByteOrder.LITTLE_ENDIAN);
			header.setStart(byteBufferReader.getTime());
			header.setByteOrder(ByteOrder.LITTLE_ENDIAN);
		}
		header.setNumberOfSamples(byteBufferReader.getUnsignedShort());
		header.setSampleRateFactor(byteBufferReader.getShort());
		header.setSampleRateMultiplier(byteBufferReader.getUnsignedShort());
		header.setActivityFlags(ActivityFlags.valueOf(byteBufferReader.getUnsignedByte()));
		header.setIoClockFlag(byteBufferReader.getUnsignedByte());
		header.setQualityIndicator(QualityFlags.valueOf(byteBufferReader.getUnsignedByte()));
		header.setNumberOfFollowingBlockettes(byteBufferReader.getUnsignedByte());
		header.setTimeCorrection(byteBufferReader.getInt());
		header.setBeginningOfData(byteBufferReader.getUnsignedShort());
		header.setFirstDataBlockette(byteBufferReader.getUnsignedShort());
		return header;
	}

	private static String clean(String src) {
		Objects.requireNonNull(src, "src string cannot be null");
		return src.trim();
	}

	public static DataBlockette create(byte[] bytes, int type, int index, ByteOrder byteOrder) throws SeedException {
		log.debug("DataBlockette create({}, {}, {})",type, index, byteOrder);
		switch (type) {
		case 100:
			return createB100(bytes, index, byteOrder);
		case 200:
			return createB200(bytes, index, byteOrder);
		case 201:
			return createB201(bytes, index, byteOrder);
		case 202:
			return createB202(bytes, index, byteOrder);
		case 300:
			return createB300(bytes, index, byteOrder);
		case 310:
			return createB310(bytes, index, byteOrder);
		case 320:
			return createB320(bytes, index, byteOrder);
		case 390:
			return createB390(bytes, index, byteOrder);
		case 395:
			return createB395(bytes, index, byteOrder);
		case 400:
			return createB400(bytes, index, byteOrder);
		case 405:
			return createB405(bytes, index, byteOrder);
		case 500:
			return createB500(bytes, index, byteOrder);
		case 1000:
			return createB1000(bytes, index, byteOrder);
		case 1001:
			return createB1001(bytes, index, byteOrder);
		case 2000:
			return createB2000(bytes, index, byteOrder);
		default:
			throw new SeedException("Unkown blockette type {}", type);
		}
	}

	public static B100 createB100(byte[] bytes) throws SeedException {
		return createB100(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B100 createB100(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B100: null bytes");
		return createB100(SeedByteBuffer.wrap(bytes, offset, 12, byteOrder));
	}

	public static B100 createB100(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B100: null byteArray");
		byteArray.checkSize(12).checkType(100);
		B100 b = new B100();

		b.setNextBlocketteByteNumber(byteArray.getShort());
		b.setActualSampleRate(byteArray.getFloat());
		b.setFlags(byteArray.getByte());
		return b;
	}

	public static B200 createB200(byte[] bytes) throws SeedException {
		return createB200(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B200 createB200(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B200: null bytes");
		return createB200(SeedByteBuffer.wrap(bytes, offset, 52, byteOrder));
	}

	public static B200 createB200(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B200: null byteArray");
		byteArray.checkSize(52).checkType(200);
		B200 b = new B200();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());
		b.setSignalAmplitude(byteArray.getFloat());
		b.setSignalPeriod(byteArray.getFloat());
		b.setBackgroundEstimate(byteArray.getFloat());
		BitSet bs = BitSet.valueOf(new byte[] { (byte) byteArray.getUnsignedByte() });
		b.setWaveType(B200.WaveType.from(bs));
		b.setAfterDeconvolution(bs.get(1));
		byteArray.skip();
		b.setSignalOnsetTime(byteArray.getTime());
		b.setDetectorName(byteArray.read(24));
		return b;
	}

	public static B201 createB201(byte[] bytes) throws SeedException {
		return createB201(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B201 createB201(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B201: null bytes");
		return createB201(SeedByteBuffer.wrap(bytes, offset, 60, byteOrder));
	}

	public static B201 createB201(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B201: null byteArray");
		byteArray.checkSize(60).checkType(201);
		B201 b = new B201();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());
		b.setSignalAmplitude(byteArray.getFloat());
		b.setSignalPeriod(byteArray.getFloat());

		b.setBackgroundEstimate(byteArray.getFloat());
		BitSet bs = BitSet.valueOf(new byte[] { (byte) byteArray.getUnsignedByte() });
		b.setWaveType(B200.WaveType.from(bs));
		byteArray.skip();
		b.setSignalOnsetTime(byteArray.getTime());
		int[] array = new int[6];
		for (int i = 0; i < array.length; i++) {
			array[i] = byteArray.getUnsignedByte();
		}

		b.setSignalToNoiseRatioValues(array);
		b.setLookbackValue(byteArray.getUnsignedByte());
		b.setPickAlgorithm(byteArray.getUnsignedByte());
		b.setDetectorName(byteArray.read(24));
		return b;
	}

	public static B202 createB202(byte[] bytes) throws SeedException {
		return createB202(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B202 createB202(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes);
		SeedByteBuffer.wrap(bytes, offset, bytes.length, byteOrder);
		return createB202(SeedByteBuffer.wrap(bytes, offset, byteOrder));
	}
	public static B202 createB202(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B300: null byteArray");
		byteArray.checkType(202);
		B202 b = new B202();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());
		return b;
	}
	public static B300 createB300(byte[] bytes) throws SeedException {
		return createB300(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B300 createB300(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B300: null bytes");
		return createB300(SeedByteBuffer.wrap(bytes, offset, 60, byteOrder));
	}

	/*-
	 * 	1 Blockette type — 300 B 2
		2 Next blockette’s byte number B 2
		3 Beginning of calibration time B 10
		4 Number of step calibrations B 1
		5 Calibration flags B 1
		6 Step duration B 4
		7 Interval duration B 4
		8 Calibration signal amplitude B 4
		9 Channel with calibration input A 3
		10 Reserved byte B 1
		11 Reference amplitude B 4
		12 Coupling A 12
		13 Rolloff A 12
	 */
	public static B300 createB300(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B300: null byteArray");
		byteArray.checkSize(60).checkType(300);
		B300 b = new B300();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());
		b.setBeginningOfCalibrationTime(byteArray.getTime());
		b.setNumberOfStepCalibrationsInSequence(byteArray.getUnsignedByte());
		b.setCalibrationFlags(BitSet.valueOf(new byte[] { (byte) byteArray.getUnsignedByte() }));
		b.setStepDuration(byteArray.getUnsignedInt());
		b.setIntervalDuration(byteArray.getUnsignedInt());
		b.setCalibrationSignalAmplitude(byteArray.getFloat());
		b.setChannelWithCalibrationInput(byteArray.read(3));
		byteArray.skip();
		b.setReferenceAmplitude(byteArray.getFloat());
		b.setCoupling(byteArray.read(12));
		b.setRolloff(byteArray.read(12));
		return b;
	}

	public static B310 createB310(byte[] bytes) throws SeedException {
		return createB310(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B310 createB310(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B310: null bytes");
		return createB310(SeedByteBuffer.wrap(bytes, offset, 60, byteOrder));
	}

	/*-
	 * 	1 Blockette type — 310 B 2
		2 Next blockette’s byte number B 2
		3 Beginning of calibration time B 10
		4 Reserved byte B 1
		5 Calibration flags B 1
		6 Calibration duration B 4
		7 Period of signal (seconds) B 4
		8 Amplitude of signal B 4
		9 Channel with calibration input A 3
		10 Reserved byte B 1
		11 Reference amplitude B 4
		12 Coupling A 12
		13 Rolloff A 12
	 */
	public static B310 createB310(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B310: null byteArray");
		byteArray.checkSize(60).checkType(310);
		B310 b = new B310();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());
		b.setBeginningOfCalibrationTime(byteArray.getTime());
		byteArray.skip();
		b.setCalibrationFlags(BitSet.valueOf(new byte[] { (byte) byteArray.getUnsignedByte() }));
		b.setCalibrationDuration(byteArray.getUnsignedInt());
		b.setPeriodOfSignalInSeconds(byteArray.getFloat());
		b.setAmptitudeOfSignal(byteArray.getFloat());
		b.setChannelWithCalibrationInput(byteArray.read(3));
		byteArray.skip();
		b.setReferenceAmplitude(byteArray.getFloat());
		b.setCoupling(byteArray.read(12));
		b.setRolloff(byteArray.read(12));

		return b;
	}

	public static B320 createB320(byte[] bytes) throws SeedException {
		return createB320(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	/*-
	 * 	1 Blockette type — 320 B 2
		2 Next blockette’s byte number B 2
		3 Beginning of calibration time B 10
		4 Reserved byte B 1
		5 Calibration flags B 1
		6 Calibration duration B 4
		7 Peak-to-peak amplitude of steps B 4
		8 Channel with calibration input A 3
		9 Reserved byte B 1
		10 Reference amplitude B 4
		11 Coupling A 12
		12 Rolloff A 12
		13 Noise type A 8
	 */
	public static B320 createB320(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B320: null bytes");
		return createB320(SeedByteBuffer.wrap(bytes, offset, 64, byteOrder));
	}

	/*-
	 * 	1 Blockette type — 320 				B 2
		2 Next blockette’s byte number 		B 2
		3 Beginning of calibration time 	B 10
		4 Reserved byte 					B 1
		5 Calibration flags					B 1
		6 Calibration duration 				B 4
		7 Peak-to-peak amplitude of steps 	B 4
		8 Channel with calibration input  	A 3
		9 Reserved byte 					B 1
		10 Reference amplitude 				B 4
		11 Coupling 						A 12
		12 Rolloff 							A 12
		13 Noise type 						A 8
	 */
	public static B320 createB320(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B320: null byteArray");
		byteArray.checkSize(64).checkType(320);
		B320 b = new B320();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());
		b.setBeginningOfCalibrationTime(byteArray.getTime());
		byteArray.skip();
		b.setCalibrationFlags(BitSet.valueOf(new byte[] { (byte) byteArray.getUnsignedByte() }));
		b.setCalibrationDuration(byteArray.getUnsignedInt());
		b.setPeakToPeakAmplitudeOfSteps(byteArray.getFloat());
		b.setChannelWithCalibrationInput(byteArray.read(3));
		byteArray.skip();
		b.setReferenceAmplitude(byteArray.getFloat());
		b.setCoupling(byteArray.read(12));
		b.setRolloff(byteArray.read(12));
		b.setNoiseType(byteArray.read(8));
		return b;
	}

	public static B390 createB390(byte[] bytes) throws SeedException {
		return createB390(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B390 createB390(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B390: null bytes");
		return createB390(SeedByteBuffer.wrap(bytes, offset, 28, byteOrder));
	}

	/*-
	 * 	1 Blockette type — 390 B 2
		2 Next blockette’s byte number B 2
		3 Beginning of calibration time B 10
		4 Reserved byte B 1
		5 Calibration flags B 1
		6 Calibration duration B 4
		7 Calibration signal amplitude B 4
		8 Channel with calibration input A 3
		9 Reserved byte B 1
	 */
	public static B390 createB390(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B390: null byteArray");
		byteArray.checkSize(28).checkType(390);

		B390 b = new B390();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());
		b.setBeginningOfCalibrationTime(byteArray.getTime());
		byteArray.skip();
		b.setCalibrationFlags(BitSet.valueOf(new byte[] { (byte) byteArray.getUnsignedByte() }));
		b.setCalibrationDuration(byteArray.getUnsignedInt());
		b.setCalibrationSignalAmplitude(byteArray.getFloat());
		b.setChannelWithCalibrationInput(byteArray.read(3));
		return b;
	}

	public static B395 createB395(byte[] bytes) throws SeedException {
		Objects.requireNonNull(bytes, "B395: null bytes");
		return createB395(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B395 createB395(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B390: null bytes");
		return createB395(SeedByteBuffer.wrap(bytes, offset, 16, byteOrder));
	}

	/*-
	 *  1 Blockette type — 395 B 2
		2 Next blockette’s byte number B 2
		3 End of calibration time B 10
		4 Reserved bytes B 2
	 */
	public static B395 createB395(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B395: null byteArray");
		byteArray.checkSize(16).checkType(395);

		B395 b = new B395();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());
		b.setEndOfCalibrationTime(byteArray.getTime());
		return b;
	}

	public static B400 create400(byte[] bytes) throws SeedException {
		return createB400(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B400 createB400(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B400: null bytes");
		return createB400(SeedByteBuffer.wrap(bytes, offset, 16, byteOrder));
	}

	/*-
	 *  1 Blockette type — 400 B 2
		2 Next blockette’s byte number B 2
		3 Beam azimuth (degrees) B 4
		4 Beam slowness (sec/degree) B 4
		5 Beam configuration B 2
		6 Reserved bytes B 2
	 */
	public static B400 createB400(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B400: null byteArray");
		byteArray.checkSize(16).checkType(400);
		B400 b = new B400();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());
		b.setBeamAzimuthInDegrees(byteArray.getFloat());
		b.setBeamSlownessInSecDegree(byteArray.getFloat());
		b.setBeamConfiguration(byteArray.getUnsignedShort());
		b.setReserved(byteArray.getUnsignedShort());
		return b;
	}

	public static B405 createB405(byte[] bytes) throws SeedException {
		return createB405(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B405 createB405(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B405: null bytes");
		return createB405(SeedByteBuffer.wrap(bytes, offset, 6, byteOrder));
	}

	public static B405 createB405(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B500: null byteArray");
		byteArray.checkSize(6).checkType(405);
		B405 b = new B405();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());
		b.setArrayOfDelayValues(byteArray.getUnsignedShort());
		return b;
	}

	public static B500 createB500(byte[] bytes) throws SeedException {
		return createB500(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B500 createB500(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B500: null bytes");
		return createB500(SeedByteBuffer.wrap(bytes, offset, 200, byteOrder));
	}

	/*-
	 * 	1 Blockette type — 500 B 2
		2 Next blockette offset B 2
		3 VCO correction B 4
		4 Time of exception B 10
		5 µsec B 1
		6 Reception Quality B 1
		7 Exception count B 4
		8 Exception type A 16
		9 Clock model A 32
		10 Clock status A 128
	 */
	public static B500 createB500(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B500: null byteArray");
		byteArray.checkSize(200).checkType(500);

		B500 b = new B500();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());
		b.setVocCorrection(byteArray.getFloat());
		b.setTimeOfException(byteArray.getTime());
		b.setClockTime(byteArray.getByte());
		b.setReceptionQuality(byteArray.getUnsignedByte());
		b.setExceptionCount(byteArray.getInt());
		b.setExceptionType(byteArray.read(16));
		b.setClockModel(byteArray.read(32));
		b.setClockStatus(byteArray.read(128));
		return b;
	}

	public static B1000 createB1000(byte[] bytes) throws SeedException {
		return createB1000(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B1000 createB1000(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B1000: null bytes");
		return createB1000(SeedByteBuffer.wrap(bytes, offset, 8, byteOrder));
	}

	/*-
	 * 	1 Blockette type — 1000 B 2
		2 Next blockette’s byte number B 2
		3 Encoding Format B 1
		4 Word order B 1
		5 Data Record Length B 1
		6 Reserved B 1
	 */
	public static B1000 createB1000(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B1000: null byteArray");
		byteArray.checkSize(8).checkType(1000);
		B1000 b = new B1000();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());

		b.setEncodingFormat(EncodingFormat.valueOf(byteArray.getUnsignedByte()));
		int order = byteArray.getUnsignedByte();
		ByteOrder bo = ByteOrder.BIG_ENDIAN;
		if (order == 0) {
			bo = ByteOrder.LITTLE_ENDIAN;
		}
		b.setByteOrder(bo);
		b.setRecordLengthExponent(byteArray.getUnsignedByte());
		b.setReserved(byteArray.getUnsignedByte());
		return b;
	}

	public static B1001 createB1001(byte[] bytes) throws SeedException {
		return createB1001(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B1001 createB1001(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B1001: null bytes");
		return createB1001(SeedByteBuffer.wrap(bytes, offset, 8, byteOrder));
	}

	/*-
	 *  1 Blockette type — 1001 B 2
		2 Next blockette’s byte number B 2
		3 Timing quality B 1
		4 µsec B 1
		5 Reserved B 1
		6 Frame count B 1
	 */
	public static B1001 createB1001(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B1001: null byteArray");
		log.trace("createB1001(byteArray)");
		byteArray.checkSize(8).checkType(1001);
		B1001 b = new B1001();
		b.setNextBlocketteByteNumber(byteArray.getUnsignedShort());
		b.setTimingQuality(byteArray.getUnsignedByte());
		b.setMicroSeconds(byteArray.getByte());
		byteArray.skip();
		b.setFrameCount(byteArray.getUnsignedByte());

		return b;
	}

	public static B2000 createB2000(byte[] bytes) throws SeedException {
		return createB2000(bytes, 0, ByteOrder.BIG_ENDIAN);
	}

	public static B2000 createB2000(byte[] bytes, int offset, ByteOrder byteOrder) throws SeedException {
		Objects.requireNonNull(bytes, "B2000: null bytes");
		return createB2000(SeedByteBuffer.wrap(bytes, offset, bytes.length-offset, byteOrder));
	}

	/*-
	 * 	1 Blockette type — 2000 B 2 0
		2 Next blockette’s byte number B 2 2
		3 Total blockette length in bytes B 2 4
		4 Offset to Opaque Data B 2 6
		5 Record number B 4 8
		6 Data Word order B 1 12
		7 Opaque Data flags B 1 13
		8 Number of Opaque Header fields B 1 14
		9 Opaque Data Header fields V V 15
			a Record type
			b Vendor type
			c Model type
			d Software
			e Firmware
		10 Opaque Data Opaque
	 */
	public static B2000 createB2000(SeedByteBuffer byteArray) throws SeedException {
		Objects.requireNonNull(byteArray, "B2000: null byteArray");
		byteArray.checkType(2000);
		int nextBlocketteOffset = byteArray.getUnsignedShort();
		int length = byteArray.getUnsignedShort();
		byteArray.checkSize(length-6);
		B2000 b = new B2000();
		b.setNextBlocketteByteNumber(nextBlocketteOffset);
		b.setTotalBlocketteLengthInBytes(length);

		b.setOffsetToOpaqueData(byteArray.getUnsignedShort());
		b.setRecordNumber(byteArray.getUnsignedInt());
		int order = byteArray.getUnsignedByte();
		ByteOrder bo = ByteOrder.BIG_ENDIAN;
		if (order == 0) {
			bo = ByteOrder.LITTLE_ENDIAN;
		}
		b.setByteOrder(bo);

		b.setOpaqueDataFlags(BitSet.valueOf(new byte[] { (byte) byteArray.getUnsignedByte() }));
		int numberOfOpaqueHeaderFields = byteArray.getUnsignedByte();
		b.setNumberOfOpaqueHeaderFields(numberOfOpaqueHeaderFields);
		String[] fields = new String[numberOfOpaqueHeaderFields];

		for (int i = 0; i < numberOfOpaqueHeaderFields; i++) {
			String opaqueHeaderField=byteArray.getUntil('~');
			fields[i] = opaqueHeaderField;
		}
		b.setOpaqueHeaders(fields);
		String[] data = new String[numberOfOpaqueHeaderFields];
		for (int i = 0; i < numberOfOpaqueHeaderFields; i++) {
			data[i] = byteArray.getUntil('~');
		}
		b.addData(data);
		return b;
	}

	public static void validate(byte[] bytes, int expectedType) throws SeedException {
		validate(bytes, expectedType, 0, ByteOrder.BIG_ENDIAN);
	}

	public static void validate(byte[] bytes, int expectedType, int offset, ByteOrder byteOrder) throws SeedException {
		if (bytes == null || bytes.length == 0) {
			throw new IllegalArgumentException("object null|empty");
		}
		int type = SeedByteBuffer.wrap(bytes, offset, byteOrder).getUnsignedShort();
		if (expectedType != type) {
			throw new SeedException("Invalid blockette type expected {} but was {}", expectedType, type);
		}
	}
}
