package objects.safepassbeta;

// Entry factory, returns a certain entry object
public class EntryFactory {

    public static Entry getEntry(String entry_type) {
        if(entry_type == null) {
            return null;
        } else if(entry_type.equalsIgnoreCase("LOGIN")) {
            return new LoginEntry();
        } else if(entry_type.equalsIgnoreCase("WALLET")) {
            return new WalletEntry();
        } else if(entry_type.equalsIgnoreCase("NOTE")) {
            return new NoteEntry();
        }
        return null;
    }
}
