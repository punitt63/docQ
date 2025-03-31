package in.docq.health.facility.exception;

public class OPDNotFound extends RuntimeException {
    public OPDNotFound(String id) {
        super(String.format("OPD ID:%s does not Exist", id));
    }
}
