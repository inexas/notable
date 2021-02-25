package org.inexas.notable.notation.model;

import org.inexas.notable.notation.parser.*;

import java.util.*;

/**
 * The TimeFrame collects the elements of a piece that must be common across
 * all phrases such as the length of measures for the duration of the
 * score. By keeping them here we force all of the phrases to be
 * synchronized.
 * <p>
 * The size for each measure will be greater than 1 so the initialized
 * value of zero means that the size has not been set.
 * <p>
 * Typically it will be the first phrase that sets the values but other
 * phrases may do so. Values should also be set successively so jumping
 * indicates that something is wrong.
 */
public class Timeline {
	public class Frame {
		private Frame pic; // Linked list: Previous
		public Frame nic; // Linked list: Next In Chain
		private TimeSignature timeSignature;
		public Cpm cpm;
		private Barline barline;
		public int actualSize = getEffectiveTimeSignature().getMeasureSize();

		Frame() {
			if(fic == null) {
				fic = lic = this;
			} else {
				pic = lic;
				lic.nic = this;
				lic = this;
			}
			frames.add(this);
		}

		public Barline getBarline() {
			return barline == Barline.next ? nic.getBarline() : barline;
		}

		public void setBarline(final Barline barline) {
			this.barline = barline;
		}

		void report(final Cpm cpm) {
			// We can only change the latest measure...
			if(this == lic) {
				if(this.cpm == null) {
					this.cpm = cpm;
					actualSize = cpm.clicks;
				} else {
					error("Attempt to redefine CPM");
				}
			} else {
				error("Can't change size of measure once it has been fixed elsewhere");
			}
		}

		void report(final TimeSignature timeSignature) {
			// We can only change the latest measure...
			if(this == lic) {
				if(this.timeSignature == null) {
					this.timeSignature = timeSignature;
					if(cpm == null) {
						actualSize = timeSignature.getMeasureSize();
					}
				} else {
					error("Attempt to redefine time signature");
				}
			} else {
				error("Can't change size of measure once it has been fixed elsewhere");
			}
		}

		void report(final Barline barline) {
			if(this.barline == null || this.barline == Barline.next) {
				this.barline = barline;
			} else if(this.barline != barline) {
				// Cannot change
				error("Barline was '" + this.barline + "', cannot change to '" + barline + '\'');
			}
		}

		/**
		 * @return the time signature for this Frame if one has been set or null
		 */
		TimeSignature getAppliedTimeSignature() {
			return timeSignature;
		}

		/**
		 * @return the time signature for this Frame if one has been set or null
		 */
		TimeSignature getEffectiveTimeSignature() {
			final TimeSignature result;
			if(timeSignature != null) {
				result = timeSignature;
			} else if(pic == null) {
				result = score.getDefaultTimeSignature();
			} else {
				result = pic.getEffectiveTimeSignature();
			}
			return result;
		}

		/**
		 * A repeat is normally started with a ':' but may also be
		 * implied at the start of the piece. So the best we can do is know
		 * that we are not in a repeat because we've seen and end repeat.
		 *
		 * @return false if this frame is not repeated.
		 */
		public boolean isRepeat() {
			final boolean result;
			if(pic == null) {
				result = seenStartRepeat;
			} else if(barline == null) {
				result = pic.isRepeat();
			} else {
				switch(barline) {
					case singleBar, doubleBar -> result = pic.isRepeat();
					case beginRepeat, beginEndRepeat, eosRepeat -> result = true;
					default -> result = false;
				}
			}

			return result;
		}

		public boolean isLast() {
			//If this measure is part of a multimeasure rest
			//then we need to look at the last in chain
			return barline == Barline.next ? nic.isLast() : this == lic;
		}
	}

	private final Score score;
	private final Messages messages;
	private final List<Frame> frames = new ArrayList<>();
	private Frame fic, lic;
	private boolean seenStartRepeat;

	Timeline(final Score score) {
		this.score = score;
		messages = score.messages;
	}

	public Frame getFrame(final int ordinal) {
		return frames.get(ordinal);
	}

	Frame report(final Measure measure) {
		final Frame result;
		if(measure.ordinal == frames.size()) {
			result = new Frame();
		} else {
			result = frames.get(measure.ordinal);
		}
		return result;
	}

	public int size() {
		return frames.size();
	}

	private void error(final String message) {
		messages.error(message);
	}

	public void setLength(final int measureCount) {
		final int currentSize = frames.size();
		assert measureCount == currentSize || measureCount == currentSize - 1;
		if(measureCount < currentSize) {
			frames.remove(measureCount);
		}
	}

	@Override
	public String toString() {
		return "Timeline{" + frames.size() + '}';
	}
}
