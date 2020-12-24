package com.zdl.demo.utils;

import com.google.common.collect.Lists;
import com.zdl.demo.entity.BaseParamEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * Created by lujianing on 2017/5/7.
 */
@Slf4j
@Component
public class JavaToPdfHtmlFreeMarker {
    @Autowired
    private FreemarkerUtil freemarkerUtil;

    public List<BufferedImage> turnImage(String template, BaseParamEntity paramEntity) throws Exception {
        String htmlTemplate = freemarkerUtil.getTemplate(template, paramEntity);
        return createPdfImg(htmlTemplate);
    }

    public List<BufferedImage> createPdfImg(String content) throws IOException, com.lowagie.text.DocumentException {
        ITextRenderer render = new ITextRenderer();
        render.setDocumentFromString(content);
        render.layout();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        render.createPDF(outStream);
        outStream.flush();
        outStream.close();
        return pdfToImg(outStream.toByteArray());
    }

    /**
     * 根据pdf二进制文件 生成图片文件
     *
     * @param bytes pdf二进制
     */
    public List<BufferedImage> pdfToImg(byte[] bytes) {
        List<BufferedImage> imageList = Lists.newArrayList();
        PDDocument pdDocument;
        try {
            pdDocument = PDDocument.load(bytes);
            PDFRenderer renderer = new PDFRenderer(pdDocument);
            int pageCount = pdDocument.getNumberOfPages();
            for (int i = 0; i < pageCount; i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 144);
                if (Objects.nonNull(image)) {
                    /* 可截取图片*/
                    imageList.add(image.getSubimage(0, 20, image.getWidth(), image.getHeight() - 20));
                }
            }
        } catch (IOException e) {
            log.error("Pdf 转图片异常, {}", e);
        }
        return imageList;
    }
}
