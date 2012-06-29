package com.technoetic.xplanner.security.module.ntlm;

import java.net.UnknownHostException;

import jcifs.smb.SmbException;

import org.apache.log4j.Priority;

/**
 * User: Mateusz Prokopowicz
 * Date: Mar 10, 2005
 * Time: 4:26:59 PM
 */
public interface NtlmLoginHelper
{
    void setLoggingPriority(Priority loggingPriority);

    void authenticate(String userId, String password, String domainController, String domain)
        throws UnknownHostException, SmbException;

    String encodePassword(String password, byte[] salt) throws Exception;
}
