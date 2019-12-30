package doc;

import com.power.common.util.DateTimeUtil;
import com.power.doc.builder.AdocDocBuilder;
import com.power.doc.builder.ApiDocBuilder;
import com.power.doc.model.ApiConfig;
import org.junit.Test;

/**
 * @author 杨戬
 * @className SmartDoc
 * @email uncle.yeung.bo@gmail.com
 * @date 19-11-14 14:48
 */
public class SmartDocTest {
    public static final String TARGET_DOC_OUT_PATH = "target/doc";
    public static final String DOCS_OUT_PATH = "src/docs/asciidoc";
    public static final String MARKDWON_OUT_PATH = "./md/DocumentInterface/";


    /**
     * 包括设置请求头，缺失注释的字段批量在文档生成期使用定义好的注释
     *
     */
    @Test
    public void testBuilderControllersApiHtml() {
        ApiConfig config = new ApiConfig();
        config.setServerUrl("http://172.16.19.194/");
        //config.setAllInOne(true);
        //设置用md5加密html文件名,不设置为true，html的文件名将直接为controller的名称
        config.setMd5EncryptedHtmlName(true);
        config.setStrict(true);//true会严格要求注释，推荐设置true
        config.setOutPath(TARGET_DOC_OUT_PATH);//输出到static/doc下
        //不指定SourcePaths默认加载代码为项目src/main/java下的,如果项目的某一些实体来自外部代码可以一起加载
        long start = System.currentTimeMillis();
        //HtmlApiDocBuilder.builderControllersApi(config);//此处使用HtmlApiDocBuilder，ApiDocBuilder提供markdown能力
        ApiDocBuilder.builderControllersApi(config);
        //AdocDocBuilder.builderControllersApi(config);
        long end = System.currentTimeMillis();
        DateTimeUtil.printRunTime(end, start);
    }

    /**
     * 包括设置请求头，缺失注释的字段批量在文档生成期使用定义好的注释
     */
    @Test
    public void testBuilderControllersApiAdoc() {
        ApiConfig config = new ApiConfig();
        config.setServerUrl("http://172.16.19.194/");
        config.setAllInOne(true);
        //设置用md5加密html文件名,不设置为true，html的文件名将直接为controller的名称
        config.setMd5EncryptedHtmlName(true);
        config.setStrict(true);//true会严格要求注释，推荐设置true
        config.setOutPath(DOCS_OUT_PATH);//输出到static/doc下
        //不指定SourcePaths默认加载代码为项目src/main/java下的,如果项目的某一些实体来自外部代码可以一起加载
        long start = System.currentTimeMillis();
        //HtmlApiDocBuilder.builderControllersApi(config);//此处使用HtmlApiDocBuilder，ApiDocBuilder提供markdown能力
        AdocDocBuilder.builderControllersApi(config);
        long end = System.currentTimeMillis();
        DateTimeUtil.printRunTime(end, start);
    }

    @Test
    public void testBuilderControllersApiMarkdown() {
        ApiConfig config = new ApiConfig();
        config.setServerUrl("http://172.16.19.194/");
        //config.setAllInOne(true);
        //设置用md5加密html文件名,不设置为true，html的文件名将直接为controller的名称
        config.setMd5EncryptedHtmlName(true);
        config.setStrict(true);//true会严格要求注释，推荐设置true
        config.setOutPath(MARKDWON_OUT_PATH);//输出到static/doc下
        //不指定SourcePaths默认加载代码为项目src/main/java下的,如果项目的某一些实体来自外部代码可以一起加载
        long start = System.currentTimeMillis();
        //HtmlApiDocBuilder.builderControllersApi(config);//此处使用HtmlApiDocBuilder，ApiDocBuilder提供markdown能力
        ApiDocBuilder.builderControllersApi(config);
        //AdocDocBuilder.builderControllersApi(config);
        long end = System.currentTimeMillis();
        DateTimeUtil.printRunTime(end, start);
    }
}
