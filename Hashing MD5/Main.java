import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
		Scanner sc = new Scanner(new File("test_data.txt"));
		ArrayList<String> param=new ArrayList<>(), comp = new ArrayList<>(), goods;
		String line;
		while(sc.hasNextLine()) {
			line = sc.nextLine();
			// filename
			param.add(line.substring(line.indexOf('(')+1, line.indexOf(')')));
			// actual md5 value of the file
			comp.add(line.substring(line.indexOf('=')+2));
		   // System.out.println(param.get(param.size()-1) + ' '+ comp.get(comp.size() - 1));
		}
		
		goods = worker(param);
		for(int i=0;i<param.size(); ++i) {
			// asert md5 returned from worker function with md5 generated by opnessl 
			// ls | foreach-object{openssl dgst -md5 $_ } | Out-File -Encoding utf8 -FilePath test_data.txt

			System.out.println(param.get(i) + ' ' + goods.get(i) + " == " + comp.get(i) + ' '+goods.get(i).equals(comp.get(i)));
		}
	}

	public static ArrayList<String> worker(ArrayList<String> li) throws NoSuchAlgorithmException, IOException{
		ArrayList<String> goods = new ArrayList<>();
		MessageDigest md = MessageDigest.getInstance("MD5");
		for(int i=0; i<li.size(); ++i) {
			try (InputStream is = Files.newInputStream(Paths.get(li.get(i)));
				     DigestInputStream dis = new DigestInputStream(is, md)) {
				dis.readAllBytes();
				goods.add(byteToHex(md.digest()));
			}
		}
		
		return goods;
	}
	
	public static String byteToHex(byte[] digest) {
		String s = "";
		for(int i=0; i<digest.length; ++i) {
			s += String.format("%02x", digest[i]);
		}
		return s;
	}
}

