package cug.school.sketchingtools.common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by Mr_Bai on 2016/1/18.
 */
public class XmlSave {

    public String Save_as_XmlString(int id, String notes, String type) {
        String xmlString = "";
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            //一级标签,xml文件的根标签
            Element rootElement = document.createElement("Information");
            document.appendChild(rootElement);
            //二级标签，用户id
            Element userId = document.createElement("id");
            rootElement.appendChild(userId);
            userId.appendChild(document.createTextNode(String.valueOf(id)));
            //二级标签，文档注释
            Element xmlNotes = document.createElement("notes");
            rootElement.appendChild(xmlNotes);
            xmlNotes.appendChild(document.createTextNode(notes));
            //二级标签，文档时间
            Element xmlTime = document.createElement("time");
            rootElement.appendChild(xmlTime);
            xmlTime.appendChild(document.createTextNode("time"));
            //二级标签，实体
            Element xmlEntities = document.createElement("entity");
            rootElement.appendChild(xmlEntities);
            //三级标签，实体类型
            Element entityType = document.createElement("type");
            xmlEntities.appendChild(entityType);
            entityType.appendChild(document.createTextNode(type));
            //三级标签，实体坐标
            Element entityPoint = document.createElement("point");
            xmlEntities.appendChild(entityPoint);
            entityPoint.appendChild(document.createTextNode(""));

            //格式转换工厂,将xml转换为String类型
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            //设置输出的格式
            Properties properties = new Properties();
            //缩进
            properties.setProperty(OutputKeys.INDENT, "yes");
            //输出格式
            properties.setProperty(OutputKeys.METHOD, "xml");
            //xml声明
            properties.setProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            //xml版本
            properties.setProperty(OutputKeys.VERSION, "1.0");
            //xml编码格式
            properties.setProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperties(properties);
            //获得输入
            DOMSource domSource = new DOMSource(document.getDocumentElement());
            //字节输出
            OutputStream outputStream = new ByteArrayOutputStream();
            //字节输出结果
            StreamResult streamResult = new StreamResult(outputStream);
            //转换doucunmen对象
            transformer.transform(domSource, streamResult);
            //从ByteArrayOutputStream对象获取String对象
            xmlString = outputStream.toString();
        } catch (ParserConfigurationException e) {

        } catch (TransformerConfigurationException e) {

        } catch (TransformerException e) {

        }
        return xmlString;
    }
}
