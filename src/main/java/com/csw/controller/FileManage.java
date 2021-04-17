package com.csw.controller;

import com.csw.dao.CloudFileDao;
import com.csw.dao.FolderDao;
import com.csw.entity.CloudFile;
import com.csw.entity.Folder;
import com.csw.entity.User;
import com.csw.util.SystemConstant;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("fm")
public class FileManage {

    @Autowired
    private FolderDao folderDao;
    @Autowired
    private CloudFileDao cloudFileDao;

    @RequestMapping("upload")
    public String FileUpload(MultipartFile[] fileList,
                             Integer folderId, Model model, HttpSession session) throws IOException {
        System.out.println("folderId="+folderId);
        if (fileList.length == 0) {
            model.addAttribute("文件为空");
            return "/vc/file?dir=" + folderId;
        }
            User user = (User) session.getAttribute("user");
            Folder folder;
            if (folderId==null)
                folder = folderDao.selectRootFolderByUserId(user.getId());
            else
                folder = folderDao.selectFolderById(folderId);
        System.out.println("folder="+folder);
            String uploadPath = SystemConstant.USER_FILE_PATH + folder.getPath() + "\\" + folder.getName();
            for (MultipartFile files : fileList) {
                String name = files.getOriginalFilename();
                int size = (int) (files.getSize() / 1024);
                if (size == 0)
                    size = 1;
                int sub = files.getOriginalFilename().lastIndexOf('.');
                Map<String, Object> params = new HashMap<>();
                params.put("fileName", name);
                params.put("userId", user.getId());
                params.put("dirId", folder.getId());
                Integer sameNamefile = cloudFileDao.selectFileByFileName(params).size();
                if (sameNamefile > 0) {
                    name += ("(" + sameNamefile + ")");
                }
                CloudFile cloudFile = new CloudFile(name, size,
                        files.getOriginalFilename().substring(sub + 1).toLowerCase(), folder, user);
                cloudFileDao.addFile(cloudFile);
                File uploadFile = new File(uploadPath, name);
                files.transferTo(uploadFile);
            }
        return "/vc/file?dir=" + folderId;
    }

    @RequestMapping("folderAdd")
    public String addFolder(@RequestParam("folderName") String folderName, @RequestParam("path") String path,
                            HttpSession session, HttpServletRequest request, Model model) {
        if (folderName == null || folderName.equals("")) {
            model.addAttribute("sysMsg", "文件名为空");
            return "/vc/file?dir=0";
        }
        if (!SystemConstant.isValidFileName(folderName)) {
            model.addAttribute("sysMsg", "文件名不合法");
            return "/vc/file?dir=0";
        }
        
        Integer folderId = Integer.valueOf(path);
            User user = (User) session.getAttribute("user");
            Folder fatherFolder, newFolder;
            if (folderId == 0)
                fatherFolder = folderDao.selectRootFolderByUserId(user.getId());
            else
                fatherFolder = folderDao.selectFolderById(folderId);
            newFolder = new Folder(folderName, fatherFolder.getId(), user, fatherFolder.getPath()
                    + fatherFolder.getName() + "\\");
            String realPath = SystemConstant.USER_FILE_PATH;
            File realFolder = new File(realPath + fatherFolder.getPath() + fatherFolder.getName()
                    + "\\" + folderName);
            if (!realFolder.exists())
                realFolder.mkdirs();
            folderDao.addFolder(newFolder);
        return "/vc/file?dir=" + folderId;
    }
    @RequestMapping("download")
    public ResponseEntity<byte[]> fileDownload(@RequestParam("file") Integer fileId,
                                               @RequestParam("folder") Integer folderId, HttpSession session) throws IOException {
        ResponseEntity<byte[]> responseEntity = null;
            User user = (User) session.getAttribute("user");
            Folder folder;
            if (folderId == 0)
                folder = folderDao.selectRootFolderByUserId(user.getId());
            else
                folder = folderDao.selectFolderById(folderId);
            CloudFile cloudFile = cloudFileDao.selectFileById(fileId);
            if (!Objects.equals(cloudFile.getUploadUser().getId(), user.getId()) && cloudFile.getStatus() == CloudFile.STATUS_PRIVATE) {
                return null;
            }
            String path = SystemConstant.USER_FILE_PATH + folder.getPath() + folder.getName() + "\\" + cloudFile.getName();
            File file = new File(path);
            HttpHeaders headers = new HttpHeaders();
            String fileName = new String(cloudFile.getName().getBytes("UTF-8"), "ISO-8859-1");
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            responseEntity = new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
        return responseEntity;
    }

    @RequestMapping("delete")
    public String fileDelete(@RequestParam("file") Integer fileId, @RequestParam("folder") Integer folderId,
                             HttpSession session, Model model) {
            User user = (User) session.getAttribute("user");
            Folder folder;
            if (folderId == 0)
                folder = folderDao.selectRootFolderByUserId(user.getId());
            else
                folder = folderDao.selectFolderById(folderId);
            CloudFile cloudFile = cloudFileDao.selectFileById(fileId);
            String path = SystemConstant.USER_FILE_PATH + folder.getPath() + folder.getName() +
                    "\\" + cloudFile.getName();
            File file = new File(path);
            if (file.exists())
                file.delete();
            cloudFileDao.deleteFileById(fileId);
        return "/vc/file?dir=" + folderId;
    }

    @RequestMapping("deleteDir")
    public String folderDelete(@RequestParam("folder") Integer folderId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
            Folder folder = folderDao.selectFolderById(folderId);
            if (folder.getCreateUser().getId() != user.getId()) {
                model.addAttribute("sysMsg", "两次获取到的用户id不一致");
                return "/index.jsp";
            }
            File target = new File(SystemConstant.USER_FILE_PATH + "\\" + folder.getPath() + folder.getName());
            folder.setPath(SystemConstant.toSQLString(folder.getPath()));
            List<Folder> targetFolder = folderDao.selectAllFolderByFatherFolder(folder);
            cloudFileDao.deleteFileByFolderId(folder.getId());
            for (Folder f : targetFolder) {
                cloudFileDao.deleteFileByFolderId(f.getId());
                folderDao.deleteFolderById(f.getId());
            }
            folderDao.deleteFolderById(folderId);
            if (target.exists()) {
                SystemConstant.deleteDir(target);
                
            } 
        return "/vc/file?dir=0";
    }

    @RequestMapping("setAccess")
    public String accessFile(@RequestParam("folder") Integer folderId, @RequestParam("file") Integer fileId,
                             Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            model.addAttribute("sysMsg", "用户名为空");
            return "/index";
        }
        
            if (folderId == 0)
                folderDao.selectRootFolderByUserId(user.getId());
            else
                folderDao.selectFolderById(folderId);

            CloudFile target = cloudFileDao.selectFileById(fileId);
            Map<String, Object> params = new HashMap<>();
            if (target.getStatus() == CloudFile.STATUS_PRIVATE) {
                params.put("access", CloudFile.STATUS_PUBLIC);
            } else {
                params.put("access", CloudFile.STATUS_PRIVATE);
            }
            params.put("fileId", target.getId());
            cloudFileDao.changeFileAccessByFileId(params);
        return "/vc/file?dir=" + folderId;
    }
}
