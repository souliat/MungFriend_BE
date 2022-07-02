package com.project.mungfriend.dto.dog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DogProfileRequestDto {

    private String name;
    private Long age;
    private String info;
    private String gender;
    private String size;
    private Boolean isRepresentative;

}
