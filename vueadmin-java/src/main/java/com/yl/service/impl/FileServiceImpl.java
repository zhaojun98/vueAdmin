package com.yl.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yl.service.FileService;
import com.yl.utils.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class FileServiceImpl implements FileService {
    public static final String ROOT = "upload";

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public String upload(MultipartFile file) throws IOException {
        File destFile = new File(Paths.get(ROOT).toAbsolutePath().toString());
        if (!destFile.exists()) {
            destFile.mkdirs();
        }
        String type = file.getOriginalFilename().substring(file.getOriginalFilename().indexOf("."));// 取文件格式后缀名
        String filename = System.currentTimeMillis() + "_" +  SignUtil.generateNonceStr() + type;// 取当前时间戳作为文件名
        File sourceFile = new File(Paths.get(ROOT, filename).toAbsolutePath().toString());
        file.transferTo(sourceFile);
        return filename;
    }

    public String upload(List<MultipartFile> files) throws Exception{
        if(files==null || files.size()==0){
            return null;
        }
        File destFile = new File(Paths.get(ROOT).toAbsolutePath().toString());
        if (!destFile.exists()) {
            destFile.mkdirs();
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(MultipartFile file: files){
            String originalFilename = file.getOriginalFilename();
            String type = originalFilename.substring(originalFilename.indexOf("."));// 取文件格式后缀名
            String filename = System.currentTimeMillis() + "_" +  SignUtil.generateNonceStr() + type;// 取当前时间戳作为文件名
            File sourceFile = new File(Paths.get(ROOT, filename).toAbsolutePath().toString());
//            file.transferTo(sourceFile);
            saveFile(file.getInputStream(),sourceFile);
            stringBuilder.append(";").append(filename);
//            replaceFileName(form,originalFilename,filename);
        }
        if(stringBuilder.length()<=0){
            return stringBuilder.toString();
        }
        return stringBuilder.substring(1);
    }

    private void replaceFileName(Object form,String originalFilename,String newName) throws IllegalAccessException {
        for(Field field : form.getClass().getDeclaredFields()){
            if(field.getName().toLowerCase().endsWith("img")){
                field.setAccessible(true);
                Object imageValue = field.get(form);
                if(imageValue!=null){
                    String oldValue = imageValue.toString();
                    if(oldValue!=null && oldValue.contains(originalFilename)){
                        field.set(form, oldValue.replace(originalFilename, newName));
                        break;
                    }
                }

            }
        }
    }


    public boolean deleteFile(String filename) {
        File file = null;
        try {
            file = resourceLoader.getResource("file:" + Paths.get(ROOT, filename).toString()).getFile();
        } catch (IOException e) {
            e.printStackTrace();
//            throw new ErrorException("删除图片失败: " + filename, e);
        }
        return file.delete();
    }


    public void deleteFiles(List<String> fileNames) {
        if(fileNames != null && fileNames.size() > 0){
            for(String filename: fileNames){
                deleteFile(filename);
            }
        }
    }

    private void saveFile(InputStream is, File dest) {
        try(FileOutputStream fos = new FileOutputStream(dest)) {
            int len;
            byte[] buffer = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //替换图片
    public void replaceImg(Object obj) throws IllegalAccessException, IOException {
        Field[] fields = obj.getClass().getDeclaredFields();
        JSONObject params = new JSONObject();
        for (Field field : fields) {
            if (field.getName().endsWith("Img")) {
                field.setAccessible(true);
                String name = field.getName().toUpperCase();
                String value = (String) field.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    params.put(name, value);
                }
            }
        }
        if (params.size() > 0) {
            for (String str : params.keySet()) {
                if (str.startsWith("OLD")) {
                    String name = str.substring(3, str.length());
                    Object value = params.get(str);
                    String[] oldFiles = value.toString().split(";");
                    Set<String> set = new HashSet<>(Arrays.asList(oldFiles));
                    for (String st : params.keySet()) {
                        if (st.equals(name)) {
                            Object va = params.get(st);
                            String[] file = va.toString().split(";");
                            for (String keep : file) {
                                if (set.contains(keep)) {
                                    set.remove(keep);
                                }
                            }
                            if (set.size() > 0) {
                                for (String del : set) {
                                    deleteFile(del);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    //替换图片(url结尾)
    public void replaceUrl(Object obj) throws IllegalAccessException, IOException {
        Field[] fields = obj.getClass().getDeclaredFields();
        JSONObject params = new JSONObject();
        for (Field field : fields) {
            if (field.getName().endsWith("Url")) {
                field.setAccessible(true);
                String name = field.getName().toUpperCase();
                String value = (String) field.get(obj);
                if (!StringUtils.isEmpty(value)) {
                    params.put(name, value);
                }
            }
        }
        if (params.size() > 0) {
            for (String str : params.keySet()) {
                if (str.startsWith("OLD")) {
                    String name = str.substring(3, str.length());
                    Object value = params.get(str);
                    String[] oldFiles = value.toString().split(",");
                    Set<String> set = new HashSet<>(Arrays.asList(oldFiles));
                    for (String st : params.keySet()) {
                        if (st.equals(name)) {
                            Object va = params.get(st);
                            String[] file = va.toString().split(",");
                            for (String keep : file) {
                                if (set.contains(keep)) {
                                    set.remove(keep);
                                }
                            }
                            if (set.size() > 0) {
                                for (String del : set) {
                                    deleteFile(del);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
