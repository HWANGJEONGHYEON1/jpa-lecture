package com.studyolle.settiongs;

import com.studyolle.account.domain.Account;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Profile {
    private String bio;

    private String url;

    private String occupation;

    private String location; // 거주지역

    public Profile(Account account) {
        this.bio = account.getBio();
        this.url = account.getUrl();
        this.occupation = account.getOccupation();
        this.location = account.getLocation();
    }
}
