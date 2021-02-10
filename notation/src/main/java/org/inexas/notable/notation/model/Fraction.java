package org.inexas.notable.notation.model;

public class Fraction {
	public final int numerator;
	public final int denominator;
	public final int clicks;

	public Fraction(final int numerator, final int denominator) {
		this.numerator = numerator;
		this.denominator = denominator;
		clicks = numerator * (32 / denominator);
	}

	public Fraction(final String numberSlashNumber) {
		try {
			final int slash = numberSlashNumber.indexOf('/');
			numerator = Integer.parseInt(numberSlashNumber.substring(0, slash));
			denominator = Integer.parseInt(numberSlashNumber.substring(slash + 1));
			clicks = numerator * (32 / denominator);
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
		final boolean result;

		if(this == object) {
			result = true;
		} else if(object == null || getClass() != object.getClass()) {
			result = false;
		} else {
			final Fraction fraction = (Fraction) object;
			result =
					numerator == fraction.numerator &&
							denominator == fraction.denominator;
		}
		return result;
	}

	public String isValid() {
		final String result;

		//   100000000 // number
		// & 011111111 // number - 1
		//   ---------
		//   000000000
		if(denominator < 2
				|| denominator > 32
				|| (denominator & (denominator - 1)) == 0) {
			result = "Invalid denominator: " + toString()
					+ ", expected 2, 4, 8, 16, or 32";
		} else {
			result = null;
		}

		return result;
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
