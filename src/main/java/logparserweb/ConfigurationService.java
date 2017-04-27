package logparserweb;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

	
	public String getConfiguration()
	{
		
		
		
		try {
			Charset charset = Charset.forName("UTF-8");

			Path path=Paths.get("D:\\Development\\workspace\\logParser1\\src\\main\\resources\\checkerDefinition.json");
			
			byte[] fileContents = Files.readAllBytes(path);
			
			return new String(fileContents, charset );
		
		
		} catch (IOException e) {
			// do nothing at the moment

		}
		return null;
	}
	
	
}
