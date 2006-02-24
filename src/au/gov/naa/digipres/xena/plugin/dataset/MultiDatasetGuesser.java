package au.gov.naa.digipres.xena.plugin.dataset;
import au.gov.naa.digipres.xena.kernel.MultiInputSource;
import au.gov.naa.digipres.xena.kernel.XenaException;
import au.gov.naa.digipres.xena.kernel.XenaInputSource;
import au.gov.naa.digipres.xena.kernel.guesser.Guess;
import au.gov.naa.digipres.xena.kernel.guesser.Guesser;
import au.gov.naa.digipres.xena.kernel.guesser.GuesserManager;
import au.gov.naa.digipres.xena.kernel.normalise.NormaliserManager;
import au.gov.naa.digipres.xena.kernel.type.FileType;
import au.gov.naa.digipres.xena.kernel.type.Type;
import au.gov.naa.digipres.xena.kernel.type.TypeManager;
import au.gov.naa.digipres.xena.kernel.type.XenaFileType;

/**
 * Guesser for a collection of Xena dataset files.
 *
 * @author Chris Bitmead
 */
public class MultiDatasetGuesser extends Guesser {
	
	public Guess guess(XenaInputSource source) throws java.io.IOException, XenaException {
		Guess guess = new Guess((FileType)TypeManager.singleton().lookup(MultiDatasetFileType.class));
		if (source instanceof MultiInputSource) {
            guess.setDataMatch(true);
			MultiInputSource mis = (MultiInputSource)source;
			for (int i = 0; i < mis.size(); i++) {
				Type type = GuesserManager.singleton().mostLikelyType(new XenaInputSource(mis.getSystemId(i), null));
				if (!(type instanceof XenaFileType)) {
					String tag = null;
					try {
						tag = NormaliserManager.singleton().getFirstContentTag(mis.getSystemId(i));
					} catch (XenaException x) {
						// Probably means the data is bogus, not even XML.
						guess.setPossible(false);
						break;
					}
					if (!tag.equals("dataset")) {
						guess.setPossible(false);
						break;
					}
				}
			}
		}
		return guess;
	}
    
    public String getName() {
        return "MultiDatasetGuesser";
    }
    
	@Override
	protected Guess createBestPossibleGuess()
	{
		Guess guess = new Guess();
		guess.setDataMatch(true);
		return guess;
	}
    
}
