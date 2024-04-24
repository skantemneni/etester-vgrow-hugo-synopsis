package com.etester.hugo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileRenameToIndexHtml {

	public static void main(String[] args) throws IOException {
		System.out.println("Lord Shiva is Great!!!");
		System.out.println("Printing args: ");
		for (String arg : args) {
			System.out.println("arg: " + arg);
		}

		final File folder = new File(args[0]);
		List<File> subFolderList = listSubFolders(folder);

		List<String> fileExtensions = new ArrayList<String>(Arrays.asList(".html", ".md"));


		List<File> subFolderListWithHtmlFiles = new ArrayList<File>();
		List<File> subFoldersWithHtmlFileNameSameAsFolderName = new ArrayList<File>();
		List<File> subFoldersWithNOHtmlFileNameSameAsFolderName = new ArrayList<File>();

		System.out.println("Here are all the Folders that are being processed.....................");
		if (subFolderList != null && subFolderList.size() > 0) {
			for (int index = 0; index < subFolderList.size() && index < 10000; index++) {
				File candidateFolder = subFolderList.get(index);
				System.out.println("Processing Folder: " + index + " : " + candidateFolder.getAbsolutePath());
				List<File> htmlFiles = listFilesWithExtension(candidateFolder, fileExtensions);
				if (htmlFiles != null && htmlFiles.size() > 0) {
					subFolderListWithHtmlFiles.add(candidateFolder);
					if (subFoldersWithHtmlFileNameSameAsFolderName(candidateFolder, htmlFiles)) {
						subFoldersWithHtmlFileNameSameAsFolderName.add(candidateFolder);
						// "COPY foldername.HTML File to index.HTML for Folders that have HTML Files same name as the FolderName.....................
						copyFolderFileNametoIndexHtml(candidateFolder, htmlFiles);
					} else {
						subFoldersWithNOHtmlFileNameSameAsFolderName.add(candidateFolder);
					}
				}
			}
		} else {
			System.out.println("No Sub Folders in the Program CandidatePath!!!");
		}


		System.out.println("Here are all the Folders that have HTML Files.....................");
		if (subFolderListWithHtmlFiles != null && subFolderListWithHtmlFiles.size() > 0) {
			for (int index = 0; index < subFolderListWithHtmlFiles.size() && index < 10000; index++) {
				File candidateFolder = subFolderListWithHtmlFiles.get(index);
				System.out.println("SubfolderWithHTMLFiles: " + index + " : " + candidateFolder.getAbsolutePath());
			}
		} else {
			System.out.println("No Sub Folders with files with given Extension: " + fileExtensions.toString());
		}
		
		
		System.out.println("Here are all the Folders that have HTML Files same name as the FolderName.....................");
		if (subFoldersWithHtmlFileNameSameAsFolderName != null && subFoldersWithHtmlFileNameSameAsFolderName.size() > 0) {
			for (int index = 0; index < subFoldersWithHtmlFileNameSameAsFolderName.size() && index < 10000; index++) {
				File candidateFolder = subFoldersWithHtmlFileNameSameAsFolderName.get(index);
				System.out.println("subFoldersWithHtmlFileNameSameAsFolderName: " + index + " : " + candidateFolder.getAbsolutePath());
			}
		} else {
			System.out.println("No Sub Folders WithHtmlFileNameSameAsFolderName: " + fileExtensions.toString());
		}
		
		
		System.out.println("Here are all the Folders that DO NOT have HTML Files same name as the FolderName.....................");
		if (subFoldersWithNOHtmlFileNameSameAsFolderName != null && subFoldersWithNOHtmlFileNameSameAsFolderName.size() > 0) {
			for (int index = 0; index < subFoldersWithNOHtmlFileNameSameAsFolderName.size() && index < 10000; index++) {
				File candidateFolder = subFoldersWithNOHtmlFileNameSameAsFolderName.get(index);
				System.out.println("subFoldersWithNOHtmlFileNameSameAsFolderName: " + index + " : " + candidateFolder.getAbsolutePath());
			}
		} else {
			System.out.println("No Sub Folders subFoldersWithNOHtmlFileNameSameAsFolderName: " + fileExtensions.toString());
		}
		
		System.out.println("COPY foldername.HTML File to index.HTML for Folders that have HTML Files same name as the FolderName.....................");
		if (subFoldersWithHtmlFileNameSameAsFolderName != null && subFoldersWithHtmlFileNameSameAsFolderName.size() > 0) {
			for (int index = 0; index < subFoldersWithHtmlFileNameSameAsFolderName.size() && index < 10000; index++) {
				File candidateFolder = subFoldersWithHtmlFileNameSameAsFolderName.get(index);
				System.out.println("subFoldersWithHtmlFileNameSameAsFolderName: " + index + " : " + candidateFolder.getAbsolutePath());
			}
		} else {
			System.out.println("No Sub Folders WithHtmlFileNameSameAsFolderName: " + fileExtensions.toString());
		}
		
	}

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

	public static boolean subFoldersWithHtmlFileNameSameAsFolderName(final File folder, List<File> files) {
		String folderName = folder.getName();
		for (final File file : files) {
			String fileNameWithExtension = file.getName();
			String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.indexOf("."));
			if (fileName.equalsIgnoreCase(folderName)) {
				return true;
			}
		}
		return false;
	}

	public static void copyFolderFileNametoIndexHtml(final File folder, List<File> files) throws IOException {
		String folderName = folder.getName();
		for (final File file : files) {
			String fileNameWithExtension = file.getName();
			String fileName = fileNameWithExtension.substring(0, fileNameWithExtension.indexOf("."));
			if (fileName.equalsIgnoreCase(folderName)) {
				Files.copy(Path.of(file.getPath()),  Path.of(folder.getPath(), "index.html"), StandardCopyOption.REPLACE_EXISTING);
			}
		}
	}

}
