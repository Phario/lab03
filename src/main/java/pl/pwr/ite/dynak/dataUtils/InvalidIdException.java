package pl.pwr.ite.dynak.dataUtils;

public class InvalidIdException extends Exception {
    public InvalidIdException() {
        super("An object with this ID doesn't exit!");
    }
}
