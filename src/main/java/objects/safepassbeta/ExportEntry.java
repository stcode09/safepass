package objects.safepassbeta;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExportEntry implements Serializable {

    private static final long serialVersionUID = 5978237501745252522L;
    private List<String> l1 = new ArrayList<String>();
    private List<String> w1 = new ArrayList<String>();
    private List<String> n1 = new ArrayList<String>();
    private String t1;

    public void setLoginItem(String str) {
        l1.add(str);
    }

    public void setWalletItem(String str) {
        w1.add(str);
    }

    public void setNoteItem(String str) {
        n1.add(str);
    }

    public String getT1() {
        return t1;
    }

    public void setT1(String t1) {
        this.t1 = t1;
    }

    public List<String> getN1() {
        return n1;
    }

    public void setN1(List<String> n1) {
        this.n1 = n1;
    }

    public List<String> getL1() {
        return l1;
    }

    public void setL1(List<String> l1) {
        this.l1 = l1;
    }

    public List<String> getW1() {
        return w1;
    }

    public void setW1(List<String> w1) {
        this.w1 = w1;
    }

}
