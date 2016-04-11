package de.tobias.utils.help.elements;

import java.util.List;
import java.util.UUID;

import com.hp.gagawa.java.Document;
import com.hp.gagawa.java.Node;
import com.hp.gagawa.java.elements.Div;
import com.hp.gagawa.java.elements.H1;
import com.hp.gagawa.java.elements.U;

import de.tobias.utils.help.HelpContent;
import de.tobias.utils.help.HelpElement;
import de.tobias.utils.help.HelpMap;
import de.tobias.utils.help.ui.HelpMapViewController2;

/**
 * 
 * @author tobias
 *
 */
public class HelpTopic extends HelpElement {

	/**
	 * 
	 */
	private List<String> tags;

	/**
	 * 
	 */
	private String headline;
	/**
	 * 
	 */
	private List<HelpContent> contents;

	public HelpTopic(String name, List<String> tags, List<HelpContent> contents, UUID uuid, HelpMap helpMap) {
		super(name, uuid, helpMap);
		this.tags = tags;
		this.contents = contents;
	}

	public List<String> getTags() {
		return tags;
	}

	public List<HelpContent> getContents() {
		return contents;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
	public Document getHtmlDocument(HelpMapViewController2 hmvc, Document htmlDocument) {
		// Überschrift
		H1 headline = new H1();
		headline.appendText(getHeadline());
		htmlDocument.body.appendChild(new U().appendChild(headline));

		getContents().forEach(contentItem -> {
			Node header = contentItem.getHeader();
			if (header != null) {
				htmlDocument.head.appendChild(header);
			}

			Node node = contentItem.getHTMLNode();
			if (node != null) {
				Div div = new Div().appendChild(node);
				div.setCSSClass("content");
				htmlDocument.body.appendChild(div);
			}
		});
		return htmlDocument;
	}

	@Override
	public String toString() {
		return name;
	}
}
