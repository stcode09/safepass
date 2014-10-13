package objects.safepassbeta;

import utilities.safepassbeta.Utility;

public class LoginEntry implements Entry{

    private String label;
    private String username;
    private String password;
    private String website;
    private String comments;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = Utility.unStrip(label);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = Utility.unStrip(username);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = Utility.unStrip(password);
    }

    public String getWebsite() {
        return (website.equals("")) ? "nUlL" : website;
    }

    public void setWebsite(String website) {
        this.website = Utility.unStrip(website);
    }

    public String getComments() {
        return (comments.equals("")) ? "nUlL" : comments;
    }

    public void setComments(String comments) {
        this.comments = Utility.unStrip(comments);
    }

    @Override
    public String toString() {
        return Utility.strip(getLabel()) + "<=>" + Utility.strip(getUsername()) + "<=>" +
                Utility.strip(getPassword()) + "<=>" + Utility.strip(getWebsite()) + "<=>" +
                Utility.strip(getComments());
    }

    @Override
    public boolean equals(Object o) {
        return o.getClass().getSimpleName().equals(this.getClass().getSimpleName()) && this.toString().equals(o.toString());
    }

}
