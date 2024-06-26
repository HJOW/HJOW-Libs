package hjow.common.xml;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLSerializableObject implements XMLSerializable
{
    private static final long serialVersionUID = -4805345756979889703L;

    public Document toXMLDocument()
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            doc.setXmlStandalone(true);

            String className = this.getClass().getName();

            Element root = doc.createElement(this.getClass().getSimpleName());
            root.setAttribute("class", className);
            doc.appendChild(root);

            String classType = "class";
            if (className.equals("byte") || className.equals("char") || className.equals("double") || className.equals("float") || className.equals("int") || className.equals("long") || className.equals("short")
                    || className.equals("boolean"))
                classType = "primitive";

            boolean islist = false;
            boolean ismap = false;

            if (this instanceof List<?>)
            {
                islist = true;
                classType = "list";
            }

            if (this instanceof Map<?, ?>)
            {
                ismap = true;
                classType = "map";
            }

            root.setAttribute("classType", classType);

            if (islist)
            {
                Element child;
                List<?> listObj = (List<?>) this;
                for (Object obj : listObj)
                {
                    child = createXMLChild(doc, obj);
                    if (child != null)
                        root.appendChild(child);
                }
            } else if (ismap)
            {
                Element child;
                Map<?, ?> mapObj = (Map<?, ?>) this;
                Set<?> set = mapObj.keySet();
                for (Object k : set)
                {
                    Object obj = mapObj.get(k);
                    child = createXMLChild(doc, obj);
                    if (child != null)
                        root.appendChild(child);
                }
            } else
            {
                Element child = null;
                Class<?> classes = this.getClass();
                Field[] fields = classes.getFields();
                for (Field f : fields)
                {
                    if (f.isAccessible())
                    {
                        try
                        {
                            Object obj = f.get(this);

                            child = createXMLChild(doc, obj);
                            if (child != null)
                                root.appendChild(child);
                        } catch (IllegalArgumentException e)
                        {
                        }
                    }
                }
            }

            return doc;
        } catch (ParserConfigurationException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IllegalArgumentException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        } catch (IllegalAccessException e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static Element createXMLChild(Document doc, Object obj)
    {
        Element child = null;
        if (obj instanceof XMLSerializable)
        {
            Document docObj = ((XMLSerializable) obj).toXMLDocument();
            child = docObj.getDocumentElement();
        } else
        {
            if ((obj instanceof Integer) || (obj instanceof Long) || (obj instanceof Byte) || (obj instanceof Float) || (obj instanceof Double) || (obj instanceof Boolean) || (obj instanceof Character))
            {
                String primitiveName = "";
                if (obj instanceof Byte)
                    primitiveName = "byte";
                if (obj instanceof Integer)
                    primitiveName = "int";
                if (obj instanceof Long)
                    primitiveName = "long";
                if (obj instanceof Float)
                    primitiveName = "float";
                if (obj instanceof Double)
                    primitiveName = "double";
                if (obj instanceof Boolean)
                    primitiveName = "boolean";
                if (obj instanceof Character)
                    primitiveName = "char";

                child = doc.createElement(primitiveName);
                child.setAttribute("class", primitiveName);
                child.setAttribute("classType", "primitive");
                child.setNodeValue(String.valueOf(obj));
            }
        }
        return child;
    }
}
