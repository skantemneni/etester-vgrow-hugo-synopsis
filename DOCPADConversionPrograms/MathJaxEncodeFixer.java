package com.etester.hugo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathJaxEncodeFixer {

	public static void main(String[] args) {
		System.out.println("Lord Shiva is Great!!!");
		System.out.println("Printing args: ");
		for (String arg : args) {
			System.out.println("arg: " + arg);
		}

		final File folder = new File(args[0]);
		List<String> fileExtensions = new ArrayList<String>(Arrays.asList(".html",".md"));
		List<File> fileList = listFilesForFolder(folder, fileExtensions);

		int filesWithLatex = 0;
		for (int i = 0; i < fileList.size() && i < 10000; i++) {
			try {
				boolean fileHasLatex = mathJaxEncodeFileInPlace(fileList.get(i), i);
				if (fileHasLatex) {
					filesWithLatex++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
		}
		System.out.println("Total Files with Latex: " + filesWithLatex);

//		String input = "This is a test <span> whatsup</span> jhnjksa <span     lang=   \"latex\"     > \\frac{b}{a} = \\frac{d}{c} </span> 2222 <span lang =  \\\"latex\\\"> \\\\frac{1}{2} = \\\\frac{3}{4} </span>  <span> whatsend</span>33";
//		String output = defaultMathJaxEncode(input);
//		System.out.println("Input:	" + input);
//		System.out.println("Output:	" + output);

	}

	public static List<File> listFilesForFolder(final File folder, List<String> fileExtensions) {
		List fileList = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				fileList.addAll(listFilesForFolder(fileEntry, fileExtensions));
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

	private static boolean mathJaxEncodeFileInPlace(File file, int index) throws IOException {
		boolean fileWithLatex = false;
		String filePath = file.getAbsolutePath();
		System.out.println("Processing : " + index + " : " + filePath);
		String content = new String(Files.readAllBytes(Paths.get(filePath)));
		String newFileContent = content;
//		newFileContent = defaultRenameCenterHTMLTags(newFileContent);
		newFileContent = addLineBreaksToSupportMathJax3(newFileContent);
		newFileContent = defaultMathJaxEncode(newFileContent);
		try {
			Files.write(Paths.get(filePath), newFileContent.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Function to replace "<center>" tahgs in html and replace with
	 * "<div class="synopsis-image"> "
	 * 
	 * @param mathJaxEncode
	 * @return
	 */
	private static String defaultRenameCenterHTMLTags(String mathJaxEncode) {
		String patternStartStr = "<center\\s*>";
		String startReplacement = "<div class=\"synopsis-image\"> ";

		String patternEndStr = "</center\\s*>";
		String endReplacement = " </div>";

		return htmlTagReplace(mathJaxEncode, patternStartStr, patternEndStr, startReplacement, endReplacement);
	}

	/**
	 * String to be encoded with MathJax begin and end delimiters
	 * 
	 * @param mathJaxEncode
	 * @return
	 */
	private static String defaultMathJaxEncode(String inputString) {
		String patternStartStr = "<span\\s*lang=\\s*\"latex\"\\s*>";
		String patternEndStr = "</span\\s*>";

		String startReplacement = "<span class=\\\"latex\\\" lang=\\\"latex\\\"> \\( ";
		String endReplacement = " \\) </span>";

		return htmlTagReplace(inputString, patternStartStr, patternEndStr, startReplacement, endReplacement);
	}

//	/**
//	 * Function to add a class to "<span lang="latex">". They will now be
//	 * "<span class="latex" lang="latex">"
//	 * 
//	 * @param mathJaxEncode
//	 * @return
//	 */
//	private static String addClassToLatexSpans(String inputString) {
//		String patternStartStr = "<span\\s*lang\\s*=\\s*\"latex\"\\s*>";
//		String startReplacement = "<span class=\"latex\" lang=\"latex\">";
//
//		String patternEndStr = null;
//		String endReplacement = null;
//
//		return htmlTagReplace(inputString, patternStartStr, patternEndStr, startReplacement, endReplacement);
//	}

	/**
	 * This method takes an input string and replaces "patternStartStr" and the
	 * immediate next "patternEndStr" with "startReplacement" and "endReplacement"
	 * 
	 * @param input            - Input string
	 * @param patternStartStr
	 * @param patternEndStr
	 * @param startReplacement
	 * @param endReplacement
	 * @return String - teh new output string with the replaces done.
	 */
	private static String htmlTagReplace(String input, String patternStartStr, String patternEndStr,
			String startReplacement, String endReplacement) {
		String output = input;

		List<String> tokens = new ArrayList<String>();
		Pattern patternStart = Pattern.compile(patternStartStr);
		Pattern patternEnd = null;
		if (patternEndStr != null && patternEndStr.trim().length() > 0) {
			patternEnd = Pattern.compile(patternEndStr);
		}

		String inputString = input;
		boolean continueParse = true;
//		boolean mathJaxSpanOpened = false;
		while (continueParse) {
			Matcher matcherStart = patternStart.matcher(inputString);
			if (matcherStart.find(0)) {
//				mathJaxSpanOpened = true;
				int startStart = matcherStart.start();
				int startEnd = matcherStart.end();
				System.out.println("Start: startStart : " + startStart + ", startEnd : " + startEnd);
				tokens.add(inputString.substring(0, startStart));
				if (startReplacement != null && startReplacement.trim().length() > 0) {
					tokens.add(startReplacement);
				}
				inputString = inputString.substring(startEnd);

				if (patternEnd != null) {
					Matcher matcherEnd = patternEnd.matcher(inputString);
					if (matcherEnd.find(0)) {
						int endStart = matcherEnd.start();
						int endEnd = matcherEnd.end();
						System.out.println("End: endStart : " + endStart + ", endEnd : " + endEnd);
						String htmlContentWithinSpan = inputString.substring(0, endStart);
						tokens.add(htmlContentWithinSpan);
//						// Do I want to transform in any way.  
//						// Looks like line breaks are not being honored in MathJax 3.0 yet.  I will call out a transform to fix that.
//						if (contentTransformFromString != null && contentTransformFromString.trim().length() > 0) {
//							String transformedHtmlContent = htmlContentTransform(htmlContentWithinSpan, contentTransformFromString, contentTransformToString);
//							tokens.add(transformedHtmlContent);
//						} else {
//							tokens.add(htmlContentWithinSpan);
//						}
						if (endReplacement != null && endReplacement.trim().length() > 0) {
							tokens.add(endReplacement);
						}
						inputString = inputString.substring(endEnd);
					}
				}

			} else {
				continueParse = false;
			}
		}
		tokens.add(inputString);

		output = String.join("", tokens);
		return output;
	}

	/**
	 * Function to add a class to "<span lang="latex">". They will now be
	 * "<span class="latex" lang="latex">"
	 * 
	 * @param mathJaxEncode
	 * @return
	 */
	private static String addLineBreaksToSupportMathJax3(String input) {
		String output = input;
		// https://stackoverflow.com/questions/4025482/cant-escape-the-backslash-with-regex
		// If you're putting this in a string within a program, you may actually need to use four backslashes (because the string parser will remove two of them when "de-escaping" it for the string, and then the regex needs two for an escaped regex backslash)
		String patternStartStr = "<span\\s*lang=\\s*\"latex\"\\s*>";
		String patternEndStr = "</span\\s*>";


		String contentTransformFromString = "\\\\\\\\";
		String contentTransformToString = " \\\\\\\\";
		String encloseStartString = " \\begin{align} ";
		String encloseEndString = " \\end{align} ";

		List<String> tokens = new ArrayList<String>();
		Pattern patternStart = Pattern.compile(patternStartStr);
		Pattern patternEnd = Pattern.compile(patternEndStr);

		String inputString = input;
		boolean continueParse = true;
		while (continueParse) {
			Matcher matcherStart = patternStart.matcher(inputString);
			if (matcherStart.find(0)) {
				int startEnd = matcherStart.end();
				System.out.println("Start: startEnd : " + startEnd);
				tokens.add(inputString.substring(0, startEnd));
				inputString = inputString.substring(startEnd);

				Matcher matcherEnd = patternEnd.matcher(inputString);
				if (matcherEnd.find(0)) {
					int endStart = matcherEnd.start();
					System.out.println("End: endStart : " + endStart);
					String htmlContentWithinSpan = inputString.substring(0, endStart);
					// Do I want to transform in any way.
					String transformedHtmlContent = htmlContentTransform(htmlContentWithinSpan,
							contentTransformFromString, contentTransformToString, encloseStartString, encloseEndString);
					tokens.add(transformedHtmlContent);
					inputString = inputString.substring(endStart);
				}

			} else {
				continueParse = false;
			}
		}
		tokens.add(inputString);
		output = String.join("", tokens);

		return output;

	}

	private static String htmlContentTransform(String htmlContentWithinSpan, String contentTransformFromString,
			String contentTransformToString, String encloseStartString, String encloseEndString) {
		String output = htmlContentWithinSpan;
		List<String> tokens = new ArrayList<String>();
		Pattern patternStart = Pattern.compile(contentTransformFromString);

		String inputString = htmlContentWithinSpan;
		boolean continueParse = true;
		while (continueParse) {
			Matcher matcherStart = patternStart.matcher(inputString);
			if (matcherStart.find(0)) {
//				mathJaxSpanOpened = true;
				int startStart = matcherStart.start();
				int startEnd = matcherStart.end();
				System.out.println("htmlContentWithinSpanStart: startStart : " + startStart
						+ ", htmlContentWithinSpanEnd : " + startEnd);
				tokens.add(inputString.substring(0, startStart));
				if (contentTransformToString != null && contentTransformToString.trim().length() > 0) {
					tokens.add(contentTransformToString);
				}
				inputString = inputString.substring(startEnd);
			} else {
				continueParse = false;
			}
		}
		
		if (tokens.size() > 0) {
			tokens.add(inputString);
			if (encloseStartString != null && encloseStartString.trim().length() > 0) {
				tokens.add(0, encloseStartString);
			}
			if (encloseEndString != null && encloseEndString.trim().length() > 0) {
				tokens.add(encloseEndString);
			}
			output = String.join("", tokens);
		} 

		// TODO Auto-generated method stub
		return output;
	}

}
