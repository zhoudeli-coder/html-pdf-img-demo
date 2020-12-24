package com.zdl.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zdl.demo.entity.BaseParamEntity;
import cz.vutbr.web.css.MediaSpec;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.layout.BrowserCanvas;
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Map;

@Component
public class FreemarkerUtil {

    private static String mediaType = "screen";
    private static Dimension windowSize = new Dimension(1086, 756);
    private static boolean cropWindow = false;
    private static boolean loadImages = true;
    private static boolean loadBackgroundImages = true;
    @Autowired
    FreeMarkerConfigurer freeMarkerConfigurer;

    public String getTemplate(String templateUrl, BaseParamEntity paramEntity) throws IOException, TemplateException {
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        Template template = configuration.getTemplate(templateUrl);
        StringWriter stringWriter = new StringWriter();
        Map<String, Object> ben2Map =
                JSON.parseObject(JSON.toJSONString(paramEntity, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteMapNullValue), Map.class);
        ben2Map.put("name", paramEntity.getName());

        template.process(ben2Map, stringWriter);
        stringWriter.flush();
        stringWriter.close();
        return stringWriter.getBuffer().toString();
    }

    public BufferedImage turnImage(String template, BaseParamEntity paramEntity) throws Exception {
        String htmlTemplate = getTemplate(template, paramEntity);
        Document document = Jsoup.parse(htmlTemplate);
        W3CDom w3CDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(document);
        DOMAnalyzer domAnalyzer = new DOMAnalyzer(w3cDoc, null);
        MediaSpec media = new MediaSpec(mediaType);
        media.setDimensions((float) windowSize.width, (float) windowSize.height);
        media.setDeviceDimensions((float) windowSize.width, (float) windowSize.height);
        domAnalyzer.setMediaSpec(media);
        domAnalyzer.attributesToStyles();
        domAnalyzer.addStyleSheet(null, loadCssStyle(), DOMAnalyzer.Origin.AGENT);
        domAnalyzer.getStyleSheets();
        BrowserCanvas contentCanvas = new BrowserCanvas(w3cDoc.getDocumentElement(), domAnalyzer, null);
        contentCanvas.setFont(loadFont());
        contentCanvas.setAutoMediaUpdate(false);
        contentCanvas.getConfig().setClipViewport(cropWindow);
        contentCanvas.getConfig().setLoadImages(loadImages);
        contentCanvas.getConfig().setLoadBackgroundImages(loadBackgroundImages);
        contentCanvas.createLayout(this.windowSize);
        return contentCanvas.getImage();
    }

    private Font loadFont() {
        ClassPathResource classPathResource = new ClassPathResource("/templates/css/Alibaba-PuHuiTi-Medium.ttf");
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, classPathResource.getInputStream());
            return font;
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String loadCssStyle() {
        ClassPathResource classPathResource = new ClassPathResource("/templates/css/style.css");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(classPathResource.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder builder = new StringBuilder();
        String line = "";
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            builder.append(line + "\n");
        }
        return builder.toString();
    }
}
