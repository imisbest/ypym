package com.csw.dao;

import com.csw.entity.Folder;
import com.csw.entity.User;

import java.util.List;

public interface FolderDao {
	public void addFolder(Folder folder);
	public void addRootFolder(User user);
	public List<Folder> selectFolderListByFolderId(Integer folderId);
	public Folder selectRootFolderByUserId(Integer userId);
	public Folder selectFolderById(Integer folderId);
	public Folder selectFatherFolderById(Integer folderId);
	public void deleteFolderById(Integer id);
	public List<Folder> selectAllFolderByFatherFolder(Folder fatherFolder);
}
