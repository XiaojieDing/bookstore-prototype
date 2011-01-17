package test.integration.jaxb.bookstore;

import java.io.IOException;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

public class SystemOutSchemaOutput extends SchemaOutputResolver {

	@Override
	public Result createOutput(String namespaceUri, String suggestedFilename) throws IOException {
		StreamResult res = new StreamResult(System.out);
		res.setSystemId(suggestedFilename);
		return res;
	}
}
