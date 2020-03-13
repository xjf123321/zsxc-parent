package com.zs.create.modules.system.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhuozhengsoft.pageoffice.FileSaver;
import com.zhuozhengsoft.pageoffice.OpenModeType;
import com.zhuozhengsoft.pageoffice.PageOfficeCtrl;
import com.zhuozhengsoft.pageoffice.wordreader.DataRegion;
import com.zhuozhengsoft.pageoffice.wordreader.WordDocument;
import com.zs.create.modules.system.entity.*;
import com.zs.create.modules.system.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @Description :Office在线编辑、预览API
 */
@Controller()
@RequestMapping("/office")
public class OfficePageController {

   /* @Value("${posyspath}")
    private String filePath;*/

    @Value(value = "${zsxc.path.upload}")
    private String uploadpath;

    @Autowired
    private ZsDocTemplatService zsDocTemplatService;

    @Autowired
    private ZsPathService zsPathService;

    @Autowired
    private ZsNoticeTemplatService zsNoticeTemplatService;

    @Autowired
    private ZsReturnPathService zsReturnPathService;

    @Autowired
    private ZsDocRecordService zsDocRecordService;

    @Autowired
    private ZsSqNoticeService zsSqNoticeService;


    /**
     * 在线word文档编辑
     * @param request
     * @param map
     * @return
     */

    @GetMapping("/word")
        public ModelAndView showWord(HttpServletRequest request, Map<String, Object> map, @RequestParam(name = "id") String id, @RequestParam(name = "type", required = false) String type) throws FileNotFoundException {

        ModelAndView mv = new ModelAndView("word");

        ZsDocTemplate template = zsDocTemplatService.getById(id);
        String addr = template.getAddr();
        if (addr.contains("/")) {
            addr = addr.replace("/", "\\");
        }
        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
        //设置授权程序servlet
        poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");
        //添加自定义按钮

        if (!"1".equals(type)) {
            poCtrl.addCustomToolButton("保存", "Save", 1);
        }
        //保存按钮接口地址
        poCtrl.setSaveFilePage(request.getContextPath() + "/office/save");
        poCtrl.setCaption("WJOA");
        //打开文件(打开的文件类型由OpenModeType决定，docAdmin是一个word，并且是管理员权限，如果是xls文件，则使用openModeType.xls开头的,其他的office格式同等)，最后一个参数是作者
        // TODO 这里有个坑，这里打开的文件是本地的，地址如果写成/结构的路径，页面就会找不到文件，会从http://xxxxx/G/id...去找，写成\\就是从本地找
        QueryWrapper<ZsPath> qw = new QueryWrapper<>();
        qw.eq("type", 0);
        ZsPath zsPath = zsPathService.getOne(qw);
        String returnPath = zsPath.getPath() + addr;
        try {
            if (!"1".equals(type)) {
                poCtrl.webOpen(returnPath, OpenModeType.docAdmin, template.getTemplateName());
            } else {
                poCtrl.webOpen(returnPath, OpenModeType.docReadOnly, template.getTemplateName());
            }
            //pageoffice 是文件的变量，前端页面通过这个变量加载出文件
            map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
            mv.addObject("url",  "word?id=" + id);
            //跳转到word.html
            return mv;
        } catch (Exception e) {
            e.printStackTrace();
            mv.setViewName("error");
            return mv;
        }


    }

    @GetMapping("/wordTemplate")
    public ModelAndView wordTemplate(HttpServletRequest request,
                                     HttpServletResponse response,
                                     Map<String, Object> map,
                                     @RequestParam(name = "id", required = false) String id,
                                     @RequestParam(name = "type", required = false) String type,
                                     @RequestParam(name = "userId", required = false) String userId) throws FileNotFoundException, UnsupportedEncodingException {
        ModelAndView mv = new ModelAndView("word");
        String addr = URLDecoder.decode(id, "UTF-8");
        if (addr.contains("/")) {
            addr = addr.replace("/", "\\");
        }
        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
        //设置授权程序servlet
        poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");
        poCtrl.setCaption("WJOA");

        QueryWrapper<ZsPath> qw = new QueryWrapper<>();
        qw.eq("type", 3);
        ZsPath zsPath = zsPathService.getOne(qw);
        String returnPath = zsPath.getPath() + addr;
        try {
            if (type !=null && !"".equals(type)) {
                poCtrl.addCustomToolButton("保存", "Save", 1);

                poCtrl.setSaveFilePage(request.getContextPath() + "/office/save?id=" + id + "&userId=" + userId + "&type=1&status="+type);
                poCtrl.webOpen(returnPath, OpenModeType.docAdmin, "");

                //签章
               /* poCtrl.addCustomToolButton("签章", "Seal", 2);
                com.zhuozhengsoft.pageoffice.wordwriter.WordDocument doc = new com.zhuozhengsoft.pageoffice.wordwriter.WordDocument();
                com.zhuozhengsoft.pageoffice.wordwriter.DataRegion dataRegion = doc.openDataRegion("PO_AA");
                dataRegion.setValue("[image]C:\\Users\\My\\Desktop\\新建文件夹\\zs\\123.png[/image]");
                poCtrl.setWriter(doc);*/


            } else {
                poCtrl.webOpen(returnPath, OpenModeType.docReadOnly, "");
            }
            //pageoffice 是文件的变量，前端页面通过这个变量加载出文件
            map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
            //跳转到word.html
            return mv;
        } catch (Exception e) {
            e.printStackTrace();
            mv.setViewName("error");
            return mv;
        }


    }




    @GetMapping("/wordNotice")
    public ModelAndView wordNotice(HttpServletRequest request, Map<String, Object> map, @RequestParam(name = "id") String id, @RequestParam(name = "type", required = false) String type) throws FileNotFoundException {
        ModelAndView mv = new ModelAndView("word");

        ZsNoticeTemplate template = zsNoticeTemplatService.getById(id);
        String addr = template.getAddr();
        if (addr.contains("/")) {
            addr = addr.replace("/", "\\");
        }

        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
        //设置授权程序servlet
        poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");
        //添加自定义按钮
        if (!"1".equals(type)) {
            poCtrl.addCustomToolButton("保存", "Save", 1);
        }
        //保存按钮接口地址
        poCtrl.setSaveFilePage(request.getContextPath() + "/office/save");
        //打开文件(打开的文件类型由OpenModeType决定，docAdmin是一个word，并且是管理员权限，如果是xls文件，则使用openModeType.xls开头的,其他的office格式同等)，最后一个参数是作者
        // TODO 这里有个坑，这里打开的文件是本地的，地址如果写成/结构的路径，页面就会找不到文件，会从http://xxxxx/G/id...去找，写成\\就是从本地找
        QueryWrapper<ZsPath> qw = new QueryWrapper<>();
        qw.eq("type", 0);
        ZsPath zsPath = zsPathService.getOne(qw);
        String returnPath = zsPath.getPath() + addr;
        try {
            if (!"1".equals(type)) {
                poCtrl.webOpen(returnPath, OpenModeType.docAdmin, template.getTemplateName());
            } else {
                poCtrl.webOpen(returnPath, OpenModeType.docReadOnly, template.getTemplateName());
            }
            //pageoffice 是文件的变量，前端页面通过这个变量加载出文件
            map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
            mv.addObject("url",  "wordNotice?id=" + id);
            //跳转到word.html
            return mv;
        } catch (Exception e) {
            e.printStackTrace();
            mv.setViewName("error");
            return mv;
        }
    }


    @GetMapping("/noticeTemplate")
    public ModelAndView noticeTemplate(HttpServletRequest request, Map<String, Object> map,
                                       @RequestParam(name = "id", required = false) String id,
                                       @RequestParam(name = "type", required = false) String type,
                                       @RequestParam(name = "userId", required = false) String userId) throws FileNotFoundException, UnsupportedEncodingException {
        ModelAndView mv = new ModelAndView("word");
        String addr = URLDecoder.decode(id, "UTF-8");
        if (addr.contains("/")) {
            addr = addr.replace("/", "\\");
        }
        PageOfficeCtrl poCtrl = new PageOfficeCtrl(request);
        //设置授权程序servlet
        poCtrl.setServerPage(request.getContextPath() + "/poserver.zz");
        QueryWrapper<ZsPath> qw = new QueryWrapper<>();
        qw.eq("type", 3);
        ZsPath zsPath = zsPathService.getOne(qw);
        String returnPath = zsPath.getPath() + addr;
        try {
            if (type !=null && !"".equals(type)) {
                poCtrl.addCustomToolButton("保存", "Save", 1);

                poCtrl.setSaveFilePage(request.getContextPath() + "/office/save?id=" + id + "&userId=" + userId + "&type=2&status="+type);
                poCtrl.webOpen(returnPath, OpenModeType.docAdmin, "");
            } else {
                poCtrl.webOpen(returnPath, OpenModeType.docReadOnly, "");
            }
            //pageoffice 是文件的变量，前端页面通过这个变量加载出文件
            map.put("pageoffice", poCtrl.getHtmlCode("PageOfficeCtrl1"));
            //跳转到word.html
            return mv;
        } catch (Exception e) {
            e.printStackTrace();
            mv.setViewName("error");
            return mv;
        }
    }



    /**
     * 保存文件接口
     *
     * @param request
     * @param response
     */

    @RequestMapping("/save")
    public void saveFile(HttpServletRequest request,
                         HttpServletResponse response,
                         @RequestParam(name = "id", required = false)String id,
                         @RequestParam(name = "userId", required = false)String userId,
                         @RequestParam(name = "type", required = false)String type,
                         @RequestParam(name = "status", required = false)String status) {

        response.setContentType("text/html;charset=UTF-8");
        // 保存修改后的文件
        FileSaver fs = new FileSaver(request, response);
        QueryWrapper<ZsPath> qw = new QueryWrapper<>();
        qw.eq("type", 1);
        ZsPath one = zsPathService.getOne(qw);
        String nowday = new SimpleDateFormat("yyyyMMdd").format(new Date());
        File file = new File(one.getPath() + File.separator + nowday);
        if (!file.exists()) {
            file.mkdirs(); // 创建文件根目录
        }

        String filename = fs.getFileName().substring(0, fs.getFileName().lastIndexOf("_") + 1) + System.currentTimeMillis() + fs.getFileName().substring(fs.getFileName().indexOf("."));
        String path = filename.replace(" ", "");

        //fs.saveToFile(file + File.separator + path);

        if (id != null && !"".equals(id)) {
            fs.saveToFile(file + File.separator + fs.getFileName());
            if ("1".equals(type)) {
                ZsDocRecord zsDocRecord = zsReturnPathService.updateUrl(id, nowday + File.separator + fs.getFileName());
                if ("2".equals(status)) {
                    zsDocRecordService.coApproval(zsDocRecord, userId);
                }
            } else if ("2".equals(type)) {
                ZsSqNotice zsSqNotice = zsReturnPathService.updateNotice(id, nowday + File.separator + fs.getFileName());
                if ("2".equals(status)) {
                    zsSqNoticeService.coApproval(zsSqNotice, userId);
                }
            }

        } else {
            fs.saveToFile(file + File.separator + path);
            ZsReturnPath zsReturnPath = new ZsReturnPath();
            zsReturnPath.setUrl(nowday + File.separator + path);
            zsReturnPathService.save(zsReturnPath);
            fs.setCustomSaveResult(zsReturnPath.getId());
        }
        fs.close();
    }






}
