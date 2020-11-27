package usercss;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.batik.util.XMLResourceDescriptor;
import org.w3c.dom.svg.SVGDocument;

public class ParserTest {

	public static void main(String[] args) throws Exception {

		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
		SVGDocument svg = (SVGDocument)f.createDocument(ParserTest.class.getResource("/test.svg").toString(), ParserTest.class.getResourceAsStream("/test.svg"));
		String css = ParserTest.class.getResource("/styles.css").toString();
		initializeEngine(svg, args.length > 0 ? css : null);
		TranscoderInput transcoderInput = new TranscoderInput(svg);
		Transcoder transcoder = new PNGTranscoder();
		transcoder.addTranscodingHint(PNGTranscoder.KEY_USER_STYLESHEET_URI, css);
		TranscoderOutput output = new TranscoderOutput(new FileOutputStream(new File("/tmp/test-css.png")));
		transcoder.transcode(transcoderInput, output);
	}

	private static BridgeContext initializeEngine(SVGDocument svg, String css) {

		UserAgentAdapter userAgent = new UserAgentAdapter() {

			@Override
			public String getUserStyleSheetURI() {

				return css;
			}
		};
		DocumentLoader loader = new DocumentLoader(userAgent);
		BridgeContext ctx = new BridgeContext(userAgent, loader);
		ctx.setDynamicState(BridgeContext.DYNAMIC);
		GVTBuilder builder = new GVTBuilder();
		builder.build(ctx, svg);
		return ctx;
	}
}
