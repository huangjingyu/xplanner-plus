package com.technoetic.xplanner.acceptance.soap;

import junitx.framework.EqualAssert;
import junitx.framework.MemberEqualAssert;
import junitx.framework.MultiLineStringMemberEqualAssert;
import junitx.framework.TimeOnlyCalendarMemberEqualAssert;

public class SoapAdapterEqualAssert extends EqualAssert {
    public SoapAdapterEqualAssert() {
        super(new MemberEqualAssert[] {
            new IdToReferenceMemberEqualAssert(),
            new MultiLineStringMemberEqualAssert(),
            new TimeOnlyCalendarMemberEqualAssert()
        });
    }

}