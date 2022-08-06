
package com.lixinming.aliyun;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ExtensionElements;
import org.camunda.bpm.model.bpmn.instance.UserTask;
import org.camunda.bpm.model.xml.Model;
import org.camunda.bpm.model.xml.impl.util.DomUtil;
import org.camunda.bpm.model.xml.instance.DomDocument;
import org.camunda.bpm.model.xml.instance.DomElement;
import org.camunda.bpm.model.xml.instance.ModelElementInstance;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

/**
 * @Author lixinming
 * @Date 2022/7/8 13:49
 * @Version 1.0
 */
public class Camunda {
    public static void main(String[] args) throws Exception {
        //step3:把需要解析的xml文件加载到一个document对象中
        /*SAXReader sr = new SAXReader();
        System.out.println(11);
        File files = new File("C:\\Users\\lixinming\\Desktop\\告警.bpmn");
        //step1:获得DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //step2:获得DocumentBuilder
        DocumentBuilder db = factory.newDocumentBuilder();
        //step3:把需要解析的xml文件加载到一个document对象中
        Document document = db.parse(files);

        NodeList elementsByTagName = document.getElementsByTagName("zeebe:userTaskForm");*/
        File files = new File("C:\\Users\\lixinming\\Desktop\\告警.bpmn");
        InputStream input = new FileInputStream(files);
        //byte[] bytes = new byte[input.available()];
        //step1:获得DocumentBuilderFactory
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //step2:获得DocumentBuilder
        DocumentBuilder db = factory.newDocumentBuilder();
        Document document1 = db.newDocument();


        //ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        BpmnModelInstance modelInstance = Bpmn.readModelFromStream(input);

        UserTask u = modelInstance.getModelElementById("Activity_1ytbhwr");
        ExtensionElements extensionElements = u.getExtensionElements();

        Collection<ModelElementInstance> elements = extensionElements.getElements();

        for (ModelElementInstance modelElementInstance : elements){
            //获取节点信息
            DomElement domElement = modelElementInstance.getDomElement();
            String localName = domElement.getLocalName();
            String namespaceURI = domElement.getNamespaceURI();
            //获取需要得节点ID
            String attribute = domElement.getAttribute("aaa");
            System.out.println(localName);
            System.out.println(namespaceURI);
            DomDocument document = domElement.getDocument();
            DOMSource domSource = document.getDomSource();
            //获取camunda得NODE
            Node node = domSource.getNode();
            if(StringUtils.equals(localName,"formDefinition")){
                parseElement(node,"userTaskForm");
            }
        }
    }

    private static void parseElement(Node ele,String part) {
        // 获取子标签
        NodeList childNodes = ele.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                // 递归处理子标签
                parseElement((Element) childNode,part);
            } else if (childNode.getNodeType() != Node.COMMENT_NODE) {
                String localName = childNode.getParentNode().getLocalName();
                if(StringUtils.equals(part,localName)){
                    NamedNodeMap attributes = childNode.getParentNode().getAttributes();
                    Node namedItem = attributes.getNamedItem("id");
                    String nodeValue = namedItem.getNodeValue();
                    System.out.println("id===="+nodeValue);
                    System.out.println("value===="+childNode.getNodeValue());
                }
            }
        }
    }

}
