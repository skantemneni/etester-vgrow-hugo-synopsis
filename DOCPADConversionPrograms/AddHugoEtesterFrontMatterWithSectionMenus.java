package com.etester.hugo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility main program that rewrites the "header" metadata as per the template. 
 * Note that metadata is this small "yaml" template on top of the file that hugo uses.
 * 
 * @author skantemneni
 *
 */
public class AddHugoEtesterFrontMatterWithSectionMenus {

	/**
	 * Standard metadata template for Hugo Content files 
	 */
//	private static String metadataTemplate_DO_NOT_USE = 
//			"---\r\n"
//			+ "title: \"%1$s\"\r\n"
//			+ "tags: [%2$s]\r\n"
//			+ "categories: [%2$s]\r\n"
//			+ "draft: false\r\n"
//			+ "---\r\n"
//			+ "";
	
	private static final List<String> CHANNELS = new ArrayList<String>(Arrays.asList(
			"FOUNDATION7",
			"FOUNDATION8",
			"FOUNDATION9",
			"FOUNDATION10",
			"INTERMEDIATE",
			"RRBSYNOPSIS", 
			"BANKINGSYNOPSIS"));

	
	private static String metadataTemplateChannelFolderMenu =
			"menu:\r\n"
			+ "  main:\r\n"
			+ "    identifier: \"%7$s\"\r\n"
			+ "    name: \"%4$s\"\r\n"
			+ "    url: \"%7$s\"\r\n"
			+ "    weight: %8$d\r\n"
			+ "  footer:\r\n"
			+ "    name: \"%4$s\"\r\n"
			+ "    weight: %8$d\r\n"
			+ "";

	private static String metadataTemplateChannelFolder =
			"---\r\n"
			+ "title: \"%1$s\"\r\n"
			+ "topic: \"%2$s\"\r\n"
			+ "subject: \"%3$s\"\r\n"
			+ "channel: \"%4$s\"\r\n"
			+ "menuname: \"%9$s\"\r\n"
			+ "draft: false\r\n"
			+ "image: \"image/%7$s.png\"\r\n"
//			+ "defaultSummary: \"<span class=\\\"font-weight-bold\\\"> %4$s </span> is an important for the pursuit of higher Education and Exam Preperation. Please review all the subjects and associated topics to perform well on teh <span class=\\\"font-weight-bold\\\"> %4$s </span> related Exams.\"\r\n"
			+ "defaultSummary: \"%4$s is an important for the pursuit of higher Education and Exam Preperation. Please review all the subjects and associated topics to perform well on teh %4$s related Exams.\"\r\n"
			+ metadataTemplateChannelFolderMenu
			+ "---\r\n"
			+ "";

	
	
	private static String metadataTemplateSubjectFolderMenu =
			"menu:\r\n"
			+ "  main:\r\n"
			+ "    identifier: \"%7$s-%6$s\"\r\n"
			+ "    name: \"%3$s\"\r\n"
			+ "    url: \"%7$s/%6$s\"\r\n"
			+ "    weight: %8$d\r\n"
			+ "    parent: \"%7$s\"\r\n"
			+ "  %6$s-submenu:\r\n"
			+ "    identifier: \"%7$s-%6$s\"\r\n"
			+ "    name: \"%3$s\"\r\n"
			+ "    url: \"%7$s/%6$s\"\r\n"
			+ "    weight: %8$d\r\n"
			+ "";

	private static String metadataTemplateSubjectFolder =
			"---\r\n"
			+ "title: \"%1$s\"\r\n"
			+ "topic: \"%2$s\"\r\n"
			+ "subject: \"%3$s\"\r\n"
			+ "channel: \"%4$s\"\r\n"
			+ "menuname: \"%9$s\"\r\n"
			+ "draft: false\r\n"
			+ "image: \"image/%7$s-%6$s.png\"\r\n"
//			+ "defaultSummary: \"<span class=\\\"font-weight-bold\\\"> %3$s </span> is an important Subject in the <span class=\\\"font-weight-bold\\\"> %4$s </span> course.  Please review all the topics and associated testing material to perform well on teh <span class=\\\"font-weight-bold\\\"> %4$s </span> related Exams.\"\r\n"
			+ "defaultSummary: \"%3$s is an important Subject in the %4$s course.  Please review all the topics and associated testing material to perform well on the %4$s related Exams.\"\r\n"
			+ metadataTemplateSubjectFolderMenu
			+ "---\r\n"
			+ "";
	
	private static String metadataTemplateTopicPageMenu =
			"menu:\r\n"
			+ "  %7$s-submenu:\r\n"
//			+ "    identifier: \"%5$s\"\r\n"
			+ "    identifier: \"%7$s-%6$s-%5$s\"\r\n"
			+ "    name: \"%2$s\"\r\n"
			+ "    url: \"%7$s/%6$s/%5$s\"\r\n"
			+ "    weight: %8$d\r\n"
			+ "";

	private static String metadataTemplateTopicPage =
			"---\r\n"
			+ "title: \"%1$s\"\r\n"
			+ "topic: \"%2$s\"\r\n"
			+ "subject: \"%3$s\"\r\n"
			+ "channel: \"%4$s\"\r\n"
			+ "tags: [%6$s, %7$s]\r\n"
			+ "categories: [%6$s, %7$s]\r\n"
			+ "menuname: \"%9$s\"\r\n"
			+ "draft: false\r\n"
			+ "image: \"image/%7$s-%6$s-%5$s.png\"\r\n"
//			+ "defaultSummary: \"<span class=\\\"font-weight-bold\\\"> %2$s </span> is an important Topic in the Subject of <span class=\\\"font-weight-bold\\\"> %3$s </span> for the <span class=\\\"font-weight-bold\\\"> %4$s </span> course.  Please review it thoroughly and attempt all the associated testing material to perform well on teh <span class=\\\"font-weight-bold\\\"> %4$s </span> related Exams.\"\r\n"
			+ "defaultSummary: \"%2$s is an important Topic in the Subject of %3$s for the %4$s course.  Please review it thoroughly and attempt all the associated testing material to perform well on the %4$s related Exams.\"\r\n"
			+ metadataTemplateTopicPageMenu
			+ "---\r\n"
			+ "";
	

	/**
	 * Main method.  Takes the target root directory as an argument
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args == null || args.length < 1) {
			System.err.println("Program needs a Folder to iterate on.  ");
			System.err.println("Usage: AddHugoEtesterHeaderMatter <directory/folder>");
			System.exit(-1);
		}
		System.out.println("Printing args: ");
		for (String arg : args) {
			System.out.println("arg: " + arg);
		} 

		// check to see if we have a createFolderIndexFiles=true flag as a second parameter
		// That will create a BLANK "_index.md" file at every folder root as needed 
		boolean createFolderIndexFilesAsNeeded = false;
		if (args.length >= 2 && args[1] != null && args[1].startsWith("createFolderIndexFiles")) {
			if (args[1].indexOf("=") != -1) {
				String createFolderIndexFilesStringValue = args[1].substring(args[1].indexOf("=") + 1);
				if (createFolderIndexFilesStringValue != null && "true".equalsIgnoreCase(createFolderIndexFilesStringValue.trim())) {
					createFolderIndexFilesAsNeeded = true;
				}
			}
		}
		final File folder = new File(args[0]);
		List<File> subFolderList = listSubFolders(folder);

		List<String> fileExtensions = new ArrayList<String>(Arrays.asList("index.html", "index.md"));


		List<File> subFolderListWithIndexHtmlFile = new ArrayList<File>();

		System.out.println("Here are all the Folders that are being processed.....................");
		if (subFolderList != null && subFolderList.size() > 0) {
			for (int index = 0; index < subFolderList.size() && index < 10000; index++) {
				File candidateFolder = subFolderList.get(index);
				System.out.println("Processing Folder: " + index + " : " + candidateFolder.getAbsolutePath());
				List<File> htmlFiles = listFilesWithExtension(candidateFolder, fileExtensions);
				if (htmlFiles != null && htmlFiles.size() > 0) {
					replaceMetaDataForIndexFile(htmlFiles.get(0));
					subFolderListWithIndexHtmlFile.add(candidateFolder);
				} else {
					if (createFolderIndexFilesAsNeeded) {
						// create a folder "_index.md"
						File indexFile = createIndexFile(candidateFolder, "_index.md");
						htmlFiles.add(indexFile);
					replaceMetaDataForIndexFile(indexFile);
						subFolderListWithIndexHtmlFile.add(candidateFolder);
					}
				}
			}
		} else {
			System.out.println("No Sub Folders in the Program CandidatePath!!!");
		}

	}

    private static File createIndexFile(File candidateFolder, String fileName) {
        try {
            // create new file
            File indexFile = new File(candidateFolder, fileName);
            
            // tries to create new file in the system
            boolean success = indexFile.createNewFile();
            
            // prints
            System.out.println("File created: " + success);
            
            return indexFile;
         } catch(Exception e) {
            e.printStackTrace();
         }
        
        return null;
		
	}



    
	/**
	 * Get a list of all subfolders (recursively) in the main folder
	 * @param folder
	 * @return
	 */
	public static List<File> listSubFolders(final File folder) {
		List fileList = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				fileList.add(fileEntry);
				fileList.addAll(listSubFolders(fileEntry));
			}
		}
		return fileList;
	}

	/**
	 * Find a list of files in teh selected folder that "end" with the given characters from amongst the list.
	 * Note that they do not need to be file extensions only like ".html."  They can also be full file names like "index.html"
	 * @param folder
	 * @param fileExtensions
	 * @return
	 */
	public static List<File> listFilesWithExtension(final File folder, List<String> fileExtensions) {
		List fileList = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
			} else {
				for (String extension : fileExtensions) {
					if (fileEntry.getName().endsWith(extension)) {
						fileList.add(fileEntry);
						break;
					}
				}
			}
		}
		return fileList;
	}

	/**
	 * Main method to replace the metadata for a given file.  
	 * This method is typically fed an "index.html" file or "index.md" file and used the parent folder for a "topic" and grand parent for a "subject"
	 * For example "C:\junk\documents3\Intermediate\Physics\Thermodynamics\index.html" would mean "Thermodynamics" for "topic" and "Physics" for subject. 
	 * @param candidateFile
	 * @return
	 */
	private static boolean replaceMetaDataForIndexFile(File candidateFile) {
		
		try {
			// 1.)  Read the file into a string
			String content = new String(Files.readAllBytes(Paths.get(candidateFile.getAbsolutePath())));
			
			// 2.)  See if there is an existing metadata header.  if so, strip it before proceeding.
			String newFileContent = content;
			// metadata for Docpad and Hugo, both start with the "yaml" delimiter of "---"
			String patternStartStr = "---";
			// metadata for Docpad and Hugo, both also END with the "yaml" delimiter of "---"
			String patternEndStr = "---";
			int startOfExistingMetadata = newFileContent.indexOf(patternStartStr, 0 );
			
			if (startOfExistingMetadata < 0 || startOfExistingMetadata > 10) {
				// the file either does not have a metadata segment or is not at teh beginning
				// SKIP
			} else {
				int endOfExistingMetadata = newFileContent.indexOf(patternEndStr, startOfExistingMetadata + 3);
				newFileContent = newFileContent.substring(endOfExistingMetadata + 3);
			}
			
			// 3.) Create the  metadata elements AND 3.1) call function to convert file/folder name (topic) to string with spaces
            String topic = candidateFile.getParentFile().getName();
            String topicSpaces = convertStringToSentence(topic);
            String subject = candidateFile.getParentFile().getParentFile().getName();
            String subjectSpaces = convertStringToSentence(subject);
            String channel = candidateFile.getParentFile().getParentFile().getParentFile().getName();
            String channelSpaces = convertStringToSentence(channel);
            String title;
            // HANDLE SOME SPECIAL CASE SCENARIO's that happen on Hugo _index.md files
            // For the Channel_Root _index.md
            
            boolean indexPage = false;
            boolean channelPage = false;
            boolean subjectPage = false;
            int baseMenuWeight = 1800;
            int menuWeight = baseMenuWeight;
            if (CHANNELS.contains(topic.toUpperCase())) {
            	// We are at Channel root
            	menuWeight += CHANNELS.indexOf(topic.toUpperCase());
            	channel = topic;
            	channelSpaces = topicSpaces;
            	subject = "";
            	subjectSpaces = "";
            	topic = "";
            	topicSpaces = "";
            	title = channelSpaces;
            	indexPage = true;
            	channelPage = true;
            } else if (CHANNELS.contains(subject.toUpperCase())) {
            	// We are at Subject root
            	menuWeight += 100;
            	channel = subject;
            	channelSpaces = subjectSpaces;
            	subject = topic;
            	subjectSpaces = topicSpaces;
            	topic = "";
            	topicSpaces = "";
            	title = subjectSpaces;
            	indexPage = true;
            	subjectPage = true;
            } else {
            	title = topicSpaces;
            }
            
            
           
			// 4.) Create and spit out the metadata to stdout
            StringWriter writer2 = new StringWriter();
            PrintWriter  writer = new PrintWriter(writer2);
            // print the formatted string
            // to this writer using printf() method
            // Note that we are sending subject, channel without spaces to set them in "Tags" and "Categories" arrays since Hugo may not render tags with spaces
            if (channelPage) {
            	String menuName = "main";
                writer.printf(metadataTemplateChannelFolder, title, topicSpaces, subjectSpaces, channelSpaces, topic, subject, channel, menuWeight, menuName);
                writer.flush();
            } else if (subjectPage) {
            	String menuName = channel + "-submenu";
                writer.printf(metadataTemplateSubjectFolder, title, topicSpaces, subjectSpaces, channelSpaces, topic, subject, channel, menuWeight, menuName);
                writer.flush();
            } else {
            	String menuName = channel + "-submenu";
                writer.printf(metadataTemplateTopicPage, title, topicSpaces, subjectSpaces, channelSpaces, topic, subject, channel, menuWeight, menuName);
                writer.flush();
            }
            
            String metadata = writer2.toString();
            System.out.println ("metadata for: " + candidateFile.getAbsolutePath());
            System.out.println (metadata);
            
            // 5.) Compose and write the file with metadata
			String response = metadata + newFileContent;
//			Files.write(Paths.get(candidateFile.getAbsolutePath()), response.getBytes());
			return true;
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
	}

	/**
	 * This function converts a Camel Case String into its component words, concats them with a single "space" and returns the concatenated string
	 * @param text
	 * @return
	 */
	private static String convertStringToSentence(String text) {
        Pattern WORD_FINDER = Pattern.compile("(([A-Z0-9]?[a-z]+)|([A-Z0-9]))");
        Matcher matcher = WORD_FINDER.matcher(text);
        List<String> words = new ArrayList<>();
        while (matcher.find()) {
            words.add(matcher.group(0));
        }
        return String.join(" ", words);
	}
}
