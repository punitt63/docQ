package in.docq.abha.rest.client.model;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class AbhaUser {

    private String abhaAddress;
    private String fullName;
    private String profilePhoto;
    private String abhaNumber;
    private String status;
    private String kycStatus;

    public static List<AbhaUser> toUsers(List<AbhaApiV3PhrWebLoginAbhaVerifyPost200ResponseUsersInner> users) {
        List<AbhaUser> abhaUsers = new ArrayList<>();
        users.forEach(
                user -> abhaUsers.add(AbhaUser.builder()
                        .abhaAddress(user.getAbhaAddress())
                        .abhaNumber(user.getAbhaNumber())
                        .status(user.getStatus())
                        .fullName(user.getFullName())
                        .profilePhoto(user.getProfilePhoto())
                        .kycStatus(user.getKycStatus())
                        .build()));
        return abhaUsers;
    }
}
