package org.inexas.notable.notation.model;

public class Fraction {
	public final int numerator;
	public final int denominator;

	public Fraction(final int numerator, final int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
	}

	public Fraction(final String numberSlashNumber) {
		try {
			final int slash = numberSlashNumber.indexOf('/');
			numerator = Integer.parseInt(numberSlashNumber.substring(0, slash));
			denominator = Integer.parseInt(numberSlashNumber.substring(slash + 1));
		} catch(final Exception e) {
			throw new RuntimeException("Invalid fraction: " + numberSlashNumber, e);
		}
	}

	@Override
	public int hashCode() {
		return denominator * 32 + numerator;
	}

	@Override
	public boolean equals(final Object object) {
		final boolean returnValue;

		if(this == object) {
			returnValue = true;
		} else if(object == null || getClass() != object.getClass()) {
			returnValue = false;
		} else {
			final Fraction fraction = (Fraction) object;
			returnValue =
					numerator == fraction.numerator &&
							denominator == fraction.denominator;
		}
		return returnValue;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		toString(sb);
		return sb.toString();
	}

	public void toString(final StringBuilder sb) {
		sb.append(numerator).append('/').append(denominator);
	}
}
