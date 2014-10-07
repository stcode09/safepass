package objects.safepassbeta;

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
        this.label = label;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getNameOnCard() {
        return nameOnCard;
    }

    public void setNameOnCard(String nameOnCard) {
        this.nameOnCard = nameOnCard;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    @Override
    public String toString() {
        return getLabel() + "<=>" + getCardType() + "<=>" + getNameOnCard() + "<=>" + getNumber() + "<=>"
                + getExpiration() + "<=>" + getSecurityCode();
    }

    @Override
    public boolean equals(Object o) {
        return o.getClass().getSimpleName().equals(this.getClass().getSimpleName()) && this.toString().equals(o.toString());
    }

}

