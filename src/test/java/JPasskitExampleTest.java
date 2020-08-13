import de.brendamour.jpasskit.PKBarcode;
import de.brendamour.jpasskit.PKField;
import de.brendamour.jpasskit.PKLocation;
import de.brendamour.jpasskit.PKPass;
import de.brendamour.jpasskit.enums.PKBarcodeFormat;
import de.brendamour.jpasskit.passes.PKGenericPass;
import de.brendamour.jpasskit.signing.PKFileBasedSigningUtil;
import de.brendamour.jpasskit.signing.PKSigningException;
import de.brendamour.jpasskit.signing.PKSigningInformation;
import de.brendamour.jpasskit.signing.PKSigningInformationUtil;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JPasskitExampleTest {
    public static void main(String[] args) throws PKSigningException, IOException, CertificateException {
        String keyStorePath = "certs/pass.p12";
        String keyStorePassword = "start1234";
        String appleWWDRCA = "certs/AppleWWDRCA.cer";
        PKSigningInformation pkSigningInformation = new PKSigningInformationUtil().loadSigningInformationFromPKCS12AndIntermediateCertificate(keyStorePath, keyStorePassword, appleWWDRCA);

        PKPass pass = new PKPass();
        pass.setPassTypeIdentifier("pass.de.codekeepers.jpass");
        pass.setAuthenticationToken("vxwxd7J8AlNNFPS8k0a0FfUFtq0ewzFdc");
        pass.setSerialNumber("12345678000");
        pass.setTeamIdentifier("8LW873B8B4");
        pass.setOrganizationName("your org");
        pass.setDescription("some description");
        pass.setLogoText("some logo text");

        PKBarcode barcode = new PKBarcode();
        barcode.setFormat(PKBarcodeFormat.PKBarcodeFormatPDF417);
        barcode.setMessageEncoding(Charset.forName("iso-8859-1"));
        barcode.setMessage("123456789");
        pass.setBarcodes(Arrays.asList(barcode));

        PKGenericPass generic = new PKGenericPass();
        List<PKField> primaryFields = new ArrayList<PKField>();
        PKField member = new PKField();
        member.setKey("mykey"); // some unique key for primary field
        member.setValue("myvalue"); // some value
        primaryFields.add(member);
        generic.setPrimaryFields(primaryFields);
        pass.setGeneric(generic);

        PKLocation location = new PKLocation();
        location.setLatitude(37.33182); // replace with some lat
        location.setLongitude(-122.03118); // replace with some long
        List<PKLocation> locations = new ArrayList<PKLocation>();
        locations.add(location);
        pass.setLocations(locations);

        if (pass.isValid()) {
            String pathToTemplateDirectory = "sample.pass";
            PKFileBasedSigningUtil pkSigningUtil = new PKFileBasedSigningUtil();
            byte[] passZipAsByteArray = pkSigningUtil.createSignedAndZippedPkPassArchive(pass, pathToTemplateDirectory, pkSigningInformation);
            String outputFile = "/Users/ralf/tmp/sample.pass/sample.pkpass";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(passZipAsByteArray);
            IOUtils.copy(inputStream, new FileOutputStream(outputFile));
            System.out.println("Done!");
        } else {
            System.out.println("the pass is NOT Valid man!!!");
        }
    }
}
