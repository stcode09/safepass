package objects.safepassbeta;

import utilities.safepassbeta.Utility;

public class WalletEntry implements Entry {

    private String label;
    private String cardType;
    private String nameOnCard;
    private String number;
    private String expiration;
    private String securityCode;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = Utility.unStrip(label);
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = Utility.unStrip(cardType);
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = Utility.unStrip(nameOnCard);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = Utility.unStrip(number);
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = Utility.unStrip(expiration);
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = Utility.unStrip(securityCode);
    }

    @Override
    public String toString() {
        return Utility.strip(getLabel()) + "<=>" + Utility.strip(getCardType()) + "<=>" + Utility.strip(getNameOnCard()) + "<=>" + Utility.strip(getNumber()) + "<=>"
                + Utility.strip(getExpiration()) + "<=>" + Utility.strip(getSecurityCode());
    }

    @Override
    public boolean equals(Object o) {
        return o.getClass().getSimpleName().equals(this.getClass().getSimpleName()) && this.toString().equals(o.toString());
    }

}

