grammar Music;

// P A R S E R

// o Octave
// f, mf, f, sfz Dynamics
//
// +, - octave
// ~1 fingering
// | Barlines
//
// [CEG] chord
// [t ...] Tuplet
//
// Available
// /
/**
 * A file contains a list of at least one blocks
 */
score: command* EOF ;

command
	:	title		// Header...
	|	subtitle
	|	composer
	|	header
	|	copyright
	|	part		// Structure...
	|	phrase
	|	clef
	|	tempo		// Substrate annnotations
	|	key
	|	time
	|	cpm 		// Clicks Per Measure
	|	barline
	|	line
	|	octave		// Event annotations
	|	dynamic
	|	text
	|	fingering
	|	event		// Event stream
	|	bind
;

title: TITLE STRING_LITERAL ;

subtitle: SUBTITLE STRING_LITERAL ;

composer: COMPOSER STRING_LITERAL ;

// todo This needs to be wiki markup
header: HEADER STRING_LITERAL ;

copyright: COPYRIGHT STRING_LITERAL ;

part: PART STRING_LITERAL ;

phrase: PHRASE STRING_LITERAL ;

clef: CLEF CLEFS ;

tempo: TEMPO ( FRACTION '=' COUNT | STRING_LITERAL ) ;

key: KEY NOTE ;

time: TIME ( FRACTION | COMMON | CUT ) ;

/**
 * Clicks Per Measure, e.g. cpm 16
 */
cpm: CPM COUNT ;

barline
	:	BAR | DOUBLE_BAR
	|	BEGIN_REPEAT | END_REPEAT | BEGIN_END_REPEAT
	|	FINAL
;

line: LINE ;

octave
	:	OCTAVE COUNT
	|	OCTAVE_UP
	|	OCTAVE_DOWN
;

dynamic: DYNAMIC ;

text: STRING_LITERAL ;

fingering: FINGERING ;

event
	:	note
	|	namedChord
	|	chord
	|	tuplet
;

note: NOTE ;

chord: START_CHORD (octave* note)+ END_NOTE_GROUP ;

tuplet: START_TUPLET (octave* note)+ END_NOTE_GROUP ;

namedChord: NAMED_CHORD ;

// Generates both a tie (same notes) or a slur (different notes)
bind: BEAM (bind | note)+ MAEB ;

// L E X E R

// Whitespace and comments

WS: [ \t\r\n\u000C]+ -> channel(HIDDEN) ;

COMMENT: '/*' .*? '*/' -> channel(HIDDEN) ;

LINE_COMMENT: '//' ~[\r\n]* -> channel(HIDDEN) ;

// Tokens

TITLE: 'title' ;

SUBTITLE: 'subtitle' ;

COMPOSER: 'composer' ;

HEADER: 'header' ;

COPYRIGHT: 'copyright' ;

PART: 'part' ;

PHRASE: 'phrase' ;

CLEF: 'clef' ;

CLEFS: ('treble15a'|'treble8a'|'treble'|'treble8b'|'treble15b'|
        'bass15a'|'bass8a'|'bass'|'bass8b'|'bass15b'|
        'soprano'|'mezzoSoprano'|'alto'|'tenor'|'baritone'|
        'tab'|
        'percussion') ;

TEMPO: 'tempo' ;

KEY: 'key' ;

CPM: 'cpm' ;

COMMON: 'common' ;

CUT: 'cut' ;

BEAM: '(' ;

MAEB: ')' ;

// Must be before NOTE
// No spaces allowed in [brackets]
NAMED_CHORD:
	'['
	Tonic
	Accidental?
	Mode?
	( [-/+]? '7'? ('add' OneOrMore)? ('sus' OneOrMore)? ('i' OneOrMore)? )
	']' Articulation? ( Duration Default? )?
;

// Must be before NOTE
LINE: '{'
	(	('bind' | 'b')
	|	('crescendo' | 'cre' | 'c')
	|	('decrescendo' | 'dec' | 'd')
	|	(('octave' | 'oct' | 'o') '-'? OneOrMore)
	|	('pedal' | 'ped' | 'p')
	|	('rest' | 'r')
	|	(('volta' | 'vol' | 'v') '-'? OneOrMore)
	)
	[ \t]+
	Count ('.' OneOrMore)? // Number of events
	'}'
;

NOTE:
	Tonic
	Accidental?
	Mode? (
		// Note events
		( Articulation? ( Duration Default? )? ) |
		// Chord names
		( [-/+]? '7'? ('add' OneOrMore)? ('sus' OneOrMore)? ('i' OneOrMore)? )
	)
;

TIME: 'time' ;

FRACTION: Count'/'Count ;

BAR: '|' ;
DOUBLE_BAR: '||' ;
BEGIN_REPEAT: '|:' ;
END_REPEAT: ':|' ;
BEGIN_END_REPEAT: ':|:' ;
FINAL: '|||' ;
REPEAT_FINAL: ':|||' ;

OCTAVE: 'o' ;

OCTAVE_UP: '+'+ ;

OCTAVE_DOWN: '-'+ ;

START_TUPLET: '[t' ;

START_CHORD: '[' ;

END_NOTE_GROUP: ']' ( Duration Default? )? Articulation? ;

DYNAMIC
	:	'f'+		// forte, fortissimo, ...
	|	'p'+		// piano, pianissimo, ...
	|	'm'[fp]		// mezzoforte/piano
	|	'sfz'		// sforzando
;

FINGERING: '~' [0-5] ;

// Literals

// This allows multi line strings for wiki markup
STRING_LITERAL: '"' (~["\\] | EscapeSequence )* '"' ;

COUNT: Count ;

ONE : '1' ;

fragment EscapeSequence
	:	'\\' [btnfr"'\\]
	|	'\\' ([0-3]? [0-7])? [0-7]
	|	'\\' 'u'+ HexDigit HexDigit HexDigit HexDigit
;

fragment HexDigits:	HexDigit ((HexDigit | '_')* HexDigit)? ;

fragment HexDigit:	[0-9a-fA-F] ;

fragment Count:	('0' | OneOrMore ) ;

fragment OneOrMore: [1-9][0-9]* ;

fragment Duration: ( '1' | '2' | '4' | '8' | '16' | '32' ) '.'* ;

fragment Tonic: [A-GR] ;

fragment Accidental: ( 'b' | 'bb' | '#' | '##' | 'n' ) ;

fragment Articulation: [._!fg]+;

fragment Default: '*' ;

/*
 * Modes
 * C 'M', 'i', '': Major or Ionion
 * D 'd': Dorian
 * E 'p': Phrygian
 * F 'y': Lydian
 * G 'x': Mixolyian
 * A 'm', 'a': Aeolian
 * B 'l': Locrian
 */
fragment Mode: [Mm] ;
