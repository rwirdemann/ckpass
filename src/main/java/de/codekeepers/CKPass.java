package de.codekeepers;

import de.brendamour.jpasskit.PKBarcode;
import de.brendamour.jpasskit.PKField;
import de.brendamour.jpasskit.PKLocation;
import de.brendamour.jpasskit.PKPass;
import de.brendamour.jpasskit.enums.PKBarcodeFormat;
import de.brendamour.jpasskit.passes.PKGenericPass;
import de.brendamour.jpasskit.signing.PKSigningInformation;
import de.brendamour.jpasskit.signing.PKSigningInformationUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CKPass {
    private static final String keyStorePath = "certs/pass.p12";
    private static final String keyStorePassword = "start1234";
    private static final String appleWWDRCA = "certs/AppleWWDRCA.cer";

    private final PKSigningInformation pkSigningInformation;
    private final PKPass pass;

    public CKPass() {
        try {
            pkSigningInformation = new PKSigningInformationUtil().loadSigningInformationFromPKCS12AndIntermediateCertificate(keyStorePath, keyStorePassword, appleWWDRCA);
            pass = new PKPass();
            pass.setPassTypeIdentifier("pass.de.codekeepers.jpass");
            pass.setAuthenticationToken("XXX");
            pass.setSerialNumber("12345678000");
            pass.setTeamIdentifier("8LW873B8B4");
            pass.setOrganizationName("CodeKeepers GmbH");
            pass.setDescription("some description");
            pass.setLogoText("some logo text");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        } catch (CertificateException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean build(String message) {
        PKBarcode bc = new PKBarcode();
        bc.setFormat(PKBarcodeFormat.PKBarcodeFormatPDF417);
        bc.setMessageEncoding(Charset.forName("iso-8859-1"));
        bc.setMessage(message);
        pass.setBarcodes(Arrays.asList(bc));

        PKGenericPass generic = new PKGenericPass();
        List<PKField> primaryFields = new ArrayList<PKField>();
        PKField member = new PKField();
        member.setKey("mykey"); // some unique key for primary field
        member.setValue("myvalue"); // some value
        primaryFields.add(member);
        generic.setPrimaryFields(primaryFields);
        pass.setGeneric(generic);

        return pass.isValid();
    }
}

