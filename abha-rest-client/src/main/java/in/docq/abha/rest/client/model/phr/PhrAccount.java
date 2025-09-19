package in.docq.abha.rest.client.model.phr;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Lightweight model representing a PHR account returned by PHR login APIs.
 */
public class PhrAccount {

    @SerializedName("ABHANumber")
    private String abhaNumber;

    @SerializedName("address")
    private String address;

    @SerializedName("authMethods")
    private List<String> authMethods = new ArrayList<>();

    @SerializedName("dayOfBirth")
    private String dayOfBirth;

    @SerializedName("districtCode")
    private String districtCode;

    @SerializedName("districtName")
    private String districtName;

    @SerializedName("email")
    private String email;

    @SerializedName("emailVerified")
    private Boolean emailVerified;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("gender")
    private String gender;

    @SerializedName("kycPhoto")
    private String kycPhoto;

    @SerializedName("kycVerified")
    private Boolean kycVerified;

    @SerializedName("lastName")
    private String lastName;

    @SerializedName("middleName")
    private String middleName;

    @SerializedName("mobile")
    private String mobile;

    @SerializedName("monthOfBirth")
    private String monthOfBirth;

    @SerializedName("name")
    private String name;

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("preferredAbhaAddress")
    private String preferredAbhaAddress;

    @SerializedName("profilePhoto")
    private String profilePhoto;

    @SerializedName("stateCode")
    private String stateCode;

    @SerializedName("stateName")
    private String stateName;

    @SerializedName("status")
    private String status;

    @SerializedName("subDistrictCode")
    private String subDistrictCode;

    @SerializedName("subdistrictName")
    private String subdistrictName;

    @SerializedName("tags")
    private Map<String, Object> tags = new HashMap<>();

    @SerializedName("townCode")
    private String townCode;

    @SerializedName("townName")
    private String townName;

    @SerializedName("verificationStatus")
    private String verificationStatus;

    @SerializedName("verificationType")
    private String verificationType;

    @SerializedName("villageCode")
    private String villageCode;

    @SerializedName("villageName")
    private String villageName;

    @SerializedName("wardCode")
    private String wardCode;

    @SerializedName("wardName")
    private String wardName;

    @SerializedName("yearOfBirth")
    private String yearOfBirth;

    public String getAbhaNumber() {
        return abhaNumber;
    }

    public PhrAccount abhaNumber(String abhaNumber) {
        this.abhaNumber = abhaNumber;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public PhrAccount address(String address) {
        this.address = address;
        return this;
    }

    public List<String> getAuthMethods() {
        return authMethods;
    }

    public PhrAccount authMethods(List<String> authMethods) {
        this.authMethods = authMethods;
        return this;
    }

    public PhrAccount addAuthMethod(String method) {
        if (this.authMethods == null) {
            this.authMethods = new ArrayList<>();
        }
        this.authMethods.add(method);
        return this;
    }

    public String getDayOfBirth() { return dayOfBirth; }
    public PhrAccount dayOfBirth(String dayOfBirth) { this.dayOfBirth = dayOfBirth; return this; }

    public String getDistrictCode() { return districtCode; }
    public PhrAccount districtCode(String districtCode) { this.districtCode = districtCode; return this; }

    public String getDistrictName() { return districtName; }
    public PhrAccount districtName(String districtName) { this.districtName = districtName; return this; }

    public String getEmail() { return email; }
    public PhrAccount email(String email) { this.email = email; return this; }

    public Boolean getEmailVerified() { return emailVerified; }
    public PhrAccount emailVerified(Boolean emailVerified) { this.emailVerified = emailVerified; return this; }

    public String getFirstName() { return firstName; }
    public PhrAccount firstName(String firstName) { this.firstName = firstName; return this; }

    public String getGender() { return gender; }
    public PhrAccount gender(String gender) { this.gender = gender; return this; }

    public String getKycPhoto() { return kycPhoto; }
    public PhrAccount kycPhoto(String kycPhoto) { this.kycPhoto = kycPhoto; return this; }

    public Boolean getKycVerified() { return kycVerified; }
    public PhrAccount kycVerified(Boolean kycVerified) { this.kycVerified = kycVerified; return this; }

    public String getLastName() { return lastName; }
    public PhrAccount lastName(String lastName) { this.lastName = lastName; return this; }

    public String getMiddleName() { return middleName; }
    public PhrAccount middleName(String middleName) { this.middleName = middleName; return this; }

    public String getMobile() { return mobile; }
    public PhrAccount mobile(String mobile) { this.mobile = mobile; return this; }

    public String getMonthOfBirth() { return monthOfBirth; }
    public PhrAccount monthOfBirth(String monthOfBirth) { this.monthOfBirth = monthOfBirth; return this; }

    public String getName() { return name; }
    public PhrAccount name(String name) { this.name = name; return this; }

    public String getPincode() { return pincode; }
    public PhrAccount pincode(String pincode) { this.pincode = pincode; return this; }

    public String getPreferredAbhaAddress() { return preferredAbhaAddress; }
    public PhrAccount preferredAbhaAddress(String preferredAbhaAddress) { this.preferredAbhaAddress = preferredAbhaAddress; return this; }

    public String getProfilePhoto() { return profilePhoto; }
    public PhrAccount profilePhoto(String profilePhoto) { this.profilePhoto = profilePhoto; return this; }

    public String getStateCode() { return stateCode; }
    public PhrAccount stateCode(String stateCode) { this.stateCode = stateCode; return this; }

    public String getStateName() { return stateName; }
    public PhrAccount stateName(String stateName) { this.stateName = stateName; return this; }

    public String getStatus() { return status; }
    public PhrAccount status(String status) { this.status = status; return this; }

    public String getSubDistrictCode() { return subDistrictCode; }
    public PhrAccount subDistrictCode(String subDistrictCode) { this.subDistrictCode = subDistrictCode; return this; }

    public String getSubdistrictName() { return subdistrictName; }
    public PhrAccount subdistrictName(String subdistrictName) { this.subdistrictName = subdistrictName; return this; }

    public Map<String, Object> getTags() { return tags; }
    public PhrAccount tags(Map<String, Object> tags) { this.tags = tags; return this; }

    public String getTownCode() { return townCode; }
    public PhrAccount townCode(String townCode) { this.townCode = townCode; return this; }

    public String getTownName() { return townName; }
    public PhrAccount townName(String townName) { this.townName = townName; return this; }

    public String getVerificationStatus() { return verificationStatus; }
    public PhrAccount verificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; return this; }

    public String getVerificationType() { return verificationType; }
    public PhrAccount verificationType(String verificationType) { this.verificationType = verificationType; return this; }

    public String getVillageCode() { return villageCode; }
    public PhrAccount villageCode(String villageCode) { this.villageCode = villageCode; return this; }

    public String getVillageName() { return villageName; }
    public PhrAccount villageName(String villageName) { this.villageName = villageName; return this; }

    public String getWardCode() { return wardCode; }
    public PhrAccount wardCode(String wardCode) { this.wardCode = wardCode; return this; }

    public String getWardName() { return wardName; }
    public PhrAccount wardName(String wardName) { this.wardName = wardName; return this; }

    public String getYearOfBirth() { return yearOfBirth; }
    public PhrAccount yearOfBirth(String yearOfBirth) { this.yearOfBirth = yearOfBirth; return this; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhrAccount that = (PhrAccount) o;
        return Objects.equals(abhaNumber, that.abhaNumber) &&
                Objects.equals(address, that.address) &&
                Objects.equals(authMethods, that.authMethods) &&
                Objects.equals(dayOfBirth, that.dayOfBirth) &&
                Objects.equals(districtCode, that.districtCode) &&
                Objects.equals(districtName, that.districtName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(emailVerified, that.emailVerified) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(kycPhoto, that.kycPhoto) &&
                Objects.equals(kycVerified, that.kycVerified) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(middleName, that.middleName) &&
                Objects.equals(mobile, that.mobile) &&
                Objects.equals(monthOfBirth, that.monthOfBirth) &&
                Objects.equals(name, that.name) &&
                Objects.equals(pincode, that.pincode) &&
                Objects.equals(preferredAbhaAddress, that.preferredAbhaAddress) &&
                Objects.equals(profilePhoto, that.profilePhoto) &&
                Objects.equals(stateCode, that.stateCode) &&
                Objects.equals(stateName, that.stateName) &&
                Objects.equals(status, that.status) &&
                Objects.equals(subDistrictCode, that.subDistrictCode) &&
                Objects.equals(subdistrictName, that.subdistrictName) &&
                Objects.equals(tags, that.tags) &&
                Objects.equals(townCode, that.townCode) &&
                Objects.equals(townName, that.townName) &&
                Objects.equals(verificationStatus, that.verificationStatus) &&
                Objects.equals(verificationType, that.verificationType) &&
                Objects.equals(villageCode, that.villageCode) &&
                Objects.equals(villageName, that.villageName) &&
                Objects.equals(wardCode, that.wardCode) &&
                Objects.equals(wardName, that.wardName) &&
                Objects.equals(yearOfBirth, that.yearOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abhaNumber, address, authMethods, dayOfBirth, districtCode, districtName, email, emailVerified, firstName, gender, kycPhoto, kycVerified, lastName, middleName, mobile, monthOfBirth, name, pincode, preferredAbhaAddress, profilePhoto, stateCode, stateName, status, subDistrictCode, subdistrictName, tags, townCode, townName, verificationStatus, verificationType, villageCode, villageName, wardCode, wardName, yearOfBirth);
    }

    @Override
    public String toString() {
        return "PhrAccount{" +
                "abhaNumber='" + abhaNumber + '\'' +
                ", address='" + address + '\'' +
                ", authMethods=" + authMethods +
                ", dayOfBirth='" + dayOfBirth + '\'' +
                ", districtCode='" + districtCode + '\'' +
                ", districtName='" + districtName + '\'' +
                ", email='" + email + '\'' +
                ", emailVerified=" + emailVerified +
                ", firstName='" + firstName + '\'' +
                ", gender='" + gender + '\'' +
                ", kycPhoto='" + kycPhoto + '\'' +
                ", kycVerified=" + kycVerified +
                ", lastName='" + lastName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", monthOfBirth='" + monthOfBirth + '\'' +
                ", name='" + name + '\'' +
                ", pincode='" + pincode + '\'' +
                ", preferredAbhaAddress='" + preferredAbhaAddress + '\'' +
                ", profilePhoto='" + profilePhoto + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", stateName='" + stateName + '\'' +
                ", status='" + status + '\'' +
                ", subDistrictCode='" + subDistrictCode + '\'' +
                ", subdistrictName='" + subdistrictName + '\'' +
                ", tags=" + tags +
                ", townCode='" + townCode + '\'' +
                ", townName='" + townName + '\'' +
                ", verificationStatus='" + verificationStatus + '\'' +
                ", verificationType='" + verificationType + '\'' +
                ", villageCode='" + villageCode + '\'' +
                ", villageName='" + villageName + '\'' +
                ", wardCode='" + wardCode + '\'' +
                ", wardName='" + wardName + '\'' +
                ", yearOfBirth='" + yearOfBirth + '\'' +
                '}';
    }
}


