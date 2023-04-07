package br.gov.saude.anderson;
/***
 * Como rodar:
 *    C:\Bin\Java\jre1.8.0_361\bin\javaw.exe -Dfile.encoding=UTF-8 \
 *           -classpath "C:\Projetos\Datasus\DLOG\MS-NFe\Java\bin" \
 *           br.gov.saude.anderson.Parser \
 *           "path/nfe-52220915800545000311550010000919611001863258.txt"
 ***/
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

/**
 * 
 */

/**
 * @author Anderson Silva
 *
 */
public class Parser {
	// static NumberFormat      nfmt = NumberFormat.getInstance(new Locale("pt", "BR"));
	// static NumberFormat nfmt = NumberFormat.getInstance(new Locale("en", "US"));
	
	private static FileInputStream getFileFromResourceAsStream(String fileName) 
	throws IllegalArgumentException {
	    File file = null;
	    FileInputStream fis = null;
        // The class loader that loaded the class kkkkkk
		try {
		    URL resource = Parser.class.getResource(fileName);
		    if ( resource == null ) {
				ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		    	file = new File(classLoader.getResource(fileName).getFile());
		    } else {
		    	file = new File(resource.toURI());
		    }
		    
		    fis = new FileInputStream(file);
		} catch (URISyntaxException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	        file  = null;
	    } catch (FileNotFoundException e1) {
	        // TODO Auto-generated catch block
	        e1.printStackTrace();
	        file  = null;
	    }	
        // the stream holding the file content
        if ( (file  == null) ) {
            throw new IllegalArgumentException("Arquivo: " 
                + fileName 
                + " não existe como recurso ou não pose ser acessado" );
        } 
        return fis;
    }
	
	static BufferedReader OpenIn (String fnameIn ) 
	throws FileNotFoundException, IllegalArgumentException {
		File            fileIn = null;
		FileInputStream    fis = null; 
		if ( fnameIn != null ) {
		   fileIn = new File( fnameIn );
		   fis    = new FileInputStream(fileIn);				   
		} else {
			fis   = getFileFromResourceAsStream("resources/parseFields.regexp");
		}
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
			
			BufferedReader bir                = OpenIn( args[0] );
			BufferedReader bregx              = OpenIn( ((args.length < 2)? null: args[1]) );
			BufferedWriter bow                = OpenOut(fnameOut);
			Vector<String> strValToReplace    = new Vector<String>();
			Vector<String> strFlagRegExp      = new Vector<String>();
			Vector<String>       arPaterns    = new Vector<String>();
			StringBuffer stringToBeMatched = new StringBuffer();
			String line;

			// REad all lines to String var
			while((line = bir.readLine()) != null){
				stringToBeMatched.append(line + "\n");
			}
			String stringToBeMatched2 = new String(stringToBeMatched.toString());
			line = null;
			
			// Get and compile each line from regxp file
			while((line = bregx.readLine()) != null){
				// Skip Empty lines
				if ( line.isEmpty() )
					continue;
				
				// Skip Comments
				if ( line.startsWith("#") )
					continue;
				
				// Get each parts of search/replace rule
				String parts[] = line.split("\\|");
				
				// Create a pattern from regex
		        // Pattern pattern = Pattern.compile(parts[0]);
		        arPaterns.add( parts[0] );
		        
			    // Save replacement values
				strValToReplace.add( ((parts[1] == null)? "": 
					    parts[1].replace("\\n", "\n")).replace("\\t", "\t") );
				
				// Save replacement flags 
				strFlagRegExp.add( (((parts[2] == null) || parts[2].isEmpty())? "g+": parts[2]) );
			}

			for ( int i=0; i < arPaterns.size(); i++) {
				// Create a matcher for the input String
		        // Matcher matcher = arPaterns.get(i).matcher(stringToBeMatched2);
				if ( strFlagRegExp.get(i).contains("g+") ) {
					stringToBeMatched2 = stringToBeMatched2.replaceAll (
							                "(?s)" + arPaterns.get(i),
							                (strValToReplace.get(i)));
				} else {
					// (matcher.replaceFirst ( Matcher.quoteReplacement(strValToReplace.get(i))));
					stringToBeMatched2 = stringToBeMatched2.replaceAll (
			                arPaterns.get(i),
			                (strValToReplace.get(i)));
				}
			}
			// Util para depurar arquivos de script .regexp
			// System.out.println(stringToBeMatched 
			//     + "\n-------------------------------------");
			// System.out.println(stringToBeMatched2);
			bow.write(stringToBeMatched2);
			bow.flush();
			bow.close();
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(-1);
		}
		System.exit(0);
	}
}
