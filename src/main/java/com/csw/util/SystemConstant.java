package com.csw.util;

import java.io.File;

public class SystemConstant 
{
	public static final String USER_FILE_PATH = "C:\\Tomcat\\CloudData";

	public static String toSQLString(String path) 
	{
		return path.replace("\\", "\\\\\\\\");
	}

	public static boolean deleteDir(File dir) 
	{
		if (dir.isDirectory()) 
		{
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) 
			{
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success)
				{
					return false;
				}
			}
		}
		return dir.delete();
	}
	
	public static boolean isValidFileName(String fileName) 
	{
		if (fileName == null || fileName.length() > 255) 
			return false; 
		else 
			return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
	}

}
