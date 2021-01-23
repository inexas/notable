grammar Music;


// P A R S E R

/**
 * A file contains a list of at least one blocks
 */
score: command* EOF ;

command
	:	title		// Header...
	|	composer
	|	header
	|	part		// Structure...
	|	phrase
	|	staff
	|	tempo		// Substrate annnotations
	|	key
	|	time
	|	pickup		// Pickup measure
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

composer: COMPOSER STRING_LITERAL ;

header: HEADER STRING_LITERAL ;

part: PART STRING_LITERAL ;

phrase: PHRASE STRING_LITERAL ;

staff: STAFF STAFFS ;

tempo: TEMPO ( FRACTION '=' COUNT | STRING_LITERAL ) ;

key: KEY NOTE ;

// todo The Q was a +, I should be able to rewrite it so it works
time: TIME ( FRACTION | COMMON | CUT ) ;

/**
 * Pickup measure, e.g. pickup 1/4 which is one 1/4 notes
 */
pickup: PICKUP FRACTION ;

barline
	:	BAR | DOUBLE_BAR
	|	BEGIN_REPEAT | END_REPEAT | BEGIN_END_RPEAT
	|	THICK_THIN | THIN_THICK
	|	DOTTED
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
	|	rest
	|	ghost
	|	namedChord
	|	chord
	|	tuplet
;

note: NOTE ;

rest: REST ;

ghost: GHOST ;

chord: START_CHORD (octave* note)+ END_NOTE_GROUP ;

tuplet: START_TUPLET (octave* note)+ END_NOTE_GROUP ;

namedChord: NAMED_CHORD ;

// Generates both a tie (same notes) or a slur (differnet notes)
bind: '(' (bind | note)+ ')' ;

// L E X E R

// Whitespace and comments

WS: [ \t\r\n\u000C]+ -> channel(HIDDEN) ;

COMMENT: '/*' .*? '*/' -> channel(HIDDEN) ;

LINE_COMMENT: '//' ~[\r\n]* -> channel(HIDDEN) ;

// Tokens

TITLE: 'title' ;

COMPOSER: 'composer' ;

HEADER: 'header' ;

PART: 'part' ;

PHRASE: 'phrase' ;

STAFF: 'staff' ;

STAFFS: ( 'alto' | 'bass' | 'grand' | 'tenor' | 'treble' ) ;

TEMPO: 'tempo' ;

KEY: 'key' ;

PICKUP: 'pickup' ;

COMMON: 'common' ;

CUT: 'cut' ;

// Before NOTE
// No spaces allowed in [brackets]
NAMED_CHORD:
	'['
	Tonic
	Accidental?
	Mode?
	( [-/+]? '7'? ('add' OneOrMore)? ('sus' OneOrMore)? ('i' OneOrMore)? )
	']' ( Duration Default? )? Articulation?
;

// Before NOTE
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
		(( Duration Default? )? Articulation? ) |
		// Chord names
		( [-/+]? '7'? ('add' OneOrMore)? ('sus' OneOrMore)? ('i' OneOrMore)? )
	)
;

REST: RestName ( Duration Default? )? ;

GHOST: GhostName ( Duration Default? )? ;

TIME: 'time' ;

FRACTION: Count'/'Count ;

BAR: '|' ;
DOUBLE_BAR: '||' ;
BEGIN_REPEAT: '|:' ;
END_REPEAT: ':|' ;
BEGIN_END_RPEAT: ':|:' ;
THIN_THICK: '-|' ;
THICK_THIN: '|-' ;
DOTTED: '::' ;

SLASH: '/' ;

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

fragment OneOrMore: [1-9] [0-9]* ;

fragment Duration: ( '1' | '2' | '4' | '8' | '16' | '32' ) ','* ;

fragment Tonic: [A-G] ;

fragment RestName: [R] ;

fragment GhostName: [X] ;

fragment Accidental: [#bn] ;

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
fragment Mode: [Mmidpyxal] ;

