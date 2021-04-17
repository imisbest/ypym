package com.csw.controller;

import com.csw.dao.CloudFileDao;
import com.csw.dao.FolderDao;
import com.csw.entity.CloudFile;
import com.csw.entity.Folder;
import com.csw.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("vc")
public class ViewController {
    @Autowired
    private FolderDao folderDao;
    @Autowired
    private CloudFileDao cloudFileDao;

    @RequestMapping(value = "")
    public ModelAndView mainPage(HttpSession session) {
        ModelAndView mav = new ModelAndView();
        User user = (User) session.getAttribute("user");
        if (user == null)
            mav.setViewName("/index");
        else
            mav.setViewName("/view/main");
        return mav;
    }

    @RequestMapping("file")
    public ModelAndView filePage(@RequestParam("dir") Integer folderId, Model model, HttpSession session) {
        System.out.println("folderId="+folderId);
        ModelAndView mav = new ModelAndView();
        try {
            List<Folder> folderList;
            List<CloudFile> fileList;
            if (folderId == 0) {
                User user = (User) session.getAttribute("user");
                if (user == null) {
                    mav.setViewName("/index");
                    return mav;
                }
                //列出所有的文件和文件夹
                Folder rootFolder = folderDao.selectRootFolderByUserId(user.getId());
                folderList = folderDao.selectFolderListByFolderId(rootFolder.getId());
                fileList = cloudFileDao.selectFileListByFolderId(rootFolder.getId());
            } else {
                folderList = folderDao.selectFolderListByFolderId(folderId);
                fileList = cloudFileDao.selectFileListByFolderId(folderId);
                Folder fatherFolder = folderDao.selectFatherFolderById(
                        folderDao.selectFolderById(folderId).getParentFolder());
                model.addAttribute("fatherFolder", fatherFolder);
            }
            model.addAttribute("folderId", folderId);
            model.addAttribute("folderList", folderList);
            model.addAttribute("fileList", fileList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mav.setViewName("/view/file");
        return mav;
    }

    @RequestMapping("class")
    public ModelAndView photoPage(@RequestParam("type") String type, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        ModelAndView mav = new ModelAndView();
        try {
            if (user == null) {
                mav.setViewName("/index");
                return mav;
            }
            List<CloudFile> fileList = null;
            if (type.equals("photo"))
                fileList = cloudFileDao.selectPhotoByUserId(user.getId());
            else if (type.equals("document"))
                fileList = cloudFileDao.selectDocumentByUserId(user.getId());
            else if (type.equals("movie"))
                fileList = cloudFileDao.selectMovieByUserId(user.getId());
            else if (type.equals("music"))
                fileList = cloudFileDao.selectMusicByUserId(user.getId());
            else if (type.equals("zip"))
                fileList = cloudFileDao.selectZipByUserId(user.getId());
            model.addAttribute("fileList", fileList);
            mav.setViewName("/view/class");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mav;
    }

    @RequestMapping("public")
    public ModelAndView publicPage(Model model) {

        try {

            List<CloudFile> fileList = cloudFileDao.selectPublicFile();
            model.addAttribute("fileList", fileList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/view/public");
        return mav;
    }
}
