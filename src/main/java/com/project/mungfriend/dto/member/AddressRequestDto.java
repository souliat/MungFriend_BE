package com.project.mungfriend.dto.member;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressRequestDto  {
    private String address;
    private String latitude;
    private String longitude;
}
