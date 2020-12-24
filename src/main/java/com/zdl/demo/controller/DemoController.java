package com.zdl.demo.controller;

import com.zdl.demo.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 服务站App控制器
 *
 * @author zhoudeli
 */
@Slf4j
@Controller
@RequestMapping("/")
public class DemoController {
    private DemoService demoService;

    /**
     * 下载, 方案1
     * -> Freemarker 將tpl模板生成动态html
     * -> ITextRenderer 转成Pdf
     * -> PDFRenderer 转成图片
     * -> 打包成zip 进行下载
     * 缺点 无法正确渲染样式
     */
    @RequestMapping("/download")
    public void download() {
        demoService.download();
    }

    /**
     * 下载, 方案2 Jsoup + cssbox
     * -> Freemarker 將tpl模板生成动态html
     * -> BrowserCanvas 转成图片
     * -> 打包成zip 进行下载
     * 缺点 无法正确渲染样式
     */
    @RequestMapping("/download2")
    public void download2() {
        demoService.download2();
    }

    /**
     * 下载, 方案3 需要使用到PDF库，Spire.PDF for Java 版本: 3.6.6
     * https://www.e-iceblue.cn/licensing/install-spirepdf-for-java-from-maven-repository.html
     * -> 直接将可访问页面渲染成图片
     * -> 打包成zip 进行下载
     * 缺点 无法正确渲染样式
     */
    @RequestMapping("/download3")
    public void download3() {
        demoService.download3();
    }
}
