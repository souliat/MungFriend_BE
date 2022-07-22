package com.project.mungfriend.util;

import com.project.mungfriend.enumeration.UserRole;
import com.project.mungfriend.model.Member;

public class MemberValidator {

    public static void updateUserRole(Member member){
        if (!member.getPhoneNum().equals("")
                && !member.getAddress().equals("")
                && !member.getLatitude().equals("")
                && !member.getLongitude().equals("")){

            member.setUserRole(UserRole.QUALIFIED_USER);
        }
    }
}
