package com.ssr.base.util.xml;

import java.io.ByteArrayInputStream;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlUtil {

	public static String createAuthXml(BuildingPower bp) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		Element common = root.addElement("common");
		Element building = common.addElement("building_id");
		building.setText(bp.getBuilding());
		Element gateway = common.addElement("gateway_id");
		gateway.setText(bp.getGateway());
		Element type = common.addElement("type");
		//request
		type.setText(bp.getType());

		return document.asXML();
	}
	
	public static String createMd5Xml(BuildingPower bp) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		Element common = root.addElement("common");
		Element building = common.addElement("building_id");
		building.setText(bp.getBuilding());
		Element gateway = common.addElement("gateway_id");
		gateway.setText(bp.getGateway());
		Element type = common.addElement("type");
		//md5
		type.setText(bp.getType());
		
		Element idValidate = root.addElement("id_validate");
		idValidate.addAttribute("operation", "md5");
		Element md5 = idValidate.addElement("md5");
		md5.setText(bp.getMd5());
		
		return document.asXML();
	}
	
	public static String createHeartBeatXml(BuildingPower bp) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		Element common = root.addElement("common");
		Element building = common.addElement("building_id");
		building.setText(bp.getBuilding());
		Element gateway = common.addElement("gateway_id");
		gateway.setText(bp.getGateway());
		Element type = common.addElement("type");
		//notify
		type.setText(bp.getType());

		return document.asXML();
	}
	
	public static String createDataXml(BuildingPower bp) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		Element common = root.addElement("common");
		Element building = common.addElement("building_id");
		building.setText(bp.getBuilding());
		Element gateway = common.addElement("gateway_id");
		gateway.setText(bp.getGateway());
		Element eType = common.addElement("type");
		//reply
		eType.setText(bp.getType());
		
		Element data = root.addElement("data");
		data.addAttribute("operation", "query/reply/report/continuous/continuous_ack");
		
		Element eSequence = data.addElement("sequence");
		eSequence.setText(bp.getSequence());
		Element parser = data.addElement("parser");
		//no
		parser.setText(bp.getParser());
		Element time = data.addElement("time");
		time.setText(bp.getTime());
		Element total = data.addElement("total");
		total.setText(bp.getTotal());
		Element current = data.addElement("current");
		current.setText(bp.getCurrent());
		
		Element meter = data.addElement("meter");
		//暂时都打包为一个数据上传,不处理数据分页上传处理
		meter.addAttribute("id", "1");
		
		int count = 0;
		for(Map<String, String> m : bp.getMeters()){
			count++;
			Element function = meter.addElement("function");
			function.addAttribute("id", String.valueOf(count));
			function.addAttribute("coding", m.get("coding"));
			function.addAttribute("error", m.get("error"));
			function.addAttribute("coltItemCode", m.get("coltItemCode"));
			function.addAttribute("ValueAttribute", m.get("valueAttribute"));
			function.setText(m.get("value"));
		}
		
		return document.asXML();
	}
	
	public static BuildingPower parserHeartBeatXml(byte[] data) {
		SAXReader saxReader = new SAXReader();
		BuildingPower bp = new BuildingPower();
		try {
			Document document = saxReader.read(new ByteArrayInputStream(data));
			Element root = document.getRootElement();
			Element common = root.element("common");
			Element building = common.element("building_id");
			bp.setBuilding(building.getText());
			Element gateway = common.element("gateway_id");
			bp.setGateway(gateway.getText());
			Element type = common.element("type");
			bp.setType(type.getText());
			Element heartBeat = root.element("heart_beat");
			Element time = heartBeat.element("time");
			bp.setTime(time.getText());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return bp;
	}

	public static BuildingPower parserAuthXml(byte[] data) {
		SAXReader saxReader = new SAXReader();
		BuildingPower bp = new BuildingPower();
		try {
			Document document = saxReader.read(new ByteArrayInputStream(data));
			Element root = document.getRootElement();
			Element common = root.element("common");
			Element building = common.element("building_id");
			bp.setBuilding(building.getText());
			Element gateway = common.element("gateway_id");
			bp.setGateway(gateway.getText());
			Element type = common.element("type");
			bp.setType(type.getText());
			Element idValidate = root.element("id_validate");
			Element sequence = idValidate.element("sequence");
			bp.setSequence(sequence.getText());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return bp;
	}
	
	public static BuildingPower parserMd5Xml(byte[] data) {
		SAXReader saxReader = new SAXReader();
		BuildingPower bp = new BuildingPower();
		try {
			Document document = saxReader.read(new ByteArrayInputStream(data));
			Element root = document.getRootElement();
			Element common = root.element("common");
			Element building = common.element("building_id");
			bp.setBuilding(building.getText());
			Element gateway = common.element("gateway_id");
			bp.setGateway(gateway.getText());
			Element type = common.element("type");
			bp.setType(type.getText());
			Element idValidate = root.element("id_validate");
			Element result = idValidate.element("result");
			bp.setResult(result.getText());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return bp;
	}
	
	public static BuildingPower parserPeriodXml(byte[] data) {
		SAXReader saxReader = new SAXReader();
		BuildingPower bp = new BuildingPower();
		try {
			Document document = saxReader.read(new ByteArrayInputStream(data));
			Element root = document.getRootElement();
			Element common = root.element("common");
			Element building = common.element("building_id");
			bp.setBuilding(building.getText());
			Element gateway = common.element("gateway_id");
			bp.setGateway(gateway.getText());
			Element type = common.element("type");
			bp.setType(type.getText());
			Element config = root.element("config");
			Element period = config.element("period");
			bp.setPeriod(period.getText());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return bp;
	}
	
	public static BuildingPower parserContinuousXml(byte[] data) {
		SAXReader saxReader = new SAXReader();
		BuildingPower bp = new BuildingPower();
		try {
			Document document = saxReader.read(new ByteArrayInputStream(data));
			Element root = document.getRootElement();
			Element common = root.element("common");
			Element building = common.element("building_id");
			bp.setBuilding(building.getText());
			Element gateway = common.element("gateway_id");
			bp.setGateway(gateway.getText());
			Element type = common.element("type");
			bp.setType(type.getText());
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return bp;
	}
}
