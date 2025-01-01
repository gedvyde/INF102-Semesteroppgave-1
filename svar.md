# Runtime Analysis
For each method of the tasks give a runtime analysis in Big-O notation and a description of why it has this runtime.

**If you have implemented new methods not listed you must add these as well, e.g. any helper methods. You need to show how you analyzed any methods used by the methods listed below.**

The runtime should be expressed using these three parameters:
   * `n` - number of words in the list allWords
   * `m` - number of words in the list possibleWords
   * `k` - number of letters in the wordleWords


## Task 1 - matchWord
* `WordleAnswer::matchWord`: O(k)

        int wordLength = answer.length(); 
    
    ##### O(1) :  Hente ut lengden av en streng krever maksimalt O(1) kjøretid

        if ( guess.length() != wordLength)
                throw new IllegalArgumentException("Guess and answer must have same number of letters but guess = " + guess
                        + " and answer = " + answer);

    ##### O(1) : Betingelsen og kastingen av exception har konstant kjøretid

        HashMap<Character,Integer> letters = makeMap(answer);
    
    ##### O(k) : Oppretter et hashmap letters tar O(k) kjøretid, se hjelpefunksjon sin kjøretids analyse

        AnswerType[] feedback = new AnswerType[wordLength];

    ##### O(k) : Oppretter et nytt array av typen AnswerType med worldLenght som innput for lengden k. 

        for (int i = 0; i < wordLength; i++) {      // O(k)
            char correct = answer.charAt(i);        //k times O(1)
            char guessed = guess.charAt(i);         //k times O(1)

            if (correct == guessed) {
                feedback[i] = AnswerType.CORRECT;   // k times O(1)
                letters.put(guessed, letters.get(guessed -1)); // k times (O(1)+O(1)) = k times O(1)   
            } else {
                feedback[i] = AnswerType.WRONG; 
            }
        }

    ##### O(k) : Funksjonen charAt() henter ut char på gitt index og har kjøretid O(1). De HashMap oprerasjonene som blir brukt i koden er put og get. I worst-case har disse operasjonene kjøretiden O(k) i tilfeller hvor med mange nøkler kolliderer. Siden vi bruker default HashMap funksjonen til java med Characters som nøkler tar vi utgangspunktet til at HashMaped i gjennomsnitt har en O(1) kjøretid. Å sette verdien til arrayen feedback ved en gitt index i tar konstant kjøretid. Alle operasjonen i for-løkken har konstand kjøretid, siden løkken iterere k times, vil total kjøretid være k times O(1) = O(k)


        for (int i = 0; i < wordLength; i++) {       // O(k)
            char guessed = guess.charAt(i);          //k times O(1)
            if (feedback[i] == AnswerType.CORRECT) { //k times O(1)
                continue;
            }
            if (letters.containsKey(guessed)) {         //k times O(1)
                Integer count = letters.get(guessed);   //k times O(1)
                if (count > 0) {
                    letters.put(guessed, count-1);      //k times O(1)
                    feedback[i] = AnswerType.MISPLACED; //k times O(1)
                }
            }
        }

        return new WordleWord(guess,feedback);

    ##### O(k) : Samme kjøretid analysen gjelder denne for løkken også. Alle operasjonen for både HashMapet og Arrayen har konstant kjøretid. Dette gjøres k times, totale kjøretiden for for-løkken er dermed O(k).


        private static HashMap<Character,Integer> makeMap(String answer) { // O(k)
            HashMap<Character,Integer> letters = new HashMap<>();          // 0(1)
            for (Character letter : answer.toCharArray()) {
                if (!letters.containsKey(letter)) {            //k times O(1)
                    letters.put(letter, 0);                    //k times O(1)
                }
                letters.put(letter, letters.get(letter)+1); //k times (O(1)+O(1)) = k times O(1)
            }

        return letters; 
        }
     
    ##### O(k) : Hjelpefunksjonen makeMap har kjøretid O(k). Igjen gjelder samme kjøretidanalysen da vi tar i bruk de samme funksjonen til HashMap (put & get) som begge har konstant kjøretid. For løkken itererer k times, kjøretiden blir da k times O(1) som tilsvarer kjøretiden O(k)

    #### For å beregne den totale kjøretiden til funksjonen matchWord, ser vi vekk fra konstander og beholder kun det største leddet. Etter å ha utført kjøretidsanalyse på de ulike delene av matchWord koden ser vi at det størte kjøretiden er O(k), dermed har matchWord tilsvarende kjøretid.

## Task 2 - EliminateStrategy
* `WordleWordList::eliminateWords`: O(k*m)

        public void eliminateWords(WordleWord feedback) { // O(k*m)

        List<String> newPossibleAnswers = new ArrayList<>();
    
    ##### O(1) : Å opprette en ArrayList tar i gjennomsnitt O(1) kjøretid.
		
		for (String word : possibleAnswers) {  // O(k*m) 
			if (WordleWord.isPossibleWord(word, feedback)) { // m times O(k)
				newPossibleAnswers.add(word); // m times O(1)
			}
		}

		this.possibleAnswers = newPossibleAnswers; // O(1)
        }

    ##### O(k*m) : For løkken itererer over alle ordene i possibleAnswers, dette tilsvarer m antall iterasjoner. For hver av disse ordene blir funksjonen isPossibleWord kalt. Denne funksjonen har O(k) kjøretid, hvor k er lengden på wordleWord. Worst case tilfredstiller alle worldeWord ordene betingelsen og blir lagt til i listen newPossibleAnswers. I gjennomsnitt (amortized) tar det O(1) kjøretid å legge til element til en Arraylist. Til slutt å overskrive en feltvariabel tar O(1) kjøretid. 

        public static boolean isPossibleWord(String word, WordleWord feedback) { // O(k)
        WordleWord otherFeedback = WordleAnswer.matchWord(feedback.getWordString(), word);  // O(k) + O(k) = 2*O(k) = O(k)
        return otherFeedback.equals(feedback);
        }

    ##### O(k): Funksjonen isPossibleWord bruker funksjonen matchWord som vi ved ufra kjøretidsanalysen i Task1 har O(k) kjøretid. For å få feedback i String format, kalles getWordString funksjonen som har kjøre tid O(k), se kjøretidsanalyse under.
 
        public String getWordString() {          // O(k)
        StringBuilder sb = new StringBuilder(); // O(1)
    
    ##### O(1): Å opprette en Stringbuilder tar konstant O(1) kjøretid.
    
        for (WordleCharacter wordleChar : word) { 
            sb.append(wordleChar.letter); // k times O(1) + O(1) = k times O(1)
        
    ##### O(k): For løkken går gjennom alle WorldeCharacter objektene i word, siden word har lengden k iterer løkken k times. I hver iterasjon kalles append funksjonen på StringBuilder. I gjennomsnitt tar dette O(1) kjøretid. Når StringBuilder er "full" må den kopiere innholde og lage mer plass (ligner på Arraylist), dette skjer skjeldent derfor sier vi at kjøretiden er O(1). Det som blir lagt til StringBuildere er letter til WorldeCharcter, dette er en public feltvariabel som tar O(1) tid å hente ut. 

        return sb.toString(); // O(k)
        }

    ##### O(k): Å returnere en String fra et StringBuilder objekt krever kall til toString() funksjonen som har O(k) kjøretid, hvor k er lengden på StringBuilderen. Når vi konverterer til String opprettes et String objekt og kopieres fra StringBuilder.

    ##### O(k): Totalt har funksjonen getWordString() en kjøretid på O(k), hvor k er lengden på wordleWord.

    #### O(k*m) : Likt task 1, må vi se på det størte leddet for å beregne kjøretiden til eliminateWord(). Vi ser utfra kjøretidsanalysen at dette tilsvarer O(k *m)


    

## Task 3 - FrequencyStrategy
* `FrequencyStrategy::makeGuess`: O(m*k)
    
        public String makeGuess(WordleWord feedback) {
            if (feedback != null) {  // 0(1)
                guesses.eliminateWords(feedback); // O(k*m)
            }

    ##### O(1) : Fra task 2 vet vi at funksjonen eliminateWord har kjøretiden O(k*m)
        
        frequencyMapIndex = guesses.getListOfFrequencyMaps(true); // O(m*k)
        String bestWord = new String(); // 0(1)
        int bestScore = 0; // 0(1)

    ##### O(k*m): Se kjøretidsanalyse av hjelpe funksjon getListOfFrequencyMaps lenger nede. Konstant kjøretid på opprettelsen på de to nye variablene.

        for (String word : guesses.possibleAnswers()) { // m times
            int wordScore = 0; // m times 0(1)
            for (int i = 0; i < word.length(); i++)  { // m*k times
                char letter = word.charAt(i);  // m*k times 0(1)
                wordScore += frequencyMapIndex.get(i).get(letter); // m*k times 0(1) + O(1)
            }
            if (wordScore > bestScore) { // m times O(1)
                bestWord = word;         // m times O(1)
                bestScore = wordScore;   // m times O(1)
            }
        }
        return bestWord;
        }

    ##### O(m * k): Nøstet for løkke repeterer alle operasjonen k*m times men siden alle operasjonene og metoden som bruker har konstant kjøretid blir kjøretiden O(k * m) 

        public List<HashMap<Character,Integer>> getListOfFrequencyMaps(boolean countByIndex) {
		List<HashMap<Character, Integer>> frequencyMaps = new ArrayList<>(5); // O(1)
		for (int i = 0; i < wordLength(); i++) { // O(k)
				frequencyMaps.add(new HashMap<>()); // k times O(1);
		}

		for (String word : this.possibleAnswers()) { // O(m*k)
			for (int i = 0; i < word.length(); i++)  { // m times O(k)
					char letter = word.charAt(i);   // m*k times O(1)
					int mapIndex = countByIndex ? i : 0; // m*k times O(1)
					frequencyMaps.get(mapIndex).put(letter, frequencyMaps.get(mapIndex).getOrDefault(letter,0)+1); // m*k times O(1)
					} 
			}
		
		if (!countByIndex) {  // O(1)
			HashMap<Character,Integer> frequencyMap = frequencyMaps.get(0); // O(1)
			frequencyMaps = new ArrayList<>(); // O(1)
			for (int i = 0; i < wordLength(); i++) { O(k)
				frequencyMaps.add(frequencyMap); // k times O(1)
			}
		}
		return frequencyMaps;
	} 

    ##### O(k*m): Lik logikk som forige punkt. Nøstede forløkker med metoder og operasjoner med konstant kjøretid. Selv om metoden getListOfFrequencyMaps har flere løkker etter hverandre er det igjen kun det størte leddet som teller. Metoden har derfor en total kjøretid på O(k * m)

    #### O(k*m) : Vi ser på det størte leddet for å beregne kjøretiden til makeGuess.  Å lage frequency mappet tar O(m * k) og nøstede løkken tar O(m * k) tid. Kjøretiden til makeGuess funksjonen blir altså O(m * k).

# Task 4 - Make your own (better) AI
For this task you do not need to give a runtime analysis. 
Instead, you must explain your code. What was your idea for getting a better result? What is your strategy?

#### Min ide for MyStrategy var å benytte seg av to ulike strategier avhengig av antall mulige ord som er igjen i guesses WordleWordListen. Hvor stort antallet mulige ord skal være (kaller konstanten size) før algoritmen bytter stretegi er litt forskjellig utfra hvordan du vurderer algoritmen. Mer om dette senere. Har tatt noe utganspunkt i frequenceStrategy, for å rangere ord/bokstaver. Også egentlig bare prøvd meg frem. Tanken var å opptimalisere hvert gjett med blandt annet forskjellig bokstaver og ikke bruke opp "plass" med bokstaver som tidligere er brukt. 

Programflyt felles for begge strategiene:

    Så fremt feedback ikke er tom (som kun hender ved første gjett). Så brukes eliminateWord funksjonen for å fjerne umulige ord fra guesses basert på feedbacken. Her oppdateres også HashSet guessedChar, med bokstavene som enten var riktig (grønn) eller wrong (grå).

Så lenge antall mulige ord i guesses er større enn size gjør algoritmen strategi 1:

    Den lager en liste av frequencyMaps utfra ordnene som ikke har blitt eliminert enda. Disse frekvensene er telt indeks vis, det vil si at hver map i på en bestemt index i listen, representere frekvensen bokstaver på tilsvarende indeks. I Strategi 1 bruker heller ikke guessedChar, altså bokstaver som blitt gjettet grønn eller grå tidligere. På denne måten bruker jeg ikke opp plass på bokstaver som jeg allerede vet er riktig eller feil.

Når antall mulige ord er mindre enn size gjør algoritment strategi 2:

    Her gjetter den ord blandt de mulige ordene og rangerer baser på frekvensen av bokstavene. Frequency mappet som blir bruk her er ikke baser på index. Og funksjonen kan bruke bokstaver som har blit grønn tidligere.

 Selve getBestWord funksjonen er lik for begge strategiene. Hvordan selve funksjonen opererer står godt forklart i java doc.


##### For å finne hvilken size jeg skal redusere mulige ord til (guesses) før algoritment bytter strategi har jeg lagd en test. Denne tester alle verdiene til size i intervallet 3-200 for best resultat. Ved å velge en høy size verdi, vil ikke like mange ord bli eliminert med første strategi og algoritmen vil begynne å gjette ord ganske tidlig. Ved en slik offensiv strategi får algoritmen en lav avarage, for den i mange av tilfelle klarer å gjette ordet mye tidligere men med mindre informasjon. Når en øker antall games eller fjerner seed blir dette en strategi som enten gjetter ordet veldig tidlig eller ikke i det hele tatt/veldig sent. Også kan en ha en veldig lav size verdi, dette gjør at den bruker flere gjett i starten for å eliminere mest mulig ord før den begynner å gjette. På "enkle" ord bruker algoritmen gjerne litt lengre tid enn nødvendig, men når den først begynner å gjette vinner den så godt som alltid, fordi det bare er et lavt antall ord igjen å velge mellom. Så avaragen øker, men spill vunnet øker og max guess reduseres. 

#### test navn : testBestSizePossibleWordsBeforeStrategySwitch()  lurer på hva den tester hehe :)