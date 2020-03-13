package com.zs.create.base.controller;

import com.zs.create.base.util.FileHelperUtil;
import com.zs.create.common.api.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @Author lingrui
 * @since 2018-12-20
 */
@Slf4j
@RestController
@RequestMapping("/sys/common")
public class CommonController {

    @Value(value = "${zsxc.path.upload}")
    private String uploadpath;

    @Value(value = "${zsxc.path.doc}")
    private String docpath;
    /**
     * @return
     * @Author lingrui
     */
    @GetMapping("/403")
    public Result<?> noauth() {
        return Result.error("没有权限，请联系管理员授权");
    }

    @PostMapping(value = "/upload")
    public Result<?> upload(HttpServletRequest request, HttpServletResponse response) {
        return FileHelperUtil.upload(uploadpath, request);
    }


    /**
     * 预览图片
     * 请求地址：http://localhost:8080/common/view/{user/20190119/e1fe9925bc315c60addea1b98eb1cb1349547719_1547866868179.jpg}
     *
     * @param request
     * @param response
     */
    @GetMapping(value = "/view/**")
    public void view(HttpServletRequest request, HttpServletResponse response) {
        FileHelperUtil.view(uploadpath, request, response);
    }

    /**
     * 下载文件
     * 请求地址：http://localhost:8080/common/download/{user/20190119/e1fe9925bc315c60addea1b98eb1cb1349547719_1547866868179.jpg}
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/download/**")
    public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
        FileHelperUtil.download(uploadpath, request, response);
    }

    @GetMapping(value = "/downloaddoc/**")
    public void downloadEmail(HttpServletRequest request, HttpServletResponse response) throws Exception {
        FileHelperUtil.download(docpath, request, response);
    }



    /**
     * @param modelAndView
     * @return
     * @功能：pdf预览Iframe
     */
    @RequestMapping("/pdf/pdfPreviewIframe")
    public ModelAndView pdfPreviewIframe(ModelAndView modelAndView) {
        modelAndView.setViewName("pdfPreviewIframe");
        return modelAndView;
    }

    /**
     * 实时磁盘监控
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/queryDiskInfo")
    public Result<List<Map<String, Object>>> queryDiskInfo(HttpServletRequest request, HttpServletResponse response) {
        Result<List<Map<String, Object>>> res = new Result<>();
        try {
            // 当前文件系统类
            FileSystemView fsv = FileSystemView.getFileSystemView();
            // 列出所有windows 磁盘
            File[] fs = File.listRoots();
            log.info("查询磁盘信息:" + fs.length + "个");
            List<Map<String, Object>> list = new ArrayList<>();

            for (int i = 0; i < fs.length; i++) {
                if (fs[i].getTotalSpace() == 0) {
                    continue;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("name", fsv.getSystemDisplayName(fs[i]));
                map.put("max", fs[i].getTotalSpace());
                map.put("rest", fs[i].getFreeSpace());
                map.put("restPPT", fs[i].getFreeSpace() * 100 / fs[i].getTotalSpace());
                list.add(map);
                log.info(map.toString());
            }
            res.setResult(list);
            res.success("查询成功");
        } catch (Exception e) {
            res.error500("查询失败" + e.getMessage());
        }
        return res;
    }


    @GetMapping(value = "/task/process")
    public String taskProcess() {
        return FileHelperUtil.readJson("classpath:com/zs/create/modules/demo/mock/json/task_process.json");
    }


}
