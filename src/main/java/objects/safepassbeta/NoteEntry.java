package objects.safepassbeta;

public class NoteEntry implements Entry {

    private String title;
    private String note;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return getTitle() + "<=>" + getNote();
    }

    @Override
    public boolean equals(Object o) {
        return o.getClass().getSimpleName().equals(this.getClass().getSimpleName()) && this.toString().equals(o.toString());
    }

}
