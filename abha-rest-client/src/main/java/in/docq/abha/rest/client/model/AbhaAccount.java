package in.docq.abha.rest.client.model;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class AbhaAccount {

    private String abhaNumber;
    private String preferredAbhaAddress;
    private String name;
    private String status;
    private String profilePhoto;

    public static List<AbhaAccount> toAccounts(List<AbhaApiV3ProfileLoginVerifyPost200ResponseAccountsInner> accounts) {
        List<AbhaAccount> abhaAccounts = new ArrayList<>();
        accounts.forEach(account -> abhaAccounts.add(AbhaAccount.builder()
                        .abhaNumber(account.getAbHANumber())
                        .preferredAbhaAddress(account.getPreferredAbhaAddress())
                        .name(account.getName())
                        .status(account.getStatus())
                        .profilePhoto(account.getProfilePhoto())
                        .build())
                );
        return abhaAccounts;
    }
}
