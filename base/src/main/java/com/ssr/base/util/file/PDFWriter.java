package com.ssr.base.util.file;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.net.FileRetrieve;
import com.itextpdf.tool.xml.net.ReadingProcessor;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.ImageProvider;
import com.ssr.base.util.CommonUtils;

/**
 * 导出PDF文件公共类
 * 
 * @author pl
 *
 */
public class PDFWriter {
	
	private Logger logger = Logger.getLogger(getClass());
	
	private String HTML_HEAD;//html头
	private String HTML_HEAD_TAG;//html尾
	private String HTML_FONT;//html字体
	
	public PDFWriter() throws Exception {
		this.HTML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				+ "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
				+ "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>"
				+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>"
				+ "<style type=\"text/css\" mce_bogus=\"1\">body {font-family: SimSun;}</style></head><body>";
		this.HTML_HEAD_TAG = "</body></html>";
		this.HTML_FONT = "resources/font";
	}

	

	/**
	 * 导出PDF公共方法
	 * 
	 * @param content ：导出内容
	 * @param fileName  ：导出PDF文件名称(不需要后缀)
	 * @param req ：HttpServletRequest
	 * @param resp：HttpServletResponse
	 * @throws Exception
	 */
	public void write(String content, String fileName, HttpServletRequest req, HttpServletResponse resp) throws Exception {
		fileName = CommonUtils.isEmpty(fileName) ? System.currentTimeMillis() + "" : fileName;
		resp.setHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
		resp.setContentType("application/pdf");
		
		StringBuilder sb = new StringBuilder(HTML_HEAD);
		sb.append(content).append(HTML_HEAD_TAG);
		
		Document document = new Document(PageSize.A4, 30, 30, 30, 30);
		document.setMargins(30, 30, 30, 30);
		PdfWriter writer = PdfWriter.getInstance(document, resp.getOutputStream());
		document.open();
		HtmlPipelineContext htmlContext = new HtmlPipelineContext(new CssAppliersImpl(
				new XMLWorkerFontProvider(req.getServletContext().getRealPath("/") + HTML_FONT){
			@Override
			public Font getFont(String fontname, String encoding,
					float size, final int style) {
				if (fontname == null) {
					fontname = "宋体";
				}
				return super.getFont(fontname, encoding, size, style);
			}
		})){
			@Override
			public HtmlPipelineContext clone() throws CloneNotSupportedException {
				HtmlPipelineContext context = super.clone();
				try {
					ImageProvider imageProvider = this.getImageProvider();
					context.setImageProvider(imageProvider);
				} catch (Exception e) {
					logger.error("生成PDF文件创建HtmlPipelineContext出错!", e);
				}
				return context;
			}
			
		};
		htmlContext.setImageProvider(new AbstractImageProvider() {
			@Override
			public String getImageRootPath() {
				return req.getServletContext().getRealPath("/");
			}
			@Override
			public Image retrieve(String src) {
				if(null == src || "".equals(src)){
					return null;
				}
				String path = req.getContextPath();
				if(src.startsWith(path)){
					src = src.substring(path.length());
					try {
						Image image = Image.getInstance(new File(req.getServletContext().getRealPath("/"), src).toURI().toString());
						if (image != null) {
							store(src, image);
							return image;
						}
					} catch (Exception e) {
						logger.error("生成PDF文件获取HTML图片资源出错!", e);
					}
				}
				return super.retrieve(src);
			}
			
		});
		htmlContext.setAcceptUnknown(true).autoBookmark(true).setTagFactory(Tags.getHtmlTagProcessorFactory());
		CSSResolver cssResolver = XMLWorkerHelper.getInstance().getDefaultCssResolver(true);
		cssResolver.setFileRetrieve(new FileRetrieve() {
			@Override
			public void processFromHref(String href, ReadingProcessor processor) throws IOException {
				if(null == href || "".equals(href)){
					return;
				}
				String path = req.getContextPath();
				if(href.startsWith(path)){
					href = href.substring(path.length());
					try {
						InputStreamReader reader = new InputStreamReader(req.getServletContext().getResourceAsStream(href), "UTF-8");
						int i = -1;
						while (-1 != (i = reader.read())) {
							processor.process(i);
						}
					} catch (Exception e) {
						logger.error("生成PDF文件获取HTML链接资源出错!", e);
					}
				}
			}
			@Override
			public void processFromStream(InputStream in, ReadingProcessor processor) throws IOException {
				try {
					InputStreamReader reader = new InputStreamReader(in, "UTF-8");
					int i = -1;
					while (-1 != (i = reader.read())) {
						processor.process(i);
					}
				} catch (Exception e) {
					logger.error("生成PDF文件获取HTML链接资源流出错!", e);
				}
			}
		});
		HtmlPipeline htmlPipeline = new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer));
		Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
		XMLWorker worker = null;
		try {
			worker = new XMLWorker(pipeline, true);
			XMLParser parser = new XMLParser(true, worker, Charset.forName("UTF-8"));
			ByteArrayInputStream  bi = new ByteArrayInputStream(sb.toString().getBytes());
			parser.parse(bi, Charset.forName("UTF-8"));
			document.close();
		} catch (Exception e) {
			logger.error("生成PDF文件输出文件流出错!", e);
		} finally {
			worker.close();
		}
	}
}
