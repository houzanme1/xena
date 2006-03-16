package au.gov.naa.digipres.xena.kernel.type;

/**
 * A type to represent unknown binary file types. In real life it probably means
 * we don't know what type of file it is.
 *
 * @author Chris Bitmead
 */
public class BinaryFileType extends FileType {
	public String getName() {
		return "Binary";
	}

	public String getMimeType() {
		return "application/octet-stream";
	}
}
