/*
    Oracle Deutschland BV & Co. KG

    This software is the confidential and proprietary information of
    Oracle Corporation. ("Confidential Information").  You shall not
    disclose such Confidential Information and shall use it only in
    accordance with the terms of the license agreement you entered
    into with Oracle.

    ORACLE MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
    SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
    IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
    PURPOSE, OR NON-INFRINGEMENT. ORACLE SHALL NOT BE LIABLE FOR ANY DAMAGES
    SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
    THIS SOFTWARE OR ITS DERIVATIVES.

    Copyright © 2022. All Rights reserved

    -----------------------------------------------------------------------

    System      :   Oracle Access Manager
    Subsystem   :   OpenIdConnect Extension

    File        :   TestTokenHash.java

    Compiler    :   Oracle JDeveloper 12c

    Author      :   jovan.j.lakic@oracle.com

    Purpose     :   This file implements the class
                    TestTokenHash.


    Revisions   Date        Editor      Comment
    -----------+-----------+-----------+-----------------------------------
    1.0.0.0     2023-19-07  JLakic     First release version
*/

package bka.iam.access.oidc.extension.utils;

import oracle.security.restsec.jwt.JwtToken;

public class TestTokenHash {


  static final String ACCESS_TOKEN   = "eyJ4NXQiOiJlRnpFQmNkX0N3SV9qRlk1bXJlVEd1OGthWlEiLCJraWQiOiJTZWN1cmVEb21haW4yIiwiYWxnIjoiUlMyNTYifQ.eyJzdWIiOiJhbjQ3MTExMjMiLCJwMjBfdWlkIjoiVC0zNi0xNS0xNS0xMDEtQU40NzExMTIzIiwiaXNzIjoiaHR0cHM6Ly9zc28uY2lubmFtb25zdGFyLm5ldDo0NDMvb2F1dGgyIiwiY29kZV9jaGFsbGVuZ2VfbWV0aG9kIjoiUzI1NiIsInNlc3Npb25JZCI6ImVkOGNmNGU1LTc4NWYtNGE5YS05ZWE3LTI0ZmQxMjBmYjZlYnxFbDZ6UHpaQUJJM0kvaG9QWTcwU2JucVN4ZVFScDNqN1c5T3lBcHp5SndVPSIsImF1ZCI6WyJodHRwczovL3Nzby5jaW5uYW1vbnN0YXIubmV0OjQ0My9vYXV0aDIiLCJaSU1QIiwiemltcCJdLCJpZHAiOiJBTiIsInNjb3BlIjpbIm9wZW5pZCIsIlpJTVAuTWFpbiJdLCJkb21haW4iOiJTZWN1cmVEb21haW4yIiwiY2xpZW50IjoiemltcCIsImV4cCI6MTczNzEzNzkwMywiZ3JhbnQiOiJBVVRIT1JJWkFUSU9OX0NPREUiLCJpYXQiOjE3MzcxMzA3MDMsImp0aSI6ImN5LXVaTGJ6SXpabV9mUVpJbWVjRVEiLCJjb2RlX2NoYWxsZW5nZSI6InoxellzdVVkcHg2ZkpVM2RfUHpLYnVZSmJMTElHd0M1RGk3Y01ydGY3SEUifQ.gG2GCqhYROoB-WzL8S5r0kiJVaF-4nyty2nKd0KHajUvaB6wLxVS4w_hUJ1u72NGqWjMmENtqKhP4upzADPBWWtu6RnoKu5LUE8tNVm6mGkNaUWRJX6yBeXe6Rj0SoRz_k7n-MYhUE33cFsctozckIe-HPcGtjXZylI0BrT8Atjn_DuJxlFXPt3uFZsNOQ_OTceV8U_vSGFpEW4ktkrBEsj_Y0Uaagsko4AL0k1w1TiA-DTAvIrvvDV0MEcAs8zzMo5DD1M7ulv0hwQAsY5_LgmqcAFOfw7BQWdCLB7EbO_PYvRdAsyBo-Hb4i-vXXIUl-V0QKtBEjA4dTBzbF5QOA";
  static final String IDENTITY_TOKEN = "eyJraWQiOiJTZWN1cmVEb21haW4yIiwieDV0IjoiZUZ6RUJjZF9Dd0lfakZZNW1yZVRHdThrYVpRIiwiYWxnIjoiUlMyNTYifQ.eyJpc3MiOiJodHRwczovL3Nzby5jaW5uYW1vbnN0YXIubmV0OjQ0My9vYXV0aDIiLCJzdWIiOiJhbjQ3MTExMjMiLCJhdWQiOlsiaHR0cHM6Ly9zc28uY2lubmFtb25zdGFyLm5ldDo0NDMvb2F1dGgyIiwiWklNUCIsInppbXAiXSwiZXhwIjoxNzM3MTM3OTA0LCJpYXQiOjE3MzcxMzA3MDQsImp0aSI6IjE3cjJVWUlkX1l3cGpOQXM0XzlfSWciLCJhdF9oYXNoIjoiOF9pWXZ3MXlSaXpELVNyRjlYUGQ4USIsImF6cCI6InppbXAiLCJhY3IiOiJTMjU2Iiwic2lkIjoiTFFuOFZGb2kvdEFvWVZDMjRFY2RYQT09flV6RVpWdnNLYy9BdzA3MWdVdzJ6bVAxdlpDVDJNWjNSRG8yN1c0RmxSS25sYWtyNk16SlR4ZGxneG4yUEtycmpSb2t2cXFjNlRDVGNSMlUwdnBCNXhDL0pUNnNISmErRjJaam5PZUJCMzAwc2pCbUVqR0xpOVFWcXFwM3F2NlpkIiwiYXV0aF90aW1lIjoxNzM3MTMwNjcwLCJhbXIiOlsicHdkIl0sInAyMF91aWQiOiJULTM2LTE1LTE1LTEwMS1BTjQ3MTExMjMiLCJpZHAiOiJBTiIsImNvZGVfY2hhbGxlbmdlX21ldGhvZCI6IlMyNTYiLCJzZXNzaW9uSWQiOiJlZDhjZjRlNS03ODVmLTRhOWEtOWVhNy0yNGZkMTIwZmI2ZWJ8RWw2elB6WkFCSTNJL2hvUFk3MFNibnFTeGVRUnAzajdXOU95QXB6eUp3VT0iLCJjb2RlX2NoYWxsZW5nZSI6InoxellzdVVkcHg2ZkpVM2RfUHpLYnVZSmJMTElHd0M1RGk3Y01ydGY3SEUifQ.UQeVjv3PyspAeJr95q7iUrk1JfkMw62QxVPQfUKggRcyoftwy5U3pAVOSSeC0iD1ETK1PqpLtUNLr2lED7Q_9aaf9YZryVzDiobHjtTfmh3xWee62y7OSHCtmBi1EKMkV48tEr5-eZk3mt-uCHGGLRFsio8XG-edhVuopRrXZ2Fk3Np_fnSAaoEnTIw3lphMQfoi01zSWtfFNSipMGMrF7wnZlGyxqo5PAj8TTZ5XYwRrosgiA-YtROCK-K7NdSHq5EbWynva8zWt4EWoUURsNdGax_YNyQnslsTae3aOycD_ZKwXuNrIp6toEEQeMye4AEuPXK4767UTGkAh2lqLA";
  
  public static void main(String[] args) {
    try {
//      final JwtToken  accessToken   = new JwtToken(ACCESS_TOKEN);
//      final JwtToken  refreshToken  = new JwtToken(REFRESH_TOKEN);
      final JwtToken  identityToken = new JwtToken(IDENTITY_TOKEN);
      final String    atHash       = TokenUtil.tokenHash(ACCESS_TOKEN);
      
      System.out.println(identityToken.getClaimParameter("at_hash"));
      System.out.println(atHash);
      
//      identityToken.signAndSerialize(arg0);
      
      //SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}