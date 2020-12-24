package com.zdl.demo.service.impl;

import com.google.common.collect.Lists;
import com.spire.pdf.htmlconverter.qt.HtmlConverter;
import com.zdl.demo.entity.BaseParamEntity;
import com.zdl.demo.service.DemoService;
import com.zdl.demo.utils.FreemarkerUtil;
import com.zdl.demo.utils.JavaToPdfHtmlFreeMarker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Autowired
    private FreemarkerUtil freemarkerUtil;
    @Autowired
    private JavaToPdfHtmlFreeMarker javaToPdfHtmlFreeMarker;
    @Override
    public void download() {
        String fileName = "下載二維碼";
        httpServletResponse.setContentType("application/zip");
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("文件名解析错误, {}", e);
        }
        httpServletResponse.setHeader("Content-disposition", String.format("attachment; filename=%s.zip", fileName));
        try {
            OutputStream outputStream = httpServletResponse.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

            try {
                BaseParamEntity baseParamEntity = new BaseParamEntity();
                baseParamEntity.setName("測試");
                List<BufferedImage> imageList = javaToPdfHtmlFreeMarker.turnImage("qrcode_station.ftl", baseParamEntity);
                for (int i = 0; i < imageList.size(); i++) {
                    BufferedImage bufferedImage = imageList.get(i);
                    ZipEntry newEntry = new ZipEntry(String.format("%s_%d.PNG", baseParamEntity.getName(), i));
                    zipOutputStream.putNextEntry(newEntry);
                    ImageIO.write(bufferedImage, "PNG", zipOutputStream);
                }
            } catch (Exception e) {
                log.error("转图片异常, {}", e);
            }

            zipOutputStream.flush();
            zipOutputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("文件名解析错误, {}", e);
        }
    }

    @Override
    public void download2() {
        String fileName = "下載二維碼";
        httpServletResponse.setContentType("application/zip");
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("文件名解析错误, {}", e);
        }
        httpServletResponse.setHeader("Content-disposition", String.format("attachment; filename=%s.zip", fileName));
        try {
            OutputStream outputStream = httpServletResponse.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

            try {
                BaseParamEntity baseParamEntity = new BaseParamEntity();
                baseParamEntity.setName("測試");
                List<BufferedImage> imageList = Lists.newArrayList(freemarkerUtil.turnImage("qrcode_station.ftl", baseParamEntity));
                for (int i = 0; i < imageList.size(); i++) {
                    BufferedImage bufferedImage = imageList.get(i);
                    ZipEntry newEntry = new ZipEntry(String.format("%s_%d.PNG", baseParamEntity.getName(), i));
                    zipOutputStream.putNextEntry(newEntry);
                    ImageIO.write(bufferedImage, "PNG", zipOutputStream);
                }
            } catch (Exception e) {
                log.error("转图片异常, {}", e);
            }

            zipOutputStream.flush();
            zipOutputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("文件名解析错误, {}", e);
        }
    }

    @Override
    public void download3() {
        String fileName = "station";
        httpServletResponse.setContentType("application/zip");
        httpServletResponse.setCharacterEncoding("UTF-8");
        try {
            fileName = URLEncoder.encode("到处", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("文件名解析错误, {}", e);
        }
        httpServletResponse.setHeader("Content-disposition", String.format("attachment; filename=%s.zip", fileName));
        try {
            OutputStream outputStream = httpServletResponse.getOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

            try {
                String url = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&rsv_idx=1&tn=baidu&wd=BufferedImage%20%E6%88%AA%E5%8F%96&fenlei=256&oq=pdf%2520%25E8%25BD%25AC%25E5%259B%25BE%25E7%2589%2587%2520%25E5%258E%25BB%25E6%258E%2589%25E9%25A6%2596%25E8%25A1%258C&rsv_pq=a8ed05860009fe73&rsv_t=a3aa3JuJT8J5hM4apC2gL6CZWbc1fKuzB5DCYv0yDhmwVGKM0rJ%2FrpzHRtc&rqlang=cn&rsv_dl=tb&rsv_enter=1&rsv_btype=t&inputT=7761&rsv_n=2&rsv_sug3=157&rsv_sug1=63&rsv_sug7=100&rsv_sug2=0&rsv_sug4=7761";

                //解压后的插件本地地址（这里是把插件包放在了Java项目文件夹下，也可以自定义其他本地路径）
                String pluginPath = "D:\\Program Files\\plugins-windows-x64";
                HtmlConverter.setPluginPath(pluginPath);

                //调用方法转换到PDF并设置PDF尺寸
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                HtmlConverter.convert(url, outStream);
                List<BufferedImage> imageList = javaToPdfHtmlFreeMarker.pdfToImg(outStream.toByteArray());
                for (int i = 0; i < imageList.size(); i++) {
                    BufferedImage bufferedImage = imageList.get(i);
                    ZipEntry newEntry = new ZipEntry(String.format("%s %s-%s-%d.PNG", "fdsf", "fdfds", "fsfsdfds", i));
                    zipOutputStream.putNextEntry(newEntry);
                    ImageIO.write(bufferedImage, "PNG", zipOutputStream);
                }
            } catch (Exception e) {
                log.error("转图片异常, {}", e);
            }

            zipOutputStream.flush();
            zipOutputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("下载服务站二维码异常, {}", e);
        }
    }
}
