package org.earthscope.seed.data;


import org.earthscope.seed.SeedFlags;

/*-
 * UBYTE: Data quality flags
	[Bit 0] — Amplifier saturation detected (station dependent)
	[Bit 1] — Digitizer clipping detected
	[Bit 2] — Spikes detected
	[Bit 3] — Glitches detected
	[Bit 4] — Missing/padded data present
	[Bit 5] — Telemetry synchronization error
	[Bit 6] — A digital filter may be charging
	[Bit 7] — Time tag is questionable
 */
public class QualityFlags extends SeedFlags {

	public QualityFlags(boolean amplifierSaturationDetected, boolean digitizerClippingDetected,
                        boolean spikesDetected, boolean glitchesDetected, boolean missingOrPaddedDataPresent,
                        boolean telemetrySynchronizationError, boolean digitalFilterMayBeCharging, boolean timeTagIsQuestionable) {
		super(amplifierSaturationDetected, digitizerClippingDetected, spikesDetected, glitchesDetected,
				missingOrPaddedDataPresent, telemetrySynchronizationError, digitalFilterMayBeCharging,
				timeTagIsQuestionable);
	}

	public QualityFlags(int value) {
		super(value);
	}
	public boolean isAmplifierSaturationDetected() {
		return this.isOn(0);
	}

	public boolean isDigitizerClippingDetected() {
		return this.isOn(1);
	}

	public boolean isSpikesDetected() {
		return this.isOn(2);
	}

	public boolean isGlitchesDetected() {
		return this.isOn(3);
	}

	public boolean isMissingOrPaddedDataPresent() {
		return this.isOn(4);
	}

	public boolean isTelemetrySynchronizationError() {
		return this.isOn(5);
	}

	public boolean isDigitalFilterMayBeCharging() {
		return this.isOn(6);
	}

	public boolean isTimeTagIsQuestionable() {
		return this.isOn(7);
	}

	public void setAmplifierSaturationDetected(boolean amplifierSaturationDetected) {
		this.set(0, amplifierSaturationDetected);
	}

	public void setDigitizerClippingDetected(boolean digitizerClippingDetected) {
		this.set(1, digitizerClippingDetected);
	}

	public void setSpikesDetected(boolean spikesDetected) {
		this.set(2, spikesDetected);
	}

	public void setGlitchesDetected(boolean glitchesDetected) {
		this.set(3, glitchesDetected);
	}

	public void setMissingOrPaddedDataPresent(boolean missingOrPaddedDataPresent) {
		this.set(4, missingOrPaddedDataPresent);
	}

	public void setTelemetrySynchronizationError(boolean telemetrySynchronizationError) {
		this.set(5, telemetrySynchronizationError);
	}

	public void setDigitalFilterMayBeCharging(boolean digitalFilterMayBeCharging) {
		this.set(6, digitalFilterMayBeCharging);
	}

	public void setTimeTagIsQuestionable(boolean timeTagIsQuestionable) {
		this.set(7, timeTagIsQuestionable);
	}


	public static QualityIndicatorBuilder builder() {
		return new QualityIndicatorBuilder();
	}

	public static class QualityIndicatorBuilder {
		private boolean amplifierSaturationDetected;
		private boolean digitizerClippingDetected;
		private boolean spikesDetected;
		private boolean glitchesDetected;
		private boolean missingOrPaddedDataPresent;
		private boolean telemetrySynchronizationError;
		private boolean digitalFilterMayBeCharging;
		private boolean timeTagIsQuestionable;

		public QualityIndicatorBuilder amplifierSaturationDetected(boolean amplifierSaturationDetected) {
			this.amplifierSaturationDetected = amplifierSaturationDetected;
			return this;
		}

		public QualityIndicatorBuilder digitizerClippingDetected(boolean digitizerClippingDetected) {
			this.digitizerClippingDetected = digitizerClippingDetected;
			return this;
		}

		public QualityIndicatorBuilder spikesDetected(boolean spikesDetected) {
			this.spikesDetected = spikesDetected;
			return this;
		}

		public QualityIndicatorBuilder glitchesDetected(boolean glitchesDetected) {
			this.glitchesDetected = glitchesDetected;
			return this;
		}

		public QualityIndicatorBuilder missingOrPaddedDataPresent(boolean missingOrPaddedDataPresent) {
			this.missingOrPaddedDataPresent = missingOrPaddedDataPresent;
			return this;
		}

		public QualityIndicatorBuilder telemetrySynchronizationError(boolean telemetrySynchronizationError) {
			this.telemetrySynchronizationError = telemetrySynchronizationError;
			return this;
		}

		public QualityIndicatorBuilder digitalFilterMayBeCharging(boolean digitalFilterMayBeCharging) {
			this.digitalFilterMayBeCharging = digitalFilterMayBeCharging;
			return this;
		}

		public QualityIndicatorBuilder timeTagIsQuestionable(boolean timeTagIsQuestionable) {
			this.timeTagIsQuestionable = timeTagIsQuestionable;
			return this;
		}

		public QualityFlags build() {
			return QualityFlags.valueOf(amplifierSaturationDetected, digitizerClippingDetected, spikesDetected,
					glitchesDetected, missingOrPaddedDataPresent, telemetrySynchronizationError,
					digitalFilterMayBeCharging, timeTagIsQuestionable);
		}
	}

	public static QualityFlags valueOf(int value) {
		return valueOf((byte) value);
	}

	public static QualityFlags valueOf(byte value) {
		return new QualityFlags(value);
	}

	public static QualityFlags valueOf(boolean amplifierSaturationDetected, boolean digitizerClippingDetected,
			boolean spikesDetected, boolean glitchesDetected, boolean missingOrPaddedDataPresent,
			boolean telemetrySynchronizationError, boolean digitalFilterMayBeCharging, boolean timeTagIsQuestionable) {
		return new QualityFlags(amplifierSaturationDetected, digitizerClippingDetected, spikesDetected,
				glitchesDetected, missingOrPaddedDataPresent, telemetrySynchronizationError, digitalFilterMayBeCharging,
				timeTagIsQuestionable);
	}
}
