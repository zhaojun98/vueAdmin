package com.yl.controller;


import com.yl.common.CommonResultVo;
import com.yl.common.exception.CustomException;
import com.yl.service.FileService;
import com.yl.utils.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.List;

import static com.yl.service.impl.FileServiceImpl.ROOT;


/**
 * @author ：jerry
 * @date ：Created in 2021/10/26 下午2:37
 * @description：文件
 * @version: V1.1
 */

@Api(tags = "文件服务管理")
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private FileService fileService;


    @ApiOperation("上传")
    @PostMapping(value = "/upload")
    public CommonResultVo upload(HttpServletRequest request) throws IOException {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("files");
        StringBuilder address = new StringBuilder();
        if (files != null && files.size() > 0) {
            for (MultipartFile file : files) {
                address.append(fileService.upload(file)).append(";");
            }
        }
        if (address.length() > 0) {
            return CommonResultVo.success((Object) address.substring(0, address.length() - 1));
        }
        throw new CustomException("路径和文件名错误");
    }

//    @MyLog(value = "文件删除")
    @ApiOperation("文件删除")
    @GetMapping(value = "/delete/{filename}")
    public CommonResultVo delete(@PathVariable("filename") String filename) throws IOException {
        return CommonResultVo.success(fileService.deleteFile(filename));
    }

//    @MyLog(value = "批量删除文件")
    @ApiOperation("批量删除文件")
    @GetMapping(value = "/deleteBatch")
    public CommonResultVo delete(@RequestParam("filenames") List<String> filenames) throws IOException {
        fileService.deleteFiles(filenames);
        return null;
    }

    @ApiOperation("查看文件")
    @GetMapping(value = "/show/{filename:.+}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filename", value = "文件名", required = true, dataType = "String", example = "ok.jpg"),
            @ApiImplicitParam(name = "height", value = "图片高度", required = false, dataType = "Integer", example = "100"),
            @ApiImplicitParam(name = "width", value = "图片宽度", required = false, dataType = "Integer", example = "100"),
            @ApiImplicitParam(name = "outputQuality", value = "输出质量(越大清晰度就越高, 0~1)", required = false, dataType = "Float", example = "0.8"),
    })
    public void loadFile(@PathVariable("filename") String filename, Integer height, Integer width, Float outputQuality,
                         HttpServletResponse response)  {
        BufferedInputStream in = null;
        ByteArrayOutputStream bos = null;
        try {
            File file = resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()).getFile();
            bos = new ByteArrayOutputStream();
            String suf = FileUtil.getSuffix(filename).toLowerCase();
            //开启图片压缩
            if (height != null && width != null && outputQuality != null) {
                Thumbnails.of(file).forceSize(width, height).outputQuality(outputQuality).toOutputStream(response.getOutputStream());
                return;
            }
            in = new BufferedInputStream(new FileInputStream(file));
            int buf_size = 1024;
            byte[] buffer = new byte[buf_size];
            int len;
            while (-1 != (len = in.read(buffer, 0, buf_size))) {
                bos.write(buffer, 0, len);
            }
            if (suf.contains("png") || suf.contains("jpg") || suf.contains("gif") || suf.contains("JPEG")) {
                response.setContentType("image/png");
            } else {
                response.setHeader("content-type", "application/octet-stream");
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename));
            }
            response.getOutputStream().write(bos.toByteArray());
        } catch (IOException e) {
            throw new CustomException("文件【" + filename + "】不存在");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



}

