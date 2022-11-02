package org.earthscope.seed.data;

import org.earthscope.seed.SeedFlags;

/*-
 * 
 * UBYTE: Activity flags:
 	[Bit 0] — Calibration signals present
	[Bit 1] — Time correction applied. Set this bit to 1 if the time correction 
				in field 16 has been applied to field 8. Set this bit to 0 if 
				the time correction in field 16 has not been applied to
				field 8.
	[Bit 2] — Beginning of an event, station trigger
	[Bit 3] — End of the event, station detriggers
	[Bit 4] — A positive leap second happened during this record (A 61 second minute).
	[Bit 5] — A negative leap second happened during this record (A 59 second minute). A negative
				leap second clock correction has not yet been used, but the U.S. National Bureau of
				Standards has said that it might be necessary someday.
	[Bit 6] — Event in progress
 */
public class ActivityFlags extends SeedFlags {

	public ActivityFlags(boolean calibrationSignalsPresent, boolean timeCorrectionApplied, boolean beginningOfAnEvent,
			boolean endOfTheEvent, boolean positiveLeapSecondHappenedDuringThisRecord,
			boolean negativeLeapSecondHappenedDuringThisRecord, boolean eventInProgress) {
		super(calibrationSignalsPresent, timeCorrectionApplied, beginningOfAnEvent, endOfTheEvent,
				positiveLeapSecondHappenedDuringThisRecord, negativeLeapSecondHappenedDuringThisRecord, eventInProgress,
				false);
	}

	public ActivityFlags(int value) {
		super(value);
	}

	public boolean isCalibrationSignalsPresent() {
		return this.isOn(0);
	}

	public boolean isTimeCorrectionApplied() {
		return this.isOn(1);
	}

	public boolean isBeginningOfAnEvent() {
		return this.isOn(2);
	}

	public boolean isEndOfTheEvent() {
		return this.isOn(3);
	}

	public boolean isPositiveLeapSecondHappenedDuringThisRecord() {
		return this.isOn(4);
	}

	public boolean isNegativeLeapSecondHappenedDuringThisRecord() {
		return this.isOn(5);
	}

	public boolean isEventInProgress() {
		return this.isOn(6);
	}

	public void setCalibrationSignalsPresent(boolean calibrationSignalsPresent) {
		this.set(0, calibrationSignalsPresent);
	}

	public void setTimeCorrectionApplied(boolean timeCorrectionApplied) {
		this.set(1, timeCorrectionApplied);
	}

	public void setBeginningOfAnEvent(boolean beginningOfAnEvent) {
		this.set(2, beginningOfAnEvent);
	}

	public void setEndOfTheEvent(boolean endOfTheEvent) {
		this.set(3, endOfTheEvent);
	}

	public void setPositiveLeapSecondHappenedDuringThisRecord(boolean positiveLeapSecondHappenedDuringThisRecord) {
		this.set(4, positiveLeapSecondHappenedDuringThisRecord);
	}

	public void setNegativeLeapSecondHappenedDuringThisRecord(boolean negativeLeapSecondHappenedDuringThisRecord) {
		this.set(5, negativeLeapSecondHappenedDuringThisRecord);
	}

	public void setEventInProgress(boolean eventInProgress) {
		this.set(6, eventInProgress);
	}

	public static ActivityFlagsBuilder builder() {
		return new ActivityFlagsBuilder();
	}

	public static class ActivityFlagsBuilder {
		private boolean calibrationSignalsPresent;
		private boolean timeCorrectionApplied;
		private boolean beginningOfAnEvent;
		private boolean endOfTheEvent;
		private boolean positiveLeapSecondHappenedDuringThisRecord;
		private boolean negativeLeapSecondHappenedDuringThisRecord;
		private boolean eventInProgress;

		public ActivityFlagsBuilder calibrationSignalsPresent(boolean calibrationSignalsPresent) {
			this.calibrationSignalsPresent = calibrationSignalsPresent;
			return this;
		}

		public ActivityFlagsBuilder timeCorrectionApplied(boolean timeCorrectionApplied) {
			this.timeCorrectionApplied = timeCorrectionApplied;
			return this;
		}

		public ActivityFlagsBuilder beginningOfAnEvent(boolean beginningOfAnEvent) {
			this.beginningOfAnEvent = beginningOfAnEvent;
			return this;
		}

		public ActivityFlagsBuilder endOfTheEvent(boolean endOfTheEvent) {
			this.endOfTheEvent = endOfTheEvent;
			return this;
		}

		public ActivityFlagsBuilder positiveLeapSecondHappenedDuringThisRecord(
				boolean positiveLeapSecondHappenedDuringThisRecord) {
			this.positiveLeapSecondHappenedDuringThisRecord = positiveLeapSecondHappenedDuringThisRecord;
			return this;
		}

		public ActivityFlagsBuilder negativeLeapSecondHappenedDuringThisRecord(
				boolean negativeLeapSecondHappenedDuringThisRecord) {
			this.negativeLeapSecondHappenedDuringThisRecord = negativeLeapSecondHappenedDuringThisRecord;
			return this;
		}

		public ActivityFlagsBuilder eventInProgress(boolean eventInProgress) {
			this.eventInProgress = eventInProgress;
			return this;
		}

		public ActivityFlags build() {
			return ActivityFlags.valueOf(calibrationSignalsPresent, timeCorrectionApplied, beginningOfAnEvent,
					endOfTheEvent, positiveLeapSecondHappenedDuringThisRecord,
					negativeLeapSecondHappenedDuringThisRecord, eventInProgress);
		}
	}

	public static ActivityFlags valueOf(int value) {
		return valueOf((byte) value);
	}

	public static ActivityFlags valueOf(byte value) {
		return new ActivityFlags(value);
	}

	public static ActivityFlags valueOf(boolean calibrationSignalsPresent, boolean timeCorrectionApplied,
			boolean beginningOfAnEvent, boolean endOfTheEvent, boolean positiveLeapSecondHappenedDuringThisRecord,
			boolean negativeLeapSecondHappenedDuringThisRecord, boolean eventInProgress) {
		return new ActivityFlags(calibrationSignalsPresent, timeCorrectionApplied, beginningOfAnEvent, endOfTheEvent,
				positiveLeapSecondHappenedDuringThisRecord, negativeLeapSecondHappenedDuringThisRecord,
				eventInProgress);
	}
}
