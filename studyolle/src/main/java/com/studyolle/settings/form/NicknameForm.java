package com.studyolle.settings;


import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

public class NickName {
    @Length(min = 3, max = 20)
    @Pattern(regexp = )
    private String name;
}
