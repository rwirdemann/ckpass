package de.codekeepers;

import de.brendamour.jpasskit.signing.PKSigningInformation;
import de.brendamour.jpasskit.signing.PKSigningInformationUtil;

import java.io.IOException;
import java.security.cert.CertificateException;

public class CKPass {
    private static final String keyStorePath = "certs/pass.p12";
    private static final String keyStorePassword = "start1234";
    private static final String appleWWDRCA = "certs/AppleWWDRCA.cer";

    private PKSigningInformation pkSigningInformation;

    public CKPass() {
        try {
            pkSigningInformation = new PKSigningInformationUtil().loadSigningInformationFromPKCS12AndIntermediateCertificate(keyStorePath, keyStorePassword, appleWWDRCA);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

