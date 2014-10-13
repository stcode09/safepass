package objects.safepassbeta;

import utilities.safepassbeta.Utility;

public class NoteEntry implements Entry {

    private String title;
    private String note;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = Utility.unStrip(title);
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = Utility.unStrip(note);
    }

    @Override
    public String toString() {
        return Utility.strip(getTitle()) + "<=>" + Utility.strip(getNote());
    }

    @Override
    public boolean equals(Object o) {
        return o.getClass().getSimpleName().equals(this.getClass().getSimpleName()) && this.toString().equals(o.toString());
    }

}
