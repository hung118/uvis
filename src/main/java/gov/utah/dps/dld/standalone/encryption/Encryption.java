// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Encryption.java

package gov.utah.dps.dld.standalone.encryption;

import java.io.*;
import java.util.*;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import sun.misc.BASE64Decoder;

@SuppressWarnings({"rawtypes", "unchecked", "restriction"})
public class Encryption
{

    public Encryption()
    {
        action = 1;
        errors = new ArrayList();
        encryptedPass = true;
        key = null;
        sKey = null;
    }

    public static void main(String args[])
    {
        try
        {
            if(args == null || args.length == 0 || "h".equalsIgnoreCase(args[0]))
            {
                printHelp();
            } else
            {
                Encryption encryptor = new Encryption();
                encryptor.parseArgs(args);
                if(encryptor.isReady())
                {
                    encryptor.process();
                } else
                {
                    String error;
                    for(Iterator i$ = encryptor.getErrors().iterator(); i$.hasNext(); System.out.println(error))
                        error = (String)i$.next();

                }
            }
        }
        catch(Exception e)
        {
            System.err.println("Failure");
        }
    }

    public void setSource(String sourceName)
    {
        source = sourceName;
    }

    public String getSource()
    {
        return source;
    }

    public void setDestination(String destName)
    {
        dest = destName;
    }

    public String getDestination()
    {
        return dest;
    }

    public void setIntermediateDest(String destName)
    {
        dest2 = destName;
    }

    public String getIntermediateDest()
    {
        return dest2;
    }

    public void setKeyFile(String fileName)
    {
        keyFile = fileName;
    }

    public String getKeyFile()
    {
        return keyFile;
    }

    public void setAction(int action)
    {
        this.action = action;
    }

    public List getErrors()
    {
        return errors;
    }

    public boolean process()
        throws Exception
    {
        boolean ret = false;
        errors = null;
        if(isReady())
            switch(action)
            {
            case 1: // '\001'
            {
                System.out.println((new StringBuilder()).append("Encrypting File ").append(source).toString());
                encrypt(new File(source), new File(dest), getKey());
                ret = true;
                break;
            }

            case 2: // '\002'
            {
                System.out.println((new StringBuilder()).append("Decrypting File ").append(source).toString());
                decrypt(new File(source), new File(dest), getKey());
                ret = true;
                break;
            }

            case 3: // '\003'
            {
                System.out.println((new StringBuilder()).append("Testing encryption/decryption File ").append(source).toString());
                encrypt(new File(source), new File(dest), getKey());
                decrypt(new File(dest), new File(dest2), getKey());
                ret = compare(source, dest2);
                break;
            }

            case 4: // '\004'
            {
                System.out.println((new StringBuilder()).append("Encrypting directory ").append(source).toString());
                //encryptFolder(source, dest, getKey());
                ret = true;
                break;
            }

            case 5: // '\005'
            {
                System.out.println((new StringBuilder()).append("Unziping file ").append(source).toString());
                String decDirectory = createDirectory(dest);
                decrypt(source, decDirectory, getKey());
                ret = true;
                break;
            }

            case 6: // '\006'
            {
                String zipFile = "tempTest.zip";
                System.out.println((new StringBuilder()).append("Testing directory encryption/decryption ").append(source).toString());
                action = 4;
                System.out.println((new StringBuilder()).append("Encrypting directory ").append(source).toString());
                //encryptFolder(source, zipFile, getKey());
                listFilesInZip(zipFile);
                action = 5;
                System.out.println("Unziping file tempTest.zip");
                String decDirectory = createDirectory(dest);
                decrypt(zipFile, decDirectory, getKey());
                ret = compareDirectories(source, dest);
                break;
            }

            default:
            {
                errors.add("Unknown Action");
                break;
            }
            }
        else
            errors.add("Aborting");
        return ret;
    }

    boolean compareDirectories(String origDirectory, String endDirectory)
    {
        boolean match = true;
        File orig = new File(origDirectory);
        File end = new File(endDirectory);
        if(orig.exists() && end.exists() && orig.isDirectory() && end.isDirectory())
        {
            File filesIn[] = orig.listFiles();
            File arr$[] = filesIn;
            int len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                File file = arr$[i$];
                if(file.isFile())
                    match = match && compare(file, new File(endDirectory, file.getName()));
            }

        } else
        {
            match = false;
            if(!orig.exists() || !orig.isDirectory())
                errors.add("Original directory does not exist");
            if(!end.exists() || !end.isDirectory())
                errors.add("Destination directory does not exist");
        }
        return match;
    }

    boolean compare(File inFile, File outFile)
    {
        boolean match = true;
        if(inFile != null && outFile != null) {
        	BufferedInputStream in = null;
        	BufferedInputStream in2 = null;
            try {
                in = new BufferedInputStream(new FileInputStream(inFile));
                in2 = new BufferedInputStream(new FileInputStream(outFile));
                int i;
                do
                {
                    i = in.read();
                    int i2 = in2.read();
                    if(i == i2)
                        continue;
                    errors.add((new StringBuilder()).append(inFile.getName()).append("::").append(outFile.getName()).append(" files not equal").toString());
                    match = false;
                    break;
                } while(i != -1);
                if(match)
                    System.out.println("Files match");
                else
                    errors.add((new StringBuilder()).append(inFile.getAbsolutePath()).append(" does not match ").append(outFile.getAbsolutePath()).toString());
            } catch(Exception e) {
                System.err.println("Failure");
            } finally {
            	try {
            		in.close();
            		in2.close();
            	} catch (Exception e) {}
            }
        } else {
            errors.add("Cannot match files  as dest does not exist");
        }
        return match;
    }

    protected boolean compare(String file1Name, String file2Name)
    {
        return compare(new File(file1Name), new File(file2Name));
    }

    protected String[] getOptions(String arg)
    {
        String ret[] = null;
        if(arg != null && arg.length() > 1)
        {
            for(int idx = 1; idx < arg.length(); idx++)
            {
                char c = arg.charAt(idx);
                switch(c)
                {
                case 101: // 'e'
                    action = 1;
                    break;

                case 100: // 'd'
                    action = 2;
                    break;

                case 116: // 't'
                    action = 3;
                    break;

                case 108: // 'l'
                    action = 4;
                    break;

                case 120: // 'x'
                    action = 5;
                    break;

                case 122: // 'z'
                    action = 6;
                    break;

                case 107: // 'k'
                    encryptedPass = false;
                    break;
                }
            }

        }
        return ret;
    }

    public void parseArgs(String args[])
    {
        String arr$[] = args;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            String arg = arr$[i$];
            if(arg == null || args.length <= 0)
                continue;
            if(arg.startsWith("-"))
            {
                getOptions(arg);
                continue;
            }
            if(keyFile == null)
            {
                keyFile = arg;
                continue;
            }
            if(source == null)
            {
                source = arg;
                continue;
            }
            if(dest == null)
            {
                dest = arg;
                continue;
            }
            if(dest2 == null)
                dest2 = arg;
        }

    }

    public boolean isReady()
    {
        boolean ret = true;
        if(keyFile == null || keyFile.length() <= 0)
        {
            ret = false;
            errors.add("Unable to run: Missing Parameter :: keyfile");
        }
        if(source == null || source.length() <= 0)
        {
            ret = false;
            errors.add("Unable to run: Missing Parameter :: source ");
        }
        if(action == 3 && (dest2 == null || dest2.length() <= 0))
        {
            ret = false;
            errors.add("Need 2 destination files for test");
        }
        if(ret && dest == null)
            if(action == 3 || action == 1 || action == 2)
                dest = (new StringBuilder()).append(source).append(".dst").toString();
            else
                dest = (new StringBuilder()).append(source).append(".zip").toString();
        if(ret)
        {
            if(!checkFile(keyFile))
            {
                ret = false;
                errors.add((new StringBuilder()).append("Unable to run: Keyfile not found :: ").append(keyFile).toString());
            }
            if((action == 1 || action == 2 || action == 3) && !checkFile(source))
            {
                ret = false;
                errors.add((new StringBuilder()).append("Unable to run: Source not found :: ").append(source).toString());
            }
            if((action == 4 || action == 6) && !checkDirectory(source))
            {
                ret = false;
                errors.add((new StringBuilder()).append("Unable to run: Source not directory :: ").append(source).toString());
            }
        }
        return ret;
    }

    public static boolean encrypt(File inFile, File outFile, String encKey)
        throws Exception
    {
        ZipFile zip = new ZipFile(outFile);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(8);
        parameters.setCompressionLevel(5);
        if(encKey != null && !encKey.isEmpty())
        {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(99);
            parameters.setAesKeyStrength(3);
            parameters.setPassword(encKey);
        }
        if(inFile.isDirectory())
            zip.addFolder(inFile, parameters);
        else
            zip.addFile(inFile, parameters);
        return true;
    }

    public static boolean encrypt(String inFile, String outFile, String encKey)
        throws Exception
    {
        ZipFile zip = new ZipFile(outFile);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(8);
        parameters.setCompressionLevel(5);
        if(encKey != null && !encKey.isEmpty())
        {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(99);
            parameters.setAesKeyStrength(3);
            parameters.setPassword(encKey);
        }
        zip.addFile(new File(inFile), parameters);
        return true;
    }

    /*
    public static boolean encryptFolder(String inFile, String outFile, String encKey)
        throws Exception
    {
        ZipFile zip = new ZipFile(outFile);
        ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(8);
        parameters.setCompressionLevel(5);
        if(encKey != null && !encKey.isEmpty())
        {
            parameters.setEncryptFiles(true);
            parameters.setEncryptionMethod(99);
            parameters.setAesKeyStrength(3);
            parameters.setPassword(encKey);
        }
        File folder = new File(inFile);
        ArrayList files = new ArrayList();
        File arr$[] = folder.listFiles();
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            File file = arr$[i$];
            if(!file.isDirectory())
                files.add(file);
        }

        String root = "";
        zip.addFiles(files, parameters);
        File arr$[] = folder.listFiles();
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            File file = arr$[i$];
            if(file.isDirectory())
                addFilesToFolder(zip, file, root, parameters);
        }

        return true;
    }*/

    protected static void addFilesToFolder(ZipFile zip, File folder, String root, ZipParameters parameters)
        throws Exception
    {
		ArrayList files = new ArrayList();
        File arr$[];
        int len$;
        if(folder.isDirectory())
        {
            arr$ = folder.listFiles();
            len$ = arr$.length;
            for(int i$ = 0; i$ < len$; i$++)
            {
                File file = arr$[i$];
                if(!file.isDirectory())
                    files.add(file);
            }

            parameters.setRootFolderInZip((new StringBuilder()).append(root).append(folder.getName()).append("/").toString());
            zip.addFiles(files, parameters);
        }
        arr$ = folder.listFiles();
        len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            File file = arr$[i$];
            if(file.isDirectory())
                addFilesToFolder(zip, file, root, parameters);
        }

    }

    public static void decrypt(File inFile, File outFile, String key)
        throws Exception
    {
        ZipFile zipFile = new ZipFile(inFile);
        if(zipFile.isEncrypted())
            zipFile.setPassword(key);
        zipFile.extractAll(outFile.getAbsolutePath());
    }

    public static void decrypt(String inFile, String outFile, String key)
        throws Exception
    {
        ZipFile zipFile = new ZipFile(inFile);
        if(zipFile.isEncrypted())
            zipFile.setPassword(key);
        zipFile.extractAll(outFile);
    }

    public static void listFilesInZip(String fileName)
        throws Exception
    {
        ZipFile zipFile = new ZipFile(fileName);
        List fileHeaderList = zipFile.getFileHeaders();
        for(Iterator i$ = fileHeaderList.iterator(); i$.hasNext(); System.out.println("************************************************************"))
        {
            Object aFileHeaderList = i$.next();
            FileHeader fileHeader = (FileHeader)aFileHeaderList;
            System.out.println((new StringBuilder()).append("****File Details for: ").append(fileHeader.getFileName()).append("*****").toString());
            System.out.println((new StringBuilder()).append("Name: ").append(fileHeader.getFileName()).toString());
            System.out.println((new StringBuilder()).append("Compressed Size: ").append(fileHeader.getCompressedSize()).toString());
            System.out.println((new StringBuilder()).append("Uncompressed Size: ").append(fileHeader.getUncompressedSize()).toString());
            System.out.println((new StringBuilder()).append("CRC: ").append(fileHeader.getCrc32()).toString());
        }

    }

    protected String createDirectory(String name)
    {
        return createDirectory(null, name);
    }

    String createDirectory(String parent, String name)
    {
        String ret = null;
        File file;
        if(parent != null)
            file = new File(parent, name);
        else
            file = new File(name);
        if(!file.exists())
        {
            if(file.mkdirs())
                ret = file.getAbsolutePath();
        } else
        {
            ret = file.getAbsolutePath();
        }
        return ret;
    }

    void cleanup(String directory)
    {
        File file = new File(directory);
        if(file.exists())
            if(file.isDirectory())
            {
                File files[] = file.listFiles();
                if(files != null && files.length > 0)
                {
                    File arr$[] = files;
                    int len$ = arr$.length;
                    for(int i$ = 0; i$ < len$; i$++)
                    {
                        File tFile = arr$[i$];
                        if(tFile.isDirectory())
                            cleanup(tFile.getAbsolutePath());
                        else
                            tFile.delete();
                    }

                }
                file.delete();
            } else
            {
                file.delete();
            }
    }

    SecretKey loadKey()
    {
        if(key == null)
            try
            {
                File file = new File(keyFile);
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String ret = reader.readLine();
                if(encryptedPass)
                    sKey = decryptPassword(ret);
                else
                    sKey = ret;
                reader.close();
            }
            catch(Exception e)
            {
                System.err.println("Failure in reading key file");
            }
        return key;
    }

    public String getKey()
    {
        if(key == null)
            loadKey();
        return sKey;
    }

    boolean checkFile(String fileName)
    {
        boolean ret = false;
        if(fileName != null && fileName.length() > 0)
        {
            File file = new File(fileName);
            ret = file.canRead();
        }
        return ret;
    }

    boolean checkDirectory(String fileName)
    {
        boolean ret = false;
        if(fileName != null && fileName.length() > 0)
        {
            File file = new File(fileName);
            ret = file.isDirectory();
        }
        return ret;
    }

    public static void printHelp()
    {
        System.out.println("encryption/decryption for Utah DPS");
        System.out.println("SYNOPSIS");
        System.out.println("    java -jar dld-encryption [ -edlxz ] keyfile source [ destination ] [ destination 2 ]");
        System.out.println("DESCRIPTION");
        System.out.println("    -e    Encrypt the file[s] with the key in the key file.");
        System.out.println("    -d    Decrypt the file");
        System.out.println("    -t    Test the encrypt/decrypt process. This needs two destinations and will compare original source to decrypted for validation");
        System.out.println("    -l    Encrypt the contents of a director and put into a resultant zip file.  (If no dest file will be encrypted.zip, Uses tmpEncryptDir as the tmp directory to hold files)");
        System.out.println("    -x    Source file is a zip containing the encrypted files of a directory (destination directory is required)");
        System.out.println("    -z test encryption/decryption of directory, . Will leave a directory and file, dest directory and tempZip.zip for comparison");
        System.out.println("    If no destination file for encryption or decryption the resultant file will be named [fileName].dst");
    }

    public static String decryptPassword(String in)
        throws Exception
    {
        String ret = in;
        if(in != null && in.length() > 0)
        {
            BASE64Decoder base64decoder = new BASE64Decoder();
            byte encrypedPwdBytes[] = base64decoder.decodeBuffer(in);
            Cipher cipher = getCipher();
            cipher.init(2, getCipherKey());
            byte plainTextPwdBytes[] = cipher.doFinal(encrypedPwdBytes);
            ret = new String(plainTextPwdBytes);
        }
        return ret;
    }

    public static SecretKey getCipherKey()
        throws Exception
    {
        if(cipherKey == null)
        {
            DESKeySpec keySpec = new DESKeySpec("n3v3rkn0w".getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            cipherKey = keyFactory.generateSecret(keySpec);
        }
        return cipherKey;
    }

    public static Cipher getCipher()
        throws Exception
    {
        if(cipher == null)
            cipher = Cipher.getInstance("DES");
        return cipher;
    }

    public static final int ENCRYPT = 1;
    public static final int DECRYPT = 2;
    public static final int TEST = 3;
    public static final int ENCRYPT_DIRECTORY = 4;
    public static final int DECRYPT_DIRECTORY = 5;
    public static final int TEST_DIRECTORY = 6;
    int action;
    List errors;
    String keyFile;
    String source;
    String dest;
    String dest2;
    boolean encryptedPass;
    SecretKey key;
    String sKey;
    private static SecretKey cipherKey = null;
    private static Cipher cipher = null;

}
