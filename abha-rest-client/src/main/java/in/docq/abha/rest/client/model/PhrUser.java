package in.docq.abha.rest.client.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Lightweight model representing a PHR user item from PHR login APIs.
 */
public class PhrUser {

    @SerializedName("abhaAddress")
    private String abhaAddress;

    @SerializedName("abhaNumber")
    private String abhaNumber;

    @SerializedName("fullName")
    private String fullName;

    @SerializedName("kycStatus")
    private String kycStatus;

    @SerializedName("status")
    private String status;

    public String getAbhaAddress() { return abhaAddress; }
    public PhrUser abhaAddress(String abhaAddress) { this.abhaAddress = abhaAddress; return this; }

    public String getAbhaNumber() { return abhaNumber; }
    public PhrUser abhaNumber(String abhaNumber) { this.abhaNumber = abhaNumber; return this; }

    public String getFullName() { return fullName; }
    public PhrUser fullName(String fullName) { this.fullName = fullName; return this; }

    public String getKycStatus() { return kycStatus; }
    public PhrUser kycStatus(String kycStatus) { this.kycStatus = kycStatus; return this; }

    public String getStatus() { return status; }
    public PhrUser status(String status) { this.status = status; return this; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhrUser phrUser = (PhrUser) o;
        return Objects.equals(abhaAddress, phrUser.abhaAddress) &&
                Objects.equals(abhaNumber, phrUser.abhaNumber) &&
                Objects.equals(fullName, phrUser.fullName) &&
                Objects.equals(kycStatus, phrUser.kycStatus) &&
                Objects.equals(status, phrUser.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abhaAddress, abhaNumber, fullName, kycStatus, status);
    }

    @Override
    public String toString() {
        return "PhrUser{" +
                "abhaAddress='" + abhaAddress + '\'' +
                ", abhaNumber='" + abhaNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", kycStatus='" + kycStatus + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}


