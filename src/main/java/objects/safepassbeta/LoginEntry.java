package objects.safepassbeta;

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
        this.label = label;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWebsite() {
        return (website.equals("")) ? "nUlL" : website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getComments() {
        return (comments.equals("")) ? "nUlL" : comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return getLabel() + "<=>" + getUsername() + "<=>" + getPassword() + "<=>" + getWebsite() + "<=>" + getComments();
    }
}
