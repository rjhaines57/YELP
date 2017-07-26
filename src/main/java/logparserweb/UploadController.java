package logparserweb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UploadController {

	@Autowired
	FileManagerService fileService;
	
	// Save the uploaded file to this folder
	private static String UPLOADED_FOLDER = "C://Tmp//logparser//";

	@RequestMapping("/upload")
	public String singleFileUpload(@RequestParam("filename") MultipartFile filename, @RequestParam("description") String description,
			RedirectAttributes redirectAttributes) {

		if (filename.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
			return "redirect:uploadStatus";
		}

		try {

			
			
			// Get the file and save it somewhere
			byte[] bytes = filename.getBytes();
			String origName=filename.getOriginalFilename();
			if (origName == null && origName.contains("../") && origName.contains("$") && origName.contains("*"))//Check the path whether it's included risk character
			{
				throw new IOException("Invalid Path");
			}
			else {
			Path path = Paths.get(UPLOADED_FOLDER + origName);
			Files.write(path, bytes);
			Long fileId=fileService.createFile(filename.getOriginalFilename(),description);
			Path newPath = Paths.get(UPLOADED_FOLDER + "log_"+fileId);
			
			Files.move(path, newPath);
			}
			
			
			redirectAttributes.addFlashAttribute("message",
					"You successfully uploaded '" + filename.getOriginalFilename() + "'");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return "redirect:/uploadStatus";
	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}

}
