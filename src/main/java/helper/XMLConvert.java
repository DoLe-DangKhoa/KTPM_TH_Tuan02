/*
 * Đỗ Lê Đăng Khoa - 18053791
 */
package helper;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class XMLConvert<T> {
	private T type;
	public XMLConvert(T type) {
	this.type = type;
	}
	@SuppressWarnings("all")
	public T xml2Object(String xml) throws Exception{
	T sv=null;
	JAXBContext ctx= JAXBContext.newInstance(type.getClass());
	Unmarshaller ms = ctx.createUnmarshaller();
	sv=(T) ms.unmarshal(new StringReader(xml));
	return sv;
	}
	public String object2XML(T obj)throws Exception{
	JAXBContext ctx= JAXBContext.newInstance(type.getClass());
	Marshaller ms=ctx.createMarshaller();
	StringWriter sw = new StringWriter();
	ms.marshal(obj, sw);
	return sw.toString();
	}


}
