/*
 * Copyright (C) 2019 Inexas. All rights reserved
 */

package org.inexas.notable.notation.model;

import java.util.*;

@SuppressWarnings("unused")
public class Pitch {
	public static final Map<String, Pitch> map = new HashMap<>();

	private static int ix = 0;
	public static final Pitch G9 = new Pitch(ix++, "G9", 12543.8539514160);
	public static final Pitch Fs9 = new Pitch(ix++, "Fs9", 11839.8215267723);
	public static final Pitch F9 = new Pitch(ix++, "F9", 11175.3034058561);
	public static final Pitch E9 = new Pitch(ix++, "E9", 10548.0818212118);
	public static final Pitch Ds9 = new Pitch(ix++, "Ds9", 9956.0634791066);
	public static final Pitch D9 = new Pitch(ix++, "D9", 9397.2725733570);
	public static final Pitch Cs9 = new Pitch(ix++, "Cs9", 8869.8441912599);
	public static final Pitch C9 = new Pitch(ix++, "C9", 8372.0180896192);
	public static final Pitch B8 = new Pitch(ix++, "B8", 7902.1328346582);
	public static final Pitch As8 = new Pitch(ix++, "As8", 7458.6202347565);
	public static final Pitch A8 = new Pitch(ix++, "A8", 7040.0000000000);
	public static final Pitch Gs8 = new Pitch(ix++, "Gs8", 6644.8751612791);
	public static final Pitch G8 = new Pitch(ix++, "G8", 6271.9269757080);
	public static final Pitch Fs8 = new Pitch(ix++, "Fs8", 5919.9107633862);
	public static final Pitch F8 = new Pitch(ix++, "F8", 5587.6517029281);
	public static final Pitch E8 = new Pitch(ix++, "E8", 5274.0409106059);
	public static final Pitch Ds8 = new Pitch(ix++, "Ds8", 4978.0317395533);
	public static final Pitch D8 = new Pitch(ix++, "D8", 4698.6362866785);
	public static final Pitch Cs8 = new Pitch(ix++, "Cs8", 4434.9220956300);
	public static final Pitch C8 = new Pitch(ix++, "C8", 4186.0090448096);
	public static final Pitch B7 = new Pitch(ix++, "B7", 3951.0664100490);
	public static final Pitch As7 = new Pitch(ix++, "As7", 3729.3100921447);
	public static final Pitch A7 = new Pitch(ix++, "A7", 3520.0000000000);
	public static final Pitch Gs7 = new Pitch(ix++, "Gs7", 3322.4375806396);
	public static final Pitch G7 = new Pitch(ix++, "G7", 3135.9634878540);
	public static final Pitch Fs7 = new Pitch(ix++, "Fs7", 2959.9553816931);
	public static final Pitch F7 = new Pitch(ix++, "F7", 2793.8258514640);
	public static final Pitch E7 = new Pitch(ix++, "E7", 2637.0204553030);
	public static final Pitch Ds7 = new Pitch(ix++, "Ds7", 2489.0158697766);
	public static final Pitch D7 = new Pitch(ix++, "D7", 2349.3181433393);
	public static final Pitch Cs7 = new Pitch(ix++, "Cs7", 2217.4610478150);
	public static final Pitch C7 = new Pitch(ix++, "C7", 2093.0045224048);
	public static final Pitch B6 = new Pitch(ix++, "B6", 1975.5332050245);
	public static final Pitch As6 = new Pitch(ix++, "As6", 1864.6550460724);
	public static final Pitch A6 = new Pitch(ix++, "A6", 1760.0000000000);
	public static final Pitch Gs6 = new Pitch(ix++, "Gs6", 1661.2187903198);
	public static final Pitch G6 = new Pitch(ix++, "G6", 1567.9817439270);
	public static final Pitch Fs6 = new Pitch(ix++, "Fs6", 1479.9776908465);
	public static final Pitch F6 = new Pitch(ix++, "F6", 1396.9129257320);
	public static final Pitch E6 = new Pitch(ix++, "E6", 1318.5102276515);
	public static final Pitch Ds6 = new Pitch(ix++, "Ds6", 1244.5079348883);
	public static final Pitch D6 = new Pitch(ix++, "D6", 1174.6590716696);
	public static final Pitch Cs6 = new Pitch(ix++, "Cs6", 1108.7305239075);
	public static final Pitch C6 = new Pitch(ix++, "C6", 1046.5022612024);
	public static final Pitch B5 = new Pitch(ix++, "B5", 987.7666025122);
	public static final Pitch As5 = new Pitch(ix++, "As5", 932.3275230362);
	public static final Pitch A5 = new Pitch(ix++, "A5", 880.0000000000);
	public static final Pitch Gs5 = new Pitch(ix++, "Gs5", 830.6093951599);
	public static final Pitch G5 = new Pitch(ix++, "G5", 783.9908719635);
	public static final Pitch Fs5 = new Pitch(ix++, "Fs5", 739.9888454233);
	public static final Pitch F5 = new Pitch(ix++, "F5", 698.4564628660);
	public static final Pitch E5 = new Pitch(ix++, "E5", 659.2551138257);
	public static final Pitch Ds5 = new Pitch(ix++, "Ds5", 622.2539674442);
	public static final Pitch D5 = new Pitch(ix++, "D5", 587.3295358348);
	public static final Pitch Cs5 = new Pitch(ix++, "Cs5", 554.3652619537);
	public static final Pitch C5 = new Pitch(ix++, "C5", 523.2511306012);
	public static final Pitch B4 = new Pitch(ix++, "B4", 493.8833012561);
	public static final Pitch As4 = new Pitch(ix++, "As4", 466.1637615181);
	public static final Pitch A4 = new Pitch(ix++, "A4", 440.0000000000);
	public static final Pitch Gs4 = new Pitch(ix++, "Gs4", 415.3046975799);
	public static final Pitch G4 = new Pitch(ix++, "G4", 391.9954359817);
	public static final Pitch Fs4 = new Pitch(ix++, "Fs4", 369.9944227116);
	public static final Pitch F4 = new Pitch(ix++, "F4", 349.2282314330);
	public static final Pitch E4 = new Pitch(ix++, "E4", 329.6275569129);
	public static final Pitch Ds4 = new Pitch(ix++, "Ds4", 311.1269837221);
	public static final Pitch D4 = new Pitch(ix++, "D4", 293.6647679174);
	public static final Pitch Cs4 = new Pitch(ix++, "Cs4", 277.1826309769);
	public static final Pitch C4 = new Pitch(ix++, "C4", 261.6255653006);
	public static final Pitch B3 = new Pitch(ix++, "B3", 246.9416506281);
	public static final Pitch As3 = new Pitch(ix++, "As3", 233.0818807590);
	public static final Pitch A3 = new Pitch(ix++, "A3", 220.0000000000);
	public static final Pitch Gs3 = new Pitch(ix++, "Gs3", 207.6523487900);
	public static final Pitch G3 = new Pitch(ix++, "G3", 195.9977179909);
	public static final Pitch Fs3 = new Pitch(ix++, "Fs3", 184.9972113558);
	public static final Pitch F3 = new Pitch(ix++, "F3", 174.6141157165);
	public static final Pitch E3 = new Pitch(ix++, "E3", 164.8137784564);
	public static final Pitch Ds3 = new Pitch(ix++, "Ds3", 155.5634918610);
	public static final Pitch D3 = new Pitch(ix++, "D3", 146.8323839587);
	public static final Pitch Cs3 = new Pitch(ix++, "Cs3", 138.5913154884);
	public static final Pitch C3 = new Pitch(ix++, "C3", 130.8127826503);
	public static final Pitch B2 = new Pitch(ix++, "B2", 123.4708253140);
	public static final Pitch As2 = new Pitch(ix++, "As2", 116.5409403795);
	public static final Pitch A2 = new Pitch(ix++, "A2", 110.0000000000);
	public static final Pitch Gs2 = new Pitch(ix++, "Gs2", 103.8261743950);
	public static final Pitch G2 = new Pitch(ix++, "G2", 97.9988589954);
	public static final Pitch Fs2 = new Pitch(ix++, "Fs2", 92.4986056779);
	public static final Pitch F2 = new Pitch(ix++, "F2", 87.3070578583);
	public static final Pitch E2 = new Pitch(ix++, "E2", 82.4068892282);
	public static final Pitch Ds2 = new Pitch(ix++, "Ds2", 77.7817459305);
	public static final Pitch D2 = new Pitch(ix++, "D2", 73.4161919794);
	public static final Pitch Cs2 = new Pitch(ix++, "Cs2", 69.2956577442);
	public static final Pitch C2 = new Pitch(ix++, "C2", 65.4063913251);
	public static final Pitch B1 = new Pitch(ix++, "B1", 61.7354126570);
	public static final Pitch As1 = new Pitch(ix++, "As1", 58.2704701898);
	public static final Pitch A1 = new Pitch(ix++, "A1", 55.0000000000);
	public static final Pitch Gs1 = new Pitch(ix++, "Gs1", 51.9130871975);
	public static final Pitch G1 = new Pitch(ix++, "G1", 48.9994294977);
	public static final Pitch Fs1 = new Pitch(ix++, "Fs1", 46.2493028390);
	public static final Pitch F1 = new Pitch(ix++, "F1", 43.6535289291);
	public static final Pitch E1 = new Pitch(ix++, "E1", 41.2034446141);
	public static final Pitch Ds1 = new Pitch(ix++, "Ds1", 38.8908729653);
	public static final Pitch D1 = new Pitch(ix++, "D1", 36.7080959897);
	public static final Pitch Cs1 = new Pitch(ix++, "Cs1", 34.6478288721);
	public static final Pitch C1 = new Pitch(ix++, "C1", 32.7031956626);
	public static final Pitch B0 = new Pitch(ix++, "B0", 30.8677063285);
	public static final Pitch As0 = new Pitch(ix++, "As0", 29.1352350949);
	public static final Pitch A0 = new Pitch(ix++, "A0", 27.5000000000);
	public static final Pitch Gs0 = new Pitch(ix++, "Gs0", 25.9565435987);
	public static final Pitch G0 = new Pitch(ix++, "G0", 24.4997147489);
	public static final Pitch Fs0 = new Pitch(ix++, "Fs0", 23.1246514195);
	public static final Pitch F0 = new Pitch(ix++, "F0", 21.8267644646);
	public static final Pitch E0 = new Pitch(ix++, "E0", 20.6017223071);
	public static final Pitch Ds0 = new Pitch(ix++, "Ds0", 19.4454364826);
	public static final Pitch D0 = new Pitch(ix++, "D0", 18.3540479948);
	public static final Pitch Cs0 = new Pitch(ix++, "Cs0", 17.3239144361);
	public static final Pitch C0 = new Pitch(ix++, "C0", 16.3515978313);
	public static final Pitch REST = new Pitch(ix++, "REST", 0);

	public final int index;
	public final String name;
	public final double frequency;

	public Pitch(final int ix, final String name, final double frequency) {
		this.index = ix;
		this.name = name;
		this.frequency = frequency;
		if(map.put(name, this) != null) {
			throw new RuntimeException("Duplicate Pitch: " + this);
		}
	}
}
