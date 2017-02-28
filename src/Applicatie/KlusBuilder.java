package Applicatie;

public class KlusBuilder {
    private String d;
    private String s;
    private int kID;
    private int aID;
    private String w;
    private String wN;
    private int i;
    private int i2;

    public KlusBuilder setD(String d) {
        this.d = d;
        return this;
    }

    public KlusBuilder setS(String s) {
        this.s = s;
        return this;
    }

    public KlusBuilder setkID(int kID) {
        this.kID = kID;
        return this;
    }

    public KlusBuilder setaID(int aID) {
        this.aID = aID;
        return this;
    }

    public KlusBuilder setW(String w) {
        this.w = w;
        return this;
    }

    public KlusBuilder setwN(String wN) {
        this.wN = wN;
        return this;
    }

    public KlusBuilder setI(int i) {
        this.i = i;
        return this;
    }

    public KlusBuilder setI2(int i2) {
        this.i2 = i2;
        return this;
    }

    public Klus createKlus() {
        return new Klus(d, s, kID, aID, w, wN, i, i2);
    }
}