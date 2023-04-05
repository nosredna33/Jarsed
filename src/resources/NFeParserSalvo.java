import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */

/**
 * @author Anderson Silva
 *
 */
public class NFeParserSalvo {
	// static NumberFormat      nfmt = NumberFormat.getInstance(new Locale("pt", "BR"));
	static NumberFormat nfmt = NumberFormat.getInstance(new Locale("en", "US"));

	static BufferedReader OpenIn (String fnameIn ) throws FileNotFoundException {
		File fileIn = new File( fnameIn );
		FileInputStream     fis = new FileInputStream(fileIn);		
		InputStreamReader   ris = new InputStreamReader(fis, StandardCharsets.UTF_8);
		BufferedReader      bir = new BufferedReader( ris );
		
		return bir;
	}	
	
	static BufferedWriter OpenOut (String fnameOut) throws IOException {
    	File fileOut = new File( fnameOut );		    	
    	FileOutputStream fos;			
		fos = new FileOutputStream(fileOut, false);
		OutputStreamWriter wos = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
		BufferedWriter      bow = new BufferedWriter( wos );
		return bow;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			String fnameOut = args[0]
					            .toLowerCase()
					            .replaceAll(" ","")
					            .replace(".txt", ".json");
			
			BufferedReader bir             = OpenIn( args[0] );
			BufferedReader bregx           = OpenIn( args[1] );
			BufferedWriter bow             = OpenOut(fnameOut);
			Vector<String> strToReplace    = new Vector<String>();
			Vector<String> strFlagRep      = new Vector<String>();
			Vector<Pattern>   arPaterns    = new Vector<Pattern>();
			StringBuffer stringToBeMatched = new StringBuffer();
			String line;
			String line2;

			// REad all lines to String var
			while((line = bir.readLine()) != null){
				stringToBeMatched.append(line + "\n");
			}
			String strToReplaced = new String(stringToBeMatched.toString());
			
			// Compile each line from regxp file
			while((line2 = bregx.readLine()) != null){
				if ( line2.isEmpty() )
					continue;
				String parts[] = line2.split("\\|");
				
				// Create a pattern from regex
		        Pattern pattern = Pattern.compile(parts[0]);
		        arPaterns.add( pattern );
		        
			    // Save replacement values
				strToReplace.add( ((parts[1] == null)? "": parts[1]) );
				
				// Save replacement flags 
				strFlagRep.add(parts[2]);
			}
	        String strToReplaced2 = strToReplaced;
			for ( int i=0; i < arPaterns.size(); i++) {
				// Create a matcher for the input String
		        Matcher matcher = arPaterns.get(i).matcher(strToReplaced2);
				if ( strFlagRep.get(i).contains("g+") ) {
		        	strToReplaced2 = (matcher.replaceAll ( Matcher.quoteReplacement(strToReplace.get(i))));
				} else {
		        	strToReplaced2 = (matcher.replaceFirst ( Matcher.quoteReplacement(strToReplace.get(i))));
				}
			}	
			System.out.println(strToReplaced + "\n-------------------------------------");
			System.out.println(strToReplaced2);
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private static char[] matcher(String string) {
		// TODO Auto-generated method stub
		return null;
	}

}
