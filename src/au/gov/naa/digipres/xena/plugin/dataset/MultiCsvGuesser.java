package au.gov.naa.digipres.xena.plugin.dataset;
import au.gov.naa.digipres.xena.kernel.MultiInputSource;
import au.gov.naa.digipres.xena.kernel.XenaException;
import au.gov.naa.digipres.xena.kernel.XenaInputSource;
import au.gov.naa.digipres.xena.kernel.guesser.Guess;
import au.gov.naa.digipres.xena.kernel.guesser.GuessPriority;
import au.gov.naa.digipres.xena.kernel.guesser.Guesser;
import au.gov.naa.digipres.xena.kernel.guesser.GuesserManager;
import au.gov.naa.digipres.xena.kernel.type.FileType;
import au.gov.naa.digipres.xena.kernel.type.Type;
import au.gov.naa.digipres.xena.kernel.type.TypeManager;

/**
 * Guesser for guessing that a collection of  files are all CSV files.
 *
 * @author Chris Bitmead
 */
public class MultiCsvGuesser extends Guesser {
	
	public Guess guess(XenaInputSource source) throws java.io.IOException, XenaException {
		Guess guess = new Guess((FileType)TypeManager.singleton().lookup(MultiCsvFileType.class));
		if (source instanceof MultiInputSource) {
            guess.setDataMatch(true);
			MultiInputSource mis = (MultiInputSource)source;
			for (int i = 0; i < mis.size(); i++) {
				Type type = GuesserManager.singleton().mostLikelyType(new XenaInputSource(mis.getSystemId(i), null));
				if (!(type instanceof CsvFileType)) {
					guess.setPossible(true);
                    guess.setPriority(GuessPriority.HIGH);
					break;
				}
			}
		}
		return guess;
	}
    

    public String getName() {
        return "MultiCSVGuesser";
    }
    
	@Override
	protected Guess createBestPossibleGuess()
	{
		Guess guess = new Guess();
		guess.setPossible(true);
		guess.setDataMatch(true);
		guess.setPriority(GuessPriority.HIGH);
		return guess;
	}

}
